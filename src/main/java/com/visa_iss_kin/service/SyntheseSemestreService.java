package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.*; 
/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseSemestreService {
    SyntheseSemestre genererSyntheseSemestre(
            String idInscription,
            Long idSessionEvaluation,
            String createdBy
    );

    List<SyntheseSemestre> genererSynthesesSemestreParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo,
            String createdBy
    );

    List<SyntheseSemestre> listerSynthesesSemestre();

    SyntheseSemestre rechercherSyntheseSemestreParId(
            Long idSyntheseSemestre
    );

    SyntheseSemestre rechercherSyntheseSemestreParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation
    );

    List<SyntheseSemestre> listerSynthesesSemestreParInscription(
            String idInscription
    );

    List<SyntheseSemestre> listerSynthesesSemestreParSession(
            Long idSessionEvaluation
    );

    List<SyntheseSemestre> listerSynthesesSemestreParStatutValidation(
            StatutValidationSemestre statutValidationSemestre
    );

    List<SyntheseSemestre> listerSynthesesSemestreParPromotion(
            String idPromo
    );

    List<SyntheseSemestre> listerSynthesesSemestreParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    );

    List<SyntheseSemestre> listerSynthesesSemestreParSessionEtStatut(
            Long idSessionEvaluation,
            StatutValidationSemestre statutValidationSemestre
    );

    List<SyntheseSemestre> listerSynthesesSemestreParAnneeAcademiqueInscription(
            String idAa
    );

    List<SyntheseSemestre> listerSynthesesSemestreParAnneeAcademiqueSession(
            String idAa
    );

    void supprimerSyntheseSemestre(
            Long idSyntheseSemestre
    );
}
