
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface AssocierFraisALaPromotionService {
    AssocierFraisALaPromotion creerAssociationFrais(
            AssocierFraisALaPromotion associationFrais
    );

    List<AssocierFraisALaPromotion> listerAssociationsFrais();

    AssocierFraisALaPromotion rechercherAssociationFraisParId(
            Long idAssociationFrais
    );

    List<AssocierFraisALaPromotion> listerAssociationsParFrais(
            String idFrais
    );

    List<AssocierFraisALaPromotion> listerAssociationsParPromotion(
            String idPromo
    );

    List<AssocierFraisALaPromotion> listerAssociationsParAnneeAcademique(
            String idAa
    );

    List<AssocierFraisALaPromotion> listerAssociationsParPromotionEtAnnee(
            String idPromo,
            String idAa
    );

    AssocierFraisALaPromotion modifierAssociationFrais(
            Long idAssociationFrais,
            AssocierFraisALaPromotion associationFrais
    );

    void supprimerAssociationFrais(Long idAssociationFrais);
}
