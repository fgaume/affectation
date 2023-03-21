package org.gaume.opendata.arcgis;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "arcgis", url = "https://services9.arcgis.com")
public interface ArcgisClient {

    @RequestLine("GET /ekT8MJFiVh8nvlV5/arcgis/rest/services/Affectation_Lyc%C3%A9es/FeatureServer/0/query?outFields=Nom_tete,secteur&returnGeometry=false&resultOffset=0&f=pjson&orderByFields=secteur&where={where}")
    @Headers("Content-Type: application/json")
    SecteursAffelnet fetchSecteursAffelnet(@Param String where);
}
