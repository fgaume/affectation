package org.gaume.affectation.repo;

import org.gaume.affectation.model.College;
import org.gaume.affectation.model.CollegeAnnuel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CollegeAnnuelRepository extends CrudRepository<CollegeAnnuel, Long> {
    Optional<CollegeAnnuel> findByCollegeAndAnnee(College college, int annee);

    List<CollegeAnnuel> findByAnneeAndIpsBonus(int annee, int ipsBonus);
}
