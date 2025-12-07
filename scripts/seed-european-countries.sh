#!/bin/bash

# Seed additional European countries with space programs into the database

API_URL="http://localhost:8080/api/countries"
CONTENT_TYPE="Content-Type: application/json"

echo "🇪🇺 Seeding additional European countries with space programs..."
echo ""

# 1. Germany
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Germany",
  "isoCode": "DEU",
  "flagUrl": "https://flagcdn.com/w320/de.png",
  "spaceAgencyName": "German Aerospace Center",
  "spaceAgencyAcronym": "DLR",
  "spaceAgencyFounded": 1969,
  "annualBudgetUsd": 2400000000,
  "budgetAsPercentOfGdp": 0.05,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 3,
  "totalSpaceAgencyEmployees": 10000,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 55.0,
  "region": "Europe",
  "description": "Germany is the largest contributor to ESA and operates DLR, one of the largest aerospace research centers in Europe. German astronauts regularly fly to the ISS, and Germany contributed the Columbus laboratory module. The country has strong expertise in Earth observation, robotics, and propulsion technology."
}'
echo "✅ Germany added"

# 2. France
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "France",
  "isoCode": "FRA",
  "flagUrl": "https://flagcdn.com/w320/fr.png",
  "spaceAgencyName": "National Centre for Space Studies",
  "spaceAgencyAcronym": "CNES",
  "spaceAgencyFounded": 1961,
  "annualBudgetUsd": 2800000000,
  "budgetAsPercentOfGdp": 0.10,
  "totalLaunches": 320,
  "successfulLaunches": 305,
  "launchSuccessRate": 95.3,
  "activeAstronauts": 2,
  "totalSpaceAgencyEmployees": 2400,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": true,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 65.0,
  "region": "Europe",
  "description": "France was the third nation to achieve independent space launch capability with the Diamant rocket in 1965. CNES is the largest national space agency in Europe and hosts the Guiana Space Centre (Kourou), the primary launch site for ESA. France is a leader in space telecommunications and Earth observation satellites."
}'
echo "✅ France added"

# 3. Italy
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Italy",
  "isoCode": "ITA",
  "flagUrl": "https://flagcdn.com/w320/it.png",
  "spaceAgencyName": "Italian Space Agency",
  "spaceAgencyAcronym": "ASI",
  "spaceAgencyFounded": 1988,
  "annualBudgetUsd": 1200000000,
  "budgetAsPercentOfGdp": 0.05,
  "totalLaunches": 30,
  "successfulLaunches": 27,
  "launchSuccessRate": 90.0,
  "activeAstronauts": 3,
  "totalSpaceAgencyEmployees": 350,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": true,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 48.0,
  "region": "Europe",
  "description": "Italy operates the San Marco equatorial launch platform and the Vega small satellite launcher through Avio. Italy contributed the Multi-Purpose Logistics Modules and Cupola to the ISS. Italian astronauts have extensive ISS experience, and the country has strong expertise in space manufacturing and telecommunications."
}'
echo "✅ Italy added"

# 4. Spain
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Spain",
  "isoCode": "ESP",
  "flagUrl": "https://flagcdn.com/w320/es.png",
  "spaceAgencyName": "Spanish Space Agency",
  "spaceAgencyAcronym": "AEE",
  "spaceAgencyFounded": 2023,
  "annualBudgetUsd": 400000000,
  "budgetAsPercentOfGdp": 0.03,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 1,
  "totalSpaceAgencyEmployees": 150,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 28.0,
  "region": "Europe",
  "description": "Spain recently established its national space agency (AEE) in 2023. The country hosts NASA Deep Space Network facilities in Madrid and has contributed to numerous ESA missions. Spain has strong capabilities in satellite telecommunications and Earth observation."
}'
echo "✅ Spain added"

# 5. Netherlands
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Netherlands",
  "isoCode": "NLD",
  "flagUrl": "https://flagcdn.com/w320/nl.png",
  "spaceAgencyName": "Netherlands Space Office",
  "spaceAgencyAcronym": "NSO",
  "spaceAgencyFounded": 2009,
  "annualBudgetUsd": 150000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 1,
  "totalSpaceAgencyEmployees": 50,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 22.0,
  "region": "Europe",
  "description": "The Netherlands hosts ESTEC, the main technology development center of ESA, in Noordwijk. Dutch astronauts have flown on multiple ISS missions. The country has expertise in space instrumentation, including contributions to the James Webb Space Telescope."
}'
echo "✅ Netherlands added"

# 6. Belgium
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Belgium",
  "isoCode": "BEL",
  "flagUrl": "https://flagcdn.com/w320/be.png",
  "spaceAgencyName": "Belgian Federal Science Policy Office - Space",
  "spaceAgencyAcronym": "BELSPO",
  "spaceAgencyFounded": 1973,
  "annualBudgetUsd": 280000000,
  "budgetAsPercentOfGdp": 0.04,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 100,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 20.0,
  "region": "Europe",
  "description": "Belgium is a founding member of ESA and hosts many European space institutions. Belgian companies contribute to Ariane rockets and various satellite programs. The country has expertise in space mechanisms and thermal control systems."
}'
echo "✅ Belgium added"

# 7. Sweden
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Sweden",
  "isoCode": "SWE",
  "flagUrl": "https://flagcdn.com/w320/se.png",
  "spaceAgencyName": "Swedish National Space Agency",
  "spaceAgencyAcronym": "SNSA",
  "spaceAgencyFounded": 1972,
  "annualBudgetUsd": 180000000,
  "budgetAsPercentOfGdp": 0.03,
  "totalLaunches": 9,
  "successfulLaunches": 8,
  "launchSuccessRate": 88.9,
  "activeAstronauts": 1,
  "totalSpaceAgencyEmployees": 80,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 25.0,
  "region": "Europe",
  "description": "Sweden operates the Esrange Space Center in Kiruna, used for sounding rockets and stratospheric balloon launches. Sweden has contributed to numerous ESA science missions and operates a satellite data receiving station. Swedish astronaut Christer Fuglesang flew on two Space Shuttle missions."
}'
echo "✅ Sweden added"

# 8. Norway
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Norway",
  "isoCode": "NOR",
  "flagUrl": "https://flagcdn.com/w320/no.png",
  "spaceAgencyName": "Norwegian Space Agency",
  "spaceAgencyAcronym": "NOSA",
  "spaceAgencyFounded": 1987,
  "annualBudgetUsd": 120000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 50,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 18.0,
  "region": "Europe",
  "description": "Norway operates the Andøya Space Center for sounding rockets and is developing the SvalRak range in Svalbard for polar orbit launches. Norway has expertise in maritime and Arctic satellite services and is developing small satellite capabilities."
}'
echo "✅ Norway added"

# 9. Switzerland
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Switzerland",
  "isoCode": "CHE",
  "flagUrl": "https://flagcdn.com/w320/ch.png",
  "spaceAgencyName": "Swiss Space Office",
  "spaceAgencyAcronym": "SSO",
  "spaceAgencyFounded": 1998,
  "annualBudgetUsd": 200000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 1,
  "totalSpaceAgencyEmployees": 40,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 24.0,
  "region": "Europe",
  "description": "Switzerland is a founding member of ESA and has contributed to many major missions. Swiss astronaut Claude Nicollier flew four Space Shuttle missions. The country has expertise in precision mechanisms, scientific instruments, and satellite navigation."
}'
echo "✅ Switzerland added"

# 10. Poland
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Poland",
  "isoCode": "POL",
  "flagUrl": "https://flagcdn.com/w320/pl.png",
  "spaceAgencyName": "Polish Space Agency",
  "spaceAgencyAcronym": "POLSA",
  "spaceAgencyFounded": 2014,
  "annualBudgetUsd": 80000000,
  "budgetAsPercentOfGdp": 0.01,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 60,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 15.0,
  "region": "Europe",
  "description": "Poland became an ESA member state in 2012 and established POLSA in 2014. The country has growing expertise in satellite technology and Earth observation. Polish engineer Mirosław Hermaszewski was the first Pole in space, flying on Soyuz 30 in 1978."
}'
echo "✅ Poland added"

# 11. Austria
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Austria",
  "isoCode": "AUT",
  "flagUrl": "https://flagcdn.com/w320/at.png",
  "spaceAgencyName": "Austrian Space Forum",
  "spaceAgencyAcronym": "OeWF",
  "spaceAgencyFounded": 1997,
  "annualBudgetUsd": 90000000,
  "budgetAsPercentOfGdp": 0.02,
  "totalLaunches": 0,
  "successfulLaunches": 0,
  "launchSuccessRate": null,
  "activeAstronauts": 0,
  "totalSpaceAgencyEmployees": 30,
  "humanSpaceflightCapable": false,
  "independentLaunchCapable": false,
  "reusableRocketCapable": false,
  "deepSpaceCapable": false,
  "spaceStationCapable": false,
  "lunarLandingCapable": false,
  "marsLandingCapable": false,
  "overallCapabilityScore": 14.0,
  "region": "Europe",
  "description": "Austria is an ESA member known for its Mars simulation research through the Austrian Space Forum. Austrian astronaut Franz Viehböck flew to Mir in 1991. The country contributes to ESA science missions and has expertise in space instrumentation."
}'
echo "✅ Austria added"

echo ""
echo "🎉 11 additional European countries have been seeded!"
echo ""
echo "Countries added:"
echo "  - Germany (DLR)"
echo "  - France (CNES)"
echo "  - Italy (ASI)"
echo "  - Spain (AEE)"
echo "  - Netherlands (NSO)"
echo "  - Belgium (BELSPO)"
echo "  - Sweden (SNSA)"
echo "  - Norway (NOSA)"
echo "  - Switzerland (SSO)"
echo "  - Poland (POLSA)"
echo "  - Austria (OeWF)"
echo ""
echo "Total countries now available: 27"
