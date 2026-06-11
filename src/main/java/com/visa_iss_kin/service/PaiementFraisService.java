
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.PaiementFrais;
import com.visa_iss_kin.model.StatutPaiement;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PaiementFraisService {
    PaiementFrais creerPaiementFrais(PaiementFrais paiementFrais);

    List<PaiementFrais> listerPaiementsFrais();

    PaiementFrais rechercherPaiementFraisParId(Long idPaiementFrais);

    PaiementFrais rechercherPaiementFraisParReference(String referencePaiement);

    List<PaiementFrais> listerPaiementsParInscription(String idInscription);

    List<PaiementFrais> listerPaiementsParAssociationFrais(Long idAssociationFrais);

    List<PaiementFrais> listerPaiementsParNumeroBanque(String idNumeroBanque);

    List<PaiementFrais> listerPaiementsParStatut(StatutPaiement statutPaiement);

    List<PaiementFrais> listerPaiementsParDevise(Devise devisePaiement);

    List<PaiementFrais> listerPaiementsParInscriptionEtAssociationFrais(
            String idInscription,
            Long idAssociationFrais
    );

    List<PaiementFrais> listerPaiementsValidesParInscription(String idInscription);

    PaiementFrais modifierPaiementFrais(Long idPaiementFrais, PaiementFrais paiementFrais);

    PaiementFrais validerPaiementFrais(Long idPaiementFrais);

    PaiementFrais rejeterPaiementFrais(Long idPaiementFrais);

    PaiementFrais annulerPaiementFrais(Long idPaiementFrais);

    void supprimerPaiementFrais(Long idPaiementFrais);
}
