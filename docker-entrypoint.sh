#!/bin/bash
set -e

# Start the application in the background
echo "Starting Spring Boot application..."
java -Dspring.profiles.active=production -jar /app/app.jar &
APP_PID=$!

# Wait for the application to be ready
echo "Waiting for application to start (this can take 90+ seconds)..."
max_attempts=120
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8080/api/engines > /dev/null 2>&1; then
        echo "Application is ready!"
        sleep 5  # Extra safety wait after API responds
        break
    fi
    attempt=$((attempt + 1))
    if [ $((attempt % 20)) -eq 0 ]; then
        echo "Still waiting... ($attempt/$max_attempts seconds)"
    fi
    sleep 1
done

if [ $attempt -eq $max_attempts ]; then
    echo "Application startup check timed out after $max_attempts seconds."
    echo "Waiting 30 more seconds before running seed scripts..."
    sleep 30
fi

# Run the master seeding script
echo "Running master seeding script..."
chmod +x /app/scripts/seed-all.sh
bash /app/scripts/seed-all.sh http://localhost:8080

echo "Application startup complete!"

# Wait for the application process to continue running
wait $APP_PID
