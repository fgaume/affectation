package org.gaume.opendata;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.gaume.opendata.annuaire.Annuaire;
import org.gaume.opendata.dnb.Brevet;
import org.gaume.opendata.ips.PositionSociale;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "opendata", url = "https://data.education.gouv.fr/api/records/1.0/search")
public interface OpenDataClient {

    // code_nature LG = 302, LGT : 300, POL = 306, collage = 340
    @RequestLine("GET /?dataset=fr-en-annuaire-education&rows=150&facet=identifiant_de_l_etablissement&facet=nom_etablissement&facet=code_postal&facet=code_commune&facet=pial&refine.statut_public_prive=Public&refine.code_academie=01&refine.code_nature={codeNature}")
    @Headers("Content-Type: application/json")
    Annuaire fetchAnnuaire(@Param String codeNature);

    @RequestLine("GET /?dataset=fr-en-dnb-par-etablissement&rows=150&facet=session&facet=numero_d_etablissement&refine.session={annee}&refine.secteur_d_enseignement=PUBLIC&refine.academie=01&refine.denomination_principale=COLLEGE")
    @Headers("Content-Type: application/json")
    Brevet fetchBrevet(@Param String annee);

    @RequestLine("GET /?dataset=fr-en-ips_colleges&rows=150&facet=uai&refine.secteur=public&refine.rentree_scolaire={anneeScolaire}&refine.code_du_departement=075")
    @Headers("Content-Type: application/json")
    PositionSociale fetchIps(@Param String anneeScolaire);
}
