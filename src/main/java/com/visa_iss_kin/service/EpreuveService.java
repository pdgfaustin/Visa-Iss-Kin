package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EpreuveService {
    Epreuve creerEpreuve(
            Epreuve epreuve
    );

    List<Epreuve> listerEpreuves();

    Epreuve rechercherEpreuveParId(
            Long idEpreuve
    );

    List<Epreuve> listerEpreuvesParSession(
            Long idSessionEvaluation
    );

    List<Epreuve> listerEpreuvesParPromotion(
            String idPromo
    );

    List<Epreuve> listerEpreuvesParElementConstitutif(
            String idEC
    );

    List<Epreuve> listerEpreuvesParType(
            TypeEpreuve typeEpreuve
    );

    List<Epreuve> listerEpreuvesOuvertes();

    List<Epreuve> listerEpreuvesParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    );

    List<Epreuve> listerEpreuvesParSessionPromotionEtEC(
            Long idSessionEvaluation,
            String idPromo,
            String idEC
    );

    List<Epreuve> listerEpreuvesParPromotionEtEC(
            String idPromo,
            String idEC
    );

    List<Epreuve> listerEpreuvesOuvertesParSession(
            Long idSessionEvaluation
    );

    Epreuve modifierEpreuve(
            Long idEpreuve,
            Epreuve epreuve
    );

    Epreuve ouvrirEpreuve(
            Long idEpreuve
    );

    Epreuve fermerEpreuve(
            Long idEpreuve
    );

    void supprimerEpreuve(
            Long idEpreuve
    );
}
