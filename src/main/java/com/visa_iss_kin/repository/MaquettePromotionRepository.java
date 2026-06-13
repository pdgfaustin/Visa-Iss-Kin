
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.ElementConstitutif;
import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.model.Semestre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface MaquettePromotionRepository extends JpaRepository<MaquettePromotion, Long> {
    boolean existsByPromotionAndElementConstitutifAndAnneeAcademiqueAndSemestre(
            Promotion promotion,
            ElementConstitutif elementConstitutif,
            AnneeAcademique anneeAcademique,
            Semestre semestre
    );

    Optional<MaquettePromotion>
            findByPromotionAndElementConstitutifAndAnneeAcademiqueAndSemestre(
                    Promotion promotion,
                    ElementConstitutif elementConstitutif,
                    AnneeAcademique anneeAcademique,
                    Semestre semestre
            );

    List<MaquettePromotion> findByPromotion(
            Promotion promotion
    );

    List<MaquettePromotion> findByElementConstitutif(
            ElementConstitutif elementConstitutif
    );

    List<MaquettePromotion> findByAnneeAcademique(
            AnneeAcademique anneeAcademique
    );

    List<MaquettePromotion> findBySemestre(
            Semestre semestre
    );

    List<MaquettePromotion> findByPromotionAndAnneeAcademique(
            Promotion promotion,
            AnneeAcademique anneeAcademique
    );

    List<MaquettePromotion> findByPromotionAndAnneeAcademiqueAndSemestre(
            Promotion promotion,
            AnneeAcademique anneeAcademique,
            Semestre semestre
    );
}
