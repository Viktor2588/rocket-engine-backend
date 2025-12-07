package com.rocket.comparison.entity;

/**
 * Types of space exploration milestones.
 * Categorized by domain for easy filtering and display.
 */
public enum MilestoneType {

    // ==================== Orbital Firsts ====================
    FIRST_SATELLITE("First Satellite", "orbital", "First artificial satellite in orbit"),
    FIRST_ANIMAL_IN_SPACE("First Animal in Space", "orbital", "First animal to reach space"),
    FIRST_HUMAN_IN_SPACE("First Human in Space", "orbital", "First human to reach space"),
    FIRST_WOMAN_IN_SPACE("First Woman in Space", "orbital", "First woman to reach space"),
    FIRST_SPACEWALK("First Spacewalk", "orbital", "First extravehicular activity (EVA)"),
    FIRST_ORBITAL_RENDEZVOUS("First Orbital Rendezvous", "orbital", "First spacecraft rendezvous in orbit"),
    FIRST_DOCKING("First Docking", "orbital", "First spacecraft docking"),
    FIRST_SPACE_STATION("First Space Station", "orbital", "First operational space station"),
    FIRST_MODULAR_SPACE_STATION("First Modular Space Station", "orbital", "First multi-module space station"),
    FIRST_COMMERCIAL_CREW("First Commercial Crew", "orbital", "First commercial crew mission"),
    FIRST_PRIVATE_ASTRONAUT("First Private Astronaut", "orbital", "First privately funded astronaut"),

    // ==================== Lunar Firsts ====================
    FIRST_LUNAR_FLYBY("First Lunar Flyby", "lunar", "First spacecraft to fly by the Moon"),
    FIRST_LUNAR_IMPACT("First Lunar Impact", "lunar", "First spacecraft to impact the Moon"),
    FIRST_LUNAR_ORBIT("First Lunar Orbit", "lunar", "First spacecraft to orbit the Moon"),
    FIRST_LUNAR_SOFT_LANDING("First Lunar Soft Landing", "lunar", "First soft landing on the Moon"),
    FIRST_LUNAR_ROVER("First Lunar Rover", "lunar", "First rover on the Moon"),
    FIRST_HUMAN_LUNAR_ORBIT("First Human Lunar Orbit", "lunar", "First humans to orbit the Moon"),
    FIRST_HUMAN_LUNAR_LANDING("First Human Lunar Landing", "lunar", "First humans to walk on the Moon"),
    FIRST_LUNAR_SAMPLE_RETURN("First Lunar Sample Return", "lunar", "First samples returned from the Moon"),
    FIRST_LUNAR_FAR_SIDE_LANDING("First Lunar Far Side Landing", "lunar", "First landing on the lunar far side"),
    FIRST_LUNAR_SOUTH_POLE_LANDING("First Lunar South Pole Landing", "lunar", "First landing near lunar south pole"),

    // ==================== Mars Firsts ====================
    FIRST_MARS_FLYBY("First Mars Flyby", "mars", "First spacecraft to fly by Mars"),
    FIRST_MARS_ORBIT("First Mars Orbit", "mars", "First spacecraft to orbit Mars"),
    FIRST_MARS_LANDING("First Mars Landing", "mars", "First spacecraft to land on Mars"),
    FIRST_MARS_ROVER("First Mars Rover", "mars", "First rover on Mars"),
    FIRST_MARS_HELICOPTER("First Mars Helicopter", "mars", "First powered flight on Mars"),
    FIRST_MARS_SAMPLE_COLLECTION("First Mars Sample Collection", "mars", "First samples collected for return"),

    // ==================== Planetary Firsts ====================
    FIRST_VENUS_FLYBY("First Venus Flyby", "planetary", "First spacecraft to fly by Venus"),
    FIRST_VENUS_LANDING("First Venus Landing", "planetary", "First spacecraft to land on Venus"),
    FIRST_VENUS_ATMOSPHERE_ENTRY("First Venus Atmosphere Entry", "planetary", "First entry into Venus atmosphere"),
    FIRST_MERCURY_FLYBY("First Mercury Flyby", "planetary", "First spacecraft to fly by Mercury"),
    FIRST_MERCURY_ORBIT("First Mercury Orbit", "planetary", "First spacecraft to orbit Mercury"),
    FIRST_JUPITER_FLYBY("First Jupiter Flyby", "planetary", "First spacecraft to fly by Jupiter"),
    FIRST_JUPITER_ORBIT("First Jupiter Orbit", "planetary", "First spacecraft to orbit Jupiter"),
    FIRST_SATURN_FLYBY("First Saturn Flyby", "planetary", "First spacecraft to fly by Saturn"),
    FIRST_SATURN_ORBIT("First Saturn Orbit", "planetary", "First spacecraft to orbit Saturn"),
    FIRST_TITAN_LANDING("First Titan Landing", "planetary", "First landing on Saturn's moon Titan"),
    FIRST_URANUS_FLYBY("First Uranus Flyby", "planetary", "First spacecraft to fly by Uranus"),
    FIRST_NEPTUNE_FLYBY("First Neptune Flyby", "planetary", "First spacecraft to fly by Neptune"),
    FIRST_PLUTO_FLYBY("First Pluto Flyby", "planetary", "First spacecraft to fly by Pluto"),

    // ==================== Small Body Firsts ====================
    FIRST_ASTEROID_FLYBY("First Asteroid Flyby", "small_body", "First spacecraft to fly by an asteroid"),
    FIRST_ASTEROID_ORBIT("First Asteroid Orbit", "small_body", "First spacecraft to orbit an asteroid"),
    FIRST_ASTEROID_LANDING("First Asteroid Landing", "small_body", "First spacecraft to land on an asteroid"),
    FIRST_ASTEROID_SAMPLE_RETURN("First Asteroid Sample Return", "small_body", "First samples returned from an asteroid"),
    FIRST_COMET_FLYBY("First Comet Flyby", "small_body", "First spacecraft to fly by a comet"),
    FIRST_COMET_ORBIT("First Comet Orbit", "small_body", "First spacecraft to orbit a comet"),
    FIRST_COMET_LANDING("First Comet Landing", "small_body", "First spacecraft to land on a comet"),

    // ==================== Technology Firsts ====================
    FIRST_REUSABLE_ROCKET("First Reusable Rocket", "technology", "First orbital-class reusable rocket"),
    FIRST_PROPULSIVE_LANDING("First Propulsive Landing", "technology", "First rocket propulsive landing"),
    FIRST_BOOSTER_CATCH("First Booster Catch", "technology", "First rocket booster caught mid-air"),
    FIRST_ORBITAL_REFUELING("First Orbital Refueling", "technology", "First in-orbit fuel transfer"),
    FIRST_SOLAR_SAIL("First Solar Sail", "technology", "First solar sail propulsion demonstration"),
    FIRST_ION_PROPULSION("First Ion Propulsion", "technology", "First ion engine in deep space"),
    FIRST_ELECTRIC_PROPULSION("First Electric Propulsion", "technology", "First electric propulsion system"),
    FIRST_NUCLEAR_THERMAL_ENGINE("First Nuclear Thermal Engine", "technology", "First nuclear thermal propulsion test"),

    // ==================== Commercial Firsts ====================
    FIRST_COMMERCIAL_SATELLITE("First Commercial Satellite", "commercial", "First commercial communications satellite"),
    FIRST_COMMERCIAL_LAUNCH("First Commercial Launch", "commercial", "First commercial orbital launch"),
    FIRST_COMMERCIAL_SPACE_STATION("First Commercial Space Station", "commercial", "First commercial space station"),
    FIRST_SPACE_TOURISM("First Space Tourism", "commercial", "First space tourist"),
    FIRST_COMMERCIAL_LUNAR_LANDING("First Commercial Lunar Landing", "commercial", "First commercial lunar lander"),

    // ==================== Deep Space Firsts ====================
    FIRST_SOLAR_PROBE("First Solar Probe", "deep_space", "First spacecraft to closely approach the Sun"),
    FIRST_HELIOCENTRIC_ORBIT("First Heliocentric Orbit", "deep_space", "First spacecraft in heliocentric orbit"),
    FIRST_LAGRANGE_POINT("First Lagrange Point", "deep_space", "First spacecraft at a Lagrange point"),
    FIRST_INTERSTELLAR_PROBE("First Interstellar Probe", "deep_space", "First spacecraft to enter interstellar space"),
    FIRST_GRAVITATIONAL_ASSIST("First Gravitational Assist", "deep_space", "First planetary gravity assist maneuver"),

    // ==================== Human Spaceflight Records ====================
    LONGEST_SPACEFLIGHT("Longest Spaceflight", "record", "Longest single spaceflight duration"),
    MOST_SPACEWALKS("Most Spacewalks", "record", "Most EVAs by an individual"),
    MOST_TIME_IN_SPACE("Most Time in Space", "record", "Most cumulative time in space"),
    OLDEST_ASTRONAUT("Oldest Astronaut", "record", "Oldest person to reach space"),
    YOUNGEST_ASTRONAUT("Youngest Astronaut", "record", "Youngest person to reach space");

    private final String displayName;
    private final String category;
    private final String description;

    MilestoneType(String displayName, String category, String description) {
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

    /**
     * Get all milestone types in a category
     */
    public static java.util.List<MilestoneType> getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(m -> m.category.equals(category))
                .toList();
    }

    /**
     * Get all unique categories
     */
    public static java.util.List<String> getAllCategories() {
        return java.util.Arrays.stream(values())
                .map(MilestoneType::getCategory)
                .distinct()
                .toList();
    }
}
