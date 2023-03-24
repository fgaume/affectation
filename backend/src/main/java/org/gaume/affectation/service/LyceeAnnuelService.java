package org.gaume.affectation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.Lycee;
import org.gaume.affectation.model.LyceeAnnuel;
import org.gaume.affectation.repo.LyceeAnnuelRepository;
import org.gaume.affectation.repo.LyceeRepository;
import org.gaume.opendata.affelnet75.AffelnetLycee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LyceeAnnuelService extends BaseService {

    private final LyceeRepository lyceeRepository;

    private final LyceeAnnuelRepository lyceeAnnuelRepository;

    @Transactional
    public void importLyceesByAnnee(int annee) {
        try {
            List<AffelnetLycee> affelnetLycees = affelnetClient.fetchAffelnetLycees(annee);
            if (affelnetLycees != null) {
                log.info("[Import Affelnet75 affelnetLycees] : {} items", affelnetLycees.size());
                for (AffelnetLycee affelnetLycee : affelnetLycees) {
                    Optional<LyceeAnnuel> lyceeAnnuelOptional = findLyceeAnnuel(affelnetLycee.lyceeId(), annee);
                    if (lyceeAnnuelOptional.isPresent()) {
                        LyceeAnnuel lyceeAnnuel = lyceeAnnuelOptional.get();
                        lyceeAnnuel.setIpsMoyen(affelnetLycee.ipsMoyen());
                        lyceeAnnuel.setCapacite(affelnetLycee.capacite());
                        lyceeAnnuel.setScoreAdmission(affelnetLycee.scoreAdmission());
                        lyceeAnnuelRepository.save(lyceeAnnuel);
                    } else {
                        log.error("[Import Affelnet75 affelnetLycees] Lycée inconnu : {} ", affelnetLycee.lyceeId());
                    }
                }
            }
            else {
                log.error("[Import Affelnet75 affelnetLycees] pas d'indos de lycées pour l'année {} ", annee);
            }
        }
        catch (Exception e) {
            log.error("[Import Affelnet75 affelnetLycees] pas d'infos de lycées pour l'année {} ", annee);
        }

    }

    private Optional<LyceeAnnuel> findLyceeAnnuel(String lyceeId, int annee) {
        Optional<Lycee> lyceeOptional = lyceeRepository.findById(lyceeId);
        if (lyceeOptional.isPresent()) {
            Optional<LyceeAnnuel> optionalLyceeAnnuel = lyceeAnnuelRepository.findByLyceeAndAnnee(lyceeOptional.get(), annee);
            if (optionalLyceeAnnuel.isEmpty()) {
                optionalLyceeAnnuel = Optional.of(new LyceeAnnuel(lyceeOptional.get(), annee));
            }
            return optionalLyceeAnnuel;
        }
        return Optional.empty();
    }

}
