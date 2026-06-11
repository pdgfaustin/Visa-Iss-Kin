
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.model.FraisAcademique;
import com.visa_iss_kin.model.Promotion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface AssocierFraisALaPromotionRepository extends JpaRepository<AssocierFraisALaPromotion, Long> {
    boolean existsByFraisAcademiqueAndPromotionAndAnneeAcademique(
            FraisAcademique fraisAcademique,
            Promotion promotion,
            AnneeAcademique anneeAcademique
    );

    Optional<AssocierFraisALaPromotion> findByFraisAcademiqueAndPromotionAndAnneeAcademique(
            FraisAcademique fraisAcademique,
            Promotion promotion,
            AnneeAcademique anneeAcademique
    );

    List<AssocierFraisALaPromotion> findByFraisAcademique(
            FraisAcademique fraisAcademique
    );

    List<AssocierFraisALaPromotion> findByPromotion(
            Promotion promotion
    );

    List<AssocierFraisALaPromotion> findByAnneeAcademique(
            AnneeAcademique anneeAcademique
    );

    List<AssocierFraisALaPromotion> findByPromotionAndAnneeAcademique(
            Promotion promotion,
            AnneeAcademique anneeAcademique
    );
}
