
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.TypeCharge;
import com.visa_iss_kin.repository.ChargeHoraireRepository;
import com.visa_iss_kin.repository.EnseignantRepository;
import com.visa_iss_kin.repository.MaquettePromotionRepository;
import com.visa_iss_kin.service.ChargeHoraireService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class ChargeHoraireServiceImpl implements ChargeHoraireService {
    private final ChargeHoraireRepository chargeHoraireRepository;
    private final EnseignantRepository enseignantRepository;
    private final MaquettePromotionRepository maquettePromotionRepository;

    public ChargeHoraireServiceImpl(
            ChargeHoraireRepository chargeHoraireRepository,
            EnseignantRepository enseignantRepository,
            MaquettePromotionRepository maquettePromotionRepository
    ) {
        this.chargeHoraireRepository = chargeHoraireRepository;
        this.enseignantRepository = enseignantRepository;
        this.maquettePromotionRepository = maquettePromotionRepository;
    }

    @Override
    public ChargeHoraire creerChargeHoraire(
            ChargeHoraire chargeHoraire
    ) {

        if (chargeHoraire.getTypeCharge() == null) {
            throw new RuntimeException(
                    "Le type de charge est obligatoire."
            );
        }

        if (chargeHoraire.getNombreHeures() == null
                || chargeHoraire.getNombreHeures() <= 0) {
            throw new RuntimeException(
                    "Le nombre d'heures doit être strictement supérieur à zéro."
            );
        }

        if (chargeHoraire.getEnseignant() == null
                || chargeHoraire.getEnseignant().getMatrEns() == null
                || chargeHoraire.getEnseignant().getMatrEns().trim().isEmpty()) {
            throw new RuntimeException(
                    "L'enseignant est obligatoire."
            );
        }

        if (chargeHoraire.getMaquettePromotion() == null
                || chargeHoraire.getMaquettePromotion().getIdMaquette() == null) {
            throw new RuntimeException(
                    "La ligne de maquette est obligatoire."
            );
        }

        Enseignant enseignant = enseignantRepository.findById(
                chargeHoraire.getEnseignant().getMatrEns()
        ).orElseThrow(() -> new RuntimeException(
                "Enseignant introuvable avec le matricule : "
                + chargeHoraire.getEnseignant().getMatrEns()
        ));

        MaquettePromotion maquettePromotion =
                maquettePromotionRepository.findById(
                        chargeHoraire.getMaquettePromotion().getIdMaquette()
                ).orElseThrow(() -> new RuntimeException(
                        "Ligne de maquette introuvable avec l'identifiant : "
                        + chargeHoraire.getMaquettePromotion().getIdMaquette()
                ));

        boolean existeDeja =
                chargeHoraireRepository
                        .existsByEnseignantAndMaquettePromotionAndTypeCharge(
                                enseignant,
                                maquettePromotion,
                                chargeHoraire.getTypeCharge()
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Cette charge horaire existe déjà pour cet enseignant, "
                    + "cette ligne de maquette et ce type de charge."
            );
        }

        verifierVolumeHoraireDisponible(
                maquettePromotion,
                chargeHoraire.getNombreHeures(),
                null
        );

        if (chargeHoraire.getCreatedAt() == null) {
            chargeHoraire.setCreatedAt(LocalDateTime.now());
        }

        chargeHoraire.setEnseignant(enseignant);
        chargeHoraire.setMaquettePromotion(maquettePromotion);

        return chargeHoraireRepository.save(chargeHoraire);
    }

    @Override
    public List<ChargeHoraire> listerChargesHoraires() {
        return chargeHoraireRepository.findAll();
    }

    @Override
    public ChargeHoraire rechercherChargeHoraireParId(
            Long idChargeHoraire
    ) {
        return chargeHoraireRepository.findById(idChargeHoraire)
                .orElseThrow(() -> new RuntimeException(
                        "Charge horaire introuvable avec l'identifiant : "
                        + idChargeHoraire
                ));
    }

    @Override
    public List<ChargeHoraire> listerChargesParEnseignant(
            String matrEns
    ) {
        Enseignant enseignant = enseignantRepository.findById(matrEns)
                .orElseThrow(() -> new RuntimeException(
                        "Enseignant introuvable avec le matricule : "
                        + matrEns
                ));

        return chargeHoraireRepository.findByEnseignant(enseignant);
    }

    @Override
    public List<ChargeHoraire> listerChargesParMaquette(
            Long idMaquette
    ) {
        MaquettePromotion maquettePromotion =
                maquettePromotionRepository.findById(idMaquette)
                        .orElseThrow(() -> new RuntimeException(
                                "Ligne de maquette introuvable avec l'identifiant : "
                                + idMaquette
                        ));

        return chargeHoraireRepository.findByMaquettePromotion(
                maquettePromotion
        );
    }

    @Override
    public List<ChargeHoraire> listerChargesParType(
            TypeCharge typeCharge
    ) {
        return chargeHoraireRepository.findByTypeCharge(typeCharge);
    }

    @Override
    public List<ChargeHoraire> listerChargesParEnseignantEtType(
            String matrEns,
            TypeCharge typeCharge
    ) {
        Enseignant enseignant = enseignantRepository.findById(matrEns)
                .orElseThrow(() -> new RuntimeException(
                        "Enseignant introuvable avec le matricule : "
                        + matrEns
                ));

        return chargeHoraireRepository.findByEnseignantAndTypeCharge(
                enseignant,
                typeCharge
        );
    }

    @Override
    public List<ChargeHoraire> listerChargesParMaquetteEtType(
            Long idMaquette,
            TypeCharge typeCharge
    ) {
        MaquettePromotion maquettePromotion =
                maquettePromotionRepository.findById(idMaquette)
                        .orElseThrow(() -> new RuntimeException(
                                "Ligne de maquette introuvable avec l'identifiant : "
                                + idMaquette
                        ));

        return chargeHoraireRepository
                .findByMaquettePromotionAndTypeCharge(
                        maquettePromotion,
                        typeCharge
                );
    }

    @Override
    public ChargeHoraire modifierChargeHoraire(
            Long idChargeHoraire,
            ChargeHoraire chargeHoraire
    ) {
        ChargeHoraire chargeExistante =
                rechercherChargeHoraireParId(idChargeHoraire);

        Enseignant enseignant = chargeExistante.getEnseignant();
        MaquettePromotion maquettePromotion =
                chargeExistante.getMaquettePromotion();
        TypeCharge typeCharge = chargeExistante.getTypeCharge();

        if (chargeHoraire.getEnseignant() != null
                && chargeHoraire.getEnseignant().getMatrEns() != null) {

            enseignant = enseignantRepository.findById(
                    chargeHoraire.getEnseignant().getMatrEns()
            ).orElseThrow(() -> new RuntimeException(
                    "Enseignant introuvable avec le matricule : "
                    + chargeHoraire.getEnseignant().getMatrEns()
            ));
        }

        if (chargeHoraire.getMaquettePromotion() != null
                && chargeHoraire.getMaquettePromotion().getIdMaquette() != null) {

            maquettePromotion = maquettePromotionRepository.findById(
                    chargeHoraire.getMaquettePromotion().getIdMaquette()
            ).orElseThrow(() -> new RuntimeException(
                    "Ligne de maquette introuvable avec l'identifiant : "
                    + chargeHoraire.getMaquettePromotion().getIdMaquette()
            ));
        }

        if (chargeHoraire.getTypeCharge() != null) {
            typeCharge = chargeHoraire.getTypeCharge();
        }

        boolean combinaisonModifiee =
                !enseignant.getMatrEns().equals(
                        chargeExistante.getEnseignant().getMatrEns()
                )
                || !maquettePromotion.getIdMaquette().equals(
                        chargeExistante.getMaquettePromotion().getIdMaquette()
                )
                || typeCharge != chargeExistante.getTypeCharge();

        if (combinaisonModifiee
                && chargeHoraireRepository
                        .existsByEnseignantAndMaquettePromotionAndTypeCharge(
                                enseignant,
                                maquettePromotion,
                                typeCharge
                        )) {
            throw new RuntimeException(
                    "Cette combinaison enseignant, maquette et type de charge "
                    + "existe déjà."
            );
        }

        Integer nouveauNombreHeures = chargeExistante.getNombreHeures();

        if (chargeHoraire.getNombreHeures() != null) {
            if (chargeHoraire.getNombreHeures() <= 0) {
                throw new RuntimeException(
                        "Le nombre d'heures doit être strictement supérieur à zéro."
                );
            }

            nouveauNombreHeures = chargeHoraire.getNombreHeures();
        }

        verifierVolumeHoraireDisponible(
                maquettePromotion,
                nouveauNombreHeures,
                chargeExistante.getIdChargeHoraire()
        );

        chargeExistante.setEnseignant(enseignant);
        chargeExistante.setMaquettePromotion(maquettePromotion);
        chargeExistante.setTypeCharge(typeCharge);
        chargeExistante.setNombreHeures(nouveauNombreHeures);

        if (chargeHoraire.getCreatedBy() != null) {
            chargeExistante.setCreatedBy(
                    chargeHoraire.getCreatedBy()
            );
        }

        return chargeHoraireRepository.save(chargeExistante);
    }

    @Override
    public void supprimerChargeHoraire(
            Long idChargeHoraire
    ) {
        ChargeHoraire chargeHoraire =
                rechercherChargeHoraireParId(idChargeHoraire);

        chargeHoraireRepository.delete(chargeHoraire);
    }

    private void verifierVolumeHoraireDisponible(
            MaquettePromotion maquettePromotion,
            Integer nombreHeuresAAjouter,
            Long idChargeHoraireAExclure
    ) {
        List<ChargeHoraire> chargesExistantes =
                chargeHoraireRepository.findByMaquettePromotion(
                        maquettePromotion
                );

        int totalHeuresAffectees = chargesExistantes.stream()
                .filter(charge -> idChargeHoraireAExclure == null
                        || !charge.getIdChargeHoraire()
                                .equals(idChargeHoraireAExclure))
                .mapToInt(ChargeHoraire::getNombreHeures)
                .sum();

        int nouveauTotal =
                totalHeuresAffectees + nombreHeuresAAjouter;

        int volumeHorairePrevu =
                maquettePromotion.getVolumeHorairePrevu();

        if (nouveauTotal > volumeHorairePrevu) {
            int heuresDisponibles =
                    volumeHorairePrevu - totalHeuresAffectees;

            throw new RuntimeException(
                    "Charge horaire refusée : le volume horaire prévu est dépassé. "
                    + "Volume prévu : " + volumeHorairePrevu
                    + " heures, déjà affecté : " + totalHeuresAffectees
                    + " heures, disponible : " + heuresDisponibles
                    + " heures, tentative : " + nombreHeuresAAjouter
                    + " heures."
            );
        }
    }
}
