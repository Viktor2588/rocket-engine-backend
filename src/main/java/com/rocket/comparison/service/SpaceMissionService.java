package com.rocket.comparison.service;

import com.rocket.comparison.entity.*;
import com.rocket.comparison.repository.CountryRepository;
import com.rocket.comparison.repository.SpaceMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceMissionService {

    private final SpaceMissionRepository missionRepository;
    private final CountryRepository countryRepository;

    // ==================== Basic CRUD ====================

    public List<SpaceMission> getAllMissions() {
        return missionRepository.findAll();
    }

    public Optional<SpaceMission> getMissionById(Long id) {
        return missionRepository.findById(id);
    }

    @Transactional
    public SpaceMission saveMission(SpaceMission mission) {
        return missionRepository.save(mission);
    }

    @Transactional
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    @Transactional
    public SpaceMission updateMission(Long id, SpaceMission details) {
        SpaceMission mission = missionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found: " + id));

        mission.setName(details.getName());
        mission.setMissionDesignation(details.getMissionDesignation());
        mission.setAlternateName(details.getAlternateName());
        mission.setCountry(details.getCountry());
        mission.setOperator(details.getOperator());
        mission.setLaunchVehicleName(details.getLaunchVehicleName());
        mission.setLaunchDate(details.getLaunchDate());
        mission.setEndDate(details.getEndDate());
        mission.setStatus(details.getStatus());
        mission.setMissionType(details.getMissionType());
        mission.setDestination(details.getDestination());
        mission.setCrewed(details.getCrewed());
        mission.setCrewSize(details.getCrewSize());
        mission.setCrewNames(details.getCrewNames());
        mission.setCommander(details.getCommander());
        mission.setLaunchSite(details.getLaunchSite());
        mission.setMissionMassKg(details.getMissionMassKg());
        mission.setPayloadMassKg(details.getPayloadMassKg());
        mission.setIsHistoricFirst(details.getIsHistoricFirst());
        mission.setHistoricFirstType(details.getHistoricFirstType());
        mission.setDescription(details.getDescription());
        mission.setObjectives(details.getObjectives());
        mission.setOutcomes(details.getOutcomes());
        mission.setImageUrl(details.getImageUrl());
        mission.setPatchUrl(details.getPatchUrl());
        mission.setReferenceUrl(details.getReferenceUrl());

        return missionRepository.save(mission);
    }

    // ==================== By Country ====================

    public List<SpaceMission> getMissionsByCountry(Long countryId) {
        return missionRepository.findByCountryIdOrderByLaunchDateDesc(countryId);
    }

    public List<SpaceMission> getMissionsByCountryCode(String isoCode) {
        return missionRepository.findByCountryIsoCodeOrderByLaunchDateDesc(isoCode.toUpperCase());
    }

    public List<SpaceMission> getMissionsByCountryAndStatus(Long countryId, MissionStatus status) {
        return missionRepository.findByCountryAndStatus(countryId, status);
    }

    // ==================== By Status ====================

    public List<SpaceMission> getMissionsByStatus(MissionStatus status) {
        return missionRepository.findByStatusOrderByLaunchDateDesc(status);
    }

    public List<SpaceMission> getActiveMissions() {
        return missionRepository.findActiveMissions();
    }

    public List<SpaceMission> getUpcomingMissions() {
        return missionRepository.findUpcomingMissions();
    }

    public List<SpaceMission> getCompletedMissions() {
        return missionRepository.findByStatusOrderByLaunchDateDesc(MissionStatus.COMPLETED);
    }

    // ==================== By Mission Type ====================

    public List<SpaceMission> getMissionsByType(MissionType type) {
        return missionRepository.findByMissionTypeOrderByLaunchDateDesc(type);
    }

    public List<SpaceMission> getMissionsByCategory(String category) {
        return missionRepository.findByMissionCategory(category);
    }

    public List<SpaceMission> getMissionsByTypes(List<MissionType> types) {
        return missionRepository.findByMissionTypes(types);
    }

    // ==================== By Destination ====================

    public List<SpaceMission> getMissionsByDestination(Destination destination) {
        return missionRepository.findByDestinationOrderByLaunchDateDesc(destination);
    }

    public List<SpaceMission> getSuccessfulMissionsByDestination(Destination destination) {
        return missionRepository.findSuccessfulMissionsByDestination(destination);
    }

    // ==================== Crewed Missions ====================

    public List<SpaceMission> getCrewedMissions() {
        return missionRepository.findCrewedMissions();
    }

    public List<SpaceMission> getCrewedMissionsByCountry(Long countryId) {
        return missionRepository.findCrewedMissionsByCountry(countryId);
    }

    // ==================== Historic Firsts ====================

    public List<SpaceMission> getHistoricFirsts() {
        return missionRepository.findHistoricFirsts();
    }

    public List<SpaceMission> getHistoricFirstsByCountry(Long countryId) {
        return missionRepository.findHistoricFirstsByCountry(countryId);
    }

    // ==================== Timeline ====================

    public List<SpaceMission> getMissionsByYear(Integer year) {
        return missionRepository.findByLaunchYear(year);
    }

    public List<SpaceMission> getMissionsByDecade(Integer decade) {
        return missionRepository.findByLaunchDecade(decade);
    }

    public List<SpaceMission> getMissionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return missionRepository.findByDateRange(startDate, endDate);
    }

    public List<SpaceMission> getMissionsByYearRange(Integer startYear, Integer endYear) {
        return missionRepository.findByYearRange(startYear, endYear);
    }

    public List<SpaceMission> getTimeline(Integer startYear, Integer endYear) {
        if (startYear == null) startYear = 1957;
        if (endYear == null) endYear = LocalDate.now().getYear();
        return missionRepository.findByYearRange(startYear, endYear);
    }

    // ==================== Statistics ====================

    public Long countMissionsByCountry(Long countryId) {
        return missionRepository.countByCountry(countryId);
    }

    public Long countSuccessfulMissionsByCountry(Long countryId) {
        return missionRepository.countSuccessfulByCountry(countryId);
    }

    public Long countFailedMissionsByCountry(Long countryId) {
        return missionRepository.countFailedByCountry(countryId);
    }

    public Double getSuccessRateByCountry(Long countryId) {
        Long total = countMissionsByCountry(countryId);
        if (total == 0) return 0.0;
        Long successful = countSuccessfulMissionsByCountry(countryId);
        return (successful.doubleValue() / total.doubleValue()) * 100.0;
    }

    public Map<String, Long> getMissionCountsByCountry() {
        List<Object[]> results = missionRepository.countMissionsByCountry();
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

    public Map<String, Long> getMissionCountsByType() {
        List<Object[]> results = missionRepository.countMissionsByType();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            MissionType type = (MissionType) row[0];
            Long count = (Long) row[1];
            counts.put(type.getDisplayName(), count);
        }
        return counts;
    }

    public Map<String, Long> getMissionCountsByDestination() {
        List<Object[]> results = missionRepository.countMissionsByDestination();
        Map<String, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            Destination dest = (Destination) row[0];
            Long count = (Long) row[1];
            counts.put(dest.getDisplayName(), count);
        }
        return counts;
    }

    public Map<Integer, Long> getMissionCountsByYear() {
        List<Object[]> results = missionRepository.countMissionsByYear();
        Map<Integer, Long> counts = new LinkedHashMap<>();

        for (Object[] row : results) {
            Integer year = (Integer) row[0];
            Long count = (Long) row[1];
            counts.put(year, count);
        }
        return counts;
    }

    // ==================== Lists ====================

    public List<Integer> getAllLaunchYears() {
        return missionRepository.findAllLaunchYears();
    }

    public List<Integer> getAllLaunchDecades() {
        return missionRepository.findAllLaunchDecades();
    }

    public List<String> getAllMissionCategories() {
        return missionRepository.findAllMissionCategories();
    }

    public List<String> getAllOperators() {
        return missionRepository.findAllOperators();
    }

    public List<String> getAllLaunchSites() {
        return missionRepository.findAllLaunchSites();
    }

    // ==================== Search ====================

    public List<SpaceMission> searchMissions(String query) {
        return missionRepository.searchMissions(query);
    }

    // ==================== Records ====================

    public List<SpaceMission> getLongestMissions(int limit) {
        return missionRepository.findLongestMissions().stream()
                .limit(limit)
                .toList();
    }

    public List<SpaceMission> getFarthestMissions(int limit) {
        return missionRepository.findFarthestMissions().stream()
                .limit(limit)
                .toList();
    }

    public List<SpaceMission> getLargestCrewMissions(int limit) {
        return missionRepository.findLargestCrewMissions().stream()
                .limit(limit)
                .toList();
    }

    public List<SpaceMission> getSampleReturnMissions() {
        return missionRepository.findSampleReturnMissions();
    }

    public List<SpaceMission> getMissionsWithEVA() {
        return missionRepository.findMissionsWithEVA();
    }

    // ==================== Comparison ====================

    /**
     * Compare missions between multiple countries
     */
    public Map<String, Object> compareMissionsByCountries(List<Long> countryIds, Integer startYear, Integer endYear) {
        Map<String, Object> comparison = new HashMap<>();
        List<Map<String, Object>> countryData = new ArrayList<>();

        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId).orElse(null);
            if (country == null) continue;

            List<SpaceMission> missions = getMissionsByCountry(countryId).stream()
                    .filter(m -> startYear == null || (m.getLaunchYear() != null && m.getLaunchYear() >= startYear))
                    .filter(m -> endYear == null || (m.getLaunchYear() != null && m.getLaunchYear() <= endYear))
                    .toList();

            Map<String, Object> data = new HashMap<>();
            data.put("countryId", countryId);
            data.put("countryName", country.getName());
            data.put("isoCode", country.getIsoCode());
            data.put("totalMissions", missions.size());
            data.put("crewedMissions", missions.stream().filter(m -> Boolean.TRUE.equals(m.getCrewed())).count());
            data.put("successfulMissions", missions.stream().filter(m -> m.getStatus() == MissionStatus.COMPLETED).count());
            data.put("failedMissions", missions.stream().filter(m -> m.getStatus() == MissionStatus.FAILED || m.getStatus() == MissionStatus.LOST).count());
            data.put("historicFirsts", missions.stream().filter(m -> Boolean.TRUE.equals(m.getIsHistoricFirst())).count());

            // By destination category
            Map<String, Long> byDestination = missions.stream()
                    .filter(m -> m.getDestination() != null)
                    .collect(Collectors.groupingBy(
                            m -> m.getDestination().getCategory(),
                            Collectors.counting()
                    ));
            data.put("missionsByDestinationCategory", byDestination);

            // By mission category
            Map<String, Long> byMissionCategory = missions.stream()
                    .filter(m -> m.getMissionCategory() != null)
                    .collect(Collectors.groupingBy(
                            SpaceMission::getMissionCategory,
                            Collectors.counting()
                    ));
            data.put("missionsByMissionCategory", byMissionCategory);

            countryData.add(data);
        }

        comparison.put("countries", countryData);
        comparison.put("startYear", startYear);
        comparison.put("endYear", endYear);

        return comparison;
    }

    /**
     * Get destination-specific comparison for countries
     */
    public Map<String, Object> compareMissionsByDestination(Destination destination, List<Long> countryIds) {
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("destination", destination.getDisplayName());

        List<Map<String, Object>> countryData = new ArrayList<>();

        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId).orElse(null);
            if (country == null) continue;

            List<SpaceMission> missions = getMissionsByCountry(countryId).stream()
                    .filter(m -> m.getDestination() == destination)
                    .toList();

            Map<String, Object> data = new HashMap<>();
            data.put("countryId", countryId);
            data.put("countryName", country.getName());
            data.put("totalMissions", missions.size());
            data.put("successfulMissions", missions.stream().filter(m -> m.getStatus() == MissionStatus.COMPLETED).count());
            data.put("firstMission", missions.stream()
                    .min(Comparator.comparing(SpaceMission::getLaunchDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(m -> Map.of("name", m.getName(), "date", m.getLaunchDate()))
                    .orElse(null));
            data.put("latestMission", missions.stream()
                    .max(Comparator.comparing(SpaceMission::getLaunchDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .map(m -> Map.of("name", m.getName(), "date", m.getLaunchDate()))
                    .orElse(null));

            countryData.add(data);
        }

        comparison.put("countries", countryData);
        return comparison;
    }

    /**
     * Get mission summary statistics
     */
    public Map<String, Object> getMissionStatistics() {
        List<SpaceMission> allMissions = getAllMissions();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMissions", allMissions.size());
        stats.put("crewedMissions", allMissions.stream().filter(m -> Boolean.TRUE.equals(m.getCrewed())).count());
        stats.put("activeMissions", allMissions.stream().filter(m -> m.getStatus().isActive()).count());
        stats.put("completedMissions", allMissions.stream().filter(m -> m.getStatus().isCompleted()).count());
        stats.put("failedMissions", allMissions.stream().filter(m -> m.getStatus().isFailed()).count());
        stats.put("plannedMissions", allMissions.stream().filter(m -> m.getStatus().isPending()).count());
        stats.put("historicFirsts", allMissions.stream().filter(m -> Boolean.TRUE.equals(m.getIsHistoricFirst())).count());

        // Counts by category
        stats.put("missionsByType", getMissionCountsByType());
        stats.put("missionsByDestination", getMissionCountsByDestination());
        stats.put("missionsByCountry", getMissionCountsByCountry());
        stats.put("missionsByYear", getMissionCountsByYear());

        // Year range
        stats.put("launchYears", getAllLaunchYears());
        stats.put("launchDecades", getAllLaunchDecades());

        return stats;
    }

    /**
     * Get country-specific mission statistics
     */
    public Map<String, Object> getCountryMissionStatistics(Long countryId) {
        List<SpaceMission> missions = getMissionsByCountry(countryId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMissions", missions.size());
        stats.put("crewedMissions", missions.stream().filter(m -> Boolean.TRUE.equals(m.getCrewed())).count());
        stats.put("activeMissions", missions.stream().filter(m -> m.getStatus().isActive()).count());
        stats.put("completedMissions", missions.stream().filter(m -> m.getStatus().isCompleted()).count());
        stats.put("failedMissions", missions.stream().filter(m -> m.getStatus().isFailed()).count());
        stats.put("historicFirsts", missions.stream().filter(m -> Boolean.TRUE.equals(m.getIsHistoricFirst())).count());
        stats.put("successRate", getSuccessRateByCountry(countryId));

        // By type
        Map<String, Long> byType = missions.stream()
                .filter(m -> m.getMissionType() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getMissionType().getDisplayName(),
                        Collectors.counting()
                ));
        stats.put("byType", byType);

        // By destination
        Map<String, Long> byDestination = missions.stream()
                .filter(m -> m.getDestination() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getDestination().getDisplayName(),
                        Collectors.counting()
                ));
        stats.put("byDestination", byDestination);

        // By year
        Map<Integer, Long> byYear = missions.stream()
                .filter(m -> m.getLaunchYear() != null)
                .collect(Collectors.groupingBy(
                        SpaceMission::getLaunchYear,
                        TreeMap::new,
                        Collectors.counting()
                ));
        stats.put("byYear", byYear);

        return stats;
    }
}
