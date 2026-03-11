# Hibiki Testing Strategy

This document defines the architectural layers of testing for Hibiki, ensuring that the "Mansion's Foundation" is verified at every level.

## 1. Unit Testing (The Bricks)
- **Scope**: Individual classes, Value Objects, and Domain Logic.
- **Goal**: Fast verification of logic isolated from dependencies.
- **Tooling**: JUnit 5, Mockito.
- **Focus**: `domain` and `shared` modules. Verify that complex logic (like metadata parsing or jam layer selection) works in isolation.

## 2. Integration Testing (The Plumbing)
- **Scope**: Interaction between multiple components within a module or across limited boundaries.
- **Goal**: Ensure the "Glue" (Mediator, EventBus) correctly routes commands and events.
- **Tooling**: Spring Boot Test, Testcontainers (for Databases/S3).
- **Focus**: `*-impl` and `orchestrator` modules.
- **Key Test**: `CompleteUploadCommandHandler` -> `IngestionPipeline` -> `MediaRepository` (Mocking external storage).

## 3. Contract Testing (The Blueprints)
- **Scope**: API boundaries between modules and external services.
- **Goal**: Ensure that if the `ingestion-api` changes, dependent modules (like `rest-api` or `orchestrator`) don't break.
- **Tooling**: Spring Cloud Contract or Pact.
- **Focus**: `*-api` modules and integration points with 3rd party services (MusicBrainz, Spotify).

## 4. End-to-End (E2E) Testing (The Inhabitation)
- **Scope**: Full system flow from entry point (REST API) to persistence.
- **Goal**: Validate that the "First Light" (Upload -> Library -> Play) works for a user.
- **Tooling**: WebTestClient, Playwright (for Frontend), Testcontainers (Full Stack).
- **Focus**: `presentation:rest-api` and cross-module orchestration.

## 5. Stress & Performance Testing (The Load Bearing)
- **Scope**: High-volume ingestion and concurrent playback.
- **Goal**: Ensure the Reactive (WebFlux) foundation can handle the projected 50k+ LOC load.
- **Tooling**: Gatling or JMeter.

## Verification Matrix
| Feature | Unit | Integration | Contract | E2E |
| :--- | :---: | :---: | :---: | :---: |
| Ingestion (Upload) | X | X | X | X |
| Dynamic Jam Layers | X | X | | |
| Metadata Sync | X | X | X | |
| User Follows | X | X | | X |
| Playback History | X | X | | |
