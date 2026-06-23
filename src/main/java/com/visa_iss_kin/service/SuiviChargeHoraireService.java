package com.visa_iss_kin.service;

import com.visa_iss_kin.model.SuiviChargeHoraire;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SuiviChargeHoraireService {
    SuiviChargeHoraire calculerSuiviParChargeHoraire(
            Long idChargeHoraire
    );

    List<SuiviChargeHoraire> listerSuivisChargesHoraires();

    List<SuiviChargeHoraire> listerSuivisParEnseignant(
            String matrEns
    );

    List<SuiviChargeHoraire> listerSuivisParPromotion(
            String idPromo
    );

    List<SuiviChargeHoraire> listerSuivisParMaquette(
            Long idMaquette
    );
}
