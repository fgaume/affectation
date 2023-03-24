package org.gaume.affectation.service;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.Etablissement;
import org.gaume.affectation.repo.EtablissementRepository;
import org.gaume.opendata.OpenDataClient;
import org.gaume.opendata.annuaire.Annuaire;
import org.gaume.opendata.annuaire.AnnuaireFields;
import org.gaume.opendata.annuaire.CodeNatureEtablissement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
@Slf4j
public class EtablissementService extends BaseService {

    private final EtablissementRepository etablissementRepository;

    public EtablissementService(EtablissementRepository etablissementRepository) {
        this.etablissementRepository = etablissementRepository;
    }

    private void importEtablissements(CodeNatureEtablissement codeNatureEtablissement) {

        Annuaire annuaire = openDataClient.fetchAnnuaire(codeNatureEtablissement.getCode());
        log.info("[Import OpenData Annuaire] : {} items {}",
                annuaire.nhits(),
                codeNatureEtablissement);
        annuaire.records().forEach((item) -> {
            AnnuaireFields data = item.fields();
            Etablissement etablissement = Etablissement.builder()
                    .id(data.identifiantEtablissement())
                    .nom(data.nomEtablissement())
                    .adresse(data.adresse())
                    .codePostal(data.codePostal())
                    .latitude(data.latitude())
                    .longitude(data.longitude())
                    .coordonneeX(data.coordonneeX())
                    .coordonneeY(data.coordonneeY())
                    .build();
            etablissementRepository.save(etablissement,
                    (codeNatureEtablissement == CodeNatureEtablissement.COLLEGE) ?
                            Etablissement.TypeEtablissement.COLLEGE :
                            Etablissement.TypeEtablissement.LYCEE);
        });
    }

    @Transactional
    public void importEtablissements() {
        EnumSet.allOf(CodeNatureEtablissement.class).forEach(this::importEtablissements);
    }
}
