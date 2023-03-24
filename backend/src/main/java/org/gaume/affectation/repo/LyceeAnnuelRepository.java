package org.gaume.affectation.repo;

import org.gaume.affectation.model.Lycee;
import org.gaume.affectation.model.LyceeAnnuel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LyceeAnnuelRepository extends CrudRepository<LyceeAnnuel, Long> {
    Optional<LyceeAnnuel> findByLyceeAndAnnee(Lycee lycee, int annee);
}
