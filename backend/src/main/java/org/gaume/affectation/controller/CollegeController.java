package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.service.CollegeAnnuelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value ="colleges", produces = "application/json")
public class CollegeController {

    private final CollegeAnnuelService collegeAnnuelService;

    @GetMapping(value = "/{annee}/import")
    public void importCollegesByAnnee(@PathVariable Integer annee) {
        collegeAnnuelService.importCollegesByAnnee(annee);
    }

}