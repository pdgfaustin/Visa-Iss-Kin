
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.TypeCharge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface ChargeHoraireRepository extends JpaRepository<ChargeHoraire, Long> {
    boolean existsByEnseignantAndMaquettePromotionAndTypeCharge(
            Enseignant enseignant,
            MaquettePromotion maquettePromotion,
            TypeCharge typeCharge
    );

    Optional<ChargeHoraire>
            findByEnseignantAndMaquettePromotionAndTypeCharge(
                    Enseignant enseignant,
                    MaquettePromotion maquettePromotion,
                    TypeCharge typeCharge
            );

    List<ChargeHoraire> findByEnseignant(
            Enseignant enseignant
    );

    List<ChargeHoraire> findByMaquettePromotion(
            MaquettePromotion maquettePromotion
    );

    List<ChargeHoraire> findByTypeCharge(
            TypeCharge typeCharge
    );

    List<ChargeHoraire> findByEnseignantAndTypeCharge(
            Enseignant enseignant,
            TypeCharge typeCharge
    );

    List<ChargeHoraire> findByMaquettePromotionAndTypeCharge(
            MaquettePromotion maquettePromotion,
            TypeCharge typeCharge
    );
}
