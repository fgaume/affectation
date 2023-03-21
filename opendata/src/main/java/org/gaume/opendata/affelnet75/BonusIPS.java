package org.gaume.opendata.affelnet75;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BonusIPS(

    @JsonProperty("BonusIPS")
    List<BonusIPSItem> bonusIPS
) {
}