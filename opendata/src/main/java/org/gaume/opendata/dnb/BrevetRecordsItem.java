package org.gaume.opendata.dnb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BrevetRecordsItem(

	@JsonProperty("fields")
	BrevetFields fields

) {
}