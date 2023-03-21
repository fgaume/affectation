package org.gaume.opendata.arcgis;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Attributes(

    @JsonProperty("secteur")
    String secteur,

    @JsonProperty("Nom_tete")
    String nomCollege
) {
}