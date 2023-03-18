package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCollegeAnnee", columnNames = { "annee", "college_id" }) })
public class CollegeAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private College college;

    private Integer annee = 2022;

    private Float ipsMoyen;

    private Float ipsEcartType;

    private Integer bonus = 0;


}
