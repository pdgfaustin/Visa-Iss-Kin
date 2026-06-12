
package com.visa_iss_kin.service;

import com.visa_iss_kin.dto.ResumeFinancierInscriptionDto;
import com.visa_iss_kin.dto.SituationFinanciereEtudiantDto;
import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.model.PaiementFrais;
import com.visa_iss_kin.model.StatutPaiement;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<SituationFinanciereEtudiantDto> consulterSituationFinanciereParInscription(
            String idInscription
    );
    ResumeFinancierInscriptionDto consulterResumeFinancierParInscription(
            String idInscription
    );
    PaiementFrais modifierPaiementFrais(Long idPaiementFrais, PaiementFrais paiementFrais);

    PaiementFrais validerPaiementFrais(Long idPaiementFrais);

    PaiementFrais rejeterPaiementFrais(Long idPaiementFrais);

    PaiementFrais annulerPaiementFrais(Long idPaiementFrais);

    void supprimerPaiementFrais(Long idPaiementFrais);
    
}
