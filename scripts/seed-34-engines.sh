#!/bin/bash

# Seed 33 rocket engines into the database

API_URL="http://localhost:8080/api/engines"
CONTENT_TYPE="Content-Type: application/json"

# 1. Merlin 1D
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Merlin 1D",
  "origin": "USA",
  "designer": "SpaceX",
  "vehicle": "Falcon 9",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Pump-fed",
  "isp_s": 282.0,
  "thrustN": 845000,
  "chamberPressureBar": 210.0,
  "massKg": 411.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.43,
  "description": "Kerolox engine for Falcon 9 first stage."
}'
echo "✅ Merlin 1D added"

# 2. Merlin Vacuum
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Merlin Vacuum",
  "origin": "USA",
  "designer": "SpaceX",
  "vehicle": "Falcon 9",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Pump-fed",
  "isp_s": 348.0,
  "thrustN": 411000,
  "chamberPressureBar": 210.0,
  "massKg": 385.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.43,
  "description": "Vacuum-optimized kerolox engine for Falcon 9 second stage."
}'
echo "✅ Merlin Vacuum added"

# 3. Raptor 3
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Raptor 3",
  "origin": "USA",
  "designer": "SpaceX",
  "vehicle": "Starship",
  "status": "Development",
  "use": "1st stage",
  "propellant": "LCH4 / LOX",
  "powerCycle": "Full-flow staged combustion",
  "isp_s": 350.0,
  "thrustN": 2200000,
  "chamberPressureBar": 300.0,
  "massKg": 2000.0,
  "thrustToWeightRatio": null,
  "ofRatio": 3.6,
  "description": "Methane/LOX engine for Super Heavy and Starship."
}'
echo "✅ Raptor 3 added"

# 4. F-1
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "F-1",
  "origin": "USA",
  "designer": "Rocketdyne",
  "vehicle": "Saturn V",
  "status": "Retired",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 304.0,
  "thrustN": 7740000,
  "chamberPressureBar": 210.0,
  "massKg": 8400.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Kerolox first-stage engine for Saturn V, most powerful single nozzle engine."
}'
echo "✅ F-1 added"

# 5. RS-25
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RS-25",
  "origin": "USA",
  "designer": "Rocketdyne",
  "vehicle": "Space Shuttle / SLS",
  "status": "Active",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Staged combustion",
  "isp_s": 453.0,
  "thrustN": 2280000,
  "chamberPressureBar": 206.0,
  "massKg": 3177.0,
  "thrustToWeightRatio": null,
  "ofRatio": 6.04,
  "description": "Hydrolox engine used on Space Shuttle and SLS core stage."
}'
echo "✅ RS-25 added"

# 6. BE-4
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "BE-4",
  "origin": "USA",
  "designer": "Blue Origin",
  "vehicle": "Vulcan",
  "status": "Active",
  "use": "1st stage",
  "propellant": "LCH4 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 339.0,
  "thrustN": 2400000,
  "chamberPressureBar": 190.0,
  "massKg": 1710.0,
  "thrustToWeightRatio": null,
  "ofRatio": 3.6,
  "description": "Methane/LOX engine for Vulcan and New Glenn."
}'
echo "✅ BE-4 added"

# 7. RS-68A
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RS-68A",
  "origin": "USA",
  "designer": "Aerojet Rocketdyne",
  "vehicle": "Delta IV",
  "status": "Active",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 414.0,
  "thrustN": 3560000,
  "chamberPressureBar": 204.0,
  "massKg": 4465.0,
  "thrustToWeightRatio": null,
  "ofRatio": 5.5,
  "description": "Hydrolox booster engine for Delta IV."
}'
echo "✅ RS-68A added"

# 8. RD-170/171M
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RD-170/171M",
  "origin": "Russia",
  "designer": "NPO Energomash",
  "vehicle": "Energia / Zenit",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 337.0,
  "thrustN": 7904000,
  "chamberPressureBar": 240.0,
  "massKg": 9750.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Four-chamber kerolox booster engine for Energia and Zenit."
}'
echo "✅ RD-170/171M added"

# 9. RD-180
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RD-180",
  "origin": "Russia",
  "designer": "NPO Energomash",
  "vehicle": "Atlas V",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 338.0,
  "thrustN": 3820000,
  "chamberPressureBar": 240.0,
  "massKg": 5390.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Two-chamber kerolox booster engine for Atlas V."
}'
echo "✅ RD-180 added"

# 10. RD-191
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RD-191",
  "origin": "Russia",
  "designer": "NPO Energomash",
  "vehicle": "Angara",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 338.0,
  "thrustN": 1900000,
  "chamberPressureBar": 240.0,
  "massKg": 2700.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Single-chamber kerolox engine for Angara."
}'
echo "✅ RD-191 added"

# 11. RD-0120
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RD-0120",
  "origin": "Russia",
  "designer": "NPO Energomash",
  "vehicle": "Energia",
  "status": "Retired",
  "use": "2nd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 413.0,
  "thrustN": 1890000,
  "chamberPressureBar": 250.0,
  "massKg": 3240.0,
  "thrustToWeightRatio": null,
  "ofRatio": 5.65,
  "description": "Hydrolox engine for Energia second stage."
}'
echo "✅ RD-0120 added"

# 12. NK-33
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "NK-33",
  "origin": "Russia",
  "designer": "Kuznetsov Design Bureau",
  "vehicle": "Soyuz-2",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 331.0,
  "thrustN": 1638000,
  "chamberPressureBar": 240.0,
  "massKg": 1650.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Kerolox engine for Soyuz second stage."
}'
echo "✅ NK-33 added"

# 13. NPO Energomash RD-108A
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "RD-108A",
  "origin": "Russia",
  "designer": "NPO Energomash",
  "vehicle": "Soyuz",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 320.0,
  "thrustN": 1020000,
  "chamberPressureBar": 210.0,
  "massKg": 1250.0,
  "thrustToWeightRatio": null,
  "ofRatio": 2.27,
  "description": "Four-chamber kerolox engine for Soyuz first stage."
}'
echo "✅ RD-108A added"

# 14. HM7B
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "HM7B",
  "origin": "France",
  "designer": "ArianeGroup",
  "vehicle": "Ariane 5",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 431.0,
  "thrustN": 645000,
  "chamberPressureBar": 80.0,
  "massKg": 277.0,
  "thrustToWeightRatio": null,
  "ofRatio": 5.9,
  "description": "Hydrolox engine for Ariane 5 second stage."
}'
echo "✅ HM7B added"

# 15. Vulcain 2
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Vulcain 2",
  "origin": "France",
  "designer": "ArianeGroup",
  "vehicle": "Ariane 5",
  "status": "Active",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 431.0,
  "thrustN": 1345000,
  "chamberPressureBar": 205.0,
  "massKg": 2163.0,
  "thrustToWeightRatio": null,
  "ofRatio": 5.9,
  "description": "Hydrolox engine for Ariane 5 first stage."
}'
echo "✅ Vulcain 2 added"

# 16. AR2
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "AR2",
  "origin": "Europe",
  "designer": "ArianeGroup",
  "vehicle": "Ariane 6",
  "status": "Development",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 447.0,
  "thrustN": 1333000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for Ariane 6."
}'
echo "✅ AR2 added"

# 17. SSME / Space Shuttle Main Engine
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "SSME",
  "origin": "USA",
  "designer": "Rocketdyne",
  "vehicle": "Space Shuttle",
  "status": "Retired",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Staged combustion",
  "isp_s": 453.0,
  "thrustN": 2280000,
  "chamberPressureBar": 206.0,
  "massKg": 3177.0,
  "thrustToWeightRatio": null,
  "ofRatio": 6.04,
  "description": "Same as RS-25, used on Space Shuttle."
}'
echo "✅ SSME added"

# 18. LE-7A
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "LE-7A",
  "origin": "Japan",
  "designer": "JAXA",
  "vehicle": "H-IIA",
  "status": "Active",
  "use": "1st stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Staged combustion",
  "isp_s": 428.0,
  "thrustN": 1098000,
  "chamberPressureBar": 196.0,
  "massKg": 1650.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for H-IIA first stage."
}'
echo "✅ LE-7A added"

# 19. LE-5B
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "LE-5B",
  "origin": "Japan",
  "designer": "JAXA",
  "vehicle": "H-IIA",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 447.0,
  "thrustN": 137000,
  "chamberPressureBar": 125.0,
  "massKg": 201.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for H-IIA second stage."
}'
echo "✅ LE-5B added"

# 20. CE-20
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "CE-20",
  "origin": "South Korea",
  "designer": "KARI",
  "vehicle": "NURI",
  "status": "Active",
  "use": "3rd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 450.0,
  "thrustN": 750000,
  "chamberPressureBar": 150.0,
  "massKg": 1350.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for NURI third stage."
}'
echo "✅ CE-20 added"

# 21. CE-75
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "CE-75",
  "origin": "South Korea",
  "designer": "KARI",
  "vehicle": "NURI",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 302.0,
  "thrustN": 755000,
  "chamberPressureBar": 210.0,
  "massKg": 1350.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Kerolox engine for NURI first stage."
}'
echo "✅ CE-75 added"

# 22. Falcon Heavy SRB-A3
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "SRB-A3",
  "origin": "USA",
  "designer": "Northrop Grumman",
  "vehicle": "Delta IV Heavy",
  "status": "Active",
  "use": "Booster",
  "propellant": "Solid",
  "powerCycle": "Solid rocket",
  "isp_s": 283.0,
  "thrustN": 17200000,
  "chamberPressureBar": 60.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Solid rocket booster for Delta IV Heavy."
}'
echo "✅ SRB-A3 added"

# 23. Vega solid boosters (P80)
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "P80",
  "origin": "Europe",
  "designer": "Avio",
  "vehicle": "Vega",
  "status": "Active",
  "use": "1st stage",
  "propellant": "Solid",
  "powerCycle": "Solid rocket",
  "isp_s": 279.0,
  "thrustN": 2668000,
  "chamberPressureBar": 67.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Solid rocket booster for Vega."
}'
echo "✅ P80 added"

# 24. Soltan
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Soltan",
  "origin": "Iran",
  "designer": "Shahid Hemmat",
  "vehicle": "Safir",
  "status": "Active",
  "use": "1st stage",
  "propellant": "Liquid",
  "powerCycle": "Pump-fed",
  "isp_s": 266.0,
  "thrustN": 1960000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Liquid engine for Safir."
}'
echo "✅ Soltan added"

# 25. SCE-200
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "SCE-200",
  "origin": "India",
  "designer": "ISRO",
  "vehicle": "LVM3",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 335.0,
  "thrustN": 2030000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Kerolox engine for LVM3."
}'
echo "✅ SCE-200 added"

# 26. Vikas
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Vikas",
  "origin": "India",
  "designer": "ISRO",
  "vehicle": "GSLV",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 293.0,
  "thrustN": 803000,
  "chamberPressureBar": 210.0,
  "massKg": 1010.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Kerolox engine for GSLV."
}'
echo "✅ Vikas added"

# 27. YF-100
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "YF-100",
  "origin": "China",
  "designer": "CNSA",
  "vehicle": "Long March 3A",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Pump-fed",
  "isp_s": 300.0,
  "thrustN": 1961000,
  "chamberPressureBar": 250.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Kerolox engine for Long March 3A."
}'
echo "✅ YF-100 added"

# 28. YF-75
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "YF-75",
  "origin": "China",
  "designer": "CNSA",
  "vehicle": "Long March 3A",
  "status": "Active",
  "use": "3rd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 426.0,
  "thrustN": 171000,
  "chamberPressureBar": 200.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for Long March 3A."
}'
echo "✅ YF-75 added"

# 29. YF-77
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "YF-77",
  "origin": "China",
  "designer": "CNSA",
  "vehicle": "Long March 5",
  "status": "Active",
  "use": "2nd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Full-flow staged combustion",
  "isp_s": 451.0,
  "thrustN": 520000,
  "chamberPressureBar": 270.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for Long March 5."
}'
echo "✅ YF-77 added"

# 30. YF-115
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "YF-115",
  "origin": "China",
  "designer": "CNSA",
  "vehicle": "Long March 5",
  "status": "Active",
  "use": "1st stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 310.0,
  "thrustN": 4250000,
  "chamberPressureBar": 260.0,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Kerolox engine for Long March 5."
}'
echo "✅ YF-115 added"

# 31. YF-280
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "YF-280",
  "origin": "China",
  "designer": "CNSA",
  "vehicle": "Long March 6",
  "status": "Development",
  "use": "2nd stage",
  "propellant": "LH2 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 442.0,
  "thrustN": 200000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Hydrolox engine for Long March 6."
}'
echo "✅ YF-280 added"

# 32. Huolian-1
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Huolian-1",
  "origin": "China",
  "designer": "Expace",
  "vehicle": "Kuaizhou",
  "status": "Active",
  "use": "1st stage",
  "propellant": "Solid",
  "powerCycle": "Solid rocket",
  "isp_s": 267.0,
  "thrustN": 5200000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Solid rocket booster for Kuaizhou."
}'
echo "✅ Huolian-1 added"

# 33. Aquila (Isar Aerospace)
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Aquila",
  "origin": "Germany",
  "designer": "Isar Aerospace",
  "vehicle": "Spectrum",
  "status": "Development",
  "use": "1st stage",
  "propellant": "Propane / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 318.0,
  "thrustN": 75000,
  "chamberPressureBar": null,
  "massKg": null,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "Propane/LOX engine for Isar Aerospace Spectrum."
}'
echo "✅ Aquila added"

# 34. Archimedes
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "name": "Archimedes",
  "origin": "USA",
  "designer": "Rocket Lab",
  "vehicle": "Neutron",
  "status": "Development",
  "use": "2nd stage",
  "propellant": "RP-1 / LOX",
  "powerCycle": "Gas generator",
  "isp_s": 290.0,
  "thrustN": 45000,
  "chamberPressureBar": null,
  "massKg": 35.0,
  "thrustToWeightRatio": null,
  "ofRatio": null,
  "description": "The Archimedes is an upper stage engine being developed by Rocket Lab for the Neutron rocket."
}'
echo "✅ Archimedes added"

echo ""
echo "✅ All 34 rocket engines seeded successfully!"
