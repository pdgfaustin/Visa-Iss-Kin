package com.visa_iss_kin.service;

import com.visa_iss_kin.model.NiveauPromotion;
import com.visa_iss_kin.model.Promotion;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PromotionService {
    Promotion creerPromotion(Promotion promotion);

    List<Promotion> listeDesPromotions();

    Promotion rechercherPromotionParId(String idPro);

    List<Promotion> listeDesPromotionsParOption(String idOpt);

    List<Promotion> listeDesPromotionsParNiveau(NiveauPromotion niveauPromotion);

    Promotion modifierPromotion(String idPro, Promotion promotion);

    void supprimerPromotion(String idPro);
}
