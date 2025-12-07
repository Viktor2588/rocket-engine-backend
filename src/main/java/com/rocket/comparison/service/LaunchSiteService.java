package com.rocket.comparison.service;

import com.rocket.comparison.entity.LaunchSite;
import com.rocket.comparison.entity.LaunchSiteStatus;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.LaunchSiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaunchSiteService {

    private final LaunchSiteRepository launchSiteRepository;
    private final CountryRepository countryRepository;

    // ==================== Basic CRUD ====================

    public List<LaunchSite> getAllLaunchSites() {
        return launchSiteRepository.findAll();
    }

    public Optional<LaunchSite> getLaunchSiteById(Long id) {
        return launchSiteRepository.findById(id);
    }

    public Optional<LaunchSite> getLaunchSiteByShortName(String shortName) {
        return launchSiteRepository.findByShortName(shortName);
    }

    @Transactional
    public LaunchSite saveLaunchSite(LaunchSite launchSite) {
        return launchSiteRepository.save(launchSite);
    }

    @Transactional
    public void deleteLaunchSite(Long id) {
        launchSiteRepository.deleteById(id);
    }

    @Transactional
    public LaunchSite updateLaunchSite(Long id, LaunchSite details) {
        LaunchSite launchSite = launchSiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Launch site not found: " + id));

        launchSite.setName(details.getName());
        launchSite.setShortName(details.getShortName());
        launchSite.setAlternateName(details.getAlternateName());
        launchSite.setCountry(details.getCountry());
        launchSite.setOperator(details.getOperator());
        launchSite.setLatitude(details.getLatitude());
        launchSite.setLongitude(details.getLongitude());
        launchSite.setRegion(details.getRegion());
        launchSite.setStatus(details.getStatus());
        launchSite.setEstablishedYear(details.getEstablishedYear());
        launchSite.setTotalLaunches(details.getTotalLaunches());
        launchSite.setSuccessfulLaunches(details.getSuccessfulLaunches());
        launchSite.setNumberOfLaunchPads(details.getNumberOfLaunchPads());
        launchSite.setHumanRatedCapable(details.getHumanRatedCapable());
        launchSite.setSupportsLeo(details.getSupportsLeo());
        launchSite.setSupportsGeo(details.getSupportsGeo());
        launchSite.setSupportsPolar(details.getSupportsPolar());
        launchSite.setSupportsInterplanetary(details.getSupportsInterplanetary());
        launchSite.setDescription(details.getDescription());
        launchSite.setImageUrl(details.getImageUrl());
        launchSite.setReferenceUrl(details.getReferenceUrl());

        return launchSiteRepository.save(launchSite);
    }

    // ==================== By Country ====================

    public List<LaunchSite> getLaunchSitesByCountry(Long countryId) {
        return launchSiteRepository.findByCountryIdOrderByNameAsc(countryId);
    }

    public List<LaunchSite> getLaunchSitesByCountryCode(String isoCode) {
        return launchSiteRepository.findByCountryIsoCodeOrderByNameAsc(isoCode.toUpperCase());
    }

    public List<LaunchSite> getLaunchSitesByCountryAndStatus(Long countryId, LaunchSiteStatus status) {
        return launchSiteRepository.findByCountryAndStatus(countryId, status);
    }

    // ==================== By Status ====================

    public List<LaunchSite> getLaunchSitesByStatus(LaunchSiteStatus status) {
        return launchSiteRepository.findByStatusOrderByNameAsc(status);
    }

    public List<LaunchSite> getActiveLaunchSites() {
        return launchSiteRepository.findActiveLaunchSites();
    }

    public Long countActiveLaunchSites() {
        return launchSiteRepository.countActiveLaunchSites();
    }

    public Long countActiveLaunchSitesByCountry(Long countryId) {
        return launchSiteRepository.countActiveLaunchSitesByCountry(countryId);
    }

    // ==================== By Capabilities ====================

    public List<LaunchSite> getHumanRatedSites() {
        return launchSiteRepository.findHumanRatedSites();
    }

    public List<LaunchSite> getInterplanetaryCapableSites() {
        return launchSiteRepository.findInterplanetaryCapableSites();
    }

    public List<LaunchSite> getGeoCapableSites() {
        return launchSiteRepository.findGeoCapableSites();
    }

    public List<LaunchSite> getPolarCapableSites() {
        return launchSiteRepository.findPolarCapableSites();
    }

    public List<LaunchSite> getSitesWithLandingFacilities() {
        return launchSiteRepository.findSitesWithLandingFacilities();
    }

    // ==================== By Location ====================

    public List<LaunchSite> getLaunchSitesByLatitudeRange(Double minLatitude, Double maxLatitude) {
        return launchSiteRepository.findByLatitudeRange(minLatitude, maxLatitude);
    }

    public List<LaunchSite> getLaunchSitesByRegion(String region) {
        return launchSiteRepository.findByRegion(region);
    }

    public List<String> getAllRegions() {
        return launchSiteRepository.findAllRegions();
    }

    // ==================== By Operator ====================

    public List<LaunchSite> getLaunchSitesByOperator(String operator) {
        return launchSiteRepository.findByOperatorOrderByNameAsc(operator);
    }

    public List<String> getAllOperators() {
        return launchSiteRepository.findAllOperators();
    }

    // ==================== Rankings ====================

    public List<LaunchSite> getLaunchSitesByMostLaunches(int limit) {
        return launchSiteRepository.findByMostLaunches().stream()
                .limit(limit)
                .toList();
    }

    public List<LaunchSite> getLaunchSitesByHighestSuccessRate(int limit) {
        return launchSiteRepository.findByHighestSuccessRate().stream()
                .limit(limit)
                .toList();
    }

    // ==================== Timeline ====================

    public List<LaunchSite> getLaunchSitesByEstablishedYear(Integer year) {
        return launchSiteRepository.findByEstablishedYear(year);
    }

    public List<LaunchSite> getLaunchSitesByEstablishedYearRange(Integer startYear, Integer endYear) {
        return launchSiteRepository.findByEstablishedYearRange(startYear, endYear);
    }

    public List<Integer> getAllEstablishedYears() {
        return launchSiteRepository.findAllEstablishedYears();
    }

    // ==================== Search ====================

    public List<LaunchSite> searchLaunchSites(String query) {
        return launchSiteRepository.searchLaunchSites(query);
    }

    // ==================== Statistics ====================

    public Long countLaunchSitesByCountry(Long countryId) {
        return launchSiteRepository.countByCountry(countryId);
    }

    public Long sumLaunchesByCountry(Long countryId) {
        Long sum = launchSiteRepository.sumLaunchesByCountry(countryId);
        return sum != null ? sum : 0L;
    }

    public Map<String, Long> getLaunchSiteCountsByCountry() {
        List<Object[]> results = launchSiteRepository.countSitesByCountry();
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

    // ==================== Country Statistics ====================

    public Map<String, Object> getCountryLaunchSiteStatistics(Long countryId) {
        List<LaunchSite> sites = getLaunchSitesByCountry(countryId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSites", sites.size());
        stats.put("activeSites", sites.stream().filter(s -> s.getStatus().isActive()).count());
        stats.put("humanRatedSites", sites.stream().filter(s -> Boolean.TRUE.equals(s.getHumanRatedCapable())).count());
        stats.put("interplanetarySites", sites.stream().filter(s -> Boolean.TRUE.equals(s.getSupportsInterplanetary())).count());

        // Total launches
        long totalLaunches = sites.stream()
                .filter(s -> s.getTotalLaunches() != null)
                .mapToLong(LaunchSite::getTotalLaunches)
                .sum();
        stats.put("totalLaunches", totalLaunches);

        // Total successful launches
        long successfulLaunches = sites.stream()
                .filter(s -> s.getSuccessfulLaunches() != null)
                .mapToLong(LaunchSite::getSuccessfulLaunches)
                .sum();
        stats.put("successfulLaunches", successfulLaunches);

        // Success rate
        if (totalLaunches > 0) {
            stats.put("successRate", (successfulLaunches * 100.0) / totalLaunches);
        }

        // By status
        Map<String, Long> byStatus = sites.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getStatus().getDisplayName(),
                        Collectors.counting()
                ));
        stats.put("byStatus", byStatus);

        // Sites list
        stats.put("sites", sites);

        return stats;
    }

    // ==================== Global Statistics ====================

    public Map<String, Object> getGlobalStatistics() {
        List<LaunchSite> allSites = getAllLaunchSites();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSites", allSites.size());
        stats.put("activeSites", allSites.stream().filter(s -> s.getStatus().isActive()).count());
        stats.put("humanRatedSites", allSites.stream().filter(s -> Boolean.TRUE.equals(s.getHumanRatedCapable())).count());
        stats.put("interplanetarySites", allSites.stream().filter(s -> Boolean.TRUE.equals(s.getSupportsInterplanetary())).count());

        // Total launches globally
        long totalLaunches = allSites.stream()
                .filter(s -> s.getTotalLaunches() != null)
                .mapToLong(LaunchSite::getTotalLaunches)
                .sum();
        stats.put("totalLaunches", totalLaunches);

        stats.put("byCountry", getLaunchSiteCountsByCountry());
        stats.put("regions", getAllRegions());
        stats.put("operators", getAllOperators());
        stats.put("establishedYears", getAllEstablishedYears());

        // By capabilities
        stats.put("geoCapableSites", getGeoCapableSites().size());
        stats.put("polarCapableSites", getPolarCapableSites().size());
        stats.put("sitesWithLanding", getSitesWithLandingFacilities().size());

        return stats;
    }

    // ==================== Geographic Analysis ====================

    public Map<String, Object> getGeographicDistribution() {
        List<LaunchSite> allSites = getAllLaunchSites();

        Map<String, Object> distribution = new HashMap<>();

        // By latitude bands
        Map<String, Long> byLatitudeBand = new LinkedHashMap<>();
        byLatitudeBand.put("Equatorial (0-15°)", allSites.stream()
                .filter(s -> s.getLatitude() != null && Math.abs(s.getLatitude()) <= 15)
                .count());
        byLatitudeBand.put("Tropical (15-30°)", allSites.stream()
                .filter(s -> s.getLatitude() != null && Math.abs(s.getLatitude()) > 15 && Math.abs(s.getLatitude()) <= 30)
                .count());
        byLatitudeBand.put("Temperate (30-45°)", allSites.stream()
                .filter(s -> s.getLatitude() != null && Math.abs(s.getLatitude()) > 30 && Math.abs(s.getLatitude()) <= 45)
                .count());
        byLatitudeBand.put("High Latitude (45-60°)", allSites.stream()
                .filter(s -> s.getLatitude() != null && Math.abs(s.getLatitude()) > 45 && Math.abs(s.getLatitude()) <= 60)
                .count());
        byLatitudeBand.put("Polar (>60°)", allSites.stream()
                .filter(s -> s.getLatitude() != null && Math.abs(s.getLatitude()) > 60)
                .count());
        distribution.put("byLatitudeBand", byLatitudeBand);

        // By region
        Map<String, Long> byRegion = allSites.stream()
                .filter(s -> s.getRegion() != null)
                .collect(Collectors.groupingBy(
                        LaunchSite::getRegion,
                        Collectors.counting()
                ));
        distribution.put("byRegion", byRegion);

        // Site locations for mapping
        List<Map<String, Object>> locations = allSites.stream()
                .filter(s -> s.getLatitude() != null && s.getLongitude() != null)
                .map(s -> {
                    Map<String, Object> loc = new HashMap<>();
                    loc.put("id", s.getId());
                    loc.put("name", s.getName());
                    loc.put("shortName", s.getShortName());
                    loc.put("latitude", s.getLatitude());
                    loc.put("longitude", s.getLongitude());
                    loc.put("country", s.getCountry().getName());
                    loc.put("status", s.getStatus().getDisplayName());
                    loc.put("totalLaunches", s.getTotalLaunches());
                    return loc;
                })
                .toList();
        distribution.put("locations", locations);

        return distribution;
    }
}
