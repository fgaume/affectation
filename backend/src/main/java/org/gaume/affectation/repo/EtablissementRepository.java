package org.gaume.affectation.repo;

import lombok.RequiredArgsConstructor;
import org.gaume.affectation.model.College;
import org.gaume.affectation.model.Etablissement;
import org.gaume.affectation.model.Lycee;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EtablissementRepository {

    private final CollegeRepository collegeRepository;

    private final LyceeRepository lyceeRepository;

    public void save(Etablissement etablissement, Etablissement.TypeEtablissement typeEtablissement) {
        if (typeEtablissement == Etablissement.TypeEtablissement.COLLEGE) {
            College college = College.builder()
                    .id(etablissement.getId())
                    .nom(etablissement.getNom())
                    .adresse(etablissement.getAdresse())
                    .codePostal(etablissement.getCodePostal())
                    .latitude(etablissement.getLatitude())
                    .longitude(etablissement.getLongitude())
                    .coordonneeX(etablissement.getCoordonneeX())
                    .coordonneeY(etablissement.getCoordonneeY())
                    .build();
            collegeRepository.save(college);
        }
        else {
            Lycee lycee = Lycee.builder()
                    .id(etablissement.getId())
                    .nom(etablissement.getNom())
                    .adresse(etablissement.getAdresse())
                    .codePostal(etablissement.getCodePostal())
                    .latitude(etablissement.getLatitude())
                    .longitude(etablissement.getLongitude())
                    .coordonneeX(etablissement.getCoordonneeX())
                    .coordonneeY(etablissement.getCoordonneeY())
                    .build();
            lyceeRepository.save(lycee);
        }
    }
}
