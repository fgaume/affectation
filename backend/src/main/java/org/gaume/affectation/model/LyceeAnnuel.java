package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueLyceeAnnee", columnNames = { "annee", "lycee_id" }) })

public class LyceeAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Lycee lycee;

    @NotNull
    private Integer annee;

    private Integer capacite = 0;

    private Float ipsMoyen = 0f;

    private Float scoreAdmission = 0f;

    public LyceeAnnuel(Lycee lycee, int annee) {
        this.lycee = lycee;
        this.annee = annee;
    }

}
