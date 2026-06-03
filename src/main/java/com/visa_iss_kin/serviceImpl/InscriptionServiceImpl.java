
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.Etudiant;
import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.model.StatutAnneeAcademique;
import com.visa_iss_kin.repository.AnneeAcademiqueRepository;
import com.visa_iss_kin.repository.EtudiantRepository;
import com.visa_iss_kin.repository.InscriptionRepository;
import com.visa_iss_kin.repository.PromotionRepository;
import com.visa_iss_kin.service.InscriptionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class InscriptionServiceImpl implements InscriptionService {
    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final PromotionRepository promotionRepository;
    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public InscriptionServiceImpl(
            InscriptionRepository inscriptionRepository,
            EtudiantRepository etudiantRepository,
            PromotionRepository promotionRepository,
            AnneeAcademiqueRepository anneeAcademiqueRepository
    ) {
        this.inscriptionRepository = inscriptionRepository;
        this.etudiantRepository = etudiantRepository;
        this.promotionRepository = promotionRepository;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    private String genererIdInscription(AnneeAcademique anneeAcademique) {

        String idAa = anneeAcademique.getIdAa();

        if (idAa == null || !idAa.contains("-")) {
            throw new RuntimeException("Format de l'année académique invalide. Exemple attendu : 2025-2026.");
        }

        String[] parties = idAa.split("-");

        if (parties.length != 2) {
            throw new RuntimeException("Format de l'année académique invalide. Exemple attendu : 2025-2026.");
        }

        String deuxiemeAnnee = parties[1];

        String prefix = "ISS." + deuxiemeAnnee + ".";

        return inscriptionRepository
                .findTopByIdInscriptionStartingWithOrderByIdInscriptionDesc(prefix)
                .map(derniereInscription -> {
                    String dernierId = derniereInscription.getIdInscription();

                    String partieNumerique = dernierId.substring(prefix.length());

                    long dernierNumero = Long.parseLong(partieNumerique);

                    long nouveauNumero = dernierNumero + 1;

                    return prefix + String.format("%016d", nouveauNumero);
                })
                .orElse(prefix + "0000000000000001");
    }

    @Override
    public Inscription creerInscription(Inscription inscription) {

        if (inscription.getIdInscription() == null || inscription.getIdInscription().trim().isEmpty()) {
            inscription.setIdInscription(genererIdInscription(inscription.getAnneeAcademique()));
        }

        if (inscription.getEtudiant() == null || inscription.getEtudiant().getIdEtudiant() == null) {
            throw new RuntimeException("L'étudiant est obligatoire pour l'inscription.");
        }

        if (inscription.getPromotion() == null || inscription.getPromotion().getIdPromo() == null) {
            throw new RuntimeException("La promotion est obligatoire pour l'inscription.");
        }

        if (inscription.getAnneeAcademique() == null || inscription.getAnneeAcademique().getIdAa() == null) {
            throw new RuntimeException("L'année académique est obligatoire pour l'inscription.");
        }

        Etudiant etudiant = etudiantRepository.findById(
                inscription.getEtudiant().getIdEtudiant()
        ).orElseThrow(() -> new RuntimeException(
                "Étudiant introuvable avec l'identifiant : "
                + inscription.getEtudiant().getIdEtudiant()
        ));

        Promotion promotion = promotionRepository.findById(
                inscription.getPromotion().getIdPromo()
        ).orElseThrow(() -> new RuntimeException(
                "Promotion introuvable avec l'identifiant : "
                + inscription.getPromotion().getIdPromo()
        ));

        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(
                inscription.getAnneeAcademique().getIdAa()
        ).orElseThrow(() -> new RuntimeException(
                "Année académique introuvable avec l'identifiant : "
                + inscription.getAnneeAcademique().getIdAa()
        ));

        if (anneeAcademique.getStatutAnneeAcademique() != StatutAnneeAcademique.OUVERTE) {
            throw new RuntimeException("Impossible d'inscrire : l'année académique n'est pas OUVERTE.");
        }

        if (inscriptionRepository.existsByEtudiantAndAnneeAcademique(etudiant, anneeAcademique)) {
            throw new RuntimeException("Cet étudiant est déjà inscrit pour cette année académique.");
        }

        inscription.setEtudiant(etudiant);
        inscription.setPromotion(promotion);
        inscription.setAnneeAcademique(anneeAcademique);

        if (inscription.getDateInscription() == null) {
            inscription.setDateInscription(LocalDate.now());
        }

        if (inscription.getStatutInscription() == null) {
            inscription.setStatutInscription(Inscription.StatutInscription.EN_COURS);
        }

        if (inscription.getCreatedAt() == null) {
            inscription.setCreatedAt(LocalDateTime.now());
        }

        return inscriptionRepository.save(inscription);
    }

    @Override
    public List<Inscription> listerInscriptions() {
        return inscriptionRepository.findAll();
    }

    @Override
    public Inscription rechercherInscriptionParId(String idInscription) {
        return inscriptionRepository.findById(idInscription)
                .orElseThrow(() -> new RuntimeException(
                        "Inscription introuvable avec l'identifiant : " + idInscription
                ));
    }

    @Override
    public List<Inscription> listerInscriptionsParEtudiant(String idEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new RuntimeException(
                        "Étudiant introuvable avec l'identifiant : " + idEtudiant
                ));

        return inscriptionRepository.findByEtudiant(etudiant);
    }

    @Override
    public List<Inscription> listerInscriptionsParPromotion(String idPromo) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        return inscriptionRepository.findByPromotion(promotion);
    }

    @Override
    public List<Inscription> listerInscriptionsParAnneeAcademique(String idAa) {
        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : " + idAa
                ));

        return inscriptionRepository.findByAnneeAcademique(anneeAcademique);
    }

    @Override
    public List<Inscription> listerInscriptionsParPromotionEtAnnee(String idPromo, String idAa) {
        Promotion promotion = promotionRepository.findById(idPromo)
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : " + idPromo
                ));

        AnneeAcademique anneeAcademique = anneeAcademiqueRepository.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : " + idAa
                ));

        return inscriptionRepository.findByPromotionAndAnneeAcademique(promotion, anneeAcademique);
    }

    @Override
    public Inscription modifierInscription(String idInscription, Inscription inscription) {
        Inscription inscriptionExistante = rechercherInscriptionParId(idInscription);

        if (inscriptionExistante.getStatutInscription() == Inscription.StatutInscription.ANNULEE) {
            throw new RuntimeException("Impossible de modifier une inscription ANNULEE.");
        }

        if (inscription.getPromotion() != null && inscription.getPromotion().getIdPromo() != null) {
            Promotion promotion = promotionRepository.findById(
                    inscription.getPromotion().getIdPromo()
            ).orElseThrow(() -> new RuntimeException(
                    "Promotion introuvable avec l'identifiant : "
                    + inscription.getPromotion().getIdPromo()
            ));

            inscriptionExistante.setPromotion(promotion);
        }

        if (inscription.getDateInscription() != null) {
            inscriptionExistante.setDateInscription(inscription.getDateInscription());
        }

        if (inscription.getCreatedBy() != null) {
            inscriptionExistante.setCreatedBy(inscription.getCreatedBy());
        }

        if (inscriptionExistante.getCreatedAt() == null) {
            inscriptionExistante.setCreatedAt(LocalDateTime.now());
        }

        return inscriptionRepository.save(inscriptionExistante);
    }

    @Override
    public Inscription validerInscription(String idInscription) {
        Inscription inscription = rechercherInscriptionParId(idInscription);

        if (inscription.getStatutInscription() == Inscription.StatutInscription.ANNULEE) {
            throw new RuntimeException("Impossible de valider une inscription ANNULEE.");
        }

        inscription.setStatutInscription(Inscription.StatutInscription.VALIDEE);

        return inscriptionRepository.save(inscription);
    }

    @Override
    public Inscription annulerInscription(String idInscription) {
        Inscription inscription = rechercherInscriptionParId(idInscription);

        if (inscription.getStatutInscription() == Inscription.StatutInscription.VALIDEE) {
            throw new RuntimeException("Impossible d'annuler directement une inscription VALIDEE sans autorisation spéciale.");
        }

        inscription.setStatutInscription(Inscription.StatutInscription.ANNULEE);

        return inscriptionRepository.save(inscription);
    }

    @Override
    public void supprimerInscription(String idInscription) {
        Inscription inscription = rechercherInscriptionParId(idInscription);

        if (inscription.getStatutInscription() == Inscription.StatutInscription.VALIDEE) {
            throw new RuntimeException("Impossible de supprimer une inscription VALIDEE.");
        }

        inscriptionRepository.delete(inscription);
    }
}
