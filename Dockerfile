FROM eclipse-temurin:21-jdk-alpine AS builder

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# simple JAR file setup
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar

# layered unpacked JAR
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENV DB_URL "jdbc:mysql://host.docker.internal:3306/devteam?serverTimezone=UTC"

ENTRYPOINT ["java","-cp","app:app/lib/*","by.teachmeskills.devteam.DevteamApplication"]

