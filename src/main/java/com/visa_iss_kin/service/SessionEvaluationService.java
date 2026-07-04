
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SessionEvaluationService {
    SessionEvaluation creerSessionEvaluation(
            SessionEvaluation sessionEvaluation
    );

    List<SessionEvaluation> listerSessionsEvaluations();

    SessionEvaluation rechercherSessionEvaluationParId(
            Long idSessionEvaluation
    );

    List<SessionEvaluation> listerSessionsParAnneeAcademique(
            String idAa
    );

    List<SessionEvaluation> listerSessionsParSemestre(
            Semestre semestre
    );

    List<SessionEvaluation> listerSessionsParTypeSession(
            TypeSession typeSession
    );

    List<SessionEvaluation> listerSessionsOuvertes();

    List<SessionEvaluation> listerSessionsOuvertesParAnneeAcademique(
            String idAa
    );

    SessionEvaluation rechercherSessionParAnneeSemestreEtType(
            String idAa,
            Semestre semestre,
            TypeSession typeSession
    );

    SessionEvaluation modifierSessionEvaluation(
            Long idSessionEvaluation,
            SessionEvaluation sessionEvaluation
    );

    SessionEvaluation ouvrirSessionEvaluation(
            Long idSessionEvaluation
    );

    SessionEvaluation fermerSessionEvaluation(
            Long idSessionEvaluation
    );

    void supprimerSessionEvaluation(
            Long idSessionEvaluation
    );
}
