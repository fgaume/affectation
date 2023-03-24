package org.gaume.opendata.affelnet75;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value = "affelnet75", url = "https://affelnet75.web.app/api")
public interface AffelnetClient {

    @RequestLine("GET /colleges/colleges_{annee}.json")
    @Headers("Content-Type: application/json")
    List<AffelnetCollege> fetchAffelnetColleges(@Param int annee);

    @RequestLine("GET /lycees/lycees_{annee}.json")
    @Headers("Content-Type: application/json")
    List<AffelnetLycee> fetchAffelnetLycees(@Param int annee);

    @RequestLine("GET /secteurs/secteurs_{annee}.json")
    @Headers("Content-Type: application/json")
    List<AffelnetSecteur> fetchAffelnetSecteurs(@Param int annee);
}
