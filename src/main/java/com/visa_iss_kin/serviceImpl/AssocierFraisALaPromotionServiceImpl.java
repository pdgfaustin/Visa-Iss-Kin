
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.model.FraisAcademique;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.repository.AnneeAcademiqueRepository;
import com.visa_iss_kin.repository.AssocierFraisALaPromotionRepository;
import com.visa_iss_kin.repository.FraisAcademiqueRepository;
import com.visa_iss_kin.repository.PromotionRepository;
import com.visa_iss_kin.service.AssocierFraisALaPromotionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class AssocierFraisALaPromotionServiceImpl implements AssocierFraisALaPromotionService {
    private final AssocierFraisALaPromotionRepository associerFraisALaPromotionRepository;
    private final FraisAcademiqueRepository fraisAcademiqueRepository;
    private final PromotionRepository promotionRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public AssocierFraisALaPromotionServiceImpl(
            AssocierFraisALaPromotionRepository associerFraisALaPromotionRepository,
            FraisAcademiqueRepository fraisAcademiqueRepository,
            PromotionRepository promotionRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.associerFraisALaPromotionRepository = associerFraisALaPromotionRepository;
        this.fraisAcademiqueRepository = fraisAcademiqueRepository;
        this.promotionRepository = promotionRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    @Override
    public AssocierFraisALaPromotion creerAssociationFrais(
            AssocierFraisALaPromotion associationFrais
    ) {

        if (associationFrais.getMontantFrais() == null) {
            throw new RuntimeException("Le montant du frais est obligatoire.");
        }

        if (associationFrais.getMontantFrais().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant du frais doit être supérieur à zéro.");
        }

        if (associationFrais.getDevise() == null) {
            throw new RuntimeException("La devise du frais est obligatoire.");
        }

        if (associationFrais.getFraisAcademique() == null
                || associationFrais.getFraisAcademique().getIdFrais() == null) {
            throw new RuntimeException("Le frais académique est obligatoire.");
        }

        if (associationFrais.getPromotion() == null
                || associationFrais.getPromotion().getIdPromo() == null) {
            throw new RuntimeException("La promotion est obligatoire.");
        }

        if (associationFrais.getAnneeAcademique() == null
                || associationFrais.getAnneeAcademique().getIdAa() == null) {
            throw new RuntimeException("L'année académique est obligatoire.");
        }

        FraisAcademique fraisAcademique = fraisAcademiqueRepository.findById(
                associationFrais.getFraisAcademique().getIdFrais()
        ).orElseThrow(() -> new RuntimeException(
                "Frais académique introuvable avec l'identifiant : "
                + associationFrais.getFraisAcademique().getIdFrais()
        ));

        Promotion promotion = promotionRepository.findById(
                associationFrais.getPromotion().getIdPromo()
        ).orElseThrow(() -> new RuntimeException(
                "Promotion introuvable avec l'identifiant : "
                + associationFrais.getPromotion().getIdPromo()
        ));

        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(
                associationFrais.getAnneeAcademique().getIdAa()
        ).orElseThrow(() -> new RuntimeException(
                "Année académique introuvable avec l'identifiant : "
                + associationFrais.getAnneeAcademique().getIdAa()
        ));

        boolean existeDeja =
                associerFraisALaPromotionRepository
                        .existsByFraisAcademiqueAndPromotionAndAnneeAcademique(
                                fraisAcademique,
                                promotion,
                                anneeAcademique
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Ce frais est déjà associé à cette promotion pour cette année académique."
            );
        }

        if (associationFrais.getCreatedAt() == null) {
            associationFrais.setCreatedAt(LocalDateTime.now());
        }

        associationFrais.setFraisAcademique(fraisAcademique);
        associationFrais.setPromotion(promotion);
        associationFrais.setAnneeAcademique(anneeAcademique);

        return associerFraisALaPromotionRepository.save(associationFrais);
    }

    @Override
    public List<AssocierFraisALaPromotion> listerAssociationsFrais() {
        return associerFraisALaPromotionRepository.findAll();
    }

    @Override
    public AssocierFraisALaPromotion rechercherAssociationFraisParId(
            Long idAssociationFrais
    ) {
        return associerFraisALaPromotionRepository.findById(idAssociationFrais)
                .orElseThrow(() -> new RuntimeException(
                        "Association frais-promotion introuvable avec l'identifiant : "
                        + idAssociationFrais
                ));
    }

    @Override
    public List<AssocierFraisALaPromotion> listerAssociationsParFrais(
            String idFrais
    ) {
        FraisAcademique fraisAcademique = fraisAcademiqueRepository.findById(idFrais)
                .orElseThrow(() -> new RuntimeException(
                        "Frais académique introuvable avec l'identifiant : " + idFrais
                ));

        return associerFraisALaPromotionRepository.findByFraisAcademique(fraisAcademique);
    }

    @Override
    public List<AssocierFraisALaPromotion> listerAssociationsParPromotion(
            String idPromo
    ) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        return associerFraisALaPromotionRepository.findByPromotion(promotion);
    }

    @Override
    public List<AssocierFraisALaPromotion> listerAssociationsParAnneeAcademique(
            String idAa
    ) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : " + idAa
                ));

        return associerFraisALaPromotionRepository.findByAnneeAcademique(anneeAcademique);
    }

    @Override
    public List<AssocierFraisALaPromotion> listerAssociationsParPromotionEtAnnee(
            String idPromo,
            String idAa
    ) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : " + idAa
                ));

        return associerFraisALaPromotionRepository
                .findByPromotionAndAnneeAcademique(promotion, anneeAcademique);
    }

    @Override
    public AssocierFraisALaPromotion modifierAssociationFrais(
            Long idAssociationFrais,
            AssocierFraisALaPromotion associationFrais
    ) {
        AssocierFraisALaPromotion associationExistante =
                rechercherAssociationFraisParId(idAssociationFrais);

        if (associationFrais.getMontantFrais() != null) {
            if (associationFrais.getMontantFrais().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Le montant du frais doit être supérieur à zéro.");
            }
            associationExistante.setMontantFrais(associationFrais.getMontantFrais());
        }

        if (associationFrais.getDevise() != null) {
            associationExistante.setDevise(associationFrais.getDevise());
        }

        if (associationFrais.getObligatoire() != null) {
            associationExistante.setObligatoire(associationFrais.getObligatoire());
        }

        if (associationFrais.getDateDebutValidite() != null) {
            associationExistante.setDateDebutValidite(associationFrais.getDateDebutValidite());
        }

        if (associationFrais.getDateFinValidite() != null) {
            associationExistante.setDateFinValidite(associationFrais.getDateFinValidite());
        }

        if (associationFrais.getCreatedBy() != null) {
            associationExistante.setCreatedBy(associationFrais.getCreatedBy());
        }

        return associerFraisALaPromotionRepository.save(associationExistante);
    }

    @Override
    public void supprimerAssociationFrais(Long idAssociationFrais) {
        AssocierFraisALaPromotion associationFrais =
                rechercherAssociationFraisParId(idAssociationFrais);

        associerFraisALaPromotionRepository.delete(associationFrais);
    }
}
