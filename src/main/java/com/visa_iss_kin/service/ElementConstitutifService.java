
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.ElementConstitutif;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface ElementConstitutifService {
    ElementConstitutif creerElementConstitutif(
            ElementConstitutif elementConstitutif
    );

    List<ElementConstitutif> listerElementsConstitutifs();

    ElementConstitutif rechercherElementConstitutifParId(String idEC);

    ElementConstitutif rechercherElementConstitutifParCode(String codeEC);

    List<ElementConstitutif> rechercherElementsConstitutifsParLibelle(
            String libeEC
    );

    List<ElementConstitutif> rechercherElementsConstitutifsParCode(
            String codeEC
    );

    List<ElementConstitutif> listerElementsConstitutifsParUE(
            String idUE
    );

    ElementConstitutif modifierElementConstitutif(
            String idEC,
            ElementConstitutif elementConstitutif
    );

    void supprimerElementConstitutif(String idEC);
}
