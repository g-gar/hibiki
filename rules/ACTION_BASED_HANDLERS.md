# Action-Based Nested Records for Handlers

This document defines the standard for refactoring CQRS Command and Query handlers to use action-based nested records. This pattern improves code clarity, reduces boilerplate, and eliminates redundant DTOs.

## 1. Interface Structure

Handlers should define their primary message (Command or Query) and their associated outcome (DomainEvent) as nested records within the handler interface.

### Naming Conventions

- **Commands/Queries**: Use the action name itself.
  - `Create`, `Update`, `Delete`, `Find`, `Search`, `Validate`, `Refresh`.
- **Events**: Use the past-tense of the action.
  - `Created`, `Updated`, `Deleted`, `Validated`, `Refreshed`.

### Example Interface

```java
public interface CreateUserCommandHandler extends CommandHandler<CreateUserCommandHandler.Create, User> {

    /**
     * Data needed to create a user.
     */
    record Create(String username, String email) implements Command<User> {}

    /**
     * Event published when a user is successfully created.
     */
    record Created(UUID userId, String username) implements DomainEvent {}
}
```

## 2. Eliminate Redundant DTOs

One of the primary goals of this refactor is to eliminate specialized DTO packages (`dto`, `event`) at the API level if they only serve as envelopes for handler inputs/outputs.

- Use **Domain Models** (`com.ggar.hibiki.core.*.model.*`) as return types or complex parameters whenever possible.
- Use **Primitive Types** within the nested records for simple inputs.
- If a record is shared across multiple handlers, move it to a `shared` package within the API, but prefer nesting if it belongs strictly to one action.

## 3. Implementation Guidelines

- Implementations should be located in the `-impl` module under the `handler` package.
- Implementation names should follow the pattern: `[Action][Entity]CommandHandlerImpl`.
- Use the `Interface.Record` format when referencing records externally (e.g., in `CreateCatalogItemsCommandHandler`).

### Example Implementation

```java
@Component
public class CreateUserCommandHandlerImpl implements CreateUserCommandHandler {

    @Override
    public Mono<User> handle(CreateUserCommandHandler.Create command) {
        // Implementation logic
        // Publish CreateUserCommandHandler.Created event
    }
}
```

## 4. Import & Style Rules

- **Avoid Fully Qualified Names (FQNs)** in the code. Use standard imports for `Command`, `Query`, `CommandHandler`, `QueryHandler`, and `DomainEvent`.
- **Visibility**: Always use `Interface.Record` for clarity even within the same module if it helps distinguish between different local actions.
- **Javadocs**: Every nested record and implementation method must have descriptive Javadocs.

## 5. Downstream & Testing Updates

Whenever a handler is refactored:
- Update all **Contract Tests** (`testing:contracts`) to reflect the new record types.
- Update **Mocked and Real Integration Tests** to use the `new Interface.Create(...)` syntax.
- Update any **Event Handlers** in other modules (e.g., `features:*`) that listen to the refactored events.
- Update **Fixtures** to ensure they return Domain Models instead of old DTOs.
