package com.rocket.comparison.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Space mission destinations.
 */
public enum Destination {
    // Earth Orbits
    LEO("Low Earth Orbit", "earth_orbit", 400, "160-2000 km altitude"),
    MEO("Medium Earth Orbit", "earth_orbit", 20200, "2000-35786 km altitude"),
    GEO("Geostationary Orbit", "earth_orbit", 35786, "35786 km altitude, 0° inclination"),
    GTO("Geostationary Transfer Orbit", "earth_orbit", 35786, "Transfer orbit to GEO"),
    HEO("Highly Elliptical Orbit", "earth_orbit", 40000, "Highly elliptical orbit"),
    SSO("Sun-Synchronous Orbit", "earth_orbit", 800, "Polar orbit synchronized with Sun"),
    POLAR("Polar Orbit", "earth_orbit", 800, "Orbit passing over poles"),

    // Earth-Moon System
    LUNAR_ORBIT("Lunar Orbit", "lunar", 384400, "Orbit around the Moon"),
    LUNAR_SURFACE("Lunar Surface", "lunar", 384400, "Moon surface landing"),
    LUNAR_FAR_SIDE("Lunar Far Side", "lunar", 384400, "Far side of the Moon"),
    EARTH_MOON_L1("Earth-Moon L1", "lagrange", 326000, "L1 Lagrange point"),
    EARTH_MOON_L2("Earth-Moon L2", "lagrange", 449000, "L2 Lagrange point (behind Moon)"),

    // Sun-Earth Lagrange Points
    SUN_EARTH_L1("Sun-Earth L1", "lagrange", 1500000, "L1 point (solar observation)"),
    SUN_EARTH_L2("Sun-Earth L2", "lagrange", 1500000, "L2 point (space telescopes)"),

    // Mars
    MARS_ORBIT("Mars Orbit", "mars", 225000000, "Orbit around Mars"),
    MARS_SURFACE("Mars Surface", "mars", 225000000, "Mars surface landing"),
    PHOBOS("Phobos", "mars", 225000000, "Mars moon Phobos"),
    DEIMOS("Deimos", "mars", 225000000, "Mars moon Deimos"),

    // Venus
    VENUS_ORBIT("Venus Orbit", "venus", 41000000, "Orbit around Venus"),
    VENUS_SURFACE("Venus Surface", "venus", 41000000, "Venus surface landing"),
    VENUS_ATMOSPHERE("Venus Atmosphere", "venus", 41000000, "Venus atmospheric probe"),

    // Mercury
    MERCURY_ORBIT("Mercury Orbit", "mercury", 77000000, "Orbit around Mercury"),
    MERCURY_SURFACE("Mercury Surface", "mercury", 77000000, "Mercury surface landing"),

    // Jupiter System
    JUPITER_ORBIT("Jupiter Orbit", "jupiter", 628000000, "Orbit around Jupiter"),
    JUPITER_ATMOSPHERE("Jupiter Atmosphere", "jupiter", 628000000, "Jupiter atmospheric probe"),
    IO("Io", "jupiter", 628000000, "Jupiter moon Io"),
    EUROPA("Europa", "jupiter", 628000000, "Jupiter moon Europa"),
    GANYMEDE("Ganymede", "jupiter", 628000000, "Jupiter moon Ganymede"),
    CALLISTO("Callisto", "jupiter", 628000000, "Jupiter moon Callisto"),

    // Saturn System
    SATURN_ORBIT("Saturn Orbit", "saturn", 1400000000, "Orbit around Saturn"),
    SATURN_RINGS("Saturn Rings", "saturn", 1400000000, "Saturn ring system"),
    TITAN("Titan", "saturn", 1400000000, "Saturn moon Titan"),
    ENCELADUS("Enceladus", "saturn", 1400000000, "Saturn moon Enceladus"),

    // Outer Planets
    URANUS_ORBIT("Uranus Orbit", "outer", 2900000000L, "Orbit around Uranus"),
    NEPTUNE_ORBIT("Neptune Orbit", "outer", 4500000000L, "Orbit around Neptune"),
    PLUTO("Pluto", "outer", 5900000000L, "Dwarf planet Pluto"),

    // Small Bodies
    ASTEROID("Asteroid", "small_body", 0, "Asteroid target"),
    ASTEROID_BELT("Asteroid Belt", "small_body", 420000000, "Main asteroid belt"),
    NEAR_EARTH_ASTEROID("Near-Earth Asteroid", "small_body", 50000000, "NEA target"),
    COMET("Comet", "small_body", 0, "Comet target"),
    KUIPER_BELT("Kuiper Belt", "outer", 6000000000L, "Kuiper Belt object"),

    // Solar
    SUN("Sun", "solar", 150000000, "Solar observation/approach"),
    SOLAR_ORBIT("Solar Orbit", "solar", 150000000, "Heliocentric orbit"),
    INNER_HELIOSPHERE("Inner Heliosphere", "solar", 150000000, "Inner solar system"),

    // Deep Space
    OUTER_HELIOSPHERE("Outer Heliosphere", "deep_space", 18000000000L, "Edge of solar system"),
    INTERSTELLAR("Interstellar Space", "deep_space", 20000000000L, "Beyond the heliopause"),

    // Suborbital
    SUBORBITAL("Suborbital", "suborbital", 100, "Suborbital trajectory");

    private final String displayName;
    private final String category;
    private final long typicalDistanceKm;
    private final String description;

    Destination(String displayName, String category, long typicalDistanceKm, String description) {
        this.displayName = displayName;
        this.category = category;
        this.typicalDistanceKm = typicalDistanceKm;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public long getTypicalDistanceKm() {
        return typicalDistanceKm;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEarthOrbit() {
        return category.equals("earth_orbit");
    }

    public boolean isLunar() {
        return category.equals("lunar");
    }

    public boolean isMars() {
        return category.equals("mars");
    }

    public boolean isDeepSpace() {
        return typicalDistanceKm > 100000000; // Beyond Mars orbit
    }

    public boolean isPlanetary() {
        return category.equals("mars") || category.equals("venus") ||
               category.equals("mercury") || category.equals("jupiter") ||
               category.equals("saturn") || category.equals("outer");
    }

    public static List<Destination> getByCategory(String category) {
        return Arrays.stream(values())
                .filter(d -> d.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public static List<String> getAllCategories() {
        return Arrays.stream(values())
                .map(Destination::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}
