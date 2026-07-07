package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.model.SyntheseUE;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseUERepository extends JpaRepository<SyntheseUE, Long> {
    boolean existsByInscriptionAndSessionEvaluationAndUniteEnseignement(
            Inscription inscription,
            SessionEvaluation sessionEvaluation,
            UniteEnseignement uniteEnseignement
    );

    Optional<SyntheseUE>
            findByInscriptionAndSessionEvaluationAndUniteEnseignement(
                    Inscription inscription,
                    SessionEvaluation sessionEvaluation,
                    UniteEnseignement uniteEnseignement
            );

    List<SyntheseUE> findByInscription(
            Inscription inscription
    );

    List<SyntheseUE> findBySessionEvaluation(
            SessionEvaluation sessionEvaluation
    );

    List<SyntheseUE> findByUniteEnseignement(
            UniteEnseignement uniteEnseignement
    );

    List<SyntheseUE> findByStatutValidationUE(
            StatutValidationUE statutValidationUE
    );

    List<SyntheseUE> findByInscriptionAndSessionEvaluation(
            Inscription inscription,
            SessionEvaluation sessionEvaluation
    );

    List<SyntheseUE>
            findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                    Long idSessionEvaluation,
                    String idPromo
            );

    List<SyntheseUE>
            findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromoAndUniteEnseignementIdUE(
                    Long idSessionEvaluation,
                    String idPromo,
                    String idUE
            );

    List<SyntheseUE> findByInscriptionPromotionIdPromo(
            String idPromo
    );
}
