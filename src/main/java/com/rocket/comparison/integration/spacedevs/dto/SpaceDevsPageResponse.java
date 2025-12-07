package com.rocket.comparison.integration.spacedevs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceDevsPageResponse<T> {
    private Integer count;
    private String next;
    private String previous;
    private List<T> results;
}
