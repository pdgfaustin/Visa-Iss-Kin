package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.EvaluationEtudiantService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class EvaluationEtudiantServiceImpl implements EvaluationEtudiantService {
    private final EvaluationEtudiantRepository evaluationEtudiantRepository;
    private final EpreuveRepository epreuveRepository;
    private final InscriptionRepository inscriptionRepository;

    public EvaluationEtudiantServiceImpl(
            EvaluationEtudiantRepository evaluationEtudiantRepository,
            EpreuveRepository epreuveRepository,
            InscriptionRepository inscriptionRepository
    ) {
        this.evaluationEtudiantRepository = evaluationEtudiantRepository;
        this.epreuveRepository = epreuveRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public EvaluationEtudiant creerEvaluationEtudiant(
            EvaluationEtudiant evaluationEtudiant
    ) {
        verifierDonneesObligatoires(evaluationEtudiant);

        Epreuve epreuve =
                rechercherEpreuve(
                        evaluationEtudiant.getEpreuve()
                                .getIdEpreuve()
                );

        Inscription inscription =
                rechercherInscription(
                        evaluationEtudiant.getInscription()
                                .getIdInscription()
                );

        verifierEpreuveOuverte(epreuve);

        verifierInscriptionCorrespondAEpreuve(
                inscription,
                epreuve
        );

        verifierNoteValide(
                evaluationEtudiant.getNoteObtenue(),
                epreuve
        );

        boolean existeDeja =
                evaluationEtudiantRepository
                        .existsByEpreuveAndInscription(
                                epreuve,
                                inscription
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Une note existe déjà pour cette inscription "
                    + "et cette épreuve."
            );
        }

        evaluationEtudiant.setEpreuve(epreuve);
        evaluationEtudiant.setInscription(inscription);

        evaluationEtudiant.setAppreciation(
                construireAppreciation(
                        evaluationEtudiant.getNoteObtenue(),
                        epreuve.getNoteMaximale()
                )
        );

        if (evaluationEtudiant.getCreatedAt() == null) {
            evaluationEtudiant.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        return evaluationEtudiantRepository.save(
                evaluationEtudiant
        );
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsEtudiants() {
        return evaluationEtudiantRepository.findAll();
    }

    @Override
    public EvaluationEtudiant rechercherEvaluationEtudiantParId(
            Long idEvaluationEtudiant
    ) {
        return evaluationEtudiantRepository
                .findById(idEvaluationEtudiant)
                .orElseThrow(() -> new RuntimeException(
                        "Évaluation étudiant introuvable avec "
                        + "l'identifiant : " + idEvaluationEtudiant
                ));
    }

    @Override
    public EvaluationEtudiant rechercherEvaluationParEpreuveEtInscription(
            Long idEpreuve,
            String idInscription
    ) {
        Epreuve epreuve = rechercherEpreuve(idEpreuve);
        Inscription inscription = rechercherInscription(idInscription);

        return evaluationEtudiantRepository
                .findByEpreuveAndInscription(
                        epreuve,
                        inscription
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune note trouvée pour cette épreuve "
                        + "et cette inscription."
                ));
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParEpreuve(
            Long idEpreuve
    ) {
        Epreuve epreuve = rechercherEpreuve(idEpreuve);

        return evaluationEtudiantRepository.findByEpreuve(epreuve);
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParInscription(
            String idInscription
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        return evaluationEtudiantRepository.findByInscription(
                inscription
        );
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return evaluationEtudiantRepository
                .findByEpreuvePromotionIdPromo(
                        idPromo.trim()
                );
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParElementConstitutif(
            String idEC
    ) {
        if (idEC == null) {
            throw new RuntimeException(
                    "L'identifiant de l'élément constitutif est obligatoire."
            );
        }

        return evaluationEtudiantRepository
                .findByEpreuveElementConstitutifIdEC(idEC);
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParSession(
            Long idSessionEvaluation
    ) {
        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        return evaluationEtudiantRepository
                .findByEpreuveSessionEvaluationIdSessionEvaluation(
                        idSessionEvaluation
                );
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParSessionEtPromotion(
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

        return evaluationEtudiantRepository
                .findByEpreuveSessionEvaluationIdSessionEvaluationAndEpreuvePromotionIdPromo(
                        idSessionEvaluation,
                        idPromo.trim()
                );
    }

    @Override
    public List<EvaluationEtudiant> listerEvaluationsParPromotionEtEC(
            String idPromo,
            String idEC
    ) {
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

        return evaluationEtudiantRepository
                .findByEpreuvePromotionIdPromoAndEpreuveElementConstitutifIdEC(
                        idPromo.trim(),
                        idEC
                );
    }

    @Override
    public EvaluationEtudiant modifierEvaluationEtudiant(
            Long idEvaluationEtudiant,
            EvaluationEtudiant evaluationEtudiant
    ) {
        EvaluationEtudiant evaluationExistante =
                rechercherEvaluationEtudiantParId(
                        idEvaluationEtudiant
                );

        Epreuve epreuve = evaluationExistante.getEpreuve();
        Inscription inscription =
                evaluationExistante.getInscription();

        if (evaluationEtudiant.getEpreuve() != null
                && evaluationEtudiant.getEpreuve()
                        .getIdEpreuve() != null) {

            epreuve = rechercherEpreuve(
                    evaluationEtudiant.getEpreuve()
                            .getIdEpreuve()
            );
        }

        if (evaluationEtudiant.getInscription() != null
                && evaluationEtudiant.getInscription()
                        .getIdInscription() != null
                && !evaluationEtudiant.getInscription()
                        .getIdInscription().trim().isEmpty()) {

            inscription = rechercherInscription(
                    evaluationEtudiant.getInscription()
                            .getIdInscription()
            );
        }

        verifierEpreuveOuverte(epreuve);

        verifierInscriptionCorrespondAEpreuve(
                inscription,
                epreuve
        );

        Double noteObtenue =
                evaluationEtudiant.getNoteObtenue() != null
                        ? evaluationEtudiant.getNoteObtenue()
                        : evaluationExistante.getNoteObtenue();

        verifierNoteValide(noteObtenue, epreuve);

        boolean combinaisonModifiee =
                !epreuve.getIdEpreuve().equals(
                        evaluationExistante.getEpreuve()
                                .getIdEpreuve()
                )
                || !inscription.getIdInscription().equals(
                        evaluationExistante.getInscription()
                                .getIdInscription()
                );

        if (combinaisonModifiee
                && evaluationEtudiantRepository
                        .existsByEpreuveAndInscription(
                                epreuve,
                                inscription
                        )) {
            throw new RuntimeException(
                    "Une autre note existe déjà pour cette épreuve "
                    + "et cette inscription."
            );
        }

        evaluationExistante.setEpreuve(epreuve);
        evaluationExistante.setInscription(inscription);
        evaluationExistante.setNoteObtenue(noteObtenue);

        evaluationExistante.setAppreciation(
                construireAppreciation(
                        noteObtenue,
                        epreuve.getNoteMaximale()
                )
        );

        if (evaluationEtudiant.getObservation() != null) {
            evaluationExistante.setObservation(
                    evaluationEtudiant.getObservation()
            );
        }

        if (evaluationEtudiant.getCreatedBy() != null) {
            evaluationExistante.setCreatedBy(
                    evaluationEtudiant.getCreatedBy()
            );
        }

        return evaluationEtudiantRepository.save(
                evaluationExistante
        );
    }

    @Override
    public void supprimerEvaluationEtudiant(
            Long idEvaluationEtudiant
    ) {
        EvaluationEtudiant evaluationEtudiant =
                rechercherEvaluationEtudiantParId(
                        idEvaluationEtudiant
                );

        verifierEpreuveOuverte(
                evaluationEtudiant.getEpreuve()
        );

        evaluationEtudiantRepository.delete(
                evaluationEtudiant
        );
    }

    private Epreuve rechercherEpreuve(Long idEpreuve) {
        if (idEpreuve == null) {
            throw new RuntimeException(
                    "L'identifiant de l'épreuve est obligatoire."
            );
        }

        return epreuveRepository.findById(idEpreuve)
                .orElseThrow(() -> new RuntimeException(
                        "Épreuve introuvable avec l'identifiant : "
                        + idEpreuve
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

        return inscriptionRepository.findById(
                idInscription.trim()
        ).orElseThrow(() -> new RuntimeException(
                "Inscription introuvable avec l'identifiant : "
                + idInscription
        ));
    }

    private void verifierDonneesObligatoires(
            EvaluationEtudiant evaluationEtudiant
    ) {
        if (evaluationEtudiant == null) {
            throw new RuntimeException(
                    "Les informations de l'évaluation sont obligatoires."
            );
        }

        if (evaluationEtudiant.getNoteObtenue() == null) {
            throw new RuntimeException(
                    "La note obtenue est obligatoire."
            );
        }

        if (evaluationEtudiant.getEpreuve() == null
                || evaluationEtudiant.getEpreuve()
                        .getIdEpreuve() == null) {
            throw new RuntimeException(
                    "L'épreuve est obligatoire."
            );
        }

        if (evaluationEtudiant.getInscription() == null
                || evaluationEtudiant.getInscription()
                        .getIdInscription() == null
                || evaluationEtudiant.getInscription()
                        .getIdInscription().trim().isEmpty()) {
            throw new RuntimeException(
                    "L'inscription est obligatoire."
            );
        }
    }

    private void verifierEpreuveOuverte(Epreuve epreuve) {
        if (!Boolean.TRUE.equals(epreuve.getOuverte())) {
            throw new RuntimeException(
                    "L'épreuve doit être ouverte pour saisir, modifier "
                    + "ou supprimer une note."
            );
        }
    }

    private void verifierNoteValide(
            Double noteObtenue,
            Epreuve epreuve
    ) {
        if (noteObtenue == null) {
            throw new RuntimeException(
                    "La note obtenue est obligatoire."
            );
        }

        if (noteObtenue < 0) {
            throw new RuntimeException(
                    "La note obtenue ne peut pas être négative."
            );
        }

        if (noteObtenue > epreuve.getNoteMaximale()) {
            throw new RuntimeException(
                    "La note obtenue ne peut pas dépasser la note maximale : "
                    + epreuve.getNoteMaximale()
            );
        }
    }

    private void verifierInscriptionCorrespondAEpreuve(
        Inscription inscription,
        Epreuve epreuve
) {
    if (inscription.getPromotion() == null
            || inscription.getPromotion().getIdPromo() == null) {
        throw new RuntimeException(
                "L'inscription n'est pas correctement liée "
                + "à une promotion."
        );
    }

    if (epreuve.getPromotion() == null
            || epreuve.getPromotion().getIdPromo() == null) {
        throw new RuntimeException(
                "L'épreuve n'est pas correctement liée "
                + "à une promotion."
        );
    }

    if (!inscription.getPromotion().getIdPromo()
            .equals(epreuve.getPromotion().getIdPromo())) {
        throw new RuntimeException(
                "L'étudiant inscrit n'appartient pas à la promotion "
                + "concernée par cette épreuve."
        );
    }

    if (inscription.getAnneeAcademique() == null
            || inscription.getAnneeAcademique().getIdAa() == null) {
        throw new RuntimeException(
                "L'inscription n'est pas correctement liée "
                + "à une année académique."
        );
    }

    if (epreuve.getSessionEvaluation() == null
            || epreuve.getSessionEvaluation()
                    .getAnneeAcademique() == null
            || epreuve.getSessionEvaluation()
                    .getAnneeAcademique()
                    .getIdAa() == null) {
        throw new RuntimeException(
                "L'épreuve n'est pas correctement liée "
                + "à une année académique via sa session."
        );
    }

    String idAaInscription =
            inscription.getAnneeAcademique().getIdAa();

    String idAaEpreuve =
            epreuve.getSessionEvaluation()
                    .getAnneeAcademique()
                    .getIdAa();

    if (!idAaInscription.equals(idAaEpreuve)) {
        throw new RuntimeException(
                "L'année académique de l'inscription ne correspond pas "
                + "à l'année académique de la session d'évaluation. "
                + "Année inscription : " + idAaInscription
                + ", année session : " + idAaEpreuve
        );
    }
}

    private String construireAppreciation(
            Double noteObtenue,
            Double noteMaximale
    ) {
        if (noteObtenue == null
                || noteMaximale == null
                || noteMaximale <= 0) {
            return null;
        }

        double noteSurVingt =
                noteObtenue * 20.0 / noteMaximale;

        if (noteSurVingt >= 16) {
            return "Très bien";
        }

        if (noteSurVingt >= 14) {
            return "Bien";
        }

        if (noteSurVingt >= 12) {
            return "Assez bien";
        }

        if (noteSurVingt >= 10) {
            return "Passable";
        }

        return "Insuffisant";
    }
}
