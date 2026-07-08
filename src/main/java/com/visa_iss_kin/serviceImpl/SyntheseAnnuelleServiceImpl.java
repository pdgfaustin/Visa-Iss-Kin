package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.SyntheseAnnuelleService;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SyntheseAnnuelleServiceImpl implements SyntheseAnnuelleService {
    private final SyntheseAnnuelleRepository syntheseAnnuelleRepository;
    private final SyntheseSemestreRepository syntheseSemestreRepository;
    private final InscriptionRepository inscriptionRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public SyntheseAnnuelleServiceImpl(
            SyntheseAnnuelleRepository syntheseAnnuelleRepository,
            SyntheseSemestreRepository syntheseSemestreRepository,
            InscriptionRepository inscriptionRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.syntheseAnnuelleRepository =
                syntheseAnnuelleRepository;
        this.syntheseSemestreRepository =
                syntheseSemestreRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.anneeAcademiqueRepository =
                anneeAcademiqueRepository;
    }

    @Override
    public SyntheseAnnuelle genererSyntheseAnnuelle(
            String idInscription,
            String idAa,
            String createdBy
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        verifierCoherenceInscriptionAnnee(
                inscription,
                anneeAcademique
        );

        List<SyntheseSemestre> synthesesSemestre =
                syntheseSemestreRepository
                        .findByInscriptionAnneeAcademiqueIdAa(idAa)
                        .stream()
                        .filter(syntheseSemestre ->
                                syntheseSemestre.getInscription() != null
                                && syntheseSemestre.getInscription()
                                        .getIdInscription() != null
                                && syntheseSemestre.getInscription()
                                        .getIdInscription()
                                        .equals(idInscription)
                        )
                        .toList();

        SyntheseAnnuelle syntheseAnnuelle =
                syntheseAnnuelleRepository
                        .findByInscriptionAndAnneeAcademique(
                                inscription,
                                anneeAcademique
                        )
                        .orElse(new SyntheseAnnuelle());

        syntheseAnnuelle.setInscription(inscription);
        syntheseAnnuelle.setAnneeAcademique(anneeAcademique);
        syntheseAnnuelle.setCreatedBy(createdBy);

        if (syntheseAnnuelle.getCreatedAt() == null) {
            syntheseAnnuelle.setCreatedAt(LocalDateTime.now());
        }

        if (synthesesSemestre.isEmpty()) {
            syntheseAnnuelle.setMoyenneAnnuelle(0.0);
            syntheseAnnuelle.setNoteMaximale(20.0);
            syntheseAnnuelle.setNombreSemestres(0);
            syntheseAnnuelle.setNombreSemestresValides(0);
            syntheseAnnuelle.setNombreSemestresNonValides(0);
            syntheseAnnuelle
                    .setNombreSemestresPartiellementValides(0);
            syntheseAnnuelle.setTotalCredits(0.0);
            syntheseAnnuelle.setCreditsValides(0.0);
            syntheseAnnuelle.setCreditsNonValides(0.0);
            syntheseAnnuelle.setTauxCreditsValides(0.0);
            syntheseAnnuelle.setAppreciation(
                    "Aucune synthèse semestrielle"
            );
            syntheseAnnuelle.setStatutValidationAnnee(
                    StatutValidationAnnee.NON_COMPOSE
            );
            syntheseAnnuelle.setDecisionFinale(
                    "Décision non disponible"
            );

            return syntheseAnnuelleRepository.save(
                    syntheseAnnuelle
            );
        }

        double sommePonderee = 0.0;
        double totalCredits = 0.0;
        double creditsValides = 0.0;

        int nombreSemestresValides = 0;
        int nombreSemestresNonValides = 0;
        int nombreSemestresPartiellementValides = 0;

        for (SyntheseSemestre syntheseSemestre
                : synthesesSemestre) {

            double creditsSemestre =
                    syntheseSemestre.getTotalCredits() == null
                            ? 0.0
                            : syntheseSemestre.getTotalCredits();

            double moyenneSemestre =
                    syntheseSemestre.getMoyenneSemestre() == null
                            ? 0.0
                            : syntheseSemestre.getMoyenneSemestre();

            sommePonderee += moyenneSemestre * creditsSemestre;
            totalCredits += creditsSemestre;

            if (syntheseSemestre.getStatutValidationSemestre()
                    == StatutValidationSemestre.VALIDE) {
                nombreSemestresValides++;
                creditsValides += creditsSemestre;
            } else if (
                    syntheseSemestre.getStatutValidationSemestre()
                    == StatutValidationSemestre.PARTIELLEMENT_VALIDE
            ) {
                nombreSemestresPartiellementValides++;
                creditsValides += syntheseSemestre
                        .getCreditsValides() == null
                                ? 0.0
                                : syntheseSemestre
                                        .getCreditsValides();
            } else {
                nombreSemestresNonValides++;
            }
        }

        double moyenneAnnuelle =
                totalCredits <= 0.0
                        ? 0.0
                        : sommePonderee / totalCredits;

        double tauxCreditsValides =
                totalCredits <= 0.0
                        ? 0.0
                        : creditsValides * 100.0 / totalCredits;

        double creditsNonValides =
                totalCredits - creditsValides;

        moyenneAnnuelle = arrondir(moyenneAnnuelle);
        tauxCreditsValides = arrondir(tauxCreditsValides);
        creditsNonValides = arrondir(creditsNonValides);

        StatutValidationAnnee statutValidationAnnee =
                determinerStatutValidationAnnee(
                        synthesesSemestre.size(),
                        nombreSemestresValides,
                        nombreSemestresPartiellementValides,
                        moyenneAnnuelle,
                        tauxCreditsValides
                );

        syntheseAnnuelle.setMoyenneAnnuelle(moyenneAnnuelle);
        syntheseAnnuelle.setNoteMaximale(20.0);
        syntheseAnnuelle.setNombreSemestres(
                synthesesSemestre.size()
        );
        syntheseAnnuelle.setNombreSemestresValides(
                nombreSemestresValides
        );
        syntheseAnnuelle.setNombreSemestresNonValides(
                nombreSemestresNonValides
        );
        syntheseAnnuelle.setNombreSemestresPartiellementValides(
                nombreSemestresPartiellementValides
        );
        syntheseAnnuelle.setTotalCredits(
                arrondir(totalCredits)
        );
        syntheseAnnuelle.setCreditsValides(
                arrondir(creditsValides)
        );
        syntheseAnnuelle.setCreditsNonValides(
                creditsNonValides
        );
        syntheseAnnuelle.setTauxCreditsValides(
                tauxCreditsValides
        );
        syntheseAnnuelle.setAppreciation(
                construireAppreciation(moyenneAnnuelle)
        );
        syntheseAnnuelle.setStatutValidationAnnee(
                statutValidationAnnee
        );
        syntheseAnnuelle.setDecisionFinale(
                construireDecisionFinale(statutValidationAnnee)
        );

        return syntheseAnnuelleRepository.save(syntheseAnnuelle);
    }

    @Override
    public List<SyntheseAnnuelle>
            genererSynthesesAnnuellesParAnneeEtPromotion(
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

        List<String> idsInscriptions =
                syntheseSemestreRepository
                        .findByInscriptionAnneeAcademiqueIdAa(idAa)
                        .stream()
                        .filter(syntheseSemestre ->
                                syntheseSemestre.getInscription() != null
                                && syntheseSemestre.getInscription()
                                        .getPromotion() != null
                                && syntheseSemestre.getInscription()
                                        .getPromotion()
                                        .getIdPromo() != null
                                && syntheseSemestre.getInscription()
                                        .getPromotion()
                                        .getIdPromo()
                                        .equals(idPromo.trim())
                        )
                        .filter(syntheseSemestre ->
                                syntheseSemestre.getInscription()
                                        .getIdInscription() != null
                        )
                        .map(syntheseSemestre ->
                                syntheseSemestre.getInscription()
                                        .getIdInscription()
                        )
                        .distinct()
                        .toList();

        return idsInscriptions.stream()
                .map(idInscription ->
                        genererSyntheseAnnuelle(
                                idInscription,
                                idAa,
                                createdBy
                        )
                )
                .toList();
    }

    @Override
    public List<SyntheseAnnuelle> listerSynthesesAnnuelles() {
        return syntheseAnnuelleRepository.findAll();
    }

    @Override
    public SyntheseAnnuelle rechercherSyntheseAnnuelleParId(
            Long idSyntheseAnnuelle
    ) {
        return syntheseAnnuelleRepository
                .findById(idSyntheseAnnuelle)
                .orElseThrow(() -> new RuntimeException(
                        "Synthèse annuelle introuvable avec "
                        + "l'identifiant : " + idSyntheseAnnuelle
                ));
    }

    @Override
    public SyntheseAnnuelle rechercherSyntheseAnnuelleParInscriptionEtAnnee(
            String idInscription,
            String idAa
    ) {
        Inscription inscription =
                rechercherInscription(idInscription);

        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        return syntheseAnnuelleRepository
                .findByInscriptionAndAnneeAcademique(
                        inscription,
                        anneeAcademique
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune synthèse annuelle trouvée pour "
                        + "cette inscription et cette année académique."
                ));
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParInscription(
                    String idInscription
            ) {

        Inscription inscription =
                rechercherInscription(idInscription);

        return syntheseAnnuelleRepository.findByInscription(
                inscription
        );
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParAnneeAcademique(
                    String idAa
            ) {

        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        return syntheseAnnuelleRepository.findByAnneeAcademique(
                anneeAcademique
        );
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParStatutValidation(
                    StatutValidationAnnee statutValidationAnnee
            ) {

        if (statutValidationAnnee == null) {
            throw new RuntimeException(
                    "Le statut de validation annuelle est obligatoire."
            );
        }

        return syntheseAnnuelleRepository
                .findByStatutValidationAnnee(
                        statutValidationAnnee
                );
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParPromotion(
                    String idPromo
            ) {

        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return syntheseAnnuelleRepository
                .findByInscriptionPromotionIdPromo(
                        idPromo.trim()
                );
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParAnneeEtPromotion(
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

        return syntheseAnnuelleRepository
                .findByAnneeAcademiqueIdAaAndInscriptionPromotionIdPromo(
                        idAa,
                        idPromo.trim()
                );
    }

    @Override
    public List<SyntheseAnnuelle>
            listerSynthesesAnnuellesParAnneeEtStatut(
                    String idAa,
                    StatutValidationAnnee statutValidationAnnee
            ) {

        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        if (statutValidationAnnee == null) {
            throw new RuntimeException(
                    "Le statut de validation annuelle est obligatoire."
            );
        }

        return syntheseAnnuelleRepository
                .findByAnneeAcademiqueIdAaAndStatutValidationAnnee(
                        idAa,
                        statutValidationAnnee
                );
    }

    @Override
    public void supprimerSyntheseAnnuelle(
            Long idSyntheseAnnuelle
    ) {
        SyntheseAnnuelle syntheseAnnuelle =
                rechercherSyntheseAnnuelleParId(
                        idSyntheseAnnuelle
                );

        syntheseAnnuelleRepository.delete(syntheseAnnuelle);
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
                        "Année académique introuvable avec "
                        + "l'identifiant : " + idAa
                ));
    }

    private void verifierCoherenceInscriptionAnnee(
            Inscription inscription,
            AnneeAcademique anneeAcademique
    ) {
        if (inscription.getAnneeAcademique() == null
                || inscription.getAnneeAcademique()
                        .getIdAa() == null) {
            throw new RuntimeException(
                    "L'inscription n'est pas correctement liée "
                    + "à une année académique."
            );
        }

        if (anneeAcademique.getIdAa() == null) {
            throw new RuntimeException(
                    "L'année académique est invalide."
            );
        }

        String idAaInscription =
                inscription.getAnneeAcademique().getIdAa();

        String idAa =
                anneeAcademique.getIdAa();

        if (!idAaInscription.equals(idAa)) {
            throw new RuntimeException(
                    "L'année académique de l'inscription ne correspond pas "
                    + "à l'année académique demandée."
            );
        }
    }

    private StatutValidationAnnee determinerStatutValidationAnnee(
            int nombreSemestres,
            int nombreSemestresValides,
            int nombreSemestresPartiellementValides,
            double moyenneAnnuelle,
            double tauxCreditsValides
    ) {
        if (nombreSemestres == 0) {
            return StatutValidationAnnee.NON_COMPOSE;
        }

        if (moyenneAnnuelle >= 10.0
                && tauxCreditsValides >= 100.0
                && nombreSemestresValides == nombreSemestres) {
            return StatutValidationAnnee.ADMIS;
        }

        if (moyenneAnnuelle >= 10.0
                && tauxCreditsValides >= 80.0) {
            return StatutValidationAnnee.ADMIS_AVEC_DETTES;
        }

        if (nombreSemestresValides > 0
                || nombreSemestresPartiellementValides > 0
                || tauxCreditsValides > 0.0) {
            return StatutValidationAnnee.PARTIELLEMENT_ADMIS;
        }

        return StatutValidationAnnee.AJOURNE;
    }

    private String construireDecisionFinale(
            StatutValidationAnnee statutValidationAnnee
    ) {
        if (statutValidationAnnee == StatutValidationAnnee.ADMIS) {
            return "Admis";
        }

        if (statutValidationAnnee
                == StatutValidationAnnee.ADMIS_AVEC_DETTES) {
            return "Admis avec dettes";
        }

        if (statutValidationAnnee
                == StatutValidationAnnee.PARTIELLEMENT_ADMIS) {
            return "Partiellement admis";
        }

        if (statutValidationAnnee == StatutValidationAnnee.AJOURNE) {
            return "Ajourné";
        }

        if (statutValidationAnnee
                == StatutValidationAnnee.NON_COMPOSE) {
            return "Non composé";
        }

        return "En attente";
    }

    private String construireAppreciation(
            double moyenneAnnuelle
    ) {
        if (moyenneAnnuelle >= 16.0) {
            return "Très bien";
        }

        if (moyenneAnnuelle >= 14.0) {
            return "Bien";
        }

        if (moyenneAnnuelle >= 12.0) {
            return "Assez bien";
        }

        if (moyenneAnnuelle >= 10.0) {
            return "Passable";
        }

        return "Insuffisant";
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
