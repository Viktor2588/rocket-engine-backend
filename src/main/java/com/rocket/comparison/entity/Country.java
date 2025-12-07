package com.rocket.comparison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;                    // "United States"

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
    @Column
    private Integer totalLaunches;

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
