package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseSemestreRepository extends JpaRepository<SyntheseSemestre, Long> {
    boolean existsByInscriptionAndSessionEvaluation(
            Inscription inscription,
            SessionEvaluation sessionEvaluation
    );

    Optional<SyntheseSemestre> findByInscriptionAndSessionEvaluation(
            Inscription inscription,
            SessionEvaluation sessionEvaluation
    );

    List<SyntheseSemestre> findByInscription(
            Inscription inscription
    );

    List<SyntheseSemestre> findBySessionEvaluation(
            SessionEvaluation sessionEvaluation
    );

    List<SyntheseSemestre> findByStatutValidationSemestre(
            StatutValidationSemestre statutValidationSemestre
    );

    List<SyntheseSemestre> findByInscriptionPromotionIdPromo(
            String idPromo
    );

    List<SyntheseSemestre>
            findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                    Long idSessionEvaluation,
                    String idPromo
            );

    List<SyntheseSemestre>
            findBySessionEvaluationIdSessionEvaluationAndStatutValidationSemestre(
                    Long idSessionEvaluation,
                    StatutValidationSemestre statutValidationSemestre
            );

    List<SyntheseSemestre>
            findByInscriptionAnneeAcademiqueIdAa(
                    String idAa
            );

    List<SyntheseSemestre>
            findBySessionEvaluationAnneeAcademiqueIdAa(
                    String idAa
            );
}
