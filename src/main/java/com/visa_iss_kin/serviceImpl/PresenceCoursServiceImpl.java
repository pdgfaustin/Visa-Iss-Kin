package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.PresenceCoursService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class PresenceCoursServiceImpl implements PresenceCoursService {
    private final PresenceCoursRepository presenceCoursRepository;
    private final HoraireCoursRepository horaireCoursRepository;
    private final ChargeHoraireRepository chargeHoraireRepository;
    private final EnseignantRepository enseignantRepository;
    private final PromotionRepository promotionRepository;

    public PresenceCoursServiceImpl(
            PresenceCoursRepository presenceCoursRepository,
            HoraireCoursRepository horaireCoursRepository,
            ChargeHoraireRepository chargeHoraireRepository,
            EnseignantRepository enseignantRepository,
            PromotionRepository promotionRepository
    ) {
        this.presenceCoursRepository = presenceCoursRepository;
        this.horaireCoursRepository = horaireCoursRepository;
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.enseignantRepository = enseignantRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    public PresenceCours creerPresenceCours(
            PresenceCours presenceCours
    ) {
        verifierDonneesObligatoires(presenceCours);

        HoraireCours horaireCours =
                rechercherHoraireCours(
                        presenceCours.getHoraireCours()
                                .getIdHoraireCours()
                );

        if (presenceCoursRepository.existsByHoraireCours(
                horaireCours
        )) {
            throw new RuntimeException(
                    "Une fiche de présence existe déjà "
                    + "pour cet horaire de cours."
            );
        }

        presenceCours.setHoraireCours(horaireCours);

        appliquerReglesSelonStatut(presenceCours);

        if (presenceCours.getCreatedAt() == null) {
            presenceCours.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        return presenceCoursRepository.save(presenceCours);
    }

    @Override
    public List<PresenceCours> listerPresencesCours() {
        return presenceCoursRepository.findAll();
    }

    @Override
    public PresenceCours rechercherPresenceCoursParId(
            Long idPresenceCours
    ) {
        return presenceCoursRepository
                .findById(idPresenceCours)
                .orElseThrow(() -> new RuntimeException(
                        "Présence de cours introuvable avec "
                        + "l'identifiant : " + idPresenceCours
                ));
    }

    @Override
    public PresenceCours rechercherPresenceParHoraire(
            Long idHoraireCours
    ) {
        HoraireCours horaireCours =
                rechercherHoraireCours(idHoraireCours);

        return presenceCoursRepository
                .findByHoraireCours(horaireCours)
                .orElseThrow(() -> new RuntimeException(
                        "Aucune fiche de présence trouvée "
                        + "pour l'horaire : " + idHoraireCours
                ));
    }

    @Override
    public List<PresenceCours> listerPresencesParStatut(
            StatutExecutionCours statutExecution
    ) {
        if (statutExecution == null) {
            throw new RuntimeException(
                    "Le statut d'exécution est obligatoire."
            );
        }

        return presenceCoursRepository.findByStatutExecution(
                statutExecution
        );
    }

    @Override
    public List<PresenceCours> listerPresencesParChargeHoraire(
            Long idChargeHoraire
    ) {
        if (idChargeHoraire == null) {
            throw new RuntimeException(
                    "L'identifiant de la charge horaire est obligatoire."
            );
        }

        if (!chargeHoraireRepository.existsById(
                idChargeHoraire
        )) {
            throw new RuntimeException(
                    "Charge horaire introuvable avec l'identifiant : "
                    + idChargeHoraire
            );
        }

        return presenceCoursRepository
                .findByHoraireCoursChargeHoraireIdChargeHoraire(
                        idChargeHoraire
                );
    }

    @Override
    public List<PresenceCours> listerPresencesParEnseignant(
            String matrEns
    ) {
        if (matrEns == null || matrEns.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le matricule de l'enseignant est obligatoire."
            );
        }

        if (!enseignantRepository.existsById(matrEns.trim())) {
            throw new RuntimeException(
                    "Enseignant introuvable avec le matricule : "
                    + matrEns
            );
        }

        return presenceCoursRepository
                .findByHoraireCoursChargeHoraireEnseignantMatrEns(
                        matrEns.trim()
                );
    }

    @Override
    public List<PresenceCours> listerPresencesParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        if (!promotionRepository.existsById(idPromo.trim())) {
            throw new RuntimeException(
                    "Promotion introuvable avec l'identifiant : "
                    + idPromo
            );
        }

        return presenceCoursRepository
                .findByHoraireCoursChargeHoraireMaquettePromotionPromotionIdPromo(
                        idPromo.trim()
                );
    }

    @Override
    public PresenceCours modifierPresenceCours(
            Long idPresenceCours,
            PresenceCours presenceCours
    ) {
        PresenceCours presenceExistante =
                rechercherPresenceCoursParId(idPresenceCours);

        if (presenceCours.getStatutExecution() != null) {
            presenceExistante.setStatutExecution(
                    presenceCours.getStatutExecution()
            );
        }

        if (presenceCours.getHeureArrivee() != null) {
            presenceExistante.setHeureArrivee(
                    presenceCours.getHeureArrivee()
            );
        }

        if (presenceCours.getHeureDebutEffective() != null) {
            presenceExistante.setHeureDebutEffective(
                    presenceCours.getHeureDebutEffective()
            );
        }

        if (presenceCours.getHeureFinEffective() != null) {
            presenceExistante.setHeureFinEffective(
                    presenceCours.getHeureFinEffective()
            );
        }

        if (presenceCours.getMotif() != null) {
            presenceExistante.setMotif(
                    presenceCours.getMotif()
            );
        }

        if (presenceCours.getObservation() != null) {
            presenceExistante.setObservation(
                    presenceCours.getObservation()
            );
        }

        if (presenceCours.getCreatedBy() != null) {
            presenceExistante.setCreatedBy(
                    presenceCours.getCreatedBy()
            );
        }

        if (presenceCours.getHoraireCours() != null
                && presenceCours.getHoraireCours()
                        .getIdHoraireCours() != null) {

            HoraireCours nouvelHoraire =
                    rechercherHoraireCours(
                            presenceCours.getHoraireCours()
                                    .getIdHoraireCours()
                    );

            presenceCoursRepository
                    .findByHoraireCours(nouvelHoraire)
                    .filter(presence ->
                            !presence.getIdPresenceCours()
                                    .equals(idPresenceCours)
                    )
                    .ifPresent(presence -> {
                        throw new RuntimeException(
                                "Une fiche de présence existe déjà "
                                + "pour le nouvel horaire."
                        );
                    });

            presenceExistante.setHoraireCours(
                    nouvelHoraire
            );
        }

        appliquerReglesSelonStatut(presenceExistante);

        return presenceCoursRepository.save(
                presenceExistante
        );
    }

    @Override
    public PresenceCours changerStatutExecution(
            Long idPresenceCours,
            StatutExecutionCours statutExecution
    ) {
        PresenceCours presenceCours =
                rechercherPresenceCoursParId(
                        idPresenceCours
                );

        if (statutExecution == null) {
            throw new RuntimeException(
                    "Le nouveau statut d'exécution est obligatoire."
            );
        }

        presenceCours.setStatutExecution(statutExecution);

        appliquerReglesSelonStatut(presenceCours);

        return presenceCoursRepository.save(
                presenceCours
        );
    }

    @Override
    public void supprimerPresenceCours(
            Long idPresenceCours
    ) {
        PresenceCours presenceCours =
                rechercherPresenceCoursParId(
                        idPresenceCours
                );

        presenceCoursRepository.delete(presenceCours);
    }

    private HoraireCours rechercherHoraireCours(
            Long idHoraireCours
    ) {
        if (idHoraireCours == null) {
            throw new RuntimeException(
                    "L'identifiant de l'horaire de cours est obligatoire."
            );
        }

        return horaireCoursRepository
                .findById(idHoraireCours)
                .orElseThrow(() -> new RuntimeException(
                        "Horaire de cours introuvable avec l'identifiant : "
                        + idHoraireCours
                ));
    }

    private void verifierDonneesObligatoires(
            PresenceCours presenceCours
    ) {
        if (presenceCours == null) {
            throw new RuntimeException(
                    "Les informations de présence sont obligatoires."
            );
        }

        if (presenceCours.getHoraireCours() == null
                || presenceCours.getHoraireCours()
                        .getIdHoraireCours() == null) {
            throw new RuntimeException(
                    "L'horaire de cours est obligatoire."
            );
        }

        if (presenceCours.getStatutExecution() == null) {
            presenceCours.setStatutExecution(
                    StatutExecutionCours.PROGRAMME
            );
        }
    }

    private void appliquerReglesSelonStatut(
            PresenceCours presenceCours
    ) {
        if (presenceCours.getStatutExecution()
                == StatutExecutionCours.EFFECTUE) {

            verifierHeuresEffectives(presenceCours);
            calculerNombreHeuresEffectuees(presenceCours);
            return;
        }

        if (presenceCours.getStatutExecution()
                == StatutExecutionCours.ANNULE
                || presenceCours.getStatutExecution()
                == StatutExecutionCours.REPORTE
                || presenceCours.getStatutExecution()
                == StatutExecutionCours.ABSENCE_ENSEIGNANT) {

            if (presenceCours.getMotif() == null
                    || presenceCours.getMotif()
                            .trim().isEmpty()) {
                throw new RuntimeException(
                        "Le motif est obligatoire pour le statut : "
                        + presenceCours.getStatutExecution()
                );
            }

            presenceCours.setNombreHeuresEffectuees(0.0);
            presenceCours.setHeureDebutEffective(null);
            presenceCours.setHeureFinEffective(null);
            return;
        }

        presenceCours.setNombreHeuresEffectuees(0.0);
    }

    private void verifierHeuresEffectives(
            PresenceCours presenceCours
    ) {
        if (presenceCours.getHeureDebutEffective() == null) {
            throw new RuntimeException(
                    "L'heure de début effective est obligatoire "
                    + "pour un cours effectué."
            );
        }

        if (presenceCours.getHeureFinEffective() == null) {
            throw new RuntimeException(
                    "L'heure de fin effective est obligatoire "
                    + "pour un cours effectué."
            );
        }

        if (!presenceCours.getHeureFinEffective()
                .isAfter(
                        presenceCours.getHeureDebutEffective()
                )) {
            throw new RuntimeException(
                    "L'heure de fin effective doit être postérieure "
                    + "à l'heure de début effective."
            );
        }
    }

    private void calculerNombreHeuresEffectuees(
            PresenceCours presenceCours
    ) {
        long minutesEffectuees = Duration.between(
                presenceCours.getHeureDebutEffective(),
                presenceCours.getHeureFinEffective()
        ).toMinutes();

        double nombreHeures =
                minutesEffectuees / 60.0;

        presenceCours.setNombreHeuresEffectuees(
                Math.round(nombreHeures * 100.0) / 100.0
        );
    }
}
