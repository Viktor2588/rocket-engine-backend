package com.rocket.comparison.config;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.integration.spacedevs.SpaceDevsSyncService;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Seeds the database with initial data on application startup.
 * Only runs when the database is empty.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)  // Run before other CommandLineRunners
public class DataSeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final EngineRepository engineRepository;
    private final LaunchVehicleRepository launchVehicleRepository;
    private final SpaceMilestoneRepository spaceMilestoneRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final SatelliteRepository satelliteRepository;
    private final LaunchSiteRepository launchSiteRepository;
    private final CapabilityScoreRepository capabilityScoreRepository;
    private final SpaceDevsSyncService spaceDevsSyncService;
    private final SyncStatusIndicator syncStatusIndicator;

    @Value("${sync.external.enabled:true}")
    private boolean externalSyncEnabled;

    @Value("${sync.external.missions-limit:200}")
    private int missionsLimit;

    @Value("${sync.external.sites-limit:100}")
    private int sitesLimit;

    private Map<String, Country> countryMap = new HashMap<>();

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database for missing seed data...");

        // Seed each entity type independently if missing
        if (countryRepository.count() == 0) {
            log.info("Seeding countries...");
            seedCountries();
        } else {
            // Load existing countries into map for reference
            loadExistingCountries();
        }

        if (engineRepository.count() == 0) {
            log.info("Seeding engines...");
            seedEngines();
        }

        if (launchVehicleRepository.count() == 0) {
            log.info("Seeding launch vehicles...");
            seedLaunchVehicles();
        }

        if (spaceMilestoneRepository.count() == 0) {
            log.info("Seeding milestones...");
            seedMilestones();
        }

        if (spaceMissionRepository.count() == 0) {
            log.info("Seeding missions...");
            seedMissions();
        }

        if (satelliteRepository.count() == 0) {
            log.info("Seeding satellites...");
            seedSatellites();
        }

        if (launchSiteRepository.count() == 0) {
            log.info("Seeding launch sites...");
            seedLaunchSites();
        }

        if (capabilityScoreRepository.count() == 0) {
            log.info("Seeding capability scores...");
            seedCapabilityScores();
        }

        log.info("Database check completed!");
        log.info("Current counts: {} countries, {} engines, {} launch vehicles, {} milestones, {} missions, {} satellites, {} launch sites",
            countryRepository.count(),
            engineRepository.count(),
            launchVehicleRepository.count(),
            spaceMilestoneRepository.count(),
            spaceMissionRepository.count(),
            satelliteRepository.count(),
            launchSiteRepository.count()
        );

        // Sync real data from TheSpaceDevs API after seeding
        syncFromExternalApi();
    }

    /**
     * Syncs additional data from TheSpaceDevs API.
     * Runs after initial seeding to enrich the database with real launch data.
     * Can be disabled via sync.external.enabled=false property (BE-060).
     * Records sync status to database for persistence across restarts (BE-061).
     */
    private void syncFromExternalApi() {
        if (!externalSyncEnabled) {
            log.info("External API sync is disabled (sync.external.enabled=false)");
            return;
        }

        // Start tracking sync status
        var syncStatus = syncStatusIndicator.startSync("spacedevs_api");
        int totalRecordsSynced = 0;

        try {
            log.info("Starting automatic sync from TheSpaceDevs API (missions={}, sites={})...",
                missionsLimit, sitesLimit);

            // Sync recent launches (past missions)
            var missionResults = spaceDevsSyncService.syncRecentLaunches(missionsLimit);
            log.info("Mission sync completed: {}", missionResults);
            totalRecordsSynced += extractSyncedCount(missionResults);

            // Sync launch sites
            var siteResults = spaceDevsSyncService.syncLaunchSites(sitesLimit);
            log.info("Launch site sync completed: {}", siteResults);
            totalRecordsSynced += extractSyncedCount(siteResults);

            // Record successful sync
            syncStatusIndicator.recordSyncSuccess(syncStatus, totalRecordsSynced);
            log.info("External API sync completed successfully! Total records synced: {}", totalRecordsSynced);
        } catch (Exception e) {
            // Record failed sync
            syncStatusIndicator.recordSyncFailure(syncStatus, e.getMessage());
            log.warn("External API sync failed (non-critical): {}", e.getMessage());
            // Don't fail startup if external API is unavailable
        }
    }

    /**
     * Extracts the number of synced records from a sync result map.
     */
    private int extractSyncedCount(Map<String, Object> result) {
        if (result == null) return 0;
        Object synced = result.get("synced");
        if (synced instanceof Number) {
            return ((Number) synced).intValue();
        }
        return 0;
    }

    private void loadExistingCountries() {
        countryRepository.findAll().forEach(country -> {
            countryMap.put(country.getIsoCode(), country);
        });
        log.info("Loaded {} existing countries", countryMap.size());
    }

    private void seedCountries() {
        log.info("Seeding countries...");

        Country usa = createCountry("United States", "USA", "https://flagcdn.com/w320/us.png",
            "National Aeronautics and Space Administration", "NASA", 1958,
            new BigDecimal("25000000000"), 0.48, 1800, 1720, 48, 18000,
            true, true, true, true, true, true, true, 98.5, "North America",
            "The United States has the most comprehensive space program.");
        countryMap.put("USA", usa);

        Country russia = createCountry("Russia", "RUS", "https://flagcdn.com/w320/ru.png",
            "Russian Federal Space Agency", "Roscosmos", 1992,
            new BigDecimal("5600000000"), 0.28, 3200, 3050, 25, 170000,
            true, true, false, true, true, true, false, 85.0, "Europe/Asia",
            "Russia pioneered human spaceflight with Yuri Gagarin.");
        countryMap.put("RUS", russia);

        Country china = createCountry("China", "CHN", "https://flagcdn.com/w320/cn.png",
            "China National Space Administration", "CNSA", 1993,
            new BigDecimal("14000000000"), 0.08, 450, 430, 18, 150000,
            true, true, false, true, true, true, true, 88.0, "Asia",
            "China operates the Tiangong space station.");
        countryMap.put("CHN", china);

        Country esa = createCountry("Europe", "ESA", "https://flagcdn.com/w320/eu.png",
            "European Space Agency", "ESA", 1975,
            new BigDecimal("7500000000"), 0.04, 320, 305, 7, 2200,
            false, true, false, true, false, false, false, 72.0, "Europe",
            "ESA represents 22 member states.");
        countryMap.put("ESA", esa);

        Country japan = createCountry("Japan", "JPN", "https://flagcdn.com/w320/jp.png",
            "Japan Aerospace Exploration Agency", "JAXA", 2003,
            new BigDecimal("3200000000"), 0.08, 120, 115, 7, 1500,
            false, true, false, true, false, true, false, 68.0, "Asia",
            "Japan operates the H-IIA/H3 rocket family.");
        countryMap.put("JPN", japan);

        Country india = createCountry("India", "IND", "https://flagcdn.com/w320/in.png",
            "Indian Space Research Organisation", "ISRO", 1969,
            new BigDecimal("1800000000"), 0.05, 130, 118, 4, 16000,
            false, true, false, true, false, true, false, 62.0, "Asia",
            "ISRO achieved Mars Orbiter Mission on first attempt.");
        countryMap.put("IND", india);

        Country korea = createCountry("South Korea", "KOR", "https://flagcdn.com/w320/kr.png",
            "Korea Aerospace Research Institute", "KARI", 1989,
            new BigDecimal("700000000"), 0.04, 8, 5, 1, 1000,
            false, true, false, false, false, false, false, 35.0, "Asia",
            "South Korea achieved independent launch capability in 2022.");
        countryMap.put("KOR", korea);

        Country israel = createCountry("Israel", "ISR", "https://flagcdn.com/w320/il.png",
            "Israel Space Agency", "ISA", 1983,
            new BigDecimal("200000000"), 0.04, 11, 9, 0, 200,
            false, true, false, false, false, false, false, 32.0, "Middle East",
            "Israel operates the Shavit launch vehicle.");
        countryMap.put("ISR", israel);

        Country uk = createCountry("United Kingdom", "GBR", "https://flagcdn.com/w320/gb.png",
            "UK Space Agency", "UKSA", 2010,
            new BigDecimal("800000000"), 0.02, 1, 0, 2, 200,
            false, false, false, false, false, false, false, 28.0, "Europe",
            "The UK participates in ESA.");
        countryMap.put("GBR", uk);

        Country canada = createCountry("Canada", "CAN", "https://flagcdn.com/w320/ca.png",
            "Canadian Space Agency", "CSA", 1989,
            new BigDecimal("400000000"), 0.02, 0, 0, 4, 700,
            false, false, false, false, false, false, false, 25.0, "North America",
            "Canada contributed the Canadarm robotic systems.");
        countryMap.put("CAN", canada);

        Country nz = createCountry("New Zealand", "NZL", "https://flagcdn.com/w320/nz.png",
            "New Zealand Space Agency", "NZSA", 2016,
            new BigDecimal("15000000"), 0.01, 45, 40, 0, 20,
            false, true, false, false, false, false, false, 30.0, "Oceania",
            "New Zealand hosts Rocket Lab operations.");
        countryMap.put("NZL", nz);

        Country ukraine = createCountry("Ukraine", "UKR", "https://flagcdn.com/w320/ua.png",
            "State Space Agency of Ukraine", "SSAU", 1992,
            new BigDecimal("50000000"), 0.03, 160, 145, 0, 7000,
            false, true, false, false, false, false, false, 35.0, "Europe",
            "Ukraine inherited significant space industry from the Soviet Union.");
        countryMap.put("UKR", ukraine);

        log.info("Seeded {} countries", countryRepository.count());
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

    private void seedEngines() {
        log.info("Seeding engines...");

        createEngine("Raptor 2", "USA", "SpaceX", "Starship", "Active", "1st stage",
            "CH4 / LOX", "Full-flow staged combustion", 350.0, 2300000L, 350.0, 1600.0, 3.55);
        createEngine("Merlin 1D", "USA", "SpaceX", "Falcon 9", "Active", "1st stage",
            "RP-1 / LOX", "Gas generator", 311.0, 845000L, 97.0, 470.0, 2.36);
        createEngine("RS-25", "USA", "Aerojet Rocketdyne", "SLS", "Active", "1st stage",
            "LH2 / LOX", "Staged combustion", 452.0, 2280000L, 206.0, 3177.0, 6.04);
        createEngine("BE-4", "USA", "Blue Origin", "New Glenn", "Active", "1st stage",
            "CH4 / LOX", "Oxidizer-rich staged combustion", 340.0, 2400000L, 134.0, 2300.0, null);
        createEngine("RD-180", "RUS", "NPO Energomash", "Atlas V", "Active", "1st stage",
            "RP-1 / LOX", "Oxidizer-rich staged combustion", 338.0, 4150000L, 267.0, 5480.0, 2.72);
        createEngine("RD-191", "RUS", "NPO Energomash", "Angara", "Active", "1st stage",
            "RP-1 / LOX", "Oxidizer-rich staged combustion", 337.0, 2090000L, 263.0, 2290.0, 2.63);
        createEngine("Vulcain 2", "ESA", "Safran", "Ariane 5", "Active", "1st stage",
            "LH2 / LOX", "Gas generator", 434.0, 1390000L, 115.0, 2100.0, 6.1);
        createEngine("YF-100", "CHN", "CNSA", "Long March 5", "Active", "1st stage",
            "RP-1 / LOX", "Pump-fed", 300.0, 1961000L, 250.0, null, null);
        createEngine("LE-7A", "JPN", "JAXA", "H-IIA", "Active", "1st stage",
            "LH2 / LOX", "Staged combustion", 428.0, 1098000L, 196.0, 1650.0, null);
        createEngine("Vikas", "IND", "ISRO", "GSLV", "Active", "2nd stage",
            "RP-1 / LOX", "Gas generator", 293.0, 803000L, 210.0, 1010.0, null);
        createEngine("Rutherford", "NZL", "Rocket Lab", "Electron", "Active", "1st stage",
            "RP-1 / LOX", "Electric pump-fed", 311.0, 25000L, 12.0, 35.0, null);
        createEngine("NK-33", "RUS", "Kuznetsov", "Soyuz-2.1v", "Retired", "1st stage",
            "RP-1 / LOX", "Staged combustion", 331.0, 1680000L, 145.0, 1240.0, 2.72);

        log.info("Seeded {} engines", engineRepository.count());
    }

    private void createEngine(String name, String origin, String designer, String vehicle,
            String status, String use, String propellant, String powerCycle,
            Double isp, Long thrustN, Double chamberPressure, Double massKg, Double ofRatio) {

        Engine engine = new Engine();
        engine.setName(name);
        engine.setOrigin(origin);
        engine.setDesigner(designer);
        engine.setVehicle(vehicle);
        engine.setStatus(status);
        engine.setUse(use);
        engine.setPropellant(propellant);
        engine.setPowerCycle(powerCycle);
        engine.setIsp_s(isp);
        engine.setThrustN(thrustN);
        engine.setChamberPressureBar(chamberPressure);
        engine.setMassKg(massKg);
        engine.setOfRatio(ofRatio);
        engineRepository.save(engine);
    }

    private void seedLaunchVehicles() {
        log.info("Seeding launch vehicles...");

        createLaunchVehicle("Falcon 9", "Falcon", "Block 5", countryMap.get("USA"), "SpaceX",
            70.0, 3.7, 549054.0, 2, 22800, 8300, null, null,
            "Merlin 1D", 9, "Merlin 1D Vacuum", 1, "RP-1 / LOX", 7600L,
            "Active", 2010, null, 300, 295, true, false, true,
            new BigDecimal("67000000"), new BigDecimal("2939"));

        createLaunchVehicle("Falcon Heavy", "Falcon", "Block 5", countryMap.get("USA"), "SpaceX",
            70.0, 12.2, 1420788.0, 2, 63800, 26700, 16800, null,
            "Merlin 1D", 27, "Merlin 1D Vacuum", 1, "RP-1 / LOX", 22819L,
            "Active", 2018, null, 10, 9, true, false, true,
            new BigDecimal("150000000"), new BigDecimal("2351"));

        createLaunchVehicle("Starship", "Starship", null, countryMap.get("USA"), "SpaceX",
            121.0, 9.0, 5000000.0, 2, 150000, 100000, 100000, 100000,
            "Raptor 2", 33, "Raptor 2 Vacuum", 6, "CH4 / LOX", 74500L,
            "In Development", 2023, null, 6, 3, true, true, true,
            new BigDecimal("100000000"), new BigDecimal("667"));

        createLaunchVehicle("SLS", "SLS", "Block 1", countryMap.get("USA"), "Boeing/NASA",
            98.0, 8.4, 2608000.0, 2, 95000, 27000, null, null,
            "RS-25", 4, "RL10", 1, "LH2 / LOX + SRBs", 39000L,
            "Active", 2022, null, 1, 1, false, true, false,
            new BigDecimal("2000000000"), new BigDecimal("21053"));

        createLaunchVehicle("New Glenn", "New Glenn", null, countryMap.get("USA"), "Blue Origin",
            98.0, 7.0, 1400000.0, 2, 45000, 13000, null, null,
            "BE-4", 7, "BE-3U", 2, "CH4 / LOX", 17100L,
            "In Development", 2024, null, 0, 0, true, false, false,
            new BigDecimal("68000000"), new BigDecimal("1511"));

        createLaunchVehicle("Soyuz-2", "Soyuz", "2.1b", countryMap.get("RUS"), "RKK Energia",
            46.3, 2.95, 312000.0, 3, 8200, 3250, null, null,
            "RD-108A", 4, "RD-0124", 1, "RP-1 / LOX", 4144L,
            "Active", 2004, null, 150, 145, false, true, true,
            new BigDecimal("48500000"), new BigDecimal("5915"));

        createLaunchVehicle("Proton-M", "Proton", "M", countryMap.get("RUS"), "Khrunichev",
            58.2, 7.4, 705000.0, 4, 23000, 6920, null, null,
            "RD-276", 6, "RD-0210", 4, "N2O4 / UDMH", 10000L,
            "Active", 2001, null, 115, 105, false, false, false,
            new BigDecimal("65000000"), new BigDecimal("2826"));

        createLaunchVehicle("Long March 5", "Long March", "5", countryMap.get("CHN"), "CALT",
            57.0, 5.0, 867000.0, 2, 25000, 14000, null, null,
            "YF-77", 2, "YF-75D", 2, "LH2 / LOX + RP-1", 10600L,
            "Active", 2016, null, 12, 11, false, false, true,
            new BigDecimal("100000000"), new BigDecimal("4000"));

        createLaunchVehicle("Ariane 6", "Ariane", "6", countryMap.get("ESA"), "ArianeGroup",
            63.0, 5.4, 530000.0, 2, 21650, 11500, null, null,
            "Vulcain 2.1", 1, "Vinci", 1, "LH2 / LOX + SRBs", 8000L,
            "Active", 2024, null, 1, 1, false, false, false,
            new BigDecimal("75000000"), new BigDecimal("3464"));

        createLaunchVehicle("H3", "H3", null, countryMap.get("JPN"), "JAXA/MHI",
            63.0, 5.2, 574000.0, 2, 6500, 4000, null, null,
            "LE-9", 3, "LE-5B-3", 1, "LH2 / LOX", 9800L,
            "Active", 2023, null, 3, 2, false, false, false,
            new BigDecimal("50000000"), new BigDecimal("7692"));

        createLaunchVehicle("LVM3", "LVM", "3", countryMap.get("IND"), "ISRO",
            43.5, 4.0, 640000.0, 3, 10000, 4000, null, null,
            "Vikas", 2, "CE-20", 1, "UDMH / N2O4 + Solid", 5150L,
            "Active", 2014, null, 8, 7, false, true, true,
            new BigDecimal("50000000"), new BigDecimal("5000"));

        createLaunchVehicle("PSLV", "PSLV", "XL", countryMap.get("IND"), "ISRO",
            44.0, 2.8, 320000.0, 4, 3800, 1425, null, null,
            "Vikas", 1, "PS-4", 2, "Solid + UDMH", 2960L,
            "Active", 1993, null, 60, 57, false, false, true,
            new BigDecimal("21000000"), new BigDecimal("5526"));

        createLaunchVehicle("Electron", "Electron", null, countryMap.get("NZL"), "Rocket Lab",
            18.0, 1.2, 12550.0, 2, 300, null, null, null,
            "Rutherford", 9, "Rutherford Vacuum", 1, "RP-1 / LOX", 224L,
            "Active", 2017, null, 50, 45, true, false, true,
            new BigDecimal("7500000"), new BigDecimal("25000"));

        createLaunchVehicle("Nuri", "Nuri", "KSLV-II", countryMap.get("KOR"), "KARI",
            47.2, 3.5, 200000.0, 3, 2600, 1500, null, null,
            "KRE-075", 4, "KRE-007", 1, "RP-1 / LOX", 3000L,
            "Active", 2021, null, 4, 3, false, false, false,
            new BigDecimal("50000000"), new BigDecimal("19231"));

        log.info("Seeded {} launch vehicles", launchVehicleRepository.count());
    }

    private void createLaunchVehicle(String name, String family, String variant, Country country,
            String manufacturer, Double height, Double diameter, Double mass, Integer stages,
            Integer payloadLeo, Integer payloadGto, Integer payloadMoon, Integer payloadMars,
            String firstEngines, Integer firstCount, String secondEngines, Integer secondCount,
            String propellant, Long thrust, String status, Integer firstFlight, Integer lastFlight,
            Integer totalLaunches, Integer successLaunches, Boolean reusable, Boolean humanRated,
            Boolean active, BigDecimal costPerLaunch, BigDecimal costPerKg) {

        LaunchVehicle lv = new LaunchVehicle();
        lv.setName(name);
        lv.setFamily(family);
        lv.setVariant(variant);
        lv.setCountry(country);
        lv.setManufacturer(manufacturer);
        lv.setHeightMeters(height);
        lv.setDiameterMeters(diameter);
        lv.setMassKg(mass);
        lv.setStages(stages);
        lv.setPayloadToLeoKg(payloadLeo);
        lv.setPayloadToGtoKg(payloadGto);
        lv.setPayloadToMoonKg(payloadMoon);
        lv.setPayloadToMarsKg(payloadMars);
        lv.setFirstStageEngines(firstEngines);
        lv.setFirstStageEngineCount(firstCount);
        lv.setSecondStageEngines(secondEngines);
        lv.setSecondStageEngineCount(secondCount);
        lv.setPropellant(propellant);
        lv.setThrustAtLiftoffKn(thrust);
        lv.setStatus(status);
        lv.setFirstFlightYear(firstFlight);
        lv.setLastFlightYear(lastFlight);
        lv.setTotalLaunches(totalLaunches);
        lv.setSuccessfulLaunches(successLaunches);
        lv.setReusable(reusable);
        lv.setHumanRated(humanRated);
        lv.setActive(active);
        lv.setCostPerLaunchUsd(costPerLaunch);
        lv.setCostPerKgToLeoUsd(costPerKg);
        launchVehicleRepository.save(lv);
    }

    private void seedMilestones() {
        log.info("Seeding milestones...");

        createMilestone(countryMap.get("RUS"), MilestoneType.FIRST_SATELLITE, LocalDate.of(1957, 10, 4),
            1, "First Artificial Satellite", "Sputnik 1", "Sputnik 1",
            "The Soviet Union launched the first artificial satellite into orbit.");

        createMilestone(countryMap.get("RUS"), MilestoneType.FIRST_HUMAN_IN_SPACE, LocalDate.of(1961, 4, 12),
            1, "First Human in Space", "Yuri Gagarin", "Vostok 1",
            "Yuri Gagarin became the first human to journey into outer space.");

        createMilestone(countryMap.get("USA"), MilestoneType.FIRST_HUMAN_LUNAR_LANDING, LocalDate.of(1969, 7, 20),
            1, "First Human Moon Landing", "Neil Armstrong & Buzz Aldrin", "Apollo 11",
            "Apollo 11 astronauts became the first humans to walk on the Moon.");

        createMilestone(countryMap.get("USA"), MilestoneType.FIRST_MARS_LANDING, LocalDate.of(1976, 7, 20),
            1, "First Mars Landing", "Viking 1", "Viking 1",
            "NASA's Viking 1 became the first spacecraft to successfully land on Mars.");

        createMilestone(countryMap.get("USA"), MilestoneType.FIRST_REUSABLE_ROCKET, LocalDate.of(2015, 12, 21),
            1, "First Orbital Rocket Landing", "Falcon 9", "ORBCOMM-2",
            "SpaceX successfully landed an orbital rocket booster for the first time.");

        createMilestone(countryMap.get("CHN"), MilestoneType.FIRST_LUNAR_FAR_SIDE_LANDING, LocalDate.of(2019, 1, 3),
            1, "First Lunar Far Side Landing", "Chang'e 4", "Chang'e 4",
            "China's Chang'e 4 became the first spacecraft to land on the far side of the Moon.");

        createMilestone(countryMap.get("IND"), MilestoneType.FIRST_LUNAR_SOUTH_POLE_LANDING, LocalDate.of(2023, 8, 23),
            1, "First Lunar South Pole Landing", "Chandrayaan-3", "Chandrayaan-3",
            "India's Chandrayaan-3 became the first spacecraft to land near the lunar south pole.");

        createMilestone(countryMap.get("USA"), MilestoneType.FIRST_MARS_HELICOPTER, LocalDate.of(2021, 4, 19),
            1, "First Powered Flight on Mars", "Ingenuity", "Mars 2020",
            "NASA's Ingenuity helicopter achieved the first powered flight on another planet.");

        log.info("Seeded {} milestones", spaceMilestoneRepository.count());
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

    private void seedMissions() {
        log.info("Seeding missions...");

        createMission("Apollo 11", countryMap.get("USA"), MissionType.LUNAR_CREWED_LANDING, MissionStatus.COMPLETED,
            LocalDate.of(1969, 7, 16), LocalDate.of(1969, 7, 24), Destination.LUNAR_SURFACE,
            "First crewed Moon landing mission.", true, true);

        createMission("Voyager 1", countryMap.get("USA"), MissionType.INTERSTELLAR_MISSION, MissionStatus.ACTIVE,
            LocalDate.of(1977, 9, 5), null, Destination.INTERSTELLAR,
            "Farthest human-made object from Earth.", false, true);

        createMission("Mars 2020", countryMap.get("USA"), MissionType.MARS_ROVER, MissionStatus.ACTIVE,
            LocalDate.of(2020, 7, 30), null, Destination.MARS_SURFACE,
            "Perseverance rover and Ingenuity helicopter.", false, true);

        createMission("Artemis I", countryMap.get("USA"), MissionType.LUNAR_CREWED_ORBIT, MissionStatus.COMPLETED,
            LocalDate.of(2022, 11, 16), LocalDate.of(2022, 12, 11), Destination.LUNAR_ORBIT,
            "First uncrewed test flight of SLS and Orion.", false, true);

        createMission("Tianwen-1", countryMap.get("CHN"), MissionType.MARS_ROVER, MissionStatus.ACTIVE,
            LocalDate.of(2020, 7, 23), null, Destination.MARS_SURFACE,
            "China's first Mars rover mission.", false, true);

        createMission("Chandrayaan-3", countryMap.get("IND"), MissionType.LUNAR_LANDER, MissionStatus.COMPLETED,
            LocalDate.of(2023, 7, 14), LocalDate.of(2023, 9, 3), Destination.LUNAR_SURFACE,
            "India's successful lunar south pole landing.", false, true);

        createMission("James Webb Space Telescope", countryMap.get("USA"), MissionType.SPACE_TELESCOPE, MissionStatus.ACTIVE,
            LocalDate.of(2021, 12, 25), null, Destination.SUN_EARTH_L2,
            "Most powerful space telescope ever built.", false, true);

        createMission("Hayabusa2", countryMap.get("JPN"), MissionType.ASTEROID_SAMPLE_RETURN, MissionStatus.COMPLETED,
            LocalDate.of(2014, 12, 3), LocalDate.of(2020, 12, 6), Destination.ASTEROID,
            "Returned samples from asteroid Ryugu.", false, true);

        log.info("Seeded {} missions", spaceMissionRepository.count());
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

    private void seedSatellites() {
        log.info("Seeding satellites...");

        createSatellite("International Space Station", countryMap.get("USA"), SatelliteType.SPACE_STATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(1998, 11, 20), OrbitType.ISS_ORBIT, 420.0, 419700.0,
            "NASA/Roscosmos/ESA/JAXA/CSA", null, "Largest modular space station.");

        createSatellite("Tiangong", countryMap.get("CHN"), SatelliteType.SPACE_STATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 4, 29), OrbitType.LEO, 390.0, 66000.0,
            "CNSA", null, "China's modular space station.");

        createSatellite("Hubble Space Telescope", countryMap.get("USA"), SatelliteType.ASTRONOMY,
            SatelliteStatus.OPERATIONAL, LocalDate.of(1990, 4, 24), OrbitType.LEO, 540.0, 11110.0,
            "NASA/ESA", null, "Iconic space telescope.");

        createSatellite("James Webb Space Telescope", countryMap.get("USA"), SatelliteType.ASTRONOMY,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 12, 25), OrbitType.L2, 1500000.0, 6500.0,
            "NASA/ESA/CSA", null, "Infrared space observatory.");

        createSatellite("GPS III", countryMap.get("USA"), SatelliteType.NAVIGATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2018, 12, 23), OrbitType.NAVIGATION_MEO, 20200.0, 3880.0,
            "US Space Force", "GPS", "Latest GPS satellite generation.");

        createSatellite("Galileo", countryMap.get("ESA"), SatelliteType.NAVIGATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2016, 12, 17), OrbitType.NAVIGATION_MEO, 23222.0, 700.0,
            "ESA/EU", "Galileo", "European navigation system.");

        createSatellite("Starlink", countryMap.get("USA"), SatelliteType.COMMUNICATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2019, 5, 24), OrbitType.LEO, 550.0, 260.0,
            "SpaceX", "Starlink", "Global internet constellation.");

        createSatellite("Landsat 9", countryMap.get("USA"), SatelliteType.EARTH_OBSERVATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 9, 27), OrbitType.SSO, 705.0, 2864.0,
            "NASA/USGS", "Landsat", "Earth observation satellite.");

        log.info("Seeded {} satellites", satelliteRepository.count());
    }

    private void createSatellite(String name, Country country, SatelliteType type,
            SatelliteStatus status, LocalDate launchDate, OrbitType orbitType, Double altitude,
            Double mass, String operator, String constellation, String description) {

        Satellite satellite = new Satellite();
        satellite.setName(name);
        satellite.setCountry(country);
        satellite.setSatelliteType(type);
        satellite.setStatus(status);
        satellite.setLaunchDate(launchDate);
        satellite.setOrbitType(orbitType);
        satellite.setAltitudeKm(altitude);
        satellite.setMassKg(mass);
        satellite.setOperator(operator);
        satellite.setConstellation(constellation);
        satellite.setPurpose(description);
        satelliteRepository.save(satellite);
    }

    private void seedLaunchSites() {
        log.info("Seeding launch sites...");

        createLaunchSite("Kennedy Space Center", countryMap.get("USA"), "Florida", "NASA",
            28.5729, -80.6490, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1962, 1000, "Historic launch site for Apollo and Space Shuttle.");

        createLaunchSite("Cape Canaveral SFS", countryMap.get("USA"), "Florida", "US Space Force",
            28.4889, -80.5778, LaunchSiteStatus.OPERATIONAL, true, true, false, false,
            1950, 900, "America's busiest spaceport.");

        createLaunchSite("Vandenberg SFB", countryMap.get("USA"), "California", "US Space Force",
            34.7420, -120.5724, LaunchSiteStatus.OPERATIONAL, true, false, true, false,
            1958, 700, "Primary west coast launch site.");

        createLaunchSite("Baikonur Cosmodrome", countryMap.get("RUS"), "Kazakhstan", "Roscosmos",
            45.9650, 63.3050, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1955, 1500, "World's oldest and largest launch facility.");

        createLaunchSite("Plesetsk Cosmodrome", countryMap.get("RUS"), "Arkhangelsk Oblast", "Russian Space Forces",
            62.9258, 40.5778, LaunchSiteStatus.OPERATIONAL, false, false, true, false,
            1957, 1600, "Northern Russian launch site.");

        createLaunchSite("Jiuquan", countryMap.get("CHN"), "Inner Mongolia", "CNSA",
            40.9606, 100.2914, LaunchSiteStatus.OPERATIONAL, true, false, false, true,
            1958, 200, "China's oldest launch center.");

        createLaunchSite("Wenchang", countryMap.get("CHN"), "Hainan", "CNSA",
            19.6145, 110.9510, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            2014, 15, "China's newest and largest launch site.");

        createLaunchSite("Guiana Space Centre", countryMap.get("ESA"), "French Guiana", "CNES/Arianespace",
            5.2322, -52.7693, LaunchSiteStatus.OPERATIONAL, true, true, true, false,
            1968, 300, "European spaceport near the equator.");

        createLaunchSite("Tanegashima", countryMap.get("JPN"), "Kagoshima", "JAXA",
            30.4000, 130.9689, LaunchSiteStatus.OPERATIONAL, true, true, true, false,
            1969, 90, "Japan's largest launch center.");

        createLaunchSite("Satish Dhawan", countryMap.get("IND"), "Andhra Pradesh", "ISRO",
            13.7199, 80.2304, LaunchSiteStatus.OPERATIONAL, true, true, true, true,
            1971, 100, "India's primary launch site.");

        createLaunchSite("Rocket Lab LC-1", countryMap.get("NZL"), "Hawkes Bay", "Rocket Lab",
            -39.2615, 177.8649, LaunchSiteStatus.OPERATIONAL, false, false, false, false,
            2016, 45, "World's first private orbital launch site.");

        log.info("Seeded {} launch sites", launchSiteRepository.count());
    }

    private void createLaunchSite(String name, Country country, String region, String operator,
            Double latitude, Double longitude, LaunchSiteStatus status,
            Boolean supportsInterplanetary, Boolean supportsGeo, Boolean supportsPolar,
            Boolean humanRated, Integer established, Integer totalLaunches, String description) {

        LaunchSite site = new LaunchSite();
        site.setName(name);
        site.setCountry(country);
        site.setRegion(region);
        site.setOperator(operator);
        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setStatus(status);
        site.setSupportsInterplanetary(supportsInterplanetary);
        site.setSupportsGeo(supportsGeo);
        site.setSupportsPolar(supportsPolar);
        site.setHumanRatedCapable(humanRated);
        site.setEstablishedYear(established);
        site.setTotalLaunches(totalLaunches);
        site.setDescription(description);
        launchSiteRepository.save(site);
    }

    private void seedCapabilityScores() {
        log.info("Seeding capability scores...");

        // USA - Global leader in all categories
        seedCountryScores("USA", 98.0, 95.0, 97.0, 96.0, 98.0, 95.0, 98.0);

        // Russia - Strong legacy, declining but capable
        seedCountryScores("RUS", 82.0, 85.0, 88.0, 45.0, 65.0, 78.0, 85.0);

        // China - Rapidly advancing
        seedCountryScores("CHN", 85.0, 75.0, 82.0, 70.0, 78.0, 80.0, 90.0);

        // ESA - Strong collaborative program
        seedCountryScores("ESA", 72.0, 70.0, 55.0, 68.0, 75.0, 65.0, 50.0);

        // Japan - Sophisticated but small
        seedCountryScores("JPN", 68.0, 65.0, 45.0, 72.0, 70.0, 60.0, 70.0);

        // India - Cost-effective, growing
        seedCountryScores("IND", 62.0, 55.0, 30.0, 60.0, 55.0, 50.0, 75.0);

        // South Korea - New entrant
        seedCountryScores("KOR", 35.0, 30.0, 5.0, 20.0, 35.0, 25.0, 40.0);

        // Israel - Small but capable
        seedCountryScores("ISR", 32.0, 25.0, 5.0, 15.0, 45.0, 20.0, 50.0);

        // UK - ESA member, limited independent
        seedCountryScores("GBR", 15.0, 25.0, 10.0, 20.0, 40.0, 15.0, 20.0);

        // Canada - Partner, specialized in robotics
        seedCountryScores("CAN", 5.0, 10.0, 35.0, 20.0, 45.0, 10.0, 15.0);

        // New Zealand - Rocket Lab base
        seedCountryScores("NZL", 45.0, 40.0, 0.0, 15.0, 20.0, 35.0, 30.0);

        // Ukraine - Legacy from USSR
        seedCountryScores("UKR", 25.0, 50.0, 15.0, 10.0, 20.0, 15.0, 35.0);

        // Additional countries (from TheSpaceDevs sync)
        // France - Strong space power, part of ESA
        seedCountryScores("FRA", 65.0, 60.0, 40.0, 55.0, 65.0, 55.0, 45.0);

        // Germany - Strong aerospace industry
        seedCountryScores("DEU", 45.0, 55.0, 25.0, 40.0, 55.0, 40.0, 35.0);

        // Italy - Active ESA partner
        seedCountryScores("ITA", 40.0, 45.0, 20.0, 35.0, 50.0, 35.0, 30.0);

        // Australia - Emerging space nation
        seedCountryScores("AUS", 20.0, 15.0, 5.0, 10.0, 30.0, 25.0, 20.0);

        // Brazil - Developing launch capability
        seedCountryScores("BRA", 25.0, 20.0, 5.0, 10.0, 25.0, 20.0, 30.0);

        // Iran - Indigenous launch capability
        seedCountryScores("IRN", 30.0, 25.0, 5.0, 5.0, 20.0, 15.0, 45.0);

        // North Korea - Basic launch capability
        seedCountryScores("PRK", 20.0, 15.0, 0.0, 0.0, 10.0, 10.0, 40.0);

        // Spain - ESA member
        seedCountryScores("ESP", 15.0, 20.0, 10.0, 15.0, 35.0, 20.0, 15.0);

        // Netherlands - ESA member, ESTEC host
        seedCountryScores("NLD", 10.0, 25.0, 15.0, 20.0, 35.0, 25.0, 15.0);

        // Belgium - ESA member
        seedCountryScores("BEL", 10.0, 15.0, 10.0, 15.0, 25.0, 15.0, 10.0);

        // Sweden - Strong satellite industry
        seedCountryScores("SWE", 15.0, 20.0, 10.0, 15.0, 40.0, 20.0, 20.0);

        // Norway - Andøya launch site
        seedCountryScores("NOR", 20.0, 15.0, 5.0, 10.0, 30.0, 25.0, 15.0);

        // Switzerland - ESA member
        seedCountryScores("CHE", 5.0, 15.0, 10.0, 15.0, 25.0, 15.0, 10.0);

        // Austria - ESA member
        seedCountryScores("AUT", 5.0, 10.0, 5.0, 10.0, 20.0, 10.0, 5.0);

        // Poland - ESA member
        seedCountryScores("POL", 5.0, 10.0, 5.0, 10.0, 20.0, 10.0, 10.0);

        log.info("Seeded {} capability scores", capabilityScoreRepository.count());
    }

    private void seedCountryScores(String isoCode, double launch, double propulsion, double human,
            double deepSpace, double satellite, double infrastructure, double independence) {
        Country country = countryMap.get(isoCode);
        if (country == null) {
            log.warn("Country not found for ISO code: {}", isoCode);
            return;
        }

        CapabilityCategory[] categories = CapabilityCategory.values();
        double[] scores = {launch, propulsion, human, deepSpace, satellite, infrastructure, independence};

        for (int i = 0; i < categories.length; i++) {
            CapabilityScore score = new CapabilityScore(country, categories[i], scores[i]);
            score.setRanking(calculateRanking(categories[i], scores[i]));
            capabilityScoreRepository.save(score);
        }
    }

    private int calculateRanking(CapabilityCategory category, double score) {
        // Simplified ranking based on score thresholds
        if (score >= 90) return 1;
        if (score >= 80) return 2;
        if (score >= 70) return 3;
        if (score >= 60) return 4;
        if (score >= 50) return 5;
        if (score >= 40) return 6;
        if (score >= 30) return 7;
        if (score >= 20) return 8;
        if (score >= 10) return 9;
        return 10;
    }
}
