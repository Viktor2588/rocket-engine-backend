package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

/**
 * Represents a significant achievement in space exploration.
 * Tracks who achieved what milestone and when, enabling timeline visualizations.
 */
@Entity
@Table(name = "space_milestones", indexes = {
    @Index(name = "idx_milestone_country_id", columnList = "country_id"),
    @Index(name = "idx_milestone_type", columnList = "milestoneType"),
    @Index(name = "idx_milestone_date", columnList = "dateAchieved"),
    @Index(name = "idx_milestone_year", columnList = "year"),
    @Index(name = "idx_milestone_decade", columnList = "decade"),
    @Index(name = "idx_milestone_global_first", columnList = "isGlobalFirst")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Country that achieved this milestone
     */
    @NotNull(message = "Country is required for milestone")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnoreProperties({"engines", "description", "hibernateLazyInitializer", "handler"})
    private Country country;

    /**
     * Type of milestone achieved
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneType milestoneType;

    /**
     * Date the milestone was achieved
     */
    @NotNull(message = "Date achieved is required")
    @Column(nullable = false)
    private LocalDate dateAchieved;

    /**
     * Global rank for this milestone (1 = first to achieve, 2 = second, etc.)
     */
    @Min(value = 1, message = "Global rank must be at least 1")
    @Column
    private Integer globalRank;

    /**
     * Custom title for this achievement
     */
    @NotBlank(message = "Milestone title is required")
    @Size(max = 200, message = "Milestone title cannot exceed 200 characters")
    @Column(nullable = false)
    private String title;

    /**
     * Name of mission, spacecraft, or person associated with milestone
     */
    @Column
    private String achievedBy;

    /**
     * Name of the specific mission
     */
    @Column
    private String missionName;

    /**
     * Detailed description of the achievement
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * URL to an image representing this milestone
     */
    @Column
    private String imageUrl;

    /**
     * URL to Wikipedia or other reference
     */
    @Column
    private String referenceUrl;

    /**
     * Year extracted for easy filtering
     */
    @Column
    private Integer year;

    /**
     * Decade for grouping (1950, 1960, 1970, etc.)
     */
    @Column
    private Integer decade;

    /**
     * Era classification (space_race, shuttle_era, commercial_era, etc.)
     */
    @Column
    private String era;

    /**
     * Whether this was a "first" achievement globally
     */
    @Column
    private Boolean isGlobalFirst;

    /**
     * Set year and decade automatically from dateAchieved
     */
    @PrePersist
    @PreUpdate
    public void updateDerivedFields() {
        if (dateAchieved != null) {
            this.year = dateAchieved.getYear();
            this.decade = (dateAchieved.getYear() / 10) * 10;

            // Determine era
            if (year < 1970) {
                this.era = "space_race";
            } else if (year < 1990) {
                this.era = "shuttle_era";
            } else if (year < 2010) {
                this.era = "iss_era";
            } else {
                this.era = "commercial_era";
            }
        }

        // Set isGlobalFirst based on rank
        if (globalRank != null) {
            this.isGlobalFirst = globalRank == 1;
        }
    }

    /**
     * Constructor for quick milestone creation
     */
    public SpaceMilestone(Country country, MilestoneType type, LocalDate date,
                          Integer rank, String title, String achievedBy) {
        this.country = country;
        this.milestoneType = type;
        this.dateAchieved = date;
        this.globalRank = rank;
        this.title = title;
        this.achievedBy = achievedBy;
        this.isGlobalFirst = rank != null && rank == 1;
        updateDerivedFields();
    }
}
