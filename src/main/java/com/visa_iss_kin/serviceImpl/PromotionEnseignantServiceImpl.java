
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.GradeEnseignant;
import com.visa_iss_kin.model.PromotionEnseignant;
import com.visa_iss_kin.model.StatutPromotionEnseignant;
import com.visa_iss_kin.repository.EnseignantRepository;
import com.visa_iss_kin.repository.PromotionEnseignantRepository;
import com.visa_iss_kin.service.PromotionEnseignantService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class PromotionEnseignantServiceImpl implements PromotionEnseignantService {
    private final PromotionEnseignantRepository promotionEnseignantRepository;
    private final EnseignantRepository enseignantRepository;

    public PromotionEnseignantServiceImpl(
            PromotionEnseignantRepository promotionEnseignantRepository,
            EnseignantRepository enseignantRepository
    ) {
        this.promotionEnseignantRepository = promotionEnseignantRepository;
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    public PromotionEnseignant creerPromotionEnseignant(
            PromotionEnseignant promotionEnseignant
    ) {
        verifierDonneesObligatoires(promotionEnseignant);

        Enseignant enseignant = rechercherEnseignant(
                promotionEnseignant.getEnseignant().getMatrEns()
        );

//        verifierNumeroDecisionUnique(
//                promotionEnseignant.getNumeroDecision(),
//                null
//        );

        verifierCoherenceDesGrades(
                enseignant,
                promotionEnseignant.getAncienGrade(),
                promotionEnseignant.getNouveauGrade(),
                promotionEnseignant.getStatutPromotion()
        );

        if (promotionEnseignant.getDatePromotion()
                .isAfter(LocalDate.now())) {
            throw new RuntimeException(
                    "La date de promotion ne peut pas être postérieure "
                    + "à la date du jour."
            );
        }

        promotionEnseignant.setEnseignant(enseignant);

        if (promotionEnseignant.getCreatedAt() == null) {
            promotionEnseignant.setCreatedAt(LocalDateTime.now());
        }

        if (promotionEnseignant.getStatutPromotion() == null) {
            promotionEnseignant.setStatutPromotion(
                    StatutPromotionEnseignant.EN_ATTENTE
            );
        }

        return promotionEnseignantRepository.save(
                promotionEnseignant
        );
    }

    @Override
    public List<PromotionEnseignant> listerPromotionsEnseignants() {
        return promotionEnseignantRepository.findAll();
    }

    @Override
    public PromotionEnseignant rechercherPromotionEnseignantParId(
            Long idPromotionEnseignant
    ) {
        return promotionEnseignantRepository
                .findById(idPromotionEnseignant)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion d'enseignant introuvable avec "
                        + "l'identifiant : " + idPromotionEnseignant
                ));
    }

    @Override
    public List<PromotionEnseignant> listerHistoriqueParEnseignant(
            String matrEns
    ) {
        Enseignant enseignant = rechercherEnseignant(matrEns);

        return promotionEnseignantRepository
                .findByEnseignantOrderByDatePromotionDesc(
                        enseignant
                );
    }

    @Override
    public List<PromotionEnseignant> listerPromotionsParGrade(
            GradeEnseignant nouveauGrade
    ) {
        if (nouveauGrade == null) {
            throw new RuntimeException(
                    "Le grade recherché est obligatoire."
            );
        }

        return promotionEnseignantRepository.findByNouveauGrade(
                nouveauGrade
        );
    }

    @Override
    public List<PromotionEnseignant> listerPromotionsParStatut(
            StatutPromotionEnseignant statutPromotion
    ) {
        if (statutPromotion == null) {
            throw new RuntimeException(
                    "Le statut de promotion est obligatoire."
            );
        }

        return promotionEnseignantRepository.findByStatutPromotion(
                statutPromotion
        );
    }

    @Override
    public List<PromotionEnseignant>
            listerPromotionsParEnseignantEtStatut(
                    String matrEns,
                    StatutPromotionEnseignant statutPromotion
            ) {
        Enseignant enseignant = rechercherEnseignant(matrEns);

        if (statutPromotion == null) {
            throw new RuntimeException(
                    "Le statut de promotion est obligatoire."
            );
        }

        return promotionEnseignantRepository
                .findByEnseignantAndStatutPromotion(
                        enseignant,
                        statutPromotion
                );
    }

    @Override
    public PromotionEnseignant rechercherGradeActuelEnseignant(
            String matrEns
    ) {
        Enseignant enseignant = rechercherEnseignant(matrEns);

        return promotionEnseignantRepository
                .findTopByEnseignantAndStatutPromotionOrderByDatePromotionDesc(
                        enseignant,
                        StatutPromotionEnseignant.VALIDEE
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune promotion validée n'a été trouvée "
                        + "pour l'enseignant : " + matrEns
                ));
    }

    @Override
    public PromotionEnseignant modifierPromotionEnseignant(
            Long idPromotionEnseignant,
            PromotionEnseignant promotionEnseignant
    ) {
        PromotionEnseignant promotionExistante =
                rechercherPromotionEnseignantParId(
                        idPromotionEnseignant
                );

        Enseignant enseignant =
                promotionExistante.getEnseignant();

        if (promotionEnseignant.getEnseignant() != null
                && promotionEnseignant.getEnseignant()
                        .getMatrEns() != null
                && !promotionEnseignant.getEnseignant()
                        .getMatrEns().trim().isEmpty()) {

            enseignant = rechercherEnseignant(
                    promotionEnseignant.getEnseignant()
                            .getMatrEns()
            );
        }

        GradeEnseignant ancienGrade =
                promotionEnseignant.getAncienGrade() != null
                        ? promotionEnseignant.getAncienGrade()
                        : promotionExistante.getAncienGrade();

        GradeEnseignant nouveauGrade =
                promotionEnseignant.getNouveauGrade() != null
                        ? promotionEnseignant.getNouveauGrade()
                        : promotionExistante.getNouveauGrade();

        LocalDate datePromotion =
                promotionEnseignant.getDatePromotion() != null
                        ? promotionEnseignant.getDatePromotion()
                        : promotionExistante.getDatePromotion();

        StatutPromotionEnseignant statutPromotion =
                promotionEnseignant.getStatutPromotion() != null
                        ? promotionEnseignant.getStatutPromotion()
                        : promotionExistante.getStatutPromotion();

        String numeroDecision =
                promotionEnseignant.getNumeroDecision() != null
                        ? promotionEnseignant.getNumeroDecision()
                        : promotionExistante.getNumeroDecision();

//        verifierNumeroDecisionUnique(
//                numeroDecision,
//                idPromotionEnseignant
//        );

        if (nouveauGrade == null) {
            throw new RuntimeException(
                    "Le nouveau grade est obligatoire."
            );
        }

        if (datePromotion == null) {
            throw new RuntimeException(
                    "La date de promotion est obligatoire."
            );
        }

        if (datePromotion.isAfter(LocalDate.now())) {
            throw new RuntimeException(
                    "La date de promotion ne peut pas être postérieure "
                    + "à la date du jour."
            );
        }

        verifierCoherenceDesGrades(
                enseignant,
                ancienGrade,
                nouveauGrade,
                statutPromotion
        );

        promotionExistante.setEnseignant(enseignant);
        promotionExistante.setAncienGrade(ancienGrade);
        promotionExistante.setNouveauGrade(nouveauGrade);
        promotionExistante.setDatePromotion(datePromotion);
        promotionExistante.setStatutPromotion(statutPromotion);
        promotionExistante.setNumeroDecision(numeroDecision);

        if (promotionEnseignant.getAutoriteDecision() != null) {
            promotionExistante.setAutoriteDecision(
                    promotionEnseignant.getAutoriteDecision()
            );
        }

        if (promotionEnseignant.getObservation() != null) {
            promotionExistante.setObservation(
                    promotionEnseignant.getObservation()
            );
        }

        if (promotionEnseignant.getCreatedBy() != null) {
            promotionExistante.setCreatedBy(
                    promotionEnseignant.getCreatedBy()
            );
        }

        return promotionEnseignantRepository.save(
                promotionExistante
        );
    }

    @Override
    public PromotionEnseignant changerStatutPromotion(
            Long idPromotionEnseignant,
            StatutPromotionEnseignant statutPromotion
    ) {
        PromotionEnseignant promotionExistante =
                rechercherPromotionEnseignantParId(
                        idPromotionEnseignant
                );

        if (statutPromotion == null) {
            throw new RuntimeException(
                    "Le nouveau statut est obligatoire."
            );
        }

        if (statutPromotion == StatutPromotionEnseignant.VALIDEE) {
            verifierCoherenceDesGrades(
                    promotionExistante.getEnseignant(),
                    promotionExistante.getAncienGrade(),
                    promotionExistante.getNouveauGrade(),
                    statutPromotion
            );

            if (promotionExistante.getNumeroDecision() == null
                    || promotionExistante.getNumeroDecision()
                            .trim().isEmpty()) {
                throw new RuntimeException(
                        "Le numéro de décision est obligatoire "
                        + "pour valider une promotion."
                );
            }

            if (promotionExistante.getAutoriteDecision() == null
                    || promotionExistante.getAutoriteDecision()
                            .trim().isEmpty()) {
                throw new RuntimeException(
                        "L'autorité ayant pris la décision est obligatoire "
                        + "pour valider une promotion."
                );
            }
        }

        promotionExistante.setStatutPromotion(statutPromotion);

        return promotionEnseignantRepository.save(
                promotionExistante
        );
    }

    @Override
    public void supprimerPromotionEnseignant(
            Long idPromotionEnseignant
    ) {
        PromotionEnseignant promotionEnseignant =
                rechercherPromotionEnseignantParId(
                        idPromotionEnseignant
                );

        if (promotionEnseignant.getStatutPromotion()
                == StatutPromotionEnseignant.VALIDEE) {
            throw new RuntimeException(
                    "Une promotion validée ne peut pas être supprimée. "
                    + "Elle doit d'abord être annulée."
            );
        }

        promotionEnseignantRepository.delete(
                promotionEnseignant
        );
    }

    private Enseignant rechercherEnseignant(String matrEns) {
        if (matrEns == null || matrEns.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le matricule de l'enseignant est obligatoire."
            );
        }

        return enseignantRepository.findById(matrEns)
                .orElseThrow(() -> new RuntimeException(
                        "Enseignant introuvable avec le matricule : "
                        + matrEns
                ));
    }

    private void verifierDonneesObligatoires(
            PromotionEnseignant promotionEnseignant
    ) {
        if (promotionEnseignant == null) {
            throw new RuntimeException(
                    "Les informations de la promotion sont obligatoires."
            );
        }

        if (promotionEnseignant.getEnseignant() == null
                || promotionEnseignant.getEnseignant()
                        .getMatrEns() == null
                || promotionEnseignant.getEnseignant()
                        .getMatrEns().trim().isEmpty()) {
            throw new RuntimeException(
                    "L'enseignant est obligatoire."
            );
        }

        if (promotionEnseignant.getNouveauGrade() == null) {
            throw new RuntimeException(
                    "Le nouveau grade est obligatoire."
            );
        }

        if (promotionEnseignant.getDatePromotion() == null) {
            throw new RuntimeException(
                    "La date de promotion est obligatoire."
            );
        }
    }

    private void verifierNumeroDecisionUnique(
            String numeroDecision,
            Long idPromotionEnseignantActuel
    ) {
        if (numeroDecision == null
                || numeroDecision.trim().isEmpty()) {
            return;
        }

        promotionEnseignantRepository.findAll()
                .stream()
                .filter(promotion ->
                        promotion.getNumeroDecision() != null
                        && promotion.getNumeroDecision()
                                .equalsIgnoreCase(
                                        numeroDecision.trim()
                                )
                )
                .filter(promotion ->
                        idPromotionEnseignantActuel == null
                        || !promotion.getIdPromotionEnseignant()
                                .equals(idPromotionEnseignantActuel)
                )
                .findFirst()
                .ifPresent(promotion -> {
                    throw new RuntimeException(
                            "Le numéro de décision existe déjà : "
                            + numeroDecision
                    );
                });
    }

    private void verifierCoherenceDesGrades(
            Enseignant enseignant,
            GradeEnseignant ancienGrade,
            GradeEnseignant nouveauGrade,
            StatutPromotionEnseignant statutPromotion
    ) {
        if (nouveauGrade == null) {
            throw new RuntimeException(
                    "Le nouveau grade est obligatoire."
            );
        }

        if (ancienGrade != null
                && ancienGrade == nouveauGrade) {
            throw new RuntimeException(
                    "L'ancien grade et le nouveau grade "
                    + "ne peuvent pas être identiques."
            );
        }

        if (statutPromotion
                != StatutPromotionEnseignant.VALIDEE) {
            return;
        }

        promotionEnseignantRepository
                .findTopByEnseignantAndStatutPromotionOrderByDatePromotionDesc(
                        enseignant,
                        StatutPromotionEnseignant.VALIDEE
                )
                .ifPresent(dernierePromotion -> {
                    GradeEnseignant gradeActuel =
                            dernierePromotion.getNouveauGrade();

                    if (ancienGrade == null) {
                        throw new RuntimeException(
                                "L'ancien grade doit correspondre "
                                + "au grade actuel de l'enseignant : "
                                + gradeActuel
                        );
                    }

                    if (ancienGrade != gradeActuel) {
                        throw new RuntimeException(
                                "L'ancien grade indiqué ne correspond pas "
                                + "au grade actuel de l'enseignant. "
                                + "Grade actuel : " + gradeActuel
                                + ", grade indiqué : " + ancienGrade
                        );
                    }
                });
    }
}
