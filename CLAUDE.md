# CLAUDE.md — Spring Boot & Java Best Practices

This file provides Claude Code with context, conventions, and rules for this project.
Read it fully before writing or modifying any code.

---

## Stack

- **Java 21** (use records, sealed classes, pattern matching, virtual threads where appropriate)
- **Spring Boot 3.x**
- **Spring Data JPA** + Hibernate
- **Spring Security** (if applicable)
- **Maven** (or Gradle — adjust accordingly)
- **PostgreSQL** (adjust to your DB)
- **MapStruct** for DTO mapping
- **Lombok** for boilerplate reduction
- **JUnit 5 + Mockito + AssertJ** for testing

---

## Project Structure

```
src/
└── main/
    └── java/com/example/app/
        ├── config/          # Spring configuration classes
        ├── controller/      # REST controllers (@RestController)
        ├── service/         # Business logic interfaces + implementations
        ├── repository/      # Spring Data JPA repositories
        ├── model/           # JPA entities
        ├── dto/             # Request/Response DTOs (use Java records)
        ├── mapper/          # MapStruct mappers
        ├── exception/       # Custom exceptions + GlobalExceptionHandler
        └── util/            # Utility/helper classes
```

---

## Architecture Rules

- Strict **layered architecture**: Controller → Service → Repository. Never skip layers.
- Controllers only handle HTTP: validate input, call service, return response. No business logic.
- Services contain all business logic. Services talk to repositories and other services, never to controllers.
- Repositories only perform queries. No business logic inside repositories.
- Never expose JPA entities directly in API responses. Always use DTOs.
- Use interfaces for services when multiple implementations are plausible or for testability.

---

## REST Controllers

```java
@RestController
@RequestMapping("/api/v1/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody ResourceRequest request) {
        ResourceResponse response = resourceService.create(request);
        URI location = URI.create("/api/v1/resource/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ResourceRequest request) {
        return ResponseEntity.ok(resourceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

- Use `ResponseEntity<T>` for explicit HTTP status control.
- Always version APIs: `/api/v1/`, `/api/v2/`.
- Use `@Valid` on every `@RequestBody`.
- Return `201 Created` with `Location` header on POST.
- Return `204 No Content` on DELETE.

---

## DTOs

Prefer **Java records** for immutable DTOs:

```java
// Request DTO
public record ResourceRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    String name,

    @NotNull
    @Positive
    Integer quantity
) {}

// Response DTO
public record ResourceResponse(
    Long id,
    String name,
    Integer quantity,
    LocalDateTime createdAt
) {}
```

- Never use JPA entities as request or response objects.
- Keep request and response DTOs separate even if they look similar.
- Apply Bean Validation annotations on request DTOs only.

---

## Entities

```java
@Entity
@Table(name = "resources")
@Getter
@Setter
@NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

- Always define `@Table(name = "...")` explicitly.
- Use `@Column(nullable = false)` to enforce constraints at DB level.
- Prefer `@GeneratedValue(strategy = GenerationType.IDENTITY)` for PostgreSQL.
- Use `@CreationTimestamp` / `@UpdateTimestamp` from Hibernate for audit fields.
- Avoid bidirectional relationships unless strictly necessary (they cause N+1 and serialization issues).
- Never use `FetchType.EAGER`. Default to `LAZY` and fetch explicitly when needed.

---

## Services

```java
public interface ResourceService {
    ResourceResponse findById(Long id);
    ResourceResponse create(ResourceRequest request);
    ResourceResponse update(Long id, ResourceRequest request);
    void delete(Long id);
}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public ResourceResponse findById(Long id) {
        Resource resource = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        return mapper.toResponse(resource);
    }

    @Override
    @Transactional
    public ResourceResponse create(ResourceRequest request) {
        Resource resource = mapper.toEntity(request);
        return mapper.toResponse(repository.save(resource));
    }

    @Override
    @Transactional
    public ResourceResponse update(Long id, ResourceRequest request) {
        Resource resource = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        mapper.updateEntity(request, resource);
        return mapper.toResponse(repository.save(resource));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
```

- Annotate the class with `@Transactional(readOnly = true)` and override with `@Transactional` on write methods.
- Always throw domain-specific exceptions (never generic `RuntimeException`).
- Use the mapper to convert between entity and DTO — never do it manually in the service.

---

## Repositories

```java
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    boolean existsByName(String name);

    @Query("SELECT r FROM Resource r WHERE r.quantity > :minQuantity")
    List<Resource> findByMinQuantity(@Param("minQuantity") int minQuantity);

    Page<Resource> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
```

- Extend `JpaRepository<Entity, ID>` — never `CrudRepository` unless there's a specific reason.
- Use JPQL (`@Query`) or derived method names — avoid native SQL unless necessary.
- When native SQL is needed, use `nativeQuery = true` and document why.
- Return `Optional<T>` for single-result queries.
- Support pagination with `Pageable` on list queries that could grow large.

---

## Exception Handling

```java
// Custom exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

// Error response DTO
public record ErrorResponse(
    int status,
    String error,
    String message,
    LocalDateTime timestamp
) {}

// Global handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorResponse(404, "Not Found", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorResponse(400, "Bad Request", message, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ErrorResponse(500, "Internal Server Error", "An unexpected error occurred", LocalDateTime.now())
        );
    }
}
```

- Never let exceptions bubble up to the client without a handler.
- Always return a consistent error response structure.
- Log unexpected exceptions before returning 500.
- Never expose stack traces or internal messages in production responses.

---

## MapStruct Mappers

```java
@Mapper(componentModel = "spring")
public interface ResourceMapper {

    ResourceResponse toResponse(Resource entity);

    Resource toEntity(ResourceRequest request);

    @MappingTarget
    void updateEntity(ResourceRequest request, @MappingTarget Resource entity);
}
```

- Always use MapStruct — never map fields manually.
- Use `@MappingTarget` for update operations to avoid creating new entity instances.
- Annotate with `componentModel = "spring"` so it's injectable.

---

## Validation

- Use Bean Validation (`jakarta.validation`) annotations on request DTOs.
- Always use `@Valid` in controllers for `@RequestBody` and `@PathVariable`.
- Use `@Validated` at class level for method-level validation.
- Common annotations: `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`, `@Pattern`.
- Create custom validators with `@Constraint` for complex business rules.

---

## Configuration & Properties

- Use `application.yml` over `application.properties`.
- Group properties by feature/module.
- Use `@ConfigurationProperties` for typed config beans — never `@Value` for groups of related properties.
- Keep secrets (passwords, API keys) out of source code. Use environment variables or a secrets manager.
- Define profiles: `application-dev.yml`, `application-prod.yml`.

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/mydb}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: validate   # Never 'create' or 'create-drop' in production
    show-sql: false         # Only true in dev profile
    open-in-view: false     # Always disable this
```

- **Always set `open-in-view: false`** — it causes lazy-loading issues and is a performance anti-pattern.
- **Never use `ddl-auto: create` or `update` in production**. Use Flyway or Liquibase for migrations.

---

## Database Migrations

- Use **Flyway** for schema migrations.
- Name files: `V1__create_users_table.sql`, `V2__add_email_index.sql`.
- Never modify an existing migration file — always create a new one.
- Keep migrations small and focused.

---

## Testing

```java
// Unit test — service layer
@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock ResourceRepository repository;
    @Mock ResourceMapper mapper;
    @InjectMocks ResourceServiceImpl service;

    @Test
    void findById_whenExists_returnsResponse() {
        Resource resource = new Resource();
        ResourceResponse expected = new ResourceResponse(1L, "test", 5, LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(resource));
        when(mapper.toResponse(resource)).thenReturn(expected);

        ResourceResponse result = service.findById(1L);

        assertThat(result).isEqualTo(expected);
        verify(repository).findById(1L);
    }

    @Test
    void findById_whenNotExists_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}

// Integration test — controller layer
@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean ResourceService service;
    @Autowired ObjectMapper objectMapper;

    @Test
    void getById_returnsOk() throws Exception {
        ResourceResponse response = new ResourceResponse(1L, "test", 5, LocalDateTime.now());
        when(service.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/resource/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("test"));
    }
}
```

- Unit tests for service layer with Mockito — fast, no Spring context.
- `@WebMvcTest` for controller layer — tests HTTP without full context.
- `@SpringBootTest` + Testcontainers for full integration tests.
- Use AssertJ (`assertThat`) over JUnit assertions for readability.
- Name tests: `methodName_whenCondition_thenExpectedResult`.
- Aim for: **unit tests** covering business logic, **integration tests** covering happy paths + main error cases.

---

## General Java Rules

- Prefer **immutability**: use `final` fields, records, and unmodifiable collections.
- Use `Optional<T>` for nullable return values — never return `null` from public methods.
- Avoid raw types — always parameterize generics.
- Use `var` for local variables when the type is obvious from context.
- Prefer `List.of()`, `Map.of()`, `Set.of()` for creating immutable collections.
- Use `Stream` API over imperative loops for collection transformations.
- Close resources with try-with-resources.
- Log with **SLF4J** (`LoggerFactory.getLogger`) — never `System.out.println`.
- Use parameterized log messages: `log.debug("Processing id={}", id)` — never string concatenation.

---

## What NOT to do

- ❌ Never put business logic in controllers or repositories.
- ❌ Never expose JPA entities in API responses.
- ❌ Never use `FetchType.EAGER` on relationships.
- ❌ Never use `open-in-view: true`.
- ❌ Never use `ddl-auto: update` in production.
- ❌ Never catch `Exception` silently (empty catch blocks).
- ❌ Never hardcode secrets or environment-specific values.
- ❌ Never return `null` from public service methods — use `Optional` or throw an exception.
- ❌ Never use `@Autowired` on fields — always inject via constructor (Lombok `@RequiredArgsConstructor`).
- ❌ Never write SQL inside service or controller classes.
