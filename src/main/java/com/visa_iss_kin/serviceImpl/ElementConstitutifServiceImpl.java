
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.ElementConstitutif;
import com.visa_iss_kin.model.UniteEnseignement;
import com.visa_iss_kin.repository.ElementConstitutifRepository;
import com.visa_iss_kin.repository.UniteEnseignementRepository;
import com.visa_iss_kin.service.ElementConstitutifService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class ElementConstitutifServiceImpl implements ElementConstitutifService {
    private final ElementConstitutifRepository elementConstitutifRepository;
    private final UniteEnseignementRepository uniteEnseignementRepository;

    public ElementConstitutifServiceImpl(
            ElementConstitutifRepository elementConstitutifRepository,
            UniteEnseignementRepository uniteEnseignementRepository
    ) {
        this.elementConstitutifRepository = elementConstitutifRepository;
        this.uniteEnseignementRepository = uniteEnseignementRepository;
    }

    @Override
    public ElementConstitutif creerElementConstitutif(
            ElementConstitutif elementConstitutif
    ) {

        if (elementConstitutif.getIdEC() == null
                || elementConstitutif.getIdEC().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant idEC est obligatoire.");
        }

        if (elementConstitutif.getCodeEC() == null
                || elementConstitutif.getCodeEC().trim().isEmpty()) {
            throw new RuntimeException("Le code de l'élément constitutif est obligatoire.");
        }

        if (elementConstitutif.getLibeEC() == null
                || elementConstitutif.getLibeEC().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de l'élément constitutif est obligatoire.");
        }

        if (elementConstitutif.getCreditEC() == null
                || elementConstitutif.getCreditEC() < 0) {
            throw new RuntimeException("Le nombre de crédits doit être supérieur ou égal à zéro.");
        }

        if (elementConstitutif.getNombreHeuresTheorie() == null
                || elementConstitutif.getNombreHeuresTheorie() < 0) {
            throw new RuntimeException(
                    "Le nombre d'heures de théorie doit être supérieur ou égal à zéro."
            );
        }

        if (elementConstitutif.getNombreHeuresPratique() == null
                || elementConstitutif.getNombreHeuresPratique() < 0) {
            throw new RuntimeException(
                    "Le nombre d'heures de pratique doit être supérieur ou égal à zéro."
            );
        }

        if (elementConstitutif.getUniteEnseignement() == null
                || elementConstitutif.getUniteEnseignement().getIdUE() == null) {
            throw new RuntimeException("L'unité d'enseignement est obligatoire.");
        }

        if (elementConstitutifRepository.existsById(elementConstitutif.getIdEC())) {
            throw new RuntimeException(
                    "Un élément constitutif existe déjà avec l'identifiant : "
                    + elementConstitutif.getIdEC()
            );
        }

        if (elementConstitutifRepository.existsByCodeEC(elementConstitutif.getCodeEC())) {
            throw new RuntimeException(
                    "Un élément constitutif existe déjà avec le code : "
                    + elementConstitutif.getCodeEC()
            );
        }

        UniteEnseignement uniteEnseignement =
                uniteEnseignementRepository.findById(
                        elementConstitutif.getUniteEnseignement().getIdUE()
                ).orElseThrow(() -> new RuntimeException(
                        "Unité d'enseignement introuvable avec l'identifiant : "
                        + elementConstitutif.getUniteEnseignement().getIdUE()
                ));

        if (elementConstitutif.getCreatedAt() == null) {
            elementConstitutif.setCreatedAt(LocalDateTime.now());
        }

        elementConstitutif.setUniteEnseignement(uniteEnseignement);

        return elementConstitutifRepository.save(elementConstitutif);
    }

    @Override
    public List<ElementConstitutif> listerElementsConstitutifs() {
        return elementConstitutifRepository.findAll();
    }

    @Override
    public ElementConstitutif rechercherElementConstitutifParId(String idEC) {
        return elementConstitutifRepository.findById(idEC)
                .orElseThrow(() -> new RuntimeException(
                        "Élément constitutif introuvable avec l'identifiant : " + idEC
                ));
    }

    @Override
    public ElementConstitutif rechercherElementConstitutifParCode(String codeEC) {
        return elementConstitutifRepository.findByCodeEC(codeEC)
                .orElseThrow(() -> new RuntimeException(
                        "Élément constitutif introuvable avec le code : " + codeEC
                ));
    }

    @Override
    public List<ElementConstitutif> rechercherElementsConstitutifsParLibelle(
            String libeEC
    ) {
        return elementConstitutifRepository.findByLibeECContainingIgnoreCase(libeEC);
    }

    @Override
    public List<ElementConstitutif> rechercherElementsConstitutifsParCode(
            String codeEC
    ) {
        return elementConstitutifRepository.findByCodeECContainingIgnoreCase(codeEC);
    }

    @Override
    public List<ElementConstitutif> listerElementsConstitutifsParUE(String idUE) {
        UniteEnseignement uniteEnseignement =
                uniteEnseignementRepository.findById(idUE)
                        .orElseThrow(() -> new RuntimeException(
                                "Unité d'enseignement introuvable avec l'identifiant : "
                                + idUE
                        ));

        return elementConstitutifRepository.findByUniteEnseignement(uniteEnseignement);
    }

    @Override
    public ElementConstitutif modifierElementConstitutif(
            String idEC,
            ElementConstitutif elementConstitutif
    ) {
        ElementConstitutif elementExistant =
                rechercherElementConstitutifParId(idEC);

        if (elementConstitutif.getCodeEC() != null) {
            String nouveauCodeEC = elementConstitutif.getCodeEC();

            if (!nouveauCodeEC.equals(elementExistant.getCodeEC())
                    && elementConstitutifRepository.existsByCodeEC(nouveauCodeEC)) {
                throw new RuntimeException(
                        "Un élément constitutif existe déjà avec le code : "
                        + nouveauCodeEC
                );
            }

            elementExistant.setCodeEC(nouveauCodeEC);
        }

        if (elementConstitutif.getLibeEC() != null) {
            elementExistant.setLibeEC(elementConstitutif.getLibeEC());
        }

        if (elementConstitutif.getCreditEC() != null) {
            if (elementConstitutif.getCreditEC() < 0) {
                throw new RuntimeException(
                        "Le nombre de crédits doit être supérieur ou égal à zéro."
                );
            }

            elementExistant.setCreditEC(elementConstitutif.getCreditEC());
        }

        if (elementConstitutif.getNombreHeuresTheorie() != null) {
            if (elementConstitutif.getNombreHeuresTheorie() < 0) {
                throw new RuntimeException(
                        "Le nombre d'heures de théorie doit être supérieur ou égal à zéro."
                );
            }

            elementExistant.setNombreHeuresTheorie(
                    elementConstitutif.getNombreHeuresTheorie()
            );
        }

        if (elementConstitutif.getNombreHeuresPratique() != null) {
            if (elementConstitutif.getNombreHeuresPratique() < 0) {
                throw new RuntimeException(
                        "Le nombre d'heures de pratique doit être supérieur ou égal à zéro."
                );
            }

            elementExistant.setNombreHeuresPratique(
                    elementConstitutif.getNombreHeuresPratique()
            );
        }

        if (elementConstitutif.getDescriptionEC() != null) {
            elementExistant.setDescriptionEC(elementConstitutif.getDescriptionEC());
        }

        if (elementConstitutif.getCreatedBy() != null) {
            elementExistant.setCreatedBy(elementConstitutif.getCreatedBy());
        }

        if (elementConstitutif.getUniteEnseignement() != null
                && elementConstitutif.getUniteEnseignement().getIdUE() != null) {

            UniteEnseignement uniteEnseignement =
                    uniteEnseignementRepository.findById(
                            elementConstitutif.getUniteEnseignement().getIdUE()
                    ).orElseThrow(() -> new RuntimeException(
                            "Unité d'enseignement introuvable avec l'identifiant : "
                            + elementConstitutif.getUniteEnseignement().getIdUE()
                    ));

            elementExistant.setUniteEnseignement(uniteEnseignement);
        }

        if (elementExistant.getCreatedAt() == null) {
            elementExistant.setCreatedAt(LocalDateTime.now());
        }

        return elementConstitutifRepository.save(elementExistant);
    }

    @Override
    public void supprimerElementConstitutif(String idEC) {
        ElementConstitutif elementConstitutif =
                rechercherElementConstitutifParId(idEC);

        elementConstitutifRepository.delete(elementConstitutif);
    }
}
