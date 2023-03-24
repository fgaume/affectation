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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value ="secteurs", produces = "application/json")
public class SecteurController {

    private static final String whereTemplate = "secteur<>'Tête' and UAI='%s'";

    private final CollegeService collegeService;
    private final SecteurAnnuelService secteurAnnuelService;


    @GetMapping(value = "/{annee}/import")
    public void importLyceesByAnnee(@PathVariable int annee) {
        secteurAnnuelService.importSecteursByAnnee(annee);
    }

    @GetMapping(value = "/{annee}/import-argis")
    public void importLyceesArcgis(@PathVariable int annee) {
        secteurAnnuelService.importSecteursArcgis(annee);
    }

    @GetMapping(value = "/{annee}/{bonus}/{score}", produces = "application/json")
    public int[] evaluerLyceesAccessibles(
            @PathVariable int annee,
            @PathVariable int bonus,
            @PathVariable float score) {
        return secteurAnnuelService.evaluerLyceesAccessibles(annee, bonus, score+bonus);
    }

}