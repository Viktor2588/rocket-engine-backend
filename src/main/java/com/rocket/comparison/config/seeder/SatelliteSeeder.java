package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.SatelliteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

/**
 * Seeds Satellite entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SatelliteSeeder implements EntitySeeder {

    private final SatelliteRepository satelliteRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding satellites...");
            seedSatellites();
            log.info("Seeded {} satellites", count());
        }
    }

    @Override
    public String getEntityName() {
        return "satellites";
    }

    @Override
    public long count() {
        return satelliteRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedSatellites() {
        createSatellite("International Space Station", getCountryMap().get("USA"), SatelliteType.SPACE_STATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(1998, 11, 20), OrbitType.ISS_ORBIT, 420.0, 419700.0,
            "NASA/Roscosmos/ESA/JAXA/CSA", null, "Largest modular space station.");

        createSatellite("Tiangong", getCountryMap().get("CHN"), SatelliteType.SPACE_STATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 4, 29), OrbitType.LEO, 390.0, 66000.0,
            "CNSA", null, "China's modular space station.");

        createSatellite("Hubble Space Telescope", getCountryMap().get("USA"), SatelliteType.ASTRONOMY,
            SatelliteStatus.OPERATIONAL, LocalDate.of(1990, 4, 24), OrbitType.LEO, 540.0, 11110.0,
            "NASA/ESA", null, "Iconic space telescope.");

        createSatellite("James Webb Space Telescope", getCountryMap().get("USA"), SatelliteType.ASTRONOMY,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 12, 25), OrbitType.L2, 1500000.0, 6500.0,
            "NASA/ESA/CSA", null, "Infrared space observatory.");

        createSatellite("GPS III", getCountryMap().get("USA"), SatelliteType.NAVIGATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2018, 12, 23), OrbitType.NAVIGATION_MEO, 20200.0, 3880.0,
            "US Space Force", "GPS", "Latest GPS satellite generation.");

        createSatellite("Galileo", getCountryMap().get("ESA"), SatelliteType.NAVIGATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2016, 12, 17), OrbitType.NAVIGATION_MEO, 23222.0, 700.0,
            "ESA/EU", "Galileo", "European navigation system.");

        createSatellite("Starlink", getCountryMap().get("USA"), SatelliteType.COMMUNICATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2019, 5, 24), OrbitType.LEO, 550.0, 260.0,
            "SpaceX", "Starlink", "Global internet constellation.");

        createSatellite("Landsat 9", getCountryMap().get("USA"), SatelliteType.EARTH_OBSERVATION,
            SatelliteStatus.OPERATIONAL, LocalDate.of(2021, 9, 27), OrbitType.SSO, 705.0, 2864.0,
            "NASA/USGS", "Landsat", "Earth observation satellite.");
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
}
