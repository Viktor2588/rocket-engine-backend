package com.rocket.comparison.integration.spacedevs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchDto {
    private String id;
    private String url;
    private String slug;
    private String name;
    private LaunchStatusDto status;

    @JsonProperty("last_updated")
    private String lastUpdated;

    private String net; // NET = No Earlier Than (launch time)

    @JsonProperty("window_start")
    private String windowStart;

    @JsonProperty("window_end")
    private String windowEnd;

    @JsonProperty("holdreason")
    private String holdReason;

    @JsonProperty("failreason")
    private String failReason;

    @JsonProperty("launch_service_provider")
    private AgencyDto launchServiceProvider;

    private RocketDto rocket;
    private MissionDto mission;
    private PadDto pad;

    @JsonProperty("webcast_live")
    private Boolean webcastLive;

    private String image;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LaunchStatusDto {
        private Integer id;
        private String name;
        private String abbrev;
        private String description;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RocketDto {
        private Integer id;
        private LauncherConfigDto configuration;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MissionDto {
        private Integer id;
        private String name;
        private String description;
        private String type;
        private OrbitDto orbit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrbitDto {
        private Integer id;
        private String name;
        private String abbrev;
    }
}
