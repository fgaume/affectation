package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gaume.affectation.controller.SecteurController;

import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCollegeLyceeAnnee", columnNames = { "annee", "lycee_id", "college_id" }) })
public class SecteurAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Integer annee;

    @NotNull
    private Integer secteur = 0;

    @ManyToOne
    private College college;

    @ManyToOne
    private Lycee lycee;

    public SecteurAnnuel(College college, Lycee lycee, int annee) {
        this.college = college;
        this.lycee = lycee;
        this.annee = annee;
    }
}
