# Hibiki — Semantic Scenario-Centered Testing (Handoff Design)

> **Objetivo**: Este documento define la arquitectura de testing "Scenario-Centered" para Hibiki. Sirve como **guía de implementación definitiva** para generar los contratos (definiciones abstractas) y sus implementaciones (Unit/Integration).
>
> **Nota para el Implementador (Gemini Flash)**: Sigue estrictamente este patrón para cada contrato. No inventes estructuras nuevas; usa siempre `@Nested` y `ScenarioResult`.

---

## 1. El Objeto `ScenarioResult`

Debe existir en `com.ggar.hibiki.test.support` para capturar el estado tras un escenario. Permite aserciones agnósticas a la técnica (**Mocked Integration** vs **Real Integration**).

```java
@Value @Builder
public class ScenarioResult<T> {
    T returnValue;        // Resultado del handler
    Throwable error;     // Excepción capturada
    List<DomainEvent> events; // Eventos del CapturingEventBus
    Map<String, Object> state; // Datos extra (exists, count, etc.)
}
```

---

## 2. Definición de Contratos por Módulo

### 2.1 Módulo `core:catalog`

#### `DeleteArtistContractTest` (Casos Críticos)
Este handler debe gestionar la integridad referencial en Neo4j.
- **Scenario: Sole Owner**:
  - **Contexto**: Artista con álbumes donde es el único dueño.
  - **Acción**: Borrar artista.
  - **Verifica**:
    - El artista, sus álbumes y sus canciones se borran físicamente (Cascada).
    - Se publica el evento `ArtistDeletedEvent` con los datos correctos.
- **Scenario: Collaborator (Featuring)**:
  - **Contexto**: Artista A es colaborador en una canción de un álbum del Artista B.
  - **Acción**: Borrar artista A.
  - **Verifica**:
    - Artista A borrado, pero el álbum y la canción se PRESERVAN porque el Artista B sigue existiendo. Solo se elimina la relación de autoría del Artista A.
    - Se publica el evento `ArtistDeletedEvent`.
- **Scenario: Idempotency (Already Deleted)**:
  - **Acción**: Borrar un artista que ya no existe.
  - **Verifica**: No se lanza excepción; éxito silencioso.

#### `CreateArtistContractTest`
- **Scenario: Success**:
  - **Acción**: Crear un artista nuevo.
  - **Verifica**:
    - Se devuelve el `ArtistDto` correcto.
    - El artista se persiste en la base de datos.
    - Se publica el evento `ArtistCreatedEvent`.
- **Scenario: Idempotency**: Si se intenta crear un artista con un nombre que ya existe (ignoring case), debe devolver el `ArtistDto` existente sin crear duplicados en la base de datos.

#### `UpdateArtistContractTest`
- **Scenario: Success**:
  - **Acción**: Actualizar nombre o ISNI de un artista.
  - **Verifica**:
    - El artista se actualiza en la base de datos.
    - Se devuelve el `ArtistDto` actualizado.
    - Se publica el evento `ArtistUpdatedEvent`.

#### `CreateAlbumContractTest`
- **Scenario: Success**:
  - **Contexto**: El artista ya existe.
  - **Acción**: Crear un álbum para ese artista.
  - **Verifica**:
    - Álbum creado y vinculado al artista.
    - Se publica el evento `AlbumCreatedEvent`.
- **Scenario: Success (Auto-create Artist)**:
  - **Contexto**: El artista NO existe.
  - **Acción**: Crear un álbum indicando el nombre de un artista nuevo.
  - **Verifica**:
    - Se crea el artista automáticamente y se publica `ArtistCreatedEvent`.
    - El álbum se crea y se vincula al nuevo artista.
    - Se publica el evento `AlbumCreatedEvent`.
- **Scenario: Idempotency**: Si el álbum ya existe para ese artista, devuelve el existente.

#### `UpdateAlbumContractTest`
- **Scenario: Success (Metadata)**:
  - **Acción**: Actualizar título, año o código de barras.
  - **Verifica**: Datos actualizados y evento `AlbumUpdatedEvent` publicado.
- **Scenario: Success (Tracklist Management)**:
  - **Acción**: Añadir canciones ya existentes en el catálogo al álbum, quitar canciones del álbum o cambiar el orden (trackNumber) de las canciones.
  - **Verifica**:
    - La lista de canciones asociada al álbum en la base de datos refleja los cambios.
    - Se publican los eventos correspondientes (`SongUpdatedEvent` para las canciones cuya relación o orden con el álbum ha cambiado).
    - El evento `AlbumUpdatedEvent` se publica indicando la actualización del tracklist.

#### `DeleteAlbumContractTest`
- **Scenario: Success**:
  - **Acción**: Borrar álbum.
  - **Verifica**:
    - El álbum y sus canciones se borran.
    - El artista se preserva.
    - Se publica el evento `AlbumDeletedEvent`.

#### `CreateSongContractTest`
- **Scenario: Success**:
  - **Contexto**: El álbum ya existe.
  - **Acción**: Crear canción vinculada al álbum.
  - **Verifica**:
    - Canción creada y vinculada correctamente al álbum.
    - Se publica el evento `SongCreatedEvent`.
- **Scenario: Fail (Album missing)**:
  - **Acción**: Intentar crear canción vinculada a un álbum inexistente.
  - **Verifica**: El contrato define que el álbum debe existir previamente (referential integrity).
- **Scenario: Idempotency**: Si el ISRC ya existe, devuelve la canción existente.

#### `UpdateSongContractTest`
- **Scenario: Success**:
  - **Acción**: Actualizar metadatos de la canción.
  - **Verifica**: Datos actualizados y evento `SongUpdatedEvent` publicado.

#### `DeleteSongContractTest`
- **Scenario: Success**:
  - **Acción**: Borrar canción.
  - **Verifica**:
    - La canción se borra.
    - El álbum y artistas se preservan.
    - Se publica el evento `SongDeletedEvent`.

---

### 2.2 Módulo `core:identity`

#### `SignupContractTest`
- **Scenario: Success**:
  - **Acción**: Registrar un usuario nuevo.
  - **Verifica**:
    - Usuario persistido con los roles por defecto (`ROLE_USER`).
    - Contraseña hasheada (si el handler lo implementa).
    - Se publica el evento `UserSignedUpEvent`.
- **Scenario: Duplicate User Credentials (Username or Email)**:
  - **Contexto**: Ya existe un usuario con ese nombre o ese email.
  - **Verifica**: Lanza error/excepción de "User already exists" o "Email already exists".
- **Scenario: Weak Password**:
  - **Contexto**: La contraseña no cumple los requisitos de seguridad.
  - **Verifica**: Lanza error/excepción de validación de contraseña.

#### `LoginContractTest`
- **Scenario: Success**:
  - **Acción**: Login con credenciales válidas.
  - **Verifica**:
    - Devuelve `AuthResponse` con tokens (access + refresh) y datos del usuario.
    - Se publica el evento `UserLoggedInEvent`.
- **Scenario: Invalid Credentials**:
  - **Acción**: Login con contraseña incorrecta o usuario inexistente.
  - **Verifica**: Lanza error de credenciales inválidas.

#### `RefreshAuthContractTest`
- **Scenario: Success**:
  - **Acción**: Refrescar sesión con un `refreshToken` válido.
  - **Verifica**: Devuelve `AuthResponse` con un nuevo par de tokens.
- **Scenario: Invalid Token Type**:
  - **Acción**: Usar un token que no es de tipo "refresh".
  - **Verifica**: Lanza error de tipo de token inválido.

#### `UpdateProfileContractTest`
- **Scenario: Success**:
  - **Acción**: Actualizar datos básicos y extendidos (email, nombre, profileImage, birthDate).
  - **Verifica**: Datos persistidos y `UserUpdatedEvent` publicado.
- **Scenario: Success (Change Password)**:
  - **Acción**: Actualizar la contraseña del usuario.
  - **Verifica**: Nueva contraseña persistida (hasheada) y `UserUpdatedEvent` (o `UserPasswordChangedEvent`) publicado.

#### `Manage2FAContractTest`
- **Scenario: Enable Success**: Activa 2FA para el usuario.
- **Verifica**: Estado de 2FA actualizado en DB.

#### `ValidateTokenContractTest`
- **Scenario: Valid Token**:
  - **Verifica**: Devuelve los claims del token correctamente.
- **Scenario: Expired/Invalid Token**:
  - **Verifica**: Lanza error de validación de JWT.

---

### 2.3 Módulo `features:library`
Este módulo gestiona la colección personal de los usuarios y sus playlists.

#### `AddMediaToLibraryContractTest`
- **Scenario: Success (New)**:
  - **Acción**: Añadir un álbum o canción a la librería.
  - **Verifica**:
    - Ítem persistido vinculado al usuario.
    - Se publica `MediaAddedToLibraryEvent`.
- **Scenario: Idempotency**: Si ya existe, devuelve el ítem y vuelve a disparar el evento.

#### `RemoveMediaFromLibraryContractTest`
- **Scenario: Success**:
  - **Acción**: Quitar ítem de la librería.
  - **Verifica**:
    - Ítem eliminado de la base de datos.
    - Se publica `MediaRemovedFromLibraryEvent`.
- **Scenario: Quiet Success**: Si no existe, no falla y se asume éxito.

#### `LibraryQueryContractTest`
- **Scenario: Paginated Results**:
  - **Acción**: Consultar librería con filtros y paginación.
  - **Verifica**: Devuelve lista de items, total y estado de paginación correcto.

#### `CreatePlaylistContractTest`
- **Scenario: Success**:
  - **Acción**: Crear playlist vacía.
  - **Verifica**: Playlist creada, marcada como "owner" y `PlaylistCreatedEvent` publicado.

#### `UpdatePlaylistContractTest`
- **Scenario: Batch Operations**:
  - **Acción**: En una sola transacción, añadir canciones, mover de posición una existente y cambiar el nombre de la playlist.
  - **Verifica**:
    - El orden de las canciones (position) es consistente.
    - Los metadatos se actualizan.
    - Se publica `PlaylistUpdatedEvent`.

#### `DeletePlaylistContractTest`
- **Scenario: Success**:
  - **Acción**: Borrar playlist.
  - **Verifica**: Playlist eliminada y `PlaylistDeletedEvent` publicado.

#### `ArtistDeletedCleanupEventHandler` (Sincronización)
- **Instrucción**: Este suscriptor reacciona al evento `ArtistDeletedEvent` de Catalog para mantener la integridad en la librería personal.
- **Scenario: Auto-cleanup**:
  - **Dado**: Usuario con canciones del Artista X en su librería.
  - **Evento**: `ArtistDeletedEvent(artistId=X)`.
  - **Verifica**: Las canciones del Artista X desaparecen de la librería de todos los usuarios afectados.

---

### 2.4 Módulo `core:orchestrator`

#### `LoginUseCaseContractTest`
- **Scenario: Success Flow**: Coordina Identity (login) + Device validation.

---

### 2.5 Módulo `features:ingestion` (Pipeline)

#### `CompleteUploadContractTest`
- **Scenario: Pipeline Execution**: Cierre de sesión S3 -> Movimiento de archivos -> Extracción de metadatos -> Notificación de finalización.

---

## 3. Guía para el Programador

1. **Mocked Integration**: Usa **Mockito** y `ArgumentCaptor` para llenar `ScenarioResult.state`.
2. **Real Integration**: Usa **Testcontainers** (Neo4j) y consulta el estado real del grafo tras la ejecución.

### 3.1 Filosofía de Testing de Eventos
- **Publicadores**: Un test de Command/Query Handler **solo** verifica que el evento se publicó en el bus (usando `CapturingEventBus`).
- **Suscriptores**: Los EventHandlers tienen su propio `ContractTest` donde se les inyecta el evento y se verifica su efecto colateral.
- **Interacción Cross-Module**: La coordinación real entre módulos se valida en tests de integración de nivel superior (E2E/Real Integration/Orchestrator), no en los contratos de handlers individuales.

3. **Reactividad (PROHIBIDO `.block()`)**:
   - No uses `.block()` dentro de los métodos `given*`. Hacerlo rompe la cadena reactiva y evita que el `CapturingEventBus` (que puede ser asíncrono) o los efectos colaterales se procesen correctamente.
   - Usa **`StepVerifier`** para consumir el flujo y mapear el resultado al `ScenarioResult`.
   - Ejemplo de patrón correcto:
     ```java
     protected ScenarioResult<Void> givenSomethingReactive() {
         AtomicReference<ScenarioResult<Void>> ref = new AtomicReference<>();
         handler.handle(command)
            .as(StepVerifier::create)
            .assertNext(res -> ref.set(ScenarioResult.success(res)))
            .verifyComplete();
         return ref.get();
     }
     ```

---

## 4. Checklist de Validación Final
- [ ] ¿Es idempotente el borrado? (SÍ)
- [ ] ¿Se gestiona correctamente el featuring? (SÍ)
- [ ] ¿Se propaga el borrado a Library? (SÍ)
- [ ] ¿Se usa `ScenarioResult` para desacoplar el test? (SÍ)
