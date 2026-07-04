package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.SessionEvaluationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SessionEvaluationServiceImpl implements SessionEvaluationService {
    private final SessionEvaluationRepository sessionEvaluationRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public SessionEvaluationServiceImpl(
            SessionEvaluationRepository sessionEvaluationRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.sessionEvaluationRepository = sessionEvaluationRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    @Override
    public SessionEvaluation creerSessionEvaluation(
            SessionEvaluation sessionEvaluation
    ) {
        verifierDonneesObligatoires(sessionEvaluation);

        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(
                        sessionEvaluation.getAnneeAcademique().getIdAa()
                );

        verifierCoherenceDates(
                sessionEvaluation.getDateDebut(),
                sessionEvaluation.getDateFin()
        );

        boolean existeDeja =
                sessionEvaluationRepository
                        .existsByAnneeAcademiqueAndSemestreAndTypeSession(
                                anneeAcademique,
                                sessionEvaluation.getSemestre(),
                                sessionEvaluation.getTypeSession()
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Cette session d'évaluation existe déjà "
                    + "pour cette année académique, ce semestre "
                    + "et ce type de session."
            );
        }

        sessionEvaluation.setAnneeAcademique(anneeAcademique);

        if (sessionEvaluation.getCreatedAt() == null) {
            sessionEvaluation.setCreatedAt(LocalDateTime.now());
        }

        if (sessionEvaluation.getOuverte() == null) {
            sessionEvaluation.setOuverte(false);
        }

        if (sessionEvaluation.getLibelleSession() == null
                || sessionEvaluation.getLibelleSession()
                        .trim().isEmpty()) {

            sessionEvaluation.setLibelleSession(
                    construireLibelleSession(sessionEvaluation)
            );
        }

        return sessionEvaluationRepository.save(sessionEvaluation);
    }

    @Override
    public List<SessionEvaluation> listerSessionsEvaluations() {
        return sessionEvaluationRepository.findAll();
    }

    @Override
    public SessionEvaluation rechercherSessionEvaluationParId(
            Long idSessionEvaluation
    ) {
        return sessionEvaluationRepository
                .findById(idSessionEvaluation)
                .orElseThrow(() -> new RuntimeException(
                        "Session d'évaluation introuvable avec "
                        + "l'identifiant : " + idSessionEvaluation
                ));
    }

    @Override
    public List<SessionEvaluation> listerSessionsParAnneeAcademique(
            String idAa
    ) {
        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        return sessionEvaluationRepository
                .findByAnneeAcademiqueOrderBySemestreAscTypeSessionAsc(
                        anneeAcademique
                );
    }

    @Override
    public List<SessionEvaluation> listerSessionsParSemestre(
            Semestre semestre
    ) {
        if (semestre == null) {
            throw new RuntimeException(
                    "Le semestre est obligatoire."
            );
        }

        return sessionEvaluationRepository.findBySemestre(semestre);
    }

    @Override
    public List<SessionEvaluation> listerSessionsParTypeSession(
            TypeSession typeSession
    ) {
        if (typeSession == null) {
            throw new RuntimeException(
                    "Le type de session est obligatoire."
            );
        }

        return sessionEvaluationRepository.findByTypeSession(
                typeSession
        );
    }

    @Override
    public List<SessionEvaluation> listerSessionsOuvertes() {
        return sessionEvaluationRepository.findByOuverte(true);
    }

    @Override
    public List<SessionEvaluation>
            listerSessionsOuvertesParAnneeAcademique(
                    String idAa
            ) {
        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        return sessionEvaluationRepository
                .findByAnneeAcademiqueAndOuverte(
                        anneeAcademique,
                        true
                );
    }

    @Override
    public SessionEvaluation rechercherSessionParAnneeSemestreEtType(
            String idAa,
            Semestre semestre,
            TypeSession typeSession
    ) {
        AnneeAcademique anneeAcademique =
                rechercherAnneeAcademique(idAa);

        if (semestre == null) {
            throw new RuntimeException(
                    "Le semestre est obligatoire."
            );
        }

        if (typeSession == null) {
            throw new RuntimeException(
                    "Le type de session est obligatoire."
            );
        }

        return sessionEvaluationRepository
                .findByAnneeAcademiqueAndSemestreAndTypeSession(
                        anneeAcademique,
                        semestre,
                        typeSession
                )
                .orElseThrow(() -> new RuntimeException(
                        "Aucune session trouvée pour cette année académique, "
                        + "ce semestre et ce type de session."
                ));
    }

    @Override
    public SessionEvaluation modifierSessionEvaluation(
            Long idSessionEvaluation,
            SessionEvaluation sessionEvaluation
    ) {
        SessionEvaluation sessionExistante =
                rechercherSessionEvaluationParId(
                        idSessionEvaluation
                );

        AnneeAcademique anneeAcademique =
                sessionExistante.getAnneeAcademique();

        if (sessionEvaluation.getAnneeAcademique() != null
                && sessionEvaluation.getAnneeAcademique()
                        .getIdAa() != null) {

            anneeAcademique = rechercherAnneeAcademique(
                    sessionEvaluation.getAnneeAcademique()
                            .getIdAa()
            );
        }

        Semestre semestre =
                sessionEvaluation.getSemestre() != null
                        ? sessionEvaluation.getSemestre()
                        : sessionExistante.getSemestre();

        TypeSession typeSession =
                sessionEvaluation.getTypeSession() != null
                        ? sessionEvaluation.getTypeSession()
                        : sessionExistante.getTypeSession();

        LocalDate dateDebut =
                sessionEvaluation.getDateDebut() != null
                        ? sessionEvaluation.getDateDebut()
                        : sessionExistante.getDateDebut();

        LocalDate dateFin =
                sessionEvaluation.getDateFin() != null
                        ? sessionEvaluation.getDateFin()
                        : sessionExistante.getDateFin();

        if (semestre == null) {
            throw new RuntimeException(
                    "Le semestre est obligatoire."
            );
        }

        if (typeSession == null) {
            throw new RuntimeException(
                    "Le type de session est obligatoire."
            );
        }

        verifierCoherenceDates(dateDebut, dateFin);

        boolean combinaisonModifiee =
                !anneeAcademique.getIdAa().equals(
                        sessionExistante.getAnneeAcademique()
                                .getIdAa()
                )
                || semestre != sessionExistante.getSemestre()
                || typeSession != sessionExistante.getTypeSession();

        if (combinaisonModifiee
                && sessionEvaluationRepository
                        .existsByAnneeAcademiqueAndSemestreAndTypeSession(
                                anneeAcademique,
                                semestre,
                                typeSession
                        )) {
            throw new RuntimeException(
                    "Une autre session existe déjà pour cette année, "
                    + "ce semestre et ce type de session."
            );
        }

        sessionExistante.setAnneeAcademique(anneeAcademique);
        sessionExistante.setSemestre(semestre);
        sessionExistante.setTypeSession(typeSession);
        sessionExistante.setDateDebut(dateDebut);
        sessionExistante.setDateFin(dateFin);

        if (sessionEvaluation.getLibelleSession() != null
                && !sessionEvaluation.getLibelleSession()
                        .trim().isEmpty()) {
            sessionExistante.setLibelleSession(
                    sessionEvaluation.getLibelleSession().trim()
            );
        }

        if (sessionEvaluation.getOuverte() != null) {
            sessionExistante.setOuverte(
                    sessionEvaluation.getOuverte()
            );
        }

        if (sessionEvaluation.getObservation() != null) {
            sessionExistante.setObservation(
                    sessionEvaluation.getObservation()
            );
        }

        if (sessionEvaluation.getCreatedBy() != null) {
            sessionExistante.setCreatedBy(
                    sessionEvaluation.getCreatedBy()
            );
        }

        return sessionEvaluationRepository.save(sessionExistante);
    }

    @Override
    public SessionEvaluation ouvrirSessionEvaluation(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluationParId(
                        idSessionEvaluation
                );

        sessionEvaluation.setOuverte(true);

        return sessionEvaluationRepository.save(sessionEvaluation);
    }

    @Override
    public SessionEvaluation fermerSessionEvaluation(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluationParId(
                        idSessionEvaluation
                );

        sessionEvaluation.setOuverte(false);

        return sessionEvaluationRepository.save(sessionEvaluation);
    }

    @Override
    public void supprimerSessionEvaluation(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluationParId(
                        idSessionEvaluation
                );

        if (Boolean.TRUE.equals(sessionEvaluation.getOuverte())) {
            throw new RuntimeException(
                    "Une session ouverte ne peut pas être supprimée. "
                    + "Veuillez d'abord la fermer."
            );
        }

        sessionEvaluationRepository.delete(sessionEvaluation);
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

    private void verifierDonneesObligatoires(
            SessionEvaluation sessionEvaluation
    ) {
        if (sessionEvaluation == null) {
            throw new RuntimeException(
                    "Les informations de la session sont obligatoires."
            );
        }

        if (sessionEvaluation.getAnneeAcademique() == null
                || sessionEvaluation.getAnneeAcademique()
                        .getIdAa() == null) {
            throw new RuntimeException(
                    "L'année académique est obligatoire."
            );
        }

        if (sessionEvaluation.getSemestre() == null) {
            throw new RuntimeException(
                    "Le semestre est obligatoire."
            );
        }

        if (sessionEvaluation.getTypeSession() == null) {
            throw new RuntimeException(
                    "Le type de session est obligatoire."
            );
        }

        if (sessionEvaluation.getDateDebut() == null) {
            throw new RuntimeException(
                    "La date de début est obligatoire."
            );
        }

        if (sessionEvaluation.getDateFin() == null) {
            throw new RuntimeException(
                    "La date de fin est obligatoire."
            );
        }
    }

    private void verifierCoherenceDates(
            LocalDate dateDebut,
            LocalDate dateFin
    ) {
        if (dateDebut == null) {
            throw new RuntimeException(
                    "La date de début est obligatoire."
            );
        }

        if (dateFin == null) {
            throw new RuntimeException(
                    "La date de fin est obligatoire."
            );
        }

        if (dateFin.isBefore(dateDebut)) {
            throw new RuntimeException(
                    "La date de fin ne peut pas être antérieure "
                    + "à la date de début."
            );
        }
    }

    private String construireLibelleSession(
            SessionEvaluation sessionEvaluation
    ) {
        return sessionEvaluation.getSemestre()
                + " - "
                + sessionEvaluation.getTypeSession();
    }
}
