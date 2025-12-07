package com.rocket.comparison.controller;

import com.rocket.comparison.entity.Country;
import com.rocket.comparison.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    // ==================== Basic CRUD ====================

    @GetMapping
    public ResponseEntity<?> getAllCountries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Boolean unpaged) {
        // If unpaged=true, return simple list (for frontend compatibility)
        if (Boolean.TRUE.equals(unpaged)) {
            return ResponseEntity.ok(countryService.getAllCountries());
        }
        Sort sort = sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sort);
        return ResponseEntity.ok(countryService.getAllCountries(pageable));
    }

    @GetMapping({"/all", "/list"})
    public ResponseEntity<List<Country>> getAllCountriesUnpaged() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        Optional<Country> country = countryService.getCountryById(id);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-code/{isoCode}")
    public ResponseEntity<Country> getCountryByIsoCode(@PathVariable String isoCode) {
        Optional<Country> country = countryService.getCountryByIsoCode(isoCode);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        Optional<Country> country = countryService.getCountryByName(name);
        return country.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) {
        // Check for duplicate ISO code
        if (country.getIsoCode() != null && countryService.existsByIsoCode(country.getIsoCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Country savedCountry = countryService.saveCountry(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @Valid @RequestBody Country countryDetails) {
        try {
            Country updatedCountry = countryService.updateCountry(id, countryDetails);
            return ResponseEntity.ok(updatedCountry);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        if (countryService.existsById(id)) {
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== Region Queries ====================

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Country>> getCountriesByRegion(@PathVariable String region) {
        return ResponseEntity.ok(countryService.getCountriesByRegion(region));
    }

    // ==================== Capability Filters ====================

    @GetMapping("/capability/human-spaceflight")
    public ResponseEntity<List<Country>> getCountriesWithHumanSpaceflight() {
        return ResponseEntity.ok(countryService.getCountriesWithHumanSpaceflight());
    }

    @GetMapping("/capability/launch")
    public ResponseEntity<List<Country>> getCountriesWithLaunchCapability() {
        return ResponseEntity.ok(countryService.getCountriesWithLaunchCapability());
    }

    @GetMapping("/capability/reusable")
    public ResponseEntity<List<Country>> getCountriesWithReusableRockets() {
        return ResponseEntity.ok(countryService.getCountriesWithReusableRockets());
    }

    @GetMapping("/capability/deep-space")
    public ResponseEntity<List<Country>> getCountriesWithDeepSpaceCapability() {
        return ResponseEntity.ok(countryService.getCountriesWithDeepSpaceCapability());
    }

    @GetMapping("/capability/space-station")
    public ResponseEntity<List<Country>> getCountriesWithSpaceStation() {
        return ResponseEntity.ok(countryService.getCountriesWithSpaceStation());
    }

    // ==================== Rankings ====================

    @GetMapping("/rankings")
    public ResponseEntity<List<Country>> getCountryRankings() {
        return ResponseEntity.ok(countryService.getCountryRankings());
    }

    @GetMapping("/rankings/min-score/{minScore}")
    public ResponseEntity<List<Country>> getCountriesByMinScore(@PathVariable Double minScore) {
        return ResponseEntity.ok(countryService.getCountriesByMinScore(minScore));
    }

    @GetMapping("/rankings/by-launches")
    public ResponseEntity<List<Country>> getTopCountriesByLaunches() {
        return ResponseEntity.ok(countryService.getTopCountriesByLaunches());
    }

    @GetMapping("/rankings/by-success-rate")
    public ResponseEntity<List<Country>> getTopCountriesBySuccessRate() {
        return ResponseEntity.ok(countryService.getTopCountriesBySuccessRate());
    }

    // ==================== Statistics ====================

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCountries", countryService.getAllCountries().size());
        stats.put("countriesWithLaunchCapability", countryService.countWithLaunchCapability());
        stats.put("countriesWithHumanSpaceflight", countryService.countWithHumanSpaceflight());
        return ResponseEntity.ok(stats);
    }

    // ==================== Comparison ====================

    @GetMapping("/compare")
    public ResponseEntity<?> compareCountries(@RequestParam List<Long> ids) {
        if (ids == null || ids.size() < 2) {
            return ResponseEntity.badRequest().body("At least 2 country IDs are required for comparison");
        }

        List<Country> countries = ids.stream()
                .map(countryService::getCountryById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (countries.size() < 2) {
            return ResponseEntity.badRequest().body("Could not find enough countries for comparison");
        }

        return ResponseEntity.ok(new CountryComparisonResult(countries));
    }

    @GetMapping("/compare/{id1}/vs/{id2}")
    public ResponseEntity<?> compareTwoCountries(@PathVariable Long id1, @PathVariable Long id2) {
        Optional<Country> country1 = countryService.getCountryById(id1);
        Optional<Country> country2 = countryService.getCountryById(id2);

        if (country1.isEmpty() || country2.isEmpty()) {
            return ResponseEntity.badRequest().body("One or both countries not found");
        }

        return ResponseEntity.ok(new DetailedCountryComparison(country1.get(), country2.get()));
    }

    // ==================== DTOs ====================

    public record CountryComparisonResult(List<Country> countries) {}

    public record DetailedCountryComparison(
            CountrySummary country1,
            CountrySummary country2,
            String overallLeader,
            Double scoreDifference,
            CapabilityComparison capabilities
    ) {
        public DetailedCountryComparison(Country c1, Country c2) {
            this(
                    new CountrySummary(c1),
                    new CountrySummary(c2),
                    determineLeader(c1, c2),
                    calculateScoreDifference(c1, c2),
                    new CapabilityComparison(c1, c2)
            );
        }

        private static String determineLeader(Country c1, Country c2) {
            if (c1.getOverallCapabilityScore() == null && c2.getOverallCapabilityScore() == null) {
                return "Cannot determine - scores not available";
            }
            if (c1.getOverallCapabilityScore() == null) return c2.getName();
            if (c2.getOverallCapabilityScore() == null) return c1.getName();
            return c1.getOverallCapabilityScore() >= c2.getOverallCapabilityScore() ? c1.getName() : c2.getName();
        }

        private static Double calculateScoreDifference(Country c1, Country c2) {
            if (c1.getOverallCapabilityScore() == null || c2.getOverallCapabilityScore() == null) {
                return null;
            }
            return Math.abs(c1.getOverallCapabilityScore() - c2.getOverallCapabilityScore());
        }
    }

    public record CountrySummary(
            Long id,
            String name,
            String isoCode,
            String spaceAgencyName,
            Double overallCapabilityScore,
            Integer totalLaunches,
            Double launchSuccessRate
    ) {
        public CountrySummary(Country c) {
            this(
                    c.getId(),
                    c.getName(),
                    c.getIsoCode(),
                    c.getSpaceAgencyName(),
                    c.getOverallCapabilityScore(),
                    c.getTotalLaunches(),
                    c.getLaunchSuccessRate()
            );
        }
    }

    public record CapabilityComparison(
            CapabilityFlags country1Capabilities,
            CapabilityFlags country2Capabilities,
            List<String> country1Advantages,
            List<String> country2Advantages
    ) {
        public CapabilityComparison(Country c1, Country c2) {
            this(
                    new CapabilityFlags(c1),
                    new CapabilityFlags(c2),
                    findAdvantages(c1, c2),
                    findAdvantages(c2, c1)
            );
        }

        private static List<String> findAdvantages(Country advantaged, Country other) {
            List<String> advantages = new java.util.ArrayList<>();

            if (Boolean.TRUE.equals(advantaged.getHumanSpaceflightCapable()) &&
                !Boolean.TRUE.equals(other.getHumanSpaceflightCapable())) {
                advantages.add("Human Spaceflight");
            }
            if (Boolean.TRUE.equals(advantaged.getReusableRocketCapable()) &&
                !Boolean.TRUE.equals(other.getReusableRocketCapable())) {
                advantages.add("Reusable Rockets");
            }
            if (Boolean.TRUE.equals(advantaged.getSpaceStationCapable()) &&
                !Boolean.TRUE.equals(other.getSpaceStationCapable())) {
                advantages.add("Space Station");
            }
            if (Boolean.TRUE.equals(advantaged.getDeepSpaceCapable()) &&
                !Boolean.TRUE.equals(other.getDeepSpaceCapable())) {
                advantages.add("Deep Space Exploration");
            }
            if (Boolean.TRUE.equals(advantaged.getLunarLandingCapable()) &&
                !Boolean.TRUE.equals(other.getLunarLandingCapable())) {
                advantages.add("Lunar Landing");
            }
            if (Boolean.TRUE.equals(advantaged.getMarsLandingCapable()) &&
                !Boolean.TRUE.equals(other.getMarsLandingCapable())) {
                advantages.add("Mars Landing");
            }

            // Compare numerical metrics
            if (advantaged.getTotalLaunches() != null && other.getTotalLaunches() != null &&
                advantaged.getTotalLaunches() > other.getTotalLaunches()) {
                advantages.add("More Total Launches");
            }
            if (advantaged.getLaunchSuccessRate() != null && other.getLaunchSuccessRate() != null &&
                advantaged.getLaunchSuccessRate() > other.getLaunchSuccessRate()) {
                advantages.add("Higher Success Rate");
            }
            if (advantaged.getActiveAstronauts() != null && other.getActiveAstronauts() != null &&
                advantaged.getActiveAstronauts() > other.getActiveAstronauts()) {
                advantages.add("Larger Astronaut Corps");
            }

            return advantages;
        }
    }

    public record CapabilityFlags(
            boolean humanSpaceflight,
            boolean independentLaunch,
            boolean reusableRockets,
            boolean deepSpace,
            boolean spaceStation,
            boolean lunarLanding,
            boolean marsLanding
    ) {
        public CapabilityFlags(Country c) {
            this(
                    Boolean.TRUE.equals(c.getHumanSpaceflightCapable()),
                    Boolean.TRUE.equals(c.getIndependentLaunchCapable()),
                    Boolean.TRUE.equals(c.getReusableRocketCapable()),
                    Boolean.TRUE.equals(c.getDeepSpaceCapable()),
                    Boolean.TRUE.equals(c.getSpaceStationCapable()),
                    Boolean.TRUE.equals(c.getLunarLandingCapable()),
                    Boolean.TRUE.equals(c.getMarsLandingCapable())
            );
        }
    }
}
