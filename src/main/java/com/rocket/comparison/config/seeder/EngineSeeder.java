package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.Engine;
import com.rocket.comparison.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Seeds Engine entities with initial data.
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
}
