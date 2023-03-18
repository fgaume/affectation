package org.gaume.affectation.service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.Lycee;
import org.gaume.affectation.model.LyceeAnnuel;
import org.gaume.affectation.repo.LyceeAnnuelRepository;
import org.gaume.affectation.repo.LyceeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LyceeService {

    @Autowired
    private LyceeRepository lyceeRepository;

    @Autowired
    private LyceeAnnuelRepository lyceeAnnuelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void importAllLycees() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            URL url = new URL("file:lycee.json");
            /* Lycee[] lycees = objectMapper.readValue(url, Lycee[].class);
            log.info(""+ lycees.length);
            for (Lycee lycee : lycees) {
                repository.save(lycee);
            } */

            Map<String,Object>[] lycees = objectMapper.readValue(url, Map[].class);
            for (Map<String,Object> lyceeMap : lycees) {
                //log.info(lyceeMap.toString());
                String id = (String)lyceeMap.get("id");
                Lycee lycee = lyceeRepository.findById(id).get();
                List<Object> seuils = (List<Object>)lyceeMap.get("seuils");
                log.info("{} {} {}", id, seuils.get(0), seuils.get(1));
                String seuil2021 = String.valueOf(seuils.get(0));
                float seuil2021float = Float.parseFloat(seuil2021);
                String seuil2022 = String.valueOf(seuils.get(1));
                float seuil2022float = Float.parseFloat(seuil2022);

                LyceeAnnuel lyceeAnnuel2021 = new LyceeAnnuel();
                lyceeAnnuel2021.setAnnee(2021);
                lyceeAnnuel2021.setLycee(lycee);
                lyceeAnnuel2021.setScoreAdmission(seuil2021float);
                lyceeAnnuelRepository.save(lyceeAnnuel2021);

                LyceeAnnuel lyceeAnnuel2022 = new LyceeAnnuel();
                lyceeAnnuel2022.setAnnee(2022);
                lyceeAnnuel2022.setLycee(lycee);
                lyceeAnnuel2022.setScoreAdmission(seuil2022float);
                lyceeAnnuelRepository.save(lyceeAnnuel2022);
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
