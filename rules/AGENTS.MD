# Antigravity Developer Guide (Hibiki)

This document provides instructions and guidelines for developers (and AI agents) working on the Hibiki project.

## Module Configuration: API/Implementation Split

Hibiki follows a strict separation of concerns by splitting features into two distinct modules: an `api` module for definitions and an `impl` module for implementations.

### 1. Project Organization

Hibiki is organized into several top-level directories under `java/`:

*   **`features/`**: Domain-specific logic, following a strict `api`/`impl` split.
*   **`core/`**: Critical system components. Some (like `identity`) use the `api`/`impl` split, while others (like `orchestrator` or `shared`) are single modules.
*   **`packages/`**: Utility and integration libraries (e.g., `jwt`, `otp`). These are single modules and are NOT segregated into `api`/`impl`.
*   **`presentation/`**: Entry points like `rest-api`.

### 2. The API/Implementation Split Pattern

For modules following this pattern (mostly under `features/` and some under `core/`):

*   **`<module-name>-api`**: Contains all interfaces, models, DTOs, ports, and domain event definitions. **CRITICAL**: This module MUST be self-contained and NOT depend on any other feature or core module API (except `core:shared`).
*   **`<module-name>-impl`**: Contains concrete implementations, infrastructure details, and persistence logic. It MAY depend on other modules' `-api` to access system-wide infrastructure (e.g., `CommandBus`, `EventBus`).

### 3. Package Conventions (`-api` module)

The following packages are standard in the `-api` module:

*   `dto`: Request/Response objects, Commands, and Queries.
*   `event`: Domain event definitions emitted by the module.
*   `model`: Domain models and Value Objects.
*   `port`: Output port interfaces (e.g., repositories).
*   `service`: Input port interfaces (e.g., command/query handlers).

### 4. Package Conventions (`-impl` module)

The following packages are standard in the `-impl` module:

*   `infrastructure/handler`: Implementations of the interfaces defined in `service`.
*   `infrastructure/persistence`: Persistence-specific logic.
    *   `entity`: Database @Node/@Table entities.
    *   `mapper`: Mappers between domain models and entities (MapStruct).
    *   `repository`: Spring Data repository interfaces.

### 5. Coding Patterns

#### 4.1 Value Objects vs. Primitive Types
*   **Domain Models:** Identifiers in domain models MUST NOT be raw `UUID`s or `String`s. Use dedicated Value Objects (e.g., `UserId`, `DeviceId`) with Lombok's `@Value` stack.
*   **DTOs (Commands, Queries, Responses):** DTOs MUST use primitive types (e.g., `UUID`, `String`, `Instant`) for all fields, including identifiers. DTOs are the system's contract and should be as simple and serialization-friendly as possible, without tying external callers to internal domain Value Objects. The mapping from primitive types to Value Objects must happen within the Handlers (implementation layer).

```java
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Builder(toBuilder = true)
public class DeviceId {
    UUID value;
}
```

#### 4.2 Lombok Usage for DTOs and Models
Standardize on the following annotations for classes in the `-api` module:
*   **Attribute Standards**: All attributes must be `private final`.
*   **Lombok Annotations for Domain Models**:
    *   `@Getter`, `@Value` or `@Data` (as appropriate, favoring immutability).
    *   `@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)`: To ensure all attributes are private and final by default.
    *   `@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)`: To provide an empty constructor (required by frameworks), using `force = true` for final fields.
    *   `@AllArgsConstructor`: To provide a constructor with all arguments.
    *   `@Builder(toBuilder = true)`: For object building.
    *   `@With`: For modified copies.
*   Include comprehensive Javadocs for all public classes and fields.

#### 4.3 No Nested Classes
Nested classes are generally forbidden to maintain a clear and flat class structure.
*   **Exception**: Nested classes ARE allowed in the `presentation/rest-api` layer (e.g., for simple internal DTOs or request/response structures).

## Dependency Management

When editing `build.gradle` files:

*   **Exact Dependencies**: Always specify exact dependencies instead of relying on transitive dependencies brought by starters.
*   **No Starters**: Avoid using "starter" dependencies (e.g., `spring-boot-starter-web`) as a shortcut. Specify the individual libraries needed to avoid bringing in unnecessary or conflicting transitive dependencies.
