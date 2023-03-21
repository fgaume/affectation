package org.gaume.opendata.affelnet75;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value = "affelnet75", url = "https://affelnet75.web.app/api")
public interface AffelnetClient {

    @RequestLine("GET /colleges/{annee}/ips.json")
    @Headers("Content-Type: application/json")
    List<BonusIPSItem> fetchBonusIPS(@Param int annee);

    @RequestLine("GET /lycees/{annee}/seuils.json")
    @Headers("Content-Type: application/json")
    BonusIPS fetchSeuils(@Param int annee);

}
