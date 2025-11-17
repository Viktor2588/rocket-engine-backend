package com.rocket.comparison.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "engines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String origin; // e.g., "USA", "Russia", "China"

    @Column(nullable = true)
    private String designer; // e.g., "SpaceX", "NPO Energomash"

    @Column(nullable = true)
    private String vehicle; // e.g., "Falcon 9", "Starship"

    @Column(nullable = true)
    private String status; // e.g., "Active", "Development", "Retired"

    @Column(nullable = true)
    private String use; // e.g., "1st", "2nd", "Upper stage"

    @Column(nullable = false)
    private String propellant; // e.g., "RP-1 / LOX", "CH4 / LOX"

    @Column(nullable = true)
    private String powerCycle; // e.g., "Gas generator", "Full-flow staged combustion"

    @Column(nullable = true)
    private Double isp_s; // Specific Impulse in seconds

    @Column(nullable = true)
    private Long thrustN; // Thrust in Newtons

    @Column(nullable = true)
    private Double chamberPressureBar; // Chamber pressure in bar

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
