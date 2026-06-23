
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.HoraireCours;
import com.visa_iss_kin.repository.ChargeHoraireRepository;
import com.visa_iss_kin.repository.HoraireCoursRepository;
import com.visa_iss_kin.repository.PromotionRepository;
import com.visa_iss_kin.service.HoraireCoursService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class HoraireCoursServiceImpl implements HoraireCoursService {
    private final HoraireCoursRepository horaireCoursRepository;
    private final ChargeHoraireRepository chargeHoraireRepository;
    private final PromotionRepository promotionRepository;

    public HoraireCoursServiceImpl(
            HoraireCoursRepository horaireCoursRepository,
            ChargeHoraireRepository chargeHoraireRepository,
            PromotionRepository promotionRepository
    ) {
        this.horaireCoursRepository = horaireCoursRepository;
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    public HoraireCours creerHoraireCours(
            HoraireCours horaireCours
    ) {
        verifierDonneesObligatoires(horaireCours);

        ChargeHoraire chargeHoraire =
                rechercherChargeHoraire(
                        horaireCours.getChargeHoraire()
                                .getIdChargeHoraire()
                );

        verifierCoherenceHeures(horaireCours);

        verifierConflitSalle(
                horaireCours,
                null
        );

        verifierConflitEnseignant(
                horaireCours,
                chargeHoraire,
                null
        );

        verifierConflitPromotion(
                horaireCours,
                chargeHoraire,
                null
        );

        horaireCours.setSalle(
                horaireCours.getSalle().trim()
        );

        horaireCours.setChargeHoraire(chargeHoraire);

        if (horaireCours.getCreatedAt() == null) {
            horaireCours.setCreatedAt(
                    LocalDateTime.now()
            );
        }

        return horaireCoursRepository.save(horaireCours);
    }

    @Override
    public List<HoraireCours> listerHorairesCours() {
        return horaireCoursRepository.findAll();
    }

    @Override
    public HoraireCours rechercherHoraireCoursParId(
            Long idHoraireCours
    ) {
        return horaireCoursRepository.findById(idHoraireCours)
                .orElseThrow(() -> new RuntimeException(
                        "Horaire de cours introuvable avec l'identifiant : "
                        + idHoraireCours
                ));
    }

    @Override
    public List<HoraireCours> listerHorairesParDate(
            LocalDate dateCours
    ) {
        if (dateCours == null) {
            throw new RuntimeException(
                    "La date du cours est obligatoire."
            );
        }

        return horaireCoursRepository
                .findByDateCoursOrderByHeureDebutAsc(
                        dateCours
                );
    }

    @Override
    public List<HoraireCours> listerHorairesEntreDeuxDates(
            LocalDate dateDebut,
            LocalDate dateFin
    ) {
        verifierPeriode(dateDebut, dateFin);

        return horaireCoursRepository
                .findByDateCoursBetweenOrderByDateCoursAscHeureDebutAsc(
                        dateDebut,
                        dateFin
                );
    }

    @Override
    public List<HoraireCours>
            listerHorairesEntreDeuxDatesParPromotion(
                    LocalDate dateDebut,
                    LocalDate dateFin,
                    String idPromo
            ) {
        verifierPeriode(dateDebut, dateFin);

        if (idPromo == null) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        if (!promotionRepository.existsById(idPromo)) {
            throw new RuntimeException(
                    "Promotion introuvable avec l'identifiant : "
                    + idPromo
            );
        }

        return horaireCoursRepository
                .listerHorairesEntreDeuxDatesParPromotion(
                        dateDebut,
                        dateFin,
                        idPromo
                );
    }

    @Override
    public List<HoraireCours> listerHorairesParSalle(
            String salle
    ) {
        if (salle == null || salle.trim().isEmpty()) {
            throw new RuntimeException(
                    "La salle est obligatoire."
            );
        }

        return horaireCoursRepository.findBySalle(
                salle.trim()
        );
    }

    @Override
    public List<HoraireCours> listerHorairesParChargeHoraire(
            Long idChargeHoraire
    ) {
        ChargeHoraire chargeHoraire =
                rechercherChargeHoraire(idChargeHoraire);

        return horaireCoursRepository
                .findByChargeHoraireOrderByDateCoursAscHeureDebutAsc(
                        chargeHoraire
                );
    }

    @Override
    public List<HoraireCours> listerHorairesParSalleEtDate(
            String salle,
            LocalDate dateCours
    ) {
        if (salle == null || salle.trim().isEmpty()) {
            throw new RuntimeException(
                    "La salle est obligatoire."
            );
        }

        if (dateCours == null) {
            throw new RuntimeException(
                    "La date du cours est obligatoire."
            );
        }

        return horaireCoursRepository
                .findBySalleAndDateCoursOrderByHeureDebutAsc(
                        salle.trim(),
                        dateCours
                );
    }

    @Override
    public HoraireCours modifierHoraireCours(
            Long idHoraireCours,
            HoraireCours horaireCours
    ) {
        HoraireCours horaireExistant =
                rechercherHoraireCoursParId(
                        idHoraireCours
                );

        if (horaireCours.getDateCours() != null) {
            horaireExistant.setDateCours(
                    horaireCours.getDateCours()
            );
        }

        if (horaireCours.getHeureDebut() != null) {
            horaireExistant.setHeureDebut(
                    horaireCours.getHeureDebut()
            );
        }

        if (horaireCours.getHeureFin() != null) {
            horaireExistant.setHeureFin(
                    horaireCours.getHeureFin()
            );
        }

        if (horaireCours.getSalle() != null
                && !horaireCours.getSalle().trim().isEmpty()) {
            horaireExistant.setSalle(
                    horaireCours.getSalle().trim()
            );
        }

        if (horaireCours.getObservation() != null) {
            horaireExistant.setObservation(
                    horaireCours.getObservation()
            );
        }

        if (horaireCours.getCreatedBy() != null) {
            horaireExistant.setCreatedBy(
                    horaireCours.getCreatedBy()
            );
        }

        ChargeHoraire chargeHoraire =
                horaireExistant.getChargeHoraire();

        if (horaireCours.getChargeHoraire() != null
                && horaireCours.getChargeHoraire()
                        .getIdChargeHoraire() != null) {

            chargeHoraire = rechercherChargeHoraire(
                    horaireCours.getChargeHoraire()
                            .getIdChargeHoraire()
            );
        }

        horaireExistant.setChargeHoraire(chargeHoraire);

        verifierDonneesObligatoires(horaireExistant);
        verifierCoherenceHeures(horaireExistant);

        verifierConflitSalle(
                horaireExistant,
                idHoraireCours
        );

        verifierConflitEnseignant(
                horaireExistant,
                chargeHoraire,
                idHoraireCours
        );

        verifierConflitPromotion(
                horaireExistant,
                chargeHoraire,
                idHoraireCours
        );

        return horaireCoursRepository.save(
                horaireExistant
        );
    }

    @Override
    public void supprimerHoraireCours(
            Long idHoraireCours
    ) {
        HoraireCours horaireCours =
                rechercherHoraireCoursParId(
                        idHoraireCours
                );

        horaireCoursRepository.delete(horaireCours);
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

    private void verifierDonneesObligatoires(
            HoraireCours horaireCours
    ) {
        if (horaireCours == null) {
            throw new RuntimeException(
                    "Les informations de l'horaire sont obligatoires."
            );
        }

        if (horaireCours.getDateCours() == null) {
            throw new RuntimeException(
                    "La date du cours est obligatoire."
            );
        }

        if (horaireCours.getHeureDebut() == null) {
            throw new RuntimeException(
                    "L'heure de début est obligatoire."
            );
        }

        if (horaireCours.getHeureFin() == null) {
            throw new RuntimeException(
                    "L'heure de fin est obligatoire."
            );
        }

        if (horaireCours.getSalle() == null
                || horaireCours.getSalle().trim().isEmpty()) {
            throw new RuntimeException(
                    "La salle est obligatoire."
            );
        }

        if (horaireCours.getChargeHoraire() == null
                || horaireCours.getChargeHoraire()
                        .getIdChargeHoraire() == null) {
            throw new RuntimeException(
                    "La charge horaire est obligatoire."
            );
        }
    }

    private void verifierCoherenceHeures(
            HoraireCours horaireCours
    ) {
        if (!horaireCours.getHeureFin()
                .isAfter(horaireCours.getHeureDebut())) {

            throw new RuntimeException(
                    "L'heure de fin doit être postérieure "
                    + "à l'heure de début."
            );
        }
    }

    private void verifierPeriode(
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

    private void verifierConflitSalle(
            HoraireCours horaireCours,
            Long idHoraireCoursAExclure
    ) {
        List<HoraireCours> horairesDeLaSalle =
                horaireCoursRepository
                        .findBySalleAndDateCoursOrderByHeureDebutAsc(
                                horaireCours.getSalle().trim(),
                                horaireCours.getDateCours()
                        );

        boolean conflit = horairesDeLaSalle.stream()
                .filter(horaire ->
                        idHoraireCoursAExclure == null
                        || !horaire.getIdHoraireCours()
                                .equals(idHoraireCoursAExclure)
                )
                .anyMatch(horaire ->
                        horairesSeChevauchent(
                                horaireCours,
                                horaire
                        )
                );

        if (conflit) {
            throw new RuntimeException(
                    "La salle est déjà occupée pendant "
                    + "cette tranche horaire."
            );
        }
    }

    private void verifierConflitEnseignant(
            HoraireCours horaireCours,
            ChargeHoraire chargeHoraire,
            Long idHoraireCoursAExclure
    ) {
        List<HoraireCours> horairesDuJour =
                horaireCoursRepository
                        .findByDateCoursOrderByHeureDebutAsc(
                                horaireCours.getDateCours()
                        );

        String matriculeEnseignant =
                chargeHoraire.getEnseignant().getMatrEns();

        boolean conflit = horairesDuJour.stream()
                .filter(horaire ->
                        idHoraireCoursAExclure == null
                        || !horaire.getIdHoraireCours()
                                .equals(idHoraireCoursAExclure)
                )
                .filter(horaire ->
                        horaire.getChargeHoraire()
                                .getEnseignant()
                                .getMatrEns()
                                .equals(matriculeEnseignant)
                )
                .anyMatch(horaire ->
                        horairesSeChevauchent(
                                horaireCours,
                                horaire
                        )
                );

        if (conflit) {
            throw new RuntimeException(
                    "Cet enseignant possède déjà un autre cours "
                    + "pendant cette tranche horaire."
            );
        }
    }

    private void verifierConflitPromotion(
            HoraireCours horaireCours,
            ChargeHoraire chargeHoraire,
            Long idHoraireCoursAExclure
    ) {
        List<HoraireCours> horairesDuJour =
                horaireCoursRepository
                        .findByDateCoursOrderByHeureDebutAsc(
                                horaireCours.getDateCours()
                        );

        String idPromo = chargeHoraire
                .getMaquettePromotion()
                .getPromotion()
                .getIdPromo();

        boolean conflit = horairesDuJour.stream()
                .filter(horaire ->
                        idHoraireCoursAExclure == null
                        || !horaire.getIdHoraireCours()
                                .equals(idHoraireCoursAExclure)
                )
                .filter(horaire ->
                        horaire.getChargeHoraire()
                                .getMaquettePromotion()
                                .getPromotion()
                                .getIdPromo()
                                .equals(idPromo)
                )
                .anyMatch(horaire ->
                        horairesSeChevauchent(
                                horaireCours,
                                horaire
                        )
                );

        if (conflit) {
            throw new RuntimeException(
                    "Cette promotion possède déjà un autre cours "
                    + "pendant cette tranche horaire."
            );
        }
    }

    private boolean horairesSeChevauchent(
            HoraireCours nouvelHoraire,
            HoraireCours horaireExistant
    ) {
        return nouvelHoraire.getHeureDebut()
                .isBefore(horaireExistant.getHeureFin())
                && nouvelHoraire.getHeureFin()
                        .isAfter(horaireExistant.getHeureDebut());
    }
}
