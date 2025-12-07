#!/bin/bash

# Seed Space Missions Data
# Run this after seed-countries.sh and seed-milestones.sh

BASE_URL="${API_BASE_URL:-http://localhost:8080}"
API="$BASE_URL/api/missions"

echo "🚀 Seeding Space Missions..."
echo "Using API: $API"

# Function to create a mission
create_mission() {
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
        echo "❌ Failed to create mission: $http_code"
        echo "$body"
    fi
}

echo ""
echo "📡 Creating historic Soviet/Russian missions..."

# Sputnik 1 - First artificial satellite
create_mission '{
    "name": "Sputnik 1",
    "missionDesignation": "PS-1",
    "country": {"id": 2},
    "operator": "Soviet Space Program",
    "launchVehicleName": "Sputnik 8K71PS",
    "launchDate": "1957-10-04",
    "endDate": "1958-01-04",
    "status": "COMPLETED",
    "missionType": "SATELLITE_DEPLOYMENT",
    "destination": "LEO",
    "crewed": false,
    "launchSite": "Baikonur Cosmodrome",
    "missionMassKg": 83.6,
    "isHistoricFirst": true,
    "historicFirstType": "First artificial satellite",
    "description": "First artificial Earth satellite, marking the start of the Space Age",
    "objectives": "Demonstrate satellite technology, study upper atmosphere propagation",
    "outcomes": "Successfully orbited Earth for 3 months, transmitted radio signals"
}'

# Vostok 1 - First human in space
create_mission '{
    "name": "Vostok 1",
    "missionDesignation": "Vostok 3KA-3",
    "country": {"id": 2},
    "operator": "Soviet Space Program",
    "launchVehicleName": "Vostok-K",
    "launchDate": "1961-04-12",
    "endDate": "1961-04-12",
    "status": "COMPLETED",
    "missionType": "CREWED_ORBITAL",
    "destination": "LEO",
    "crewed": true,
    "crewSize": 1,
    "crewNames": "Yuri Gagarin",
    "commander": "Yuri Gagarin",
    "launchSite": "Baikonur Cosmodrome",
    "missionMassKg": 4725,
    "isHistoricFirst": true,
    "historicFirstType": "First human in space",
    "description": "First human spaceflight, Yuri Gagarin completed one orbit of Earth",
    "objectives": "First crewed orbital flight",
    "outcomes": "108-minute flight, single orbit, successful landing",
    "durationDays": 0,
    "orbitsCompleted": 1
}'

# Voskhod 2 - First spacewalk
create_mission '{
    "name": "Voskhod 2",
    "missionDesignation": "Voskhod 3KD",
    "country": {"id": 2},
    "operator": "Soviet Space Program",
    "launchVehicleName": "Voskhod",
    "launchDate": "1965-03-18",
    "endDate": "1965-03-19",
    "status": "COMPLETED",
    "missionType": "CREWED_ORBITAL",
    "destination": "LEO",
    "crewed": true,
    "crewSize": 2,
    "crewNames": "Pavel Belyayev, Alexei Leonov",
    "commander": "Pavel Belyayev",
    "launchSite": "Baikonur Cosmodrome",
    "isHistoricFirst": true,
    "historicFirstType": "First spacewalk",
    "description": "First extravehicular activity (spacewalk) by Alexei Leonov",
    "objectives": "Conduct first EVA",
    "outcomes": "12-minute spacewalk, suit nearly failed due to expansion",
    "evaCount": 1,
    "evaDurationHours": 0.2
}'

# Luna 9 - First soft landing on Moon
create_mission '{
    "name": "Luna 9",
    "missionDesignation": "E-6M No.202",
    "country": {"id": 2},
    "operator": "Soviet Space Program",
    "launchVehicleName": "Molniya-M",
    "launchDate": "1966-01-31",
    "endDate": "1966-02-06",
    "status": "COMPLETED",
    "missionType": "LUNAR_LANDER",
    "destination": "LUNAR_SURFACE",
    "crewed": false,
    "launchSite": "Baikonur Cosmodrome",
    "missionMassKg": 1538,
    "isHistoricFirst": true,
    "historicFirstType": "First soft Moon landing",
    "description": "First spacecraft to achieve soft landing on the Moon and transmit photos",
    "objectives": "Soft land on Moon, return surface images",
    "outcomes": "Landed in Oceanus Procellarum, returned first close-up photos of lunar surface"
}'

# Venera 7 - First Venus landing
create_mission '{
    "name": "Venera 7",
    "missionDesignation": "4V-1 No.630",
    "country": {"id": 2},
    "operator": "Soviet Space Program",
    "launchVehicleName": "Molniya-M",
    "launchDate": "1970-08-17",
    "endDate": "1970-12-15",
    "status": "COMPLETED",
    "missionType": "VENUS_MISSION",
    "destination": "VENUS_SURFACE",
    "crewed": false,
    "launchSite": "Baikonur Cosmodrome",
    "missionMassKg": 1180,
    "isHistoricFirst": true,
    "historicFirstType": "First Venus surface landing",
    "description": "First spacecraft to successfully land on Venus and transmit data",
    "objectives": "Land on Venus surface, return atmospheric data",
    "outcomes": "Transmitted data for 23 minutes from Venus surface"
}'

echo ""
echo "🇺🇸 Creating historic American missions..."

# Explorer 1 - First US satellite
create_mission '{
    "name": "Explorer 1",
    "missionDesignation": "1958 Alpha 1",
    "country": {"id": 1},
    "operator": "NASA/JPL",
    "launchVehicleName": "Juno I",
    "launchDate": "1958-02-01",
    "endDate": "1970-03-31",
    "status": "COMPLETED",
    "missionType": "SATELLITE_DEPLOYMENT",
    "destination": "LEO",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 13.97,
    "isHistoricFirst": false,
    "description": "First American artificial satellite, discovered Van Allen radiation belts",
    "objectives": "Orbital research, cosmic ray detection",
    "outcomes": "Discovered Van Allen radiation belts, operated 111 days"
}'

# Mercury-Atlas 6 - First American orbital flight
create_mission '{
    "name": "Mercury-Atlas 6",
    "alternateName": "Friendship 7",
    "missionDesignation": "MA-6",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Atlas LV-3B",
    "launchDate": "1962-02-20",
    "endDate": "1962-02-20",
    "status": "COMPLETED",
    "missionType": "CREWED_ORBITAL",
    "destination": "LEO",
    "crewed": true,
    "crewSize": 1,
    "crewNames": "John Glenn",
    "commander": "John Glenn",
    "launchSite": "Cape Canaveral",
    "missionMassKg": 1352,
    "isHistoricFirst": true,
    "historicFirstType": "First American orbital flight",
    "description": "First American to orbit Earth",
    "objectives": "Achieve orbital spaceflight",
    "outcomes": "Three orbits, 4 hours 55 minutes flight time",
    "orbitsCompleted": 3
}'

# Gemini 4 - First American spacewalk
create_mission '{
    "name": "Gemini 4",
    "missionDesignation": "GT-4",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Titan II GLV",
    "launchDate": "1965-06-03",
    "endDate": "1965-06-07",
    "status": "COMPLETED",
    "missionType": "CREWED_ORBITAL",
    "destination": "LEO",
    "crewed": true,
    "crewSize": 2,
    "crewNames": "James McDivitt, Ed White",
    "commander": "James McDivitt",
    "launchSite": "Cape Canaveral",
    "isHistoricFirst": true,
    "historicFirstType": "First American spacewalk",
    "description": "First American EVA by Ed White",
    "objectives": "Extended duration flight, first US EVA",
    "outcomes": "23-minute EVA, 97 hours flight duration",
    "evaCount": 1,
    "evaDurationHours": 0.38
}'

# Apollo 8 - First humans to orbit the Moon
create_mission '{
    "name": "Apollo 8",
    "missionDesignation": "AS-503",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Saturn V",
    "launchDate": "1968-12-21",
    "endDate": "1968-12-27",
    "status": "COMPLETED",
    "missionType": "LUNAR_CREWED_ORBIT",
    "destination": "LUNAR_ORBIT",
    "crewed": true,
    "crewSize": 3,
    "crewNames": "Frank Borman, James Lovell, William Anders",
    "commander": "Frank Borman",
    "launchSite": "Kennedy Space Center",
    "missionMassKg": 28833,
    "isHistoricFirst": true,
    "historicFirstType": "First humans to orbit Moon",
    "description": "First crewed spacecraft to leave low Earth orbit and orbit the Moon",
    "objectives": "Lunar orbital mission, test CSM in lunar environment",
    "outcomes": "10 lunar orbits, famous Earthrise photo",
    "orbitsCompleted": 10,
    "maxDistanceFromEarthKm": 377349
}'

# Apollo 11 - First Moon landing
create_mission '{
    "name": "Apollo 11",
    "missionDesignation": "AS-506",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Saturn V",
    "launchDate": "1969-07-16",
    "endDate": "1969-07-24",
    "status": "COMPLETED",
    "missionType": "LUNAR_CREWED_LANDING",
    "destination": "LUNAR_SURFACE",
    "crewed": true,
    "crewSize": 3,
    "crewNames": "Neil Armstrong, Buzz Aldrin, Michael Collins",
    "commander": "Neil Armstrong",
    "launchSite": "Kennedy Space Center",
    "missionMassKg": 28801,
    "isHistoricFirst": true,
    "historicFirstType": "First human Moon landing",
    "description": "First crewed Moon landing, Neil Armstrong first human to walk on Moon",
    "objectives": "Land humans on Moon, return safely to Earth",
    "outcomes": "2 hours 31 minutes on lunar surface, 21.5 kg samples returned",
    "sampleReturnMassKg": 21.5,
    "evaCount": 1,
    "evaDurationHours": 2.52,
    "maxDistanceFromEarthKm": 389564
}'

# Pioneer 10 - First Jupiter flyby
create_mission '{
    "name": "Pioneer 10",
    "missionDesignation": "Pioneer F",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Atlas-Centaur",
    "launchDate": "1972-03-02",
    "endDate": "2003-01-23",
    "status": "COMPLETED",
    "missionType": "JUPITER_MISSION",
    "destination": "JUPITER_ORBIT",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 258,
    "isHistoricFirst": true,
    "historicFirstType": "First Jupiter flyby",
    "description": "First spacecraft to traverse asteroid belt and fly by Jupiter",
    "objectives": "Study Jupiter, interplanetary medium",
    "outcomes": "First close-up images of Jupiter, crossed asteroid belt",
    "maxDistanceFromEarthKm": 12000000000
}'

# Voyager 1 - Interstellar probe
create_mission '{
    "name": "Voyager 1",
    "missionDesignation": "Mariner Jupiter/Saturn A",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "Titan IIIE",
    "launchDate": "1977-09-05",
    "status": "ACTIVE",
    "missionType": "INTERSTELLAR_MISSION",
    "destination": "INTERSTELLAR",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 721.9,
    "isHistoricFirst": true,
    "historicFirstType": "First interstellar probe",
    "description": "Farthest human-made object from Earth, entered interstellar space",
    "objectives": "Study outer planets, interstellar medium",
    "outcomes": "Visited Jupiter, Saturn; crossed heliopause in 2012",
    "maxDistanceFromEarthKm": 24000000000
}'

# Mars Pathfinder - First Mars rover
create_mission '{
    "name": "Mars Pathfinder",
    "missionDesignation": "MESUR Pathfinder",
    "country": {"id": 1},
    "operator": "NASA/JPL",
    "launchVehicleName": "Delta II 7925",
    "launchDate": "1996-12-04",
    "endDate": "1997-09-27",
    "status": "COMPLETED",
    "missionType": "MARS_ROVER",
    "destination": "MARS_SURFACE",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 890,
    "isHistoricFirst": true,
    "historicFirstType": "First successful Mars rover",
    "description": "First successful Mars rover mission with Sojourner rover",
    "objectives": "Demonstrate low-cost Mars landing, rover technology",
    "outcomes": "83 days of operations, 16500 images, 8.5 million rock and soil spectra"
}'

# SpaceX Falcon 9 - First orbital rocket landing
create_mission '{
    "name": "Falcon 9 Flight 20",
    "alternateName": "ORBCOMM-2",
    "country": {"id": 1},
    "operator": "SpaceX",
    "launchVehicleName": "Falcon 9 v1.2",
    "launchDate": "2015-12-22",
    "status": "COMPLETED",
    "missionType": "SATELLITE_DEPLOYMENT",
    "destination": "LEO",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "isHistoricFirst": true,
    "historicFirstType": "First orbital rocket landing",
    "description": "First successful landing of orbital class rocket first stage",
    "objectives": "Deploy 11 ORBCOMM satellites, land first stage",
    "outcomes": "All satellites deployed, first stage landed at LZ-1"
}'

echo ""
echo "🇨🇳 Creating Chinese missions..."

# Dongfanghong 1 - First Chinese satellite
create_mission '{
    "name": "Dongfanghong 1",
    "alternateName": "DFH-1",
    "country": {"id": 3},
    "operator": "CNSA",
    "launchVehicleName": "Long March 1",
    "launchDate": "1970-04-24",
    "endDate": "1970-05-14",
    "status": "COMPLETED",
    "missionType": "SATELLITE_DEPLOYMENT",
    "destination": "LEO",
    "crewed": false,
    "launchSite": "Jiuquan Satellite Launch Center",
    "missionMassKg": 173,
    "isHistoricFirst": false,
    "description": "First Chinese satellite, making China the 5th country to launch own satellite",
    "objectives": "Demonstrate satellite capability, broadcast music",
    "outcomes": "Orbited successfully, broadcast The East is Red"
}'

# Shenzhou 5 - First Chinese crewed spaceflight
create_mission '{
    "name": "Shenzhou 5",
    "missionDesignation": "SZ-5",
    "country": {"id": 3},
    "operator": "CNSA",
    "launchVehicleName": "Long March 2F",
    "launchDate": "2003-10-15",
    "endDate": "2003-10-16",
    "status": "COMPLETED",
    "missionType": "CREWED_ORBITAL",
    "destination": "LEO",
    "crewed": true,
    "crewSize": 1,
    "crewNames": "Yang Liwei",
    "commander": "Yang Liwei",
    "launchSite": "Jiuquan Satellite Launch Center",
    "missionMassKg": 7840,
    "isHistoricFirst": false,
    "description": "First Chinese crewed spaceflight, making China 3rd country with independent human spaceflight",
    "objectives": "First Chinese crewed orbital flight",
    "outcomes": "14 orbits, 21 hours flight duration"
}'

# Chang e 4 - First far side landing
create_mission '{
    "name": "Chang''e 4",
    "missionDesignation": "CE-4",
    "country": {"id": 3},
    "operator": "CNSA",
    "launchVehicleName": "Long March 3B",
    "launchDate": "2018-12-07",
    "status": "ACTIVE",
    "missionType": "LUNAR_LANDER",
    "destination": "LUNAR_FAR_SIDE",
    "crewed": false,
    "launchSite": "Xichang Satellite Launch Center",
    "missionMassKg": 3780,
    "isHistoricFirst": true,
    "historicFirstType": "First far side Moon landing",
    "description": "First spacecraft to land on the far side of the Moon",
    "objectives": "Far side landing, rover exploration, radio astronomy",
    "outcomes": "Landed in Von Karman crater, Yutu-2 rover still operational"
}'

# Chang e 5 - First Chinese sample return
create_mission '{
    "name": "Chang''e 5",
    "missionDesignation": "CE-5",
    "country": {"id": 3},
    "operator": "CNSA",
    "launchVehicleName": "Long March 5",
    "launchDate": "2020-11-23",
    "endDate": "2020-12-16",
    "status": "COMPLETED",
    "missionType": "LUNAR_SAMPLE_RETURN",
    "destination": "LUNAR_SURFACE",
    "crewed": false,
    "launchSite": "Wenchang Space Launch Center",
    "missionMassKg": 8200,
    "isHistoricFirst": false,
    "description": "First lunar sample return mission since Luna 24 in 1976",
    "objectives": "Return lunar samples to Earth",
    "outcomes": "Returned 1.73 kg of lunar samples",
    "sampleReturnMassKg": 1.73
}'

echo ""
echo "🇮🇳 Creating Indian missions..."

# Chandrayaan-1
create_mission '{
    "name": "Chandrayaan-1",
    "missionDesignation": "CH-1",
    "country": {"id": 4},
    "operator": "ISRO",
    "launchVehicleName": "PSLV-XL",
    "launchDate": "2008-10-22",
    "endDate": "2009-08-29",
    "status": "COMPLETED",
    "missionType": "LUNAR_ORBITER",
    "destination": "LUNAR_ORBIT",
    "crewed": false,
    "launchSite": "Satish Dhawan Space Centre",
    "missionMassKg": 1380,
    "isHistoricFirst": false,
    "description": "First Indian lunar probe, discovered water on Moon",
    "objectives": "Lunar orbit, surface mapping, mineral analysis",
    "outcomes": "Discovered water molecules on lunar surface, 3400+ orbits"
}'

# Mangalyaan - Mars Orbiter Mission
create_mission '{
    "name": "Mars Orbiter Mission",
    "alternateName": "Mangalyaan",
    "missionDesignation": "MOM",
    "country": {"id": 4},
    "operator": "ISRO",
    "launchVehicleName": "PSLV-XL",
    "launchDate": "2013-11-05",
    "endDate": "2022-10-03",
    "status": "COMPLETED",
    "missionType": "MARS_ORBITER",
    "destination": "MARS_ORBIT",
    "crewed": false,
    "launchSite": "Satish Dhawan Space Centre",
    "missionMassKg": 1337,
    "isHistoricFirst": true,
    "historicFirstType": "First Asian Mars orbiter",
    "description": "First Asian nation to reach Mars orbit on first attempt",
    "objectives": "Technology demonstration, Mars exploration",
    "outcomes": "8 years of operations, studied Mars atmosphere and surface"
}'

# Chandrayaan-3
create_mission '{
    "name": "Chandrayaan-3",
    "missionDesignation": "CH-3",
    "country": {"id": 4},
    "operator": "ISRO",
    "launchVehicleName": "LVM3",
    "launchDate": "2023-07-14",
    "endDate": "2023-09-02",
    "status": "COMPLETED",
    "missionType": "LUNAR_LANDER",
    "destination": "LUNAR_SURFACE",
    "crewed": false,
    "launchSite": "Satish Dhawan Space Centre",
    "missionMassKg": 3900,
    "isHistoricFirst": true,
    "historicFirstType": "First landing near lunar south pole",
    "description": "First successful landing near lunar south pole",
    "objectives": "Soft landing, rover exploration of south polar region",
    "outcomes": "Successful landing, Pragyan rover explored surface"
}'

echo ""
echo "🇯🇵 Creating Japanese missions..."

# Hayabusa - First asteroid sample return
create_mission '{
    "name": "Hayabusa",
    "alternateName": "MUSES-C",
    "missionDesignation": "MUSES-C",
    "country": {"id": 5},
    "operator": "JAXA",
    "launchVehicleName": "M-V",
    "launchDate": "2003-05-09",
    "endDate": "2010-06-13",
    "status": "COMPLETED",
    "missionType": "ASTEROID_SAMPLE_RETURN",
    "destination": "ASTEROID",
    "crewed": false,
    "launchSite": "Uchinoura Space Center",
    "missionMassKg": 510,
    "isHistoricFirst": true,
    "historicFirstType": "First asteroid sample return",
    "description": "First spacecraft to return asteroid samples to Earth",
    "objectives": "Return samples from asteroid Itokawa",
    "outcomes": "Returned microscopic samples despite multiple failures"
}'

# Hayabusa2
create_mission '{
    "name": "Hayabusa2",
    "missionDesignation": "Hayabusa2",
    "country": {"id": 5},
    "operator": "JAXA",
    "launchVehicleName": "H-IIA",
    "launchDate": "2014-12-03",
    "status": "ACTIVE",
    "missionType": "ASTEROID_SAMPLE_RETURN",
    "destination": "ASTEROID",
    "crewed": false,
    "launchSite": "Tanegashima Space Center",
    "missionMassKg": 590,
    "isHistoricFirst": false,
    "description": "Returned samples from asteroid Ryugu, now on extended mission",
    "objectives": "Return samples from C-type asteroid",
    "outcomes": "Returned 5.4g of samples, continuing to other asteroids",
    "sampleReturnMassKg": 0.0054
}'

echo ""
echo "🇪🇺 Creating ESA missions..."

# Rosetta - First comet landing
create_mission '{
    "name": "Rosetta",
    "missionDesignation": "Rosetta",
    "country": {"id": 7},
    "operator": "ESA",
    "launchVehicleName": "Ariane 5 G+",
    "launchDate": "2004-03-02",
    "endDate": "2016-09-30",
    "status": "COMPLETED",
    "missionType": "COMET_MISSION",
    "destination": "COMET",
    "crewed": false,
    "launchSite": "Guiana Space Centre",
    "missionMassKg": 2900,
    "isHistoricFirst": true,
    "historicFirstType": "First comet landing",
    "description": "First spacecraft to orbit and land on a comet",
    "objectives": "Orbit comet 67P, deploy Philae lander",
    "outcomes": "Successfully orbited comet, Philae landed on surface"
}'

# JWST (ESA contribution)
create_mission '{
    "name": "James Webb Space Telescope",
    "alternateName": "JWST",
    "country": {"id": 1},
    "operator": "NASA/ESA/CSA",
    "launchVehicleName": "Ariane 5 ECA",
    "launchDate": "2021-12-25",
    "status": "ACTIVE",
    "missionType": "SPACE_TELESCOPE",
    "destination": "SUN_EARTH_L2",
    "crewed": false,
    "launchSite": "Guiana Space Centre",
    "missionMassKg": 6500,
    "isHistoricFirst": false,
    "description": "Most powerful space telescope ever built",
    "objectives": "Study early universe, exoplanets, star formation",
    "outcomes": "Operational, delivering unprecedented deep space imagery",
    "maxDistanceFromEarthKm": 1500000
}'

echo ""
echo "🚀 Creating recent/ongoing missions..."

# Artemis I
create_mission '{
    "name": "Artemis I",
    "missionDesignation": "Artemis I",
    "country": {"id": 1},
    "operator": "NASA",
    "launchVehicleName": "SLS Block 1",
    "launchDate": "2022-11-16",
    "endDate": "2022-12-11",
    "status": "COMPLETED",
    "missionType": "LUNAR_CREWED_ORBIT",
    "destination": "LUNAR_ORBIT",
    "crewed": false,
    "launchSite": "Kennedy Space Center",
    "missionMassKg": 2607000,
    "isHistoricFirst": false,
    "description": "First flight of SLS and Orion, uncrewed test mission",
    "objectives": "Test Orion spacecraft in lunar environment",
    "outcomes": "Successful 25.5 day mission, furthest any human-rated spacecraft has traveled",
    "maxDistanceFromEarthKm": 432210
}'

# Perseverance
create_mission '{
    "name": "Mars 2020",
    "alternateName": "Perseverance",
    "missionDesignation": "Mars 2020",
    "country": {"id": 1},
    "operator": "NASA/JPL",
    "launchVehicleName": "Atlas V 541",
    "launchDate": "2020-07-30",
    "status": "ACTIVE",
    "missionType": "MARS_ROVER",
    "destination": "MARS_SURFACE",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 1025,
    "isHistoricFirst": false,
    "description": "Mars rover searching for signs of ancient life, includes Ingenuity helicopter",
    "objectives": "Search for biosignatures, cache samples for return",
    "outcomes": "Operating in Jezero Crater, 72+ helicopter flights"
}'

# Ingenuity
create_mission '{
    "name": "Ingenuity",
    "missionDesignation": "Mars Helicopter Scout",
    "country": {"id": 1},
    "operator": "NASA/JPL",
    "launchVehicleName": "Atlas V 541",
    "launchDate": "2020-07-30",
    "endDate": "2024-01-18",
    "status": "COMPLETED",
    "missionType": "MARS_HELICOPTER",
    "destination": "MARS_SURFACE",
    "crewed": false,
    "launchSite": "Cape Canaveral",
    "missionMassKg": 1.8,
    "isHistoricFirst": true,
    "historicFirstType": "First powered flight on another planet",
    "description": "First aircraft to achieve powered, controlled flight on another planet",
    "objectives": "Demonstrate aerial flight on Mars",
    "outcomes": "72 flights over 3 years, exceeded all expectations"
}'

# Tianwen-1
create_mission '{
    "name": "Tianwen-1",
    "missionDesignation": "TW-1",
    "country": {"id": 3},
    "operator": "CNSA",
    "launchVehicleName": "Long March 5",
    "launchDate": "2020-07-23",
    "status": "ACTIVE",
    "missionType": "MARS_ROVER",
    "destination": "MARS_SURFACE",
    "crewed": false,
    "launchSite": "Wenchang Space Launch Center",
    "missionMassKg": 5000,
    "isHistoricFirst": false,
    "description": "First Chinese Mars mission with orbiter, lander, and Zhurong rover",
    "objectives": "Study Mars geology, atmosphere, search for water",
    "outcomes": "Successfully landed Zhurong rover, orbiter still operational"
}'

echo ""
echo "✅ Space missions seeding complete!"
echo ""
echo "📊 Summary of seeded missions:"
curl -s "$API/statistics" | head -20

echo ""
echo "🔗 Test the API:"
echo "  All missions: $API"
echo "  Historic firsts: $API/historic-firsts"
echo "  Crewed missions: $API/crewed"
echo "  Active missions: $API/active"
echo "  By country (USA): $API/by-country-code/USA"
