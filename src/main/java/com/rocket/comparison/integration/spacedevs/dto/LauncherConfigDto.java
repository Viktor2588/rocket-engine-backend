package com.rocket.comparison.integration.spacedevs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LauncherConfigDto {
    private Integer id;
    private String url;
    private String name;
    private AgencyDto manufacturer;
    private String family;

    @JsonProperty("full_name")
    private String fullName;

    private String variant;
    private Boolean reusable;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("info_url")
    private String infoUrl;

    @JsonProperty("wiki_url")
    private String wikiUrl;
}
