package org.gaume.opendata.dnb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrevetFields(

        @JsonProperty("numero_d_etablissement")
        String identifiantEtablissement,

        @JsonProperty("patronyme")
        String patronyme,

        @JsonProperty("session")
        String annee,

        @JsonProperty("taux_de_reussite")
        String dnbTauxReussite,

        @JsonProperty("nombre_de_presents")
        int dnbPresents,

        @JsonProperty("nombre_total_d_admis")
        int dnbAdmis,

        @JsonProperty("nombre_d_admis_sans_mention")
        int dnbAdmisSansMention,

        @JsonProperty("nombre_d_admis_mention_ab")
        int dnbAdmisAssezBien,

        @JsonProperty("nombre_d_admis_mention_b")
        int dnbAdmisBien,

        @JsonProperty("nombre_d_admis_mention_tb")
        int dnbAdmisTresBien
        ) {
}