
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Banque;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface BanqueService {
    Banque creerBanque(Banque banque);

    List<Banque> listerBanques();

    Banque rechercherBanqueParId(String idBanque);

    List<Banque> rechercherBanquesParLibelle(String libeBanque);

    List<Banque> rechercherBanquesParSigle(String sigleBanque);

    Banque modifierBanque(String idBanque, Banque banque);

    void supprimerBanque(String idBanque);
}
