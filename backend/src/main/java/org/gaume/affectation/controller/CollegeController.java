package org.gaume.affectation.controller;


import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.service.CollegeAnnuelService;
import org.gaume.affectation.service.CollegeService;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.ips.PositionSociale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
public class CollegeController {

    private final CollegeService collegeService;
    private final CollegeAnnuelService collegeAnnuelService;

    @GetMapping(value = "/colleges/import")
    public void importColleges() {
        collegeService.importColleges();
    }

    @GetMapping(value = "/colleges/{annee}/import")
    public void importCollegesByAnnee(@PathVariable Integer annee) {
        collegeAnnuelService.importCollegesByAnnee(annee);
    }

}