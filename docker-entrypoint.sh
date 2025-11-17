#!/bin/bash
set -e

# Start the application in the background
echo "Starting Spring Boot application..."
java -Dspring.profiles.active=production -jar /app/app.jar &
APP_PID=$!

# Wait for the application to be ready
echo "Waiting for application to start..."
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -f http://localhost:8080/api/engines > /dev/null 2>&1; then
        echo "Application is ready!"
        break
    fi
    attempt=$((attempt + 1))
    echo "Waiting... ($attempt/$max_attempts)"
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "Application failed to start"
    kill $APP_PID 2>/dev/null || true
    exit 1
fi

# Check if database is empty
ENGINE_COUNT=$(curl -s http://localhost:8080/api/engines | grep -o '\[' | wc -l)

if [ "$ENGINE_COUNT" -eq 1 ]; then
    # Database has data (array was returned), don't seed
    echo "Database already contains data, skipping seed..."
else
    # Database is empty, run seed script
    echo "Database is empty, seeding with rocket engines..."
    bash /app/scripts/seed-34-engines.sh
    echo "Seeding completed!"
fi

# Wait for the application process
wait $APP_PID
