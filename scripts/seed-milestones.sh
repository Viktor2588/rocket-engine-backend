#!/bin/bash

# Seed historical space milestones into the database
# Requires countries to be seeded first

API_URL="http://localhost:8080/api/milestones"
CONTENT_TYPE="Content-Type: application/json"

echo "🚀 Seeding historical space milestones..."
echo ""

# Helper function to get country ID by ISO code
get_country_id() {
    curl -s "http://localhost:8080/api/countries/by-code/$1" | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*'
}

# Get country IDs
USA_ID=$(get_country_id "USA")
RUS_ID=$(get_country_id "RUS")
CHN_ID=$(get_country_id "CHN")
ESA_ID=$(get_country_id "ESA")
JPN_ID=$(get_country_id "JPN")
IND_ID=$(get_country_id "IND")

echo "Country IDs: USA=$USA_ID, RUS=$RUS_ID, CHN=$CHN_ID, ESA=$ESA_ID, JPN=$JPN_ID, IND=$IND_ID"
echo ""

# ==================== ORBITAL FIRSTS ====================

# First Satellite - Sputnik 1 (USSR/Russia)
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_SATELLITE",
  "dateAchieved": "1957-10-04",
  "globalRank": 1,
  "title": "First Artificial Satellite",
  "achievedBy": "Sputnik 1",
  "missionName": "Sputnik 1",
  "description": "The Soviet Union launched Sputnik 1, the first artificial satellite to orbit Earth, marking the beginning of the Space Age."
}'
echo "✅ First Satellite (USSR)"

# First Satellite - USA (Explorer 1)
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_SATELLITE",
  "dateAchieved": "1958-02-01",
  "globalRank": 2,
  "title": "First American Satellite",
  "achievedBy": "Explorer 1",
  "missionName": "Explorer 1",
  "description": "The United States launched Explorer 1, discovering the Van Allen radiation belts."
}'
echo "✅ First Satellite (USA)"

# First Animal in Space
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_ANIMAL_IN_SPACE",
  "dateAchieved": "1957-11-03",
  "globalRank": 1,
  "title": "First Animal in Orbit",
  "achievedBy": "Laika",
  "missionName": "Sputnik 2",
  "description": "Laika, a Soviet space dog, became the first animal to orbit Earth aboard Sputnik 2."
}'
echo "✅ First Animal in Space"

# First Human in Space - Yuri Gagarin
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_HUMAN_IN_SPACE",
  "dateAchieved": "1961-04-12",
  "globalRank": 1,
  "title": "First Human in Space",
  "achievedBy": "Yuri Gagarin",
  "missionName": "Vostok 1",
  "description": "Soviet cosmonaut Yuri Gagarin became the first human to journey into outer space and orbit Earth."
}'
echo "✅ First Human in Space (USSR)"

# First American in Space - Alan Shepard
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_HUMAN_IN_SPACE",
  "dateAchieved": "1961-05-05",
  "globalRank": 2,
  "title": "First American in Space",
  "achievedBy": "Alan Shepard",
  "missionName": "Freedom 7",
  "description": "Alan Shepard became the first American in space with a suborbital flight."
}'
echo "✅ First Human in Space (USA)"

# First Woman in Space
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_WOMAN_IN_SPACE",
  "dateAchieved": "1963-06-16",
  "globalRank": 1,
  "title": "First Woman in Space",
  "achievedBy": "Valentina Tereshkova",
  "missionName": "Vostok 6",
  "description": "Soviet cosmonaut Valentina Tereshkova became the first woman to fly in space."
}'
echo "✅ First Woman in Space"

# First Spacewalk
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_SPACEWALK",
  "dateAchieved": "1965-03-18",
  "globalRank": 1,
  "title": "First Spacewalk (EVA)",
  "achievedBy": "Alexei Leonov",
  "missionName": "Voskhod 2",
  "description": "Soviet cosmonaut Alexei Leonov performed the first extravehicular activity (spacewalk), spending 12 minutes outside the spacecraft."
}'
echo "✅ First Spacewalk"

# First Space Station
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_SPACE_STATION",
  "dateAchieved": "1971-04-19",
  "globalRank": 1,
  "title": "First Space Station",
  "achievedBy": "Salyut 1",
  "missionName": "Salyut 1",
  "description": "The Soviet Union launched Salyut 1, the first space station in orbit."
}'
echo "✅ First Space Station"

# ==================== LUNAR FIRSTS ====================

# First Lunar Impact
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_LUNAR_IMPACT",
  "dateAchieved": "1959-09-13",
  "globalRank": 1,
  "title": "First Lunar Impact",
  "achievedBy": "Luna 2",
  "missionName": "Luna 2",
  "description": "Soviet Luna 2 became the first human-made object to reach the surface of the Moon."
}'
echo "✅ First Lunar Impact"

# First Lunar Orbit
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_LUNAR_ORBIT",
  "dateAchieved": "1966-04-03",
  "globalRank": 1,
  "title": "First Lunar Orbit",
  "achievedBy": "Luna 10",
  "missionName": "Luna 10",
  "description": "Soviet Luna 10 became the first spacecraft to orbit the Moon."
}'
echo "✅ First Lunar Orbit"

# First Lunar Soft Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_LUNAR_SOFT_LANDING",
  "dateAchieved": "1966-02-03",
  "globalRank": 1,
  "title": "First Lunar Soft Landing",
  "achievedBy": "Luna 9",
  "missionName": "Luna 9",
  "description": "Soviet Luna 9 achieved the first soft landing on the Moon and transmitted the first photographs from the lunar surface."
}'
echo "✅ First Lunar Soft Landing"

# First Human Lunar Orbit
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_HUMAN_LUNAR_ORBIT",
  "dateAchieved": "1968-12-24",
  "globalRank": 1,
  "title": "First Humans to Orbit the Moon",
  "achievedBy": "Frank Borman, Jim Lovell, William Anders",
  "missionName": "Apollo 8",
  "description": "Apollo 8 astronauts became the first humans to orbit the Moon and see the far side with their own eyes."
}'
echo "✅ First Human Lunar Orbit"

# First Human Lunar Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_HUMAN_LUNAR_LANDING",
  "dateAchieved": "1969-07-20",
  "globalRank": 1,
  "title": "First Humans on the Moon",
  "achievedBy": "Neil Armstrong, Buzz Aldrin",
  "missionName": "Apollo 11",
  "description": "Neil Armstrong and Buzz Aldrin became the first humans to walk on the Moon, while Michael Collins orbited above."
}'
echo "✅ First Human Lunar Landing"

# First Lunar Rover
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_LUNAR_ROVER",
  "dateAchieved": "1970-11-17",
  "globalRank": 1,
  "title": "First Lunar Rover",
  "achievedBy": "Lunokhod 1",
  "missionName": "Luna 17",
  "description": "Soviet Lunokhod 1 became the first remote-controlled rover to land on another celestial body."
}'
echo "✅ First Lunar Rover"

# First Lunar Sample Return
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_LUNAR_SAMPLE_RETURN",
  "dateAchieved": "1969-07-24",
  "globalRank": 1,
  "title": "First Lunar Sample Return",
  "achievedBy": "Apollo 11 Crew",
  "missionName": "Apollo 11",
  "description": "Apollo 11 returned 21.5 kg of lunar samples to Earth."
}'
echo "✅ First Lunar Sample Return"

# First Lunar Far Side Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$CHN_ID'},
  "milestoneType": "FIRST_LUNAR_FAR_SIDE_LANDING",
  "dateAchieved": "2019-01-03",
  "globalRank": 1,
  "title": "First Landing on Lunar Far Side",
  "achievedBy": "Yutu-2 Rover",
  "missionName": "Chang'\''e 4",
  "description": "China'\''s Chang'\''e 4 became the first spacecraft to land on the far side of the Moon."
}'
echo "✅ First Lunar Far Side Landing"

# First Lunar South Pole Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$IND_ID'},
  "milestoneType": "FIRST_LUNAR_SOUTH_POLE_LANDING",
  "dateAchieved": "2023-08-23",
  "globalRank": 1,
  "title": "First Landing Near Lunar South Pole",
  "achievedBy": "Pragyan Rover",
  "missionName": "Chandrayaan-3",
  "description": "India'\''s Chandrayaan-3 became the first mission to soft-land near the lunar south pole."
}'
echo "✅ First Lunar South Pole Landing"

# ==================== MARS FIRSTS ====================

# First Mars Flyby
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_MARS_FLYBY",
  "dateAchieved": "1965-07-15",
  "globalRank": 1,
  "title": "First Mars Flyby",
  "achievedBy": "Mariner 4",
  "missionName": "Mariner 4",
  "description": "NASA'\''s Mariner 4 performed the first successful flyby of Mars, returning the first close-up images of another planet."
}'
echo "✅ First Mars Flyby"

# First Mars Orbit
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_MARS_ORBIT",
  "dateAchieved": "1971-11-14",
  "globalRank": 1,
  "title": "First Mars Orbit",
  "achievedBy": "Mariner 9",
  "missionName": "Mariner 9",
  "description": "NASA'\''s Mariner 9 became the first spacecraft to orbit another planet."
}'
echo "✅ First Mars Orbit"

# First Mars Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_MARS_LANDING",
  "dateAchieved": "1971-12-02",
  "globalRank": 1,
  "title": "First Mars Landing",
  "achievedBy": "Mars 3 Lander",
  "missionName": "Mars 3",
  "description": "Soviet Mars 3 achieved the first soft landing on Mars, though contact was lost shortly after."
}'
echo "✅ First Mars Landing"

# First Successful Mars Rover
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_MARS_ROVER",
  "dateAchieved": "1997-07-04",
  "globalRank": 1,
  "title": "First Mars Rover",
  "achievedBy": "Sojourner",
  "missionName": "Mars Pathfinder",
  "description": "NASA'\''s Sojourner became the first rover to operate on Mars, exploring for 83 days."
}'
echo "✅ First Mars Rover"

# First Mars Helicopter
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_MARS_HELICOPTER",
  "dateAchieved": "2021-04-19",
  "globalRank": 1,
  "title": "First Powered Flight on Another Planet",
  "achievedBy": "Ingenuity",
  "missionName": "Mars 2020",
  "description": "NASA'\''s Ingenuity helicopter achieved the first powered, controlled flight on another planet."
}'
echo "✅ First Mars Helicopter"

# China Mars Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$CHN_ID'},
  "milestoneType": "FIRST_MARS_LANDING",
  "dateAchieved": "2021-05-14",
  "globalRank": 3,
  "title": "First Chinese Mars Landing",
  "achievedBy": "Zhurong Rover",
  "missionName": "Tianwen-1",
  "description": "China became the second country to successfully operate a rover on Mars."
}'
echo "✅ First Mars Landing (China)"

# ==================== PLANETARY FIRSTS ====================

# First Venus Flyby
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_VENUS_FLYBY",
  "dateAchieved": "1962-12-14",
  "globalRank": 1,
  "title": "First Venus Flyby",
  "achievedBy": "Mariner 2",
  "missionName": "Mariner 2",
  "description": "NASA'\''s Mariner 2 performed the first successful flyby of another planet."
}'
echo "✅ First Venus Flyby"

# First Venus Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$RUS_ID'},
  "milestoneType": "FIRST_VENUS_LANDING",
  "dateAchieved": "1970-12-15",
  "globalRank": 1,
  "title": "First Venus Landing",
  "achievedBy": "Venera 7",
  "missionName": "Venera 7",
  "description": "Soviet Venera 7 became the first spacecraft to successfully land on another planet and transmit data."
}'
echo "✅ First Venus Landing"

# First Jupiter Flyby
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_JUPITER_FLYBY",
  "dateAchieved": "1973-12-03",
  "globalRank": 1,
  "title": "First Jupiter Flyby",
  "achievedBy": "Pioneer 10",
  "missionName": "Pioneer 10",
  "description": "NASA'\''s Pioneer 10 became the first spacecraft to fly by Jupiter."
}'
echo "✅ First Jupiter Flyby"

# First Saturn Flyby
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_SATURN_FLYBY",
  "dateAchieved": "1979-09-01",
  "globalRank": 1,
  "title": "First Saturn Flyby",
  "achievedBy": "Pioneer 11",
  "missionName": "Pioneer 11",
  "description": "NASA'\''s Pioneer 11 became the first spacecraft to fly by Saturn."
}'
echo "✅ First Saturn Flyby"

# First Titan Landing
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$ESA_ID'},
  "milestoneType": "FIRST_TITAN_LANDING",
  "dateAchieved": "2005-01-14",
  "globalRank": 1,
  "title": "First Landing on Titan",
  "achievedBy": "Huygens Probe",
  "missionName": "Cassini-Huygens",
  "description": "ESA'\''s Huygens probe landed on Saturn'\''s moon Titan, the first landing in the outer solar system."
}'
echo "✅ First Titan Landing"

# ==================== TECHNOLOGY FIRSTS ====================

# First Reusable Rocket
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_REUSABLE_ROCKET",
  "dateAchieved": "2015-12-22",
  "globalRank": 1,
  "title": "First Orbital-Class Reusable Rocket",
  "achievedBy": "Falcon 9",
  "missionName": "Orbcomm OG2 Mission 2",
  "description": "SpaceX'\''s Falcon 9 became the first orbital-class rocket to land vertically after delivering payload to orbit."
}'
echo "✅ First Reusable Rocket"

# First Booster Catch
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_BOOSTER_CATCH",
  "dateAchieved": "2024-10-13",
  "globalRank": 1,
  "title": "First Rocket Booster Catch",
  "achievedBy": "Super Heavy Booster",
  "missionName": "Starship Flight 5",
  "description": "SpaceX caught a Super Heavy booster using the launch tower arms, a first in rocket history."
}'
echo "✅ First Booster Catch"

# First Commercial Crew
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_COMMERCIAL_CREW",
  "dateAchieved": "2020-05-30",
  "globalRank": 1,
  "title": "First Commercial Crew Mission",
  "achievedBy": "Doug Hurley, Bob Behnken",
  "missionName": "SpaceX Demo-2",
  "description": "SpaceX Crew Dragon launched the first commercial crew mission to the ISS."
}'
echo "✅ First Commercial Crew"

# First Interstellar Probe
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$USA_ID'},
  "milestoneType": "FIRST_INTERSTELLAR_PROBE",
  "dateAchieved": "2012-08-25",
  "globalRank": 1,
  "title": "First Spacecraft to Enter Interstellar Space",
  "achievedBy": "Voyager 1",
  "missionName": "Voyager 1",
  "description": "NASA'\''s Voyager 1 became the first human-made object to enter interstellar space."
}'
echo "✅ First Interstellar Probe"

# First Asteroid Sample Return
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$JPN_ID'},
  "milestoneType": "FIRST_ASTEROID_SAMPLE_RETURN",
  "dateAchieved": "2010-06-13",
  "globalRank": 1,
  "title": "First Asteroid Sample Return",
  "achievedBy": "Hayabusa",
  "missionName": "Hayabusa",
  "description": "JAXA'\''s Hayabusa became the first spacecraft to return samples from an asteroid to Earth."
}'
echo "✅ First Asteroid Sample Return"

# China Space Station
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$CHN_ID'},
  "milestoneType": "FIRST_MODULAR_SPACE_STATION",
  "dateAchieved": "2022-11-03",
  "globalRank": 3,
  "title": "Tiangong Space Station Completed",
  "achievedBy": "Tiangong",
  "missionName": "Mengtian",
  "description": "China completed its Tiangong space station with the addition of the Mengtian laboratory module."
}'
echo "✅ Tiangong Space Station"

# China First Human in Space
curl -s -X POST "$API_URL" -H "$CONTENT_TYPE" -d '{
  "country": {"id": '$CHN_ID'},
  "milestoneType": "FIRST_HUMAN_IN_SPACE",
  "dateAchieved": "2003-10-15",
  "globalRank": 3,
  "title": "First Chinese Taikonaut",
  "achievedBy": "Yang Liwei",
  "missionName": "Shenzhou 5",
  "description": "Yang Liwei became the first Chinese citizen in space, making China the third country with independent human spaceflight."
}'
echo "✅ First Human in Space (China)"

echo ""
echo "🎉 Historical space milestones have been seeded!"
echo ""
echo "Categories seeded:"
echo "  - Orbital firsts (satellites, human spaceflight)"
echo "  - Lunar exploration milestones"
echo "  - Mars exploration milestones"
echo "  - Planetary exploration (Venus, Jupiter, Saturn, Titan)"
echo "  - Technology firsts (reusable rockets, commercial crew)"
echo ""
echo "Test with: curl http://localhost:8080/api/milestones | jq"
