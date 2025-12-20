#!/bin/bash
set -e

# Convert Render's postgres:// URL to jdbc:postgresql:// format if needed
if [ -n "$DATABASE_URL" ]; then
    # Replace postgres:// with jdbc:postgresql://
    export SPRING_DATASOURCE_URL=$(echo "$DATABASE_URL" | sed 's|^postgres://|jdbc:postgresql://|; s|^postgresql://|jdbc:postgresql://|')
    echo "Database URL configured"
fi

echo "Starting Spring Boot application..."

# Run Spring Boot in foreground - seeding will happen via DataSeeder
# This ensures proper logging and port binding detection by Render
exec java -Dspring.profiles.active=production \
    -Dserver.port=8080 \
    -jar /app/app.jar
