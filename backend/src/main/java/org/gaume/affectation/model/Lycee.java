package org.gaume.affectation.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Data
public class Lycee extends Etablissement {
    private Boolean tousSecteurs = false;
}
