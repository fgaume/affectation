package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
import org.gaume.affectation.model.CollegeAnnuel;
import org.gaume.affectation.repo.CollegeAnnuelRepository;
import org.gaume.affectation.repo.CollegeRepository;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.affelnet75.AffelnetClient;
import org.gaume.opendata.affelnet75.BonusIPSItem;
import org.gaume.opendata.dnb.Brevet;
import org.gaume.opendata.dnb.BrevetFields;
import org.gaume.opendata.dnb.BrevetRecordsItem;
import org.gaume.opendata.ips.PositionSociale;
import org.gaume.opendata.ips.PositionSocialeFields;
import org.gaume.opendata.ips.PositionSocialeRecordsItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CollegeAnnuelService {

    private final OpenDataClient openDataClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(OpenDataClient.class, "https://data.education.gouv.fr/api/records/1.0/search");

    private final AffelnetClient affelnetClient = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(AffelnetClient.class, "https://affelnet75.web.app/api");

    private final CollegeRepository collegeRepository;

    private final CollegeAnnuelRepository collegeAnnuelRepository;


    public CollegeAnnuelService(
            CollegeRepository collegeRepository,
            CollegeAnnuelRepository collegeAnnuelRepository) {
        this.collegeRepository = collegeRepository;
        this.collegeAnnuelRepository = collegeAnnuelRepository;
    }

    @Transactional
    public void importCollegesByAnnee(int annee) {
        importBonusIpsCollegeByAnnee(annee);
        importIpsCollegeByAnnee(annee);
        importBrevetCollegeByAnnee(annee);
    }

    private void importBrevetCollegeByAnnee(int annee) {
        Brevet brevet = openDataClient.fetchBrevet(String.valueOf(annee));
        log.info("[Import OpenData DNB] : {} items", brevet.nhits());
        for (BrevetRecordsItem item : brevet.records()) {
            BrevetFields data = item.fields();
            Optional<College> collegeOptional = collegeRepository.findById(data.identifiantEtablissement());
            if (collegeOptional.isPresent()) {
                String tauxReussite = data.dnbTauxReussite()
                        .replaceFirst("%", "")
                        .replace(',', '.');
                CollegeAnnuel collegeAnnuel = CollegeAnnuel.builder()
                        .annee(annee)
                        .college(collegeOptional.get())
                        .dnbPresents(data.dnbPresents())
                        .dnbAdmis(data.dnbAdmis())
                        .dnbTauxReussite(Float.parseFloat(tauxReussite))
                        .dnbAdmisSansMention(data.dnbAdmisSansMention())
                        .dnbAdmisAssezBien(data.dnbAdmisAssezBien())
                        .dnbAdmisBien(data.dnbAdmisBien())
                        .dnbAdmisTresBien(data.dnbAdmisTresBien())
                        .build();
                Optional<CollegeAnnuel> collegeAnnuelOptional = collegeAnnuelRepository.findByCollegeAndAnnee(collegeOptional.get(), annee);
                if (collegeAnnuelOptional.isPresent()) {
                    CollegeAnnuel existingCollegeAnnuel = collegeAnnuelOptional.get();
                    existingCollegeAnnuel.updateFrom(collegeAnnuel);
                    collegeAnnuelRepository.save(existingCollegeAnnuel);
                }
                else {
                    collegeAnnuelRepository.save(collegeAnnuel);
                }
            } else {
                log.error("[Import OpenData DNB] Collège inconnu : {} ", data.identifiantEtablissement());
            }
        }
    }

    private void importIpsCollegeByAnnee(int annee) {

        String rentresScolaire = String.format("%s-%s", annee - 1, annee);

        PositionSociale positionSociale = openDataClient.fetchIps(rentresScolaire);
        log.info("[Import OpenData IPS] : {} items", positionSociale.nhits());
        for (PositionSocialeRecordsItem item : positionSociale.records()) {
            PositionSocialeFields data = item.fields();
            Optional<College> collegeOptional = collegeRepository.findById(data.identifiantEtablissement());
            if (collegeOptional.isPresent()) {
                Optional<CollegeAnnuel> collegeAnnuelOptional = collegeAnnuelRepository.findByCollegeAndAnnee(collegeOptional.get(), annee);
                if (collegeAnnuelOptional.isPresent()) {
                    CollegeAnnuel collegeAnnuel = collegeAnnuelOptional.get();
                    if (!Float.valueOf(data.ipsMoyenne()).equals(collegeAnnuel.getIpsMoyen()))
                        log.warn("{} {} {}", collegeOptional.get().getNom(), data.ipsMoyenne(), collegeAnnuel.getIpsMoyen());
                    collegeAnnuel.setIpsMoyen(data.ipsMoyenne());
                    collegeAnnuel.setIpsEcartType(data.ipsEcartType());
                    collegeAnnuelRepository.save(collegeAnnuel);
                }
                else {
                    log.error("[Import OpenData IPS] Collège {} absent de la table pour {}", data.identifiantEtablissement(), annee);
                }
            } else {
                log.error("[Import OpenData IPS] Collège inconnu : {} ", data.identifiantEtablissement());
            }
        }
    }
    private void importBonusIpsCollegeByAnnee(int annee) {

        List<BonusIPSItem> bonusIPSItems = affelnetClient.fetchBonusIPS(annee);
        if (bonusIPSItems != null) {
            log.info("[Import Affelnet75 bonusIPSItems] : {} items", bonusIPSItems.size());
            for (BonusIPSItem item : bonusIPSItems) {
                Optional<College> collegeOptional = collegeRepository.findById(item.collegeId());
                if (collegeOptional.isPresent()) {
                    Optional<CollegeAnnuel> collegeAnnuelOptional = collegeAnnuelRepository.findByCollegeAndAnnee(collegeOptional.get(), annee);
                    if (collegeAnnuelOptional.isPresent()) {
                        CollegeAnnuel collegeAnnuel = collegeAnnuelOptional.get();
                        collegeAnnuel.setIpsMoyen(item.ipsMoyen());
                        collegeAnnuel.setIpsBonus(item.ipsBonus());
                        collegeAnnuelRepository.save(collegeAnnuel);
                    }
                    else {
                        CollegeAnnuel collegeAnnuel = CollegeAnnuel.builder()
                                .college(collegeOptional.get())
                                .annee(annee)
                                .ipsMoyen(item.ipsMoyen())
                                .ipsBonus(item.ipsBonus())
                                .build();
                        collegeAnnuelRepository.save(collegeAnnuel);
                    }
                } else {
                    log.error("[Import Affelnet75 bonusIPSItems] Collège inconnu : {} ", item.collegeId());
                }
            }
        }
    }

    //@Autowired
    //private ObjectMapper objectMapper;

    /* @Transactional
    public void importIPS2022() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            URL url = new URL("file:colleges.json");
            Map<String, Object>[] data = objectMapper.readValue(url, Map[].class);
            log.info(""+ data.length);
            for (Map<String,Object> collegeMap : data) {
                Object nom = collegeMap.get("nom");
                Optional<College> collegeOptional = collegeRepository.findByNom(nom.toString());
                if (collegeOptional.isEmpty()) {
                    log.error("adresse absent : " + nom);
                }
                else {
                    College college = collegeOptional.get();
                    Optional<CollegeAnnuel> collegeAnnuelOpt = collegeAnnuelRepository.findByCollegeAndAnnee(college, 2022);
                    if (collegeAnnuelOpt.isEmpty()) {
                        log.info(nom + " absent");
                        CollegeAnnuel collegeAnnuel = new CollegeAnnuel();
                        collegeAnnuel.setCollege(college);
                        collegeAnnuel.setIpsBonus((Integer)collegeMap.get("bonus"));
                        collegeAnnuel.setAnnee(2022);
                        collegeAnnuelRepository.save(collegeAnnuel);
                    }
                }
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    } */

            /* objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            URL url = new URL("file:fr-en-ips_colleges.json");
            Map<String, String>[] data = objectMapper.readValue(url, Map[].class);
            log.info(""+ data.length);
            for (Map<String,String> collegeMap : data) {
                String nom = collegeMap.get("nom_de_l_etablissment").replaceAll("COLLEGE ", "");
                College college = new College(collegeMap.get("uai"), nom);
                repository.save(college);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } */



}
