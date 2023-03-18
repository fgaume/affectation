package org.gaume.affectation.service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
import org.gaume.affectation.model.CollegeAnnuel;
import org.gaume.affectation.repo.CollegeAnnuelRepository;
import org.gaume.affectation.repo.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CollegeAnnuelService {

    @Autowired
    private CollegeAnnuelRepository collegeAnnuelRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void importIPS2022() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            URL url = new URL("file:colleges.json");
            Map<String, Object>[] data = objectMapper.readValue(url, Map[].class);
            log.info(""+ data.length);
            for (Map<String,Object> collegeMap : data) {
                Object nom = collegeMap.get("nom");
                Optional<College> collegeOptional = collegeRepository.findByNom(nom.toString());
                if (collegeOptional.isEmpty()) {
                    log.error("college absent : " + nom);
                }
                else {
                    College college = collegeOptional.get();
                    Optional<CollegeAnnuel> collegeAnnuelOpt = collegeAnnuelRepository.findByCollegeAndAnnee(college, 2022);
                    if (collegeAnnuelOpt.isEmpty()) {
                        log.info(nom + " absent");
                        CollegeAnnuel collegeAnnuel = new CollegeAnnuel();
                        collegeAnnuel.setCollege(college);
                        collegeAnnuel.setBonus((Integer)collegeMap.get("bonus"));
                        collegeAnnuel.setAnnee(2022);
                        collegeAnnuelRepository.save(collegeAnnuel);
                    }
                }
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
