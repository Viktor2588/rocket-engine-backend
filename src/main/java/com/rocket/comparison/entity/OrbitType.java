package com.rocket.comparison.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Classification of orbital types.
 */
public enum OrbitType {
    // Low Earth Orbit variants
    LEO("Low Earth Orbit", "leo", 400, 2000, "160-2000 km altitude"),
    VLEO("Very Low Earth Orbit", "leo", 200, 450, "Below 450 km altitude"),
    ISS_ORBIT("ISS Orbit", "leo", 400, 420, "~400 km, 51.6° inclination"),

    // Medium Earth Orbit
    MEO("Medium Earth Orbit", "meo", 2000, 35786, "2000-35786 km altitude"),
    NAVIGATION_MEO("Navigation MEO", "meo", 19000, 24000, "GPS/GLONASS altitude"),

    // Geostationary/Geosynchronous
    GEO("Geostationary Orbit", "geo", 35786, 35786, "35786 km, 0° inclination"),
    GSO("Geosynchronous Orbit", "geo", 35786, 35786, "35786 km, any inclination"),
    GTO("Geostationary Transfer Orbit", "geo", 200, 35786, "Transfer orbit to GEO"),
    SUPER_GEO("Super-Geostationary", "geo", 35786, 50000, "Above GEO altitude"),

    // High Earth Orbit
    HEO("Highly Elliptical Orbit", "heo", 500, 40000, "Highly elliptical, e.g., Molniya"),
    MOLNIYA("Molniya Orbit", "heo", 500, 40000, "12-hour highly elliptical"),
    TUNDRA("Tundra Orbit", "heo", 25000, 46000, "24-hour highly elliptical"),

    // Sun-Synchronous
    SSO("Sun-Synchronous Orbit", "polar", 600, 800, "Polar orbit synchronized with Sun"),
    POLAR("Polar Orbit", "polar", 600, 1000, "~90° inclination"),
    NEAR_POLAR("Near-Polar Orbit", "polar", 600, 1000, "High inclination orbit"),

    // Special orbits
    RETROGRADE("Retrograde Orbit", "special", 0, 0, "Orbiting opposite to Earth rotation"),
    GRAVEYARD("Graveyard Orbit", "special", 36000, 36500, "Disposal orbit above GEO"),
    HELIOCENTRIC("Heliocentric Orbit", "special", 0, 0, "Orbiting the Sun"),
    EARTH_ESCAPE("Earth Escape", "special", 0, 0, "Beyond Earth's gravity"),
    LUNAR("Lunar Orbit", "special", 0, 0, "Orbiting the Moon"),

    // Lagrange points
    L1("Sun-Earth L1", "lagrange", 1500000, 1500000, "L1 Lagrange point"),
    L2("Sun-Earth L2", "lagrange", 1500000, 1500000, "L2 Lagrange point"),
    EARTH_MOON_L1("Earth-Moon L1", "lagrange", 326000, 326000, "Earth-Moon L1"),
    EARTH_MOON_L2("Earth-Moon L2", "lagrange", 449000, 449000, "Earth-Moon L2"),

    // Suborbital
    SUBORBITAL("Suborbital", "suborbital", 0, 100, "Does not complete orbit"),

    // Unknown
    UNKNOWN("Unknown", "unknown", 0, 0, "Orbit type unknown");

    private final String displayName;
    private final String category;
    private final int minAltitudeKm;
    private final int maxAltitudeKm;
    private final String description;

    OrbitType(String displayName, String category, int minAltitudeKm, int maxAltitudeKm, String description) {
        this.displayName = displayName;
        this.category = category;
        this.minAltitudeKm = minAltitudeKm;
        this.maxAltitudeKm = maxAltitudeKm;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public int getMinAltitudeKm() {
        return minAltitudeKm;
    }

    public int getMaxAltitudeKm() {
        return maxAltitudeKm;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLowEarthOrbit() {
        return category.equals("leo");
    }

    public boolean isGeostationary() {
        return category.equals("geo");
    }

    public boolean isPolar() {
        return category.equals("polar");
    }

    public static List<OrbitType> getByCategory(String category) {
        return Arrays.stream(values())
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public static List<String> getAllCategories() {
        return Arrays.stream(values())
                .map(OrbitType::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}
