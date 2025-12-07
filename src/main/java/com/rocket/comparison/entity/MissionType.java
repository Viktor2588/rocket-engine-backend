package com.rocket.comparison.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Classification of space mission types.
 */
public enum MissionType {
    // Crewed Missions
    CREWED_ORBITAL("Crewed Orbital", "crewed", "Human spaceflight to Earth orbit"),
    CREWED_LUNAR("Crewed Lunar", "crewed", "Human spaceflight to the Moon"),
    CREWED_PLANETARY("Crewed Planetary", "crewed", "Human spaceflight beyond Earth-Moon system"),
    CREWED_STATION("Crewed Station", "crewed", "Human spaceflight to a space station"),

    // Cargo & Logistics
    CARGO_RESUPPLY("Cargo Resupply", "logistics", "Resupply mission to space station"),
    CREW_ROTATION("Crew Rotation", "logistics", "Crew transport to space station"),

    // Satellite Operations
    SATELLITE_DEPLOYMENT("Satellite Deployment", "satellite", "Deployment of satellites to orbit"),
    SATELLITE_SERVICING("Satellite Servicing", "satellite", "In-orbit satellite repair or upgrade"),
    CONSTELLATION_DEPLOYMENT("Constellation Deployment", "satellite", "Multi-satellite constellation deployment"),

    // Space Station
    SPACE_STATION_MODULE("Station Module", "station", "Space station module delivery"),
    SPACE_STATION_ASSEMBLY("Station Assembly", "station", "Space station assembly mission"),

    // Lunar Missions
    LUNAR_ORBITER("Lunar Orbiter", "lunar", "Spacecraft in lunar orbit"),
    LUNAR_LANDER("Lunar Lander", "lunar", "Robotic landing on the Moon"),
    LUNAR_ROVER("Lunar Rover", "lunar", "Mobile exploration of lunar surface"),
    LUNAR_SAMPLE_RETURN("Lunar Sample Return", "lunar", "Collection and return of lunar samples"),
    LUNAR_CREWED_ORBIT("Lunar Crewed Orbit", "lunar", "Human spaceflight to lunar orbit"),
    LUNAR_CREWED_LANDING("Lunar Crewed Landing", "lunar", "Human landing on the Moon"),

    // Mars Missions
    MARS_ORBITER("Mars Orbiter", "mars", "Spacecraft in Mars orbit"),
    MARS_LANDER("Mars Lander", "mars", "Robotic landing on Mars"),
    MARS_ROVER("Mars Rover", "mars", "Mobile exploration of Mars surface"),
    MARS_HELICOPTER("Mars Helicopter", "mars", "Aerial exploration of Mars"),
    MARS_SAMPLE_RETURN("Mars Sample Return", "mars", "Collection and return of Mars samples"),

    // Planetary Exploration
    VENUS_MISSION("Venus Mission", "planetary", "Mission to Venus"),
    MERCURY_MISSION("Mercury Mission", "planetary", "Mission to Mercury"),
    JUPITER_MISSION("Jupiter Mission", "planetary", "Mission to Jupiter system"),
    SATURN_MISSION("Saturn Mission", "planetary", "Mission to Saturn system"),
    OUTER_PLANETS("Outer Planets", "planetary", "Mission to Uranus, Neptune, or beyond"),

    // Small Bodies
    ASTEROID_FLYBY("Asteroid Flyby", "small_body", "Close approach to an asteroid"),
    ASTEROID_ORBITER("Asteroid Orbiter", "small_body", "Spacecraft orbiting an asteroid"),
    ASTEROID_LANDER("Asteroid Lander", "small_body", "Landing on an asteroid"),
    ASTEROID_SAMPLE_RETURN("Asteroid Sample Return", "small_body", "Sample collection from asteroid"),
    COMET_MISSION("Comet Mission", "small_body", "Mission to a comet"),

    // Solar & Deep Space
    SOLAR_OBSERVATION("Solar Observation", "solar", "Study of the Sun"),
    SOLAR_PROBE("Solar Probe", "solar", "Close approach to the Sun"),
    HELIOPHYSICS("Heliophysics", "solar", "Study of the heliosphere"),
    DEEP_SPACE_PROBE("Deep Space Probe", "deep_space", "Probe to outer solar system"),
    INTERSTELLAR_MISSION("Interstellar Mission", "deep_space", "Mission beyond the solar system"),

    // Earth Science & Technology
    EARTH_OBSERVATION("Earth Observation", "earth_science", "Earth monitoring satellite"),
    WEATHER_SATELLITE("Weather Satellite", "earth_science", "Meteorological observation"),
    CLIMATE_MONITORING("Climate Monitoring", "earth_science", "Climate research mission"),
    TECHNOLOGY_DEMO("Technology Demonstration", "technology", "Testing new space technologies"),
    COMMUNICATIONS("Communications", "infrastructure", "Communications satellite deployment"),
    NAVIGATION("Navigation", "infrastructure", "Navigation/GNSS satellite"),

    // Scientific
    SPACE_TELESCOPE("Space Telescope", "scientific", "Astronomical observatory in space"),
    ASTROPHYSICS("Astrophysics", "scientific", "Astrophysics research mission"),
    GRAVITATIONAL_WAVE("Gravitational Wave", "scientific", "Gravitational wave detection"),
    PARTICLE_PHYSICS("Particle Physics", "scientific", "Space-based particle research"),

    // Defense & Other
    MILITARY_RECONNAISSANCE("Military Reconnaissance", "military", "Military observation satellite"),
    MILITARY_COMMUNICATIONS("Military Communications", "military", "Military communications satellite"),
    MILITARY_NAVIGATION("Military Navigation", "military", "Military navigation satellite"),
    EXPERIMENTAL("Experimental", "other", "Experimental or test mission"),
    SUBORBITAL("Suborbital", "other", "Suborbital spaceflight");

    private final String displayName;
    private final String category;
    private final String description;

    MissionType(String displayName, String category, String description) {
        this.displayName = displayName;
        this.category = category;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCrewed() {
        return category.equals("crewed");
    }

    public boolean isLunar() {
        return category.equals("lunar");
    }

    public boolean isMars() {
        return category.equals("mars");
    }

    public boolean isPlanetary() {
        return category.equals("planetary") || category.equals("mars") || category.equals("lunar");
    }

    public boolean isDeepSpace() {
        return category.equals("deep_space") || category.equals("planetary") ||
               category.equals("small_body") || category.equals("solar");
    }

    public static List<MissionType> getByCategory(String category) {
        return Arrays.stream(values())
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public static List<String> getAllCategories() {
        return Arrays.stream(values())
                .map(MissionType::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}
