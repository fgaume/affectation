package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
import org.gaume.affectation.repo.CollegeRepository;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.annuaire.Annuaire;
import org.gaume.opendata.annuaire.AnnuaireFields;
import org.gaume.opendata.annuaire.CodeNatureEtablissement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void importColleges() {

        if (!collegeRepository.findAll().iterator().hasNext()) {
            Annuaire annuaire = openDataClient.fetchAnnuaire(CodeNatureEtablissement.COLLEGE.getCode());
            log.info("[Import OpenData Annuaire] : {} items", annuaire.nhits());
            annuaire.records().forEach((item) -> {
                AnnuaireFields data = item.fields();
                College college = College.builder()
                        .id(data.identifiantEtablissement())
                        .nom(data.nomEtablissement())
                        .adresse(data.adresse())
                        .codePostal(data.codePostal())
                        .latitude(data.latitude())
                        .longitude(data.longitude())
                        .coordonneeX(data.coordonneeX())
                        .coordonneeY(data.coordonneeY())
                        .build();
                collegeRepository.save(college);
            });
        }
        else {
            log.warn("[Import OpenData Annuaire] : table non vide donc pas d'import");
        }
    }


    /* @Transactional
    public void saveColleges() {
        // save a few customers
        //repository.save(new College("0750465Y", "LUCIE ET RAYMOND AUBRAC"));
        //repository.save(new College("0750484U", "WOLFGANG AMADEUS MOZART"));

        // fetch all customers
        log.info("College found with findAll():");
        log.info("-------------------------------");
        for (College customer : collegeRepository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Optional<College> customer = collegeRepository.findById("0750465Y");
        if (customer.isPresent()) {
            log.info("Customer found with findById(\"0750465Y\"):");
            log.info("--------------------------------");
            log.info(customer.get().toString());
        }

        Optional<College> college = collegeRepository.findByNom("WOLFGANG AMADEUS MOZART");
        if (college.isPresent()) {
            log.info("Customer found with findByNom(\"WOLFGANG AMADEUS MOZART\"):");
            log.info("--------------------------------");
            log.info(college.get().toString());
        }

    } */
}
