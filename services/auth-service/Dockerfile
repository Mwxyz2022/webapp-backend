# -------- Stage 1: Build JAR --------
FROM gradle:8.4.0-jdk21 AS builder

WORKDIR /build

COPY common ./common
COPY services/auth-service ./services/auth-service
COPY infrastructure/redis-config ./infrastructure/redis-config
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradlew .
COPY gradle ./gradle

RUN ./gradlew :services:auth-service:bootJar --no-daemon

# -------- Stage 2: Runtime --------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /build/services/auth-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
