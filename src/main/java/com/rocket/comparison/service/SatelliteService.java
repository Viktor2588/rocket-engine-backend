package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.SatelliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SatelliteService {

    private final SatelliteRepository satelliteRepository;
    private final CountryRepository countryRepository;

    // ==================== Basic CRUD ====================

    public List<Satellite> getAllSatellites() {
        return satelliteRepository.findAll();
    }

    public Optional<Satellite> getSatelliteById(Long id) {
        return satelliteRepository.findById(id);
    }

    public Optional<Satellite> getSatelliteByNoradId(String noradId) {
        return satelliteRepository.findByNoradId(noradId);
    }

    public Optional<Satellite> getSatelliteByCosparId(String cosparId) {
        return satelliteRepository.findByCosparId(cosparId);
    }

    @Transactional
    public Satellite saveSatellite(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }

    @Transactional
    public void deleteSatellite(Long id) {
        satelliteRepository.deleteById(id);
    }

    @Transactional
    public Satellite updateSatellite(Long id, Satellite details) {
        Satellite satellite = satelliteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Satellite not found: " + id));

        satellite.setName(details.getName());
        satellite.setAlternateName(details.getAlternateName());
        satellite.setNoradId(details.getNoradId());
        satellite.setCosparId(details.getCosparId());
        satellite.setCountry(details.getCountry());
        satellite.setOperator(details.getOperator());
        satellite.setManufacturer(details.getManufacturer());
        satellite.setSatelliteType(details.getSatelliteType());
        satellite.setStatus(details.getStatus());
        satellite.setOrbitType(details.getOrbitType());
        satellite.setLaunchDate(details.getLaunchDate());
        satellite.setMassKg(details.getMassKg());
        satellite.setPowerWatts(details.getPowerWatts());
        satellite.setAltitudeKm(details.getAltitudeKm());
        satellite.setInclinationDeg(details.getInclinationDeg());
        satellite.setConstellation(details.getConstellation());
        satellite.setPurpose(details.getPurpose());
        satellite.setImageUrl(details.getImageUrl());
        satellite.setReferenceUrl(details.getReferenceUrl());

        return satelliteRepository.save(satellite);
    }

    // ==================== By Country ====================

    public List<Satellite> getSatellitesByCountry(Long countryId) {
        return satelliteRepository.findByCountryIdOrderByLaunchDateDesc(countryId);
    }

    public List<Satellite> getSatellitesByCountryCode(String isoCode) {
        return satelliteRepository.findByCountryIsoCodeOrderByLaunchDateDesc(isoCode.toUpperCase());
    }

    public List<Satellite> getSatellitesByCountryAndStatus(Long countryId, SatelliteStatus status) {
        return satelliteRepository.findByCountryAndStatus(countryId, status);
    }

    // ==================== By Status ====================

    public List<Satellite> getSatellitesByStatus(SatelliteStatus status) {
        return satelliteRepository.findByStatusOrderByLaunchDateDesc(status);
    }

    public List<Satellite> getActiveSatellites() {
        return satelliteRepository.findActiveSatellites();
    }

    public Long countActiveSatellites() {
        return satelliteRepository.countActiveSatellites();
    }

    public Long countActiveSatellitesByCountry(Long countryId) {
        return satelliteRepository.countActiveSatellitesByCountry(countryId);
    }

    // ==================== By Type ====================

    public List<Satellite> getSatellitesByType(SatelliteType type) {
        return satelliteRepository.findBySatelliteTypeOrderByLaunchDateDesc(type);
    }

    public List<Satellite> getSatellitesByTypes(List<SatelliteType> types) {
        return satelliteRepository.findByTypes(types);
    }

    // ==================== By Orbit ====================

    public List<Satellite> getSatellitesByOrbitType(OrbitType orbitType) {
        return satelliteRepository.findByOrbitTypeOrderByLaunchDateDesc(orbitType);
    }

    public List<Satellite> getSatellitesByOrbitTypes(List<OrbitType> orbitTypes) {
        return satelliteRepository.findByOrbitTypes(orbitTypes);
    }

    public List<Satellite> getSatellitesByAltitudeRange(Double minAltitude, Double maxAltitude) {
        return satelliteRepository.findByAltitudeRange(minAltitude, maxAltitude);
    }

    // ==================== By Constellation ====================

    public List<Satellite> getSatellitesByConstellation(String constellation) {
        return satelliteRepository.findByConstellationOrderByLaunchDateDesc(constellation);
    }

    public List<Satellite> getConstellationSatellites() {
        return satelliteRepository.findConstellationSatellites();
    }

    public List<String> getAllConstellations() {
        return satelliteRepository.findAllConstellations();
    }

    public Long countByConstellation(String constellation) {
        return satelliteRepository.countByConstellation(constellation);
    }

    public Long countActiveByConstellation(String constellation) {
        return satelliteRepository.countActiveByConstellation(constellation);
    }

    // ==================== By Operator ====================

    public List<Satellite> getSatellitesByOperator(String operator) {
        return satelliteRepository.findByOperatorOrderByLaunchDateDesc(operator);
    }

    public List<String> getAllOperators() {
        return satelliteRepository.findAllOperators();
    }

    // ==================== Timeline ====================

    public List<Satellite> getSatellitesByLaunchYear(Integer year) {
        return satelliteRepository.findByLaunchYear(year);
    }

    public List<Satellite> getSatellitesByLaunchYearRange(Integer startYear, Integer endYear) {
        return satelliteRepository.findByLaunchYearRange(startYear, endYear);
    }

    public List<Integer> getAllLaunchYears() {
        return satelliteRepository.findAllLaunchYears();
    }

    // ==================== Special Queries ====================

    public List<Satellite> getNavigationSatellites() {
        return satelliteRepository.findNavigationSatellites();
    }

    public List<Satellite> getSpaceStations() {
        return satelliteRepository.findSpaceStations();
    }

    public List<Satellite> getGeostationarySatellites() {
        return satelliteRepository.findGeostationarySatellites();
    }

    // ==================== Search ====================

    public List<Satellite> searchSatellites(String query) {
        return satelliteRepository.searchSatellites(query);
    }

    // ==================== Statistics ====================

    public Long countSatellitesByCountry(Long countryId) {
        return satelliteRepository.countByCountry(countryId);
    }

    public Map<String, Long> getSatelliteCountsByCountry() {
        List<Object[]> results = satelliteRepository.countSatellitesByCountry();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long countryId = (Long) row[0];
            Long count = (Long) row[1];
            countryRepository.findById(countryId).ifPresent(country ->
                    counts.put(country.getName(), count)
            );
        }
        return counts;
    }

    public Map<String, Long> getSatelliteCountsByType() {
        List<Object[]> results = satelliteRepository.countSatellitesByType();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            SatelliteType type = (SatelliteType) row[0];
            Long count = (Long) row[1];
            counts.put(type.getDisplayName(), count);
        }
        return counts;
    }

    public Map<String, Long> getSatelliteCountsByOrbit() {
        List<Object[]> results = satelliteRepository.countSatellitesByOrbit();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            OrbitType orbit = (OrbitType) row[0];
            Long count = (Long) row[1];
            counts.put(orbit.getDisplayName(), count);
        }
        return counts;
    }

    public Map<String, Long> getSatelliteCountsByConstellation() {
        List<Object[]> results = satelliteRepository.countSatellitesByConstellation();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            String constellation = (String) row[0];
            Long count = (Long) row[1];
            counts.put(constellation, count);
        }
        return counts;
    }

    public Map<Integer, Long> getSatelliteCountsByYear() {
        List<Object[]> results = satelliteRepository.countSatellitesByYear();
        Map<Integer, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            Long count = (Long) row[1];
            counts.put(year, count);
        }
        return counts;
    }

    // ==================== Country Statistics ====================

    public Map<String, Object> getCountrySatelliteStatistics(Long countryId) {
        List<Satellite> satellites = getSatellitesByCountry(countryId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSatellites", satellites.size());
        stats.put("activeSatellites", satellites.stream().filter(s -> s.getStatus().isActive()).count());
        stats.put("inSpaceSatellites", satellites.stream().filter(s -> s.getStatus().isInSpace()).count());

        // By type
        Map<String, Long> byType = satellites.stream()
                .filter(s -> s.getSatelliteType() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getSatelliteType().getDisplayName(),
                        Collectors.counting()
                ));
        stats.put("byType", byType);

        // By orbit
        Map<String, Long> byOrbit = satellites.stream()
                .filter(s -> s.getOrbitType() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getOrbitType().getDisplayName(),
                        Collectors.counting()
                ));
        stats.put("byOrbit", byOrbit);

        // By constellation
        Map<String, Long> byConstellation = satellites.stream()
                .filter(s -> s.getConstellation() != null)
                .collect(Collectors.groupingBy(
                        Satellite::getConstellation,
                        Collectors.counting()
                ));
        stats.put("byConstellation", byConstellation);

        // By year
        Map<Integer, Long> byYear = satellites.stream()
                .filter(s -> s.getLaunchYear() != null)
                .collect(Collectors.groupingBy(
                        Satellite::getLaunchYear,
                        TreeMap::new,
                        Collectors.counting()
                ));
        stats.put("byYear", byYear);

        return stats;
    }

    // ==================== Global Statistics ====================

    public Map<String, Object> getGlobalStatistics() {
        List<Satellite> allSatellites = getAllSatellites();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSatellites", allSatellites.size());
        stats.put("activeSatellites", allSatellites.stream().filter(s -> s.getStatus().isActive()).count());
        stats.put("inSpaceSatellites", allSatellites.stream().filter(s -> s.getStatus().isInSpace()).count());

        stats.put("byCountry", getSatelliteCountsByCountry());
        stats.put("byType", getSatelliteCountsByType());
        stats.put("byOrbit", getSatelliteCountsByOrbit());
        stats.put("byConstellation", getSatelliteCountsByConstellation());
        stats.put("byYear", getSatelliteCountsByYear());
        stats.put("constellations", getAllConstellations());
        stats.put("operators", getAllOperators());
        stats.put("launchYears", getAllLaunchYears());

        return stats;
    }

    // ==================== Constellation Analysis ====================

    public Map<String, Object> getConstellationAnalysis(String constellation) {
        List<Satellite> satellites = getSatellitesByConstellation(constellation);

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("constellation", constellation);
        analysis.put("totalSatellites", satellites.size());
        analysis.put("activeSatellites", satellites.stream().filter(s -> s.getStatus().isActive()).count());
        analysis.put("satellites", satellites);

        // Country distribution
        Map<String, Long> byCountry = satellites.stream()
                .filter(s -> s.getCountry() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getCountry().getName(),
                        Collectors.counting()
                ));
        analysis.put("byCountry", byCountry);

        // Orbit distribution
        Map<String, Long> byOrbit = satellites.stream()
                .filter(s -> s.getOrbitType() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getOrbitType().getDisplayName(),
                        Collectors.counting()
                ));
        analysis.put("byOrbit", byOrbit);

        // Launch timeline
        Map<Integer, Long> byYear = satellites.stream()
                .filter(s -> s.getLaunchYear() != null)
                .collect(Collectors.groupingBy(
                        Satellite::getLaunchYear,
                        TreeMap::new,
                        Collectors.counting()
                ));
        analysis.put("launchTimeline", byYear);

        return analysis;
    }
}
