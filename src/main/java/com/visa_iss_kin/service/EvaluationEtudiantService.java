package com.visa_iss_kin.service;

import com.visa_iss_kin.model.EvaluationEtudiant;
import java.util.*;
/**
 *
 * @author Faustin PADINGANYI
 */
public interface EvaluationEtudiantService {
    EvaluationEtudiant creerEvaluationEtudiant(
            EvaluationEtudiant evaluationEtudiant
    );

    List<EvaluationEtudiant> listerEvaluationsEtudiants();

    EvaluationEtudiant rechercherEvaluationEtudiantParId(
            Long idEvaluationEtudiant
    );

    EvaluationEtudiant rechercherEvaluationParEpreuveEtInscription(
            Long idEpreuve,
            String idInscription
    );

    List<EvaluationEtudiant> listerEvaluationsParEpreuve(
            Long idEpreuve
    );

    List<EvaluationEtudiant> listerEvaluationsParInscription(
            String idInscription
    );

    List<EvaluationEtudiant> listerEvaluationsParPromotion(
            String idPromo
    );

    List<EvaluationEtudiant> listerEvaluationsParElementConstitutif(
            String idEC
    );

    List<EvaluationEtudiant> listerEvaluationsParSession(
            Long idSessionEvaluation
    );

    List<EvaluationEtudiant> listerEvaluationsParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    );

    List<EvaluationEtudiant> listerEvaluationsParPromotionEtEC(
            String idPromo,
            String idEC
    );

    EvaluationEtudiant modifierEvaluationEtudiant(
            Long idEvaluationEtudiant,
            EvaluationEtudiant evaluationEtudiant
    );

    void supprimerEvaluationEtudiant(
            Long idEvaluationEtudiant
    );
}
