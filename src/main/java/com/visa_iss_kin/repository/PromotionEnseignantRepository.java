
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.GradeEnseignant;
import com.visa_iss_kin.model.PromotionEnseignant;
import com.visa_iss_kin.model.StatutPromotionEnseignant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PromotionEnseignantRepository extends JpaRepository<PromotionEnseignant, Long> {
    List<PromotionEnseignant> findByEnseignant(
            Enseignant enseignant
    );

    List<PromotionEnseignant> findByEnseignantOrderByDatePromotionAsc(
            Enseignant enseignant
    );

    List<PromotionEnseignant> findByEnseignantOrderByDatePromotionDesc(
            Enseignant enseignant
    );

    List<PromotionEnseignant> findByNouveauGrade(
            GradeEnseignant nouveauGrade
    );

    List<PromotionEnseignant> findByStatutPromotion(
            StatutPromotionEnseignant statutPromotion
    );

    List<PromotionEnseignant> findByEnseignantAndStatutPromotion(
            Enseignant enseignant,
            StatutPromotionEnseignant statutPromotion
    );

    Optional<PromotionEnseignant>
            findTopByEnseignantAndStatutPromotionOrderByDatePromotionDesc(
                    Enseignant enseignant,
                    StatutPromotionEnseignant statutPromotion
            );

    boolean existsByNumeroDecision(String numeroDecision);
}
