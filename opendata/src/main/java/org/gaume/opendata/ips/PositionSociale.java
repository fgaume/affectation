package org.gaume.opendata.ips;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PositionSociale(

    @JsonProperty("nhits")
    int nhits,

    @JsonProperty("records")
    List<PositionSocialeRecordsItem> records
    ) {
}