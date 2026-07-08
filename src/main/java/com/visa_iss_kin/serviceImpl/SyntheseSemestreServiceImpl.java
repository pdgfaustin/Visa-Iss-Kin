package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.SyntheseSemestreService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SyntheseSemestreServiceImpl implements SyntheseSemestreService {
    private final SyntheseSemestreRepository syntheseSemestreRepository;
    private final SyntheseUERepository syntheseUERepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;

    public SyntheseSemestreServiceImpl(
            SyntheseSemestreRepository syntheseSemestreRepository,
            SyntheseUERepository syntheseUERepository,
            InscriptionRepository inscriptionRepository,
            SessionEvaluationRepository sessionEvaluationRepository
    ) {
        this.syntheseSemestreRepository =
                syntheseSemestreRepository;
        this.syntheseUERepository = syntheseUERepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionEvaluationRepository = sessionEvaluationRepository;
    }

    @Override
    public SyntheseSemestre genererSyntheseSemestre(
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

        List<SyntheseUE> synthesesUE =
                syntheseUERepository
                        .findByInscriptionAndSessionEvaluation(
                                inscription,
                                sessionEvaluation
                        );

        SyntheseSemestre syntheseSemestre =
                syntheseSemestreRepository
                        .findByInscriptionAndSessionEvaluation(
                                inscription,
                                sessionEvaluation
                        )
                        .orElse(new SyntheseSemestre());

        syntheseSemestre.setInscription(inscription);
        syntheseSemestre.setSessionEvaluation(sessionEvaluation);
        syntheseSemestre.setCreatedBy(createdBy);

        if (syntheseSemestre.getCreatedAt() == null) {
            syntheseSemestre.setCreatedAt(LocalDateTime.now());
        }

        if (synthesesUE.isEmpty()) {
            syntheseSemestre.setMoyenneSemestre(0.0);
            syntheseSemestre.setNoteMaximale(20.0);
            syntheseSemestre.setNombreUE(0);
            syntheseSemestre.setNombreUEValidees(0);
            syntheseSemestre.setNombreUENonValidees(0);
            syntheseSemestre.setNombreUEPartiellementValidees(0);
            syntheseSemestre.setTotalCredits(0.0);
            syntheseSemestre.setCreditsValides(0.0);
            syntheseSemestre.setTauxCreditsValides(0.0);
            syntheseSemestre.setAppreciation("Aucune synthèse UE");
            syntheseSemestre.setStatutValidationSemestre(
                    StatutValidationSemestre.NON_COMPOSE
            );

            return syntheseSemestreRepository.save(syntheseSemestre);
        }

        double sommePonderee = 0.0;
        double totalCredits = 0.0;
        double creditsValides = 0.0;

        int nombreUEValidees = 0;
        int nombreUENonValidees = 0;
        int nombreUEPartiellementValidees = 0;

        for (SyntheseUE syntheseUE : synthesesUE) {

            double creditsUE =
                    syntheseUE.getTotalCredits() == null
                            ? 0.0
                            : syntheseUE.getTotalCredits();

            double moyenneUE =
                    syntheseUE.getMoyenneSurVingt() == null
                            ? 0.0
                            : syntheseUE.getMoyenneSurVingt();

            sommePonderee += moyenneUE * creditsUE;
            totalCredits += creditsUE;

            if (syntheseUE.getStatutValidationUE()
                    == StatutValidationUE.VALIDEE) {
                nombreUEValidees++;
                creditsValides += creditsUE;
            } else if (syntheseUE.getStatutValidationUE()
                    == StatutValidationUE.PARTIELLEMENT_VALIDEE) {
                nombreUEPartiellementValidees++;
                creditsValides += syntheseUE.getCreditsValides() == null
                        ? 0.0
                        : syntheseUE.getCreditsValides();
            } else {
                nombreUENonValidees++;
            }
        }

        double moyenneSemestre =
                totalCredits <= 0.0
                        ? 0.0
                        : sommePonderee / totalCredits;

        double tauxCreditsValides =
                totalCredits <= 0.0
                        ? 0.0
                        : creditsValides * 100.0 / totalCredits;

        moyenneSemestre = arrondir(moyenneSemestre);
        tauxCreditsValides = arrondir(tauxCreditsValides);

        syntheseSemestre.setMoyenneSemestre(moyenneSemestre);
        syntheseSemestre.setNoteMaximale(20.0);
        syntheseSemestre.setNombreUE(synthesesUE.size());
        syntheseSemestre.setNombreUEValidees(nombreUEValidees);
        syntheseSemestre.setNombreUENonValidees(nombreUENonValidees);
        syntheseSemestre.setNombreUEPartiellementValidees(
                nombreUEPartiellementValidees
        );
        syntheseSemestre.setTotalCredits(arrondir(totalCredits));
        syntheseSemestre.setCreditsValides(arrondir(creditsValides));
        syntheseSemestre.setTauxCreditsValides(tauxCreditsValides);
        syntheseSemestre.setAppreciation(
                construireAppreciation(moyenneSemestre)
        );
        syntheseSemestre.setStatutValidationSemestre(
                determinerStatutValidationSemestre(
                        synthesesUE.size(),
                        nombreUEValidees,
                        nombreUEPartiellementValidees,
                        moyenneSemestre,
                        tauxCreditsValides
                )
        );

        return syntheseSemestreRepository.save(syntheseSemestre);
    }

    @Override
    public List<SyntheseSemestre>
            genererSynthesesSemestreParSessionEtPromotion(
                    Long idSessionEvaluation,
                    String idPromo,
                    String createdBy
            ) {

        rechercherSessionEvaluation(idSessionEvaluation);

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        List<String> idsInscriptions =
                syntheseUERepository
                        .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                                idSessionEvaluation,
                                idPromo.trim()
                        )
                        .stream()
                        .filter(syntheseUE ->
                                syntheseUE.getInscription() != null
                                && syntheseUE.getInscription()
                                        .getIdInscription() != null
                        )
                        .map(syntheseUE ->
                                syntheseUE.getInscription()
                                        .getIdInscription()
                        )
                        .distinct()
                        .toList();

        return idsInscriptions.stream()
                .map(idInscription ->
                        genererSyntheseSemestre(
                                idInscription,
                                idSessionEvaluation,
                                createdBy
                        )
                )
                .toList();
    }

    @Override
    public List<SyntheseSemestre> listerSynthesesSemestre() {
        return syntheseSemestreRepository.findAll();
    }

    @Override
    public SyntheseSemestre rechercherSyntheseSemestreParId(
            Long idSyntheseSemestre
    ) {
        return syntheseSemestreRepository.findById(idSyntheseSemestre)
                .orElseThrow(() -> new RuntimeException(
                        "Synthèse semestrielle introuvable avec "
                        + "l'identifiant : " + idSyntheseSemestre
                ));
    }

    @Override
    public SyntheseSemestre rechercherSyntheseSemestreParInscriptionEtSession(
            String idInscription,
            Long idSessionEvaluation
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return syntheseSemestreRepository
                .findByInscriptionAndSessionEvaluation(
                        inscription,
                        sessionEvaluation
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune synthèse semestrielle trouvée pour "
                        + "cette inscription et cette session."
                ));
    }

    @Override
    public List<SyntheseSemestre> listerSynthesesSemestreParInscription(
            String idInscription
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        return syntheseSemestreRepository.findByInscription(
                inscription
        );
    }

    @Override
    public List<SyntheseSemestre> listerSynthesesSemestreParSession(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return syntheseSemestreRepository.findBySessionEvaluation(
                sessionEvaluation
        );
    }

    @Override
    public List<SyntheseSemestre>
            listerSynthesesSemestreParStatutValidation(
                    StatutValidationSemestre statutValidationSemestre
            ) {

        if (statutValidationSemestre == null) {
            throw new RuntimeException(
                    "Le statut de validation du semestre est obligatoire."
            );
        }

        return syntheseSemestreRepository
                .findByStatutValidationSemestre(
                        statutValidationSemestre
                );
    }

    @Override
    public List<SyntheseSemestre> listerSynthesesSemestreParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return syntheseSemestreRepository
                .findByInscriptionPromotionIdPromo(idPromo.trim());
    }

    @Override
    public List<SyntheseSemestre>
            listerSynthesesSemestreParSessionEtPromotion(
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

        return syntheseSemestreRepository
                .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                        idSessionEvaluation,
                        idPromo.trim()
                );
    }

    @Override
    public List<SyntheseSemestre>
            listerSynthesesSemestreParSessionEtStatut(
                    Long idSessionEvaluation,
                    StatutValidationSemestre statutValidationSemestre
            ) {

        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        if (statutValidationSemestre == null) {
            throw new RuntimeException(
                    "Le statut de validation du semestre est obligatoire."
            );
        }

        return syntheseSemestreRepository
                .findBySessionEvaluationIdSessionEvaluationAndStatutValidationSemestre(
                        idSessionEvaluation,
                        statutValidationSemestre
                );
    }

    @Override
    public List<SyntheseSemestre>
            listerSynthesesSemestreParAnneeAcademiqueInscription(
                    String idAa
            ) {

        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        return syntheseSemestreRepository
                .findByInscriptionAnneeAcademiqueIdAa(idAa);
    }

    @Override
    public List<SyntheseSemestre>
            listerSynthesesSemestreParAnneeAcademiqueSession(
                    String idAa
            ) {

        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        return syntheseSemestreRepository
                .findBySessionEvaluationAnneeAcademiqueIdAa(idAa);
    }

    @Override
    public void supprimerSyntheseSemestre(
            Long idSyntheseSemestre
    ) {
        SyntheseSemestre syntheseSemestre =
                rechercherSyntheseSemestreParId(
                        idSyntheseSemestre
                );

        syntheseSemestreRepository.delete(syntheseSemestre);
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

    private StatutValidationSemestre determinerStatutValidationSemestre(
            int nombreUE,
            int nombreUEValidees,
            int nombreUEPartiellementValidees,
            double moyenneSemestre,
            double tauxCreditsValides
    ) {
        if (nombreUE == 0) {
            return StatutValidationSemestre.NON_COMPOSE;
        }

        if (moyenneSemestre >= 10.0
                && tauxCreditsValides >= 100.0
                && nombreUEValidees == nombreUE) {
            return StatutValidationSemestre.VALIDE;
        }

        if (nombreUEValidees > 0
                || nombreUEPartiellementValidees > 0
                || tauxCreditsValides > 0.0) {
            return StatutValidationSemestre.PARTIELLEMENT_VALIDE;
        }

        return StatutValidationSemestre.NON_VALIDE;
    }

    private String construireAppreciation(
            double moyenneSemestre
    ) {
        if (moyenneSemestre >= 16.0) {
            return "Très bien";
        }

        if (moyenneSemestre >= 14.0) {
            return "Bien";
        }

        if (moyenneSemestre >= 12.0) {
            return "Assez bien";
        }

        if (moyenneSemestre >= 10.0) {
            return "Passable";
        }

        return "Insuffisant";
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
