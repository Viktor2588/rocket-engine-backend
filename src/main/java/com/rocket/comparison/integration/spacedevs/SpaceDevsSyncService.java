package com.rocket.comparison.integration.spacedevs;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.integration.spacedevs.dto.*;
import com.rocket.comparison.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service for synchronizing data from TheSpaceDevs API to our database.
 * Handles mapping, deduplication, and updates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpaceDevsSyncService {

    private final SpaceDevsApiClient apiClient;
    private final CountryRepository countryRepository;
    private final SpaceMissionRepository spaceMissionRepository;
    private final LaunchSiteRepository launchSiteRepository;

    // Country code to Country entity cache (thread-safe)
    private final Map<String, Country> countryCache = new ConcurrentHashMap<>();

    /**
     * Full sync - fetches and updates all data types
     */
    @Transactional
    public Map<String, Object> fullSync() {
        log.info("Starting full sync from TheSpaceDevs API");
        Map<String, Object> results = new LinkedHashMap<>();

        // Load country cache
        loadCountryCache();

        // Sync launch sites first (from pads)
        results.put("launchSites", syncLaunchSites(100));

        // Sync recent launches (missions)
        results.put("missions", syncRecentLaunches(200));

        results.put("syncedAt", LocalDate.now().toString());
        log.info("Full sync completed: {}", results);

        return results;
    }

    /**
     * Sync only recent launches (missions)
     */
    @Transactional
    public Map<String, Object> syncRecentLaunches(int limit) {
        log.info("Syncing recent {} launches", limit);
        loadCountryCache();

        List<LaunchDto> launches = apiClient.fetchLaunches(limit);
        int created = 0;
        int updated = 0;
        int skipped = 0;

        for (LaunchDto launch : launches) {
            try {
                SyncResult result = syncLaunch(launch);
                switch (result) {
                    case CREATED -> created++;
                    case UPDATED -> updated++;
                    case SKIPPED -> skipped++;
                }
            } catch (Exception e) {
                log.warn("Error syncing launch {}: {}", launch.getName(), e.getMessage());
                skipped++;
            }
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("fetched", launches.size());
        stats.put("created", created);
        stats.put("updated", updated);
        stats.put("skipped", skipped);

        log.info("Launch sync completed: {}", stats);
        return stats;
    }

    /**
     * Sync launch sites from pads endpoint
     */
    @Transactional
    public Map<String, Object> syncLaunchSites(int limit) {
        log.info("Syncing launch sites (pads)");
        loadCountryCache();

        List<PadDto> pads = apiClient.fetchPads(limit);
        int created = 0;
        int updated = 0;
        int skipped = 0;

        // Group pads by location to create launch sites
        Map<String, List<PadDto>> padsByLocation = pads.stream()
            .filter(p -> p.getLocation() != null)
            .collect(Collectors.groupingBy(p -> p.getLocation().getName()));

        for (Map.Entry<String, List<PadDto>> entry : padsByLocation.entrySet()) {
            try {
                SyncResult result = syncLaunchSite(entry.getKey(), entry.getValue());
                switch (result) {
                    case CREATED -> created++;
                    case UPDATED -> updated++;
                    case SKIPPED -> skipped++;
                }
            } catch (Exception e) {
                log.warn("Error syncing launch site {}: {}", entry.getKey(), e.getMessage());
                skipped++;
            }
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("fetched", padsByLocation.size());
        stats.put("created", created);
        stats.put("updated", updated);
        stats.put("skipped", skipped);

        log.info("Launch site sync completed: {}", stats);
        return stats;
    }

    /**
     * Sync upcoming launches
     */
    @Transactional
    public Map<String, Object> syncUpcomingLaunches(int limit) {
        log.info("Syncing upcoming {} launches", limit);
        loadCountryCache();

        List<LaunchDto> launches = apiClient.fetchUpcomingLaunches(limit);
        int created = 0;
        int updated = 0;
        int skipped = 0;

        for (LaunchDto launch : launches) {
            try {
                SyncResult result = syncLaunch(launch);
                switch (result) {
                    case CREATED -> created++;
                    case UPDATED -> updated++;
                    case SKIPPED -> skipped++;
                }
            } catch (Exception e) {
                log.warn("Error syncing upcoming launch {}: {}", launch.getName(), e.getMessage());
                skipped++;
            }
        }

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("fetched", launches.size());
        stats.put("created", created);
        stats.put("updated", updated);
        stats.put("skipped", skipped);

        log.info("Upcoming launch sync completed: {}", stats);
        return stats;
    }

    // ==================== Private Sync Methods ====================

    private SyncResult syncLaunch(LaunchDto launch) {
        if (launch.getName() == null || launch.getLaunchServiceProvider() == null) {
            return SyncResult.SKIPPED;
        }

        // Try to find existing mission by name
        Optional<SpaceMission> existingOpt = spaceMissionRepository.findAll().stream()
            .filter(m -> m.getName().equalsIgnoreCase(launch.getName()))
            .findFirst();

        // Try multiple ways to resolve country
        Country country = resolveCountry(launch.getLaunchServiceProvider().getCountryCode());

        // If country_code is null, try inferring from agency name
        if (country == null) {
            country = inferCountryFromAgencyName(launch.getLaunchServiceProvider().getName());
        }

        // If still null, try from pad location
        if (country == null && launch.getPad() != null) {
            country = resolveCountry(launch.getPad().getCountryCode());
            if (country == null && launch.getPad().getLocation() != null) {
                country = resolveCountry(launch.getPad().getLocation().getCountryCode());
            }
        }

        if (country == null) {
            log.debug("Skipping launch {} - could not resolve country from agency: {}",
                launch.getName(), launch.getLaunchServiceProvider().getName());
            return SyncResult.SKIPPED;
        }

        SpaceMission mission;
        boolean isNew;

        if (existingOpt.isPresent()) {
            mission = existingOpt.get();
            isNew = false;
        } else {
            mission = new SpaceMission();
            mission.setName(launch.getName());
            isNew = true;
        }

        // Map launch data to mission
        mapLaunchToMission(launch, mission, country);

        spaceMissionRepository.save(mission);

        return isNew ? SyncResult.CREATED : SyncResult.UPDATED;
    }

    private void mapLaunchToMission(LaunchDto launch, SpaceMission mission, Country country) {
        mission.setCountry(country);

        // Parse launch date
        if (launch.getNet() != null) {
            try {
                ZonedDateTime zdt = ZonedDateTime.parse(launch.getNet());
                mission.setLaunchDate(zdt.toLocalDate());
            } catch (DateTimeParseException e) {
                log.debug("Could not parse date: {}", launch.getNet());
            }
        }

        // Set operator
        if (launch.getLaunchServiceProvider() != null) {
            mission.setOperator(launch.getLaunchServiceProvider().getName());
        }

        // Set launch vehicle
        if (launch.getRocket() != null && launch.getRocket().getConfiguration() != null) {
            mission.setLaunchVehicleName(launch.getRocket().getConfiguration().getFullName());
        }

        // Set mission status
        mission.setStatus(mapLaunchStatus(launch.getStatus()));

        // Set mission type
        mission.setMissionType(mapMissionType(launch.getMission()));

        // Set destination based on mission type/orbit
        mission.setDestination(mapDestination(launch.getMission()));

        // Set description
        if (launch.getMission() != null && launch.getMission().getDescription() != null) {
            mission.setDescription(launch.getMission().getDescription());
        }

        // Set launch site
        if (launch.getPad() != null) {
            if (launch.getPad().getLocation() != null) {
                mission.setLaunchSite(launch.getPad().getLocation().getName());
                mission.setLaunchSiteCountry(launch.getPad().getCountryCode());
            } else {
                mission.setLaunchSite(launch.getPad().getName());
            }
        }

        // Set image
        if (launch.getImage() != null) {
            mission.setImageUrl(launch.getImage());
        }

        // Set reference URL
        mission.setReferenceUrl(launch.getUrl());
    }

    private MissionStatus mapLaunchStatus(Object statusObj) {
        if (statusObj == null) return MissionStatus.PLANNED;

        // The status comes as a nested object with name field
        String statusName = "";
        if (statusObj instanceof Map) {
            statusName = String.valueOf(((Map<?, ?>) statusObj).get("abbrev"));
        } else if (statusObj instanceof LaunchDto.LaunchStatusDto statusDto) {
            String abbrev = statusDto.getAbbrev() != null ? statusDto.getAbbrev() : "";
            return switch (abbrev.toLowerCase()) {
                case "success" -> MissionStatus.COMPLETED;
                case "failure" -> MissionStatus.FAILED;
                case "partial failure" -> MissionStatus.PARTIAL_SUCCESS;
                case "in flight" -> MissionStatus.LAUNCHED;
                default -> MissionStatus.PLANNED;
            };
        }

        return switch (statusName.toLowerCase()) {
            case "success" -> MissionStatus.COMPLETED;
            case "failure" -> MissionStatus.FAILED;
            case "partial failure" -> MissionStatus.PARTIAL_SUCCESS;
            case "in flight" -> MissionStatus.LAUNCHED;
            case "tbc", "tbd", "go" -> MissionStatus.PLANNED;
            default -> MissionStatus.PLANNED;
        };
    }

    private MissionType mapMissionType(Object missionObj) {
        if (missionObj == null) return MissionType.SATELLITE_DEPLOYMENT;

        String type = "";
        if (missionObj instanceof Map) {
            type = String.valueOf(((Map<?, ?>) missionObj).get("type"));
        }

        String typeLower = type.toLowerCase();

        if (typeLower.contains("crewed") || typeLower.contains("human")) {
            return MissionType.CREWED_ORBITAL;
        } else if (typeLower.contains("cargo") || typeLower.contains("resupply")) {
            return MissionType.CARGO_RESUPPLY;
        } else if (typeLower.contains("test") || typeLower.contains("demo")) {
            return MissionType.TECHNOLOGY_DEMO;
        } else if (typeLower.contains("lunar") || typeLower.contains("moon")) {
            return MissionType.LUNAR_ORBITER;
        } else if (typeLower.contains("mars")) {
            return MissionType.MARS_ORBITER;
        } else if (typeLower.contains("earth observation")) {
            return MissionType.EARTH_OBSERVATION;
        } else if (typeLower.contains("communication")) {
            return MissionType.COMMUNICATIONS;
        } else if (typeLower.contains("navigation")) {
            return MissionType.NAVIGATION;
        } else if (typeLower.contains("weather")) {
            return MissionType.WEATHER_SATELLITE;
        } else if (typeLower.contains("science") || typeLower.contains("research")) {
            return MissionType.ASTROPHYSICS;
        } else if (typeLower.contains("military") || typeLower.contains("reconnaissance")) {
            return MissionType.MILITARY_RECONNAISSANCE;
        } else if (typeLower.contains("suborbital")) {
            return MissionType.SUBORBITAL;
        }

        return MissionType.SATELLITE_DEPLOYMENT;
    }

    private Destination mapDestination(Object missionObj) {
        if (missionObj == null) return Destination.LEO;

        String orbit = "";
        if (missionObj instanceof Map) {
            Object orbitObj = ((Map<?, ?>) missionObj).get("orbit");
            if (orbitObj instanceof Map) {
                orbit = String.valueOf(((Map<?, ?>) orbitObj).get("abbrev"));
            }
        }

        return switch (orbit.toUpperCase()) {
            case "LEO" -> Destination.LEO;
            case "GEO" -> Destination.GEO;
            case "GTO" -> Destination.GTO;
            case "MEO" -> Destination.MEO;
            case "SSO" -> Destination.SSO;
            case "HEO" -> Destination.HEO;
            case "TLI", "LLO" -> Destination.LUNAR_ORBIT;
            case "TMI" -> Destination.MARS_ORBIT;
            case "POLAR" -> Destination.POLAR;
            case "L1", "L2" -> Destination.SUN_EARTH_L1;
            default -> Destination.LEO;
        };
    }

    private SyncResult syncLaunchSite(String locationName, List<PadDto> pads) {
        if (locationName == null || pads.isEmpty()) {
            return SyncResult.SKIPPED;
        }

        PadDto firstPad = pads.get(0);
        if (firstPad.getLocation() == null) {
            return SyncResult.SKIPPED;
        }

        // Try to find existing launch site by name
        Optional<LaunchSite> existingOpt = launchSiteRepository.findAll().stream()
            .filter(ls -> ls.getName().equalsIgnoreCase(locationName))
            .findFirst();

        Country country = resolveCountry(firstPad.getLocation().getCountryCode());
        if (country == null) {
            log.debug("Skipping launch site {} - unknown country: {}",
                locationName, firstPad.getLocation().getCountryCode());
            return SyncResult.SKIPPED;
        }

        LaunchSite site;
        boolean isNew;

        if (existingOpt.isPresent()) {
            site = existingOpt.get();
            isNew = false;
        } else {
            site = new LaunchSite();
            site.setName(locationName);
            isNew = true;
        }

        // Map pad data to launch site
        mapPadsToLaunchSite(pads, site, country);

        launchSiteRepository.save(site);

        return isNew ? SyncResult.CREATED : SyncResult.UPDATED;
    }

    private void mapPadsToLaunchSite(List<PadDto> pads, LaunchSite site, Country country) {
        site.setCountry(country);

        PadDto firstPad = pads.get(0);

        // Set location details
        if (firstPad.getLocation() != null) {
            site.setDescription(firstPad.getLocation().getDescription());
            site.setTimeZone(firstPad.getLocation().getTimezoneName());

            // Calculate total launches from all pads at this location
            int totalLaunches = firstPad.getLocation().getTotalLaunchCount() != null
                ? firstPad.getLocation().getTotalLaunchCount() : 0;
            site.setTotalLaunches(totalLaunches);

            // Set image URL
            if (firstPad.getLocation().getMapImage() != null) {
                site.setImageUrl(firstPad.getLocation().getMapImage());
            }
        }

        // Set coordinates from first pad
        if (firstPad.getLatitude() != null) {
            try {
                site.setLatitude(Double.parseDouble(firstPad.getLatitude()));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        if (firstPad.getLongitude() != null) {
            try {
                site.setLongitude(Double.parseDouble(firstPad.getLongitude()));
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        // Count pads
        site.setNumberOfLaunchPads(pads.size());
        site.setActiveLaunchPads(pads.size()); // Assume all are active

        // Set status to operational if there are recent launches
        site.setStatus(site.getTotalLaunches() > 0 ? LaunchSiteStatus.OPERATIONAL : LaunchSiteStatus.PLANNED);

        // Set reference URL
        if (firstPad.getWikiUrl() != null) {
            site.setReferenceUrl(firstPad.getWikiUrl());
        }

        // Aggregate supported vehicles from pad names
        String supportedVehicles = pads.stream()
            .map(PadDto::getName)
            .distinct()
            .collect(Collectors.joining(", "));
        site.setSupportedVehicles(supportedVehicles);

        // Set orbital capabilities (assume LEO support if operational)
        if (site.getTotalLaunches() > 0) {
            site.setSupportsLeo(true);
        }
    }

    // ==================== Helper Methods ====================

    private void loadCountryCache() {
        if (countryCache.isEmpty()) {
            countryRepository.findAll().forEach(c -> {
                if (c.getIsoCode() != null) {
                    countryCache.put(c.getIsoCode().toUpperCase(), c);
                }
            });
            log.info("Loaded {} countries into cache", countryCache.size());
        }
    }

    /**
     * Infer country from agency name for common space agencies
     */
    private Country inferCountryFromAgencyName(String agencyName) {
        if (agencyName == null) return null;

        String name = agencyName.toLowerCase();

        // US agencies
        if (name.contains("spacex") || name.contains("nasa") || name.contains("united launch") ||
            name.contains("rocket lab") || name.contains("blue origin") || name.contains("orbital") ||
            name.contains("northrop") || name.contains("lockheed") || name.contains("boeing")) {
            return countryCache.get("USA");
        }

        // Russian agencies
        if (name.contains("roscosmos") || name.contains("soviet") || name.contains("russian") ||
            name.contains("khrunichev") || name.contains("progress") || name.contains("energia")) {
            return countryCache.get("RUS");
        }

        // Chinese agencies
        if (name.contains("china") || name.contains("casc") || name.contains("chinese") ||
            name.contains("long march") || name.contains("calt")) {
            return countryCache.get("CHN");
        }

        // European
        if (name.contains("arianespace") || name.contains("esa") || name.contains("european")) {
            return countryCache.get("ESA");
        }

        // Japanese
        if (name.contains("jaxa") || name.contains("japan") || name.contains("mitsubishi heavy")) {
            return countryCache.get("JPN");
        }

        // Indian
        if (name.contains("isro") || name.contains("india") || name.contains("indian")) {
            return countryCache.get("IND");
        }

        // Other countries
        if (name.contains("korea") && !name.contains("north")) {
            return countryCache.get("KOR");
        }
        if (name.contains("iran")) {
            return countryCache.get("IRN");
        }
        if (name.contains("israel")) {
            return countryCache.get("ISR");
        }
        if (name.contains("new zealand") || name.contains("rocket lab launch")) {
            return countryCache.get("NZL");
        }
        if (name.contains("ukrain")) {
            return countryCache.get("UKR");
        }

        return null;
    }

    private Country resolveCountry(String countryCode) {
        if (countryCode == null) return null;

        String code = countryCode.toUpperCase();

        // Direct lookup
        Country country = countryCache.get(code);
        if (country != null) return country;

        // Try reverse mapping for 2-letter to 3-letter codes (API sometimes uses 2-letter)
        String mapped = switch (code) {
            case "RU" -> "RUS";
            case "CN" -> "CHN";
            case "JP" -> "JPN";
            case "KR" -> "KOR";
            case "GB", "UK" -> "GBR";
            case "FR" -> "FRA";
            case "DE" -> "DEU";
            case "IN" -> "IND";
            case "IL" -> "ISR";
            case "IR" -> "IRN";
            case "KZ" -> "KAZ";
            case "NZ" -> "NZL";
            case "AE" -> "ARE";
            case "BR" -> "BRA";
            case "AU" -> "AUS";
            case "IT" -> "ITA";
            case "ES" -> "ESP";
            case "PL" -> "POL";
            case "SE" -> "SWE";
            case "CH" -> "CHE";
            case "NL" -> "NLD";
            case "BE" -> "BEL";
            case "AT" -> "AUT";
            case "NO" -> "NOR";
            case "CA" -> "CAN";
            case "UA" -> "UKR";
            case "KP" -> "PRK";
            default -> code;
        };

        return countryCache.get(mapped);
    }

    private enum SyncResult {
        CREATED, UPDATED, SKIPPED
    }
}
