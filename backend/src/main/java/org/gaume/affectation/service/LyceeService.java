package org.gaume.affectation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
import org.gaume.affectation.model.Lycee;
import org.gaume.affectation.model.LyceeAnnuel;
import org.gaume.affectation.model.SecteurAnnuel;
import org.gaume.affectation.repo.CollegeRepository;
import org.gaume.affectation.repo.LyceeAnnuelRepository;
import org.gaume.affectation.repo.LyceeRepository;
import org.gaume.affectation.repo.SecteurAnnuelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LyceeService extends BaseService {

    private final CollegeRepository collegeRepository;
    private final LyceeRepository lyceeRepository;
    private final LyceeAnnuelRepository lyceeAnnuelRepository;
    private final SecteurAnnuelRepository secteurAnnuelRepository;

    @Transactional
    public void cleanLycees() {
        Iterable<Lycee> lycees = lyceeRepository.findAll();
        lycees.forEach(lycee -> {
            Optional<LyceeAnnuel> optionalLyceeAnnuel = lyceeAnnuelRepository.findByLyceeAndAnnee(lycee, 2022);
            if (optionalLyceeAnnuel.isEmpty()) {
                log.info("suppression de {}", lycee.getNom());
                lyceeRepository.delete(lycee);
            }
            List<SecteurAnnuel> secteurAnnuelList = secteurAnnuelRepository.findByLycee(lycee);
            if (secteurAnnuelList.isEmpty()) {
                log.info("lycee tous secteurs : {}", lycee);
                lycee.setTousSecteurs(true);
                lyceeRepository.save(lycee);
            }
        });
    }

}
