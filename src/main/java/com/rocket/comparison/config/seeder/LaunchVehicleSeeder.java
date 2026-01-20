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
 * Contains 80+ launch vehicles from around the world.
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
        // ==================== SpaceX ====================
        createLaunchVehicle("Falcon 1", "Falcon", null, getCountryMap().get("USA"), "SpaceX",
            21.3, 1.7, 38555.0, 2, 670, null, null, null,
            "Merlin 1A/1C", 1, "Kestrel", 1, "RP-1 / LOX", 454L,
            "Retired", 2006, 2009, 5, 2, false, false, false,
            new BigDecimal("7000000"), new BigDecimal("10448"));

        createLaunchVehicle("Falcon 9 v1.0", "Falcon", "v1.0", getCountryMap().get("USA"), "SpaceX",
            54.9, 3.7, 333400.0, 2, 10450, 4540, null, null,
            "Merlin 1C", 9, "Merlin 1C Vacuum", 1, "RP-1 / LOX", 4940L,
            "Retired", 2010, 2013, 5, 5, false, false, false,
            new BigDecimal("54000000"), new BigDecimal("5167"));

        createLaunchVehicle("Falcon 9 v1.1", "Falcon", "v1.1", getCountryMap().get("USA"), "SpaceX",
            68.4, 3.7, 505846.0, 2, 13150, 4850, null, null,
            "Merlin 1D", 9, "Merlin 1D Vacuum", 1, "RP-1 / LOX", 5885L,
            "Retired", 2013, 2016, 15, 14, false, false, false,
            new BigDecimal("56500000"), new BigDecimal("4297"));

        createLaunchVehicle("Falcon 9 Full Thrust", "Falcon", "FT", getCountryMap().get("USA"), "SpaceX",
            70.0, 3.7, 549054.0, 2, 22800, 8300, null, null,
            "Merlin 1D+", 9, "Merlin 1D+ Vacuum", 1, "RP-1 / LOX", 7607L,
            "Retired", 2015, 2018, 35, 34, true, false, true,
            new BigDecimal("62000000"), new BigDecimal("2719"));

        createLaunchVehicle("Falcon 9 Block 5", "Falcon", "Block 5", getCountryMap().get("USA"), "SpaceX",
            70.0, 3.7, 549054.0, 2, 22800, 8300, null, null,
            "Merlin 1D++", 9, "Merlin 1D Vacuum++", 1, "RP-1 / LOX", 7607L,
            "Active", 2018, null, 320, 318, true, true, true,
            new BigDecimal("67000000"), new BigDecimal("2939"));

        createLaunchVehicle("Falcon Heavy", "Falcon Heavy", "Block 5", getCountryMap().get("USA"), "SpaceX",
            70.0, 12.2, 1420788.0, 2, 63800, 26700, 16800, null,
            "Merlin 1D++", 27, "Merlin 1D Vacuum++", 1, "RP-1 / LOX", 22819L,
            "Active", 2018, null, 12, 12, true, false, true,
            new BigDecimal("150000000"), new BigDecimal("2351"));

        createLaunchVehicle("Starship", "Starship", null, getCountryMap().get("USA"), "SpaceX",
            121.0, 9.0, 5000000.0, 2, 150000, 100000, 100000, 100000,
            "Raptor 2", 33, "Raptor 2 Vacuum", 6, "CH4 / LOX", 74500L,
            "In Development", 2023, null, 7, 4, true, true, true,
            new BigDecimal("100000000"), new BigDecimal("667"));

        createLaunchVehicle("Crew Dragon", "Dragon", "Crew", getCountryMap().get("USA"), "SpaceX",
            8.1, 4.0, 12519.0, 1, null, null, null, null,
            "SuperDraco", 8, null, null, "MMH / NTO", 71L,
            "Active", 2020, null, 15, 15, true, true, true,
            null, null);

        createLaunchVehicle("Cargo Dragon", "Dragon", "Cargo", getCountryMap().get("USA"), "SpaceX",
            8.1, 4.0, 12519.0, 1, null, null, null, null,
            "Draco", 16, null, null, "MMH / NTO", 4L,
            "Active", 2020, null, 12, 12, true, false, true,
            null, null);

        // ==================== Blue Origin ====================
        createLaunchVehicle("New Shepard", "New Shepard", null, getCountryMap().get("USA"), "Blue Origin",
            18.0, 3.7, 75000.0, 1, null, null, null, null,
            "BE-3", 1, null, null, "LH2 / LOX", 490L,
            "Active", 2015, null, 25, 24, true, true, true,
            null, null);

        createLaunchVehicle("New Glenn", "New Glenn", null, getCountryMap().get("USA"), "Blue Origin",
            98.0, 7.0, 1400000.0, 2, 45000, 13000, null, null,
            "BE-4", 7, "BE-3U", 2, "CH4 / LOX", 17100L,
            "Active", 2025, null, 1, 1, true, false, true,
            new BigDecimal("68000000"), new BigDecimal("1511"));

        // ==================== ULA ====================
        createLaunchVehicle("Atlas V 401", "Atlas V", "401", getCountryMap().get("USA"), "ULA",
            58.3, 3.81, 334500.0, 2, 9797, 4950, null, null,
            "RD-180", 1, "RL-10A", 1, "RP-1 / LOX + LH2", 3827L,
            "Active", 2002, null, 65, 65, false, false, true,
            new BigDecimal("109000000"), new BigDecimal("11125"));

        createLaunchVehicle("Atlas V 551", "Atlas V", "551", getCountryMap().get("USA"), "ULA",
            62.2, 3.81, 540000.0, 2, 18850, 8900, null, null,
            "RD-180", 1, "RL-10A", 1, "RP-1 / LOX + LH2 + SRBs", 4152L,
            "Active", 2006, null, 12, 12, false, false, true,
            new BigDecimal("153000000"), new BigDecimal("8117"));

        createLaunchVehicle("Atlas V N22", "Atlas V", "N22", getCountryMap().get("USA"), "ULA",
            58.3, 3.81, 388000.0, 2, 13600, null, null, null,
            "RD-180", 1, "RL-10C", 2, "RP-1 / LOX + LH2 + SRBs", 4152L,
            "Active", 2019, null, 4, 4, false, true, true,
            new BigDecimal("140000000"), new BigDecimal("10294"));

        createLaunchVehicle("Delta II", "Delta", "II 7925", getCountryMap().get("USA"), "Boeing/ULA",
            39.0, 2.44, 231870.0, 3, 6100, 2170, null, null,
            "RS-27A", 1, "AJ10-118K", 1, "RP-1 / LOX + Solid", 2850L,
            "Retired", 1989, 2018, 155, 153, false, false, false,
            new BigDecimal("65000000"), new BigDecimal("10656"));

        createLaunchVehicle("Delta IV Heavy", "Delta IV", "Heavy", getCountryMap().get("USA"), "ULA",
            72.0, 5.1, 733000.0, 2, 28790, 14220, null, null,
            "RS-68A", 3, "RL-10B-2", 1, "LH2 / LOX", 9420L,
            "Retired", 2004, 2024, 16, 15, false, true, false,
            new BigDecimal("350000000"), new BigDecimal("12157"));

        createLaunchVehicle("Vulcan Centaur", "Vulcan", "VC2S", getCountryMap().get("USA"), "ULA",
            61.6, 5.4, 546700.0, 2, 27200, 14400, null, null,
            "BE-4", 2, "RL-10C", 2, "CH4 / LOX + LH2", 4900L,
            "Active", 2024, null, 3, 3, false, false, true,
            new BigDecimal("110000000"), new BigDecimal("4044"));

        // ==================== NASA/Government ====================
        createLaunchVehicle("SLS Block 1", "SLS", "Block 1", getCountryMap().get("USA"), "Boeing/NASA",
            98.0, 8.4, 2608000.0, 2, 95000, 27000, null, null,
            "RS-25", 4, "RL-10", 1, "LH2 / LOX + SRBs", 39000L,
            "Active", 2022, null, 2, 2, false, true, true,
            new BigDecimal("2000000000"), new BigDecimal("21053"));

        createLaunchVehicle("Space Shuttle", "Space Shuttle", null, getCountryMap().get("USA"), "NASA/Rockwell",
            56.1, 8.7, 2030000.0, 2, 24400, null, null, null,
            "RS-25", 3, null, null, "LH2 / LOX + SRBs", 30160L,
            "Retired", 1981, 2011, 135, 133, true, true, false,
            new BigDecimal("450000000"), new BigDecimal("18443"));

        createLaunchVehicle("Saturn V", "Saturn", "V", getCountryMap().get("USA"), "Boeing/NASA",
            110.6, 10.1, 2970000.0, 3, 140000, 48600, 43500, null,
            "F-1", 5, "J-2", 6, "RP-1 / LOX + LH2", 35100L,
            "Retired", 1967, 1973, 13, 12, false, true, false,
            new BigDecimal("185000000"), new BigDecimal("1321"));

        createLaunchVehicle("Saturn IB", "Saturn", "IB", getCountryMap().get("USA"), "Chrysler/NASA",
            68.1, 6.6, 589770.0, 2, 21000, null, null, null,
            "H-1", 8, "J-2", 1, "RP-1 / LOX + LH2", 7100L,
            "Retired", 1966, 1975, 9, 9, false, true, false,
            new BigDecimal("55000000"), new BigDecimal("2619"));

        // ==================== Rocket Lab ====================
        createLaunchVehicle("Electron", "Electron", null, getCountryMap().get("NZL"), "Rocket Lab",
            18.0, 1.2, 12550.0, 2, 300, null, null, null,
            "Rutherford", 9, "Rutherford Vacuum", 1, "RP-1 / LOX", 224L,
            "Active", 2017, null, 55, 50, true, false, true,
            new BigDecimal("7500000"), new BigDecimal("25000"));

        createLaunchVehicle("Neutron", "Neutron", null, getCountryMap().get("NZL"), "Rocket Lab",
            40.0, 4.5, 480000.0, 2, 13000, 1500, null, null,
            "Archimedes", 9, "Archimedes Vacuum", 1, "CH4 / LOX", 3600L,
            "In Development", 2025, null, 0, 0, true, false, false,
            new BigDecimal("50000000"), new BigDecimal("3846"));

        // ==================== Other US ====================
        createLaunchVehicle("Firefly Alpha", "Alpha", null, getCountryMap().get("USA"), "Firefly Aerospace",
            29.0, 1.8, 54000.0, 2, 1170, 630, null, null,
            "Reaver 1", 4, "Lightning 1", 1, "RP-1 / LOX", 736L,
            "Active", 2021, null, 9, 6, false, false, true,
            new BigDecimal("15000000"), new BigDecimal("12821"));

        createLaunchVehicle("Terran 1", "Terran", "1", getCountryMap().get("USA"), "Relativity Space",
            33.5, 2.3, 90700.0, 2, 1250, null, null, null,
            "Aeon 1", 9, "Aeon Vacuum", 1, "CH4 / LOX", 900L,
            "In Development", 2023, null, 1, 0, false, false, false,
            new BigDecimal("12000000"), new BigDecimal("9600"));

        createLaunchVehicle("Antares 230", "Antares", "230", getCountryMap().get("USA"), "Northrop Grumman",
            42.5, 3.9, 298000.0, 2, 8000, null, null, null,
            "RD-181", 2, "Castor 30XL", 1, "RP-1 / LOX + Solid", 3844L,
            "Retired", 2016, 2023, 8, 8, false, false, false,
            new BigDecimal("85000000"), new BigDecimal("10625"));

        createLaunchVehicle("Pegasus XL", "Pegasus", "XL", getCountryMap().get("USA"), "Orbital ATK",
            17.6, 1.3, 23130.0, 3, 443, null, null, null,
            "Orion 50SXLG", 1, "Orion 50XL", 1, "Solid", 726L,
            "Active", 1994, null, 39, 34, false, false, true,
            new BigDecimal("40000000"), new BigDecimal("90293"));

        createLaunchVehicle("LauncherOne", "LauncherOne", null, getCountryMap().get("USA"), "Virgin Orbit",
            21.3, 1.6, 30000.0, 2, 500, null, null, null,
            "NewtonThree", 1, "NewtonFour", 1, "RP-1 / LOX", 327L,
            "Retired", 2020, 2023, 6, 4, false, false, false,
            new BigDecimal("12000000"), new BigDecimal("24000"));

        // ==================== Russian ====================
        createLaunchVehicle("Soyuz-2.1a", "Soyuz", "2.1a", getCountryMap().get("RUS"), "RKK Energia",
            46.3, 2.95, 312000.0, 3, 7800, 3250, null, null,
            "RD-107A", 4, "RD-0110", 1, "RP-1 / LOX", 4220L,
            "Active", 2004, null, 75, 73, false, true, true,
            new BigDecimal("48500000"), new BigDecimal("6218"));

        createLaunchVehicle("Soyuz-2.1b", "Soyuz", "2.1b", getCountryMap().get("RUS"), "RKK Energia",
            46.3, 2.95, 312000.0, 3, 8200, 3250, null, null,
            "RD-107A", 4, "RD-0124", 1, "RP-1 / LOX", 4220L,
            "Active", 2006, null, 85, 82, false, true, true,
            new BigDecimal("48500000"), new BigDecimal("5915"));

        createLaunchVehicle("Soyuz-2.1v", "Soyuz", "2.1v", getCountryMap().get("RUS"), "TsSKB-Progress",
            44.0, 2.95, 158000.0, 2, 2850, null, null, null,
            "NK-33", 1, "RD-0124", 1, "RP-1 / LOX", 1631L,
            "Active", 2013, null, 12, 11, false, false, true,
            new BigDecimal("35000000"), new BigDecimal("12281"));

        createLaunchVehicle("Proton-M", "Proton", "M", getCountryMap().get("RUS"), "Khrunichev",
            58.2, 7.4, 705000.0, 4, 23000, 6920, null, null,
            "RD-276", 6, "RD-0210", 4, "N2O4 / UDMH", 10000L,
            "Active", 2001, null, 115, 105, false, false, true,
            new BigDecimal("65000000"), new BigDecimal("2826"));

        createLaunchVehicle("Angara A5", "Angara", "A5", getCountryMap().get("RUS"), "Khrunichev",
            64.0, 8.86, 773000.0, 3, 24500, 5400, null, null,
            "RD-191", 5, "RD-0124A", 1, "RP-1 / LOX", 9800L,
            "Active", 2014, null, 7, 6, false, false, true,
            new BigDecimal("100000000"), new BigDecimal("4082"));

        createLaunchVehicle("Angara 1.2", "Angara", "1.2", getCountryMap().get("RUS"), "Khrunichev",
            41.5, 2.9, 171000.0, 2, 3500, null, null, null,
            "RD-191", 1, "RD-0124A", 1, "RP-1 / LOX", 1920L,
            "Active", 2014, null, 4, 3, false, false, true,
            new BigDecimal("40000000"), new BigDecimal("11429"));

        createLaunchVehicle("Energia", "Energia", null, getCountryMap().get("RUS"), "NPO Energia",
            59.0, 17.6, 2400000.0, 2, 105000, null, 32000, null,
            "RD-170", 4, "RD-0120", 4, "RP-1 / LOX + LH2", 35600L,
            "Retired", 1987, 1988, 2, 2, false, true, false,
            new BigDecimal("226000000"), new BigDecimal("2152"));

        createLaunchVehicle("N1", "N1", null, getCountryMap().get("RUS"), "OKB-1",
            105.0, 17.0, 2735000.0, 5, 95000, null, 23500, null,
            "NK-15", 30, "NK-15V", 8, "RP-1 / LOX", 45400L,
            "Retired", 1969, 1972, 4, 0, false, true, false,
            null, null);

        createLaunchVehicle("Zenit-2", "Zenit", "2", getCountryMap().get("UKR"), "Yuzhnoye",
            57.0, 3.9, 459000.0, 2, 13740, 4500, null, null,
            "RD-171", 1, "RD-120", 1, "RP-1 / LOX", 7260L,
            "Retired", 1985, 2017, 37, 33, false, false, false,
            new BigDecimal("35000000"), new BigDecimal("2548"));

        // ==================== European ====================
        createLaunchVehicle("Ariane 5 ECA", "Ariane", "5 ECA", getCountryMap().get("ESA"), "ArianeGroup",
            53.0, 5.4, 780000.0, 2, 21000, 10500, null, null,
            "Vulcain 2", 1, "HM7B", 1, "LH2 / LOX + SRBs", 13800L,
            "Retired", 2002, 2023, 117, 112, false, false, false,
            new BigDecimal("165000000"), new BigDecimal("7857"));

        createLaunchVehicle("Ariane 6 A62", "Ariane", "6 A62", getCountryMap().get("ESA"), "ArianeGroup",
            63.0, 5.4, 530000.0, 2, 10350, 5000, null, null,
            "Vulcain 2.1", 1, "Vinci", 1, "LH2 / LOX + SRBs", 8000L,
            "Active", 2024, null, 2, 2, false, false, true,
            new BigDecimal("75000000"), new BigDecimal("7246"));

        createLaunchVehicle("Ariane 6 A64", "Ariane", "6 A64", getCountryMap().get("ESA"), "ArianeGroup",
            63.0, 5.4, 860000.0, 2, 21650, 11500, null, null,
            "Vulcain 2.1", 1, "Vinci", 1, "LH2 / LOX + SRBs", 15400L,
            "Active", 2024, null, 0, 0, false, false, true,
            new BigDecimal("115000000"), new BigDecimal("5312"));

        createLaunchVehicle("Vega", "Vega", null, getCountryMap().get("ESA"), "Avio",
            30.0, 3.0, 137000.0, 4, 1500, null, null, null,
            "P80", 1, "AVUM", 1, "Solid + UDMH/N2O4", 2261L,
            "Retired", 2012, 2024, 24, 22, false, false, false,
            new BigDecimal("37000000"), new BigDecimal("24667"));

        createLaunchVehicle("Vega C", "Vega", "C", getCountryMap().get("ESA"), "Avio",
            35.0, 3.4, 210000.0, 4, 2300, null, null, null,
            "P120C", 1, "AVUM+", 1, "Solid + UDMH/N2O4", 4500L,
            "Active", 2022, null, 3, 2, false, false, true,
            new BigDecimal("45000000"), new BigDecimal("19565"));

        createLaunchVehicle("Ariane 4", "Ariane", "4 44L", getCountryMap().get("ESA"), "ArianeGroup",
            58.7, 3.8, 470000.0, 3, 10200, 4950, null, null,
            "Viking 5C", 4, "HM7B", 1, "N2O4 / UH25 + LH2", 5480L,
            "Retired", 1988, 2003, 116, 113, false, false, false,
            new BigDecimal("85000000"), new BigDecimal("8333"));

        // ==================== Chinese ====================
        createLaunchVehicle("Long March 5", "Long March", "5", getCountryMap().get("CHN"), "CALT",
            57.0, 5.0, 867000.0, 2, 25000, 14000, 8200, null,
            "YF-77", 2, "YF-75D", 2, "LH2 / LOX + RP-1", 10600L,
            "Active", 2016, null, 15, 14, false, false, true,
            new BigDecimal("100000000"), new BigDecimal("4000"));

        createLaunchVehicle("Long March 5B", "Long March", "5B", getCountryMap().get("CHN"), "CALT",
            53.7, 5.0, 837500.0, 1, 25000, null, null, null,
            "YF-77", 2, null, null, "LH2 / LOX + RP-1", 10600L,
            "Active", 2020, null, 5, 5, false, false, true,
            new BigDecimal("90000000"), new BigDecimal("3600"));

        createLaunchVehicle("Long March 7", "Long March", "7", getCountryMap().get("CHN"), "CALT",
            53.1, 3.35, 597000.0, 2, 13500, 5500, null, null,
            "YF-100", 6, "YF-115", 2, "RP-1 / LOX", 7200L,
            "Active", 2016, null, 12, 12, false, false, true,
            new BigDecimal("55000000"), new BigDecimal("4074"));

        createLaunchVehicle("Long March 7A", "Long March", "7A", getCountryMap().get("CHN"), "CALT",
            60.1, 3.35, 573000.0, 3, null, 7000, null, null,
            "YF-100", 6, "YF-75D", 2, "RP-1 / LOX + LH2", 7200L,
            "Active", 2021, null, 5, 4, false, false, true,
            new BigDecimal("70000000"), new BigDecimal("10000"));

        createLaunchVehicle("Long March 2D", "Long March", "2D", getCountryMap().get("CHN"), "SAST",
            41.1, 3.35, 232250.0, 2, 3500, 1300, null, null,
            "YF-21C", 4, "YF-24E", 1, "N2O4 / UDMH", 2962L,
            "Active", 1992, null, 90, 89, false, false, true,
            new BigDecimal("30000000"), new BigDecimal("8571"));

        createLaunchVehicle("Long March 2C", "Long March", "2C", getCountryMap().get("CHN"), "CALT",
            42.0, 3.35, 233000.0, 2, 3850, 1250, null, null,
            "YF-21C", 4, "YF-24E", 1, "N2O4 / UDMH", 2962L,
            "Active", 1982, null, 70, 68, false, false, true,
            new BigDecimal("25000000"), new BigDecimal("6494"));

        createLaunchVehicle("Long March 2F", "Long March", "2F", getCountryMap().get("CHN"), "CALT",
            62.0, 3.35, 480000.0, 2, 8400, null, null, null,
            "YF-20C", 4, "YF-24E", 1, "N2O4 / UDMH + SRBs", 6040L,
            "Active", 1999, null, 20, 20, false, true, true,
            new BigDecimal("70000000"), new BigDecimal("8333"));

        createLaunchVehicle("Long March 3B", "Long March", "3B", getCountryMap().get("CHN"), "CALT",
            54.8, 3.35, 425800.0, 3, 11200, 5100, null, null,
            "YF-21C", 4, "YF-75", 2, "N2O4 / UDMH + LH2", 5923L,
            "Active", 1996, null, 95, 93, false, false, true,
            new BigDecimal("70000000"), new BigDecimal("6250"));

        createLaunchVehicle("Long March 8", "Long March", "8", getCountryMap().get("CHN"), "CALT",
            50.3, 3.35, 356000.0, 2, 8100, 2800, null, null,
            "YF-100", 2, "YF-75D", 1, "RP-1 / LOX + LH2", 2400L,
            "Active", 2020, null, 6, 6, false, false, true,
            new BigDecimal("40000000"), new BigDecimal("4938"));

        createLaunchVehicle("Long March 11", "Long March", "11", getCountryMap().get("CHN"), "CALT",
            20.8, 2.0, 58000.0, 4, 700, 350, null, null,
            "Solid", 1, "Solid", 1, "Solid", 1200L,
            "Active", 2015, null, 20, 19, false, false, true,
            new BigDecimal("5000000"), new BigDecimal("7143"));

        createLaunchVehicle("Kuaizhou 1A", "Kuaizhou", "1A", getCountryMap().get("CHN"), "ExPace",
            20.0, 1.4, 30000.0, 4, 300, null, null, null,
            "Solid", 1, "Solid", 1, "Solid", 590L,
            "Active", 2017, null, 25, 23, false, false, true,
            new BigDecimal("4000000"), new BigDecimal("13333"));

        createLaunchVehicle("Ceres-1", "Ceres", "1", getCountryMap().get("CHN"), "Galactic Energy",
            19.0, 1.4, 33000.0, 4, 400, null, null, null,
            "Solid", 1, "Solid", 1, "Solid", 600L,
            "Active", 2020, null, 15, 14, false, false, true,
            new BigDecimal("4500000"), new BigDecimal("11250"));

        createLaunchVehicle("Zhuque-2", "Zhuque", "2", getCountryMap().get("CHN"), "LandSpace",
            49.5, 3.35, 216000.0, 2, 4000, 1500, null, null,
            "TQ-12", 4, "TQ-11", 1, "CH4 / LOX", 2680L,
            "Active", 2023, null, 5, 3, true, false, true,
            new BigDecimal("45000000"), new BigDecimal("11250"));

        // ==================== Japanese ====================
        createLaunchVehicle("H-IIA", "H-II", "A", getCountryMap().get("JPN"), "MHI",
            53.0, 4.0, 289000.0, 2, 15000, 6000, null, null,
            "LE-7A", 1, "LE-5B", 1, "LH2 / LOX", 2200L,
            "Active", 2001, null, 50, 49, false, false, true,
            new BigDecimal("90000000"), new BigDecimal("6000"));

        createLaunchVehicle("H-IIB", "H-II", "B", getCountryMap().get("JPN"), "MHI",
            56.6, 5.2, 531000.0, 2, 19000, 8000, null, null,
            "LE-7A", 2, "LE-5B-2", 1, "LH2 / LOX + SRBs", 5600L,
            "Retired", 2009, 2020, 9, 9, false, false, false,
            new BigDecimal("110000000"), new BigDecimal("5789"));

        createLaunchVehicle("H3-22", "H3", "22", getCountryMap().get("JPN"), "JAXA/MHI",
            63.0, 5.2, 574000.0, 2, 6500, 4000, null, null,
            "LE-9", 3, "LE-5B-3", 1, "LH2 / LOX + SRBs", 9800L,
            "Active", 2023, null, 4, 3, false, false, true,
            new BigDecimal("50000000"), new BigDecimal("7692"));

        createLaunchVehicle("Epsilon", "Epsilon", null, getCountryMap().get("JPN"), "JAXA/IHI",
            26.0, 2.6, 95600.0, 3, 1200, 450, null, null,
            "SRB-A3", 1, "M-35", 1, "Solid", 2270L,
            "Active", 2013, null, 6, 5, false, false, true,
            new BigDecimal("38000000"), new BigDecimal("31667"));

        // ==================== Indian ====================
        createLaunchVehicle("PSLV-XL", "PSLV", "XL", getCountryMap().get("IND"), "ISRO",
            44.0, 2.8, 320000.0, 4, 3800, 1425, null, null,
            "S139", 1, "L-40", 4, "Solid + UDMH", 2960L,
            "Active", 1993, null, 60, 57, false, false, true,
            new BigDecimal("21000000"), new BigDecimal("5526"));

        createLaunchVehicle("PSLV-CA", "PSLV", "CA", getCountryMap().get("IND"), "ISRO",
            44.0, 2.8, 230000.0, 4, 1100, null, null, null,
            "S139", 1, "L-40", 0, "Solid + UDMH", 2960L,
            "Active", 2007, null, 20, 19, false, false, true,
            new BigDecimal("15000000"), new BigDecimal("13636"));

        createLaunchVehicle("GSLV Mk II", "GSLV", "Mk II", getCountryMap().get("IND"), "ISRO",
            49.1, 2.8, 414750.0, 3, 5000, 2500, null, null,
            "Vikas", 4, "CE-7.5", 1, "UDMH / N2O4 + LH2", 4740L,
            "Active", 2010, null, 10, 8, false, false, true,
            new BigDecimal("36000000"), new BigDecimal("7200"));

        createLaunchVehicle("LVM3", "LVM", "3", getCountryMap().get("IND"), "ISRO",
            43.5, 4.0, 640000.0, 3, 10000, 4000, null, null,
            "Vikas", 2, "CE-20", 1, "UDMH / N2O4 + LH2", 5150L,
            "Active", 2014, null, 10, 9, false, true, true,
            new BigDecimal("50000000"), new BigDecimal("5000"));

        createLaunchVehicle("SSLV", "SSLV", null, getCountryMap().get("IND"), "ISRO",
            34.0, 2.0, 120000.0, 3, 500, 300, null, null,
            "SS1", 1, "Velocity Trimming Module", 1, "Solid", 2490L,
            "Active", 2022, null, 3, 2, false, false, true,
            new BigDecimal("4500000"), new BigDecimal("9000"));

        // ==================== South Korean ====================
        createLaunchVehicle("Nuri", "Nuri", "KSLV-II", getCountryMap().get("KOR"), "KARI",
            47.2, 3.5, 200000.0, 3, 2600, 1500, null, null,
            "KRE-075", 4, "KRE-007", 1, "RP-1 / LOX", 3000L,
            "Active", 2021, null, 4, 3, false, false, true,
            new BigDecimal("50000000"), new BigDecimal("19231"));

        createLaunchVehicle("Naro", "Naro", "KSLV-1", getCountryMap().get("KOR"), "KARI/Khrunichev",
            33.0, 2.9, 140000.0, 2, 100, null, null, null,
            "RD-151", 1, "KSR-1", 1, "RP-1 / LOX + Solid", 1700L,
            "Retired", 2009, 2013, 3, 1, false, false, false,
            new BigDecimal("32000000"), new BigDecimal("320000"));

        // ==================== Israeli ====================
        createLaunchVehicle("Shavit", "Shavit", "2", getCountryMap().get("ISR"), "Israel Aerospace Industries",
            21.0, 1.4, 30000.0, 4, 350, null, null, null,
            "Solid", 1, "Solid", 1, "Solid", 770L,
            "Active", 1988, null, 11, 9, false, false, true,
            new BigDecimal("25000000"), new BigDecimal("71429"));

        // ==================== Iranian ====================
        createLaunchVehicle("Safir", "Safir", null, getCountryMap().get("IRN"), "ISA",
            22.0, 1.25, 26000.0, 2, 50, null, null, null,
            "Nodong", 1, "Solid/Liquid", 1, "UDMH / N2O4", 300L,
            "Active", 2008, null, 9, 5, false, false, true,
            null, null);

        createLaunchVehicle("Simorgh", "Simorgh", null, getCountryMap().get("IRN"), "ISA",
            27.0, 2.4, 87000.0, 2, 350, null, null, null,
            "Nodong", 4, "Safir Upper", 1, "UDMH / N2O4", 1240L,
            "Active", 2016, null, 6, 2, false, false, true,
            null, null);

        // ==================== New Space Startups ====================
        createLaunchVehicle("Spectrum", "Spectrum", null, getCountryMap().get("DEU"), "Isar Aerospace",
            27.0, 2.0, 50000.0, 2, 1000, 700, null, null,
            "Aquila", 9, "Aquila Vacuum", 1, "CH4 / LOX", 1000L,
            "In Development", 2025, null, 0, 0, true, false, false,
            new BigDecimal("12000000"), new BigDecimal("12000"));

        createLaunchVehicle("RFA One", "RFA One", null, getCountryMap().get("DEU"), "Rocket Factory Augsburg",
            30.0, 2.0, 60000.0, 3, 1300, 450, null, null,
            "Helix", 9, "Redshift", 1, "RP-1 / LOX", 1000L,
            "In Development", 2024, null, 1, 0, false, false, false,
            new BigDecimal("10000000"), new BigDecimal("7692"));

        createLaunchVehicle("Prime", "Prime", null, getCountryMap().get("GBR"), "Orbex",
            19.0, 1.3, 18000.0, 2, 180, null, null, null,
            "BE-7 derived", 6, null, 1, "Bio-propane / LOX", 200L,
            "In Development", 2025, null, 0, 0, false, false, false,
            new BigDecimal("6000000"), new BigDecimal("33333"));

        createLaunchVehicle("Miura 5", "Miura", "5", getCountryMap().get("ESP"), "PLD Space",
            34.0, 1.8, 30000.0, 2, 500, 300, null, null,
            "TEPREL-C", 5, "TEPREL-2", 1, "RP-1 / LOX", 800L,
            "In Development", 2025, null, 0, 0, true, false, false,
            new BigDecimal("12000000"), new BigDecimal("24000"));
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
