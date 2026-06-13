
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.ElementConstitutif;
import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.Obligatoire;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.model.Semestre;
import com.visa_iss_kin.repository.AnneeAcademiqueRepository;
import com.visa_iss_kin.repository.ElementConstitutifRepository;
import com.visa_iss_kin.repository.MaquettePromotionRepository;
import com.visa_iss_kin.repository.PromotionRepository;
import com.visa_iss_kin.service.MaquettePromotionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class MaquettePromotionServiceImpl implements MaquettePromotionService {
    private final MaquettePromotionRepository maquettePromotionRepository;
    private final PromotionRepository promotionRepository;
    private final ElementConstitutifRepository elementConstitutifRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public MaquettePromotionServiceImpl(
            MaquettePromotionRepository maquettePromotionRepository,
            PromotionRepository promotionRepository,
            ElementConstitutifRepository elementConstitutifRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.maquettePromotionRepository = maquettePromotionRepository;
        this.promotionRepository = promotionRepository;
        this.elementConstitutifRepository = elementConstitutifRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    @Override
    public MaquettePromotion creerMaquettePromotion(
            MaquettePromotion maquettePromotion
    ) {

        if (maquettePromotion.getSemestre() == null) {
            throw new RuntimeException("Le semestre est obligatoire.");
        }

        if (maquettePromotion.getCoefficient() == null
                || maquettePromotion.getCoefficient().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Le coefficient doit être strictement supérieur à zéro."
            );
        }

        if (maquettePromotion.getCreditApplicable() == null
                || maquettePromotion.getCreditApplicable() < 0) {
            throw new RuntimeException(
                    "Le nombre de crédits applicables doit être supérieur ou égal à zéro."
            );
        }

        if (maquettePromotion.getVolumeHorairePrevu() == null
                || maquettePromotion.getVolumeHorairePrevu() < 0) {
            throw new RuntimeException(
                    "Le volume horaire prévu doit être supérieur ou égal à zéro."
            );
        }

        if (maquettePromotion.getPromotion() == null
                || maquettePromotion.getPromotion().getIdPromo() == null) {
            throw new RuntimeException("La promotion est obligatoire.");
        }

        if (maquettePromotion.getElementConstitutif() == null
                || maquettePromotion.getElementConstitutif().getIdEC() == null) {
            throw new RuntimeException("L'élément constitutif est obligatoire.");
        }

        if (maquettePromotion.getAnneeAcademique() == null
                || maquettePromotion.getAnneeAcademique().getIdAa() == null) {
            throw new RuntimeException("L'année académique est obligatoire.");
        }

        Promotion promotion = promotionRepository.findById(
                maquettePromotion.getPromotion().getIdPromo()
        ).orElseThrow(() -> new RuntimeException(
                "Promotion introuvable avec l'identifiant : "
                + maquettePromotion.getPromotion().getIdPromo()
        ));

        ElementConstitutif elementConstitutif =
                elementConstitutifRepository.findById(
                        maquettePromotion.getElementConstitutif().getIdEC()
                ).orElseThrow(() -> new RuntimeException(
                        "Élément constitutif introuvable avec l'identifiant : "
                        + maquettePromotion.getElementConstitutif().getIdEC()
                ));

        AnneeAcademique anneeAcademique =
                anneeAcademiqueRepository.findById(
                        maquettePromotion.getAnneeAcademique().getIdAa()
                ).orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : "
                        + maquettePromotion.getAnneeAcademique().getIdAa()
                ));

        boolean existeDeja =
                maquettePromotionRepository
                        .existsByPromotionAndElementConstitutifAndAnneeAcademiqueAndSemestre(
                                promotion,
                                elementConstitutif,
                                anneeAcademique,
                                maquettePromotion.getSemestre()
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Cet élément constitutif est déjà inscrit dans la maquette "
                    + "de cette promotion, pour cette année académique et ce semestre."
            );
        }

        if (maquettePromotion.getObligatoire() == null) {
            maquettePromotion.setObligatoire(Obligatoire.OUI);
        }

        if (maquettePromotion.getCreatedAt() == null) {
            maquettePromotion.setCreatedAt(LocalDateTime.now());
        }

        maquettePromotion.setPromotion(promotion);
        maquettePromotion.setElementConstitutif(elementConstitutif);
        maquettePromotion.setAnneeAcademique(anneeAcademique);

        return maquettePromotionRepository.save(maquettePromotion);
    }

    @Override
    public List<MaquettePromotion> listerMaquettesPromotion() {
        return maquettePromotionRepository.findAll();
    }

    @Override
    public MaquettePromotion rechercherMaquettePromotionParId(
            Long idMaquette
    ) {
        return maquettePromotionRepository.findById(idMaquette)
                .orElseThrow(() -> new RuntimeException(
                        "Ligne de maquette introuvable avec l'identifiant : "
                        + idMaquette
                ));
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParPromotion(
            String idPromo
    ) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        return maquettePromotionRepository.findByPromotion(promotion);
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParElementConstitutif(
            String idEC
    ) {
        ElementConstitutif elementConstitutif =
                elementConstitutifRepository.findById(idEC)
                        .orElseThrow(() -> new RuntimeException(
                                "Élément constitutif introuvable avec l'identifiant : "
                                + idEC
                        ));

        return maquettePromotionRepository.findByElementConstitutif(
                elementConstitutif
        );
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParAnneeAcademique(
            String idAa
    ) {
        AnneeAcademique anneeAcademique =
                anneeAcademiqueRepository.findById(idAa)
                        .orElseThrow(() -> new RuntimeException(
                                "Année académique introuvable avec l'identifiant : "
                                + idAa
                        ));

        return maquettePromotionRepository.findByAnneeAcademique(
                anneeAcademique
        );
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParSemestre(
            Semestre semestre
    ) {
        return maquettePromotionRepository.findBySemestre(semestre);
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParPromotionEtAnnee(
            String idPromo,
            String idAa
    ) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        AnneeAcademique anneeAcademique =
                anneeAcademiqueRepository.findById(idAa)
                        .orElseThrow(() -> new RuntimeException(
                                "Année académique introuvable avec l'identifiant : "
                                + idAa
                        ));

        return maquettePromotionRepository.findByPromotionAndAnneeAcademique(
                promotion,
                anneeAcademique
        );
    }

    @Override
    public List<MaquettePromotion> listerMaquettesParPromotionAnneeEtSemestre(
            String idPromo,
            String idAa,
            Semestre semestre
    ) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        AnneeAcademique anneeAcademique =
                anneeAcademiqueRepository.findById(idAa)
                        .orElseThrow(() -> new RuntimeException(
                                "Année académique introuvable avec l'identifiant : "
                                + idAa
                        ));

        return maquettePromotionRepository
                .findByPromotionAndAnneeAcademiqueAndSemestre(
                        promotion,
                        anneeAcademique,
                        semestre
                );
    }

    @Override
    public MaquettePromotion modifierMaquettePromotion(
            Long idMaquette,
            MaquettePromotion maquettePromotion
    ) {
        MaquettePromotion maquetteExistante =
                rechercherMaquettePromotionParId(idMaquette);

        Promotion promotion = maquetteExistante.getPromotion();
        ElementConstitutif elementConstitutif =
                maquetteExistante.getElementConstitutif();
        AnneeAcademique anneeAcademique =
                maquetteExistante.getAnneeAcademique();
        Semestre semestre = maquetteExistante.getSemestre();

        if (maquettePromotion.getPromotion() != null
                && maquettePromotion.getPromotion().getIdPromo() != null) {
            promotion = promotionRepository.findById(
                    maquettePromotion.getPromotion().getIdPromo()
            ).orElseThrow(() -> new RuntimeException(
                    "Promotion introuvable avec l'identifiant : "
                    + maquettePromotion.getPromotion().getIdPromo()
            ));
        }

        if (maquettePromotion.getElementConstitutif() != null
                && maquettePromotion.getElementConstitutif().getIdEC() != null) {
            elementConstitutif = elementConstitutifRepository.findById(
                    maquettePromotion.getElementConstitutif().getIdEC()
            ).orElseThrow(() -> new RuntimeException(
                    "Élément constitutif introuvable avec l'identifiant : "
                    + maquettePromotion.getElementConstitutif().getIdEC()
            ));
        }

        if (maquettePromotion.getAnneeAcademique() != null
                && maquettePromotion.getAnneeAcademique().getIdAa() != null) {
            anneeAcademique = anneeAcademiqueRepository.findById(
                    maquettePromotion.getAnneeAcademique().getIdAa()
            ).orElseThrow(() -> new RuntimeException(
                    "Année académique introuvable avec l'identifiant : "
                    + maquettePromotion.getAnneeAcademique().getIdAa()
            ));
        }

        if (maquettePromotion.getSemestre() != null) {
            semestre = maquettePromotion.getSemestre();
        }

        boolean combinaisonModifiee =
                !promotion.getIdPromo().equals(
                        maquetteExistante.getPromotion().getIdPromo()
                )
                || !elementConstitutif.getIdEC().equals(
                        maquetteExistante.getElementConstitutif().getIdEC()
                )
                || !anneeAcademique.getIdAa().equals(
                        maquetteExistante.getAnneeAcademique().getIdAa()
                )
                || semestre != maquetteExistante.getSemestre();

        if (combinaisonModifiee
                && maquettePromotionRepository
                        .existsByPromotionAndElementConstitutifAndAnneeAcademiqueAndSemestre(
                                promotion,
                                elementConstitutif,
                                anneeAcademique,
                                semestre
                        )) {
            throw new RuntimeException(
                    "Cette combinaison promotion, élément constitutif, "
                    + "année académique et semestre existe déjà."
            );
        }

        if (maquettePromotion.getCoefficient() != null) {
            if (maquettePromotion.getCoefficient()
                    .compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException(
                        "Le coefficient doit être strictement supérieur à zéro."
                );
            }

            maquetteExistante.setCoefficient(
                    maquettePromotion.getCoefficient()
            );
        }

        if (maquettePromotion.getCreditApplicable() != null) {
            if (maquettePromotion.getCreditApplicable() < 0) {
                throw new RuntimeException(
                        "Le nombre de crédits applicables doit être supérieur ou égal à zéro."
                );
            }

            maquetteExistante.setCreditApplicable(
                    maquettePromotion.getCreditApplicable()
            );
        }

        if (maquettePromotion.getVolumeHorairePrevu() != null) {
            if (maquettePromotion.getVolumeHorairePrevu() < 0) {
                throw new RuntimeException(
                        "Le volume horaire prévu doit être supérieur ou égal à zéro."
                );
            }

            maquetteExistante.setVolumeHorairePrevu(
                    maquettePromotion.getVolumeHorairePrevu()
            );
        }

        if (maquettePromotion.getObligatoire() != null) {
            maquetteExistante.setObligatoire(
                    maquettePromotion.getObligatoire()
            );
        }

        if (maquettePromotion.getCreatedBy() != null) {
            maquetteExistante.setCreatedBy(
                    maquettePromotion.getCreatedBy()
            );
        }

        maquetteExistante.setPromotion(promotion);
        maquetteExistante.setElementConstitutif(elementConstitutif);
        maquetteExistante.setAnneeAcademique(anneeAcademique);
        maquetteExistante.setSemestre(semestre);

        return maquettePromotionRepository.save(maquetteExistante);
    }

    @Override
    public void supprimerMaquettePromotion(Long idMaquette) {
        MaquettePromotion maquettePromotion =
                rechercherMaquettePromotionParId(idMaquette);

        maquettePromotionRepository.delete(maquettePromotion);
    }
}
