package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EpreuveRepository extends JpaRepository<Epreuve, Long> {
    boolean existsBySessionEvaluationAndPromotionAndElementConstitutifAndTypeEpreuve(
            SessionEvaluation sessionEvaluation,
            Promotion promotion,
            ElementConstitutif elementConstitutif,
            TypeEpreuve typeEpreuve
    );

    Optional<Epreuve>
            findBySessionEvaluationAndPromotionAndElementConstitutifAndTypeEpreuve(
                    SessionEvaluation sessionEvaluation,
                    Promotion promotion,
                    ElementConstitutif elementConstitutif,
                    TypeEpreuve typeEpreuve
            );

    List<Epreuve> findBySessionEvaluation(
            SessionEvaluation sessionEvaluation
    );

    List<Epreuve> findByPromotion(
            Promotion promotion
    );

    List<Epreuve> findByElementConstitutif(
            ElementConstitutif elementConstitutif
    );

    List<Epreuve> findByTypeEpreuve(
            TypeEpreuve typeEpreuve
    );

    List<Epreuve> findByOuverte(
            Boolean ouverte
    );

    List<Epreuve> findBySessionEvaluationAndPromotion(
            SessionEvaluation sessionEvaluation,
            Promotion promotion
    );

    List<Epreuve> findBySessionEvaluationAndPromotionAndElementConstitutif(
            SessionEvaluation sessionEvaluation,
            Promotion promotion,
            ElementConstitutif elementConstitutif
    );

    List<Epreuve> findByPromotionAndElementConstitutif(
            Promotion promotion,
            ElementConstitutif elementConstitutif
    );

    List<Epreuve> findBySessionEvaluationAndOuverte(
            SessionEvaluation sessionEvaluation,
            Boolean ouverte
    );
}
