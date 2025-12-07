package com.rocket.comparison.integration.spacedevs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgencyDto {
    private Integer id;
    private String url;
    private String name;
    private Boolean featured;
    private String type;

    @JsonProperty("country_code")
    private String countryCode;

    private String abbrev;
    private String description;
    private String administrator;

    @JsonProperty("founding_year")
    private String foundingYear;

    private String launchers;
    private String spacecraft;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("logo_url")
    private String logoUrl;
}
