package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.SyntheseUEService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SyntheseUEServiceImpl implements SyntheseUEService {
    private final SyntheseUERepository syntheseUERepository;
    private final SyntheseEvaluationECRepository syntheseEvaluationECRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;
    private final UniteEnseignementRepository uniteEnseignementRepository;
    private final ElementConstitutifRepository elementConstitutifRepository;

    public SyntheseUEServiceImpl(
            SyntheseUERepository syntheseUERepository,
            SyntheseEvaluationECRepository syntheseEvaluationECRepository,
            InscriptionRepository inscriptionRepository,
            SessionEvaluationRepository sessionEvaluationRepository,
            UniteEnseignementRepository uniteEnseignementRepository,
            ElementConstitutifRepository elementConstitutifRepository
    ) {
        this.syntheseUERepository = syntheseUERepository;
        this.syntheseEvaluationECRepository =
                syntheseEvaluationECRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionEvaluationRepository = sessionEvaluationRepository;
        this.uniteEnseignementRepository = uniteEnseignementRepository;
        this.elementConstitutifRepository =
                elementConstitutifRepository;
    }

    @Override
    public SyntheseUE genererSyntheseUE(
            String idInscription,
            Long idSessionEvaluation,
            String idUE,
            String createdBy
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        UniteEnseignement uniteEnseignement =
                rechercherUniteEnseignement(idUE);

        verifierCoherenceInscriptionSession(
                inscription,
                sessionEvaluation
        );

        List<SyntheseEvaluationEC> synthesesEC =
                syntheseEvaluationECRepository
                        .findByInscriptionAndSessionEvaluation(
                                inscription,
                                sessionEvaluation
                        )
                        .stream()
                        .filter(syntheseEC ->
                                syntheseEC.getElementConstitutif() != null
                                && syntheseEC.getElementConstitutif()
                                        .getUniteEnseignement() != null
                                && syntheseEC.getElementConstitutif()
                                        .getUniteEnseignement()
                                        .getIdUE()
                                        .equals(idUE)
                        )
                        .toList();

        SyntheseUE syntheseUE =
                syntheseUERepository
                        .findByInscriptionAndSessionEvaluationAndUniteEnseignement(
                                inscription,
                                sessionEvaluation,
                                uniteEnseignement
                        )
                        .orElse(new SyntheseUE());

        syntheseUE.setInscription(inscription);
        syntheseUE.setSessionEvaluation(sessionEvaluation);
        syntheseUE.setUniteEnseignement(uniteEnseignement);
        syntheseUE.setCreatedBy(createdBy);

        if (syntheseUE.getCreatedAt() == null) {
            syntheseUE.setCreatedAt(LocalDateTime.now());
        }

        if (synthesesEC.isEmpty()) {
            syntheseUE.setNoteFinale(0.0);
            syntheseUE.setNoteMaximale(20.0);
            syntheseUE.setMoyenneSurVingt(0.0);
            syntheseUE.setNombreEC(0);
            syntheseUE.setNombreECValides(0);
            syntheseUE.setNombreECNonValides(0);
            syntheseUE.setTotalCredits(0.0);
            syntheseUE.setCreditsValides(0.0);
            syntheseUE.setAppreciation("Aucune synthèse EC");
            syntheseUE.setStatutValidationUE(
                    StatutValidationUE.NON_COMPOSEE
            );

            return syntheseUERepository.save(syntheseUE);
        }

        double sommePonderee = 0.0;
        double totalCredits = 0.0;
        double creditsValides = 0.0;

        int nombreECValides = 0;
        int nombreECNonValides = 0;

        for (SyntheseEvaluationEC syntheseEC : synthesesEC) {

            ElementConstitutif elementConstitutif =
                    syntheseEC.getElementConstitutif();

            double creditEC =
                    elementConstitutif.getCreditEC() == null
                            ? 0.0
                            : elementConstitutif.getCreditEC();

            double moyenneEC =
                    syntheseEC.getMoyenneSurVingt() == null
                            ? 0.0
                            : syntheseEC.getMoyenneSurVingt();

            sommePonderee += moyenneEC * creditEC;
            totalCredits += creditEC;

            if (syntheseEC.getStatutValidation()
                    == StatutValidationEC.VALIDE) {
                nombreECValides++;
                creditsValides += creditEC;
            } else {
                nombreECNonValides++;
            }
        }

        double moyenneSurVingt =
                totalCredits <= 0.0
                        ? 0.0
                        : sommePonderee / totalCredits;

        moyenneSurVingt = arrondir(moyenneSurVingt);

        syntheseUE.setNoteFinale(moyenneSurVingt);
        syntheseUE.setNoteMaximale(20.0);
        syntheseUE.setMoyenneSurVingt(moyenneSurVingt);
        syntheseUE.setNombreEC(synthesesEC.size());
        syntheseUE.setNombreECValides(nombreECValides);
        syntheseUE.setNombreECNonValides(nombreECNonValides);
        syntheseUE.setTotalCredits(arrondir(totalCredits));
        syntheseUE.setCreditsValides(arrondir(creditsValides));
        syntheseUE.setAppreciation(
                construireAppreciation(moyenneSurVingt)
        );
        syntheseUE.setStatutValidationUE(
                determinerStatutValidationUE(
                        moyenneSurVingt,
                        synthesesEC.size(),
                        nombreECValides
                )
        );

        return syntheseUERepository.save(syntheseUE);
    }

    @Override
    public List<SyntheseUE> genererSynthesesUEParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation,
            String createdBy
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        verifierCoherenceInscriptionSession(
                inscription,
                sessionEvaluation
        );

        List<String> idsUE =
                syntheseEvaluationECRepository
                        .findByInscriptionAndSessionEvaluation(
                                inscription,
                                sessionEvaluation
                        )
                        .stream()
                        .filter(syntheseEC ->
                                syntheseEC.getElementConstitutif() != null
                                && syntheseEC.getElementConstitutif()
                                        .getUniteEnseignement() != null
                                && syntheseEC.getElementConstitutif()
                                        .getUniteEnseignement()
                                        .getIdUE() != null
                        )
                        .map(syntheseEC ->
                                syntheseEC.getElementConstitutif()
                                        .getUniteEnseignement()
                                        .getIdUE()
                        )
                        .distinct()
                        .toList();

        return idsUE.stream()
                .map(idUE ->
                        genererSyntheseUE(
                                idInscription,
                                idSessionEvaluation,
                                idUE,
                                createdBy
                        )
                )
                .toList();
    }

    @Override
    public List<SyntheseUE> listerSynthesesUE() {
        return syntheseUERepository.findAll();
    }

    @Override
    public SyntheseUE rechercherSyntheseUEParId(
            Long idSyntheseUE
    ) {
        return syntheseUERepository.findById(idSyntheseUE)
                .orElseThrow(() -> new RuntimeException(
                        "Synthèse UE introuvable avec l'identifiant : "
                        + idSyntheseUE
                ));
    }

    @Override
    public SyntheseUE rechercherSyntheseUEParInscriptionSessionEtUE(
            String idInscription,
            Long idSessionEvaluation,
            String idUE
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        UniteEnseignement uniteEnseignement =
                rechercherUniteEnseignement(idUE);

        return syntheseUERepository
                .findByInscriptionAndSessionEvaluationAndUniteEnseignement(
                        inscription,
                        sessionEvaluation,
                        uniteEnseignement
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune synthèse UE trouvée pour cette inscription, "
                        + "cette session et cette unité d'enseignement."
                ));
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParInscription(
            String idInscription
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        return syntheseUERepository.findByInscription(inscription);
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParSession(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return syntheseUERepository.findBySessionEvaluation(
                sessionEvaluation
        );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParUniteEnseignement(
            String idUE
    ) {
        UniteEnseignement uniteEnseignement =
                rechercherUniteEnseignement(idUE);

        return syntheseUERepository.findByUniteEnseignement(
                uniteEnseignement
        );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParStatutValidation(
            StatutValidationUE statutValidationUE
    ) {
        if (statutValidationUE == null) {
            throw new RuntimeException(
                    "Le statut de validation UE est obligatoire."
            );
        }

        return syntheseUERepository.findByStatutValidationUE(
                statutValidationUE
        );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return syntheseUERepository.findByInscriptionAndSessionEvaluation(
                inscription,
                sessionEvaluation
        );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return syntheseUERepository.findByInscriptionPromotionIdPromo(
                idPromo.trim()
        );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    ) {
        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return syntheseUERepository
                .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                        idSessionEvaluation,
                        idPromo.trim()
                );
    }

    @Override
    public List<SyntheseUE> listerSynthesesUEParSessionPromotionEtUE(
            Long idSessionEvaluation,
            String idPromo,
            String idUE
    ) {
        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        if (idUE == null) {
            throw new RuntimeException(
                    "L'identifiant de l'unité d'enseignement est obligatoire."
            );
        }

        return syntheseUERepository
                .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromoAndUniteEnseignementIdUE(
                        idSessionEvaluation,
                        idPromo.trim(),
                        idUE
                );
    }

    @Override
    public void supprimerSyntheseUE(Long idSyntheseUE) {
        SyntheseUE syntheseUE =
                rechercherSyntheseUEParId(idSyntheseUE);

        syntheseUERepository.delete(syntheseUE);
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

    private SessionEvaluation rechercherSessionEvaluation(
            Long idSessionEvaluation
    ) {
        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        return sessionEvaluationRepository.findById(idSessionEvaluation)
                .orElseThrow(() -> new RuntimeException(
                        "Session d'évaluation introuvable avec "
                        + "l'identifiant : " + idSessionEvaluation
                ));
    }

    private UniteEnseignement rechercherUniteEnseignement(
            String idUE
    ) {
        if (idUE == null) {
            throw new RuntimeException(
                    "L'identifiant de l'unité d'enseignement est obligatoire."
            );
        }

        return uniteEnseignementRepository.findById(idUE)
                .orElseThrow(() -> new RuntimeException(
                        "Unité d'enseignement introuvable avec "
                        + "l'identifiant : " + idUE
                ));
    }

    private void verifierCoherenceInscriptionSession(
            Inscription inscription,
            SessionEvaluation sessionEvaluation
    ) {
        if (inscription.getAnneeAcademique() == null
                || inscription.getAnneeAcademique().getIdAa() == null) {
            throw new RuntimeException(
                    "L'inscription n'est pas correctement liée "
                    + "à une année académique."
            );
        }

        if (sessionEvaluation.getAnneeAcademique() == null
                || sessionEvaluation.getAnneeAcademique()
                        .getIdAa() == null) {
            throw new RuntimeException(
                    "La session d'évaluation n'est pas correctement liée "
                    + "à une année académique."
            );
        }

        String idAaInscription =
                inscription.getAnneeAcademique().getIdAa();

        String idAaSession =
                sessionEvaluation.getAnneeAcademique().getIdAa();

        if (!idAaInscription.equals(idAaSession)) {
            throw new RuntimeException(
                    "L'année académique de l'inscription ne correspond pas "
                    + "à l'année académique de la session d'évaluation."
            );
        }
    }

    private StatutValidationUE determinerStatutValidationUE(
            double moyenneSurVingt,
            int nombreEC,
            int nombreECValides
    ) {
        if (nombreEC == 0) {
            return StatutValidationUE.NON_COMPOSEE;
        }

        if (moyenneSurVingt >= 10.0
                && nombreECValides == nombreEC) {
            return StatutValidationUE.VALIDEE;
        }

        if (nombreECValides > 0) {
            return StatutValidationUE.PARTIELLEMENT_VALIDEE;
        }

        return StatutValidationUE.NON_VALIDEE;
    }

    private String construireAppreciation(
            double moyenneSurVingt
    ) {
        if (moyenneSurVingt >= 16.0) {
            return "Très bien";
        }

        if (moyenneSurVingt >= 14.0) {
            return "Bien";
        }

        if (moyenneSurVingt >= 12.0) {
            return "Assez bien";
        }

        if (moyenneSurVingt >= 10.0) {
            return "Passable";
        }

        return "Insuffisant";
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
