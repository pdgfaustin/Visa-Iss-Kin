package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EvaluationEtudiantRepository extends JpaRepository<EvaluationEtudiant, Long> {
    boolean existsByEpreuveAndInscription(
            Epreuve epreuve,
            Inscription inscription
    );

    Optional<EvaluationEtudiant> findByEpreuveAndInscription(
            Epreuve epreuve,
            Inscription inscription
    );

    List<EvaluationEtudiant> findByEpreuve(
            Epreuve epreuve
    );

    List<EvaluationEtudiant> findByInscription(
            Inscription inscription
    );

    List<EvaluationEtudiant> findByEpreuvePromotionIdPromo(
            String idPromo
    );

    List<EvaluationEtudiant> findByEpreuveElementConstitutifIdEC(
            String idEC
    );

    List<EvaluationEtudiant> findByEpreuveSessionEvaluationIdSessionEvaluation(
            Long idSessionEvaluation
    );

    List<EvaluationEtudiant>
            findByEpreuveSessionEvaluationIdSessionEvaluationAndEpreuvePromotionIdPromo(
                    Long idSessionEvaluation,
                    String idPromo
            );

    List<EvaluationEtudiant>
            findByEpreuvePromotionIdPromoAndEpreuveElementConstitutifIdEC(
                    String idPromo,
                    String idEC
            );
}
