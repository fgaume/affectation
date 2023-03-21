package org.gaume;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.ips.PositionSociale;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        OpenDataClient openDataClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OpenDataClient.class, "https://data.education.gouv.fr/api/records/1.0/search");

        /* Annuaire response = openDataClient.getEtablissementResponse("340");
        System.out.println("" + response.nhits());
        Annuaire response1 = openDataClient.getEtablissementResponse("300");
        System.out.println("" + response1.nhits());
        Annuaire response2 = openDataClient.getEtablissementResponse("302");
        System.out.println("" + response2.nhits());
        Annuaire response3 = openDataClient.getEtablissementResponse("306");
        System.out.println("" + response3.nhits()); */

        /* Brevet brevetResponse = openDataClient.fetchBrevet("2021");
        System.out.println("" + brevetResponse.nhits()); */

        PositionSociale positionSocialeResponse = openDataClient.fetchIps("2021-2022");
        System.out.println("" + positionSocialeResponse.nhits());



    }
}