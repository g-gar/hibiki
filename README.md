# Hibiki

Hibiki is a high-performance, reactive media streaming and management engine designed to operate as a self-hosted, sovereign infrastructure platform. It provides lossless audio delivery, deterministic metadata synchronization, and extensible domain capabilities.

This repository prioritizes architectural resilience, strict domain isolation, and horizontal scalability over monolithic coupling.

## System Architecture

The project implements a **Modular Monolith** pattern as a transitional state toward a distributed microservices topology. This allows for unified deployments while enforcing strict physical boundaries between bounded contexts.

* **`core/`**: The central domain model. Encapsulates pure business rules for identity management, catalog operations, and distributed orchestration.
* **`features/`**: Domain slices containing specific, isolated capabilities (e.g., semantic library indexing, metadata resolution, concurrent ingestion pipelines).
* **`presentation/`**: The ingress layer handling external client traffic, routing, and REST API exposure.
* **`packages/`**: Shared infrastructure libraries and cross-domain utilities.
* **`testing/`**: A consolidated verification suite enforcing system invariants across all architectural layers.

## Core Engineering Principles

To ensure system stability under highly concurrent loads and maintain development velocity as the codebase scales, Hibiki adheres to the following paradigms:

### 1. Command Query Responsibility Segregation (CQRS)
We strictly decouple state-mutating operations from read models. 
* **Command Pipelines** handle rich domain logic, strict validation, and state transitions.
* **Query Projections** are heavily optimized and indexed for low-latency data retrieval.
This segregation ensures we can scale the read and write paths asymmetrically based on load profiles.

### 2. Event-Driven Architecture
Bounded contexts communicate exclusively via an asynchronous **EventBus**. 
Cross-domain side effects (e.g., cascading deletions across indices) are triggered via domain events. This fire-and-forget publish/subscribe mechanism prevents systemic unresponsiveness and ensures eventual consistency without tight coupling.

### 3. Non-Blocking I/O & Reactive Streams
The core engine is built on Project Reactor and Spring WebFlux. All data flows, from HTTP ingress to the persistence layer, utilize non-blocking, asynchronous reactive streams. This architecture is critical for maximizing thread utility and handling high-volume concurrent streaming or ingestion spikes.

### 4. Evolutionary Infrastructure & Observability
While designed for unified deployment initially, the strict enforcement of API and event boundaries guarantees seamless migration to dedicated **Microservices** per domain.
To support this distributed nature, the system requires observability as a primitive. Distributed tracing, structured logging, and metric aggregation are built into the foundation, providing deep operational visibility across cluster boundaries.

## Technical Roadmap

Hibiki aims to define the state-of-the-art for sovereign audio infrastructure. Current engineering focus areas include:

* **High-Throughput Ingestion**: Implementing content-aware deduplication via SHA-256 fingerprinting, batch processing queues, and native web-to-library extraction pipelines.
* **Lossless Streaming Engine**: Guaranteeing bit-perfect output layers, continuous gapless packet delivery, and dynamic payload transcoding for constrained bandwidth environments.
* **Deterministic Metadata Resolution**: Integrating acoustic fingerprinting (AcoustID) and strict conflict resolution against external authorities (MusicBrainz).
* **Federated Ecosystem**: Exposing ActivityPub endpoints for cross-instance social graphing and maintaining Subsonic protocol compatibility.
* **Extensible Runtime (Plugins)**: Developing a high-performance plugin API to support complex overlays such as WebAudio mixing interfaces, dynamic multi-track layering, and VST signal processing.

## Verification Strategy

We enforce a rigorous, multi-layered testing topology to validate the system architecture:

* **Mocked Integration (`integration:mocked`)**: Deterministic verification of domain logic, command handlers, and reactive execution chains using isolated mocks.
* **Infrastructure Integration (`integration:real`)**: Ephemeral environment testing using Testcontainers to validate interactions against actual datastores, message brokers, and graph databases.
* **Contract Definitions (`contracts`)**: Scenario-driven consumer-driven contracts to lock down API schemas and boundary definitions.
* **End-to-End (`e2e`)**: High-level synthetic transaction flows verifying the entire path from ingress controller to persistence.

---

*For comprehensive architectural decision records (ADRs) and detailed domain documentation, refer to the [`documentation/`](./documentation) directory.*
