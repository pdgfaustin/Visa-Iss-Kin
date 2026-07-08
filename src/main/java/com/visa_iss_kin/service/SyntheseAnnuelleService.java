package com.visa_iss_kin.service;

import com.visa_iss_kin.model.StatutValidationAnnee;
import com.visa_iss_kin.model.SyntheseAnnuelle;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseAnnuelleService {
    SyntheseAnnuelle genererSyntheseAnnuelle(
            String idInscription,
            String idAa,
            String createdBy
    );

    List<SyntheseAnnuelle> genererSynthesesAnnuellesParAnneeEtPromotion(
            String idAa,
            String idPromo,
            String createdBy
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuelles();

    SyntheseAnnuelle rechercherSyntheseAnnuelleParId(
            Long idSyntheseAnnuelle
    );

    SyntheseAnnuelle rechercherSyntheseAnnuelleParInscriptionEtAnnee(
            String idInscription,
            String idAa
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParInscription(
            String idInscription
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParAnneeAcademique(
            String idAa
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParStatutValidation(
            StatutValidationAnnee statutValidationAnnee
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParPromotion(
            String idPromo
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParAnneeEtPromotion(
            String idAa,
            String idPromo
    );

    List<SyntheseAnnuelle> listerSynthesesAnnuellesParAnneeEtStatut(
            String idAa,
            StatutValidationAnnee statutValidationAnnee
    );

    void supprimerSyntheseAnnuelle(
            Long idSyntheseAnnuelle
    );
}
