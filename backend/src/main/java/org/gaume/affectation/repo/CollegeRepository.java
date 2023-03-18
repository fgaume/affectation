package org.gaume.affectation.repo;

import org.gaume.affectation.model.College;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CollegeRepository extends CrudRepository<College, String> {
    Optional<College> findByNom(String name);
    //College findByUai(String uai);
}
