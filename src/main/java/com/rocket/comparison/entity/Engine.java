package com.rocket.comparison.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "engines", indexes = {
    @Index(name = "idx_engine_designer", columnList = "designer"),
    @Index(name = "idx_engine_propellant", columnList = "propellant"),
    @Index(name = "idx_engine_origin", columnList = "origin"),
    @Index(name = "idx_engine_country_id", columnList = "country_id"),
    @Index(name = "idx_engine_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Engine name is required")
    @Size(max = 100, message = "Engine name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;

    // Legacy field - kept for backwards compatibility with existing data
    @Column(nullable = true)
    private String origin; // e.g., "USA", "Russia", "China"

    // New relationship to Country entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = true)
    @JsonIgnoreProperties({"engines", "description", "hibernateLazyInitializer", "handler"})
    private Country country;

    @Column(nullable = true)
    private String designer; // e.g., "SpaceX", "NPO Energomash"

    @Column(nullable = true)
    private String vehicle; // e.g., "Falcon 9", "Starship"

    @Column(nullable = true)
    private String status; // e.g., "Active", "Development", "Retired"

    @Column(nullable = true)
    private String use; // e.g., "1st", "2nd", "Upper stage"

    @NotBlank(message = "Propellant type is required")
    @Column(nullable = false)
    private String propellant; // e.g., "RP-1 / LOX", "CH4 / LOX"

    @Column(nullable = true)
    private String powerCycle; // e.g., "Gas generator", "Full-flow staged combustion"

    @Min(value = 0, message = "ISP cannot be negative")
    @Column(nullable = true)
    private Double isp_s; // Specific Impulse in seconds

    @Min(value = 0, message = "Thrust cannot be negative")
    @Column(nullable = true)
    private Long thrustN; // Thrust in Newtons

    @Column(nullable = true)
    private Double chamberPressureBar; // Chamber pressure in bar

    @Min(value = 0, message = "Mass cannot be negative")
    @Column(nullable = true)
    private Double massKg; // Engine mass in kg

    @Column(nullable = true)
    private Double thrustToWeightRatio; // Stored T/W ratio

    @Column(nullable = true)
    private Double ofRatio; // Oxidizer-to-Fuel ratio

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Calculate thrust-to-weight ratio (T/W)
     * Formula: Thrust (N) / (Mass (kg) * 9.81 m/s²)
     * T/W = thrustN / (massKg * 9.81)
     *
     * @return Thrust-to-weight ratio, or null if mass is not available
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Transient
    public Double calculateThrustToWeightRatio() {
        if (massKg == null || massKg <= 0 || thrustN == null) {
            return null;
        }
        double weight = massKg * 9.81;
        return thrustN / weight;
    }
}
