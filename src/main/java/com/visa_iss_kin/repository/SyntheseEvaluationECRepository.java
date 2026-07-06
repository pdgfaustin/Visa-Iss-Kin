package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseEvaluationECRepository extends JpaRepository<SyntheseEvaluationEC, Long> {

    boolean existsByInscriptionAndSessionEvaluationAndElementConstitutif(
            Inscription inscription,
            SessionEvaluation sessionEvaluation,
            ElementConstitutif elementConstitutif
    );

    Optional<SyntheseEvaluationEC>
            findByInscriptionAndSessionEvaluationAndElementConstitutif(
                    Inscription inscription,
                    SessionEvaluation sessionEvaluation,
                    ElementConstitutif elementConstitutif
            );

    List<SyntheseEvaluationEC> findByInscription(
            Inscription inscription
    );

    List<SyntheseEvaluationEC> findBySessionEvaluation(
            SessionEvaluation sessionEvaluation
    );

    List<SyntheseEvaluationEC> findByElementConstitutif(
            ElementConstitutif elementConstitutif
    );

    List<SyntheseEvaluationEC> findByStatutValidation(
            StatutValidationEC statutValidation
    );

    List<SyntheseEvaluationEC>
            findBySessionEvaluationAndElementConstitutif(
                    SessionEvaluation sessionEvaluation,
                    ElementConstitutif elementConstitutif
            );

    List<SyntheseEvaluationEC>
            findByInscriptionAndSessionEvaluation(
                    Inscription inscription,
                    SessionEvaluation sessionEvaluation
            );

    List<SyntheseEvaluationEC>
            findByInscriptionPromotionIdPromo(
                    String idPromo
            );

    List<SyntheseEvaluationEC>
            findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                    Long idSessionEvaluation,
                    String idPromo
            );

    List<SyntheseEvaluationEC>
            findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromoAndElementConstitutifIdEC(
                    Long idSessionEvaluation,
                    String idPromo,
                    String idEC
            );
}
