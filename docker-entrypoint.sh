#!/bin/bash
set -e

# Start the application in the background
echo "Starting Spring Boot application..."
java -Dspring.profiles.active=production -jar /app/app.jar &
APP_PID=$!

# Wait for the application to be ready
echo "Waiting for application to start..."
max_attempts=60
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8080/api/engines > /dev/null 2>&1; then
        echo "✅ Application is ready!"
        break
    fi
    attempt=$((attempt + 1))
    if [ $((attempt % 10)) -eq 0 ]; then
        echo "⏳ Still waiting... ($attempt/$max_attempts seconds)"
    fi
    sleep 1
done

if [ $attempt -eq $max_attempts ]; then
    echo "⚠️ Application startup check timed out, but continuing anyway..."
fi

# Give app a bit more time to settle
sleep 3

# Check if database is empty by trying to get engines
echo "Checking database status..."
response=$(curl -s http://localhost:8080/api/engines 2>/dev/null || echo "[]")

# If response is empty array, seed the database
if [ "$response" = "[]" ]; then
    echo "📦 Database is empty, seeding with rocket engines..."

    # Make script executable just in case
    chmod +x /app/scripts/seed-34-engines.sh

    # Run seed script
    bash /app/scripts/seed-34-engines.sh

    echo "✅ Seeding completed!"
else
    echo "✅ Database already contains data, skipping seed..."
fi

echo "🚀 Application startup complete!"

# Wait for the application process to continue running
wait $APP_PID
