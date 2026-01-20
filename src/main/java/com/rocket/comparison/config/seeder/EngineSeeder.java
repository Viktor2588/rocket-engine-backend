package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Seeds Engine entities with comprehensive rocket engine data.
 * Data sourced from Truth Ledger verified facts and public sources.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EngineSeeder implements EntitySeeder {

    private final EngineRepository engineRepository;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding engines...");
            seedEngines();
            log.info("Seeded {} engines", count());
        }
    }

    @Override
    public String getEntityName() {
        return "engines";
    }

    @Override
    public long count() {
        return engineRepository.count();
    }

    private void seedEngines() {
        // ============================================================================
        // SpaceX Engines
        // ============================================================================
        createEngine("Merlin 1A", "USA", "SpaceX", "Falcon 1", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 255.0, 340000L, null, null, null,
            "First version of Merlin engine, used on Falcon 1");
        createEngine("Merlin 1B", "USA", "SpaceX", "Falcon 1", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 260.0, 380000L, null, null, null,
            "Improved Merlin with regeneratively cooled nozzle");
        createEngine("Merlin 1C", "USA", "SpaceX", "Falcon 9 v1.0", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 304.0, 556000L, 61.0, 630.0, null,
            "First Merlin to fly on Falcon 9");
        createEngine("Merlin 1D", "USA", "SpaceX", "Falcon 9", "Active", "1st stage",
            "RP-1 / LOX", "Gas generator", 311.0, 845000L, 97.0, 470.0, 2.36,
            "Current production Merlin, highly reliable workhorse engine");
        createEngine("Merlin Vacuum", "USA", "SpaceX", "Falcon 9", "Active", "2nd stage",
            "RP-1 / LOX", "Gas generator", 348.0, 981000L, 97.0, 490.0, 2.36,
            "Vacuum-optimized Merlin with extended nozzle");
        createEngine("Raptor 1", "USA", "SpaceX", "Starship", "Retired", "1st stage",
            "CH4 / LOX", "Full-flow staged combustion", 330.0, 1850000L, 300.0, 1500.0, 3.55,
            "First full-flow staged combustion engine to fly");
        createEngine("Raptor 2", "USA", "SpaceX", "Starship", "Active", "1st stage",
            "CH4 / LOX", "Full-flow staged combustion", 350.0, 2300000L, 350.0, 1600.0, 3.55,
            "Simplified design with higher thrust, current production version");
        createEngine("Raptor 3", "USA", "SpaceX", "Starship", "Development", "1st stage",
            "CH4 / LOX", "Full-flow staged combustion", 355.0, 2750000L, 350.0, 1500.0, 3.55,
            "Next-gen Raptor with simplified design and higher performance");
        createEngine("Raptor Vacuum", "USA", "SpaceX", "Starship", "Active", "2nd stage",
            "CH4 / LOX", "Full-flow staged combustion", 380.0, 2500000L, 300.0, 1700.0, 3.55,
            "Vacuum-optimized Raptor with extended nozzle");
        createEngine("Draco", "USA", "SpaceX", "Dragon", "Active", "RCS",
            "NTO / MMH", "Pressure-fed", 300.0, 400L, null, 4.5, null,
            "Reaction control thrusters for Dragon spacecraft");
        createEngine("SuperDraco", "USA", "SpaceX", "Dragon 2", "Active", "Launch escape",
            "NTO / MMH", "Pressure-fed", 235.0, 71000L, null, 65.0, null,
            "Launch escape and propulsive landing engine");
        createEngine("Kestrel", "USA", "SpaceX", "Falcon 1", "Retired", "2nd stage",
            "RP-1 / LOX", "Pressure-fed", 317.0, 31000L, null, 52.0, null,
            "Upper stage engine for Falcon 1");

        // ============================================================================
        // Blue Origin Engines
        // ============================================================================
        createEngine("BE-3", "USA", "Blue Origin", "New Shepard", "Active", "1st stage",
            "LH2 / LOX", "Tap-off", 310.0, 490000L, 145.0, null, null,
            "Hydrogen-fueled engine for suborbital New Shepard");
        createEngine("BE-4", "USA", "Blue Origin", "New Glenn", "Active", "1st stage",
            "CH4 / LOX", "Oxidizer-rich staged combustion", 340.0, 2400000L, 134.0, 2300.0, null,
            "Most powerful methane engine, also powers Vulcan");
        createEngine("BE-7", "USA", "Blue Origin", "Blue Moon", "Development", "Descent",
            "LH2 / LOX", "Dual expander cycle", 453.0, 44000L, null, null, null,
            "High-efficiency lunar lander engine");

        // ============================================================================
        // Aerojet Rocketdyne Engines
        // ============================================================================
        createEngine("RS-25", "USA", "Aerojet Rocketdyne", "SLS", "Active", "1st stage",
            "LH2 / LOX", "Staged combustion", 452.0, 2280000L, 206.0, 3177.0, 6.04,
            "Space Shuttle Main Engine, most tested large engine");
        createEngine("RS-27", "USA", "Rocketdyne", "Delta II", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 295.0, 1023000L, 48.0, 1027.0, null,
            "Workhorse engine derived from Thor/Jupiter");
        createEngine("RS-27A", "USA", "Rocketdyne", "Delta II", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 302.0, 1054000L, 48.0, 1054.0, null,
            "Improved RS-27 with higher expansion nozzle");
        createEngine("RS-68", "USA", "Aerojet Rocketdyne", "Delta IV", "Active", "1st stage",
            "LH2 / LOX", "Gas generator", 420.0, 3137000L, 97.0, 6597.0, null,
            "Largest hydrogen-fueled engine, designed for low cost");
        createEngine("RS-68A", "USA", "Aerojet Rocketdyne", "Delta IV Heavy", "Active", "1st stage",
            "LH2 / LOX", "Gas generator", 412.0, 3560000L, 102.0, 6696.0, null,
            "Upgraded RS-68 with higher thrust");
        createEngine("RL-10", "USA", "Aerojet Rocketdyne", "Centaur", "Active", "Upper stage",
            "LH2 / LOX", "Expander cycle", 465.0, 110000L, 39.0, 167.0, null,
            "Longest-serving US upper stage engine");
        createEngine("RL-10A-4-2", "USA", "Aerojet Rocketdyne", "Atlas V Centaur", "Active", "Upper stage",
            "LH2 / LOX", "Expander cycle", 451.0, 99000L, 39.0, 167.0, null,
            "Current production RL-10 for Atlas V");
        createEngine("RL-10B-2", "USA", "Aerojet Rocketdyne", "Delta IV", "Active", "Upper stage",
            "LH2 / LOX", "Expander cycle", 465.0, 110000L, 39.0, 277.0, null,
            "Extended nozzle variant for Delta IV");
        createEngine("RL-10C-1", "USA", "Aerojet Rocketdyne", "Vulcan Centaur", "Active", "Upper stage",
            "LH2 / LOX", "Expander cycle", 453.0, 106000L, 44.0, 190.0, null,
            "Common upper stage engine for multiple vehicles");
        createEngine("AJ10", "USA", "Aerojet", "Delta II", "Active", "Upper stage",
            "NTO / Aerozine-50", "Pressure-fed", 319.0, 43700L, 8.7, 100.0, null,
            "Pressure-fed hypergolic upper stage engine");
        createEngine("AJ-26", "USA", "Aerojet Rocketdyne", "Antares", "Retired", "1st stage",
            "RP-1 / LOX", "Staged combustion", 311.0, 1800000L, 150.0, 1290.0, null,
            "Americanized NK-33 for Antares rocket");

        // ============================================================================
        // Russian Engines - RD Series
        // ============================================================================
        createEngine("RD-107", "RUS", "NPO Energomash", "Soyuz", "Active", "1st stage",
            "RP-1 / LOX", "Gas generator", 313.0, 1000000L, 60.0, 1190.0, null,
            "One of the most flown rocket engines in history");
        createEngine("RD-108", "RUS", "NPO Energomash", "Soyuz", "Active", "Core stage",
            "RP-1 / LOX", "Gas generator", 315.0, 922000L, 60.0, 1250.0, null,
            "Core stage variant of RD-107");
        createEngine("RD-120", "RUS", "NPO Energomash", "Zenit", "Active", "2nd stage",
            "RP-1 / LOX", "Staged combustion", 350.0, 833400L, 163.0, 1125.0, null,
            "Second stage engine for Zenit rocket");
        createEngine("RD-170", "RUS", "NPO Energomash", "Energia", "Retired", "Booster",
            "RP-1 / LOX", "Staged combustion", 337.0, 7904000L, 245.0, 9750.0, null,
            "Most powerful liquid rocket engine ever built, 4 chambers");
        createEngine("RD-171", "RUS", "NPO Energomash", "Zenit", "Active", "1st stage",
            "RP-1 / LOX", "Staged combustion", 337.0, 7904000L, 245.0, 9750.0, null,
            "Single-nozzle version of RD-170 family");
        createEngine("RD-180", "RUS", "NPO Energomash", "Atlas V", "Active", "1st stage",
            "RP-1 / LOX", "Staged combustion", 338.0, 4150000L, 267.0, 5480.0, 2.72,
            "Two-chamber derivative of RD-170, powers Atlas V");
        createEngine("RD-181", "RUS", "NPO Energomash", "Antares", "Active", "1st stage",
            "RP-1 / LOX", "Staged combustion", 339.0, 1920000L, 258.0, 2200.0, null,
            "Single-chamber RD-180 derivative for Antares");
        createEngine("RD-191", "RUS", "NPO Energomash", "Angara", "Active", "1st stage",
            "RP-1 / LOX", "Staged combustion", 337.0, 2090000L, 263.0, 2290.0, 2.63,
            "Single-chamber RD-170 derivative for Angara");
        createEngine("RD-253", "RUS", "NPO Energomash", "Proton", "Active", "1st stage",
            "UDMH / N2O4", "Staged combustion", 316.0, 1745000L, 147.0, 1280.0, null,
            "Hypergolic engine for Proton rocket");
        createEngine("RD-275", "RUS", "NPO Energomash", "Proton-M", "Active", "1st stage",
            "UDMH / N2O4", "Staged combustion", 316.0, 1830000L, 166.0, 1320.0, null,
            "Upgraded RD-253 for Proton-M");
        createEngine("RD-0110", "RUS", "CADB", "Soyuz", "Active", "Upper stage",
            "RP-1 / LOX", "Gas generator", 330.0, 298000L, 68.0, 408.0, null,
            "Four-chamber upper stage engine for Soyuz");
        createEngine("RD-0120", "RUS", "CADB", "Energia", "Retired", "Core stage",
            "LH2 / LOX", "Staged combustion", 455.0, 1962000L, 218.0, 3450.0, null,
            "Soviet answer to SSME, flew on Energia");
        createEngine("RD-0124", "RUS", "CADB", "Soyuz-2.1b", "Active", "Upper stage",
            "RP-1 / LOX", "Staged combustion", 359.0, 294000L, 157.0, 480.0, null,
            "High-performance upper stage for Soyuz-2");
        createEngine("NK-33", "RUS", "SNTK Kuznetsov", "N-1", "Retired", "1st stage",
            "RP-1 / LOX", "Staged combustion", 331.0, 1680000L, 145.0, 1240.0, 2.72,
            "Legendary N-1 moon rocket engine, high efficiency");

        // ============================================================================
        // European Engines
        // ============================================================================
        createEngine("Vulcain", "ESA", "Snecma", "Ariane 5", "Retired", "Core stage",
            "LH2 / LOX", "Gas generator", 431.0, 1140000L, 112.0, 1800.0, null,
            "First version of Ariane 5 core engine");
        createEngine("Vulcain 2", "ESA", "Safran", "Ariane 5", "Active", "Core stage",
            "LH2 / LOX", "Gas generator", 434.0, 1390000L, 115.0, 2100.0, 6.1,
            "Upgraded Vulcain for Ariane 5 ECA");
        createEngine("Vulcain 2.1", "ESA", "Safran", "Ariane 6", "Active", "Core stage",
            "LH2 / LOX", "Gas generator", 434.0, 1370000L, 116.0, 2100.0, null,
            "Ariane 6 main engine variant");
        createEngine("Vinci", "ESA", "Safran", "Ariane 6", "Active", "Upper stage",
            "LH2 / LOX", "Expander cycle", 457.0, 180000L, 61.0, 538.0, null,
            "Restartable upper stage engine with expander cycle");
        createEngine("HM7B", "ESA", "Safran", "Ariane 5 ECA", "Active", "Upper stage",
            "LH2 / LOX", "Gas generator", 446.0, 64800L, 35.0, 165.0, null,
            "Upper stage engine for Ariane 5 ECA");
        createEngine("Viking 5C", "ESA", "SEP", "Ariane 4", "Retired", "1st stage",
            "UDMH / N2O4", "Gas generator", 278.0, 678000L, 58.0, 826.0, null,
            "Ariane 4 first stage engine");
        createEngine("Prometheus", "ESA", "ArianeGroup", "Ariane Next", "Development", "1st stage",
            "CH4 / LOX", "Gas generator", 360.0, 1000000L, 100.0, 1300.0, null,
            "Low-cost reusable methane engine demonstrator");
        createEngine("P120C", "ESA", "Avio", "Ariane 6", "Active", "Booster",
            "Solid", "Solid", 278.0, 4500000L, null, 142000.0, null,
            "Solid rocket booster for Ariane 6 and Vega-C");

        // ============================================================================
        // Japanese Engines
        // ============================================================================
        createEngine("LE-5", "JPN", "MHI", "H-I", "Retired", "Upper stage",
            "LH2 / LOX", "Expander bleed cycle", 450.0, 102900L, 36.0, 255.0, null,
            "Japan's first cryogenic engine");
        createEngine("LE-5B", "JPN", "MHI", "H-IIA", "Active", "Upper stage",
            "LH2 / LOX", "Expander bleed cycle", 447.0, 137200L, 37.0, 269.0, null,
            "Improved LE-5 for H-IIA/B");
        createEngine("LE-7", "JPN", "MHI", "H-II", "Retired", "1st stage",
            "LH2 / LOX", "Staged combustion", 446.0, 1078000L, 127.0, 1714.0, null,
            "Japan's first staged combustion engine");
        createEngine("LE-7A", "JPN", "MHI", "H-IIA", "Active", "1st stage",
            "LH2 / LOX", "Staged combustion", 440.0, 1098000L, 121.0, 1832.0, null,
            "Simplified LE-7 for improved reliability");
        createEngine("LE-9", "JPN", "MHI", "H3", "Active", "1st stage",
            "LH2 / LOX", "Expander bleed cycle", 425.0, 1471000L, 100.0, 2400.0, null,
            "Innovative expander bleed engine for H3 rocket");

        // ============================================================================
        // Chinese Engines
        // ============================================================================
        createEngine("YF-20", "CHN", "AALPT", "Long March 2/3/4", "Active", "1st stage",
            "UDMH / N2O4", "Gas generator", 289.0, 730000L, 74.0, 700.0, null,
            "Workhorse engine for Long March 2/3/4 family");
        createEngine("YF-21", "CHN", "AALPT", "Long March 3", "Active", "2nd stage",
            "UDMH / N2O4", "Gas generator", 291.0, 742000L, 78.0, 550.0, null,
            "Second stage engine for various Long March");
        createEngine("YF-22", "CHN", "AALPT", "Long March 2E", "Active", "2nd stage",
            "UDMH / N2O4", "Gas generator", 303.0, 742000L, null, null, null,
            "Vacuum optimized version of YF-21");
        createEngine("YF-73", "CHN", "AALPT", "Long March 3", "Active", "3rd stage",
            "LH2 / LOX", "Gas generator", 438.0, 44000L, 25.0, 260.0, null,
            "China's first cryogenic engine");
        createEngine("YF-75", "CHN", "AALPT", "Long March 3A", "Active", "3rd stage",
            "LH2 / LOX", "Gas generator", 442.0, 78500L, 37.0, 550.0, null,
            "Improved cryogenic upper stage engine");
        createEngine("YF-75D", "CHN", "AALPT", "Long March 5", "Active", "2nd stage",
            "LH2 / LOX", "Expander cycle", 453.0, 88000L, 40.0, 660.0, null,
            "Advanced expander cycle upper stage engine");
        createEngine("YF-77", "CHN", "AALPT", "Long March 5", "Active", "Core stage",
            "LH2 / LOX", "Gas generator", 430.0, 510000L, 102.0, 3300.0, null,
            "Large hydrogen engine for Long March 5");
        createEngine("YF-100", "CHN", "AALPT", "Long March 5/6/7", "Active", "1st stage",
            "RP-1 / LOX", "Staged combustion", 335.0, 1200000L, 180.0, 1850.0, null,
            "China's first staged combustion kerolox engine");
        createEngine("YF-115", "CHN", "AALPT", "Long March 6", "Active", "2nd stage",
            "RP-1 / LOX", "Gas generator", 342.0, 180000L, 120.0, 470.0, null,
            "Upper stage kerolox engine");

        // ============================================================================
        // Indian Engines
        // ============================================================================
        createEngine("Vikas", "IND", "ISRO", "GSLV", "Active", "2nd stage",
            "UDMH / N2O4", "Gas generator", 293.0, 803000L, 58.0, 900.0, null,
            "Licensed Viking derivative, GSLV workhorse");
        createEngine("CE-7.5", "IND", "ISRO", "GSLV Mk II", "Active", "Upper stage",
            "LH2 / LOX", "Staged combustion", 454.0, 73500L, 58.0, 700.0, null,
            "India's indigenous cryogenic upper stage");
        createEngine("CE-20", "IND", "ISRO", "LVM3", "Active", "Upper stage",
            "LH2 / LOX", "Gas generator", 443.0, 200000L, 60.0, 600.0, null,
            "High-thrust cryogenic engine for LVM3");

        // ============================================================================
        // Rocket Lab Engines
        // ============================================================================
        createEngine("Rutherford", "NZL", "Rocket Lab", "Electron", "Active", "1st stage",
            "RP-1 / LOX", "Electric pump-fed", 311.0, 25000L, null, 35.0, null,
            "First electric pump-fed engine to reach orbit");
        createEngine("Rutherford Vacuum", "NZL", "Rocket Lab", "Electron", "Active", "2nd stage",
            "RP-1 / LOX", "Electric pump-fed", 343.0, 26000L, null, 35.0, null,
            "Vacuum-optimized Rutherford");
        createEngine("Curie", "NZL", "Rocket Lab", "Electron", "Active", "Kick stage",
            "Monopropellant", "Pressure-fed", 245.0, 120L, null, 3.5, null,
            "Small kick stage engine for precise orbit insertion");
        createEngine("Archimedes", "NZL", "Rocket Lab", "Neutron", "Development", "1st stage",
            "CH4 / LOX", "Oxidizer-rich staged combustion", 320.0, 980000L, null, null, null,
            "Reusable methane engine for Neutron rocket");

        // ============================================================================
        // Relativity Space Engines
        // ============================================================================
        createEngine("Aeon 1", "USA", "Relativity Space", "Terran 1", "Active", "1st stage",
            "CH4 / LOX", "Gas generator", 310.0, 100000L, 65.0, 100.0, null,
            "3D printed engine for Terran 1");
        createEngine("Aeon R", "USA", "Relativity Space", "Terran R", "Development", "1st stage",
            "CH4 / LOX", "Gas generator", 325.0, 302000L, null, null, null,
            "Larger 3D printed engine for reusable Terran R");
        createEngine("Aeon Vacuum", "USA", "Relativity Space", "Terran 1", "Active", "2nd stage",
            "CH4 / LOX", "Gas generator", 360.0, 100000L, null, null, null,
            "Vacuum-optimized Aeon engine");

        // ============================================================================
        // Firefly Aerospace Engines
        // ============================================================================
        createEngine("Reaver", "USA", "Firefly Aerospace", "Alpha", "Active", "1st stage",
            "RP-1 / LOX", "Tap-off", 295.0, 200000L, null, 175.0, null,
            "First stage engine for Firefly Alpha");
        createEngine("Lightning", "USA", "Firefly Aerospace", "Alpha", "Active", "2nd stage",
            "RP-1 / LOX", "Tap-off", 322.0, 70000L, null, 55.0, null,
            "Vacuum-optimized upper stage engine");

        // ============================================================================
        // Historic US Engines
        // ============================================================================
        createEngine("F-1", "USA", "Rocketdyne", "Saturn V", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 304.0, 6770000L, 70.0, 8400.0, 0.82,
            "Largest single-chamber liquid engine ever flown, Moon rocket");
        createEngine("J-2", "USA", "Rocketdyne", "Saturn V", "Retired", "Upper stage",
            "LH2 / LOX", "Gas generator", 421.0, 1033000L, 53.0, 1788.0, null,
            "Saturn V upper stage engine, restartable");
        createEngine("J-2X", "USA", "Aerojet Rocketdyne", "SLS", "Cancelled", "Upper stage",
            "LH2 / LOX", "Gas generator", 448.0, 1310000L, 92.0, 2472.0, null,
            "Upgraded J-2 for SLS upper stage (cancelled)");
        createEngine("LR-87", "USA", "Aerojet", "Titan", "Retired", "1st stage",
            "NTO / Aerozine-50", "Gas generator", 302.0, 2340000L, 54.0, 2181.0, null,
            "Twin-chamber engine for Titan ICBM and launcher");

        // ============================================================================
        // Other US Commercial Engines
        // ============================================================================
        createEngine("GEM 63", "USA", "Northrop Grumman", "Atlas V", "Active", "Booster",
            "Solid", "Solid", 275.0, 1670000L, null, 43100.0, null,
            "Solid rocket booster for Atlas V and Vulcan");
        createEngine("GEM 63XL", "USA", "Northrop Grumman", "Vulcan", "Active", "Booster",
            "Solid", "Solid", 275.0, 2090000L, null, 49000.0, null,
            "Extended length solid booster for Vulcan");
        createEngine("Hadley", "USA", "Astra", "Rocket 3", "Development", "1st stage",
            "RP-1 / LOX", "Electric pump-fed", 316.0, 14000L, null, 27.0, null,
            "Small electric pump-fed engine for Astra Rocket 3");
        createEngine("Newton", "USA", "Virgin Orbit", "LauncherOne", "Retired", "1st stage",
            "RP-1 / LOX", "Gas generator", 295.0, 327000L, null, null, null,
            "First stage engine for air-launched LauncherOne");

        log.info("Seeded comprehensive engine database with verified data");
    }

    private void createEngine(String name, String origin, String designer, String vehicle,
            String status, String use, String propellant, String powerCycle,
            Double isp, Long thrustN, Double chamberPressure, Double massKg, Double ofRatio,
            String description) {

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
        engine.setDescription(description);
        engineRepository.save(engine);
    }

    // Legacy method for backwards compatibility
    private void createEngine(String name, String origin, String designer, String vehicle,
            String status, String use, String propellant, String powerCycle,
            Double isp, Long thrustN, Double chamberPressure, Double massKg, Double ofRatio) {
        createEngine(name, origin, designer, vehicle, status, use, propellant, powerCycle,
            isp, thrustN, chamberPressure, massKg, ofRatio, null);
    }
}
