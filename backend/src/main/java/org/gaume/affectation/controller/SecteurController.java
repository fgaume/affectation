package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import org.gaume.affectation.service.CollegeService;
import org.gaume.affectation.service.SecteurAnnuelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
public class SecteurController {

    private final CollegeService collegeService;
    private final SecteurAnnuelService secteurAnnuelService;



    @GetMapping(value = "/secteurs/2022")
    public void importIPS2022() {
        secteurAnnuelService.importSecteurs();
    }

    @GetMapping(value = "/secteurs/{annee}/{bonus}/{score}", produces = "application/json")
    public int[] evaluerLyceesAccessibles(
            @PathVariable int annee,
            @PathVariable int bonus,
            @PathVariable float score) {
        return secteurAnnuelService.evaluerLyceesAccessibles(annee, bonus, score+bonus);
    }

}