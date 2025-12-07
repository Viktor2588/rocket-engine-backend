package com.rocket.comparison.entity;

/**
 * Categories for measuring space program capabilities.
 * Each category contributes to the overall Space Capability Index (SCI).
 */
public enum CapabilityCategory {

    /**
     * Ability to reach space independently
     * Metrics: Active launch vehicles, payload capacity, launch frequency, success rate
     * Weight: 20%
     */
    LAUNCH_CAPABILITY("Launch Capability", 0.20,
        "Ability to independently launch payloads to orbit"),

    /**
     * Engine sophistication and technology level
     * Metrics: Engine count, propellant diversity, max ISP, advanced cycles, reusability
     * Weight: 15%
     */
    PROPULSION_TECHNOLOGY("Propulsion Technology", 0.15,
        "Rocket engine technology sophistication"),

    /**
     * Crewed mission capability
     * Metrics: Has capability, crewed missions, astronaut corps, space station, duration
     * Weight: 20%
     */
    HUMAN_SPACEFLIGHT("Human Spaceflight", 0.20,
        "Capability to send and support humans in space"),

    /**
     * Beyond Earth orbit capability
     * Metrics: Lunar missions, Mars missions, planetary probes, sample return
     * Weight: 15%
     */
    DEEP_SPACE_EXPLORATION("Deep Space Exploration", 0.15,
        "Capability to explore beyond Earth orbit"),

    /**
     * Space asset portfolio
     * Metrics: Active satellites, satellite diversity, GNSS, mega-constellations
     * Weight: 15%
     */
    SATELLITE_INFRASTRUCTURE("Satellite Infrastructure", 0.15,
        "Operational satellites and space-based assets"),

    /**
     * Ground infrastructure
     * Metrics: Launch sites, tracking stations, manufacturing capacity
     * Weight: 10%
     */
    SPACE_INFRASTRUCTURE("Space Infrastructure", 0.10,
        "Ground-based space infrastructure and facilities"),

    /**
     * Self-reliance in space technology
     * Metrics: Indigenous launch, engines, satellites, end-to-end capability
     * Weight: 5%
     */
    TECHNOLOGICAL_INDEPENDENCE("Technological Independence", 0.05,
        "Self-reliance in space technology development");

    private final String displayName;
    private final double weight;
    private final String description;

    CapabilityCategory(String displayName, double weight, String description) {
        this.displayName = displayName;
        this.weight = weight;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }
}
