package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCollegeLyceeAnnee", columnNames = { "annee", "lycee_id", "college_id" }) })
public class SecteurAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer annee = 2022;

    private Integer secteur = 1;

    @ManyToOne
    private College college;

    @ManyToOne
    private Lycee lycee;
}
