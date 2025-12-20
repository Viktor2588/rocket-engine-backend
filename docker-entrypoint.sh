#!/bin/bash
set -e

# Parse Render's DATABASE_URL into Spring Boot format
# Render format: postgres://user:password@host:port/database
# Spring format: jdbc:postgresql://host:port/database (with separate user/password)
if [ -n "$DATABASE_URL" ]; then
    # Extract components from DATABASE_URL
    # Remove the protocol prefix
    DB_URL_NO_PROTO=$(echo "$DATABASE_URL" | sed 's|^postgres://||; s|^postgresql://||')

    # Extract user:password (before @)
    DB_CREDENTIALS=$(echo "$DB_URL_NO_PROTO" | cut -d'@' -f1)
    DB_USER=$(echo "$DB_CREDENTIALS" | cut -d':' -f1)
    DB_PASSWORD=$(echo "$DB_CREDENTIALS" | cut -d':' -f2)

    # Extract host:port/database (after @)
    DB_HOST_PATH=$(echo "$DB_URL_NO_PROTO" | cut -d'@' -f2)

    # Build JDBC URL
    export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST_PATH}"
    export SPRING_DATASOURCE_USERNAME="$DB_USER"
    export SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD"

    echo "Database configured: jdbc:postgresql://${DB_HOST_PATH}"
fi

echo "Starting Spring Boot application..."

# Run Spring Boot in foreground - seeding will happen via DataSeeder
# This ensures proper logging and port binding detection by Render
exec java -Dspring.profiles.active=production \
    -Dserver.port=8080 \
    -jar /app/app.jar
