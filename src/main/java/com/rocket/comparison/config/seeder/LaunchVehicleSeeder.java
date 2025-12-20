package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.entity.LaunchVehicle;
import com.rocket.comparison.repository.LaunchVehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Seeds LaunchVehicle entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LaunchVehicleSeeder implements EntitySeeder {

    private final LaunchVehicleRepository launchVehicleRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding launch vehicles...");
            seedLaunchVehicles();
            log.info("Seeded {} launch vehicles", count());
        }
    }

    @Override
    public String getEntityName() {
        return "launch vehicles";
    }

    @Override
    public long count() {
        return launchVehicleRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedLaunchVehicles() {
        createLaunchVehicle("Falcon 9", "Falcon", "Block 5", getCountryMap().get("USA"), "SpaceX",
            70.0, 3.7, 549054.0, 2, 22800, 8300, null, null,
            "Merlin 1D", 9, "Merlin 1D Vacuum", 1, "RP-1 / LOX", 7600L,
            "Active", 2010, null, 300, 295, true, false, true,
            new BigDecimal("67000000"), new BigDecimal("2939"));

        createLaunchVehicle("Falcon Heavy", "Falcon", "Block 5", getCountryMap().get("USA"), "SpaceX",
            70.0, 12.2, 1420788.0, 2, 63800, 26700, 16800, null,
            "Merlin 1D", 27, "Merlin 1D Vacuum", 1, "RP-1 / LOX", 22819L,
            "Active", 2018, null, 10, 9, true, false, true,
            new BigDecimal("150000000"), new BigDecimal("2351"));

        createLaunchVehicle("Starship", "Starship", null, getCountryMap().get("USA"), "SpaceX",
            121.0, 9.0, 5000000.0, 2, 150000, 100000, 100000, 100000,
            "Raptor 2", 33, "Raptor 2 Vacuum", 6, "CH4 / LOX", 74500L,
            "In Development", 2023, null, 6, 3, true, true, true,
            new BigDecimal("100000000"), new BigDecimal("667"));

        createLaunchVehicle("SLS", "SLS", "Block 1", getCountryMap().get("USA"), "Boeing/NASA",
            98.0, 8.4, 2608000.0, 2, 95000, 27000, null, null,
            "RS-25", 4, "RL10", 1, "LH2 / LOX + SRBs", 39000L,
            "Active", 2022, null, 1, 1, false, true, false,
            new BigDecimal("2000000000"), new BigDecimal("21053"));

        createLaunchVehicle("New Glenn", "New Glenn", null, getCountryMap().get("USA"), "Blue Origin",
            98.0, 7.0, 1400000.0, 2, 45000, 13000, null, null,
            "BE-4", 7, "BE-3U", 2, "CH4 / LOX", 17100L,
            "In Development", 2024, null, 0, 0, true, false, false,
            new BigDecimal("68000000"), new BigDecimal("1511"));

        createLaunchVehicle("Soyuz-2", "Soyuz", "2.1b", getCountryMap().get("RUS"), "RKK Energia",
            46.3, 2.95, 312000.0, 3, 8200, 3250, null, null,
            "RD-108A", 4, "RD-0124", 1, "RP-1 / LOX", 4144L,
            "Active", 2004, null, 150, 145, false, true, true,
            new BigDecimal("48500000"), new BigDecimal("5915"));

        createLaunchVehicle("Proton-M", "Proton", "M", getCountryMap().get("RUS"), "Khrunichev",
            58.2, 7.4, 705000.0, 4, 23000, 6920, null, null,
            "RD-276", 6, "RD-0210", 4, "N2O4 / UDMH", 10000L,
            "Active", 2001, null, 115, 105, false, false, false,
            new BigDecimal("65000000"), new BigDecimal("2826"));

        createLaunchVehicle("Long March 5", "Long March", "5", getCountryMap().get("CHN"), "CALT",
            57.0, 5.0, 867000.0, 2, 25000, 14000, null, null,
            "YF-77", 2, "YF-75D", 2, "LH2 / LOX + RP-1", 10600L,
            "Active", 2016, null, 12, 11, false, false, true,
            new BigDecimal("100000000"), new BigDecimal("4000"));

        createLaunchVehicle("Ariane 6", "Ariane", "6", getCountryMap().get("ESA"), "ArianeGroup",
            63.0, 5.4, 530000.0, 2, 21650, 11500, null, null,
            "Vulcain 2.1", 1, "Vinci", 1, "LH2 / LOX + SRBs", 8000L,
            "Active", 2024, null, 1, 1, false, false, false,
            new BigDecimal("75000000"), new BigDecimal("3464"));

        createLaunchVehicle("H3", "H3", null, getCountryMap().get("JPN"), "JAXA/MHI",
            63.0, 5.2, 574000.0, 2, 6500, 4000, null, null,
            "LE-9", 3, "LE-5B-3", 1, "LH2 / LOX", 9800L,
            "Active", 2023, null, 3, 2, false, false, false,
            new BigDecimal("50000000"), new BigDecimal("7692"));

        createLaunchVehicle("LVM3", "LVM", "3", getCountryMap().get("IND"), "ISRO",
            43.5, 4.0, 640000.0, 3, 10000, 4000, null, null,
            "Vikas", 2, "CE-20", 1, "UDMH / N2O4 + Solid", 5150L,
            "Active", 2014, null, 8, 7, false, true, true,
            new BigDecimal("50000000"), new BigDecimal("5000"));

        createLaunchVehicle("PSLV", "PSLV", "XL", getCountryMap().get("IND"), "ISRO",
            44.0, 2.8, 320000.0, 4, 3800, 1425, null, null,
            "Vikas", 1, "PS-4", 2, "Solid + UDMH", 2960L,
            "Active", 1993, null, 60, 57, false, false, true,
            new BigDecimal("21000000"), new BigDecimal("5526"));

        createLaunchVehicle("Electron", "Electron", null, getCountryMap().get("NZL"), "Rocket Lab",
            18.0, 1.2, 12550.0, 2, 300, null, null, null,
            "Rutherford", 9, "Rutherford Vacuum", 1, "RP-1 / LOX", 224L,
            "Active", 2017, null, 50, 45, true, false, true,
            new BigDecimal("7500000"), new BigDecimal("25000"));

        createLaunchVehicle("Nuri", "Nuri", "KSLV-II", getCountryMap().get("KOR"), "KARI",
            47.2, 3.5, 200000.0, 3, 2600, 1500, null, null,
            "KRE-075", 4, "KRE-007", 1, "RP-1 / LOX", 3000L,
            "Active", 2021, null, 4, 3, false, false, false,
            new BigDecimal("50000000"), new BigDecimal("19231"));
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
}
