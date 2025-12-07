package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a space mission with comprehensive tracking of mission details,
 * crew information, objectives, and outcomes.
 */
@Entity
@Table(name = "space_missions", indexes = {
    @Index(name = "idx_mission_country_id", columnList = "country_id"),
    @Index(name = "idx_mission_launch_date", columnList = "launchDate"),
    @Index(name = "idx_mission_launch_year", columnList = "launchYear"),
    @Index(name = "idx_mission_status", columnList = "status"),
    @Index(name = "idx_mission_type", columnList = "missionType"),
    @Index(name = "idx_mission_destination", columnList = "destination")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mission name (e.g., "Apollo 11", "Voyager 1")
     */
    @NotBlank(message = "Mission name is required")
    @Size(max = 200, message = "Mission name cannot exceed 200 characters")
    @Column(nullable = false)
    private String name;

    /**
     * Official mission designation (e.g., "AS-506")
     */
    @Column
    private String missionDesignation;

    /**
     * Alternative or international name
     */
    @Column
    private String alternateName;

    /**
     * Country that conducted the mission
     */
    @NotNull(message = "Country is required for mission")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnoreProperties({"engines", "description", "hibernateLazyInitializer", "handler"})
    private Country country;

    /**
     * Organization operating the mission (NASA, SpaceX, etc.)
     */
    @Column
    private String operator;

    /**
     * Launch vehicle used (optional - can be linked later)
     */
    @Column
    private String launchVehicleName;

    // ==================== Timeline ====================

    /**
     * Launch date
     */
    @Column
    private LocalDate launchDate;

    /**
     * Mission end date
     */
    @Column
    private LocalDate endDate;

    /**
     * Mission duration in days
     */
    @Column
    private Integer durationDays;

    /**
     * Year of launch for easy filtering
     */
    @Column
    private Integer launchYear;

    /**
     * Decade of launch for grouping
     */
    @Column
    private Integer launchDecade;

    // ==================== Classification ====================

    /**
     * Current mission status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    /**
     * Type of mission
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType missionType;

    /**
     * Mission destination
     */
    @Enumerated(EnumType.STRING)
    @Column
    private Destination destination;

    /**
     * Mission category for grouping
     */
    @Column
    private String missionCategory;

    // ==================== Crew Information ====================

    /**
     * Whether this is a crewed mission
     */
    @Column
    private Boolean crewed;

    /**
     * Number of crew members
     */
    @Column
    private Integer crewSize;

    /**
     * Names of crew members (comma-separated)
     */
    @Column(columnDefinition = "TEXT")
    private String crewNames;

    /**
     * Commander name
     */
    @Column
    private String commander;

    // ==================== Launch Details ====================

    /**
     * Launch site name
     */
    @Column
    private String launchSite;

    /**
     * Launch site country (for multi-national launches)
     */
    @Column
    private String launchSiteCountry;

    /**
     * Total mission mass in kg
     */
    @Column
    private Double missionMassKg;

    /**
     * Payload mass in kg
     */
    @Column
    private Double payloadMassKg;

    // ==================== Mission Achievements ====================

    /**
     * Whether this was a historic first achievement
     */
    @Column
    private Boolean isHistoricFirst;

    /**
     * Type of historic first if applicable
     */
    @Column
    private String historicFirstType;

    /**
     * Link to SpaceMilestone if this mission achieved one
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SpaceMilestone milestone;

    // ==================== Mission Content ====================

    /**
     * Brief description of the mission
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Primary mission objectives
     */
    @Column(columnDefinition = "TEXT")
    private String objectives;

    /**
     * Mission outcomes and achievements
     */
    @Column(columnDefinition = "TEXT")
    private String outcomes;

    /**
     * Notable events during mission
     */
    @Column(columnDefinition = "TEXT")
    private String notableEvents;

    // ==================== Scientific Data ====================

    /**
     * Scientific instruments aboard
     */
    @Column(columnDefinition = "TEXT")
    private String instruments;

    /**
     * Key scientific discoveries
     */
    @Column(columnDefinition = "TEXT")
    private String discoveries;

    /**
     * Sample mass returned (if applicable), in kg
     */
    @Column
    private Double sampleReturnMassKg;

    // ==================== Economics ====================

    /**
     * Estimated mission cost in USD
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal costUsd;

    /**
     * Year the cost is adjusted to
     */
    @Column
    private Integer costYear;

    // ==================== Media ====================

    /**
     * URL to mission image
     */
    @Column
    private String imageUrl;

    /**
     * URL to mission patch/logo
     */
    @Column
    private String patchUrl;

    /**
     * Reference URL (Wikipedia, NASA, etc.)
     */
    @Column
    private String referenceUrl;

    // ==================== Statistics ====================

    /**
     * Distance traveled in km
     */
    @Column
    private Long distanceTraveledKm;

    /**
     * Maximum distance from Earth in km
     */
    @Column
    private Long maxDistanceFromEarthKm;

    /**
     * Number of orbits completed (for orbital missions)
     */
    @Column
    private Integer orbitsCompleted;

    /**
     * EVA (spacewalk) count
     */
    @Column
    private Integer evaCount;

    /**
     * Total EVA duration in hours
     */
    @Column
    private Double evaDurationHours;

    // ==================== Lifecycle Methods ====================

    /**
     * Set derived fields automatically
     */
    @PrePersist
    @PreUpdate
    public void updateDerivedFields() {
        if (launchDate != null) {
            this.launchYear = launchDate.getYear();
            this.launchDecade = (launchDate.getYear() / 10) * 10;

            // Calculate duration if end date is set
            if (endDate != null) {
                this.durationDays = (int) java.time.temporal.ChronoUnit.DAYS.between(launchDate, endDate);
            }
        }

        // Set mission category from mission type
        if (missionType != null) {
            this.missionCategory = missionType.getCategory();
        }

        // Set crewed flag based on mission type
        if (missionType != null && crewed == null) {
            this.crewed = missionType.isCrewed();
        }
    }

    /**
     * Constructor for quick mission creation
     */
    public SpaceMission(String name, Country country, MissionType missionType,
                        Destination destination, MissionStatus status, LocalDate launchDate) {
        this.name = name;
        this.country = country;
        this.missionType = missionType;
        this.destination = destination;
        this.status = status;
        this.launchDate = launchDate;
        updateDerivedFields();
    }
}
