package com.rocket.comparison.config.seeder;

import com.rocket.comparison.entity.CapabilityCategory;
import com.rocket.comparison.entity.CapabilityScore;
import com.rocket.comparison.entity.Country;
import com.rocket.comparison.repository.CapabilityScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Seeds CapabilityScore entities with initial data.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CapabilityScoreSeeder implements EntitySeeder {

    private final CapabilityScoreRepository capabilityScoreRepository;
    private final CountrySeeder countrySeeder;

    @Override
    public void seedIfEmpty() {
        if (count() == 0) {
            log.info("Seeding capability scores...");
            seedCapabilityScores();
            log.info("Seeded {} capability scores", count());
        }
    }

    @Override
    public String getEntityName() {
        return "capability scores";
    }

    @Override
    public long count() {
        return capabilityScoreRepository.count();
    }

    private Map<String, Country> getCountryMap() {
        return countrySeeder.getCountryMap();
    }

    private void seedCapabilityScores() {
        // USA - Global leader in all categories
        seedCountryScores("USA", 98.0, 95.0, 97.0, 96.0, 98.0, 95.0, 98.0);

        // Russia - Strong legacy, declining but capable
        seedCountryScores("RUS", 82.0, 85.0, 88.0, 45.0, 65.0, 78.0, 85.0);

        // China - Rapidly advancing
        seedCountryScores("CHN", 85.0, 75.0, 82.0, 70.0, 78.0, 80.0, 90.0);

        // ESA - Strong collaborative program
        seedCountryScores("ESA", 72.0, 70.0, 55.0, 68.0, 75.0, 65.0, 50.0);

        // Japan - Sophisticated but small
        seedCountryScores("JPN", 68.0, 65.0, 45.0, 72.0, 70.0, 60.0, 70.0);

        // India - Cost-effective, growing
        seedCountryScores("IND", 62.0, 55.0, 30.0, 60.0, 55.0, 50.0, 75.0);

        // South Korea - New entrant
        seedCountryScores("KOR", 35.0, 30.0, 5.0, 20.0, 35.0, 25.0, 40.0);

        // Israel - Small but capable
        seedCountryScores("ISR", 32.0, 25.0, 5.0, 15.0, 45.0, 20.0, 50.0);

        // UK - ESA member, limited independent
        seedCountryScores("GBR", 15.0, 25.0, 10.0, 20.0, 40.0, 15.0, 20.0);

        // Canada - Partner, specialized in robotics
        seedCountryScores("CAN", 5.0, 10.0, 35.0, 20.0, 45.0, 10.0, 15.0);

        // New Zealand - Rocket Lab base
        seedCountryScores("NZL", 45.0, 40.0, 0.0, 15.0, 20.0, 35.0, 30.0);

        // Ukraine - Legacy from USSR
        seedCountryScores("UKR", 25.0, 50.0, 15.0, 10.0, 20.0, 15.0, 35.0);

        // Additional countries (from TheSpaceDevs sync)
        // France - Strong space power, part of ESA
        seedCountryScores("FRA", 65.0, 60.0, 40.0, 55.0, 65.0, 55.0, 45.0);

        // Germany - Strong aerospace industry
        seedCountryScores("DEU", 45.0, 55.0, 25.0, 40.0, 55.0, 40.0, 35.0);

        // Italy - Active ESA partner
        seedCountryScores("ITA", 40.0, 45.0, 20.0, 35.0, 50.0, 35.0, 30.0);

        // Australia - Emerging space nation
        seedCountryScores("AUS", 20.0, 15.0, 5.0, 10.0, 30.0, 25.0, 20.0);

        // Brazil - Developing launch capability
        seedCountryScores("BRA", 25.0, 20.0, 5.0, 10.0, 25.0, 20.0, 30.0);

        // Iran - Indigenous launch capability
        seedCountryScores("IRN", 30.0, 25.0, 5.0, 5.0, 20.0, 15.0, 45.0);

        // North Korea - Basic launch capability
        seedCountryScores("PRK", 20.0, 15.0, 0.0, 0.0, 10.0, 10.0, 40.0);

        // Spain - ESA member
        seedCountryScores("ESP", 15.0, 20.0, 10.0, 15.0, 35.0, 20.0, 15.0);

        // Netherlands - ESA member, ESTEC host
        seedCountryScores("NLD", 10.0, 25.0, 15.0, 20.0, 35.0, 25.0, 15.0);

        // Belgium - ESA member
        seedCountryScores("BEL", 10.0, 15.0, 10.0, 15.0, 25.0, 15.0, 10.0);

        // Sweden - Strong satellite industry
        seedCountryScores("SWE", 15.0, 20.0, 10.0, 15.0, 40.0, 20.0, 20.0);

        // Norway - Andøya launch site
        seedCountryScores("NOR", 20.0, 15.0, 5.0, 10.0, 30.0, 25.0, 15.0);

        // Switzerland - ESA member
        seedCountryScores("CHE", 5.0, 15.0, 10.0, 15.0, 25.0, 15.0, 10.0);

        // Austria - ESA member
        seedCountryScores("AUT", 5.0, 10.0, 5.0, 10.0, 20.0, 10.0, 5.0);

        // Poland - ESA member
        seedCountryScores("POL", 5.0, 10.0, 5.0, 10.0, 20.0, 10.0, 10.0);
    }

    private void seedCountryScores(String isoCode, double launch, double propulsion, double human,
            double deepSpace, double satellite, double infrastructure, double independence) {
        Country country = getCountryMap().get(isoCode);
        if (country == null) {
            log.warn("Country not found for ISO code: {}", isoCode);
            return;
        }

        CapabilityCategory[] categories = CapabilityCategory.values();
        double[] scores = {launch, propulsion, human, deepSpace, satellite, infrastructure, independence};

        for (int i = 0; i < categories.length; i++) {
            CapabilityScore score = new CapabilityScore(country, categories[i], scores[i]);
            score.setRanking(calculateRanking(scores[i]));
            capabilityScoreRepository.save(score);
        }
    }

    private int calculateRanking(double score) {
        // Simplified ranking based on score thresholds
        if (score >= 90) return 1;
        if (score >= 80) return 2;
        if (score >= 70) return 3;
        if (score >= 60) return 4;
        if (score >= 50) return 5;
        if (score >= 40) return 6;
        if (score >= 30) return 7;
        if (score >= 20) return 8;
        if (score >= 10) return 9;
        return 10;
    }
}
