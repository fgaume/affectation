package org.gaume.opendata.annuaire;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnnuaireRecordsItem(
        @JsonProperty("fields")
        AnnuaireFields fields
) {
}