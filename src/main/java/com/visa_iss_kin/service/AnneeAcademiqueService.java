package com.visa_iss_kin.service;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.StatutAnneeAcademique;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface AnneeAcademiqueService {
    AnneeAcademique creerAnneeAcademique(AnneeAcademique anneeAcademique);

    List<AnneeAcademique> listerAnneesAcademiques();

    AnneeAcademique rechercherAnneeAcademiqueParId(String idAa);

    List<AnneeAcademique> listerAnneesParStatut(StatutAnneeAcademique statutAnnee);

    AnneeAcademique modifierAnneeAcademique(String idAa, AnneeAcademique anneeAcademique);

    AnneeAcademique ouvrirAnneeAcademique(String idAa);

    AnneeAcademique fermerAnneeAcademique(String idAa);

    void supprimerAnneeAcademique(String idAa);
}
