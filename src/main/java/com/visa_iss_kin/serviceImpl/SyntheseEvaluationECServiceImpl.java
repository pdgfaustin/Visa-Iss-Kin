
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import java.util.*;
import com.visa_iss_kin.service.SyntheseEvaluationECService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SyntheseEvaluationECServiceImpl implements SyntheseEvaluationECService {
    private final SyntheseEvaluationECRepository syntheseEvaluationECRepository;
    private final EvaluationEtudiantRepository evaluationEtudiantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;
    private final ElementConstitutifRepository elementConstitutifRepository;

    public SyntheseEvaluationECServiceImpl(
            SyntheseEvaluationECRepository syntheseEvaluationECRepository,
            EvaluationEtudiantRepository evaluationEtudiantRepository,
            InscriptionRepository inscriptionRepository,
            SessionEvaluationRepository sessionEvaluationRepository,
            ElementConstitutifRepository elementConstitutifRepository
    ) {
        this.syntheseEvaluationECRepository =
                syntheseEvaluationECRepository;
        this.evaluationEtudiantRepository =
                evaluationEtudiantRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.sessionEvaluationRepository = sessionEvaluationRepository;
        this.elementConstitutifRepository = elementConstitutifRepository;
    }

    @Override
    public SyntheseEvaluationEC genererSyntheseEvaluationEC(
            String idInscription,
            Long idSessionEvaluation,
            String idEC,
            String createdBy
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        verifierCoherenceInscriptionSession(
                inscription,
                sessionEvaluation
        );

        List<EvaluationEtudiant> evaluations =
                evaluationEtudiantRepository
                        .findByEpreuveSessionEvaluationIdSessionEvaluationAndEpreuvePromotionIdPromo(
                                idSessionEvaluation,
                                inscription.getPromotion().getIdPromo()
                        )
                        .stream()
                        .filter(evaluation ->
                                evaluation.getInscription() != null
                                && evaluation.getInscription()
                                        .getIdInscription()
                                        .equals(idInscription)
                        )
                        .filter(evaluation ->
                                evaluation.getEpreuve() != null
                                && evaluation.getEpreuve()
                                        .getElementConstitutif() != null
                                && evaluation.getEpreuve()
                                        .getElementConstitutif()
                                        .getIdEC()
                                        .equals(idEC)
                        )
                        .toList();

        SyntheseEvaluationEC synthese =
                syntheseEvaluationECRepository
                        .findByInscriptionAndSessionEvaluationAndElementConstitutif(
                                inscription,
                                sessionEvaluation,
                                elementConstitutif
                        )
                        .orElse(new SyntheseEvaluationEC());

        synthese.setInscription(inscription);
        synthese.setSessionEvaluation(sessionEvaluation);
        synthese.setElementConstitutif(elementConstitutif);
        synthese.setCreatedBy(createdBy);

        if (synthese.getCreatedAt() == null) {
            synthese.setCreatedAt(LocalDateTime.now());
        }

        if (evaluations.isEmpty()) {
            synthese.setNoteFinale(0.0);
            synthese.setNoteMaximale(20.0);
            synthese.setMoyenneSurVingt(0.0);
            synthese.setNombreEvaluations(0);
            synthese.setTotalPonderation(0.0);
            synthese.setAppreciation("Aucune évaluation");
            synthese.setStatutValidation(
                    StatutValidationEC.NON_COMPOSE
            );

            return syntheseEvaluationECRepository.save(synthese);
        }

        double sommePonderee = 0.0;
        double totalPonderation = 0.0;

        for (EvaluationEtudiant evaluation : evaluations) {

            double noteSurVingt =
                    convertirNoteSurVingt(
                            evaluation.getNoteObtenue(),
                            evaluation.getEpreuve().getNoteMaximale()
                    );

            double ponderation =
                    evaluation.getEpreuve().getPonderation() == null
                            ? 1.0
                            : evaluation.getEpreuve().getPonderation();

            sommePonderee += noteSurVingt * ponderation;
            totalPonderation += ponderation;
        }

        double moyenneSurVingt =
                totalPonderation <= 0.0
                        ? 0.0
                        : sommePonderee / totalPonderation;

        moyenneSurVingt = arrondir(moyenneSurVingt);

        synthese.setNoteFinale(moyenneSurVingt);
        synthese.setNoteMaximale(20.0);
        synthese.setMoyenneSurVingt(moyenneSurVingt);
        synthese.setNombreEvaluations(evaluations.size());
        synthese.setTotalPonderation(arrondir(totalPonderation));
        synthese.setAppreciation(
                construireAppreciation(moyenneSurVingt)
        );
        synthese.setStatutValidation(
                determinerStatutValidation(moyenneSurVingt)
        );

        return syntheseEvaluationECRepository.save(synthese);
    }

    @Override
    public List<SyntheseEvaluationEC>
            genererSynthesesParInscriptionEtSession(
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

        List<String> idsEC =
                evaluationEtudiantRepository
                        .findByEpreuveSessionEvaluationIdSessionEvaluationAndEpreuvePromotionIdPromo(
                                idSessionEvaluation,
                                inscription.getPromotion().getIdPromo()
                        )
                        .stream()
                        .filter(evaluation ->
                                evaluation.getInscription() != null
                                && evaluation.getInscription()
                                        .getIdInscription()
                                        .equals(idInscription)
                        )
                        .filter(evaluation ->
                                evaluation.getEpreuve() != null
                                && evaluation.getEpreuve()
                                        .getElementConstitutif() != null
                                && evaluation.getEpreuve()
                                        .getElementConstitutif()
                                        .getIdEC() != null
                        )
                        .map(evaluation ->
                                evaluation.getEpreuve()
                                        .getElementConstitutif()
                                        .getIdEC()
                        )
                        .distinct()
                        .toList();

        return idsEC.stream()
                .map(idEC ->
                        genererSyntheseEvaluationEC(
                                idInscription,
                                idSessionEvaluation,
                                idEC,
                                createdBy
                        )
                )
                .toList();
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesEvaluationEC() {
        return syntheseEvaluationECRepository.findAll();
    }

    @Override
    public SyntheseEvaluationEC rechercherSyntheseEvaluationECParId(
            Long idSyntheseEvaluationEC
    ) {
        return syntheseEvaluationECRepository
                .findById(idSyntheseEvaluationEC)
                .orElseThrow(() -> new RuntimeException(
                        "Synthèse d'évaluation EC introuvable avec "
                        + "l'identifiant : " + idSyntheseEvaluationEC
                ));
    }

    @Override
    public SyntheseEvaluationEC rechercherSyntheseParInscriptionSessionEtEC(
            String idInscription,
            Long idSessionEvaluation,
            String idEC
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        return syntheseEvaluationECRepository
                .findByInscriptionAndSessionEvaluationAndElementConstitutif(
                        inscription,
                        sessionEvaluation,
                        elementConstitutif
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune synthèse trouvée pour cette inscription, "
                        + "cette session et cet élément constitutif."
                ));
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParInscription(
            String idInscription
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        return syntheseEvaluationECRepository.findByInscription(
                inscription
        );
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParSession(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return syntheseEvaluationECRepository.findBySessionEvaluation(
                sessionEvaluation
        );
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParElementConstitutif(
            String idEC
    ) {
        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        return syntheseEvaluationECRepository
                .findByElementConstitutif(elementConstitutif);
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParStatutValidation(
            StatutValidationEC statutValidation
    ) {
        if (statutValidation == null) {
            throw new RuntimeException(
                    "Le statut de validation est obligatoire."
            );
        }

        return syntheseEvaluationECRepository
                .findByStatutValidation(statutValidation);
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return syntheseEvaluationECRepository
                .findByInscriptionPromotionIdPromo(idPromo.trim());
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParSessionEtPromotion(
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

        return syntheseEvaluationECRepository
                .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromo(
                        idSessionEvaluation,
                        idPromo.trim()
                );
    }

    @Override
    public List<SyntheseEvaluationEC> listerSynthesesParSessionPromotionEtEC(
            Long idSessionEvaluation,
            String idPromo,
            String idEC
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

        if (idEC == null) {
            throw new RuntimeException(
                    "L'identifiant de l'élément constitutif est obligatoire."
            );
        }

        return syntheseEvaluationECRepository
                .findBySessionEvaluationIdSessionEvaluationAndInscriptionPromotionIdPromoAndElementConstitutifIdEC(
                        idSessionEvaluation,
                        idPromo.trim(),
                        idEC
                );
    }

    @Override
    public void supprimerSyntheseEvaluationEC(
            Long idSyntheseEvaluationEC
    ) {
        SyntheseEvaluationEC synthese =
                rechercherSyntheseEvaluationECParId(
                        idSyntheseEvaluationEC
                );

        syntheseEvaluationECRepository.delete(synthese);
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

        return sessionEvaluationRepository
                .findById(idSessionEvaluation)
                .orElseThrow(() -> new RuntimeException(
                        "Session d'évaluation introuvable avec "
                        + "l'identifiant : " + idSessionEvaluation
                ));
    }

    private ElementConstitutif rechercherElementConstitutif(
            String idEC
    ) {
        if (idEC == null) {
            throw new RuntimeException(
                    "L'identifiant de l'élément constitutif est obligatoire."
            );
        }

        return elementConstitutifRepository.findById(idEC)
                .orElseThrow(() -> new RuntimeException(
                        "Élément constitutif introuvable avec "
                        + "l'identifiant : " + idEC
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

    private double convertirNoteSurVingt(
            Double noteObtenue,
            Double noteMaximale
    ) {
        if (noteObtenue == null
                || noteMaximale == null
                || noteMaximale <= 0.0) {
            return 0.0;
        }

        return noteObtenue * 20.0 / noteMaximale;
    }

    private StatutValidationEC determinerStatutValidation(
            double moyenneSurVingt
    ) {
        if (moyenneSurVingt >= 10.0) {
            return StatutValidationEC.VALIDE;
        }

        return StatutValidationEC.NON_VALIDE;
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
