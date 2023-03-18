package org.gaume.affectation.repo;

import org.gaume.affectation.model.Lycee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LyceeRepository extends CrudRepository<Lycee, String> {
    Optional<Lycee> findByNom(String name);
    //College findByUai(String uai);
}
