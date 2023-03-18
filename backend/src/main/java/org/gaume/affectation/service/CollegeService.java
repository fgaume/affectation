package org.gaume.affectation.service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gaume.affectation.model.College;
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
public class CollegeService {

    @Autowired
    private CollegeRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void importAllColleges() {
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            URL url = new URL("file:fr-en-ips_colleges.json");
            Map<String, String>[] data = objectMapper.readValue(url, Map[].class);
            log.info(""+ data.length);
            for (Map<String,String> collegeMap : data) {
                String nom = collegeMap.get("nom_de_l_etablissment").replaceAll("COLLEGE ", "");
                College college = new College(collegeMap.get("uai"), nom);
                repository.save(college);
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

    @Transactional
    public void saveColleges() {
        // save a few customers
        //repository.save(new College("0750465Y", "LUCIE ET RAYMOND AUBRAC"));
        //repository.save(new College("0750484U", "WOLFGANG AMADEUS MOZART"));

        // fetch all customers
        log.info("College found with findAll():");
        log.info("-------------------------------");
        for (College customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");

        // fetch an individual customer by ID
        Optional<College> customer = repository.findById("0750465Y");
        if (customer.isPresent()) {
            log.info("Customer found with findById(\"0750465Y\"):");
            log.info("--------------------------------");
            log.info(customer.get().toString());
        }

        Optional<College> college = repository.findByNom("WOLFGANG AMADEUS MOZART");
        if (college.isPresent()) {
            log.info("Customer found with findByNom(\"WOLFGANG AMADEUS MOZART\"):");
            log.info("--------------------------------");
            log.info(college.get().toString());
        }

    }
}
