package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.DeliberationService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class DeliberationServiceImpl implements DeliberationService {
    private final DeliberationRepository deliberationRepository;
    private final SyntheseAnnuelleRepository syntheseAnnuelleRepository;
    private final InscriptionRepository inscriptionRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public DeliberationServiceImpl(
            DeliberationRepository deliberationRepository,
            SyntheseAnnuelleRepository syntheseAnnuelleRepository,
            InscriptionRepository inscriptionRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.deliberationRepository = deliberationRepository;
        this.syntheseAnnuelleRepository = syntheseAnnuelleRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    @Override
    public Deliberation genererDeliberation(
            Long idSyntheseAnnuelle,
            String createdBy
    ) {
        SyntheseAnnuelle syntheseAnnuelle =
                rechercherSyntheseAnnuelle(idSyntheseAnnuelle);

        verifierSyntheseAnnuelleValide(syntheseAnnuelle);

        Deliberation deliberation =
                deliberationRepository
                        .findBySyntheseAnnuelle(syntheseAnnuelle)
                        .orElse(new Deliberation());

        if (deliberation.getValidee() != null
                && deliberation.getValidee()) {
            throw new RuntimeException(
                    "Cette délibération est déjà validée. "
                    + "Elle ne peut plus être régénérée."
            );
        }

        DecisionDeliberation decision =
                determinerDecisionDeliberation(syntheseAnnuelle);

        deliberation.setSyntheseAnnuelle(syntheseAnnuelle);
        deliberation.setInscription(syntheseAnnuelle.getInscription());
        deliberation.setAnneeAcademique(
                syntheseAnnuelle.getAnneeAcademique()
        );

        deliberation.setMoyenneFinale(
                syntheseAnnuelle.getMoyenneAnnuelle()
        );
        deliberation.setTotalCredits(
                syntheseAnnuelle.getTotalCredits()
        );
        deliberation.setCreditsValides(
                syntheseAnnuelle.getCreditsValides()
        );
        deliberation.setCreditsNonValides(
                syntheseAnnuelle.getCreditsNonValides()
        );
        deliberation.setTauxCreditsValides(
                syntheseAnnuelle.getTauxCreditsValides()
        );

        deliberation.setDecisionDeliberation(decision);
        deliberation.setMention(
                construireMention(syntheseAnnuelle.getMoyenneAnnuelle())
        );
        deliberation.setMotifDecision(
                construireMotifDecision(decision, syntheseAnnuelle)
        );

        deliberation.setValidee(false);
        deliberation.setCreatedBy(createdBy);

        if (deliberation.getCreatedAt() == null) {
            deliberation.setCreatedAt(LocalDateTime.now());
        }

        if (deliberation.getDateDeliberation() == null) {
            deliberation.setDateDeliberation(LocalDateTime.now());
        }

        return deliberationRepository.save(deliberation);
    }

    @Override
    public List<Deliberation> genererDeliberationsParAnneeEtPromotion(
            String idAa,
            String idPromo,
            String createdBy
    ) {
        rechercherAnneeAcademique(idAa);

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        List<Long> idsSynthesesAnnuelles =
                syntheseAnnuelleRepository
                        .findByAnneeAcademiqueIdAaAndInscriptionPromotionIdPromo(
                                idAa,
                                idPromo.trim()
                        )
                        .stream()
                        .filter(syntheseAnnuelle ->
                                syntheseAnnuelle.getIdSyntheseAnnuelle()
                                        != null
                        )
                        .map(SyntheseAnnuelle::getIdSyntheseAnnuelle)
                        .distinct()
                        .toList();

        return idsSynthesesAnnuelles.stream()
                .map(idSyntheseAnnuelle ->
                        genererDeliberation(
                                idSyntheseAnnuelle,
                                createdBy
                        )
                )
                .toList();
    }

    @Override
    public Deliberation validerDeliberation(
            Long idDeliberation,
            String validatedBy,
            String observationJury
    ) {
        Deliberation deliberation =
                rechercherDeliberationParId(idDeliberation);

        if (deliberation.getValidee() != null
                && deliberation.getValidee()) {
            throw new RuntimeException(
                    "Cette délibération est déjà validée."
            );
        }

        if (deliberation.getDecisionDeliberation()
                == DecisionDeliberation.EN_ATTENTE) {
            throw new RuntimeException(
                    "Impossible de valider une délibération "
                    + "dont la décision est encore EN_ATTENTE."
            );
        }

        deliberation.setValidee(true);
        deliberation.setValidatedBy(validatedBy);
        deliberation.setValidatedAt(LocalDateTime.now());

        if (observationJury != null
                && !observationJury.trim().isEmpty()) {
            deliberation.setObservationJury(
                    observationJury.trim()
            );
        }

        return deliberationRepository.save(deliberation);
    }

    @Override
    public Deliberation modifierDecisionDeliberation(
            Long idDeliberation,
            DecisionDeliberation decisionDeliberation,
            String motifDecision,
            String observationJury
    ) {
        Deliberation deliberation =
                rechercherDeliberationParId(idDeliberation);

        if (deliberation.getValidee() != null
                && deliberation.getValidee()) {
            throw new RuntimeException(
                    "Impossible de modifier une délibération déjà validée."
            );
        }

        if (decisionDeliberation == null) {
            throw new RuntimeException(
                    "La décision de délibération est obligatoire."
            );
        }

        deliberation.setDecisionDeliberation(decisionDeliberation);
        deliberation.setMotifDecision(motifDecision);
        deliberation.setObservationJury(observationJury);

        return deliberationRepository.save(deliberation);
    }

    @Override
    public List<Deliberation> listerDeliberations() {
        return deliberationRepository.findAll();
    }

    @Override
    public Deliberation rechercherDeliberationParId(
            Long idDeliberation
    ) {
        if (idDeliberation == null) {
            throw new RuntimeException(
                    "L'identifiant de la délibération est obligatoire."
            );
        }

        return deliberationRepository.findById(idDeliberation)
                .orElseThrow(() -> new RuntimeException(
                        "Délibération introuvable avec l'identifiant : "
                        + idDeliberation
                ));
    }

    @Override
    public Deliberation rechercherDeliberationParSyntheseAnnuelle(
            Long idSyntheseAnnuelle
    ) {
        SyntheseAnnuelle syntheseAnnuelle =
                rechercherSyntheseAnnuelle(idSyntheseAnnuelle);

        return deliberationRepository.findBySyntheseAnnuelle(
                syntheseAnnuelle
        ).orElseThrow(() -> new RuntimeException(
                "Aucune délibération trouvée pour cette synthèse annuelle."
        ));
    }

    @Override
    public List<Deliberation> listerDeliberationsParInscription(
            String idInscription
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        return deliberationRepository.findByInscription(inscription);
    }

    @Override
    public List<Deliberation> listerDeliberationsParAnneeAcademique(
            String idAa
    ) {
        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        return deliberationRepository.findByAnneeAcademique(
                anneeAcademique
        );
    }

    @Override
    public List<Deliberation> listerDeliberationsParDecision(
            DecisionDeliberation decisionDeliberation
    ) {
        if (decisionDeliberation == null) {
            throw new RuntimeException(
                    "La décision de délibération est obligatoire."
            );
        }

        return deliberationRepository.findByDecisionDeliberation(
                decisionDeliberation
        );
    }

    @Override
    public List<Deliberation> listerDeliberationsParValidation(
            Boolean validee
    ) {
        if (validee == null) {
            throw new RuntimeException(
                    "L'état de validation est obligatoire."
            );
        }

        return deliberationRepository.findByValidee(validee);
    }

    @Override
    public List<Deliberation> listerDeliberationsParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return deliberationRepository
                .findByInscriptionPromotionIdPromo(idPromo.trim());
    }

    @Override
    public List<Deliberation> listerDeliberationsParAnneeEtPromotion(
            String idAa,
            String idPromo
    ) {
        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return deliberationRepository
                .findByAnneeAcademiqueIdAaAndInscriptionPromotionIdPromo(
                        idAa,
                        idPromo.trim()
                );
    }

    @Override
    public List<Deliberation> listerDeliberationsParAnneeEtDecision(
            String idAa,
            DecisionDeliberation decisionDeliberation
    ) {
        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        if (decisionDeliberation == null) {
            throw new RuntimeException(
                    "La décision de délibération est obligatoire."
            );
        }

        return deliberationRepository
                .findByAnneeAcademiqueIdAaAndDecisionDeliberation(
                        idAa,
                        decisionDeliberation
                );
    }

    @Override
    public List<Deliberation> listerDeliberationsParAnneeEtValidation(
            String idAa,
            Boolean validee
    ) {
        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        if (validee == null) {
            throw new RuntimeException(
                    "L'état de validation est obligatoire."
            );
        }

        return deliberationRepository
                .findByAnneeAcademiqueIdAaAndValidee(
                        idAa,
                        validee
                );
    }

    @Override
    public void supprimerDeliberation(
            Long idDeliberation
    ) {
        Deliberation deliberation =
                rechercherDeliberationParId(idDeliberation);

        if (deliberation.getValidee() != null
                && deliberation.getValidee()) {
            throw new RuntimeException(
                    "Impossible de supprimer une délibération déjà validée."
            );
        }

        deliberationRepository.delete(deliberation);
    }

    private SyntheseAnnuelle rechercherSyntheseAnnuelle(
            Long idSyntheseAnnuelle
    ) {
        if (idSyntheseAnnuelle == null) {
            throw new RuntimeException(
                    "L'identifiant de la synthèse annuelle est obligatoire."
            );
        }

        return syntheseAnnuelleRepository.findById(idSyntheseAnnuelle)
                .orElseThrow(() -> new RuntimeException(
                        "Synthèse annuelle introuvable avec "
                        + "l'identifiant : " + idSyntheseAnnuelle
                ));
    }

    private Inscription rechercherInscription(
            String idInscription
    ) {
        if (idInscription == null
                || idInscription.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de l'inscription est obligatoire."
            );
        }

        return inscriptionRepository.findById(idInscription.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Inscription introuvable avec l'identifiant : "
                        + idInscription
                ));
    }

    private AnneeAcademique rechercherAnneeAcademique(
            String idAa
    ) {
        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        return anneeAcademiqueRepository.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : "
                        + idAa
                ));
    }

    private void verifierSyntheseAnnuelleValide(
            SyntheseAnnuelle syntheseAnnuelle
    ) {
        if (syntheseAnnuelle.getInscription() == null) {
            throw new RuntimeException(
                    "La synthèse annuelle n'est pas liée à une inscription."
            );
        }

        if (syntheseAnnuelle.getAnneeAcademique() == null) {
            throw new RuntimeException(
                    "La synthèse annuelle n'est pas liée "
                    + "à une année académique."
            );
        }
    }

    private DecisionDeliberation determinerDecisionDeliberation(
            SyntheseAnnuelle syntheseAnnuelle
    ) {
        if (syntheseAnnuelle.getStatutValidationAnnee() == null) {
            return DecisionDeliberation.EN_ATTENTE;
        }

        if (syntheseAnnuelle.getStatutValidationAnnee()
                == StatutValidationAnnee.ADMIS) {
            return DecisionDeliberation.ADMIS;
        }

        if (syntheseAnnuelle.getStatutValidationAnnee()
                == StatutValidationAnnee.ADMIS_AVEC_DETTES) {
            return DecisionDeliberation.ADMIS_AVEC_DETTES;
        }

        if (syntheseAnnuelle.getStatutValidationAnnee()
                == StatutValidationAnnee.AJOURNE) {
            return DecisionDeliberation.AJOURNE;
        }

        if (syntheseAnnuelle.getStatutValidationAnnee()
                == StatutValidationAnnee.PARTIELLEMENT_ADMIS) {
            return DecisionDeliberation.AJOURNE;
        }

        return DecisionDeliberation.EN_ATTENTE;
    }

    private String construireMention(
            Double moyenneFinale
    ) {
        if (moyenneFinale == null) {
            return "Non disponible";
        }

        if (moyenneFinale >= 16.0) {
            return "Très bien";
        }

        if (moyenneFinale >= 14.0) {
            return "Bien";
        }

        if (moyenneFinale >= 12.0) {
            return "Assez bien";
        }

        if (moyenneFinale >= 10.0) {
            return "Passable";
        }

        return "Insuffisant";
    }

    private String construireMotifDecision(
            DecisionDeliberation decision,
            SyntheseAnnuelle syntheseAnnuelle
    ) {
        if (decision == DecisionDeliberation.ADMIS) {
            return "Année validée avec la totalité des crédits requis.";
        }

        if (decision == DecisionDeliberation.ADMIS_AVEC_DETTES) {
            return "Année validée avec crédits non validés à régulariser.";
        }

        if (decision == DecisionDeliberation.AJOURNE) {
            return "Conditions académiques de passage non totalement remplies.";
        }

        if (decision == DecisionDeliberation.EN_ATTENTE) {
            return "Décision en attente de validation par le jury.";
        }

        return syntheseAnnuelle.getDecisionFinale();
    }
}
