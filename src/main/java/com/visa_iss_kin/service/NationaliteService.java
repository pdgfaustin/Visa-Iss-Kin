package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Nationalite;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface NationaliteService {
    Nationalite creerNationalite(Nationalite nationalite);

    List<Nationalite> listeDesNationalites();

    Nationalite rechercherNationaliteParId(String idNation);

    Nationalite modifierNationalite(String idNation, Nationalite nationalite);

    void supprimerNationalite(String idNation);
}
