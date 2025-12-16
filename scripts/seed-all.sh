#!/bin/bash

# Master seeding script - Seeds all data in the correct order
# Usage: ./scripts/seed-all.sh [API_URL]

API_URL="${1:-http://localhost:8080}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "========================================"
echo "  Space Capabilities Dashboard Seeder"
echo "========================================"
echo ""
echo "API URL: $API_URL"
echo ""

# Function to check if an entity collection is empty
check_empty() {
    local endpoint=$1
    local response=$(curl -s "$API_URL/api/$endpoint" 2>/dev/null || echo "[]")
    if [ "$response" = "[]" ]; then
        return 0  # Empty
    else
        return 1  # Not empty
    fi
}

# Function to count entities
count_entities() {
    local endpoint=$1
    curl -s "$API_URL/api/$endpoint" 2>/dev/null | jq 'length' 2>/dev/null || echo "0"
}

# Wait for API to be ready
echo "Checking API availability..."
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s "$API_URL/api/engines" > /dev/null 2>&1; then
        echo "API is ready!"
        break
    fi
    attempt=$((attempt + 1))
    echo "Waiting for API... ($attempt/$max_attempts)"
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "API not available after $max_attempts attempts. Exiting."
    exit 1
fi

echo ""
echo "========================================"
echo "  Current Database Status"
echo "========================================"
echo "Engines:         $(count_entities engines)"
echo "Countries:       $(count_entities countries)"
echo "Launch Vehicles: $(count_entities launch-vehicles)"
echo "Milestones:      $(count_entities milestones)"
echo "Missions:        $(count_entities missions)"
echo "Satellites:      $(count_entities satellites)"
echo "Launch Sites:    $(count_entities launch-sites)"
echo ""

# 1. Seed Engines (no dependencies)
echo "========================================"
echo "  Step 1: Seeding Engines"
echo "========================================"
if check_empty "engines"; then
    echo "Engines collection is empty. Seeding..."
    bash "$SCRIPT_DIR/seed-34-engines.sh"
    echo "Engines seeded!"
else
    echo "Engines already exist. Skipping..."
fi
echo ""

# 2. Seed Launch Vehicles (no dependencies)
echo "========================================"
echo "  Step 2: Seeding Launch Vehicles"
echo "========================================"
if check_empty "launch-vehicles"; then
    echo "Launch vehicles collection is empty. Seeding..."
    bash "$SCRIPT_DIR/seed-launch-vehicles.sh"
    echo "Launch vehicles seeded!"
else
    echo "Launch vehicles already exist. Skipping..."
fi
echo ""

# 3. Seed Countries (no dependencies)
echo "========================================"
echo "  Step 3: Seeding Countries"
echo "========================================"
if check_empty "countries"; then
    echo "Countries collection is empty. Seeding..."
    bash "$SCRIPT_DIR/seed-countries.sh"
    echo "Base countries seeded!"

    echo "Seeding European countries..."
    bash "$SCRIPT_DIR/seed-european-countries.sh"
    echo "European countries seeded!"
else
    echo "Countries already exist. Skipping..."
fi
echo ""

# 4. Seed Milestones (depends on countries)
echo "========================================"
echo "  Step 4: Seeding Milestones"
echo "========================================"
if check_empty "milestones"; then
    if ! check_empty "countries"; then
        echo "Milestones collection is empty. Seeding..."
        bash "$SCRIPT_DIR/seed-milestones.sh"
        echo "Milestones seeded!"
    else
        echo "Cannot seed milestones: Countries not available"
    fi
else
    echo "Milestones already exist. Skipping..."
fi
echo ""

# 5. Seed Missions (depends on countries)
echo "========================================"
echo "  Step 5: Seeding Missions"
echo "========================================"
if check_empty "missions"; then
    if ! check_empty "countries"; then
        echo "Missions collection is empty. Seeding..."
        bash "$SCRIPT_DIR/seed-missions.sh"
        echo "Missions seeded!"
    else
        echo "Cannot seed missions: Countries not available"
    fi
else
    echo "Missions already exist. Skipping..."
fi
echo ""

# 6. Seed Satellites (depends on countries)
echo "========================================"
echo "  Step 6: Seeding Satellites"
echo "========================================"
if check_empty "satellites"; then
    if ! check_empty "countries"; then
        echo "Satellites collection is empty. Seeding..."
        bash "$SCRIPT_DIR/seed-satellites.sh"
        echo "Satellites seeded!"
    else
        echo "Cannot seed satellites: Countries not available"
    fi
else
    echo "Satellites already exist. Skipping..."
fi
echo ""

# 7. Seed Launch Sites (depends on countries)
echo "========================================"
echo "  Step 7: Seeding Launch Sites"
echo "========================================"
if check_empty "launch-sites"; then
    if ! check_empty "countries"; then
        echo "Launch sites collection is empty. Seeding..."
        bash "$SCRIPT_DIR/seed-launch-sites.sh"
        echo "Launch sites seeded!"
    else
        echo "Cannot seed launch sites: Countries not available"
    fi
else
    echo "Launch sites already exist. Skipping..."
fi
echo ""

# Final status
echo "========================================"
echo "  Final Database Status"
echo "========================================"
echo "Engines:         $(count_entities engines)"
echo "Countries:       $(count_entities countries)"
echo "Launch Vehicles: $(count_entities launch-vehicles)"
echo "Milestones:      $(count_entities milestones)"
echo "Missions:        $(count_entities missions)"
echo "Satellites:      $(count_entities satellites)"
echo "Launch Sites:    $(count_entities launch-sites)"
echo ""
echo "Seeding complete!"
echo ""
echo "Test the API:"
echo "  Overview:   $API_URL/api/statistics/overview"
echo "  Countries:  $API_URL/api/countries"
echo "  Engines:    $API_URL/api/engines"
echo "  Analytics:  $API_URL/api/analytics/summary"
echo "  Rankings:   $API_URL/api/rankings"
