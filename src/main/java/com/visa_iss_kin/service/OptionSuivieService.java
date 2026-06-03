package com.visa_iss_kin.service;

import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.model.Section;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface OptionSuivieService {
    OptionSuivie creerOptionSuivie(OptionSuivie optS);
    List<OptionSuivie> listeOptionSuivie();
    OptionSuivie chercherOptionSuivie(String idOpt);
    List<OptionSuivie> optionSuivieParSection(String sect);
    OptionSuivie modifierOptionSuivie(String idOpt, OptionSuivie opt);
    void supprimerOptionSuivie(String idOpt);
}
