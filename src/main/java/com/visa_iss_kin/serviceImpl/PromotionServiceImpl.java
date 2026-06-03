package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.NiveauPromotion;
import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.repository.OptionSuivieRepository;
import com.visa_iss_kin.repository.PromotionRepository;
import com.visa_iss_kin.service.PromotionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final OptionSuivieRepository optionSuivieRepository;

    public PromotionServiceImpl(
            PromotionRepository promotionRepository,
            OptionSuivieRepository optionSuivieRepository
    ) {
        this.promotionRepository = promotionRepository;
        this.optionSuivieRepository = optionSuivieRepository;
    }

    @Override
    public Promotion creerPromotion(Promotion promo) {

        if (promo.getIdPromo() == null || promo.getIdPromo().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant de la promotion idPro est obligatoire.");
        }

        if (promo.getLibePromo()== null || promo.getLibePromo().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de la promotion libePro est obligatoire.");
        }

        if (promo.getNiveauPromotion() == null) {
            throw new RuntimeException("Le niveau de la promotion niveauPromotion est obligatoire.");
        }

        if (promo.getOptionSuivie() == null || promo.getOptionSuivie().getIdOpt() == null) {
            throw new RuntimeException("L'option suivie de la promotion est obligatoire.");
        }

        OptionSuivie optionSuivie = optionSuivieRepository.findById(
                promo.getOptionSuivie().getIdOpt()
        ).orElseThrow(() -> new RuntimeException(
                "Option suivie introuvable avec l'identifiant : "
                + promo.getOptionSuivie().getIdOpt()
        ));

        promo.setOptionSuivie(optionSuivie);

        if (promo.getTotUE() == null) {
            promo.setTotUE(0);
        }

        if (promo.getCreatedAt() == null) {
            promo.setCreatedAt(LocalDateTime.now());
        }

        return promotionRepository.save(promo);
    }

    @Override
    public List<Promotion> listeDesPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion rechercherPromotionParId(String idPro) {
        return promotionRepository.findById(idPro)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPro
                ));
    }

    @Override
    public List<Promotion> listeDesPromotionsParOption(String idOpt) {
        OptionSuivie optionSuivie = optionSuivieRepository.findById(idOpt)
                .orElseThrow(() -> new RuntimeException(
                        "Option suivie introuvable avec l'identifiant : " + idOpt
                ));

        return promotionRepository.findByOptionSuivie(optionSuivie);
    }

    @Override
    public List<Promotion> listeDesPromotionsParNiveau(NiveauPromotion niveauPromotion) {
        return promotionRepository.findByNiveauPromotion(niveauPromotion);
    }

    @Override
    public Promotion modifierPromotion(String idPro, Promotion promotion) {
        Promotion promotionExistante = rechercherPromotionParId(idPro);

        if (promotion.getLibePromo() != null) {
            promotionExistante.setLibePromo(promotion.getLibePromo());
        }

        if (promotion.getNiveauPromotion() != null) {
            promotionExistante.setNiveauPromotion(promotion.getNiveauPromotion());
        }

        if (promotion.getTotUE() != null) {
            promotionExistante.setTotUE(promotion.getTotUE());
        }

        if (promotion.getCreatedBy() != null) {
            promotionExistante.setCreatedBy(promotion.getCreatedBy());
        }

        if (promotion.getOptionSuivie() != null && promotion.getOptionSuivie().getIdOpt() != null) {
            OptionSuivie optionSuivie = optionSuivieRepository.findById(
                    promotion.getOptionSuivie().getIdOpt()
            ).orElseThrow(() -> new RuntimeException(
                    "Option suivie introuvable avec l'identifiant : "
                    + promotion.getOptionSuivie().getIdOpt()
            ));

            promotionExistante.setOptionSuivie(optionSuivie);
        }

        if (promotionExistante.getCreatedAt() == null) {
            promotionExistante.setCreatedAt(LocalDateTime.now());
        }

        return promotionRepository.save(promotionExistante);
    }

    @Override
    public void supprimerPromotion(String idPro) {
        Promotion promotion = rechercherPromotionParId(idPro);
        promotionRepository.delete(promotion);
    }

}
