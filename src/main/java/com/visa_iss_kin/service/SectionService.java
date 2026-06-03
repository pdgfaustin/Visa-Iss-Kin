package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Section;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SectionService {
    Section creerSection(Section sect);
    List<Section> listerSection();
    Section chercherParId(String idSect);
    Section modifierSection(String idSect, Section sect);
    void supprimerSection(String idSect);
}
