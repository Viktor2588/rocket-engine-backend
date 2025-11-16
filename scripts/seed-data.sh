#!/bin/bash

# Seed data script for Rocket Engine Comparison application
# This script adds sample rocket engine data to the database

API_URL="http://localhost:8080/api/engines"

# Function to add an engine
add_engine() {
  local name=$1
  local manufacturer=$2
  local thrust=$3
  local isp=$4
  local propellant=$5
  local mass=$6
  local description=$7

  curl -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"$name\",
      \"manufacturer\": \"$manufacturer\",
      \"thrust\": $thrust,
      \"isp\": $isp,
      \"propellantType\": \"$propellant\",
      \"mass\": $mass,
      \"description\": \"$description\"
    }"

  echo ""
}

echo "🚀 Starting seed data insertion..."
echo ""

# SpaceX engines
echo "Adding SpaceX engines..."
add_engine \
  "Merlin 1D" \
  "SpaceX" \
  "934" \
  "282" \
  "RP-1/LOX" \
  "470" \
  "The Merlin 1D is a rocket engine developed by SpaceX for the Falcon 9 rocket. It's a single-nozzle engine that underwent significant developments from its original form."

add_engine \
  "Raptor 2" \
  "SpaceX" \
  "2050" \
  "380" \
  "Methane/LOX" \
  "1400" \
  "The Raptor 2 is a full-flow staged combustion methane-oxygen rocket engine developed by SpaceX for use on the Starship launch vehicle. Sea-level optimized engine."

# Blue Origin engines
echo "Adding Blue Origin engines..."
add_engine \
  "BE-4" \
  "Blue Origin" \
  "2450" \
  "380" \
  "NG/LOX" \
  "1300" \
  "The BE-4 is a gas-generator rocket engine built by Blue Origin. It's used on United Launch Alliance's Atlas V and Vulcan rockets."

add_engine \
  "BE-3" \
  "Blue Origin" \
  "667" \
  "268" \
  "Hydrogen/LOX" \
  "750" \
  "The BE-3 is a hydrogen-oxygen rocket engine used on Blue Origin's New Shepard suborbital spaceplane and New Glenn orbital launch vehicle."

# Rocket Lab engines
echo "Adding Rocket Lab engines..."
add_engine \
  "Rutherford" \
  "Rocket Lab" \
  "25" \
  "303" \
  "RP-1/LOX" \
  "35" \
  "The Rutherford is a small, fuel-rich staged combustion engine developed by Rocket Lab for the Electron launch vehicle."

add_engine \
  "Archimedes" \
  "Rocket Lab" \
  "35" \
  "290" \
  "RP-1/LOX" \
  "45" \
  "The Archimedes is an upper stage engine being developed by Rocket Lab for the Neutron rocket."

# Traditional engines (for comparison)
echo "Adding traditional rocket engines..."
add_engine \
  "RS-25" \
  "Aerojet Rocketdyne" \
  "1860" \
  "452" \
  "Hydrogen/LOX" \
  "3600" \
  "The RS-25 is a large, high-performance hydrogen-oxygen rocket engine used on the Space Launch System. Also used on the Space Shuttle."

add_engine \
  "F-1" \
  "Rocketdyne" \
  "7645" \
  "263" \
  "RP-1/LOX" \
  "18100" \
  "The F-1 is the highest-thrust single-nozzle liquid-fueled rocket engine ever developed. It was used on the Saturn V rocket."

add_engine \
  "Vulcain 2" \
  "Aerojet Rocketdyne" \
  "1390" \
  "431" \
  "Hydrogen/LOX" \
  "2310" \
  "The Vulcain 2 is a hydrogen-oxygen rocket engine developed for the Ariane 5 and Ariane 6 rockets."

# German engines
echo "Adding German rocket engines..."
add_engine \
  "Aquila" \
  "Isar Aerospace" \
  "45" \
  "310" \
  "RP-1/LOX" \
  "85" \
  "The Aquila is an experimental rocket engine developed by Isar Aerospace for the Spectrum rocket. It features advanced combustion chamber design for improved performance."

echo ""
echo "✅ Seed data insertion complete!"
echo ""
echo "Verifying data..."
curl -s "$API_URL" | jq '.[] | {id, name, manufacturer, thrust, isp, propellantType}'
