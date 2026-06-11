
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.StatutCompte;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface NumeroCompteService {
    NumeroCompte creerNumeroCompte(NumeroCompte numeroBanque);

    List<NumeroCompte> listerNumerosCompte();

    NumeroCompte rechercherNumeroCompteParId(String idNumeroBanque);

    NumeroCompte rechercherNumeroCompteParNumeroCompte(String numeroCompte);

    List<NumeroCompte> listerNumerosParBanque(String idBanque);

    List<NumeroCompte> listerNumerosParDevise(Devise deviseCompte);

    List<NumeroCompte> listerNumerosParStatut(StatutCompte statutCompte);

    List<NumeroCompte> listerNumerosParBanqueEtDevise(String idBanque, Devise deviseCompte);

    List<NumeroCompte> listerNumerosParBanqueEtStatut(String idBanque, StatutCompte statutCompte);

    List<NumeroCompte> rechercherNumerosParIntitule(String intituleCompte);

    NumeroCompte modifierNumeroCompte(String idNumeroBanque, NumeroCompte numeroBanque);

    void supprimerNumeroCompte(String idNumeroBanque);
}
