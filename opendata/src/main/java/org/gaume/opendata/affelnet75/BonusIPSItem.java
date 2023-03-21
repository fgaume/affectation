package org.gaume.opendata.affelnet75;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BonusIPSItem(

    @JsonProperty("college_id")
    String collegeId,

    @JsonProperty("annee")
    Integer annee,

    @JsonProperty("ips_bonus")
    Integer ipsBonus,

    @JsonProperty("ips_moyen")
    Float ipsMoyen
) {
}