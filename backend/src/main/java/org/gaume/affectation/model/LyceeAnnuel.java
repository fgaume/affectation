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

    @ManyToOne
    private Lycee lycee;

    private Integer annee = 2022;

    private Integer effectif = 0;

    private Float ips = 0f;

    private Float scoreAdmission = 0f;

}
