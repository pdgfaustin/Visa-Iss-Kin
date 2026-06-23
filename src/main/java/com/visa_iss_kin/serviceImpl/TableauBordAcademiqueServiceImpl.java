package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class TableauBordAcademiqueServiceImpl implements TableauBordAcademiqueService {
    private final ChargeHoraireRepository chargeHoraireRepository;
    private final HoraireCoursRepository horaireCoursRepository;
    private final PresenceCoursRepository presenceCoursRepository;
    private final EnseignantRepository enseignantRepository;
    private final PromotionRepository promotionRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;
    private final SuiviChargeHoraireService suiviChargeHoraireService;

    public TableauBordAcademiqueServiceImpl(
            ChargeHoraireRepository chargeHoraireRepository,
            HoraireCoursRepository horaireCoursRepository,
            PresenceCoursRepository presenceCoursRepository,
            EnseignantRepository enseignantRepository,
            PromotionRepository promotionRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository,
            SuiviChargeHoraireService suiviChargeHoraireService
    ) {
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.horaireCoursRepository = horaireCoursRepository;
        this.presenceCoursRepository = presenceCoursRepository;
        this.enseignantRepository = enseignantRepository;
        this.promotionRepository = promotionRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
        this.suiviChargeHoraireService = suiviChargeHoraireService;
    }

    @Override
    public TableauBordAcademique genererTableauBordGlobal() {

        List<ChargeHoraire> charges =
                chargeHoraireRepository.findAll();

        return construireTableauBord(charges);
    }

    @Override
    public TableauBordAcademique genererTableauBordParPromotion(
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

        List<ChargeHoraire> charges =
                chargeHoraireRepository.findAll()
                        .stream()
                        .filter(charge ->
                                charge.getMaquettePromotion() != null
                                && charge.getMaquettePromotion()
                                        .getPromotion() != null
                                && idPromo.trim().equals(
                                        charge.getMaquettePromotion()
                                                .getPromotion()
                                                .getIdPromo()
                                )
                        )
                        .toList();

        return construireTableauBord(charges);
    }

    @Override
    public TableauBordAcademique genererTableauBordParEnseignant(
            String matrEns
    ) {
        if (matrEns == null || matrEns.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le matricule de l'enseignant est obligatoire."
            );
        }

        var enseignant =
                enseignantRepository.findById(matrEns.trim())
                        .orElseThrow(() -> new RuntimeException(
                                "Enseignant introuvable avec le matricule : "
                                + matrEns
                        ));

        List<ChargeHoraire> charges =
                chargeHoraireRepository.findByEnseignant(
                        enseignant
                );

        return construireTableauBord(charges);
    }

    @Override
    public TableauBordAcademique genererTableauBordParAnneeAcademique(
            String idAa
    ) {
        if (idAa == null) {
            throw new RuntimeException(
                    "L'identifiant de l'année académique est obligatoire."
            );
        }

        if (!anneeAcademiqueRepository.existsById(idAa)) {
            throw new RuntimeException(
                    "Année académique introuvable avec l'identifiant : "
                    + idAa
            );
        }

        List<ChargeHoraire> charges =
                chargeHoraireRepository.findAll()
                        .stream()
                        .filter(charge ->
                                charge.getMaquettePromotion() != null
                                && charge.getMaquettePromotion()
                                        .getAnneeAcademique() != null
                                && idAa.equals(
                                        charge.getMaquettePromotion()
                                                .getAnneeAcademique()
                                                .getIdAa()
                                )
                        )
                        .toList();

        return construireTableauBord(charges);
    }

    private TableauBordAcademique construireTableauBord(
            List<ChargeHoraire> charges
    ) {
        Set<Long> idsCharges = new HashSet<>();

        Set<String> matriculesEnseignants =
                new HashSet<>();

        Set<String> idsPromotions =
                new HashSet<>();

        Set<Long> idsMaquettes =
                new HashSet<>();

        double volumeHorairePrevu = 0.0;
        double volumeHoraireAffecte = 0.0;

        for (ChargeHoraire charge : charges) {

            if (charge.getIdChargeHoraire() != null) {
                idsCharges.add(
                        charge.getIdChargeHoraire()
                );
            }

            if (charge.getEnseignant() != null
                    && charge.getEnseignant()
                            .getMatrEns() != null) {

                matriculesEnseignants.add(
                        charge.getEnseignant()
                                .getMatrEns()
                );
            }

            if (charge.getMaquettePromotion() != null) {

                if (charge.getMaquettePromotion()
                        .getPromotion() != null
                        && charge.getMaquettePromotion()
                                .getPromotion()
                                .getIdPromo() != null) {

                    idsPromotions.add(
                            charge.getMaquettePromotion()
                                    .getPromotion()
                                    .getIdPromo()
                    );
                }

                Long idMaquette =
                        charge.getMaquettePromotion()
                                .getIdMaquette();

                if (idMaquette != null
                        && idsMaquettes.add(idMaquette)) {

                    Integer volumePrevu =
                            charge.getMaquettePromotion()
                                    .getVolumeHorairePrevu();

                    if (volumePrevu != null) {
                        volumeHorairePrevu += volumePrevu;
                    }
                }
            }

            if (charge.getNombreHeures() != null) {
                volumeHoraireAffecte +=
                        charge.getNombreHeures();
            }
        }

        List<HoraireCours> horaires =
                horaireCoursRepository.findAll()
                        .stream()
                        .filter(horaire ->
                                horaire.getChargeHoraire() != null
                                && horaire.getChargeHoraire()
                                        .getIdChargeHoraire() != null
                                && idsCharges.contains(
                                        horaire.getChargeHoraire()
                                                .getIdChargeHoraire()
                                )
                        )
                        .toList();

        List<PresenceCours> presences =
                presenceCoursRepository.findAll()
                        .stream()
                        .filter(presence ->
                                presence.getHoraireCours() != null
                                && presence.getHoraireCours()
                                        .getChargeHoraire() != null
                                && presence.getHoraireCours()
                                        .getChargeHoraire()
                                        .getIdChargeHoraire() != null
                                && idsCharges.contains(
                                        presence.getHoraireCours()
                                                .getChargeHoraire()
                                                .getIdChargeHoraire()
                                )
                        )
                        .toList();

        long nombreCoursEffectues =
                compterPresencesParStatut(
                        presences,
                        StatutExecutionCours.EFFECTUE
                );

        long nombreCoursAnnules =
                compterPresencesParStatut(
                        presences,
                        StatutExecutionCours.ANNULE
                );

        long nombreCoursReportes =
                compterPresencesParStatut(
                        presences,
                        StatutExecutionCours.REPORTE
                );

        long nombreAbsencesEnseignants =
                compterPresencesParStatut(
                        presences,
                        StatutExecutionCours.ABSENCE_ENSEIGNANT
                );

        List<SuiviChargeHoraire> suivis =
                charges.stream()
                        .map(charge ->
                                suiviChargeHoraireService
                                        .calculerSuiviParChargeHoraire(
                                                charge.getIdChargeHoraire()
                                        )
                        )
                        .toList();

        double volumeHoraireProgramme =
                suivis.stream()
                        .filter(suivi ->
                                suivi.getVolumeHoraireProgramme()
                                        != null
                        )
                        .mapToDouble(
                                SuiviChargeHoraire
                                        ::getVolumeHoraireProgramme
                        )
                        .sum();

        double volumeHoraireEffectue =
                suivis.stream()
                        .filter(suivi ->
                                suivi.getVolumeHoraireEffectue()
                                        != null
                        )
                        .mapToDouble(
                                SuiviChargeHoraire
                                        ::getVolumeHoraireEffectue
                        )
                        .sum();

        double volumeRestantAProgrammer =
                Math.max(
                        0.0,
                        volumeHoraireAffecte
                        - volumeHoraireProgramme
                );

        double volumeRestantAExecuter =
                Math.max(
                        0.0,
                        volumeHoraireAffecte
                        - volumeHoraireEffectue
                );

        double tauxGlobalProgrammation =
                calculerPourcentage(
                        volumeHoraireProgramme,
                        volumeHoraireAffecte
                );

        double tauxGlobalExecution =
                calculerPourcentage(
                        volumeHoraireEffectue,
                        volumeHoraireAffecte
                );

        TableauBordAcademique tableauBord =
                new TableauBordAcademique();

        tableauBord.setNombreEnseignants(
                (long) matriculesEnseignants.size()
        );

        tableauBord.setNombrePromotions(
                (long) idsPromotions.size()
        );

        tableauBord.setNombreMaquettes(
                (long) idsMaquettes.size()
        );

        tableauBord.setNombreChargesHoraires(
                (long) charges.size()
        );

        tableauBord.setNombreCoursProgrammes(
                (long) horaires.size()
        );

        tableauBord.setNombreCoursEffectues(
                nombreCoursEffectues
        );

        tableauBord.setNombreCoursAnnules(
                nombreCoursAnnules
        );

        tableauBord.setNombreCoursReportes(
                nombreCoursReportes
        );

        tableauBord.setNombreAbsencesEnseignants(
                nombreAbsencesEnseignants
        );

        tableauBord.setVolumeHorairePrevu(
                arrondir(volumeHorairePrevu)
        );

        tableauBord.setVolumeHoraireAffecte(
                arrondir(volumeHoraireAffecte)
        );

        tableauBord.setVolumeHoraireProgramme(
                arrondir(volumeHoraireProgramme)
        );

        tableauBord.setVolumeHoraireEffectue(
                arrondir(volumeHoraireEffectue)
        );

        tableauBord.setVolumeHoraireRestantAProgrammer(
                arrondir(volumeRestantAProgrammer)
        );

        tableauBord.setVolumeHoraireRestantAExecuter(
                arrondir(volumeRestantAExecuter)
        );

        tableauBord.setTauxGlobalProgrammation(
                arrondir(tauxGlobalProgrammation)
        );

        tableauBord.setTauxGlobalExecution(
                arrondir(tauxGlobalExecution)
        );

        return tableauBord;
    }

    private long compterPresencesParStatut(
            List<PresenceCours> presences,
            StatutExecutionCours statutExecution
    ) {
        return presences.stream()
                .filter(presence ->
                        presence.getStatutExecution()
                                == statutExecution
                )
                .count();
    }

    private double calculerPourcentage(
            double valeur,
            double total
    ) {
        if (total <= 0.0) {
            return 0.0;
        }

        return valeur * 100.0 / total;
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }
}
