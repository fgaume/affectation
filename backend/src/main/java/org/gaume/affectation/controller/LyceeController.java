package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import org.gaume.affectation.service.LyceeAnnuelService;
import org.gaume.affectation.service.LyceeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value ="lycees", produces = "application/json")
public class LyceeController {

    private final LyceeAnnuelService lyceeAnnuelService;
    private final LyceeService lyceeService;

    @GetMapping(value = "/{annee}/import")
    public void importLyceesByAnnee(@PathVariable Integer annee) {
        lyceeAnnuelService.importLyceesByAnnee(annee);
    }

    @GetMapping(value = "/clean")
    public void cleanLycees() {
        lyceeService.cleanLycees();
    }


}