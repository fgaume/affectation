package org.gaume.opendata.annuaire;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnnuaireFields(

        @JsonProperty("identifiant_de_l_etablissement")
        String identifiantEtablissement,

        @JsonProperty("nom_etablissement")
        String nomEtablissement,

        @JsonProperty("adresse_1")
        String adresse,

        @JsonProperty("code_postal")
        String codePostal,

        @JsonProperty("nombre_d_eleves")
        int nombreEleves,

        @JsonProperty("latitude")
        double latitude,

        @JsonProperty("longitude")
        double longitude,

        @JsonProperty("coordx_origine")
        float coordonneeX,

        @JsonProperty("coordy_origine")
        float coordonneeY

) {
}