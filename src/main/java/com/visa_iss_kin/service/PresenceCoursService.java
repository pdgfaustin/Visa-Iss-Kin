
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.PresenceCours;
import com.visa_iss_kin.model.StatutExecutionCours;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PresenceCoursService {
    PresenceCours creerPresenceCours(
            PresenceCours presenceCours
    );

    List<PresenceCours> listerPresencesCours();

    PresenceCours rechercherPresenceCoursParId(
            Long idPresenceCours
    );

    PresenceCours rechercherPresenceParHoraire(
            Long idHoraireCours
    );

    List<PresenceCours> listerPresencesParStatut(
            StatutExecutionCours statutExecution
    );

    List<PresenceCours> listerPresencesParChargeHoraire(
            Long idChargeHoraire
    );

    List<PresenceCours> listerPresencesParEnseignant(
            String matrEns
    );

    List<PresenceCours> listerPresencesParPromotion(
            String idPromo
    );

    PresenceCours modifierPresenceCours(
            Long idPresenceCours,
            PresenceCours presenceCours
    );

    PresenceCours changerStatutExecution(
            Long idPresenceCours,
            StatutExecutionCours statutExecution
    );

    void supprimerPresenceCours(
            Long idPresenceCours
    );
}
