package org.gaume.affectation.controller;


import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.service.CollegeService;
import org.gaume.affectation.service.SecteurAnnuelService;
import org.gaume.opendata.arcgis.ArcgisClient;
import org.gaume.opendata.arcgis.SecteursAffelnet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
public class SecteurController {

    private static final String whereTemplate = "secteur<>'Tête' and UAI='%s'";

    private final CollegeService collegeService;
    private final SecteurAnnuelService secteurAnnuelService;


    @GetMapping(value = "/secteurs/2022/{idLycee}")
    public void importIPS2022(@PathVariable String idLycee) {
        //secteurAnnuelService.importSecteurs();
        ArcgisClient affelnetClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ArcgisClient.class, "https://services9.arcgis.com");
        SecteursAffelnet secteursAffelnet = affelnetClient.fetchSecteursAffelnet(
                String.format(whereTemplate, idLycee));
        log.info("nbHits2={}", secteursAffelnet.features().size());

    }

    @GetMapping(value = "/secteurs/{annee}/{bonus}/{score}", produces = "application/json")
    public int[] evaluerLyceesAccessibles(
            @PathVariable int annee,
            @PathVariable int bonus,
            @PathVariable float score) {
        return secteurAnnuelService.evaluerLyceesAccessibles(annee, bonus, score+bonus);
    }

}