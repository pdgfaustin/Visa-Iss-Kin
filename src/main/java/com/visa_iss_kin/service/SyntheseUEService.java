package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseUEService {
    SyntheseUE genererSyntheseUE(
            String idInscription,
            Long idSessionEvaluation,
            String idUE,
            String createdBy
    );

    List<SyntheseUE> genererSynthesesUEParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation,
            String createdBy
    );

    List<SyntheseUE> listerSynthesesUE();

    SyntheseUE rechercherSyntheseUEParId(
            Long idSyntheseUE
    );

    SyntheseUE rechercherSyntheseUEParInscriptionSessionEtUE(
            String idInscription,
            Long idSessionEvaluation,
            String idUE
    );

    List<SyntheseUE> listerSynthesesUEParInscription(
            String idInscription
    );

    List<SyntheseUE> listerSynthesesUEParSession(
            Long idSessionEvaluation
    );

    List<SyntheseUE> listerSynthesesUEParUniteEnseignement(
            String idUE
    );

    List<SyntheseUE> listerSynthesesUEParStatutValidation(
            StatutValidationUE statutValidationUE
    );

    List<SyntheseUE> listerSynthesesUEParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation
    );

    List<SyntheseUE> listerSynthesesUEParPromotion(
            String idPromo
    );

    List<SyntheseUE> listerSynthesesUEParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    );

    List<SyntheseUE> listerSynthesesUEParSessionPromotionEtUE(
            Long idSessionEvaluation,
            String idPromo,
            String idUE
    );

    void supprimerSyntheseUE(
            Long idSyntheseUE
    );
}
