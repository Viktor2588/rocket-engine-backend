# Build stage
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /build
COPY . .
RUN ./gradlew clean build -x test

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install curl, bash, and jq for entrypoint and seeding scripts
RUN apt-get update && apt-get install -y curl bash jq && rm -rf /var/lib/apt/lists/*

# Copy application JAR
COPY --from=builder /build/build/libs/*.jar app.jar

# Copy seed scripts and entrypoint
COPY scripts/ ./scripts/
COPY docker-entrypoint.sh ./
RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT ["/app/docker-entrypoint.sh"]
