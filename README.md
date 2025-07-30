## Run Application Locally

**Preconditions:**
- Docker must be installed and running

Run [`DevteamApplication.java`](src/main/java/by/teachmeskills/devteam/DevteamApplication.java)  
The application will automatically start `docker-compose` with a `mysql` container via Spring Boot's Docker Compose integration.

---

## Build Docker Image

```bash
./mvnw spring-boot:build-image