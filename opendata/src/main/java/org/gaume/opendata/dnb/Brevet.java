package org.gaume.opendata.dnb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Brevet(

	@JsonProperty("nhits")
	int nhits,

	@JsonProperty("records")
	List<BrevetRecordsItem> records
) {
}