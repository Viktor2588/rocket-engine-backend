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
        echo "✅ Application is ready!"
        sleep 5  # Extra safety wait after API responds
        break
    fi
    attempt=$((attempt + 1))
    if [ $((attempt % 20)) -eq 0 ]; then
        echo "⏳ Still waiting... ($attempt/$max_attempts seconds)"
    fi
    sleep 1
done

if [ $attempt -eq $max_attempts ]; then
    echo "⚠️ Application startup check timed out, but continuing anyway..."
    sleep 10
fi

# Check if database is empty by trying to get engines
echo "Checking database status..."
engines_response=$(curl -s http://localhost:8080/api/engines 2>/dev/null || echo "[]")
countries_response=$(curl -s http://localhost:8080/api/countries 2>/dev/null || echo "[]")

# Seed engines if empty
if [ "$engines_response" = "[]" ]; then
    echo "📦 Seeding engines..."
    chmod +x /app/scripts/seed-34-engines.sh
    bash /app/scripts/seed-34-engines.sh
    echo "✅ Engines seeded!"
else
    echo "✅ Engines already exist, skipping..."
fi

# Seed countries if empty
if [ "$countries_response" = "[]" ]; then
    echo "🌍 Seeding countries..."
    chmod +x /app/scripts/seed-countries.sh
    bash /app/scripts/seed-countries.sh

    echo "🇪🇺 Seeding European countries..."
    chmod +x /app/scripts/seed-european-countries.sh
    bash /app/scripts/seed-european-countries.sh
    echo "✅ Countries seeded!"
else
    echo "✅ Countries already exist, skipping..."
fi

# Seed other entities if countries exist but milestones don't
milestones_response=$(curl -s http://localhost:8080/api/milestones 2>/dev/null || echo "[]")
if [ "$milestones_response" = "[]" ] && [ "$countries_response" != "[]" ]; then
    echo "🏆 Seeding milestones..."
    chmod +x /app/scripts/seed-milestones.sh
    bash /app/scripts/seed-milestones.sh
    echo "✅ Milestones seeded!"
fi

missions_response=$(curl -s http://localhost:8080/api/missions 2>/dev/null || echo "[]")
if [ "$missions_response" = "[]" ] && [ "$countries_response" != "[]" ]; then
    echo "🛸 Seeding missions..."
    chmod +x /app/scripts/seed-missions.sh
    bash /app/scripts/seed-missions.sh
    echo "✅ Missions seeded!"
fi

satellites_response=$(curl -s http://localhost:8080/api/satellites 2>/dev/null || echo "[]")
if [ "$satellites_response" = "[]" ] && [ "$countries_response" != "[]" ]; then
    echo "🛰️ Seeding satellites..."
    chmod +x /app/scripts/seed-satellites.sh
    bash /app/scripts/seed-satellites.sh
    echo "✅ Satellites seeded!"
fi

launch_sites_response=$(curl -s http://localhost:8080/api/launch-sites 2>/dev/null || echo "[]")
if [ "$launch_sites_response" = "[]" ] && [ "$countries_response" != "[]" ]; then
    echo "🏗️ Seeding launch sites..."
    chmod +x /app/scripts/seed-launch-sites.sh
    bash /app/scripts/seed-launch-sites.sh
    echo "✅ Launch sites seeded!"
fi

echo "✅ All seeding completed!"

echo "🚀 Application startup complete!"

# Wait for the application process to continue running
wait $APP_PID
