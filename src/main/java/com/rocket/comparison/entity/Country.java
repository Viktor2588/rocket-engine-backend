package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries", indexes = {
    @Index(name = "idx_country_region", columnList = "region"),
    @Index(name = "idx_country_iso_code", columnList = "isoCode"),
    @Index(name = "idx_country_capability_score", columnList = "overallCapabilityScore"),
    @Index(name = "idx_country_human_spaceflight", columnList = "humanSpaceflightCapable"),
    @Index(name = "idx_country_launch_capable", columnList = "independentLaunchCapable")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String name;                    // "United States"

    @NotBlank(message = "ISO code is required")
    @Size(min = 2, max = 3, message = "ISO code must be 2-3 characters")
    @Column(nullable = false, unique = true, length = 3)
    private String isoCode;                 // "USA"

    @Column
    private String flagUrl;

    // Space Agency Info
    @Column
    private String spaceAgencyName;         // "NASA"

    @Column
    private String spaceAgencyAcronym;      // "NASA"

    @Column
    private Integer spaceAgencyFounded;     // 1958

    // Budget & Resources
    @Column(precision = 15, scale = 2)
    private BigDecimal annualBudgetUsd;

    @Column
    private Double budgetAsPercentOfGdp;

    // Launch Statistics
    @Min(value = 0, message = "Total launches cannot be negative")
    @Column
    private Integer totalLaunches;

    @Min(value = 0, message = "Successful launches cannot be negative")
    @Column
    private Integer successfulLaunches;

    @Column
    private Double launchSuccessRate;

    // Workforce
    @Column
    private Integer activeAstronauts;

    @Column
    private Integer totalSpaceAgencyEmployees;

    // Capability Flags
    @Column
    private Boolean humanSpaceflightCapable;

    @Column
    private Boolean independentLaunchCapable;

    @Column
    private Boolean reusableRocketCapable;

    @Column
    private Boolean deepSpaceCapable;

    @Column
    private Boolean spaceStationCapable;

    @Column
    private Boolean lunarLandingCapable;

    @Column
    private Boolean marsLandingCapable;

    // Calculated Score (0-100)
    @Column
    private Double overallCapabilityScore;

    // Region for grouping
    @Column
    private String region;                  // "North America", "Europe", "Asia"

    // Relationships - engines in this country
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Engine> engines = new ArrayList<>();

    // Description
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Calculate launch success rate based on total and successful launches
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    public Double calculateLaunchSuccessRate() {
        if (totalLaunches == null || totalLaunches == 0) {
            return null;
        }
        if (successfulLaunches == null) {
            return 0.0;
        }
        return (successfulLaunches.doubleValue() / totalLaunches.doubleValue()) * 100;
    }

    /**
     * Get the number of engines from this country
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    public Integer getEngineCount() {
        return engines != null ? engines.size() : 0;
    }
}
