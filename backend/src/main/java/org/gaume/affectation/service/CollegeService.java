package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.repo.CollegeRepository;
import org.gaume.opendata.OpenDataClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CollegeService {
    private final OpenDataClient openDataClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(OpenDataClient.class, "https://data.education.gouv.fr/api/records/1.0/search");

    private final CollegeRepository collegeRepository;

    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

}
