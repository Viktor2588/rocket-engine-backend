# Build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY . .
RUN ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "app.jar"]
