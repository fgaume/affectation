package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.affelnet75.AffelnetClient;

public class BaseService {

    protected final OpenDataClient openDataClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(OpenDataClient.class, "https://data.education.gouv.fr/api/records/1.0/search");

    protected final AffelnetClient affelnetClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(AffelnetClient.class, "https://affelnet75.web.app/api");

}
