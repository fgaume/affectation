package org.gaume.opendata.affelnet75;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AffelnetLycee(

    @JsonProperty("score_admission")
    Float scoreAdmission,

    @JsonProperty("capacite")
    Integer capacite,

    @JsonProperty("annee")
    Integer annee,

    @JsonProperty("lycee_id")
    String lyceeId,

    @JsonProperty("ips_moyen")
    Float ipsMoyen
) {
}