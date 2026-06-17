
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.GradeEnseignant;
import com.visa_iss_kin.model.PromotionEnseignant;
import com.visa_iss_kin.model.StatutPromotionEnseignant;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PromotionEnseignantService {
    PromotionEnseignant creerPromotionEnseignant(
            PromotionEnseignant promotionEnseignant
    );

    List<PromotionEnseignant> listerPromotionsEnseignants();

    PromotionEnseignant rechercherPromotionEnseignantParId(
            Long idPromotionEnseignant
    );

    List<PromotionEnseignant> listerHistoriqueParEnseignant(
            String matrEns
    );

    List<PromotionEnseignant> listerPromotionsParGrade(
            GradeEnseignant nouveauGrade
    );

    List<PromotionEnseignant> listerPromotionsParStatut(
            StatutPromotionEnseignant statutPromotion
    );

    List<PromotionEnseignant> listerPromotionsParEnseignantEtStatut(
            String matrEns,
            StatutPromotionEnseignant statutPromotion
    );

    PromotionEnseignant rechercherGradeActuelEnseignant(
            String matrEns
    );

    PromotionEnseignant modifierPromotionEnseignant(
            Long idPromotionEnseignant,
            PromotionEnseignant promotionEnseignant
    );

    PromotionEnseignant changerStatutPromotion(
            Long idPromotionEnseignant,
            StatutPromotionEnseignant statutPromotion
    );

    void supprimerPromotionEnseignant(
            Long idPromotionEnseignant
    );
}
