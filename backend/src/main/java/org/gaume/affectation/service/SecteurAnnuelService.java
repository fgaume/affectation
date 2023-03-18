package org.gaume.affectation.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.io.AffelnetClient;
import org.gaume.affectation.io.FeaturesItem;
import org.gaume.affectation.io.Response;
import org.gaume.affectation.model.*;
import org.gaume.affectation.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import feign.Feign;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SecteurAnnuelService {

    private static final String whereTemplate = "secteur<>'Tête' and Nom_tete='%s'";

    @Autowired
    private SecteurAnnuelRepository secteurAnnuelRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private CollegeAnnuelRepository collegeAnnuelRepository;

    @Autowired
    private LyceeRepository lyceeRepository;

    @Autowired
    private LyceeAnnuelRepository lyceeAnnuelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public int[] evaluerLyceesAccessibles(int annee, int bonus, float score) {
        List<CollegeAnnuel> collegeAnnuels = collegeAnnuelRepository.findByAnneeAndBonus(annee, bonus);
        int[] distribution = new int[6];
        for (CollegeAnnuel collegeAnnuel : collegeAnnuels) {
            int lyceeAccessibles = 0;
            List<SecteurAnnuel> secteurs = secteurAnnuelRepository.findByCollegeAndAnneeAndSecteur(collegeAnnuel.getCollege(), annee, 1);
            if (secteurs.size() > 5) {
                log.error("error {} {}", collegeAnnuel.getCollege().getId(), collegeAnnuel.getCollege());
            }
            for (SecteurAnnuel secteurAnnuel : secteurs) {
                Lycee lycee = secteurAnnuel.getLycee();
                Optional<LyceeAnnuel> lyceeAnnuelOpt = lyceeAnnuelRepository.findByLyceeAndAnnee(lycee, annee);
                if (lyceeAnnuelOpt.isPresent()) {
                    LyceeAnnuel lyceeAnnuel = lyceeAnnuelOpt.get();
                    float seuilAdmission = lyceeAnnuel.getScoreAdmission();
                    if (score >= seuilAdmission) lyceeAccessibles++;
                }
            }
            if (lyceeAccessibles < 4) {
                log.info("{} : {}", collegeAnnuel.getCollege().getNom(), lyceeAccessibles);
            }
            if (lyceeAccessibles > 5) {
                log.error("erreur {} : {}", collegeAnnuel.getCollege().getNom(), lyceeAccessibles);
            }
            else {
                distribution[lyceeAccessibles] = distribution[lyceeAccessibles] + 1;
            }
        }
        return distribution;
    }
    @Transactional
    public void importSecteurs() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            AffelnetClient secteurResource = Feign.builder()
                    .target(AffelnetClient.class, "https://services9.arcgis.com");
            //Iterable<College> colleges = collegeRepository.findAll();
            List<College> colleges = new ArrayList<>();
            Optional<College> college1 = collegeRepository.findById("0750484U");
            colleges.add(college1.get());
            for (College college: colleges) {
                log.info("traitement college : {}", college.getNom());
                String where = String.format(whereTemplate, college.getNom());
                String response = secteurResource.getSecteurReponse(where);
                String reponse = response.replaceAll("UAI", "lyceeId");
                Response rep = objectMapper.readValue(reponse, Response.class);
                //log.info(rep.toString());
                List<FeaturesItem> items = rep.getFeatures();
                if (items.isEmpty()) {
                    log.error("college absent : {} {}", college.getId(), college.getNom());
                }
                else {
                    //log.info("college present : " + college.getNom());
                    for(FeaturesItem item : items) {
                        int secteur = Integer.parseInt(item.getAttributes().getSecteur());
                        String lyceeId = item.getAttributes().getLyceeId();
                        Optional<Lycee> lyceeOptional = lyceeRepository.findById(lyceeId);
                        if (lyceeOptional.isPresent()) {
                            Lycee lycee = lyceeOptional.get();
                            //log.info("lycee present : " + lycee);
                            SecteurAnnuel secteurAnnuel = new SecteurAnnuel();
                            secteurAnnuel.setAnnee(2022);
                            secteurAnnuel.setCollege(college);
                            secteurAnnuel.setSecteur(secteur);
                            secteurAnnuel.setLycee(lycee);
                            //log.info("saving secteur {}", secteurAnnuel);
                            secteurAnnuelRepository.save(secteurAnnuel);
                        }
                        else {
                            log.error("lycee absent : " + lyceeId);
                        }
                    }
                }
                log.info("traitement college : {} termine", college.getNom());

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
