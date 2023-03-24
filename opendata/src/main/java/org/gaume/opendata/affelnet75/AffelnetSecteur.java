package org.gaume.opendata.affelnet75;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AffelnetSecteur(

    @JsonProperty("secteur")
    Integer secteur,

    @JsonProperty("college_id")
    String collegeId,

    @JsonProperty("annee")
    Integer annee,

    @JsonProperty("lycee_id")
    String lyceeId
) {
}