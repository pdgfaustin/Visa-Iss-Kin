
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.Semestre;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface MaquettePromotionService {
    MaquettePromotion creerMaquettePromotion(
            MaquettePromotion maquettePromotion
    );

    List<MaquettePromotion> listerMaquettesPromotion();

    MaquettePromotion rechercherMaquettePromotionParId(
            Long idMaquette
    );

    List<MaquettePromotion> listerMaquettesParPromotion(
            String idPromo
    );

    List<MaquettePromotion> listerMaquettesParElementConstitutif(
            String idEC
    );

    List<MaquettePromotion> listerMaquettesParAnneeAcademique(
            String idAa
    );

    List<MaquettePromotion> listerMaquettesParSemestre(
            Semestre semestre
    );

    List<MaquettePromotion> listerMaquettesParPromotionEtAnnee(
            String idPromo,
            String idAa
    );

    List<MaquettePromotion> listerMaquettesParPromotionAnneeEtSemestre(
            String idPromo,
            String idAa,
            Semestre semestre
    );

    MaquettePromotion modifierMaquettePromotion(
            Long idMaquette,
            MaquettePromotion maquettePromotion
    );

    void supprimerMaquettePromotion(Long idMaquette);
}
