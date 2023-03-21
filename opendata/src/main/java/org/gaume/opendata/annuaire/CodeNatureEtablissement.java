package org.gaume.opendata.annuaire;

public enum CodeNatureEtablissement {
    LGT("300"), LG("302"), LP("306"), COLLEGE("340") ;

    private String code;

    private CodeNatureEtablissement(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
