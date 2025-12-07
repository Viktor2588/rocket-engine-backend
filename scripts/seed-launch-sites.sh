#!/bin/bash

# Seed Launch Sites Data
# Run this after seed-countries.sh

BASE_URL="${API_BASE_URL:-http://localhost:8080}"
API="$BASE_URL/api/launch-sites"

echo "🚀 Seeding Launch Sites..."
echo "Using API: $API"

# Function to create a launch site
create_site() {
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
        echo "❌ Failed to create launch site: $http_code"
        echo "$body"
    fi
}

echo ""
echo "🇺🇸 Creating US launch sites..."

# Kennedy Space Center
create_site '{
    "name": "Kennedy Space Center",
    "shortName": "KSC",
    "country": {"id": 1},
    "operator": "NASA",
    "latitude": 28.5729,
    "longitude": -80.6490,
    "region": "Florida",
    "nearestCity": "Cape Canaveral",
    "status": "OPERATIONAL",
    "establishedYear": 1962,
    "firstLaunchYear": 1967,
    "totalLaunches": 185,
    "successfulLaunches": 180,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 2,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": true,
    "supportsPolar": false,
    "supportsSso": false,
    "supportsInterplanetary": true,
    "hasLandingFacilities": true,
    "minInclinationDeg": 28.5,
    "maxInclinationDeg": 57.0,
    "description": "Primary American launch site for human spaceflight",
    "historicalSignificance": "Launch site for Apollo Moon missions, Space Shuttle, and Artemis",
    "notableLaunches": "Apollo 11, STS-1, Artemis I"
}'

# Cape Canaveral Space Force Station
create_site '{
    "name": "Cape Canaveral Space Force Station",
    "shortName": "CCSFS",
    "alternateName": "Cape Canaveral Air Force Station",
    "country": {"id": 1},
    "operator": "US Space Force",
    "latitude": 28.4889,
    "longitude": -80.5778,
    "region": "Florida",
    "nearestCity": "Cape Canaveral",
    "status": "OPERATIONAL",
    "establishedYear": 1950,
    "firstLaunchYear": 1950,
    "totalLaunches": 900,
    "successfulLaunches": 850,
    "numberOfLaunchPads": 4,
    "activeLaunchPads": 3,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": true,
    "supportsPolar": false,
    "supportsInterplanetary": true,
    "hasLandingFacilities": true,
    "description": "Primary US military and commercial launch facility",
    "historicalSignificance": "Americas first spaceport, launched Explorer 1 and Mercury missions"
}'

# Vandenberg Space Force Base
create_site '{
    "name": "Vandenberg Space Force Base",
    "shortName": "VSFB",
    "alternateName": "Vandenberg AFB",
    "country": {"id": 1},
    "operator": "US Space Force",
    "latitude": 34.7420,
    "longitude": -120.5724,
    "region": "California",
    "nearestCity": "Lompoc",
    "status": "OPERATIONAL",
    "establishedYear": 1957,
    "firstLaunchYear": 1959,
    "totalLaunches": 700,
    "successfulLaunches": 670,
    "numberOfLaunchPads": 3,
    "activeLaunchPads": 2,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": false,
    "supportsPolar": true,
    "supportsSso": true,
    "supportsInterplanetary": false,
    "hasLandingFacilities": true,
    "minInclinationDeg": 56.0,
    "maxInclinationDeg": 104.0,
    "description": "Primary US launch site for polar and sun-synchronous orbits",
    "historicalSignificance": "First polar orbit launch, military satellite launches"
}'

# SpaceX Starbase
create_site '{
    "name": "SpaceX Starbase",
    "shortName": "Starbase",
    "alternateName": "Boca Chica",
    "country": {"id": 1},
    "operator": "SpaceX",
    "latitude": 25.9972,
    "longitude": -97.1571,
    "region": "Texas",
    "nearestCity": "Brownsville",
    "status": "OPERATIONAL",
    "establishedYear": 2014,
    "firstLaunchYear": 2019,
    "totalLaunches": 10,
    "successfulLaunches": 5,
    "numberOfLaunchPads": 1,
    "activeLaunchPads": 1,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsGeo": true,
    "supportsInterplanetary": true,
    "hasLandingFacilities": true,
    "hasManufacturing": true,
    "description": "SpaceX Starship development and launch facility",
    "historicalSignificance": "First Starship orbital launch attempts"
}'

echo ""
echo "🇷🇺 Creating Russian launch sites..."

# Baikonur Cosmodrome
create_site '{
    "name": "Baikonur Cosmodrome",
    "shortName": "Baikonur",
    "country": {"id": 2},
    "operator": "Roscosmos",
    "latitude": 45.9650,
    "longitude": 63.3050,
    "region": "Kazakhstan",
    "nearestCity": "Baikonur",
    "status": "OPERATIONAL",
    "establishedYear": 1955,
    "firstLaunchYear": 1957,
    "totalLaunches": 1500,
    "successfulLaunches": 1400,
    "numberOfLaunchPads": 15,
    "activeLaunchPads": 5,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": true,
    "supportsPolar": false,
    "supportsInterplanetary": true,
    "minInclinationDeg": 51.6,
    "maxInclinationDeg": 65.0,
    "description": "Worlds first spaceport, launched Sputnik and Vostok 1",
    "historicalSignificance": "Sputnik 1, Vostok 1, all crewed Soyuz launches",
    "notableLaunches": "Sputnik 1, Vostok 1, ISS assembly missions"
}'

# Plesetsk Cosmodrome
create_site '{
    "name": "Plesetsk Cosmodrome",
    "shortName": "Plesetsk",
    "country": {"id": 2},
    "operator": "Russian Space Forces",
    "latitude": 62.9271,
    "longitude": 40.5777,
    "region": "Arkhangelsk Oblast",
    "nearestCity": "Mirny",
    "status": "OPERATIONAL",
    "establishedYear": 1957,
    "firstLaunchYear": 1966,
    "totalLaunches": 1600,
    "successfulLaunches": 1550,
    "numberOfLaunchPads": 6,
    "activeLaunchPads": 4,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsPolar": true,
    "supportsSso": true,
    "supportsInterplanetary": false,
    "minInclinationDeg": 62.8,
    "maxInclinationDeg": 98.7,
    "description": "Primary Russian military and commercial launch site",
    "historicalSignificance": "Most active launch site by number of launches"
}'

# Vostochny Cosmodrome
create_site '{
    "name": "Vostochny Cosmodrome",
    "shortName": "Vostochny",
    "country": {"id": 2},
    "operator": "Roscosmos",
    "latitude": 51.8844,
    "longitude": 128.3333,
    "region": "Amur Oblast",
    "nearestCity": "Tsiolkovsky",
    "status": "OPERATIONAL",
    "establishedYear": 2012,
    "firstLaunchYear": 2016,
    "totalLaunches": 15,
    "successfulLaunches": 14,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 1,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsGeo": true,
    "supportsInterplanetary": true,
    "description": "New Russian civilian spaceport on Russian territory",
    "historicalSignificance": "First launch site built by Russia after Soviet era"
}'

echo ""
echo "🇨🇳 Creating Chinese launch sites..."

# Jiuquan Satellite Launch Center
create_site '{
    "name": "Jiuquan Satellite Launch Center",
    "shortName": "JSLC",
    "country": {"id": 3},
    "operator": "CNSA",
    "latitude": 40.9606,
    "longitude": 100.2911,
    "region": "Inner Mongolia",
    "nearestCity": "Jiuquan",
    "status": "OPERATIONAL",
    "establishedYear": 1958,
    "firstLaunchYear": 1970,
    "totalLaunches": 150,
    "successfulLaunches": 140,
    "numberOfLaunchPads": 4,
    "activeLaunchPads": 3,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": false,
    "supportsSso": true,
    "supportsInterplanetary": false,
    "description": "Chinas primary crewed spaceflight launch site",
    "historicalSignificance": "First Chinese satellite, all crewed Shenzhou launches",
    "notableLaunches": "Dongfanghong 1, Shenzhou missions"
}'

# Xichang Satellite Launch Center
create_site '{
    "name": "Xichang Satellite Launch Center",
    "shortName": "XSLC",
    "country": {"id": 3},
    "operator": "CNSA",
    "latitude": 28.2467,
    "longitude": 102.0267,
    "region": "Sichuan",
    "nearestCity": "Xichang",
    "status": "OPERATIONAL",
    "establishedYear": 1984,
    "firstLaunchYear": 1984,
    "totalLaunches": 180,
    "successfulLaunches": 170,
    "numberOfLaunchPads": 3,
    "activeLaunchPads": 2,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsGeo": true,
    "supportsInterplanetary": true,
    "description": "Primary Chinese launch site for GEO and lunar missions",
    "historicalSignificance": "Chang e lunar missions, BeiDou constellation",
    "notableLaunches": "Chang e 1-4"
}'

# Wenchang Spacecraft Launch Site
create_site '{
    "name": "Wenchang Spacecraft Launch Site",
    "shortName": "WSLC",
    "country": {"id": 3},
    "operator": "CNSA",
    "latitude": 19.6145,
    "longitude": 110.9510,
    "region": "Hainan",
    "nearestCity": "Wenchang",
    "status": "OPERATIONAL",
    "establishedYear": 2009,
    "firstLaunchYear": 2016,
    "totalLaunches": 30,
    "successfulLaunches": 29,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 2,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsGeo": true,
    "supportsInterplanetary": true,
    "hasLandingFacilities": false,
    "description": "Chinas newest and most southern launch site for heavy-lift vehicles",
    "historicalSignificance": "Long March 5, Tianwen-1, Chang e 5",
    "notableLaunches": "Tianwen-1, Chang e 5, Tiangong modules"
}'

echo ""
echo "🇪🇺 Creating European launch sites..."

# Guiana Space Centre
create_site '{
    "name": "Guiana Space Centre",
    "shortName": "CSG",
    "alternateName": "Kourou",
    "country": {"id": 7},
    "operator": "CNES/Arianespace",
    "latitude": 5.2322,
    "longitude": -52.7693,
    "region": "French Guiana",
    "nearestCity": "Kourou",
    "status": "OPERATIONAL",
    "establishedYear": 1968,
    "firstLaunchYear": 1970,
    "totalLaunches": 300,
    "successfulLaunches": 285,
    "numberOfLaunchPads": 3,
    "activeLaunchPads": 2,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": true,
    "supportsPolar": true,
    "supportsSso": true,
    "supportsInterplanetary": true,
    "minInclinationDeg": 5.2,
    "maxInclinationDeg": 100.0,
    "description": "ESA spaceport, ideal equatorial location for GEO launches",
    "historicalSignificance": "All Ariane launches, JWST launch",
    "notableLaunches": "Ariane 5, Vega, JWST"
}'

echo ""
echo "🇮🇳 Creating Indian launch sites..."

# Satish Dhawan Space Centre
create_site '{
    "name": "Satish Dhawan Space Centre",
    "shortName": "SDSC",
    "alternateName": "SHAR",
    "country": {"id": 4},
    "operator": "ISRO",
    "latitude": 13.7199,
    "longitude": 80.2304,
    "region": "Andhra Pradesh",
    "nearestCity": "Sriharikota",
    "status": "OPERATIONAL",
    "establishedYear": 1971,
    "firstLaunchYear": 1979,
    "totalLaunches": 90,
    "successfulLaunches": 82,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 2,
    "humanRatedCapable": true,
    "supportsLeo": true,
    "supportsMeo": true,
    "supportsGeo": true,
    "supportsPolar": true,
    "supportsSso": true,
    "supportsInterplanetary": true,
    "description": "Indias primary spaceport for orbital launches",
    "historicalSignificance": "Chandrayaan, Mangalyaan missions",
    "notableLaunches": "Chandrayaan-1, Mangalyaan, Chandrayaan-3"
}'

echo ""
echo "🇯🇵 Creating Japanese launch sites..."

# Tanegashima Space Center
create_site '{
    "name": "Tanegashima Space Center",
    "shortName": "TNSC",
    "country": {"id": 5},
    "operator": "JAXA",
    "latitude": 30.3975,
    "longitude": 130.9706,
    "region": "Kagoshima",
    "nearestCity": "Tanegashima",
    "status": "OPERATIONAL",
    "establishedYear": 1969,
    "firstLaunchYear": 1975,
    "totalLaunches": 90,
    "successfulLaunches": 85,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 2,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsGeo": true,
    "supportsInterplanetary": true,
    "description": "Japans largest launch facility",
    "historicalSignificance": "All H-IIA/H-IIB launches, Hayabusa missions",
    "notableLaunches": "Hayabusa, Hayabusa2, SLIM"
}'

echo ""
echo "🌍 Creating other launch sites..."

# Rocket Lab Launch Complex 1
create_site '{
    "name": "Rocket Lab Launch Complex 1",
    "shortName": "LC-1",
    "alternateName": "Mahia",
    "country": {"id": 16},
    "operator": "Rocket Lab",
    "latitude": -39.2615,
    "longitude": 177.8649,
    "region": "Hawkes Bay",
    "nearestCity": "Mahia",
    "status": "OPERATIONAL",
    "establishedYear": 2016,
    "firstLaunchYear": 2017,
    "totalLaunches": 45,
    "successfulLaunches": 40,
    "numberOfLaunchPads": 2,
    "activeLaunchPads": 2,
    "humanRatedCapable": false,
    "supportsLeo": true,
    "supportsSso": true,
    "supportsPolar": true,
    "hasLandingFacilities": false,
    "description": "Worlds first private orbital launch facility",
    "historicalSignificance": "First orbital launch from New Zealand"
}'

echo ""
echo "✅ Launch sites seeding complete!"
echo ""
echo "📊 Summary of seeded launch sites:"
curl -s "$API/statistics" | head -20

echo ""
echo "🔗 Test the API:"
echo "  All sites: $API"
echo "  Active sites: $API/active"
echo "  Human-rated: $API/human-rated"
echo "  By country (USA): $API/by-country-code/USA"
