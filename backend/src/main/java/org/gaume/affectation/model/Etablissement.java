package org.gaume.affectation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Size;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Etablissement {

    public enum TypeEtablissement {COLLEGE, LYCEE}

    @Id
    @Column(length = 8)
    @Size(min = 8, max = 8)
    private String id;

    @Column(length = 128)
    @Size(max = 128)
    private String nom;

    @Column(length = 64)
    @Size(max = 64)
    private String adresse;

    @Column(length = 5)
    @Size(min = 5, max = 5)
    private String codePostal;

    private Double latitude;

    private Double longitude;

    @Column(name = "coordonnee_x")
    private Float coordonneeX;

    @Column(name = "coordonnee_y")
    private Float coordonneeY;

}
