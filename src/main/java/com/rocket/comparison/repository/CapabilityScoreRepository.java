package com.rocket.comparison.repository;

import com.rocket.comparison.entity.CapabilityCategory;
import com.rocket.comparison.entity.CapabilityScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapabilityScoreRepository extends JpaRepository<CapabilityScore, Long> {

    /**
     * Find all scores for a country
     */
    List<CapabilityScore> findByCountryId(Long countryId);

    /**
     * Find a specific category score for a country
     */
    Optional<CapabilityScore> findByCountryIdAndCategory(Long countryId, CapabilityCategory category);

    /**
     * Find all scores for a category (for ranking)
     */
    List<CapabilityScore> findByCategoryOrderByScoreDesc(CapabilityCategory category);

    /**
     * Delete all scores for a country (before recalculation)
     */
    void deleteByCountryId(Long countryId);

    /**
     * Get top N countries by category
     */
    @Query("SELECT cs FROM CapabilityScore cs WHERE cs.category = :category ORDER BY cs.score DESC")
    List<CapabilityScore> findTopByCategory(@Param("category") CapabilityCategory category);

    /**
     * Get average score for a category across all countries
     */
    @Query("SELECT AVG(cs.score) FROM CapabilityScore cs WHERE cs.category = :category")
    Double getAverageScoreByCategory(@Param("category") CapabilityCategory category);

    /**
     * Get global average across all categories
     */
    @Query("SELECT AVG(cs.score) FROM CapabilityScore cs")
    Double getGlobalAverageScore();

    /**
     * Count countries with scores in a category
     */
    @Query("SELECT COUNT(DISTINCT cs.country.id) FROM CapabilityScore cs WHERE cs.category = :category")
    Long countCountriesWithScoreInCategory(@Param("category") CapabilityCategory category);

    /**
     * Find countries above a threshold in a category
     */
    @Query("SELECT cs FROM CapabilityScore cs WHERE cs.category = :category AND cs.score >= :minScore ORDER BY cs.score DESC")
    List<CapabilityScore> findByMinScoreInCategory(@Param("category") CapabilityCategory category, @Param("minScore") Double minScore);
}
