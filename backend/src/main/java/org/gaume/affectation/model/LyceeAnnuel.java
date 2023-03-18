package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueLyceeAnnee", columnNames = { "annee", "lycee_id" }) })

public class LyceeAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int effectif = 0;

    private float ips = 0f;

    private int annee = 2022;

    private float scoreAdmission = 0f;

    @ManyToOne
    private Lycee lycee;
}
