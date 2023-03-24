package org.gaume.affectation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
import org.gaume.affectation.model.CollegeAnnuel;
import org.gaume.affectation.repo.CollegeAnnuelRepository;
import org.gaume.affectation.repo.CollegeRepository;
import org.gaume.opendata.affelnet75.AffelnetCollege;
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
@RequiredArgsConstructor
public class CollegeAnnuelService extends BaseService {

    private final CollegeRepository collegeRepository;

    private final CollegeAnnuelRepository collegeAnnuelRepository;

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
            Optional<CollegeAnnuel> collegeAnnuelOptional = findCollegeAnnuel(data.identifiantEtablissement(), annee);
            if (collegeAnnuelOptional.isPresent()) {
                CollegeAnnuel collegeAnnuel = collegeAnnuelOptional.get();

                String tauxReussite = data.dnbTauxReussite()
                        .replaceFirst("%", "")
                        .replace(',', '.');
                collegeAnnuel.setDnbTauxReussite(Float.parseFloat(tauxReussite));

                collegeAnnuel.setDnbPresents(data.dnbPresents());
                collegeAnnuel.setDnbAdmis(data.dnbAdmis());
                collegeAnnuel.setDnbAdmisSansMention(data.dnbAdmisSansMention());
                collegeAnnuel.setDnbAdmisAssezBien(data.dnbAdmisAssezBien());
                collegeAnnuel.setDnbAdmisBien(data.dnbAdmisBien());
                collegeAnnuel.setDnbAdmisTresBien(data.dnbAdmisTresBien());
                collegeAnnuelRepository.save(collegeAnnuel);

                // on en profite pour charger les nom de college compatible arcgis
                College college = collegeAnnuel.getCollege();
                if (college.getNomAffelnet() == null) {
                    college.setNomAffelnet(data.patronyme().replaceAll("É", "E"));
                    collegeRepository.save(college);
                }
            }
            else {
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
            Optional<CollegeAnnuel> collegeAnnuelOptional = findCollegeAnnuel(data.identifiantEtablissement(), annee);
            if (collegeAnnuelOptional.isPresent()) {
                CollegeAnnuel collegeAnnuel = collegeAnnuelOptional.get();
                if (!Float.valueOf(data.ipsMoyenne()).equals(collegeAnnuel.getIpsMoyen()))
                    log.warn("Ecart d'IPS {} : {} {} {}",
                            annee,
                            collegeAnnuel.getCollege().getNom(),
                            data.ipsMoyenne(),
                            collegeAnnuel.getIpsMoyen());
                collegeAnnuel.setIpsMoyen(data.ipsMoyenne());
                collegeAnnuel.setIpsEcartType(data.ipsEcartType());
                collegeAnnuelRepository.save(collegeAnnuel);
            }
            else {
                log.error("[Import OpenData IPS] Collège inconnu : {} ", data.identifiantEtablissement());
            }
        }
    }
    private void importBonusIpsCollegeByAnnee(int annee) {
        try {
            List<AffelnetCollege> affelnetColleges = affelnetClient.fetchAffelnetColleges(annee);
            if (affelnetColleges != null) {
                log.info("[Import Affelnet75 affelnetColleges] : {} items", affelnetColleges.size());
                for (AffelnetCollege affelnetCollege : affelnetColleges) {
                    Optional<CollegeAnnuel> collegeAnnuelOptional = findCollegeAnnuel(affelnetCollege.collegeId(), annee);
                    if (collegeAnnuelOptional.isPresent()) {
                        CollegeAnnuel collegeAnnuel = collegeAnnuelOptional.get();
                        collegeAnnuel.setIpsMoyen(affelnetCollege.ipsMoyen());
                        collegeAnnuel.setIpsBonus(affelnetCollege.ipsBonus());
                        collegeAnnuelRepository.save(collegeAnnuel);
                    }
                    else {
                        log.error("[Import Affelnet75 affelnetColleges] Collège inconnu : {} ", affelnetCollege.collegeId());
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("[Import Affelnet75 affelnetColleges] pas d'infos de collèges pour l'année {} ", annee);
        }

    }

    private Optional<CollegeAnnuel> findCollegeAnnuel(String collegeId, int annee) {
        Optional<College> collegeOptional = collegeRepository.findById(collegeId);
        if (collegeOptional.isPresent()) {
            Optional<CollegeAnnuel>  collegeAnnuelOptional = collegeAnnuelRepository.findByCollegeAndAnnee(collegeOptional.get(), annee);
            if (collegeAnnuelOptional.isEmpty()) {
                collegeAnnuelOptional = Optional.of(new CollegeAnnuel(collegeOptional.get(), annee));
            }
            return collegeAnnuelOptional;
        }
        return Optional.empty();
    }


}
