
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.UniteEnseignement;
import com.visa_iss_kin.repository.UniteEnseignementRepository;
import com.visa_iss_kin.service.UniteEnseignementService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class UniteEnseignementServiceImpl implements UniteEnseignementService {
    private final UniteEnseignementRepository uniteEnseignementRepository;

    public UniteEnseignementServiceImpl(
            UniteEnseignementRepository uniteEnseignementRepository
    ) {
        this.uniteEnseignementRepository = uniteEnseignementRepository;
    }

    @Override
    public UniteEnseignement creerUniteEnseignement(
            UniteEnseignement uniteEnseignement
    ) {
        if (uniteEnseignement.getIdUE() == null
                || uniteEnseignement.getIdUE().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant idUE est obligatoire.");
        }

        if (uniteEnseignement.getCodeUE() == null
                || uniteEnseignement.getCodeUE().trim().isEmpty()) {
            throw new RuntimeException("Le code de l'unité d'enseignement est obligatoire.");
        }

        if (uniteEnseignement.getLibeUE() == null
                || uniteEnseignement.getLibeUE().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de l'unité d'enseignement est obligatoire.");
        }

        if (uniteEnseignementRepository.existsById(uniteEnseignement.getIdUE())) {
            throw new RuntimeException(
                    "Une unité d'enseignement existe déjà avec l'identifiant : "
                    + uniteEnseignement.getIdUE()
            );
        }

        if (uniteEnseignementRepository.existsByCodeUE(uniteEnseignement.getCodeUE())) {
            throw new RuntimeException(
                    "Une unité d'enseignement existe déjà avec le code : "
                    + uniteEnseignement.getCodeUE()
            );
        }

        if (uniteEnseignement.getCreatedAt() == null) {
            uniteEnseignement.setCreatedAt(LocalDateTime.now());
        }

        return uniteEnseignementRepository.save(uniteEnseignement);
    }

    @Override
    public List<UniteEnseignement> listerUnitesEnseignement() {
        return uniteEnseignementRepository.findAll();
    }

    @Override
    public UniteEnseignement rechercherUniteEnseignementParId(String idUE) {
        return uniteEnseignementRepository.findById(idUE)
                .orElseThrow(() -> new RuntimeException(
                        "Unité d'enseignement introuvable avec l'identifiant : " + idUE
                ));
    }

    @Override
    public UniteEnseignement rechercherUniteEnseignementParCode(String codeUE) {
        return uniteEnseignementRepository.findByCodeUE(codeUE)
                .orElseThrow(() -> new RuntimeException(
                        "Unité d'enseignement introuvable avec le code : " + codeUE
                ));
    }

    @Override
    public List<UniteEnseignement> rechercherUnitesEnseignementParLibelle(
            String libeUE
    ) {
        return uniteEnseignementRepository.findByLibeUEContainingIgnoreCase(libeUE);
    }

    @Override
    public List<UniteEnseignement> rechercherUnitesEnseignementParCode(
            String codeUE
    ) {
        return uniteEnseignementRepository.findByCodeUEContainingIgnoreCase(codeUE);
    }

    @Override
    public UniteEnseignement modifierUniteEnseignement(
            String idUE,
            UniteEnseignement uniteEnseignement
    ) {
        UniteEnseignement uniteExistante =
                rechercherUniteEnseignementParId(idUE);

        if (uniteEnseignement.getCodeUE() != null) {
            String nouveauCodeUE = uniteEnseignement.getCodeUE();

            if (!nouveauCodeUE.equals(uniteExistante.getCodeUE())
                    && uniteEnseignementRepository.existsByCodeUE(nouveauCodeUE)) {
                throw new RuntimeException(
                        "Une unité d'enseignement existe déjà avec le code : "
                        + nouveauCodeUE
                );
            }

            uniteExistante.setCodeUE(nouveauCodeUE);
        }

        if (uniteEnseignement.getLibeUE() != null) {
            uniteExistante.setLibeUE(uniteEnseignement.getLibeUE());
        }

        if (uniteEnseignement.getDescriptionUE() != null) {
            uniteExistante.setDescriptionUE(uniteEnseignement.getDescriptionUE());
        }

        if (uniteEnseignement.getCreatedBy() != null) {
            uniteExistante.setCreatedBy(uniteEnseignement.getCreatedBy());
        }

        if (uniteExistante.getCreatedAt() == null) {
            uniteExistante.setCreatedAt(LocalDateTime.now());
        }

        return uniteEnseignementRepository.save(uniteExistante);
    }

    @Override
    public void supprimerUniteEnseignement(String idUE) {
        UniteEnseignement uniteEnseignement =
                rechercherUniteEnseignementParId(idUE);

        uniteEnseignementRepository.delete(uniteEnseignement);
    }
}
