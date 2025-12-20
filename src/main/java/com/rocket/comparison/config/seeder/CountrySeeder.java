package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.CountryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Seeds Country entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CountrySeeder implements EntitySeeder {

    private final CountryRepository countryRepository;

    @Getter
    private Map<String, Country> countryMap = new HashMap<>();

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding countries...");
            seedCountries();
            log.info("Seeded {} countries", count());
        } else {
            loadExistingCountries();
        }
    }

    @Override
    public String getEntityName() {
        return "countries";
    }

    @Override
    public long count() {
        return countryRepository.count();
    }

    /**
     * Loads existing countries into the map for reference by other seeders.
     */
    public void loadExistingCountries() {
        countryRepository.findAll().forEach(country ->
            countryMap.put(country.getIsoCode(), country)
        );
        log.info("Loaded {} existing countries", countryMap.size());
    }

    private void seedCountries() {
        countryMap.put("USA", createCountry("United States", "USA", "https://flagcdn.com/w320/us.png",
            "National Aeronautics and Space Administration", "NASA", 1958,
            new BigDecimal("25000000000"), 0.48, 1800, 1720, 48, 18000,
            true, true, true, true, true, true, true, 98.5, "North America",
            "The United States has the most comprehensive space program."));

        countryMap.put("RUS", createCountry("Russia", "RUS", "https://flagcdn.com/w320/ru.png",
            "Russian Federal Space Agency", "Roscosmos", 1992,
            new BigDecimal("5600000000"), 0.28, 3200, 3050, 25, 170000,
            true, true, false, true, true, true, false, 85.0, "Europe/Asia",
            "Russia pioneered human spaceflight with Yuri Gagarin."));

        countryMap.put("CHN", createCountry("China", "CHN", "https://flagcdn.com/w320/cn.png",
            "China National Space Administration", "CNSA", 1993,
            new BigDecimal("14000000000"), 0.08, 450, 430, 18, 150000,
            true, true, false, true, true, true, true, 88.0, "Asia",
            "China operates the Tiangong space station."));

        countryMap.put("ESA", createCountry("Europe", "ESA", "https://flagcdn.com/w320/eu.png",
            "European Space Agency", "ESA", 1975,
            new BigDecimal("7500000000"), 0.04, 320, 305, 7, 2200,
            false, true, false, true, false, false, false, 72.0, "Europe",
            "ESA represents 22 member states."));

        countryMap.put("JPN", createCountry("Japan", "JPN", "https://flagcdn.com/w320/jp.png",
            "Japan Aerospace Exploration Agency", "JAXA", 2003,
            new BigDecimal("3200000000"), 0.08, 120, 115, 7, 1500,
            false, true, false, true, false, true, false, 68.0, "Asia",
            "Japan operates the H-IIA/H3 rocket family."));

        countryMap.put("IND", createCountry("India", "IND", "https://flagcdn.com/w320/in.png",
            "Indian Space Research Organisation", "ISRO", 1969,
            new BigDecimal("1800000000"), 0.05, 130, 118, 4, 16000,
            false, true, false, true, false, true, false, 62.0, "Asia",
            "ISRO achieved Mars Orbiter Mission on first attempt."));

        countryMap.put("KOR", createCountry("South Korea", "KOR", "https://flagcdn.com/w320/kr.png",
            "Korea Aerospace Research Institute", "KARI", 1989,
            new BigDecimal("700000000"), 0.04, 8, 5, 1, 1000,
            false, true, false, false, false, false, false, 35.0, "Asia",
            "South Korea achieved independent launch capability in 2022."));

        countryMap.put("ISR", createCountry("Israel", "ISR", "https://flagcdn.com/w320/il.png",
            "Israel Space Agency", "ISA", 1983,
            new BigDecimal("200000000"), 0.04, 11, 9, 0, 200,
            false, true, false, false, false, false, false, 32.0, "Middle East",
            "Israel operates the Shavit launch vehicle."));

        countryMap.put("GBR", createCountry("United Kingdom", "GBR", "https://flagcdn.com/w320/gb.png",
            "UK Space Agency", "UKSA", 2010,
            new BigDecimal("800000000"), 0.02, 1, 0, 2, 200,
            false, false, false, false, false, false, false, 28.0, "Europe",
            "The UK participates in ESA."));

        countryMap.put("CAN", createCountry("Canada", "CAN", "https://flagcdn.com/w320/ca.png",
            "Canadian Space Agency", "CSA", 1989,
            new BigDecimal("400000000"), 0.02, 0, 0, 4, 700,
            false, false, false, false, false, false, false, 25.0, "North America",
            "Canada contributed the Canadarm robotic systems."));

        countryMap.put("NZL", createCountry("New Zealand", "NZL", "https://flagcdn.com/w320/nz.png",
            "New Zealand Space Agency", "NZSA", 2016,
            new BigDecimal("15000000"), 0.01, 45, 40, 0, 20,
            false, true, false, false, false, false, false, 30.0, "Oceania",
            "New Zealand hosts Rocket Lab operations."));

        countryMap.put("UKR", createCountry("Ukraine", "UKR", "https://flagcdn.com/w320/ua.png",
            "State Space Agency of Ukraine", "SSAU", 1992,
            new BigDecimal("50000000"), 0.03, 160, 145, 0, 7000,
            false, true, false, false, false, false, false, 35.0, "Europe",
            "Ukraine inherited significant space industry from the Soviet Union."));
    }

    private Country createCountry(String name, String isoCode, String flagUrl,
            String agencyName, String agencyAcronym, Integer founded,
            BigDecimal budget, Double budgetPercent, Integer totalLaunches, Integer successLaunches,
            Integer astronauts, Integer employees, Boolean humanCapable, Boolean launchCapable,
            Boolean reusableCapable, Boolean deepSpaceCapable, Boolean stationCapable,
            Boolean lunarCapable, Boolean marsCapable, Double score, String region, String description) {

        Country country = new Country();
        country.setName(name);
        country.setIsoCode(isoCode);
        country.setFlagUrl(flagUrl);
        country.setSpaceAgencyName(agencyName);
        country.setSpaceAgencyAcronym(agencyAcronym);
        country.setSpaceAgencyFounded(founded);
        country.setAnnualBudgetUsd(budget);
        country.setBudgetAsPercentOfGdp(budgetPercent);
        country.setTotalLaunches(totalLaunches);
        country.setSuccessfulLaunches(successLaunches);
        country.setActiveAstronauts(astronauts);
        country.setTotalSpaceAgencyEmployees(employees);
        country.setHumanSpaceflightCapable(humanCapable);
        country.setIndependentLaunchCapable(launchCapable);
        country.setReusableRocketCapable(reusableCapable);
        country.setDeepSpaceCapable(deepSpaceCapable);
        country.setSpaceStationCapable(stationCapable);
        country.setLunarLandingCapable(lunarCapable);
        country.setMarsLandingCapable(marsCapable);
        country.setOverallCapabilityScore(score);
        country.setRegion(region);
        country.setDescription(description);
        return countryRepository.save(country);
    }
}
