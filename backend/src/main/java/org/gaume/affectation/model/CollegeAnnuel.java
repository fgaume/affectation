package org.gaume.affectation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueCollegeAnnee", columnNames = { "annee", "college_id" }) })
public class CollegeAnnuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private College college;

    @NotNull
    private Integer annee = 2022;

    private Float ipsMoyen;

    private Float ipsEcartType;

    private Integer ipsBonus = 0;

    private Integer dnbPresents = 0;

    private Integer dnbAdmis = 0;

    private Integer dnbAdmisSansMention = 0;

    private Integer dnbAdmisAssezBien = 0;

    private Integer dnbAdmisBien = 0;

    private Integer dnbAdmisTresBien = 0;

    private Float dnbTauxReussite = 0f;

    public CollegeAnnuel(College college, int annee) {
        this.college = college;
        this.annee = annee;
    }

    /* public void updateFrom(CollegeAnnuel collegeAnnuel) {
        if (collegeAnnuel.ipsMoyen != null)
            this.ipsMoyen = collegeAnnuel.ipsMoyen;
        if (collegeAnnuel.ipsEcartType != null)
            this.ipsEcartType = collegeAnnuel.ipsEcartType;
        if (collegeAnnuel.ipsBonus != null)
            this.ipsBonus = collegeAnnuel.ipsBonus;
        if (collegeAnnuel.dnbPresents != null)
            this.dnbPresents = collegeAnnuel.dnbPresents;
        if (collegeAnnuel.dnbAdmis != null)
            this.dnbAdmis = collegeAnnuel.dnbAdmis;
        if (collegeAnnuel.dnbAdmisSansMention != null)
            this.dnbAdmisSansMention = collegeAnnuel.dnbAdmisSansMention;
        if (collegeAnnuel.dnbAdmisAssezBien != null)
            this.dnbAdmisAssezBien = collegeAnnuel.dnbAdmisAssezBien;
        if (collegeAnnuel.ipsBonus != null)
            this.ipsBonus = collegeAnnuel.ipsBonus;
        if (collegeAnnuel.dnbAdmisBien != null)
            this.dnbAdmisBien = collegeAnnuel.dnbAdmisBien;
        if (collegeAnnuel.dnbAdmisTresBien != null)
            this.dnbAdmisTresBien = collegeAnnuel.dnbAdmisTresBien;
        if (collegeAnnuel.dnbTauxReussite != null)
            this.dnbTauxReussite = collegeAnnuel.dnbTauxReussite;
    } */

}
