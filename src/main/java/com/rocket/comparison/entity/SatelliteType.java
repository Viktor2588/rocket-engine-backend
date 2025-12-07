package com.rocket.comparison.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Classification of satellite types by primary function.
 */
public enum SatelliteType {
    // Communications
    COMMUNICATION("Communication", "communications", "General communications satellite"),
    BROADCAST("Broadcast", "communications", "Television and radio broadcast satellite"),
    MOBILE_COMMUNICATION("Mobile Communication", "communications", "Mobile phone and IoT communication"),
    DATA_RELAY("Data Relay", "communications", "Satellite-to-satellite data relay"),

    // Navigation
    NAVIGATION("Navigation", "navigation", "Global navigation satellite system (GNSS)"),
    GPS("GPS", "navigation", "US Global Positioning System"),
    GLONASS("GLONASS", "navigation", "Russian navigation system"),
    GALILEO("Galileo", "navigation", "European navigation system"),
    BEIDOU("BeiDou", "navigation", "Chinese navigation system"),
    REGIONAL_NAVIGATION("Regional Navigation", "navigation", "Regional navigation augmentation"),

    // Earth Observation
    EARTH_OBSERVATION("Earth Observation", "earth_observation", "General Earth observation"),
    OPTICAL_IMAGING("Optical Imaging", "earth_observation", "High-resolution optical imaging"),
    RADAR_IMAGING("Radar Imaging", "earth_observation", "Synthetic aperture radar (SAR) imaging"),
    MULTISPECTRAL("Multispectral", "earth_observation", "Multispectral imaging for agriculture/environment"),
    HYPERSPECTRAL("Hyperspectral", "earth_observation", "Hyperspectral imaging for detailed analysis"),

    // Weather & Climate
    WEATHER("Weather", "weather", "Meteorological observation"),
    WEATHER_GEO("Geostationary Weather", "weather", "Geostationary weather satellite"),
    WEATHER_POLAR("Polar Weather", "weather", "Polar-orbiting weather satellite"),
    CLIMATE_MONITORING("Climate Monitoring", "weather", "Long-term climate observation"),
    OCEAN_MONITORING("Ocean Monitoring", "weather", "Ocean and maritime monitoring"),

    // Scientific
    SCIENTIFIC("Scientific", "scientific", "General scientific research"),
    ASTRONOMY("Astronomy", "scientific", "Space telescope and astronomical observation"),
    SOLAR_OBSERVATION("Solar Observation", "scientific", "Sun and heliosphere observation"),
    SPACE_PHYSICS("Space Physics", "scientific", "Magnetosphere and space environment"),
    PLANETARY_SCIENCE("Planetary Science", "scientific", "Planetary exploration"),
    GRAVITATIONAL_WAVE("Gravitational Wave", "scientific", "Gravitational wave detection"),

    // Technology
    TECHNOLOGY_DEMO("Technology Demonstration", "technology", "Testing new space technologies"),
    EXPERIMENTAL("Experimental", "technology", "Experimental satellite platform"),
    CUBESAT("CubeSat", "technology", "Small standardized satellite"),
    SMALLSAT("SmallSat", "technology", "Small satellite under 500kg"),

    // Military & Intelligence
    MILITARY_RECONNAISSANCE("Military Reconnaissance", "military", "Military imaging and surveillance"),
    MILITARY_COMMUNICATION("Military Communication", "military", "Secure military communications"),
    EARLY_WARNING("Early Warning", "military", "Missile launch detection"),
    SIGNALS_INTELLIGENCE("Signals Intelligence", "military", "Electronic signals collection"),
    MILITARY_NAVIGATION("Military Navigation", "military", "Military navigation augmentation"),

    // Space Infrastructure
    SPACE_STATION("Space Station", "infrastructure", "Orbital space station"),
    SPACE_STATION_MODULE("Station Module", "infrastructure", "Space station component"),
    CREWED_SPACECRAFT("Crewed Spacecraft", "infrastructure", "Human-occupied vehicle"),
    CARGO_SPACECRAFT("Cargo Spacecraft", "infrastructure", "Cargo resupply vehicle"),
    SPACE_TUG("Space Tug", "infrastructure", "Orbital transfer vehicle"),
    DEBRIS_REMOVAL("Debris Removal", "infrastructure", "Space debris mitigation"),

    // Commercial
    INTERNET_CONSTELLATION("Internet Constellation", "commercial", "Broadband internet mega-constellation"),
    IOT_CONSTELLATION("IoT Constellation", "commercial", "Internet of Things connectivity"),
    COMMERCIAL_IMAGING("Commercial Imaging", "commercial", "Commercial Earth imaging service"),
    COMMERCIAL_COMMUNICATION("Commercial Communication", "commercial", "Commercial telecom service"),

    // Other
    AMATEUR("Amateur", "other", "Amateur radio satellite"),
    EDUCATIONAL("Educational", "other", "University/educational satellite"),
    UNKNOWN("Unknown", "other", "Unknown or classified type");

    private final String displayName;
    private final String category;
    private final String description;

    SatelliteType(String displayName, String category, String description) {
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

    public boolean isMilitary() {
        return category.equals("military");
    }

    public boolean isCommercial() {
        return category.equals("commercial") || category.equals("communications");
    }

    public boolean isScientific() {
        return category.equals("scientific");
    }

    public static List<SatelliteType> getByCategory(String category) {
        return Arrays.stream(values())
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public static List<String> getAllCategories() {
        return Arrays.stream(values())
                .map(SatelliteType::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}
