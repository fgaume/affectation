package org.gaume.affectation.repo;

import org.gaume.affectation.model.College;
import org.gaume.affectation.model.Lycee;
import org.gaume.affectation.model.SecteurAnnuel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SecteurAnnuelRepository extends CrudRepository<SecteurAnnuel, Long> {
    List<SecteurAnnuel> findByCollegeAndAnneeAndSecteur(College college, int annee, int secteur);

    Optional<SecteurAnnuel> findByCollegeAndLyceeAndAnnee(College college, Lycee lycee, int annee);

    List<SecteurAnnuel> findByLycee(Lycee lycee);
}
