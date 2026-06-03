package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.StatutAnneeAcademique;
import com.visa_iss_kin.repository.AnneeAcademiqueRepository;
import com.visa_iss_kin.service.AnneeAcademiqueService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class AnneeAcademiqueServiceImpl implements AnneeAcademiqueService {
    private final AnneeAcademiqueRepository anneeAAR;

    public AnneeAcademiqueServiceImpl(AnneeAcademiqueRepository anneeAAR) {
        this.anneeAAR = anneeAAR;
    }

    @Override
    public AnneeAcademique creerAnneeAcademique(AnneeAcademique anneeAcademique) {

        if (anneeAcademique.getIdAa() == null || anneeAcademique.getIdAa().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant de l'année académique idAa est obligatoire.");
        }

        if (anneeAcademique.getDateDebut() == null) {
            throw new RuntimeException("La date de début est obligatoire.");
        }

        if (anneeAcademique.getDateFin() == null) {
            throw new RuntimeException("La date de fin est obligatoire.");
        }

        if (!anneeAcademique.getDateFin().isAfter(anneeAcademique.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être postérieure à la date de début.");
        }

        if (anneeAcademique.getStatutAnneeAcademique()== null) {
            anneeAcademique.setStatutAnneeAcademique(StatutAnneeAcademique.EN_PREPARATION);
        }

        if (anneeAcademique.getStatutAnneeAcademique()== StatutAnneeAcademique.OUVERTE
                && anneeAAR.existsByStatutAnneeAcademique(StatutAnneeAcademique.OUVERTE)) {
            throw new RuntimeException("Impossible de créer cette année comme OUVERTE : une année académique est déjà ouverte.");
        }

        if (anneeAcademique.getCreatedAt() == null) {
            anneeAcademique.setCreatedAt(LocalDateTime.now());
        }

        return anneeAAR.save(anneeAcademique);
    }

    @Override
    public List<AnneeAcademique> listerAnneesAcademiques() {
        return anneeAAR.findAll();
    }

    @Override
    public AnneeAcademique rechercherAnneeAcademiqueParId(String idAa) {
        return anneeAAR.findById(idAa)
                .orElseThrow(() -> new RuntimeException(
                        "Année académique introuvable avec l'identifiant : " + idAa
                ));
    }

    @Override
    public List<AnneeAcademique> listerAnneesParStatut(StatutAnneeAcademique statutAnnee) {
        return anneeAAR.findByStatutAnneeAcademique(statutAnnee);
    }

    @Override
    public AnneeAcademique modifierAnneeAcademique(String idAa, AnneeAcademique anneeAcademique) {
        AnneeAcademique anneeExistante = rechercherAnneeAcademiqueParId(idAa);

        if (anneeExistante.getStatutAnneeAcademique()== StatutAnneeAcademique.FERMEE) {
            throw new RuntimeException("Impossible de modifier une année académique déjà FERMEE.");
        }

        if (anneeAcademique.getDateDebut() != null) {
            anneeExistante.setDateDebut(anneeAcademique.getDateDebut());
        }

        if (anneeAcademique.getDateFin() != null) {
            anneeExistante.setDateFin(anneeAcademique.getDateFin());
        }

        if (anneeExistante.getDateDebut() != null
                && anneeExistante.getDateFin() != null
                && !anneeExistante.getDateFin().isAfter(anneeExistante.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être postérieure à la date de début.");
        }

        if (anneeAcademique.getCreatedBy() != null) {
            anneeExistante.setCreatedBy(anneeAcademique.getCreatedBy());
        }

        if (anneeExistante.getCreatedAt() == null) {
            anneeExistante.setCreatedAt(LocalDateTime.now());
        }

        return anneeAAR.save(anneeExistante);
    }

    @Override
    public AnneeAcademique ouvrirAnneeAcademique(String idAa) {
        AnneeAcademique annee = rechercherAnneeAcademiqueParId(idAa);

        if (annee.getStatutAnneeAcademique()== StatutAnneeAcademique.FERMEE) {
            throw new RuntimeException("Impossible d'ouvrir une année académique déjà FERMEE.");
        }

        if (anneeAAR.existsByStatutAnneeAcademique(StatutAnneeAcademique.OUVERTE)) {
            List<AnneeAcademique> anneesOuvertes =
                    anneeAAR.findByStatutAnneeAcademique(StatutAnneeAcademique.OUVERTE);

            boolean memeAnnee = anneesOuvertes.stream()
                    .anyMatch(a -> a.getIdAa().equals(idAa));

            if (!memeAnnee) {
                throw new RuntimeException("Impossible d'ouvrir cette année : une autre année académique est déjà OUVERTE.");
            }
        }

        annee.setStatutAnneeAcademique(StatutAnneeAcademique.OUVERTE);

        if (annee.getCreatedAt() == null) {
            annee.setCreatedAt(LocalDateTime.now());
        }

        return anneeAAR.save(annee);
    }

    @Override
    public AnneeAcademique fermerAnneeAcademique(String idAa) {
        AnneeAcademique annee = rechercherAnneeAcademiqueParId(idAa);

        if (annee.getStatutAnneeAcademique()== StatutAnneeAcademique.FERMEE) {
            throw new RuntimeException("Cette année académique est déjà FERMEE.");
        }

        annee.setStatutAnneeAcademique(StatutAnneeAcademique.FERMEE);

        if (annee.getCreatedAt() == null) {
            annee.setCreatedAt(LocalDateTime.now());
        }

        return anneeAAR.save(annee);
    }

    @Override
    public void supprimerAnneeAcademique(String idAa) {
        AnneeAcademique annee = rechercherAnneeAcademiqueParId(idAa);

        if (annee.getStatutAnneeAcademique()== StatutAnneeAcademique.OUVERTE) {
            throw new RuntimeException("Impossible de supprimer une année académique OUVERTE.");
        }

        anneeAAR.delete(annee);
    }
}
