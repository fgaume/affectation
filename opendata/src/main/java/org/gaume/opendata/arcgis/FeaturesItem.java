package org.gaume.opendata.arcgis;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FeaturesItem(

    @JsonProperty("attributes")
    Attributes attributes
) {
}