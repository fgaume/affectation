package org.gaume.opendata.arcgis;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SecteursAffelnet(

    @JsonProperty("features")
    List<FeaturesItem> features
) {
}