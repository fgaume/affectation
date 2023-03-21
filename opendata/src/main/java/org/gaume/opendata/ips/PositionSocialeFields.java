package org.gaume.opendata.ips;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PositionSocialeFields(

        @JsonProperty("uai")
        String identifiantEtablissement,

        @JsonProperty("rentree_scolaire")
        String rentreeScolaire,
        @JsonProperty("nom_de_l_etablissment")
        String nomEtablissment,

        @JsonProperty("ips")
        float ipsMoyenne,

        @JsonProperty("ecart_type_de_l_ips")
        float ipsEcartType
) {
}