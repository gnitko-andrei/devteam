## Run Application Locally

**Preconditions:**
- Docker must be installed and running

Run [`DevteamApplication.java`](src/main/java/by/teachmeskills/devteam/DevteamApplication.java)  
The application will automatically start `docker-compose` with a `mysql` container via Spring Boot's Docker Compose integration.

---

## Build Docker Image

```bash
./mvnw spring-boot:build-image
```
## Testing Structure Overview

This project follows a layered test strategy aligned with the Spring Boot Testing Pyramid.
Each layer has its own naming convention, tag, and execution scope.

 Layer | Class name pattern | JUnit Tag(s) | Maven Plugin | Purpose |
|-------|--------------------|---------------|---------------|----------|
| Unit | `*UnitTest` | — | Surefire | Fast, isolated logic tests using mocks. Run by default. |
| Web Slice | `*WebTest` | `@Tag("web")` | Surefire | MVC controller tests with `@WebMvcTest`, mock security and request handling. |
| JPA Slice | `*JpaTest` | `@Tag("jpa")` | Surefire | Repository tests with real DB (Testcontainers + Flyway). |
| Service Layer (optional) | `*ServiceTest` | *(optional)* `@Tag("service")` | Surefire | Business/service layer tests running in Spring context. |
| End-to-End / Integration | `*IT` | `@Tag("e2e")` | Failsafe | Full application context with real HTTP, DB, security, and Thymeleaf. |
| E2E Smoke Tests | `ApplicationHttpSmokeIT` | `@Tag("e2e")`, `@Tag("smoke")` | Failsafe | Minimal full-stack verification of key endpoints and redirects. |
| Context Sanity Check | `DevteamApplicationTests` | `@Tag("smoke")` | Surefire | Ensures the Spring Boot application context starts successfully. |


### Naming and Execution Rules

- Unit tests run automatically via `mvn test`.
- Integration (E2E) tests run via `mvn verify`.
- Failsafe plugin ensures E2E tests (`*IT`, `*ITCase`) execute after packaging.
- Tags enable selective execution and reporting.

### Example commands

Run only fast tests:
```bash
mvn test -DexcludeTags=e2e
```

Run only E2E tests:
```bash
mvn verify -DincludeTags=e2e
```

Run only smoke checks (context + HTTP smoke):
```bash
mvn verify -DincludeTags=smoke
```