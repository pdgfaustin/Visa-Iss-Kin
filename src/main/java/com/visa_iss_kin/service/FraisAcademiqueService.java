package com.visa_iss_kin.service;

import com.visa_iss_kin.model.FraisAcademique;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface FraisAcademiqueService {
    FraisAcademique creerFraisAcademique(FraisAcademique fraisAcademique);

    List<FraisAcademique> listerFraisAcademiques();

    FraisAcademique rechercherFraisAcademiqueParId(String idFrais);

    List<FraisAcademique> rechercherFraisAcademiquesParLibelle(String libeFrais);

    FraisAcademique modifierFraisAcademique(String idFrais, FraisAcademique fraisAcademique);

    void supprimerFraisAcademique(String idFrais);
}
