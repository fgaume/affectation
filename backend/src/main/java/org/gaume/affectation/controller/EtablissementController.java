package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.service.EtablissementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@Slf4j
@RequestMapping("etablissements")
public class EtablissementController {

    private final EtablissementService etablissementService;

    @GetMapping(value = "/import")
    public void importEtablissements() {
        etablissementService.importEtablissements();
    }

}