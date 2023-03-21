package org.gaume.opendata.annuaire;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Annuaire(

        @JsonProperty("nhits")
        int nhits,

        @JsonProperty("records")
        List<AnnuaireRecordsItem> records) {
}