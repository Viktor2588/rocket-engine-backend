package com.rocket.comparison.integration.spacedevs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PadDto {
    private Integer id;
    private String url;

    @JsonProperty("agency_id")
    private Integer agencyId;

    private String name;
    private String description;

    @JsonProperty("info_url")
    private String infoUrl;

    @JsonProperty("wiki_url")
    private String wikiUrl;

    @JsonProperty("map_url")
    private String mapUrl;

    private String latitude;
    private String longitude;
    private LocationDto location;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("map_image")
    private String mapImage;

    @JsonProperty("total_launch_count")
    private Integer totalLaunchCount;

    @JsonProperty("orbital_launch_attempt_count")
    private Integer orbitalLaunchAttemptCount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationDto {
        private Integer id;
        private String url;
        private String name;

        @JsonProperty("country_code")
        private String countryCode;

        private String description;

        @JsonProperty("map_image")
        private String mapImage;

        @JsonProperty("timezone_name")
        private String timezoneName;

        @JsonProperty("total_launch_count")
        private Integer totalLaunchCount;

        @JsonProperty("total_landing_count")
        private Integer totalLandingCount;
    }
}
