package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.Etudiant;
import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.model.Promotion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface InscriptionRepository extends JpaRepository<Inscription, String> {
    boolean existsByEtudiantAndAnneeAcademique(Etudiant etud,AnneeAcademique aa);
    Optional<Inscription> findByEtudiantAndAnneeAcademique(Etudiant etud, AnneeAcademique aa);
    List<Inscription> findByEtudiant(Etudiant etud);
    List<Inscription> findByPromotion(Promotion promo);
    List<Inscription> findByAnneeAcademique(AnneeAcademique aa);
    List<Inscription> findByPromotionAndAnneeAcademique(Promotion promo, AnneeAcademique aa);
    Optional<Inscription> findTopByIdInscriptionStartingWithOrderByIdInscriptionDesc(String prefix);
}
