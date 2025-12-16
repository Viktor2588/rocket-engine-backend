package com.rocket.comparison.service;

import com.rocket.comparison.entity.MilestoneType;
import com.rocket.comparison.entity.SpaceMilestone;
import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.SpaceMilestoneRepository;
import com.rocket.comparison.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceMilestoneService {

    private final SpaceMilestoneRepository milestoneRepository;
    private final CountryRepository countryRepository;

    // ==================== Basic CRUD ====================

    public List<SpaceMilestone> getAllMilestones() {
        return milestoneRepository.findAll();
    }

    public Optional<SpaceMilestone> getMilestoneById(Long id) {
        return milestoneRepository.findById(id);
    }

    @Transactional
    public SpaceMilestone saveMilestone(SpaceMilestone milestone) {
        // Auto-assign global rank if this is a new milestone type achievement
        if (milestone.getGlobalRank() == null) {
            List<SpaceMilestone> existing = milestoneRepository
                    .findByMilestoneTypeOrderByGlobalRankAsc(milestone.getMilestoneType());
            milestone.setGlobalRank(existing.size() + 1);
        }
        return milestoneRepository.save(milestone);
    }

    @Transactional
    public void deleteMilestone(Long id) {
        milestoneRepository.deleteById(id);
    }

    @Transactional
    public SpaceMilestone updateMilestone(Long id, SpaceMilestone details) {
        SpaceMilestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Milestone not found: " + id));

        milestone.setCountry(details.getCountry());
        milestone.setMilestoneType(details.getMilestoneType());
        milestone.setDateAchieved(details.getDateAchieved());
        milestone.setGlobalRank(details.getGlobalRank());
        milestone.setTitle(details.getTitle());
        milestone.setAchievedBy(details.getAchievedBy());
        milestone.setMissionName(details.getMissionName());
        milestone.setDescription(details.getDescription());
        milestone.setImageUrl(details.getImageUrl());
        milestone.setReferenceUrl(details.getReferenceUrl());

        return milestoneRepository.save(milestone);
    }

    // ==================== By Country ====================

    public List<SpaceMilestone> getMilestonesByCountry(Long countryId) {
        return milestoneRepository.findByCountryIdOrderByDateAchievedAsc(countryId);
    }

    public List<SpaceMilestone> getMilestonesByCountryCode(String isoCode) {
        return milestoneRepository.findByCountryIsoCodeOrderByDateAchievedAsc(isoCode.toUpperCase());
    }

    public List<SpaceMilestone> getFirstsByCountry(Long countryId) {
        return milestoneRepository.findFirstsByCountry(countryId);
    }

    // ==================== By Milestone Type ====================

    public List<SpaceMilestone> getMilestonesByType(MilestoneType type) {
        return milestoneRepository.findByMilestoneTypeOrderByGlobalRankAsc(type);
    }

    public Optional<SpaceMilestone> getFirstAchiever(MilestoneType type) {
        return milestoneRepository.findFirstAchiever(type);
    }

    public List<SpaceMilestone> getMilestonesByCategory(String category) {
        List<MilestoneType> types = MilestoneType.getByCategory(category);
        return milestoneRepository.findByMilestoneTypes(types);
    }

    // ==================== Timeline ====================

    public List<SpaceMilestone> getTimeline(Integer startYear, Integer endYear) {
        return milestoneRepository.findByYearRange(startYear, endYear);
    }

    public List<SpaceMilestone> getMilestonesByYear(Integer year) {
        return milestoneRepository.findByYearOrderByDateAchievedAsc(year);
    }

    public List<SpaceMilestone> getMilestonesByDecade(Integer decade) {
        return milestoneRepository.findByDecadeOrderByDateAchievedAsc(decade);
    }

    public List<SpaceMilestone> getMilestonesByEra(String era) {
        return milestoneRepository.findByEraOrderByDateAchievedAsc(era);
    }

    public List<SpaceMilestone> getMilestonesByDateRange(LocalDate start, LocalDate end) {
        return milestoneRepository.findByDateRange(start, end);
    }

    // ==================== Global Firsts ====================

    public List<SpaceMilestone> getAllGlobalFirsts() {
        return milestoneRepository.findAllGlobalFirsts();
    }

    public List<SpaceMilestone> getFirstAchievers() {
        return milestoneRepository.findFirstAchievers();
    }

    // ==================== Statistics (BE-010: Use DB aggregations) ====================

    public long countAll() {
        return milestoneRepository.count();
    }

    public long countGlobalFirsts() {
        return milestoneRepository.countGlobalFirsts();
    }

    public Map<String, Long> countByCategory() {
        List<Object[]> results = milestoneRepository.countByMilestoneType();
        return results.stream()
                .collect(Collectors.groupingBy(
                        row -> ((MilestoneType) row[0]).getCategory(),
                        Collectors.summingLong(row -> (Long) row[1])
                ));
    }

    public Map<String, Long> countByEra() {
        List<Object[]> results = milestoneRepository.countByEra();
        Map<String, Long> byEra = new LinkedHashMap<>();
        for (Object[] row : results) {
            byEra.put((String) row[0], (Long) row[1]);
        }
        return byEra;
    }

    public int countYearsSpanned() {
        return milestoneRepository.findAllYears().size();
    }

    public Long countMilestonesByCountry(Long countryId) {
        return milestoneRepository.countByCountry(countryId);
    }

    public Long countFirstsByCountry(Long countryId) {
        return milestoneRepository.countFirstsByCountry(countryId);
    }

    public Map<String, Long> getFirstsLeaderboard() {
        List<Object[]> results = milestoneRepository.countFirstsByCountryRanked();
        Map<String, Long> leaderboard = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long countryId = (Long) row[0];
            Long count = (Long) row[1];

            countryRepository.findById(countryId).ifPresent(country ->
                    leaderboard.put(country.getName(), count)
            );
        }

        return leaderboard;
    }

    public List<Integer> getAllYears() {
        return milestoneRepository.findAllYears();
    }

    public List<Integer> getAllDecades() {
        return milestoneRepository.findAllDecades();
    }

    public List<String> getAllEras() {
        return milestoneRepository.findAllEras();
    }

    // ==================== Search ====================

    public List<SpaceMilestone> searchMilestones(String query) {
        return milestoneRepository.searchMilestones(query);
    }

    // ==================== Timeline Comparison ====================

    /**
     * Get comparative timeline showing milestones from multiple countries
     */
    public Map<String, Object> compareTimelines(List<Long> countryIds, Integer startYear, Integer endYear) {
        Map<String, Object> comparison = new HashMap<>();

        // Get milestones for each country
        List<Map<String, Object>> countryTimelines = new ArrayList<>();
        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId).orElse(null);
            if (country != null) {
                Map<String, Object> timeline = new HashMap<>();
                timeline.put("countryId", country.getId());
                timeline.put("countryName", country.getName());
                timeline.put("isoCode", country.getIsoCode());

                List<SpaceMilestone> milestones = getMilestonesByCountry(countryId).stream()
                        .filter(m -> startYear == null || m.getYear() >= startYear)
                        .filter(m -> endYear == null || m.getYear() <= endYear)
                        .toList();

                timeline.put("milestones", milestones);
                timeline.put("totalMilestones", milestones.size());
                timeline.put("globalFirsts", milestones.stream().filter(m -> Boolean.TRUE.equals(m.getIsGlobalFirst())).count());

                countryTimelines.add(timeline);
            }
        }

        comparison.put("countryTimelines", countryTimelines);
        comparison.put("startYear", startYear);
        comparison.put("endYear", endYear);

        return comparison;
    }

    /**
     * Get "Space Race" view showing key milestones for competing nations
     */
    public Map<String, Object> getSpaceRaceTimeline(Long country1Id, Long country2Id) {
        Map<String, Object> spaceRace = new HashMap<>();

        Country country1 = countryRepository.findById(country1Id).orElse(null);
        Country country2 = countryRepository.findById(country2Id).orElse(null);

        if (country1 == null || country2 == null) {
            return Map.of("error", "One or both countries not found");
        }

        List<SpaceMilestone> milestones1 = getMilestonesByCountry(country1Id);
        List<SpaceMilestone> milestones2 = getMilestonesByCountry(country2Id);

        // Create combined timeline
        List<Map<String, Object>> combinedTimeline = new ArrayList<>();

        Set<MilestoneType> achievedTypes = new HashSet<>();
        milestones1.forEach(m -> achievedTypes.add(m.getMilestoneType()));
        milestones2.forEach(m -> achievedTypes.add(m.getMilestoneType()));

        for (MilestoneType type : achievedTypes) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("milestoneType", type.name());
            entry.put("displayName", type.getDisplayName());
            entry.put("category", type.getCategory());

            // Find achievements by each country
            Optional<SpaceMilestone> achievement1 = milestones1.stream()
                    .filter(m -> m.getMilestoneType() == type)
                    .findFirst();
            Optional<SpaceMilestone> achievement2 = milestones2.stream()
                    .filter(m -> m.getMilestoneType() == type)
                    .findFirst();

            entry.put(country1.getIsoCode(), achievement1.map(this::toSummary).orElse(null));
            entry.put(country2.getIsoCode(), achievement2.map(this::toSummary).orElse(null));

            // Determine winner
            if (achievement1.isPresent() && achievement2.isPresent()) {
                if (achievement1.get().getDateAchieved().isBefore(achievement2.get().getDateAchieved())) {
                    entry.put("winner", country1.getIsoCode());
                } else if (achievement2.get().getDateAchieved().isBefore(achievement1.get().getDateAchieved())) {
                    entry.put("winner", country2.getIsoCode());
                } else {
                    entry.put("winner", "tie");
                }
            } else if (achievement1.isPresent()) {
                entry.put("winner", country1.getIsoCode());
            } else {
                entry.put("winner", country2.getIsoCode());
            }

            combinedTimeline.add(entry);
        }

        // Sort by earliest achievement date
        combinedTimeline.sort((a, b) -> {
            LocalDate date1 = getEarliestDate(a, country1.getIsoCode(), country2.getIsoCode());
            LocalDate date2 = getEarliestDate(b, country1.getIsoCode(), country2.getIsoCode());
            return date1.compareTo(date2);
        });

        spaceRace.put("country1", Map.of("id", country1.getId(), "name", country1.getName(), "isoCode", country1.getIsoCode()));
        spaceRace.put("country2", Map.of("id", country2.getId(), "name", country2.getName(), "isoCode", country2.getIsoCode()));
        spaceRace.put("timeline", combinedTimeline);

        // Summary stats
        long wins1 = combinedTimeline.stream().filter(e -> country1.getIsoCode().equals(e.get("winner"))).count();
        long wins2 = combinedTimeline.stream().filter(e -> country2.getIsoCode().equals(e.get("winner"))).count();
        spaceRace.put("summary", Map.of(
                country1.getIsoCode() + "_wins", wins1,
                country2.getIsoCode() + "_wins", wins2,
                "ties", combinedTimeline.stream().filter(e -> "tie".equals(e.get("winner"))).count()
        ));

        return spaceRace;
    }

    private Map<String, Object> toSummary(SpaceMilestone milestone) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("id", milestone.getId());
        summary.put("date", milestone.getDateAchieved());
        summary.put("achievedBy", milestone.getAchievedBy());
        summary.put("missionName", milestone.getMissionName());
        summary.put("globalRank", milestone.getGlobalRank());
        return summary;
    }

    @SuppressWarnings("unchecked")
    private LocalDate getEarliestDate(Map<String, Object> entry, String code1, String code2) {
        LocalDate date1 = null;
        LocalDate date2 = null;

        Map<String, Object> m1 = (Map<String, Object>) entry.get(code1);
        Map<String, Object> m2 = (Map<String, Object>) entry.get(code2);

        if (m1 != null) date1 = (LocalDate) m1.get("date");
        if (m2 != null) date2 = (LocalDate) m2.get("date");

        if (date1 == null) return date2 != null ? date2 : LocalDate.MAX;
        if (date2 == null) return date1;
        return date1.isBefore(date2) ? date1 : date2;
    }
}
