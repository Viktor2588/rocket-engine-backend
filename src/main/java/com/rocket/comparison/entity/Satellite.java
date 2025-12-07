package com.rocket.comparison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

/**
 * Represents a satellite or spacecraft in orbit.
 */
@Entity
@Table(name = "satellites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Satellite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Satellite name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Alternative name or series designation
     */
    @Column
    private String alternateName;

    /**
     * NORAD Catalog Number (unique identifier)
     */
    @Column
    private String noradId;

    /**
     * International designator (COSPAR ID)
     */
    @Column
    private String cosparId;

    /**
     * Country of origin/ownership
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    @JsonIgnoreProperties({"engines", "description"})
    private Country country;

    /**
     * Operating organization/company
     */
    @Column
    private String operator;

    /**
     * Manufacturer
     */
    @Column
    private String manufacturer;

    // ==================== Classification ====================

    /**
     * Type of satellite
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SatelliteType satelliteType;

    /**
     * Operational status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SatelliteStatus status;

    /**
     * Orbit type
     */
    @Enumerated(EnumType.STRING)
    @Column
    private OrbitType orbitType;

    // ==================== Timeline ====================

    /**
     * Launch date
     */
    @Column
    private LocalDate launchDate;

    /**
     * Date when satellite became operational
     */
    @Column
    private LocalDate operationalDate;

    /**
     * Decommission date (if applicable)
     */
    @Column
    private LocalDate decommissionDate;

    /**
     * Expected lifespan in years
     */
    @Column
    private Integer expectedLifespanYears;

    /**
     * Launch year for filtering
     */
    @Column
    private Integer launchYear;

    // ==================== Physical Specifications ====================

    /**
     * Satellite mass in kg
     */
    @Column
    private Double massKg;

    /**
     * Dry mass (without fuel) in kg
     */
    @Column
    private Double dryMassKg;

    /**
     * Power generation capacity in watts
     */
    @Column
    private Double powerWatts;

    /**
     * Satellite dimensions (e.g., "4.5m x 2.5m x 2.5m")
     */
    @Column
    private String dimensions;

    /**
     * Satellite bus/platform type
     */
    @Column
    private String busPlatform;

    // ==================== Orbital Parameters ====================

    /**
     * Orbital altitude in km (or apogee for elliptical)
     */
    @Column
    private Double altitudeKm;

    /**
     * Apogee (highest point) in km
     */
    @Column
    private Double apogeeKm;

    /**
     * Perigee (lowest point) in km
     */
    @Column
    private Double perigeeKm;

    /**
     * Orbital inclination in degrees
     */
    @Column
    private Double inclinationDeg;

    /**
     * Orbital period in minutes
     */
    @Column
    private Double periodMinutes;

    /**
     * Eccentricity of orbit
     */
    @Column
    private Double eccentricity;

    /**
     * Longitude for geostationary satellites
     */
    @Column
    private Double geoLongitude;

    // ==================== Constellation Info ====================

    /**
     * Constellation name (e.g., "Starlink", "GPS", "Galileo")
     */
    @Column
    private String constellation;

    /**
     * Whether satellite is part of a constellation
     */
    @Column
    private Boolean isPartOfConstellation;

    /**
     * Position in constellation (e.g., orbital plane, slot)
     */
    @Column
    private String constellationPosition;

    /**
     * Generation or block within constellation
     */
    @Column
    private String constellationGeneration;

    // ==================== Launch Info ====================

    /**
     * Launch vehicle used
     */
    @Column
    private String launchVehicle;

    /**
     * Launch site
     */
    @Column
    private String launchSite;

    /**
     * Mission that deployed this satellite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "launch_mission_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SpaceMission launchMission;

    // ==================== Capabilities ====================

    /**
     * Primary mission/purpose description
     */
    @Column(columnDefinition = "TEXT")
    private String purpose;

    /**
     * Payload instruments
     */
    @Column(columnDefinition = "TEXT")
    private String payloadDescription;

    /**
     * Communication frequencies/bands
     */
    @Column
    private String frequencyBands;

    /**
     * Coverage area or footprint
     */
    @Column
    private String coverageArea;

    /**
     * Data transmission rate
     */
    @Column
    private String dataRate;

    // ==================== Media & References ====================

    /**
     * URL to satellite image
     */
    @Column
    private String imageUrl;

    /**
     * Reference URL
     */
    @Column
    private String referenceUrl;

    // ==================== Lifecycle Methods ====================

    /**
     * Set derived fields automatically
     */
    @PrePersist
    @PreUpdate
    public void updateDerivedFields() {
        if (launchDate != null) {
            this.launchYear = launchDate.getYear();
        }

        // Set constellation flag
        if (constellation != null && !constellation.isEmpty()) {
            this.isPartOfConstellation = true;
        }

        // Calculate altitude from apogee/perigee if not set
        if (altitudeKm == null && apogeeKm != null && perigeeKm != null) {
            this.altitudeKm = (apogeeKm + perigeeKm) / 2;
        }
    }

    /**
     * Constructor for quick satellite creation
     */
    public Satellite(String name, Country country, SatelliteType type,
                     SatelliteStatus status, OrbitType orbitType, LocalDate launchDate) {
        this.name = name;
        this.country = country;
        this.satelliteType = type;
        this.status = status;
        this.orbitType = orbitType;
        this.launchDate = launchDate;
        updateDerivedFields();
    }
}
