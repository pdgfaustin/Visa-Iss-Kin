package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface DeliberationService {
    Deliberation genererDeliberation(
            Long idSyntheseAnnuelle,
            String createdBy
    );

    List<Deliberation> genererDeliberationsParAnneeEtPromotion(
            String idAa,
            String idPromo,
            String createdBy
    );

    Deliberation validerDeliberation(
            Long idDeliberation,
            String validatedBy,
            String observationJury
    );

    Deliberation modifierDecisionDeliberation(
            Long idDeliberation,
            DecisionDeliberation decisionDeliberation,
            String motifDecision,
            String observationJury
    );

    List<Deliberation> listerDeliberations();

    Deliberation rechercherDeliberationParId(
            Long idDeliberation
    );

    Deliberation rechercherDeliberationParSyntheseAnnuelle(
            Long idSyntheseAnnuelle
    );

    List<Deliberation> listerDeliberationsParInscription(
            String idInscription
    );

    List<Deliberation> listerDeliberationsParAnneeAcademique(
            String idAa
    );

    List<Deliberation> listerDeliberationsParDecision(
            DecisionDeliberation decisionDeliberation
    );

    List<Deliberation> listerDeliberationsParValidation(
            Boolean validee
    );

    List<Deliberation> listerDeliberationsParPromotion(
            String idPromo
    );

    List<Deliberation> listerDeliberationsParAnneeEtPromotion(
            String idAa,
            String idPromo
    );

    List<Deliberation> listerDeliberationsParAnneeEtDecision(
            String idAa,
            DecisionDeliberation decisionDeliberation
    );

    List<Deliberation> listerDeliberationsParAnneeEtValidation(
            String idAa,
            Boolean validee
    );

    void supprimerDeliberation(
            Long idDeliberation
    );
}
