package com.rocket.comparison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a rocket launch site/spaceport.
 */
@Entity
@Table(name = "launch_sites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaunchSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the launch site
     */
    @Column(nullable = false)
    private String name;

    /**
     * Short name or abbreviation (e.g., "KSC", "VAFB")
     */
    @Column
    private String shortName;

    /**
     * Alternative names
     */
    @Column
    private String alternateName;

    /**
     * Country where launch site is located
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnoreProperties({"engines", "description"})
    private Country country;

    /**
     * Operating organization
     */
    @Column
    private String operator;

    // ==================== Location ====================

    /**
     * Latitude in degrees
     */
    @Column
    private Double latitude;

    /**
     * Longitude in degrees
     */
    @Column
    private Double longitude;

    /**
     * Elevation above sea level in meters
     */
    @Column
    private Double elevationMeters;

    /**
     * State/Province/Region
     */
    @Column
    private String region;

    /**
     * Nearest city
     */
    @Column
    private String nearestCity;

    /**
     * Time zone
     */
    @Column
    private String timeZone;

    // ==================== Status & History ====================

    /**
     * Operational status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LaunchSiteStatus status;

    /**
     * Year established
     */
    @Column
    private Integer establishedYear;

    /**
     * Year of first launch
     */
    @Column
    private Integer firstLaunchYear;

    /**
     * Year closed (if applicable)
     */
    @Column
    private Integer closedYear;

    // ==================== Launch Statistics ====================

    /**
     * Total number of launches
     */
    @Column
    private Integer totalLaunches;

    /**
     * Successful launches
     */
    @Column
    private Integer successfulLaunches;

    /**
     * Failed launches
     */
    @Column
    private Integer failedLaunches;

    /**
     * Success rate percentage
     */
    @Column
    private Double successRate;

    /**
     * Launches in current year
     */
    @Column
    private Integer launchesThisYear;

    // ==================== Facilities ====================

    /**
     * Number of launch pads
     */
    @Column
    private Integer numberOfLaunchPads;

    /**
     * Number of active launch pads
     */
    @Column
    private Integer activeLaunchPads;

    /**
     * Whether site is human-rated for crewed launches
     */
    @Column
    private Boolean humanRatedCapable;

    /**
     * Maximum payload capacity to LEO (kg)
     */
    @Column
    private Integer maxPayloadCapacityKg;

    /**
     * Types of launch vehicles supported
     */
    @Column(columnDefinition = "TEXT")
    private String supportedVehicles;

    // ==================== Orbital Capabilities ====================

    /**
     * Can reach Low Earth Orbit
     */
    @Column
    private Boolean supportsLeo;

    /**
     * Can reach Medium Earth Orbit
     */
    @Column
    private Boolean supportsMeo;

    /**
     * Can reach Geostationary Orbit
     */
    @Column
    private Boolean supportsGeo;

    /**
     * Can launch to polar orbits
     */
    @Column
    private Boolean supportsPolar;

    /**
     * Can launch to sun-synchronous orbits
     */
    @Column
    private Boolean supportsSso;

    /**
     * Can launch interplanetary missions
     */
    @Column
    private Boolean supportsInterplanetary;

    /**
     * Minimum achievable inclination (degrees)
     */
    @Column
    private Double minInclinationDeg;

    /**
     * Maximum achievable inclination (degrees)
     */
    @Column
    private Double maxInclinationDeg;

    // ==================== Infrastructure ====================

    /**
     * Site area in square kilometers
     */
    @Column
    private Double areaSqKm;

    /**
     * Has on-site rocket manufacturing
     */
    @Column
    private Boolean hasManufacturing;

    /**
     * Has vehicle integration facilities
     */
    @Column
    private Boolean hasIntegrationFacility;

    /**
     * Has propellant production
     */
    @Column
    private Boolean hasPropellantProduction;

    /**
     * Has landing facilities for reusable rockets
     */
    @Column
    private Boolean hasLandingFacilities;

    /**
     * Has tracking/telemetry stations
     */
    @Column
    private Boolean hasTrackingStations;

    // ==================== Description ====================

    /**
     * Description of the launch site
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Historical significance
     */
    @Column(columnDefinition = "TEXT")
    private String historicalSignificance;

    /**
     * Notable launches from this site
     */
    @Column(columnDefinition = "TEXT")
    private String notableLaunches;

    // ==================== Media & References ====================

    /**
     * URL to site image
     */
    @Column
    private String imageUrl;

    /**
     * Reference URL
     */
    @Column
    private String referenceUrl;

    /**
     * Website URL
     */
    @Column
    private String websiteUrl;

    // ==================== Lifecycle Methods ====================

    /**
     * Update derived fields
     */
    @PrePersist
    @PreUpdate
    public void updateDerivedFields() {
        // Calculate success rate
        if (totalLaunches != null && totalLaunches > 0 && successfulLaunches != null) {
            this.successRate = (successfulLaunches.doubleValue() / totalLaunches.doubleValue()) * 100.0;
        }

        // Calculate failed launches
        if (totalLaunches != null && successfulLaunches != null) {
            this.failedLaunches = totalLaunches - successfulLaunches;
        }
    }

    /**
     * Constructor for quick launch site creation
     */
    public LaunchSite(String name, String shortName, Country country,
                      LaunchSiteStatus status, Double latitude, Double longitude) {
        this.name = name;
        this.shortName = shortName;
        this.country = country;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
