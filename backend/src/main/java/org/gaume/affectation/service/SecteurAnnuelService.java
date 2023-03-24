package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.*;
import org.gaume.affectation.repo.*;
import org.gaume.opendata.affelnet75.AffelnetSecteur;
import org.gaume.opendata.arcgis.ArcgisClient;
import org.gaume.opendata.arcgis.FeaturesItem;
import org.gaume.opendata.arcgis.SecteursAffelnet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecteurAnnuelService extends BaseService {

    private static final String whereTemplate = "secteur<>'Tête' and UAI='%s'";

    private final SecteurAnnuelRepository secteurAnnuelRepository;

    private final CollegeRepository collegeRepository;

    private final CollegeAnnuelRepository collegeAnnuelRepository;

    private final LyceeRepository lyceeRepository;

    private final LyceeAnnuelRepository lyceeAnnuelRepository;

    @Transactional
    public void importSecteursByAnnee(int annee) {
        try {
            List<AffelnetSecteur> secteurs = affelnetClient.fetchAffelnetSecteurs(annee);
            if (secteurs != null) {
                secteurs.forEach(secteur -> {
                    Optional<SecteurAnnuel> optionalSecteurAnnuel = findSecteurAnnuel(
                            secteur.collegeId(),
                            secteur.lyceeId(),
                            secteur.annee());
                    if (optionalSecteurAnnuel.isPresent()) {
                        SecteurAnnuel secteurAnnuel = optionalSecteurAnnuel.get();
                        secteurAnnuel.setSecteur(secteur.secteur());
                        secteurAnnuelRepository.save(secteurAnnuel);
                    } else {
                        log.error("secteur avec etablissement inconnu : {}", secteur);
                    }
                });
            } else {
                log.error("Pas de secteurs en {}", annee);
            }

        } catch (Exception e) {
            log.error("Pas de secteurs en {}", annee);
        }
    }

    public int[] evaluerLyceesAccessibles(int annee, int bonus, float score) {
        List<CollegeAnnuel> collegeAnnuels = collegeAnnuelRepository.findByAnneeAndIpsBonus(annee, bonus);
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
            } else {
                distribution[lyceeAccessibles] = distribution[lyceeAccessibles] + 1;
            }
        }
        return distribution;
    }

    private Optional<SecteurAnnuel> findSecteurAnnuel(String collegeId, String lyceeId, int annee) {
        Optional<Lycee> lyceeOptional = lyceeRepository.findById(lyceeId);
        Optional<College> collegeOptional = collegeRepository.findById(collegeId);
        if (lyceeOptional.isPresent() && collegeOptional.isPresent()) {
            Optional<SecteurAnnuel> optionalSecteurAnnuel = secteurAnnuelRepository.findByCollegeAndLyceeAndAnnee(
                    collegeOptional.get(),
                    lyceeOptional.get(),
                    annee);
            if (optionalSecteurAnnuel.isEmpty()) {
                optionalSecteurAnnuel = Optional.of(new SecteurAnnuel(
                        collegeOptional.get(),
                        lyceeOptional.get(),
                        annee));
            }
            return optionalSecteurAnnuel;
        }
        return Optional.empty();
    }

    @Transactional
    public void importSecteursArcgis(int annee) {
        try {
            ArcgisClient secteurResource = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .target(ArcgisClient.class, "https://services9.arcgis.com");

            Iterable<Lycee> lycees = lyceeRepository.findAll();
            lycees.forEach(lycee -> {
                String where = String.format(whereTemplate, lycee.getId());
                SecteursAffelnet secteurList = secteurResource.fetchSecteursAffelnet(where);
                List<FeaturesItem> items = secteurList.features();
                if (items.isEmpty()) {
                    log.error("secteur absent : {} {}", lycee.getId(), lycee.getNom());
                } else {
                    //log.info("adresse present : " + adresse.getNom());
                    for (FeaturesItem item : items) {
                        int secteur = Integer.parseInt(item.attributes().secteur());
                        Optional<College> collegeOptional = collegeRepository.findByNomAffelnet(item.attributes().nomCollege());
                        if (collegeOptional.isPresent()) {
                            College college = collegeOptional.get();
                            Optional<SecteurAnnuel> secteurAnnuelOptional = findSecteurAnnuel(
                                    college.getId(),
                                    lycee.getId(),
                                    annee);
                            if (secteurAnnuelOptional.isPresent()) {
                                SecteurAnnuel secteurAnnuel = secteurAnnuelOptional.get();
                                secteurAnnuel.setAnnee(annee);
                                secteurAnnuel.setCollege(college);
                                secteurAnnuel.setSecteur(secteur);
                                secteurAnnuel.setLycee(lycee);
                                //log.info("saving arcgis {}", secteurAnnuel);
                                secteurAnnuelRepository.save(secteurAnnuel);
                            }
                            else {
                                log.error("lycee ou college inconnu de la base :{} {}", lycee.getId(), college.getId());
                            }
                        } else {
                            log.error("college absent : " + item.attributes().nomCollege());
                        }
                    }
                }
            });
        }
        catch (Exception e) {
            log.error("Exception : ", e);
        }
    }

}
