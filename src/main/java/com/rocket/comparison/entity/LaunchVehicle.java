package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Represents a launch vehicle (rocket) used for space launches.
 */
@Entity
@Table(name = "launch_vehicles", indexes = {
    @Index(name = "idx_lv_country_id", columnList = "country_id"),
    @Index(name = "idx_lv_manufacturer", columnList = "manufacturer"),
    @Index(name = "idx_lv_status", columnList = "status"),
    @Index(name = "idx_lv_reusable", columnList = "reusable"),
    @Index(name = "idx_lv_human_rated", columnList = "humanRated")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaunchVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Launch vehicle name is required")
    @Size(max = 100, message = "Launch vehicle name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;

    @Column
    private String family;

    @Column
    private String variant;

    @Column
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @JsonIgnoreProperties({"engines", "description", "hibernateLazyInitializer", "handler"})
    private Country country;

    @Column
    private String manufacturer;

    // ==================== Physical Specs ====================

    @Column
    private Double heightMeters;

    @Column
    private Double diameterMeters;

    @Column
    private Double massKg;

    @Column
    private Integer stages;

    // ==================== Performance ====================

    @Column
    private Integer payloadToLeoKg;

    @Column
    private Integer payloadToGtoKg;

    @Column
    private Integer payloadToMoonKg;

    @Column
    private Integer payloadToMarsKg;

    // ==================== Propulsion ====================

    @Column
    private String firstStageEngines;

    @Column
    private Integer firstStageEngineCount;

    @Column
    private String secondStageEngines;

    @Column
    private Integer secondStageEngineCount;

    @Column
    private String propellant;

    @Column
    private Long thrustAtLiftoffKn;

    // ==================== Status & History ====================

    @Column
    private String status; // Active, Retired, In Development

    @Column
    private Integer firstFlightYear;

    @Column
    private Integer lastFlightYear;

    @Column
    private Integer totalLaunches;

    @Column
    private Integer successfulLaunches;

    @Column
    private Integer failedLaunches;

    @Column
    private Double successRate;

    // ==================== Capabilities ====================

    @Column
    private Boolean reusable;

    @Column
    private Boolean humanRated;

    @Column
    private Boolean active;

    // ==================== Economics ====================

    @Column(precision = 15, scale = 2)
    private BigDecimal costPerLaunchUsd;

    @Column(precision = 15, scale = 2)
    private BigDecimal costPerKgToLeoUsd;

    // ==================== Description ====================

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String imageUrl;

    @Column
    private String wikiUrl;

    // ==================== Lifecycle Methods ====================

    @PrePersist
    @PreUpdate
    public void updateDerivedFields() {
        if (totalLaunches != null && totalLaunches > 0 && successfulLaunches != null) {
            this.successRate = (successfulLaunches.doubleValue() / totalLaunches.doubleValue()) * 100.0;
        }
        if (totalLaunches != null && successfulLaunches != null) {
            this.failedLaunches = totalLaunches - successfulLaunches;
        }
        if (name != null && variant != null && fullName == null) {
            this.fullName = name + " " + variant;
        }
    }
}
