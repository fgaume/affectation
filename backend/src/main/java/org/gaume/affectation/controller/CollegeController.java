package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import org.gaume.affectation.model.College;
import org.gaume.affectation.service.CollegeAnnuelService;
import org.gaume.affectation.service.CollegeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;
    private final CollegeAnnuelService collegeAnnuelService;

    @GetMapping(value = "/colleges/{collegeId}")
    public College getCollege(@PathVariable String collegeId) {
        College college = new College("0750465Y", "LUCIE ET RAYMOND AUBRAC");
        collegeService.saveColleges();
        return college;
    }

    @GetMapping(value = "/colleges/import")
    public void importAllColleges() {
        collegeService.importAllColleges();
    }

    @GetMapping(value = "/colleges/ips2022")
    public void importIPS2022() {
        collegeAnnuelService.importIPS2022();
    }
}