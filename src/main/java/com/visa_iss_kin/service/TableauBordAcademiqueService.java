package com.visa_iss_kin.service;

import com.visa_iss_kin.model.TableauBordAcademique;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface TableauBordAcademiqueService {
    TableauBordAcademique genererTableauBordGlobal();

    TableauBordAcademique genererTableauBordParPromotion(
            String idPromo
    );

    TableauBordAcademique genererTableauBordParEnseignant(
            String matrEns
    );

    TableauBordAcademique genererTableauBordParAnneeAcademique(
            String idAa
    );
}
