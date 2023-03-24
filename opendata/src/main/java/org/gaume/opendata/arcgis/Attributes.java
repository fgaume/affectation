package org.gaume.opendata.arcgis;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Attributes(

        @JsonProperty("Nom_tete")
        String nomCollege,

        @JsonProperty("secteur")
        String secteur
) {
}