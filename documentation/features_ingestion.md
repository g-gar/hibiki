# features:ingestion — Design Decisions & Patterns

> Documented: 2026-03-09
> Module: `features/ingestion` (`ingestion-api` + `ingestion-impl`)

---

## 1. Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                    REST Controller                       │
│              (presentation:rest-api)                     │
└──────────────────────┬──────────────────────────────────┘
                       │ Commands / Queries
                       ▼
┌─────────────────────────────────────────────────────────┐
│                   core:orchestrator                      │
│              (use case composition)                      │
└──────────────────────┬──────────────────────────────────┘
                       │ Mediator.send()
                       ▼
┌─────────────────────────────────────────────────────────┐
│                   ingestion-api                          │
│  Commands, Queries, Models, Ports, Events, Pipeline SPI │
└──────────────────────┬──────────────────────────────────┘
                       │ implements
                       ▼
┌─────────────────────────────────────────────────────────┐
│                   ingestion-impl                         │
│  Handlers, Pipeline, S3 Adapter, Tika, SHA-256, Neo4j   │
└─────────┬───────────┬───────────┬───────────────────────┘
          │           │           │
          ▼           ▼           ▼
       S3 (any)    Neo4j      EventBus
```

### Hexagonal Architecture

- **Ports** (in `ingestion-api`): `MediaStorage`, `MediaTypeResolver`, `ContentHasher`, `UploadSessionRepository`, `MediaRepository`
- **Adapters** (in `ingestion-impl`): `S3MediaStorage`, `TikaMediaTypeResolver`, `Sha256ContentHasher`, persistence adapters
- Ports are interfaces in the API module — adapters are swappable implementations in the impl module

---

## 2. Chunked Upload Protocol

**Why:** Supports pause/resume/cancel, progress tracking, and enables in-flight processing (MIME detection, progressive hashing).

### Flow

```
Client                          Backend                         S3
  │                                │                             │
  │─── InitiateUploadCommand ─────▶│                             │
  │                                │── createMultipartUpload ───▶│
  │◀── UploadSession (INITIATED) ──│                             │
  │                                │                             │
  │─── UploadChunkCommand [0] ────▶│                             │
  │                                │── detect MIME (first chunk) │
  │                                │── update hash              │
  │                                │── uploadPart ──────────────▶│
  │◀── UploadProgress (33%) ───────│                             │
  │                                │── publish ChunkUploadedEvent│
  │                                │                             │
  │─── UploadChunkCommand [1] ────▶│  ...repeat...               │
  │─── UploadChunkCommand [2] ────▶│                             │
  │                                │                             │
  │─── CompleteUploadCommand ─────▶│                             │
  │                                │── completeMultipartUpload ─▶│
  │                                │── persist Media entity      │
  │                                │── run IngestionPipeline     │
  │                                │── publish UploadCompletedEvent
  │                                │── publish MediaIngestedEvent│
  │◀── UploadSession (COMPLETED) ──│                             │
```

### Cancellation

```
Client                          Backend                         S3
  │─── CancelUploadCommand ──────▶│                             │
  │                                │── abortMultipartUpload ────▶│
  │                                │── publish UploadCancelledEvent
  │◀── UploadSession (CANCELLED) ─│                             │
```

---

## 3. Why Reactive Streams (Not WebSockets or Polling)

Progress reporting uses **Server-Sent Events (SSE)** backed by `Flux<UploadProgress>`. Three approaches were evaluated:

| Approach | Pros | Cons |
|---|---|---|
| **Polling** | Simple | Wasteful, high latency, N requests per upload |
| **WebSockets** | Bidirectional, real-time | Overkill (we only push), stateful connection, harder to load-balance, not cacheable |
| **SSE / Reactive Streams** ✅ | Unidirectional push, native HTTP, auto-reconnect, backpressure | One-way only (sufficient for progress) |

**Why SSE wins for ingestion:**

- **Unidirectional** — progress flows server→client only, no need for bidirectional
- **Native HTTP** — works through proxies, CDNs, and load balancers without special config
- **Backpressure** — Reactor's `Flux` naturally applies backpressure; if the client can't consume progress events fast enough, the server doesn't overwhelm it
- **Auto-reconnect** — SSE spec includes automatic reconnection with `Last-Event-ID`
- **Transport-agnostic commands** — the same `UploadChunkCommand` works whether the client uses REST+SSE, gRPC streaming, or WebSockets. The transport layer is decoupled from the domain

WebSockets remain an option for future features that need bidirectional communication (e.g., collaborative playlists), but for ingestion progress, SSE is the right tool.

---

## 4. CQRS + EventBus: Scalable Processing Without Burning the Server

This is arguably the most powerful architectural decision in the module. The combination of CQRS (Command Query Responsibility Segregation) with a reactive EventBus enables processing patterns that would be extremely difficult with a traditional synchronous server.

### The Core Insight

A single `UploadChunkCommand` triggers a **cascade of asynchronous, decoupled behaviors**:

```
UploadChunkCommand
  │
  ├─▶ Upload chunk to S3
  ├─▶ Compute progressive hash
  ├─▶ Update session state in Neo4j
  │
  └─▶ publish(ChunkUploadedEvent)
        │
        ├─▶ features:deduplication ──▶ compare hash ──▶ if duplicate ──▶ CancelUploadCommand
        ├─▶ features:quotas ──▶ track storage usage
        └─▶ (any future subscriber)
```

**What makes this powerful:**

1. **A command can trigger events that generate new commands** — `ChunkUploadedEvent` can cause `features:deduplication` to emit a `CancelUploadCommand`, effectively killing an in-progress upload mid-flight. With a traditional synchronous server, you'd need to poll state or use shared mutable state.

2. **Non-blocking at every level** — Reactor's event loop handles thousands of concurrent uploads on a handful of threads. No thread-per-request model. No thread pool exhaustion when 500 users upload simultaneously.

3. **Decoupled scaling** — each event consumer processes independently. If deduplication is slow, it doesn't block the upload. If quotas are down, uploads still succeed (fault-tolerant publish).

4. **Zero-cost extensibility** — adding a new behavior (e.g., virus scanning) means adding a new `@Component` that subscribes to `ChunkUploadedEvent`. Zero changes to the upload handler.

### Traditional Server vs CQRS+EventBus

```
Traditional:                          CQRS + EventBus:

uploadFile() {                        UploadChunkCommandHandler {
  validate();                           upload to S3
  upload();                             update state
  checkDuplicates();  ◄── blocking      publish(ChunkUploadedEvent) ◄── async
  updateQuotas();     ◄── blocking    }
  scanVirus();        ◄── blocking
  extractMetadata();  ◄── blocking    DeduplicationListener {
  notifyUser();       ◄── blocking      handle(ChunkUploadedEvent)  ◄── independent
}                                     }

One method does everything.            QuotasListener {
If virus scan is slow,                  handle(ChunkUploadedEvent)  ◄── independent
  everything waits.                   }
If quotas service is down,
  upload fails entirely.              Each listener is autonomous.
```

### Batch Upload Scenario (1000 files)

With CQRS+EventBus, uploading 1000 files means:
- 1000 `InitiateUploadCommand` → non-blocking, backpressured by `Flux`
- N×1000 `UploadChunkCommand` → each processes independently on the event loop
- Events are published asynchronously → dedup, quotas, and catalog update concurrently
- The server uses ~4 threads for all of this (Reactor's default event loop)

A traditional blocking server would need 1000+ threads and would likely OOM or hit thread pool limits.

---

## 5. Dual Pipeline Architecture: In-Flight vs Post-Upload

There are two distinct processing windows, each with different constraints and purposes.

### In-Flight Processing (during upload, per-chunk)

Happens **inside `UploadChunkCommandHandler`**, synchronously with each chunk. Not a formal pipeline — it's inline processing that rides the upload stream.

| Processing | When | How | Why Inline |
|---|---|---|---|
| **MIME detection** (Tika) | First chunk only | `MediaTypeResolver.resolve(bytes, filename)` | Must know MIME before S3 upload to set `Content-Type` |
| **Progressive hashing** | Every chunk | `ContentHasher.update(state, bytes)` | Enables early dedup via `ChunkUploadedEvent.accumulatedHash` |

These are **not pipeline stages** — they're integral to the upload handler because they need access to the raw bytes as they flow through.

**Key detail:** The `accumulatedHash` emitted in `ChunkUploadedEvent` is consumed **immediately** by `features:deduplication`. If dedup detects a match after chunk 3 of a 100-chunk file, it can fire a `CancelUploadCommand` to abort the remaining 97 chunks. This saves bandwidth and storage — you don't upload 100MB only to delete it in post-processing.

```
Chunk 0 ──▶ MIME detect + hash ──▶ ChunkUploadedEvent(hash="a1b2...")
                                          │
Chunk 1 ──▶ hash ──▶ ChunkUploadedEvent(hash="c3d4...")
                            │
                            └──▶ features:dedup compares hash
                                          │
Chunk 2 ──▶ hash ──▶ ChunkUploadedEvent(hash="e5f6...")
                            │
                            └──▶ MATCH FOUND ──▶ CancelUploadCommand
                                                      │
                                                      ▼
                                              Abort S3 + mark CANCELLED
                                              (saved 97 chunks of bandwidth)
```

### Post-Upload Pipeline (after completion, per-file)

Runs **after `CompleteUploadCommand`** assembles the file in S3. This is the formal `IngestionPipeline` with `IngestionStage` SPI.

```java
public interface IngestionStage {
    int order();
    boolean supports(IngestionContext context);
    Mono<IngestionContext> process(IngestionContext context);
}
```

| Stage | Order | Purpose |
|---|---|---|
| `MetadataExtractionStage` | 200 | Duration, bitrate, codec, sample rate |
| `ThumbnailGenerationStage` | 300 | Waveform image or album art |
| *(future stages)* | 400+ | Transcoding, normalization, etc. |

- Stages are Spring `@Component` beans, auto-discovered and ordered
- `IngestionContext` is immutable (`@With`) with an extensible `attributes` map
- Runs **before** `MediaIngestedEvent` is published — downstream modules receive a fully processed media

### Why Two Pipelines?

| | In-Flight | Post-Upload |
|---|---|---|
| **When** | During upload, per chunk | After upload, per file |
| **Access to** | Raw bytes as they stream | Complete file in S3 |
| **Purpose** | Early detection, optimization | Rich processing, enrichment |
| **Failure impact** | Can cancel upload early | Marks media as FAILED |
| **Formal SPI** | No (inline) | Yes (`IngestionStage`) |

---

## 6. Key Design Decisions

### 6.1 Immutable Domain Models (`@Value`)

All models use Lombok `@Value` + `@Builder` — fully immutable. State changes produce new instances via `@With`.

**Rationale:** Thread safety in reactive pipelines. No accidental mutation across async boundaries.

### 6.2 MIME Detection on First Chunk (Inline)

MIME type is detected using Tika on the **first chunk only**, inline within `UploadChunkCommandHandler`. Not a pipeline stage.

**Rationale:** Needs to happen before any S3 upload to set `Content-Type`. A pipeline stage would be too late.

### 6.3 Progressive Hashing

Each chunk is fed into `ContentHasher` incrementally. The accumulated hash is stored on `UploadItem` and emitted in `ChunkUploadedEvent`.

**Rationale:** Enables early duplicate detection by a future `features:deduplication` module without waiting for the full file to upload.

### 6.4 `ContentHasher` as a Port

Hashing is abstracted behind a port with an opaque `HashState`. The current implementation uses SHA-256 via `MessageDigest`.

**Rationale:** Algorithm is swappable (e.g., BLAKE3 for performance). Could also be moved to a `features:security` module later.

### 6.5 S3 Storage without File Extensions

Files are stored in S3 with UUID keys (no extensions). `Content-Type` is set via S3 headers. Metadata (codec, extension, etc.) is stored in Neo4j.

**Rationale:** Decouples storage key from file metadata. Avoids filename collisions and extension guessing.

### 6.6 Vendor-Agnostic S3 (AWS SDK v2)

Uses the AWS S3 SDK v2 instead of MinIO client. Works with **any** S3-compatible server (SeaweedFS, Garage, MinIO, Ceph).

**Rationale:** MinIO has licensing/community concerns (AGPL, Aistor transition). The standard S3 API is the universal contract — only `storage.s3.endpoint` changes between servers.

Configuration: `S3Config` with `forcePathStyle(true)` for non-AWS compatibility.

### 6.7 Reactive Event Publishing (Fault-Tolerant)

Events are **chained into the reactive pipeline** via `.flatMap()` + `.onErrorResume()`:

```java
.flatMap(s -> eventBus.publish(event)
    .onErrorResume(e -> {
        log.error("Failed to publish event", e);
        return Mono.empty();
    })
    .thenReturn(s))
```

**Rationale:** Three options were evaluated:
1. ❌ Fire-and-forget (`doOnSuccess` + ignore `Mono`) — event may never execute
2. ❌ Strict chain (`.flatMap` without error handling) — event failure breaks the operation
3. ✅ **Fault-tolerant chain** — event is properly subscribed, but failures don't break the core operation

### 6.8 `UploadSession` is a Domain Entity, Not an HTTP Session

Despite the name, `UploadSession` is a first-class domain aggregate persisted in Neo4j. It tracks multi-item uploads across multiple HTTP requests.

**Rationale:** REST is stateless — each request is independent. The session state lives in the database, not in `HttpSession`.

### 6.9 Mediator Returns `Publisher<R>`

Changed `CommandHandler` and `QueryHandler` to return `Publisher<R>` instead of `Mono<R>`.

**Rationale:** Allows handlers to return `Flux<R>` natively (e.g., streaming playback history). Eliminates the `Mono<Flux<>>` antipattern. Consumers use `Mono.from()` or `Flux.from()` as needed.

---

_(Pipeline SPI documented in section 5 above)_

---

## 7. Domain Events

| Event | Emitted By | Consumers |
|---|---|---|
| `ChunkUploadedEvent` | `UploadChunkCommandHandler` | Quotas, deduplication |
| `UploadCompletedEvent` | `CompleteUploadCommandHandler` | Quotas |
| `UploadCancelledEvent` | `CancelUploadCommandHandler` | Quotas (rollback) |
| `MediaIngestedEvent` | `CompleteUploadCommandHandler` | Orchestrator, library, catalog, dedup |

`MediaIngestedEvent` is the **terminal event** — it signals that a media file is fully uploaded, processed, and ready for downstream modules.

---

## 8. Lifecycle State Machine

```
IngestionPhase:
  INITIATED ──▶ UPLOADING ──▶ UPLOADED ──▶ PROCESSING ──▶ COMPLETED
       │              │                                        │
       └──────────────┴───── CANCELLED                     FAILED
```

```
MediaStatus:
  PENDING ──▶ PROCESSING ──▶ READY
                    │
                  FAILED
```

---

## 9. Dependencies

| Dependency | Purpose |
|---|---|
| `core:shared` | `Mediator`, `EventBus`, `DomainEvent` |
| `packages:uuid` | `UuidV7Generator` (time-ordered IDs) |
| AWS S3 SDK v2 | Object storage (multipart uploads) |
| Apache Tika | MIME type detection from magic bytes |
| MapStruct | Entity ↔ Domain model mapping |
| Spring Data Neo4j | Graph persistence |
| Spring WebFlux | `DataBuffer` for reactive byte streams |

---

## 10. Future Considerations

- **Deduplication module** (`features:deduplication`) — consumes `ChunkUploadedEvent` for early detection via accumulated hash, and `MediaIngestedEvent` for final content hash verification
- **Outbox Pattern** — if event delivery guarantee becomes critical, persist events in the same transaction as domain state and publish asynchronously
- **Project Loom (Java 21)** — would simplify S3/Neo4j adapters by allowing blocking calls on virtual threads, but the reactive domain API (`Publisher`/`Mono`/`Flux`) would remain unchanged
- **gRPC transport** — commands are transport-agnostic; a gRPC adapter would work alongside REST
