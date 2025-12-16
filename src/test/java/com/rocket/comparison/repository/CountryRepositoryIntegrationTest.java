package com.rocket.comparison.repository;

import com.rocket.comparison.BaseIntegrationTest;
import com.rocket.comparison.entity.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for CountryRepository using TestContainers (Step 1.3)
 * Tests run against a real PostgreSQL database.
 */
@Transactional
class CountryRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        // Create test data since we're using create-drop (no DataSeeder)
        countryRepository.deleteAll();

        Country usa = createCountry("United States", "USA", 95.0, true, true, 1800, 85.0);
        Country russia = createCountry("Russia", "RUS", 85.0, true, true, 3200, 75.0);
        Country china = createCountry("China", "CHN", 88.0, true, true, 450, 80.0);
        Country india = createCountry("India", "IND", 62.0, false, true, 130, 70.0);

        countryRepository.saveAll(List.of(usa, russia, china, india));
    }

    private Country createCountry(String name, String isoCode, Double score,
                                   Boolean humanCapable, Boolean launchCapable,
                                   Integer totalLaunches, Double successRate) {
        Country country = new Country();
        country.setName(name);
        country.setIsoCode(isoCode);
        country.setOverallCapabilityScore(score);
        country.setHumanSpaceflightCapable(humanCapable);
        country.setIndependentLaunchCapable(launchCapable);
        country.setTotalLaunches(totalLaunches);
        country.setLaunchSuccessRate(successRate);
        country.setRegion("Test Region");
        return country;
    }

    @Test
    void shouldFindCountryByIsoCode() {
        // When
        Optional<Country> usa = countryRepository.findByIsoCode("USA");

        // Then
        assertThat(usa).isPresent();
        assertThat(usa.get().getName()).isEqualTo("United States");
    }

    @Test
    void shouldFindCountriesWithLaunchCapability() {
        // When
        List<Country> launchCapable = countryRepository.findByIndependentLaunchCapableTrue();

        // Then
        assertThat(launchCapable).hasSize(4);
        assertThat(launchCapable)
                .extracting(Country::getIndependentLaunchCapable)
                .containsOnly(true);
    }

    @Test
    void shouldCountCountriesWithHumanSpaceflight() {
        // When
        Long count = countryRepository.countWithHumanSpaceflight();

        // Then
        assertThat(count).isEqualTo(3L); // USA, Russia, China
    }

    @Test
    void shouldFindCountryWithMostLaunches() {
        // When
        Optional<Country> topLauncher = countryRepository.findCountryWithMostLaunches();

        // Then
        assertThat(topLauncher).isPresent();
        assertThat(topLauncher.get().getIsoCode()).isEqualTo("RUS"); // Russia has 3200 launches
    }

    @Test
    void shouldFindAllCountriesOrderedByCapabilityScore() {
        // When
        List<Country> ranked = countryRepository.findAllOrderByCapabilityScoreDesc();

        // Then
        assertThat(ranked).hasSize(4);
        assertThat(ranked.get(0).getIsoCode()).isEqualTo("USA"); // 95.0
        assertThat(ranked.get(1).getIsoCode()).isEqualTo("CHN"); // 88.0
        assertThat(ranked.get(2).getIsoCode()).isEqualTo("RUS"); // 85.0
        assertThat(ranked.get(3).getIsoCode()).isEqualTo("IND"); // 62.0
    }

    @Test
    void shouldSaveAndRetrieveCountry() {
        // Given
        Country newCountry = new Country();
        newCountry.setName("Test Country");
        newCountry.setIsoCode("TST");
        newCountry.setRegion("Test Region");
        newCountry.setIndependentLaunchCapable(false);

        // When
        Country saved = countryRepository.save(newCountry);
        Optional<Country> retrieved = countryRepository.findById(saved.getId());

        // Then
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Test Country");
        assertThat(retrieved.get().getIsoCode()).isEqualTo("TST");
    }
}
