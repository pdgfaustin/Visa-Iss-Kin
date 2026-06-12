
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.UniteEnseignement;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface UniteEnseignementService {
    UniteEnseignement creerUniteEnseignement(UniteEnseignement uniteEnseignement);

    List<UniteEnseignement> listerUnitesEnseignement();

    UniteEnseignement rechercherUniteEnseignementParId(String idUE);

    UniteEnseignement rechercherUniteEnseignementParCode(String codeUE);

    List<UniteEnseignement> rechercherUnitesEnseignementParLibelle(String libeUE);

    List<UniteEnseignement> rechercherUnitesEnseignementParCode(String codeUE);

    UniteEnseignement modifierUniteEnseignement(
            String idUE,
            UniteEnseignement uniteEnseignement
    );

    void supprimerUniteEnseignement(String idUE);
}
