package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseEvaluationECService {
    SyntheseEvaluationEC genererSyntheseEvaluationEC(
            String idInscription,
            Long idSessionEvaluation,
            String idEC,
            String createdBy
    );

    List<SyntheseEvaluationEC> genererSynthesesParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation,
            String createdBy
    );

    List<SyntheseEvaluationEC> listerSynthesesEvaluationEC();

    SyntheseEvaluationEC rechercherSyntheseEvaluationECParId(
            Long idSyntheseEvaluationEC
    );

    SyntheseEvaluationEC rechercherSyntheseParInscriptionSessionEtEC(
            String idInscription,
            Long idSessionEvaluation,
            String idEC
    );

    List<SyntheseEvaluationEC> listerSynthesesParInscription(
            String idInscription
    );

    List<SyntheseEvaluationEC> listerSynthesesParSession(
            Long idSessionEvaluation
    );

    List<SyntheseEvaluationEC> listerSynthesesParElementConstitutif(
            String idEC
    );

    List<SyntheseEvaluationEC> listerSynthesesParStatutValidation(
            StatutValidationEC statutValidation
    );

    List<SyntheseEvaluationEC> listerSynthesesParPromotion(
            String idPromo
    );

    List<SyntheseEvaluationEC> listerSynthesesParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    );

    List<SyntheseEvaluationEC> listerSynthesesParSessionPromotionEtEC(
            Long idSessionEvaluation,
            String idPromo,
            String idEC
    );

    void supprimerSyntheseEvaluationEC(
            Long idSyntheseEvaluationEC
    );
}
