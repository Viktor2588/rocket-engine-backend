#!/bin/bash

# Seed countries with space programs into the database

API_URL="http://localhost:8080/api/countries"
CONTENT_TYPE="Content-Type: application/json"

echo "🌍 Seeding countries with space programs..."
echo ""

# 1. United States
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "United States",
  "isoCode": "USA",
  "flagUrl": "https://flagcdn.com/w320/us.png",
  "spaceAgencyName": "National Aeronautics and Space Administration",
  "spaceAgencyAcronym": "NASA",
  "spaceAgencyFounded": 1958,
  "annualBudgetUsd": 25000000000,
  "budgetAsPercentOfGdp": 0.48,
  "totalLaunches": 1800,
  "successfulLaunches": 1720,
  "launchSuccessRate": 95.5,
  "activeAstronauts": 48,
  "totalSpaceAgencyEmployees": 18000,
  "humanSpaceflightCapable": true,
  "independentLaunchCapable": true,
  "reusableRocketCapable": true,
  "deepSpaceCapable": true,
  "spaceStationCapable": true,
  "lunarLandingCapable": true,
  "marsLandingCapable": true,
  "overallCapabilityScore": 98.5,
  "region": "North America",
  "description": "The United States has the most comprehensive space program, with achievements including the Apollo Moon landings, Space Shuttle program, Mars rovers, and leadership in the International Space Station. Commercial space companies like SpaceX and Blue Origin have revolutionized launch services."
}'
echo "✅ United States added"

# 2. Russia
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Russia",
  "isoCode": "RUS",
  "flagUrl": "https://flagcdn.com/w320/ru.png",
  "spaceAgencyName": "Russian Federal Space Agency",
  "spaceAgencyAcronym": "Roscosmos",
  "spaceAgencyFounded": 1992,
  "annualBudgetUsd": 5600000000,
  "budgetAsPercentOfGdp": 0.28,
  "totalLaunches": 3200,
  "successfulLaunches": 3050,
  "launchSuccessRate": 95.3,
  "activeAstronauts": 25,
  "totalSpaceAgencyEmployees": 170000,
  "humanSpaceflightCapable": true,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": true,
  "lunarLandingCapable": true,
  "marsLandingCapable": false,
  "overallCapabilityScore": 85.0,
  "region": "Europe/Asia",
  "description": "Russia (and its predecessor the Soviet Union) pioneered human spaceflight with Yuri Gagarin, the first human in space. Russia operates the Soyuz spacecraft, maintains the Russian segment of the ISS, and has extensive experience in long-duration spaceflight."
}'
echo "✅ Russia added"

# 3. China
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "China",
  "isoCode": "CHN",
  "flagUrl": "https://flagcdn.com/w320/cn.png",
  "spaceAgencyName": "China National Space Administration",
  "spaceAgencyAcronym": "CNSA",
  "spaceAgencyFounded": 1993,
  "annualBudgetUsd": 14000000000,
  "budgetAsPercentOfGdp": 0.08,
  "totalLaunches": 450,
  "successfulLaunches": 430,
  "launchSuccessRate": 95.5,
  "activeAstronauts": 18,
  "totalSpaceAgencyEmployees": 150000,
  "humanSpaceflightCapable": true,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": true,
  "lunarLandingCapable": true,
  "marsLandingCapable": true,
  "overallCapabilityScore": 88.0,
  "region": "Asia",
  "description": "China has rapidly developed its space program, becoming the third nation to independently send humans to space. China operates the Tiangong space station, has landed rovers on the Moon (including the far side) and Mars, and maintains an ambitious exploration roadmap."
}'
echo "✅ China added"

# 4. European Space Agency (represented as Europe)
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Europe",
  "isoCode": "ESA",
  "flagUrl": "https://flagcdn.com/w320/eu.png",
  "spaceAgencyName": "European Space Agency",
  "spaceAgencyAcronym": "ESA",
  "spaceAgencyFounded": 1975,
  "annualBudgetUsd": 7500000000,
  "budgetAsPercentOfGdp": 0.04,
  "totalLaunches": 320,
  "successfulLaunches": 305,
  "launchSuccessRate": 95.3,
  "activeAstronauts": 7,
  "totalSpaceAgencyEmployees": 2200,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 72.0,
  "region": "Europe",
  "description": "The European Space Agency represents 22 member states and operates the Ariane launch vehicle family from French Guiana. ESA has contributed significantly to space science, Earth observation, and the ISS, though it relies on partners for human spaceflight."
}'
echo "✅ Europe (ESA) added"

# 5. Japan
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Japan",
  "isoCode": "JPN",
  "flagUrl": "https://flagcdn.com/w320/jp.png",
  "spaceAgencyName": "Japan Aerospace Exploration Agency",
  "spaceAgencyAcronym": "JAXA",
  "spaceAgencyFounded": 2003,
  "annualBudgetUsd": 3200000000,
  "budgetAsPercentOfGdp": 0.08,
  "totalLaunches": 120,
  "successfulLaunches": 115,
  "launchSuccessRate": 95.8,
  "activeAstronauts": 7,
  "totalSpaceAgencyEmployees": 1500,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": false,
  "lunarLandingCapable": true,
  "marsLandingCapable": false,
  "overallCapabilityScore": 68.0,
  "region": "Asia",
  "description": "Japan operates the H-IIA/H3 rocket family and has achieved notable successes including asteroid sample return missions (Hayabusa, Hayabusa2). JAXA contributes to the ISS through the Kibo laboratory and HTV cargo spacecraft."
}'
echo "✅ Japan added"

# 6. India
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "India",
  "isoCode": "IND",
  "flagUrl": "https://flagcdn.com/w320/in.png",
  "spaceAgencyName": "Indian Space Research Organisation",
  "spaceAgencyAcronym": "ISRO",
  "spaceAgencyFounded": 1969,
  "annualBudgetUsd": 1800000000,
  "budgetAsPercentOfGdp": 0.05,
  "totalLaunches": 130,
  "successfulLaunches": 118,
  "launchSuccessRate": 90.7,
  "activeAstronauts": 4,
  "totalSpaceAgencyEmployees": 16000,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": false,
  "lunarLandingCapable": true,
  "marsLandingCapable": false,
  "overallCapabilityScore": 62.0,
  "region": "Asia",
  "description": "India has developed a cost-effective space program with the PSLV and GSLV launch vehicles. ISRO achieved the Mars Orbiter Mission on its first attempt and successfully landed Chandrayaan-3 near the lunar south pole in 2023."
}'
echo "✅ India added"

# 7. South Korea
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "South Korea",
  "isoCode": "KOR",
  "flagUrl": "https://flagcdn.com/w320/kr.png",
  "spaceAgencyName": "Korea Aerospace Research Institute",
  "spaceAgencyAcronym": "KARI",
  "spaceAgencyFounded": 1989,
  "annualBudgetUsd": 700000000,
  "budgetAsPercentOfGdp": 0.04,
  "totalLaunches": 8,
  "successfulLaunches": 5,
  "launchSuccessRate": 62.5,
  "activeAstronauts": 1,
  "totalSpaceAgencyEmployees": 1000,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 35.0,
  "region": "Asia",
  "description": "South Korea achieved independent launch capability in 2022 with the Nuri rocket. The country is developing lunar exploration capabilities and aims to land on the Moon by 2032."
}'
echo "✅ South Korea added"

# 8. Israel
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Israel",
  "isoCode": "ISR",
  "flagUrl": "https://flagcdn.com/w320/il.png",
  "spaceAgencyName": "Israel Space Agency",
  "spaceAgencyAcronym": "ISA",
  "spaceAgencyFounded": 1983,
  "annualBudgetUsd": 200000000,
  "budgetAsPercentOfGdp": 0.04,
  "totalLaunches": 11,
  "successfulLaunches": 9,
  "launchSuccessRate": 81.8,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 200,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 32.0,
  "region": "Middle East",
  "description": "Israel operates the Shavit launch vehicle, launching satellites westward over the Mediterranean due to geopolitical constraints. The country has a strong reconnaissance satellite program and attempted a lunar landing with Beresheet in 2019."
}'
echo "✅ Israel added"

# 9. Iran
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Iran",
  "isoCode": "IRN",
  "flagUrl": "https://flagcdn.com/w320/ir.png",
  "spaceAgencyName": "Iranian Space Agency",
  "spaceAgencyAcronym": "ISA",
  "spaceAgencyFounded": 2004,
  "annualBudgetUsd": 100000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 14,
  "successfulLaunches": 6,
  "launchSuccessRate": 42.8,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 5000,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 22.0,
  "region": "Middle East",
  "description": "Iran has developed indigenous launch capability with the Safir and Simorgh rockets. The program has faced international scrutiny and has had mixed success rates with satellite launches."
}'
echo "✅ Iran added"

# 10. North Korea
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "North Korea",
  "isoCode": "PRK",
  "flagUrl": "https://flagcdn.com/w320/kp.png",
  "spaceAgencyName": "National Aerospace Development Administration",
  "spaceAgencyAcronym": "NADA",
  "spaceAgencyFounded": 2013,
  "annualBudgetUsd": null,
  "budgetAsPercentOfGdp": null,
  "totalLaunches": 6,
  "successfulLaunches": 3,
  "launchSuccessRate": 50.0,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": null,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 15.0,
  "region": "Asia",
  "description": "North Korea claims to have placed satellites in orbit using the Unha rocket. The program is controversial due to its overlap with ballistic missile development."
}'
echo "✅ North Korea added"

# 11. United Kingdom
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "United Kingdom",
  "isoCode": "GBR",
  "flagUrl": "https://flagcdn.com/w320/gb.png",
  "spaceAgencyName": "UK Space Agency",
  "spaceAgencyAcronym": "UKSA",
  "spaceAgencyFounded": 2010,
  "annualBudgetUsd": 800000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 1,
  "successfulLaunches": 0,
  "launchSuccessRate": 0.0,
  "activeAstronauts": 2,
  "totalSpaceAgencyEmployees": 200,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 28.0,
  "region": "Europe",
  "description": "The UK participates in ESA and is developing domestic launch capability. The country has a strong satellite industry and space science sector, contributing instruments to many international missions."
}'
echo "✅ United Kingdom added"

# 12. Canada
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Canada",
  "isoCode": "CAN",
  "flagUrl": "https://flagcdn.com/w320/ca.png",
  "spaceAgencyName": "Canadian Space Agency",
  "spaceAgencyAcronym": "CSA",
  "spaceAgencyFounded": 1989,
  "annualBudgetUsd": 400000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 4,
  "totalSpaceAgencyEmployees": 700,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 25.0,
  "region": "North America",
  "description": "Canada is a key ISS partner, contributing the Canadarm robotic systems. The country has a strong astronaut program and participates in NASA missions, including the Artemis program with the Canadarm3 for the Lunar Gateway."
}'
echo "✅ Canada added"

# 13. Australia
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Australia",
  "isoCode": "AUS",
  "flagUrl": "https://flagcdn.com/w320/au.png",
  "spaceAgencyName": "Australian Space Agency",
  "spaceAgencyAcronym": "ASA",
  "spaceAgencyFounded": 2018,
  "annualBudgetUsd": 300000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 3,
  "successfulLaunches": 2,
  "launchSuccessRate": 66.7,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 100,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 18.0,
  "region": "Oceania",
  "description": "Australia recently established its space agency and is developing commercial launch capabilities. The country hosts important ground stations for deep space communications and is participating in the Artemis program."
}'
echo "✅ Australia added"

# 14. Brazil
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Brazil",
  "isoCode": "BRA",
  "flagUrl": "https://flagcdn.com/w320/br.png",
  "spaceAgencyName": "Brazilian Space Agency",
  "spaceAgencyAcronym": "AEB",
  "spaceAgencyFounded": 1994,
  "annualBudgetUsd": 150000000,
  "budgetAsPercentOfGdp": 0.01,
  "totalLaunches": 3,
  "successfulLaunches": 0,
  "launchSuccessRate": 0.0,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 500,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 15.0,
  "region": "South America",
  "description": "Brazil operates the Alcantara Launch Center, one of the best-located launch sites in the world due to its proximity to the equator. The VLS rocket program has faced setbacks but the country continues to develop launch capabilities."
}'
echo "✅ Brazil added"

# 15. New Zealand
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "New Zealand",
  "isoCode": "NZL",
  "flagUrl": "https://flagcdn.com/w320/nz.png",
  "spaceAgencyName": "New Zealand Space Agency",
  "spaceAgencyAcronym": "NZSA",
  "spaceAgencyFounded": 2016,
  "annualBudgetUsd": 15000000,
  "budgetAsPercentOfGdp": 0.01,
  "totalLaunches": 45,
  "successfulLaunches": 40,
  "launchSuccessRate": 88.9,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 20,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 30.0,
  "region": "Oceania",
  "description": "New Zealand hosts Rocket Lab operations, which has made it a significant player in the small satellite launch market. The Electron rocket launches frequently from the Mahia Peninsula."
}'
echo "✅ New Zealand added"

# 16. Ukraine
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Ukraine",
  "isoCode": "UKR",
  "flagUrl": "https://flagcdn.com/w320/ua.png",
  "spaceAgencyName": "State Space Agency of Ukraine",
  "spaceAgencyAcronym": "SSAU",
  "spaceAgencyFounded": 1992,
  "annualBudgetUsd": 50000000,
  "budgetAsPercentOfGdp": 0.03,
  "totalLaunches": 160,
  "successfulLaunches": 145,
  "launchSuccessRate": 90.6,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 7000,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 35.0,
  "region": "Europe",
  "description": "Ukraine inherited significant space industry from the Soviet Union, including the Yuzhnoye Design Office. The country has built the Zenit rocket and contributes to international launch programs."
}'
echo "✅ Ukraine added"

echo ""
echo "🎉 All 16 countries with space programs have been seeded!"
echo ""
echo "Countries added:"
echo "  - USA (NASA)"
echo "  - Russia (Roscosmos)"
echo "  - China (CNSA)"
echo "  - Europe (ESA)"
echo "  - Japan (JAXA)"
echo "  - India (ISRO)"
echo "  - South Korea (KARI)"
echo "  - Israel (ISA)"
echo "  - Iran (ISA)"
echo "  - North Korea (NADA)"
echo "  - United Kingdom (UKSA)"
echo "  - Canada (CSA)"
echo "  - Australia (ASA)"
echo "  - Brazil (AEB)"
echo "  - New Zealand (NZSA)"
echo "  - Ukraine (SSAU)"
