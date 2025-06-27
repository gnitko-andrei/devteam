FROM eclipse-temurin:21-jdk-alpine AS builder

# layered unpacked JAR
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

# default url when db runs locally, overriden by secrets when run via compose
ENV DB_URL "jdbc:mysql://host.docker.internal:3306/devteam?serverTimezone=UTC"

COPY docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
