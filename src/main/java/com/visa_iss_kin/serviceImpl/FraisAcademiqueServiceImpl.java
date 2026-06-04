
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.FraisAcademique;
import com.visa_iss_kin.repository.FraisAcademiqueRepository;
import com.visa_iss_kin.service.FraisAcademiqueService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class FraisAcademiqueServiceImpl implements FraisAcademiqueService {
    private final FraisAcademiqueRepository fraisAcademiqueRepository;

    public FraisAcademiqueServiceImpl(FraisAcademiqueRepository fraisAcademiqueRepository) {
        this.fraisAcademiqueRepository = fraisAcademiqueRepository;
    }

    @Override
    public FraisAcademique creerFraisAcademique(FraisAcademique fraisAcademique) {

        if (fraisAcademique.getIdFrais() == null || fraisAcademique.getIdFrais().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant du frais idFrais est obligatoire.");
        }

        if (fraisAcademique.getLibeFrais() == null || fraisAcademique.getLibeFrais().trim().isEmpty()) {
            throw new RuntimeException("Le libellé du frais libeFrais est obligatoire.");
        }

        if (fraisAcademiqueRepository.existsById(fraisAcademique.getIdFrais())) {
            throw new RuntimeException("Un frais académique existe déjà avec l'identifiant : "
                    + fraisAcademique.getIdFrais());
        }

        if (fraisAcademique.getCreatedAt() == null) {
            fraisAcademique.setCreatedAt(LocalDateTime.now());
        }

        return fraisAcademiqueRepository.save(fraisAcademique);
    }

    @Override
    public List<FraisAcademique> listerFraisAcademiques() {
        return fraisAcademiqueRepository.findAll();
    }

    @Override
    public FraisAcademique rechercherFraisAcademiqueParId(String idFrais) {
        return fraisAcademiqueRepository.findById(idFrais)
                .orElseThrow(() -> new RuntimeException(
                        "Frais académique introuvable avec l'identifiant : " + idFrais
                ));
    }

    @Override
    public List<FraisAcademique> rechercherFraisAcademiquesParLibelle(String libeFrais) {
        return fraisAcademiqueRepository.findByLibeFraisContainingIgnoreCase(libeFrais);
    }

    @Override
    public FraisAcademique modifierFraisAcademique(String idFrais, FraisAcademique fraisAcademique) {
        FraisAcademique fraisExistant = rechercherFraisAcademiqueParId(idFrais);

        if (fraisAcademique.getLibeFrais() != null) {
            fraisExistant.setLibeFrais(fraisAcademique.getLibeFrais());
        }

        if (fraisAcademique.getDescriptionFrais() != null) {
            fraisExistant.setDescriptionFrais(fraisAcademique.getDescriptionFrais());
        }

        if (fraisAcademique.getCreatedBy() != null) {
            fraisExistant.setCreatedBy(fraisAcademique.getCreatedBy());
        }

        if (fraisExistant.getCreatedAt() == null) {
            fraisExistant.setCreatedAt(LocalDateTime.now());
        }

        return fraisAcademiqueRepository.save(fraisExistant);
    }

    @Override
    public void supprimerFraisAcademique(String idFrais) {
        FraisAcademique fraisAcademique = rechercherFraisAcademiqueParId(idFrais);
        fraisAcademiqueRepository.delete(fraisAcademique);
    }
}
