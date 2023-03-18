package org.gaume.affectation.repo;

import org.gaume.affectation.model.College;
import org.gaume.affectation.model.SecteurAnnuel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SecteurAnnuelRepository extends CrudRepository<SecteurAnnuel, Long> {
    //Optional<College> findByNom(String name);
    //College findByUai(String uai);
    //Optional<LyceeAnnuel> findByLyceeAndAnnee(Lycee lycee, int annee);

    List<SecteurAnnuel> findByCollegeAndAnneeAndSecteur(College college, int annee, int secteur);
}
