package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Inscription;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface InscriptionService {
    Inscription creerInscription(Inscription inscription);

    List<Inscription> listerInscriptions();

    Inscription rechercherInscriptionParId(String idInscription);

    List<Inscription> listerInscriptionsParEtudiant(String idEtudiant);

    List<Inscription> listerInscriptionsParPromotion(String idPromo);

    List<Inscription> listerInscriptionsParAnneeAcademique(String idAa);

    List<Inscription> listerInscriptionsParPromotionEtAnnee(String idPromo, String idAa);

    Inscription modifierInscription(String idInscription, Inscription inscription);

    Inscription validerInscription(String idInscription);

    Inscription annulerInscription(String idInscription);

    void supprimerInscription(String idInscription);
}
