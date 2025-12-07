package com.rocket.comparison.constants;

/**
 * Application-wide constants for the Space Capabilities Dashboard.
 * Centralizes magic numbers and threshold values for maintainability.
 */
public final class SpaceConstants {

    private SpaceConstants() {
        // Prevent instantiation
    }

    // ==================== Tier Thresholds ====================

    /** Score threshold for Tier 1 - Space Superpower */
    public static final double TIER_1_THRESHOLD = 80.0;

    /** Score threshold for Tier 2 - Major Space Power */
    public static final double TIER_2_THRESHOLD = 60.0;

    /** Score threshold for Tier 3 - Established Space Nation */
    public static final double TIER_3_THRESHOLD = 40.0;

    /** Score threshold for Tier 4 - Emerging Space Nation */
    public static final double TIER_4_THRESHOLD = 20.0;

    /** Score threshold for top-tier nations in analytics */
    public static final double TOP_TIER_SCORE_THRESHOLD = 70.0;

    // ==================== Analysis Thresholds ====================

    /** Minimum launches required for success rate ranking */
    public static final int MIN_LAUNCHES_FOR_RANKING = 10;

    /** Years to look back for emerging nations analysis */
    public static final int EMERGING_NATIONS_YEARS_WINDOW = 10;

    /** Minimum engines to qualify as "strong engine program" */
    public static final int STRONG_ENGINE_PROGRAM_THRESHOLD = 5;

    /** Minimum satellites to qualify as "large satellite fleet" */
    public static final int LARGE_SATELLITE_FLEET_THRESHOLD = 20;

    /** Excellent success rate threshold (percentage) */
    public static final double EXCELLENT_SUCCESS_RATE = 95.0;

    /** Concerning success rate threshold (percentage) */
    public static final double CONCERNING_SUCCESS_RATE = 90.0;

    /** Low capability score threshold */
    public static final double LOW_CAPABILITY_SCORE = 30.0;

    // ==================== Budget Thresholds ====================

    /** Limited budget threshold in USD */
    public static final long LIMITED_BUDGET_USD = 500_000_000L;

    /** Budget constraint threshold in USD */
    public static final long BUDGET_CONSTRAINT_THRESHOLD_USD = 1_000_000_000L;

    // ==================== Similarity Scoring ====================

    /** Weight for propellant match in engine similarity */
    public static final double SIMILARITY_PROPELLANT_WEIGHT = 30.0;

    /** Weight for power cycle match in engine similarity */
    public static final double SIMILARITY_CYCLE_WEIGHT = 30.0;

    /** Weight for thrust similarity in engine similarity */
    public static final double SIMILARITY_THRUST_WEIGHT = 20.0;

    /** Weight for ISP similarity in engine similarity */
    public static final double SIMILARITY_ISP_WEIGHT = 20.0;

    // ==================== Capability Scoring ====================

    /** Score for human-rated capability */
    public static final int CAPABILITY_HUMAN_RATED_SCORE = 25;

    /** Score for GEO support capability */
    public static final int CAPABILITY_GEO_SUPPORT_SCORE = 20;

    /** Score for polar orbit support */
    public static final int CAPABILITY_POLAR_SUPPORT_SCORE = 15;

    /** Score for interplanetary capability */
    public static final int CAPABILITY_INTERPLANETARY_SCORE = 25;

    /** Score for landing facilities */
    public static final int CAPABILITY_LANDING_FACILITIES_SCORE = 15;

    // ==================== Emergence Scoring ====================

    /** Points per recent mission in emergence calculation */
    public static final int EMERGENCE_POINTS_PER_MISSION = 5;

    /** Points per recent milestone in emergence calculation */
    public static final int EMERGENCE_POINTS_PER_MILESTONE = 10;

    /** Bonus points for independent launch capability */
    public static final int EMERGENCE_LAUNCH_CAPABILITY_BONUS = 20;

    // ==================== Display Constants ====================

    /** Default limit for top results */
    public static final int DEFAULT_TOP_LIMIT = 5;

    /** Default limit for emerging nations list */
    public static final int EMERGING_NATIONS_LIMIT = 10;

    /** Current budget data year */
    public static final int CURRENT_BUDGET_YEAR = 2024;

    // ==================== Physical Constants ====================

    /** Standard gravity in m/s² for thrust-to-weight calculations */
    public static final double STANDARD_GRAVITY = 9.81;

    // ==================== Tier Names ====================

    public static final String TIER_1_NAME = "Tier 1 - Space Superpower";
    public static final String TIER_2_NAME = "Tier 2 - Major Space Power";
    public static final String TIER_3_NAME = "Tier 3 - Established Space Nation";
    public static final String TIER_4_NAME = "Tier 4 - Emerging Space Nation";
    public static final String TIER_5_NAME = "Tier 5 - Developing Space Capability";
    public static final String TIER_UNRANKED = "Unranked";
}
