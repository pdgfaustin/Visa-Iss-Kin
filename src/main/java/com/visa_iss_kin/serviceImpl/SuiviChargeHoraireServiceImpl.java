package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SuiviChargeHoraireService;
import com.visa_iss_kin.repository.*;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SuiviChargeHoraireServiceImpl implements SuiviChargeHoraireService {
    private final ChargeHoraireRepository chargeHoraireRepository;
    private final HoraireCoursRepository horaireCoursRepository;
    private final PresenceCoursRepository presenceCoursRepository;
    private final EnseignantRepository enseignantRepository;
    private final PromotionRepository promotionRepository;
    private final MaquettePromotionRepository maquettePromotionRepository;

    public SuiviChargeHoraireServiceImpl(
            ChargeHoraireRepository chargeHoraireRepository,
            HoraireCoursRepository horaireCoursRepository,
            PresenceCoursRepository presenceCoursRepository,
            EnseignantRepository enseignantRepository,
            PromotionRepository promotionRepository,
            MaquettePromotionRepository maquettePromotionRepository
    ) {
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.horaireCoursRepository = horaireCoursRepository;
        this.presenceCoursRepository = presenceCoursRepository;
        this.enseignantRepository = enseignantRepository;
        this.promotionRepository = promotionRepository;
        this.maquettePromotionRepository = maquettePromotionRepository;
    }

    @Override
    public SuiviChargeHoraire calculerSuiviParChargeHoraire(
            Long idChargeHoraire
    ) {
        ChargeHoraire chargeHoraire =
                rechercherChargeHoraire(idChargeHoraire);

        return construireSuiviChargeHoraire(chargeHoraire);
    }

    @Override
    public List<SuiviChargeHoraire> listerSuivisChargesHoraires() {
        return chargeHoraireRepository.findAll()
                .stream()
                .map(this::construireSuiviChargeHoraire)
                .toList();
    }

    @Override
    public List<SuiviChargeHoraire> listerSuivisParEnseignant(
            String matrEns
    ) {
        if (matrEns == null || matrEns.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le matricule de l'enseignant est obligatoire."
            );
        }

        Enseignant enseignant =
                enseignantRepository.findById(matrEns.trim())
                        .orElseThrow(() -> new RuntimeException(
                                "Enseignant introuvable avec le matricule : "
                                + matrEns
                        ));

        return chargeHoraireRepository.findByEnseignant(enseignant)
                .stream()
                .map(this::construireSuiviChargeHoraire)
                .toList();
    }

    @Override
    public List<SuiviChargeHoraire> listerSuivisParPromotion(
            String idPromo
    ) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        Promotion promotion =
                promotionRepository.findById(idPromo.trim())
                        .orElseThrow(() -> new RuntimeException(
                                "Promotion introuvable avec l'identifiant : "
                                + idPromo
                        ));

        return chargeHoraireRepository.findAll()
                .stream()
                .filter(charge ->
                        charge.getMaquettePromotion() != null
                        && charge.getMaquettePromotion()
                                .getPromotion() != null
                        && promotion.getIdPromo().equals(
                                charge.getMaquettePromotion()
                                        .getPromotion()
                                        .getIdPromo()
                        )
                )
                .map(this::construireSuiviChargeHoraire)
                .toList();
    }

    @Override
    public List<SuiviChargeHoraire> listerSuivisParMaquette(
            Long idMaquette
    ) {
        if (idMaquette == null) {
            throw new RuntimeException(
                    "L'identifiant de la maquette est obligatoire."
            );
        }

        MaquettePromotion maquettePromotion =
                maquettePromotionRepository.findById(idMaquette)
                        .orElseThrow(() -> new RuntimeException(
                                "Maquette introuvable avec l'identifiant : "
                                + idMaquette
                        ));

        return chargeHoraireRepository
                .findByMaquettePromotion(maquettePromotion)
                .stream()
                .map(this::construireSuiviChargeHoraire)
                .toList();
    }

    private SuiviChargeHoraire construireSuiviChargeHoraire(
            ChargeHoraire chargeHoraire
    ) {
        MaquettePromotion maquettePromotion =
                chargeHoraire.getMaquettePromotion();

        Enseignant enseignant =
                chargeHoraire.getEnseignant();

        Promotion promotion =
                maquettePromotion.getPromotion();

        ElementConstitutif elementConstitutif =
                maquettePromotion.getElementConstitutif();

        Integer volumeHorairePrevu =
                maquettePromotion.getVolumeHorairePrevu() == null
                        ? 0
                        : maquettePromotion.getVolumeHorairePrevu();

        Integer volumeHoraireAffecte =
                chargeHoraire.getNombreHeures() == null
                        ? 0
                        : chargeHoraire.getNombreHeures();

        double volumeHoraireProgramme =
                calculerVolumeHoraireProgramme(chargeHoraire);

        double volumeHoraireEffectue =
                calculerVolumeHoraireEffectue(chargeHoraire);

        double volumeRestantAProgrammer =
                Math.max(
                        0.0,
                        volumeHoraireAffecte - volumeHoraireProgramme
                );

        double volumeRestantAExecuter =
                Math.max(
                        0.0,
                        volumeHoraireAffecte - volumeHoraireEffectue
                );

        double tauxProgrammation =
                calculerPourcentage(
                        volumeHoraireProgramme,
                        volumeHoraireAffecte
                );

        double tauxExecution =
                calculerPourcentage(
                        volumeHoraireEffectue,
                        volumeHoraireAffecte
                );

        SuiviChargeHoraire suivi =
                new SuiviChargeHoraire();

        suivi.setIdChargeHoraire(
                chargeHoraire.getIdChargeHoraire()
        );

        suivi.setMatrEns(
                enseignant.getMatrEns()
        );

        suivi.setNomCompletEnseignant(
                construireNomCompletEnseignant(enseignant)
        );

        suivi.setIdPromo(
                promotion.getIdPromo()
        );

        suivi.setLibellePromotion(
                promotion.getLibePromo()
        );

        suivi.setIdMaquette(
                maquettePromotion.getIdMaquette()
        );

        suivi.setCodeEC(
                elementConstitutif.getCodeEC()
        );

        suivi.setLibelleEC(
                elementConstitutif.getLibeEC()
        );

        suivi.setTypeCharge(
                chargeHoraire.getTypeCharge()
        );

        suivi.setVolumeHorairePrevu(
                volumeHorairePrevu
        );

        suivi.setVolumeHoraireAffecte(
                volumeHoraireAffecte
        );

        suivi.setVolumeHoraireProgramme(
                arrondir(volumeHoraireProgramme)
        );

        suivi.setVolumeHoraireEffectue(
                arrondir(volumeHoraireEffectue)
        );

        suivi.setVolumeHoraireRestantAProgrammer(
                arrondir(volumeRestantAProgrammer)
        );

        suivi.setVolumeHoraireRestantAExecuter(
                arrondir(volumeRestantAExecuter)
        );

        suivi.setTauxProgrammation(
                arrondir(tauxProgrammation)
        );

        suivi.setTauxExecution(
                arrondir(tauxExecution)
        );

        return suivi;
    }

    private double calculerVolumeHoraireProgramme(
            ChargeHoraire chargeHoraire
    ) {
        List<HoraireCours> horaires =
                horaireCoursRepository
                        .findByChargeHoraireOrderByDateCoursAscHeureDebutAsc(
                                chargeHoraire
                        );

        return horaires.stream()
                .filter(horaire ->
                        horaire.getHeureDebut() != null
                        && horaire.getHeureFin() != null
                        && horaire.getHeureFin()
                                .isAfter(horaire.getHeureDebut())
                )
                .mapToDouble(horaire -> {
                    long minutes = Duration.between(
                            horaire.getHeureDebut(),
                            horaire.getHeureFin()
                    ).toMinutes();

                    return minutes / 60.0;
                })
                .sum();
    }

    private double calculerVolumeHoraireEffectue(
            ChargeHoraire chargeHoraire
    ) {
        List<PresenceCours> presences =
                presenceCoursRepository
                        .findByHoraireCoursChargeHoraireIdChargeHoraire(
                                chargeHoraire.getIdChargeHoraire()
                        );

        return presences.stream()
                .filter(presence ->
                        presence.getStatutExecution()
                                == StatutExecutionCours.EFFECTUE
                )
                .filter(presence ->
                        presence.getNombreHeuresEffectuees() != null
                )
                .mapToDouble(
                        PresenceCours::getNombreHeuresEffectuees
                )
                .sum();
    }

    private String construireNomCompletEnseignant(
            Enseignant enseignant
    ) {
        StringBuilder nomComplet =
                new StringBuilder();

        ajouterPartieNom(
                nomComplet,
                enseignant.getNomEns()
        );

        ajouterPartieNom(
                nomComplet,
                enseignant.getPostNomEns()
        );

        ajouterPartieNom(
                nomComplet,
                enseignant.getPrenomEns()
        );

        return nomComplet.toString().trim();
    }

    private void ajouterPartieNom(
            StringBuilder nomComplet,
            String partieNom
    ) {
        if (partieNom != null
                && !partieNom.trim().isEmpty()) {

            if (!nomComplet.isEmpty()) {
                nomComplet.append(" ");
            }

            nomComplet.append(partieNom.trim());
        }
    }

    private double calculerPourcentage(
            double valeur,
            int total
    ) {
        if (total <= 0) {
            return 0.0;
        }

        return valeur * 100.0 / total;
    }

    private double arrondir(double valeur) {
        return Math.round(valeur * 100.0) / 100.0;
    }

    private ChargeHoraire rechercherChargeHoraire(
            Long idChargeHoraire
    ) {
        if (idChargeHoraire == null) {
            throw new RuntimeException(
                    "L'identifiant de la charge horaire est obligatoire."
            );
        }

        return chargeHoraireRepository
                .findById(idChargeHoraire)
                .orElseThrow(() -> new RuntimeException(
                        "Charge horaire introuvable avec l'identifiant : "
                        + idChargeHoraire
                ));
    }
}
