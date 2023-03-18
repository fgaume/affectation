package org.gaume.affectation.controller;


import lombok.RequiredArgsConstructor;
import org.gaume.affectation.service.LyceeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
public class LyceeController {

    private final LyceeService lyceeService;

    /* @GetMapping(value = "/lycees/{collegeId}")
    public College getCollege(@PathVariable String collegeId) {
        College college = new College("0750465Y", "LUCIE ET RAYMOND AUBRAC");
        collegeService.saveColleges();
        return college;
    } */

    @GetMapping(value = "/lycees/import")
    public void importAllLycees() {
        lyceeService.importAllLycees();
    }

}