
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.TypeCharge;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface ChargeHoraireService {
    ChargeHoraire creerChargeHoraire(
            ChargeHoraire chargeHoraire
    );

    List<ChargeHoraire> listerChargesHoraires();

    ChargeHoraire rechercherChargeHoraireParId(
            Long idChargeHoraire
    );

    List<ChargeHoraire> listerChargesParEnseignant(
            String matrEns
    );

    List<ChargeHoraire> listerChargesParMaquette(
            Long idMaquette
    );

    List<ChargeHoraire> listerChargesParType(
            TypeCharge typeCharge
    );

    List<ChargeHoraire> listerChargesParEnseignantEtType(
            String matrEns,
            TypeCharge typeCharge
    );

    List<ChargeHoraire> listerChargesParMaquetteEtType(
            Long idMaquette,
            TypeCharge typeCharge
    );

    ChargeHoraire modifierChargeHoraire(
            Long idChargeHoraire,
            ChargeHoraire chargeHoraire
    );

    void supprimerChargeHoraire(
            Long idChargeHoraire
    );
}
