#!/bin/bash

# Seed Launch Vehicles Data
# Comprehensive launch vehicle database

API_URL="${1:-http://localhost:8080}"
ENDPOINT="$API_URL/api/launch-vehicles"

echo "🚀 Seeding Launch Vehicles to: $ENDPOINT"
echo "================================================"

# Function to create a launch vehicle
create_vehicle() {
    local json="$1"
    local name=$(echo "$json" | jq -r '.name')

    response=$(curl -s -w "\n%{http_code}" -X POST "$ENDPOINT" \
        -H "Content-Type: application/json" \
        -d "$json")

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" = "200" ] || [ "$http_code" = "201" ]; then
        echo "✅ Created: $name"
    else
        echo "❌ Failed: $name (HTTP $http_code)"
    fi
}

# ==================== USA Launch Vehicles ====================
echo ""
echo "🇺🇸 United States Launch Vehicles"
echo "-----------------------------------"

# Falcon 9
create_vehicle '{
    "name": "Falcon 9",
    "family": "Falcon",
    "variant": "Block 5",
    "fullName": "Falcon 9 Block 5",
    "country": {"id": 1},
    "manufacturer": "SpaceX",
    "heightMeters": 70.0,
    "diameterMeters": 3.7,
    "massKg": 549054.0,
    "stages": 2,
    "payloadToLeoKg": 22800,
    "payloadToGtoKg": 8300,
    "payloadToMarsKg": 4020,
    "firstStageEngines": "Merlin 1D",
    "firstStageEngineCount": 9,
    "secondStageEngines": "Merlin 1D Vacuum",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 7607,
    "status": "Active",
    "firstFlightYear": 2010,
    "totalLaunches": 300,
    "successfulLaunches": 298,
    "reusable": true,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 67000000,
    "description": "SpaceX Falcon 9 is a two-stage rocket designed for reliable and cost-effective transport of satellites and cargo to orbit. It features reusable first stage boosters.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Falcon_9"
}'

# Falcon Heavy
create_vehicle '{
    "name": "Falcon Heavy",
    "family": "Falcon",
    "variant": "Block 5",
    "fullName": "Falcon Heavy",
    "country": {"id": 1},
    "manufacturer": "SpaceX",
    "heightMeters": 70.0,
    "diameterMeters": 12.2,
    "massKg": 1420788.0,
    "stages": 2,
    "payloadToLeoKg": 63800,
    "payloadToGtoKg": 26700,
    "payloadToMarsKg": 16800,
    "firstStageEngines": "Merlin 1D",
    "firstStageEngineCount": 27,
    "secondStageEngines": "Merlin 1D Vacuum",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 22819,
    "status": "Active",
    "firstFlightYear": 2018,
    "totalLaunches": 10,
    "successfulLaunches": 10,
    "reusable": true,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 97000000,
    "description": "Falcon Heavy is the most powerful operational rocket in the world, capable of lifting large payloads to orbit and beyond.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Falcon_Heavy"
}'

# Starship
create_vehicle '{
    "name": "Starship",
    "family": "Starship",
    "variant": "Full Stack",
    "fullName": "Starship + Super Heavy",
    "country": {"id": 1},
    "manufacturer": "SpaceX",
    "heightMeters": 121.0,
    "diameterMeters": 9.0,
    "massKg": 5000000.0,
    "stages": 2,
    "payloadToLeoKg": 150000,
    "payloadToGtoKg": 21000,
    "payloadToMoonKg": 100000,
    "payloadToMarsKg": 100000,
    "firstStageEngines": "Raptor 2",
    "firstStageEngineCount": 33,
    "secondStageEngines": "Raptor Vacuum",
    "secondStageEngineCount": 6,
    "propellant": "CH4/LOX",
    "thrustAtLiftoffKn": 74500,
    "status": "In Development",
    "firstFlightYear": 2023,
    "totalLaunches": 6,
    "successfulLaunches": 3,
    "reusable": true,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 10000000,
    "description": "Starship is designed to be a fully reusable transportation system for crew and cargo to Earth orbit, the Moon, Mars, and beyond.",
    "wikiUrl": "https://en.wikipedia.org/wiki/SpaceX_Starship"
}'

# Atlas V
create_vehicle '{
    "name": "Atlas V",
    "family": "Atlas",
    "variant": "551",
    "fullName": "Atlas V 551",
    "country": {"id": 1},
    "manufacturer": "United Launch Alliance",
    "heightMeters": 62.2,
    "diameterMeters": 3.81,
    "massKg": 590000.0,
    "stages": 2,
    "payloadToLeoKg": 18850,
    "payloadToGtoKg": 8900,
    "firstStageEngines": "RD-180",
    "firstStageEngineCount": 1,
    "secondStageEngines": "RL-10C",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 4152,
    "status": "Active",
    "firstFlightYear": 2002,
    "totalLaunches": 99,
    "successfulLaunches": 99,
    "reusable": false,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 153000000,
    "description": "Atlas V is an expendable launch system known for its exceptional reliability, operated by United Launch Alliance.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Atlas_V"
}'

# Vulcan Centaur
create_vehicle '{
    "name": "Vulcan",
    "family": "Vulcan",
    "variant": "Centaur",
    "fullName": "Vulcan Centaur",
    "country": {"id": 1},
    "manufacturer": "United Launch Alliance",
    "heightMeters": 61.6,
    "diameterMeters": 5.4,
    "massKg": 546700.0,
    "stages": 2,
    "payloadToLeoKg": 27200,
    "payloadToGtoKg": 14400,
    "firstStageEngines": "BE-4",
    "firstStageEngineCount": 2,
    "secondStageEngines": "RL-10C",
    "secondStageEngineCount": 2,
    "propellant": "CH4/LOX",
    "thrustAtLiftoffKn": 4900,
    "status": "Active",
    "firstFlightYear": 2024,
    "totalLaunches": 2,
    "successfulLaunches": 2,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 110000000,
    "description": "Vulcan Centaur is ULA next-generation rocket, designed to replace both Atlas V and Delta IV.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Vulcan_Centaur"
}'

# Delta IV Heavy
create_vehicle '{
    "name": "Delta IV Heavy",
    "family": "Delta",
    "variant": "Heavy",
    "fullName": "Delta IV Heavy",
    "country": {"id": 1},
    "manufacturer": "United Launch Alliance",
    "heightMeters": 72.0,
    "diameterMeters": 5.0,
    "massKg": 733000.0,
    "stages": 2,
    "payloadToLeoKg": 28790,
    "payloadToGtoKg": 14220,
    "firstStageEngines": "RS-68A",
    "firstStageEngineCount": 3,
    "secondStageEngines": "RL-10B-2",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 9420,
    "status": "Retired",
    "firstFlightYear": 2004,
    "lastFlightYear": 2024,
    "totalLaunches": 16,
    "successfulLaunches": 15,
    "reusable": false,
    "humanRated": false,
    "active": false,
    "costPerLaunchUsd": 350000000,
    "description": "Delta IV Heavy was the most powerful rocket in the US fleet before Falcon Heavy, primarily used for national security missions.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Delta_IV_Heavy"
}'

# SLS
create_vehicle '{
    "name": "SLS",
    "family": "Space Launch System",
    "variant": "Block 1",
    "fullName": "Space Launch System Block 1",
    "country": {"id": 1},
    "manufacturer": "Boeing/Northrop Grumman",
    "heightMeters": 98.0,
    "diameterMeters": 8.4,
    "massKg": 2608000.0,
    "stages": 2,
    "payloadToLeoKg": 95000,
    "payloadToMoonKg": 27000,
    "firstStageEngines": "RS-25",
    "firstStageEngineCount": 4,
    "secondStageEngines": "RL-10",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 39000,
    "status": "Active",
    "firstFlightYear": 2022,
    "totalLaunches": 1,
    "successfulLaunches": 1,
    "reusable": false,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 2200000000,
    "description": "The Space Launch System is NASA super heavy-lift launch vehicle for the Artemis program to return humans to the Moon.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Space_Launch_System"
}'

# New Glenn
create_vehicle '{
    "name": "New Glenn",
    "family": "New Glenn",
    "variant": "2-Stage",
    "fullName": "New Glenn",
    "country": {"id": 1},
    "manufacturer": "Blue Origin",
    "heightMeters": 98.0,
    "diameterMeters": 7.0,
    "massKg": 1500000.0,
    "stages": 2,
    "payloadToLeoKg": 45000,
    "payloadToGtoKg": 13000,
    "firstStageEngines": "BE-4",
    "firstStageEngineCount": 7,
    "secondStageEngines": "BE-3U",
    "secondStageEngineCount": 2,
    "propellant": "CH4/LOX",
    "thrustAtLiftoffKn": 17100,
    "status": "Active",
    "firstFlightYear": 2025,
    "totalLaunches": 1,
    "successfulLaunches": 1,
    "reusable": true,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 68000000,
    "description": "New Glenn is a heavy-lift orbital launch vehicle being developed by Blue Origin with a reusable first stage.",
    "wikiUrl": "https://en.wikipedia.org/wiki/New_Glenn"
}'

# ==================== Russia Launch Vehicles ====================
echo ""
echo "🇷🇺 Russia Launch Vehicles"
echo "---------------------------"

# Soyuz-2
create_vehicle '{
    "name": "Soyuz-2",
    "family": "Soyuz",
    "variant": "1b",
    "fullName": "Soyuz-2.1b",
    "country": {"id": 2},
    "manufacturer": "RKK Energia/Progress",
    "heightMeters": 46.3,
    "diameterMeters": 2.95,
    "massKg": 312000.0,
    "stages": 3,
    "payloadToLeoKg": 8200,
    "payloadToGtoKg": 3250,
    "firstStageEngines": "RD-107A",
    "firstStageEngineCount": 4,
    "secondStageEngines": "RD-108A",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 4144,
    "status": "Active",
    "firstFlightYear": 2006,
    "totalLaunches": 150,
    "successfulLaunches": 147,
    "reusable": false,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 48500000,
    "description": "Soyuz-2 is the modernized version of the legendary Soyuz rocket family, which has been in service since 1966.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Soyuz-2"
}'

# Proton-M
create_vehicle '{
    "name": "Proton-M",
    "family": "Proton",
    "variant": "M/Briz-M",
    "fullName": "Proton-M/Briz-M",
    "country": {"id": 2},
    "manufacturer": "Khrunichev",
    "heightMeters": 58.2,
    "diameterMeters": 7.4,
    "massKg": 705000.0,
    "stages": 4,
    "payloadToLeoKg": 23000,
    "payloadToGtoKg": 6300,
    "firstStageEngines": "RD-275M",
    "firstStageEngineCount": 6,
    "secondStageEngines": "RD-0210/0211",
    "secondStageEngineCount": 4,
    "propellant": "N2O4/UDMH",
    "thrustAtLiftoffKn": 10470,
    "status": "Active",
    "firstFlightYear": 2001,
    "totalLaunches": 115,
    "successfulLaunches": 105,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 65000000,
    "description": "Proton-M is a heavy-lift launch vehicle capable of placing large payloads into geostationary orbit.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Proton-M"
}'

# Angara A5
create_vehicle '{
    "name": "Angara A5",
    "family": "Angara",
    "variant": "A5",
    "fullName": "Angara A5",
    "country": {"id": 2},
    "manufacturer": "Khrunichev",
    "heightMeters": 55.4,
    "diameterMeters": 8.86,
    "massKg": 773000.0,
    "stages": 3,
    "payloadToLeoKg": 24500,
    "payloadToGtoKg": 7500,
    "firstStageEngines": "RD-191",
    "firstStageEngineCount": 5,
    "secondStageEngines": "RD-191",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 9610,
    "status": "Active",
    "firstFlightYear": 2014,
    "totalLaunches": 5,
    "successfulLaunches": 5,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 100000000,
    "description": "Angara is a new family of Russian space launch vehicles using environmentally friendly propellants.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Angara_(rocket_family)"
}'

# ==================== China Launch Vehicles ====================
echo ""
echo "🇨🇳 China Launch Vehicles"
echo "--------------------------"

# Long March 5
create_vehicle '{
    "name": "Long March 5",
    "family": "Long March",
    "variant": "5B",
    "fullName": "Long March 5B",
    "country": {"id": 3},
    "manufacturer": "CALT",
    "heightMeters": 53.7,
    "diameterMeters": 5.0,
    "massKg": 849000.0,
    "stages": 2,
    "payloadToLeoKg": 25000,
    "payloadToGtoKg": 14000,
    "firstStageEngines": "YF-77",
    "firstStageEngineCount": 2,
    "secondStageEngines": "YF-75D",
    "secondStageEngineCount": 2,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 10600,
    "status": "Active",
    "firstFlightYear": 2016,
    "totalLaunches": 14,
    "successfulLaunches": 13,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 100000000,
    "description": "Long March 5 is China largest rocket, used for heavy-lift missions including space station modules.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Long_March_5"
}'

# Long March 2F
create_vehicle '{
    "name": "Long March 2F",
    "family": "Long March",
    "variant": "2F",
    "fullName": "Long March 2F",
    "country": {"id": 3},
    "manufacturer": "CALT",
    "heightMeters": 58.3,
    "diameterMeters": 3.35,
    "massKg": 493000.0,
    "stages": 2,
    "payloadToLeoKg": 8400,
    "firstStageEngines": "YF-21C",
    "firstStageEngineCount": 4,
    "secondStageEngines": "YF-24E",
    "secondStageEngineCount": 1,
    "propellant": "N2O4/UDMH",
    "thrustAtLiftoffKn": 6040,
    "status": "Active",
    "firstFlightYear": 1999,
    "totalLaunches": 20,
    "successfulLaunches": 20,
    "reusable": false,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 70000000,
    "description": "Long March 2F is China human-rated launch vehicle for the Shenzhou crewed program and Tiangong space station.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Long_March_2F"
}'

# Long March 7
create_vehicle '{
    "name": "Long March 7",
    "family": "Long March",
    "variant": "7A",
    "fullName": "Long March 7A",
    "country": {"id": 3},
    "manufacturer": "CALT",
    "heightMeters": 60.1,
    "diameterMeters": 3.35,
    "massKg": 597000.0,
    "stages": 3,
    "payloadToLeoKg": 14000,
    "payloadToGtoKg": 7000,
    "firstStageEngines": "YF-100",
    "firstStageEngineCount": 2,
    "secondStageEngines": "YF-115",
    "secondStageEngineCount": 4,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 7200,
    "status": "Active",
    "firstFlightYear": 2016,
    "totalLaunches": 8,
    "successfulLaunches": 7,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 60000000,
    "description": "Long March 7 is a medium-lift rocket designed for cargo resupply missions to the Chinese space station.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Long_March_7"
}'

# ==================== Europe Launch Vehicles ====================
echo ""
echo "🇪🇺 European Launch Vehicles"
echo "-----------------------------"

# Ariane 6
create_vehicle '{
    "name": "Ariane 6",
    "family": "Ariane",
    "variant": "64",
    "fullName": "Ariane 64",
    "country": {"id": 4},
    "manufacturer": "ArianeGroup",
    "heightMeters": 63.0,
    "diameterMeters": 5.4,
    "massKg": 860000.0,
    "stages": 2,
    "payloadToLeoKg": 21650,
    "payloadToGtoKg": 11500,
    "firstStageEngines": "Vulcain 2.1",
    "firstStageEngineCount": 1,
    "secondStageEngines": "Vinci",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 8000,
    "status": "Active",
    "firstFlightYear": 2024,
    "totalLaunches": 2,
    "successfulLaunches": 2,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 115000000,
    "description": "Ariane 6 is ESA next-generation launcher, designed for flexible and cost-effective access to space.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Ariane_6"
}'

# Ariane 5
create_vehicle '{
    "name": "Ariane 5",
    "family": "Ariane",
    "variant": "ECA",
    "fullName": "Ariane 5 ECA",
    "country": {"id": 4},
    "manufacturer": "ArianeGroup",
    "heightMeters": 53.0,
    "diameterMeters": 5.4,
    "massKg": 777000.0,
    "stages": 2,
    "payloadToLeoKg": 21000,
    "payloadToGtoKg": 10500,
    "firstStageEngines": "Vulcain 2",
    "firstStageEngineCount": 1,
    "secondStageEngines": "HM-7B",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 15120,
    "status": "Retired",
    "firstFlightYear": 1996,
    "lastFlightYear": 2023,
    "totalLaunches": 117,
    "successfulLaunches": 112,
    "reusable": false,
    "humanRated": false,
    "active": false,
    "costPerLaunchUsd": 178000000,
    "description": "Ariane 5 was Europe flagship rocket for over 25 years, successfully launching the James Webb Space Telescope.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Ariane_5"
}'

# Vega-C
create_vehicle '{
    "name": "Vega-C",
    "family": "Vega",
    "variant": "C",
    "fullName": "Vega-C",
    "country": {"id": 4},
    "manufacturer": "Avio",
    "heightMeters": 35.0,
    "diameterMeters": 3.4,
    "massKg": 210000.0,
    "stages": 4,
    "payloadToLeoKg": 2300,
    "firstStageEngines": "P120C",
    "firstStageEngineCount": 1,
    "secondStageEngines": "Zefiro 40",
    "secondStageEngineCount": 1,
    "propellant": "Solid",
    "thrustAtLiftoffKn": 4500,
    "status": "Active",
    "firstFlightYear": 2022,
    "totalLaunches": 3,
    "successfulLaunches": 2,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 37000000,
    "description": "Vega-C is an upgraded version of the Vega rocket for small to medium payload missions.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Vega_(rocket)"
}'

# ==================== Japan Launch Vehicles ====================
echo ""
echo "🇯🇵 Japan Launch Vehicles"
echo "--------------------------"

# H3
create_vehicle '{
    "name": "H3",
    "family": "H-Series",
    "variant": "24",
    "fullName": "H3-24",
    "country": {"id": 5},
    "manufacturer": "Mitsubishi Heavy Industries",
    "heightMeters": 63.0,
    "diameterMeters": 5.2,
    "massKg": 574000.0,
    "stages": 2,
    "payloadToLeoKg": 6500,
    "payloadToGtoKg": 4000,
    "firstStageEngines": "LE-9",
    "firstStageEngineCount": 3,
    "secondStageEngines": "LE-5B-3",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 8400,
    "status": "Active",
    "firstFlightYear": 2024,
    "totalLaunches": 4,
    "successfulLaunches": 3,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 51000000,
    "description": "H3 is Japan newest flagship rocket, designed to reduce launch costs while maintaining reliability.",
    "wikiUrl": "https://en.wikipedia.org/wiki/H3_(rocket)"
}'

# H-IIA
create_vehicle '{
    "name": "H-IIA",
    "family": "H-Series",
    "variant": "202",
    "fullName": "H-IIA 202",
    "country": {"id": 5},
    "manufacturer": "Mitsubishi Heavy Industries",
    "heightMeters": 53.0,
    "diameterMeters": 4.0,
    "massKg": 285000.0,
    "stages": 2,
    "payloadToLeoKg": 10000,
    "payloadToGtoKg": 4100,
    "firstStageEngines": "LE-7A",
    "firstStageEngineCount": 1,
    "secondStageEngines": "LE-5B",
    "secondStageEngineCount": 1,
    "propellant": "LH2/LOX",
    "thrustAtLiftoffKn": 2196,
    "status": "Retired",
    "firstFlightYear": 2001,
    "lastFlightYear": 2024,
    "totalLaunches": 50,
    "successfulLaunches": 49,
    "reusable": false,
    "humanRated": false,
    "active": false,
    "costPerLaunchUsd": 90000000,
    "description": "H-IIA was Japan primary launch vehicle for two decades with an exceptional 98% success rate.",
    "wikiUrl": "https://en.wikipedia.org/wiki/H-IIA"
}'

# Epsilon
create_vehicle '{
    "name": "Epsilon",
    "family": "Epsilon",
    "variant": "S",
    "fullName": "Epsilon S",
    "country": {"id": 5},
    "manufacturer": "IHI Corporation",
    "heightMeters": 27.0,
    "diameterMeters": 2.5,
    "massKg": 96000.0,
    "stages": 3,
    "payloadToLeoKg": 1500,
    "firstStageEngines": "SRB-3",
    "firstStageEngineCount": 1,
    "propellant": "Solid",
    "thrustAtLiftoffKn": 2700,
    "status": "Active",
    "firstFlightYear": 2013,
    "totalLaunches": 6,
    "successfulLaunches": 5,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 38000000,
    "description": "Epsilon is a Japanese solid-fuel rocket designed for small satellite launches with autonomous checkout.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Epsilon_(rocket)"
}'

# ==================== India Launch Vehicles ====================
echo ""
echo "🇮🇳 India Launch Vehicles"
echo "--------------------------"

# GSLV Mk III (LVM3)
create_vehicle '{
    "name": "LVM3",
    "family": "GSLV",
    "variant": "Mk III",
    "fullName": "LVM3 (GSLV Mk III)",
    "country": {"id": 6},
    "manufacturer": "ISRO",
    "heightMeters": 43.4,
    "diameterMeters": 4.0,
    "massKg": 640000.0,
    "stages": 3,
    "payloadToLeoKg": 10000,
    "payloadToGtoKg": 4000,
    "firstStageEngines": "S200",
    "firstStageEngineCount": 2,
    "secondStageEngines": "Vikas",
    "secondStageEngineCount": 2,
    "propellant": "Solid/UDMH/LH2",
    "thrustAtLiftoffKn": 10200,
    "status": "Active",
    "firstFlightYear": 2017,
    "totalLaunches": 8,
    "successfulLaunches": 8,
    "reusable": false,
    "humanRated": true,
    "active": true,
    "costPerLaunchUsd": 50000000,
    "description": "LVM3 is India heaviest rocket, used for Chandrayaan missions and future crewed Gaganyaan flights.",
    "wikiUrl": "https://en.wikipedia.org/wiki/LVM3"
}'

# PSLV
create_vehicle '{
    "name": "PSLV",
    "family": "PSLV",
    "variant": "XL",
    "fullName": "PSLV-XL",
    "country": {"id": 6},
    "manufacturer": "ISRO",
    "heightMeters": 44.4,
    "diameterMeters": 2.8,
    "massKg": 320000.0,
    "stages": 4,
    "payloadToLeoKg": 3800,
    "payloadToGtoKg": 1425,
    "firstStageEngines": "S139",
    "firstStageEngineCount": 1,
    "propellant": "Solid/UDMH",
    "thrustAtLiftoffKn": 5040,
    "status": "Active",
    "firstFlightYear": 1993,
    "totalLaunches": 60,
    "successfulLaunches": 57,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 21000000,
    "description": "PSLV is India most reliable workhorse rocket, famous for launching 104 satellites in a single mission.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Polar_Satellite_Launch_Vehicle"
}'

# ==================== New Zealand Launch Vehicles ====================
echo ""
echo "🇳🇿 New Zealand Launch Vehicles"
echo "--------------------------------"

# Electron
create_vehicle '{
    "name": "Electron",
    "family": "Electron",
    "variant": "Standard",
    "fullName": "Electron",
    "country": {"id": 15},
    "manufacturer": "Rocket Lab",
    "heightMeters": 18.0,
    "diameterMeters": 1.2,
    "massKg": 13000.0,
    "stages": 2,
    "payloadToLeoKg": 300,
    "firstStageEngines": "Rutherford",
    "firstStageEngineCount": 9,
    "secondStageEngines": "Rutherford Vacuum",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 224,
    "status": "Active",
    "firstFlightYear": 2017,
    "totalLaunches": 50,
    "successfulLaunches": 46,
    "reusable": true,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 7500000,
    "description": "Electron is a small-lift rocket optimized for dedicated small satellite launches with rapid turnaround.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Electron_(rocket)"
}'

# Neutron
create_vehicle '{
    "name": "Neutron",
    "family": "Neutron",
    "variant": "Standard",
    "fullName": "Neutron",
    "country": {"id": 15},
    "manufacturer": "Rocket Lab",
    "heightMeters": 40.0,
    "diameterMeters": 7.0,
    "massKg": 480000.0,
    "stages": 2,
    "payloadToLeoKg": 13000,
    "payloadToGtoKg": 1500,
    "firstStageEngines": "Archimedes",
    "firstStageEngineCount": 7,
    "secondStageEngines": "Archimedes Vacuum",
    "secondStageEngineCount": 1,
    "propellant": "CH4/LOX",
    "thrustAtLiftoffKn": 6300,
    "status": "In Development",
    "firstFlightYear": 2025,
    "totalLaunches": 0,
    "successfulLaunches": 0,
    "reusable": true,
    "humanRated": true,
    "active": false,
    "costPerLaunchUsd": 50000000,
    "description": "Neutron is Rocket Lab upcoming medium-lift reusable rocket designed for constellation deployment and crew missions.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Rocket_Lab_Neutron"
}'

# ==================== South Korea Launch Vehicles ====================
echo ""
echo "🇰🇷 South Korea Launch Vehicles"
echo "--------------------------------"

# KSLV-II Nuri
create_vehicle '{
    "name": "Nuri",
    "family": "KSLV",
    "variant": "II",
    "fullName": "KSLV-II Nuri",
    "country": {"id": 7},
    "manufacturer": "KARI",
    "heightMeters": 47.2,
    "diameterMeters": 3.5,
    "massKg": 200000.0,
    "stages": 3,
    "payloadToLeoKg": 1500,
    "firstStageEngines": "KRE-075",
    "firstStageEngineCount": 4,
    "secondStageEngines": "KRE-075",
    "secondStageEngineCount": 1,
    "propellant": "RP-1/LOX",
    "thrustAtLiftoffKn": 2700,
    "status": "Active",
    "firstFlightYear": 2021,
    "totalLaunches": 4,
    "successfulLaunches": 3,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 45000000,
    "description": "Nuri is South Korea first domestically developed orbital launch vehicle.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Nuri_(rocket)"
}'

# ==================== Israel Launch Vehicles ====================
echo ""
echo "🇮🇱 Israel Launch Vehicles"
echo "---------------------------"

# Shavit
create_vehicle '{
    "name": "Shavit",
    "family": "Shavit",
    "variant": "2",
    "fullName": "Shavit 2",
    "country": {"id": 8},
    "manufacturer": "IAI",
    "heightMeters": 20.0,
    "diameterMeters": 1.35,
    "massKg": 30000.0,
    "stages": 4,
    "payloadToLeoKg": 350,
    "firstStageEngines": "Solid Motor",
    "firstStageEngineCount": 1,
    "propellant": "Solid",
    "thrustAtLiftoffKn": 730,
    "status": "Active",
    "firstFlightYear": 1988,
    "totalLaunches": 11,
    "successfulLaunches": 9,
    "reusable": false,
    "humanRated": false,
    "active": true,
    "costPerLaunchUsd": 20000000,
    "description": "Shavit is Israel indigenous space launch vehicle, uniquely launching westward to avoid overflying hostile neighbors.",
    "wikiUrl": "https://en.wikipedia.org/wiki/Shavit"
}'

echo ""
echo "================================================"
echo "✅ Launch vehicle seeding complete!"

# Summary
echo ""
echo "📊 Summary:"
curl -s "$ENDPOINT" | jq 'length' | xargs -I {} echo "Total launch vehicles created: {}"
