package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.SpaceMissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * Seeds SpaceMission entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceMissionSeeder implements EntitySeeder {

    private final SpaceMissionRepository spaceMissionRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding missions...");
            seedMissions();
            log.info("Seeded {} missions", count());
        }
    }

    @Override
    public String getEntityName() {
        return "missions";
    }

    @Override
    public long count() {
        return spaceMissionRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedMissions() {
        createMission("Apollo 11", getCountryMap().get("USA"), MissionType.LUNAR_CREWED_LANDING, MissionStatus.COMPLETED,
            LocalDate.of(1969, 7, 16), LocalDate.of(1969, 7, 24), Destination.LUNAR_SURFACE,
            "First crewed Moon landing mission.", true, true);

        createMission("Voyager 1", getCountryMap().get("USA"), MissionType.INTERSTELLAR_MISSION, MissionStatus.ACTIVE,
            LocalDate.of(1977, 9, 5), null, Destination.INTERSTELLAR,
            "Farthest human-made object from Earth.", false, true);

        createMission("Mars 2020", getCountryMap().get("USA"), MissionType.MARS_ROVER, MissionStatus.ACTIVE,
            LocalDate.of(2020, 7, 30), null, Destination.MARS_SURFACE,
            "Perseverance rover and Ingenuity helicopter.", false, true);

        createMission("Artemis I", getCountryMap().get("USA"), MissionType.LUNAR_CREWED_ORBIT, MissionStatus.COMPLETED,
            LocalDate.of(2022, 11, 16), LocalDate.of(2022, 12, 11), Destination.LUNAR_ORBIT,
            "First uncrewed test flight of SLS and Orion.", false, true);

        createMission("Tianwen-1", getCountryMap().get("CHN"), MissionType.MARS_ROVER, MissionStatus.ACTIVE,
            LocalDate.of(2020, 7, 23), null, Destination.MARS_SURFACE,
            "China's first Mars rover mission.", false, true);

        createMission("Chandrayaan-3", getCountryMap().get("IND"), MissionType.LUNAR_LANDER, MissionStatus.COMPLETED,
            LocalDate.of(2023, 7, 14), LocalDate.of(2023, 9, 3), Destination.LUNAR_SURFACE,
            "India's successful lunar south pole landing.", false, true);

        createMission("James Webb Space Telescope", getCountryMap().get("USA"), MissionType.SPACE_TELESCOPE, MissionStatus.ACTIVE,
            LocalDate.of(2021, 12, 25), null, Destination.SUN_EARTH_L2,
            "Most powerful space telescope ever built.", false, true);

        createMission("Hayabusa2", getCountryMap().get("JPN"), MissionType.ASTEROID_SAMPLE_RETURN, MissionStatus.COMPLETED,
            LocalDate.of(2014, 12, 3), LocalDate.of(2020, 12, 6), Destination.ASTEROID,
            "Returned samples from asteroid Ryugu.", false, true);
    }

    private void createMission(String name, Country country, MissionType type, MissionStatus status,
            LocalDate launchDate, LocalDate endDate, Destination destination,
            String description, Boolean crewed, Boolean historicFirst) {

        SpaceMission mission = new SpaceMission();
        mission.setName(name);
        mission.setCountry(country);
        mission.setMissionType(type);
        mission.setStatus(status);
        mission.setLaunchDate(launchDate);
        mission.setEndDate(endDate);
        mission.setDestination(destination);
        mission.setDescription(description);
        mission.setCrewed(crewed);
        mission.setIsHistoricFirst(historicFirst);
        spaceMissionRepository.save(mission);
    }
}
