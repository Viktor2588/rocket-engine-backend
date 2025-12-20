package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.MilestoneType;
import com.rocket.comparison.entity.SpaceMilestone;
import com.rocket.comparison.repository.SpaceMilestoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * Seeds SpaceMilestone entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceMilestoneSeeder implements EntitySeeder {

    private final SpaceMilestoneRepository spaceMilestoneRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding milestones...");
            seedMilestones();
            log.info("Seeded {} milestones", count());
        }
    }

    @Override
    public String getEntityName() {
        return "milestones";
    }

    @Override
    public long count() {
        return spaceMilestoneRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedMilestones() {
        createMilestone(getCountryMap().get("RUS"), MilestoneType.FIRST_SATELLITE, LocalDate.of(1957, 10, 4),
            1, "First Artificial Satellite", "Sputnik 1", "Sputnik 1",
            "The Soviet Union launched the first artificial satellite into orbit.");

        createMilestone(getCountryMap().get("RUS"), MilestoneType.FIRST_HUMAN_IN_SPACE, LocalDate.of(1961, 4, 12),
            1, "First Human in Space", "Yuri Gagarin", "Vostok 1",
            "Yuri Gagarin became the first human to journey into outer space.");

        createMilestone(getCountryMap().get("USA"), MilestoneType.FIRST_HUMAN_LUNAR_LANDING, LocalDate.of(1969, 7, 20),
            1, "First Human Moon Landing", "Neil Armstrong & Buzz Aldrin", "Apollo 11",
            "Apollo 11 astronauts became the first humans to walk on the Moon.");

        createMilestone(getCountryMap().get("USA"), MilestoneType.FIRST_MARS_LANDING, LocalDate.of(1976, 7, 20),
            1, "First Mars Landing", "Viking 1", "Viking 1",
            "NASA's Viking 1 became the first spacecraft to successfully land on Mars.");

        createMilestone(getCountryMap().get("USA"), MilestoneType.FIRST_REUSABLE_ROCKET, LocalDate.of(2015, 12, 21),
            1, "First Orbital Rocket Landing", "Falcon 9", "ORBCOMM-2",
            "SpaceX successfully landed an orbital rocket booster for the first time.");

        createMilestone(getCountryMap().get("CHN"), MilestoneType.FIRST_LUNAR_FAR_SIDE_LANDING, LocalDate.of(2019, 1, 3),
            1, "First Lunar Far Side Landing", "Chang'e 4", "Chang'e 4",
            "China's Chang'e 4 became the first spacecraft to land on the far side of the Moon.");

        createMilestone(getCountryMap().get("IND"), MilestoneType.FIRST_LUNAR_SOUTH_POLE_LANDING, LocalDate.of(2023, 8, 23),
            1, "First Lunar South Pole Landing", "Chandrayaan-3", "Chandrayaan-3",
            "India's Chandrayaan-3 became the first spacecraft to land near the lunar south pole.");

        createMilestone(getCountryMap().get("USA"), MilestoneType.FIRST_MARS_HELICOPTER, LocalDate.of(2021, 4, 19),
            1, "First Powered Flight on Mars", "Ingenuity", "Mars 2020",
            "NASA's Ingenuity helicopter achieved the first powered flight on another planet.");
    }

    private void createMilestone(Country country, MilestoneType type, LocalDate date,
            Integer rank, String title, String achievedBy, String missionName, String description) {

        SpaceMilestone milestone = new SpaceMilestone();
        milestone.setCountry(country);
        milestone.setMilestoneType(type);
        milestone.setDateAchieved(date);
        milestone.setGlobalRank(rank);
        milestone.setTitle(title);
        milestone.setAchievedBy(achievedBy);
        milestone.setMissionName(missionName);
        milestone.setDescription(description);
        spaceMilestoneRepository.save(milestone);
    }
}
