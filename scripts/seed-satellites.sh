#!/bin/bash

# Seed Satellites Data
# Run this after seed-countries.sh

BASE_URL="${API_BASE_URL:-http://localhost:8080}"
API="$BASE_URL/api/satellites"

echo "🛰️ Seeding Satellites..."
echo "Using API: $API"

# Function to create a satellite
create_satellite() {
    local json="$1"
    response=$(curl -s -w "\n%{http_code}" -X POST "$API" \
        -H "Content-Type: application/json" \
        -d "$json")

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" -eq 201 ]; then
        name=$(echo "$body" | grep -o '"name":"[^"]*"' | head -1 | cut -d'"' -f4)
        echo "✅ Created: $name"
    else
        echo "❌ Failed to create satellite: $http_code"
        echo "$body"
    fi
}

echo ""
echo "🌍 Creating Space Stations..."

# ISS
create_satellite '{
    "name": "International Space Station",
    "alternateName": "ISS",
    "noradId": "25544",
    "cosparId": "1998-067A",
    "country": {"id": 1},
    "operator": "NASA/Roscosmos/ESA/JAXA/CSA",
    "manufacturer": "Boeing/RSC Energia/ESA/JAXA",
    "satelliteType": "SPACE_STATION",
    "status": "OPERATIONAL",
    "orbitType": "ISS_ORBIT",
    "launchDate": "1998-11-20",
    "operationalDate": "2000-11-02",
    "massKg": 420000,
    "powerWatts": 75000,
    "altitudeKm": 420,
    "inclinationDeg": 51.6,
    "periodMinutes": 92.9,
    "purpose": "Microgravity research laboratory and human spaceflight",
    "payloadDescription": "Multiple research modules, external platforms"
}'

# Tiangong
create_satellite '{
    "name": "Tiangong Space Station",
    "alternateName": "Chinese Space Station",
    "noradId": "48274",
    "cosparId": "2021-035A",
    "country": {"id": 3},
    "operator": "CNSA",
    "manufacturer": "CAST",
    "satelliteType": "SPACE_STATION",
    "status": "OPERATIONAL",
    "orbitType": "LEO",
    "launchDate": "2021-04-29",
    "operationalDate": "2022-11-03",
    "massKg": 100000,
    "powerWatts": 100000,
    "altitudeKm": 400,
    "inclinationDeg": 41.5,
    "periodMinutes": 92.0,
    "purpose": "Chinese national space laboratory",
    "payloadDescription": "Tianhe core module, Wentian, Mengtian lab modules"
}'

echo ""
echo "📡 Creating Navigation Satellites..."

# GPS satellite
create_satellite '{
    "name": "GPS III SV01",
    "alternateName": "Vespucci",
    "noradId": "43873",
    "cosparId": "2018-109A",
    "country": {"id": 1},
    "operator": "US Space Force",
    "manufacturer": "Lockheed Martin",
    "satelliteType": "GPS",
    "status": "OPERATIONAL",
    "orbitType": "NAVIGATION_MEO",
    "launchDate": "2018-12-23",
    "operationalDate": "2020-01-13",
    "expectedLifespanYears": 15,
    "massKg": 3880,
    "powerWatts": 4480,
    "altitudeKm": 20200,
    "inclinationDeg": 55.0,
    "periodMinutes": 718,
    "constellation": "GPS",
    "isPartOfConstellation": true,
    "purpose": "Global navigation satellite system"
}'

# GLONASS
create_satellite '{
    "name": "GLONASS-M No.761",
    "noradId": "43508",
    "country": {"id": 2},
    "operator": "Roscosmos",
    "manufacturer": "ISS Reshetnev",
    "satelliteType": "GLONASS",
    "status": "OPERATIONAL",
    "orbitType": "NAVIGATION_MEO",
    "launchDate": "2018-06-17",
    "expectedLifespanYears": 7,
    "massKg": 1415,
    "altitudeKm": 19140,
    "inclinationDeg": 64.8,
    "periodMinutes": 675,
    "constellation": "GLONASS",
    "isPartOfConstellation": true,
    "purpose": "Russian navigation satellite system"
}'

# Galileo
create_satellite '{
    "name": "Galileo FOC FM22",
    "noradId": "43567",
    "country": {"id": 7},
    "operator": "ESA/EU",
    "manufacturer": "OHB System",
    "satelliteType": "GALILEO",
    "status": "OPERATIONAL",
    "orbitType": "NAVIGATION_MEO",
    "launchDate": "2018-07-25",
    "expectedLifespanYears": 12,
    "massKg": 715,
    "powerWatts": 1500,
    "altitudeKm": 23222,
    "inclinationDeg": 56.0,
    "periodMinutes": 846,
    "constellation": "Galileo",
    "isPartOfConstellation": true,
    "purpose": "European navigation satellite system"
}'

# BeiDou
create_satellite '{
    "name": "BeiDou-3 M23",
    "noradId": "44543",
    "country": {"id": 3},
    "operator": "CNSA",
    "manufacturer": "CAST",
    "satelliteType": "BEIDOU",
    "status": "OPERATIONAL",
    "orbitType": "NAVIGATION_MEO",
    "launchDate": "2019-11-23",
    "expectedLifespanYears": 12,
    "massKg": 1014,
    "altitudeKm": 21528,
    "inclinationDeg": 55.0,
    "periodMinutes": 773,
    "constellation": "BeiDou",
    "isPartOfConstellation": true,
    "purpose": "Chinese navigation satellite system"
}'

echo ""
echo "🌐 Creating Internet Constellation Satellites..."

# Starlink
create_satellite '{
    "name": "Starlink-1007",
    "noradId": "44713",
    "country": {"id": 1},
    "operator": "SpaceX",
    "manufacturer": "SpaceX",
    "satelliteType": "INTERNET_CONSTELLATION",
    "status": "OPERATIONAL",
    "orbitType": "LEO",
    "launchDate": "2020-01-07",
    "expectedLifespanYears": 5,
    "massKg": 260,
    "altitudeKm": 550,
    "inclinationDeg": 53.0,
    "periodMinutes": 95.6,
    "constellation": "Starlink",
    "isPartOfConstellation": true,
    "constellationGeneration": "v1.0",
    "purpose": "Global broadband internet coverage",
    "frequencyBands": "Ku-band, Ka-band"
}'

# OneWeb
create_satellite '{
    "name": "OneWeb-0012",
    "noradId": "45131",
    "country": {"id": 6},
    "operator": "OneWeb",
    "manufacturer": "Airbus Defence and Space",
    "satelliteType": "INTERNET_CONSTELLATION",
    "status": "OPERATIONAL",
    "orbitType": "LEO",
    "launchDate": "2020-02-07",
    "expectedLifespanYears": 5,
    "massKg": 150,
    "altitudeKm": 1200,
    "inclinationDeg": 87.9,
    "periodMinutes": 109.4,
    "constellation": "OneWeb",
    "isPartOfConstellation": true,
    "purpose": "Global internet coverage focusing on remote areas"
}'

echo ""
echo "🔭 Creating Science Satellites..."

# Hubble Space Telescope
create_satellite '{
    "name": "Hubble Space Telescope",
    "alternateName": "HST",
    "noradId": "20580",
    "cosparId": "1990-037B",
    "country": {"id": 1},
    "operator": "NASA/ESA",
    "manufacturer": "Lockheed Martin",
    "satelliteType": "ASTRONOMY",
    "status": "OPERATIONAL",
    "orbitType": "LEO",
    "launchDate": "1990-04-24",
    "operationalDate": "1990-05-20",
    "massKg": 11110,
    "powerWatts": 2800,
    "altitudeKm": 540,
    "inclinationDeg": 28.5,
    "periodMinutes": 95.4,
    "purpose": "Optical/UV space telescope for astronomical observation",
    "payloadDescription": "2.4m primary mirror, WFC3, COS, ACS, STIS instruments"
}'

# James Webb Space Telescope
create_satellite '{
    "name": "James Webb Space Telescope",
    "alternateName": "JWST",
    "noradId": "50463",
    "cosparId": "2021-130A",
    "country": {"id": 1},
    "operator": "NASA/ESA/CSA",
    "manufacturer": "Northrop Grumman",
    "satelliteType": "ASTRONOMY",
    "status": "OPERATIONAL",
    "orbitType": "L2",
    "launchDate": "2021-12-25",
    "operationalDate": "2022-07-12",
    "massKg": 6161,
    "powerWatts": 2000,
    "altitudeKm": 1500000,
    "purpose": "Infrared space telescope for deep universe observation",
    "payloadDescription": "6.5m segmented gold-coated beryllium primary mirror, NIRCam, NIRSpec, MIRI, FGS/NIRISS"
}'

# Chandra X-ray Observatory
create_satellite '{
    "name": "Chandra X-ray Observatory",
    "alternateName": "CXO",
    "noradId": "25867",
    "cosparId": "1999-040B",
    "country": {"id": 1},
    "operator": "NASA",
    "manufacturer": "TRW",
    "satelliteType": "ASTRONOMY",
    "status": "OPERATIONAL",
    "orbitType": "HEO",
    "launchDate": "1999-07-23",
    "operationalDate": "1999-08-12",
    "massKg": 4790,
    "powerWatts": 2350,
    "apogeeKm": 133000,
    "perigeeKm": 16000,
    "inclinationDeg": 28.5,
    "periodMinutes": 3854,
    "purpose": "X-ray astronomy observatory"
}'

echo ""
echo "🌍 Creating Earth Observation Satellites..."

# Landsat 9
create_satellite '{
    "name": "Landsat 9",
    "noradId": "49260",
    "cosparId": "2021-088A",
    "country": {"id": 1},
    "operator": "NASA/USGS",
    "manufacturer": "Northrop Grumman",
    "satelliteType": "EARTH_OBSERVATION",
    "status": "OPERATIONAL",
    "orbitType": "SSO",
    "launchDate": "2021-09-27",
    "operationalDate": "2022-02-10",
    "expectedLifespanYears": 5,
    "massKg": 2711,
    "powerWatts": 1550,
    "altitudeKm": 705,
    "inclinationDeg": 98.2,
    "periodMinutes": 98.9,
    "constellation": "Landsat",
    "isPartOfConstellation": true,
    "purpose": "Earth surface monitoring and land use mapping",
    "payloadDescription": "OLI-2, TIRS-2 instruments"
}'

# Sentinel-2A
create_satellite '{
    "name": "Sentinel-2A",
    "noradId": "40697",
    "cosparId": "2015-028A",
    "country": {"id": 7},
    "operator": "ESA",
    "manufacturer": "Airbus Defence and Space",
    "satelliteType": "EARTH_OBSERVATION",
    "status": "OPERATIONAL",
    "orbitType": "SSO",
    "launchDate": "2015-06-23",
    "expectedLifespanYears": 7,
    "massKg": 1140,
    "powerWatts": 1700,
    "altitudeKm": 786,
    "inclinationDeg": 98.5,
    "periodMinutes": 100.6,
    "constellation": "Copernicus",
    "isPartOfConstellation": true,
    "purpose": "High-resolution optical imaging for land monitoring"
}'

echo ""
echo "🛡️ Creating Weather Satellites..."

# GOES-18
create_satellite '{
    "name": "GOES-18",
    "alternateName": "GOES-West",
    "noradId": "51850",
    "cosparId": "2022-021A",
    "country": {"id": 1},
    "operator": "NOAA",
    "manufacturer": "Lockheed Martin",
    "satelliteType": "WEATHER_GEO",
    "status": "OPERATIONAL",
    "orbitType": "GEO",
    "launchDate": "2022-03-01",
    "operationalDate": "2023-01-04",
    "expectedLifespanYears": 15,
    "massKg": 5192,
    "powerWatts": 5000,
    "altitudeKm": 35786,
    "geoLongitude": -137.2,
    "constellation": "GOES",
    "isPartOfConstellation": true,
    "purpose": "Western US weather monitoring and forecasting"
}'

# Himawari-9
create_satellite '{
    "name": "Himawari-9",
    "noradId": "41836",
    "cosparId": "2016-064A",
    "country": {"id": 5},
    "operator": "JMA",
    "manufacturer": "Mitsubishi Electric",
    "satelliteType": "WEATHER_GEO",
    "status": "OPERATIONAL",
    "orbitType": "GEO",
    "launchDate": "2016-11-02",
    "expectedLifespanYears": 15,
    "massKg": 3500,
    "altitudeKm": 35786,
    "geoLongitude": 140.7,
    "purpose": "Asia-Pacific weather monitoring"
}'

echo ""
echo "🛰️ Creating Communication Satellites..."

# Intelsat 40e
create_satellite '{
    "name": "Intelsat 40e",
    "noradId": "52911",
    "country": {"id": 1},
    "operator": "Intelsat",
    "manufacturer": "Maxar Technologies",
    "satelliteType": "COMMUNICATION",
    "status": "OPERATIONAL",
    "orbitType": "GEO",
    "launchDate": "2023-04-07",
    "expectedLifespanYears": 15,
    "massKg": 5960,
    "powerWatts": 25000,
    "altitudeKm": 35786,
    "geoLongitude": -91.0,
    "purpose": "Commercial telecommunications for Americas",
    "frequencyBands": "C-band, Ku-band"
}'

echo ""
echo "✅ Satellites seeding complete!"
echo ""
echo "📊 Summary of seeded satellites:"
curl -s "$API/statistics" | head -30

echo ""
echo "🔗 Test the API:"
echo "  All satellites: $API"
echo "  Active satellites: $API/active"
echo "  Space stations: $API/space-stations"
echo "  Navigation: $API/navigation"
echo "  Constellations: $API/constellations"
echo "  By country (USA): $API/by-country-code/USA"
