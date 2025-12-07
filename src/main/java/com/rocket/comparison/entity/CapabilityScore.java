package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Stores capability scores for each country by category.
 * Enables historical tracking and detailed breakdowns.
 */
@Entity
@Table(name = "capability_scores",
       uniqueConstraints = @UniqueConstraint(columnNames = {"country_id", "category"}),
       indexes = {
           @Index(name = "idx_capability_country_id", columnList = "country_id"),
           @Index(name = "idx_capability_category", columnList = "category"),
           @Index(name = "idx_capability_score", columnList = "score"),
           @Index(name = "idx_capability_ranking", columnList = "ranking")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Country is required for capability score")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnoreProperties({"engines", "description", "hibernateLazyInitializer", "handler"})
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CapabilityCategory category;

    /**
     * Score for this category (0-100)
     */
    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score cannot be negative")
    @Max(value = 100, message = "Score cannot exceed 100")
    @Column(nullable = false)
    private Double score;

    /**
     * Global rank in this category (1 = best)
     */
    @Min(value = 1, message = "Ranking must be at least 1")
    @Column
    private Integer ranking;

    /**
     * When this score was calculated
     */
    @Column
    private LocalDateTime calculatedAt;

    /**
     * Algorithm version used for calculation
     */
    @Column
    private String calculationVersion;

    /**
     * JSON breakdown of score components
     * e.g., {"vehicleScore": 25, "payloadScore": 20, "frequencyScore": 15, "successScore": 22}
     */
    @Column(columnDefinition = "TEXT")
    private String scoreBreakdown;

    /**
     * Constructor for quick score creation
     */
    public CapabilityScore(Country country, CapabilityCategory category, Double score) {
        this.country = country;
        this.category = category;
        this.score = score;
        this.calculatedAt = LocalDateTime.now();
        this.calculationVersion = "1.0";
    }

    /**
     * Get the weighted contribution of this score to overall SCI
     */
    public Double getWeightedScore() {
        return score * category.getWeight();
    }
}
