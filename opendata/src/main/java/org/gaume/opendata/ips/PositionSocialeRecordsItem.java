package org.gaume.opendata.ips;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PositionSocialeRecordsItem(

    @JsonProperty("fields")
    PositionSocialeFields fields
) {
}