package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.repository.*;
import com.visa_iss_kin.service.EpreuveService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class EpreuveServiceImpl implements EpreuveService {
    private final EpreuveRepository epreuveRepository;
    private final SessionEvaluationRepository sessionEvaluationRepository;
    private final PromotionRepository promotionRepository;
    private final ElementConstitutifRepository elementConstitutifRepository;

    public EpreuveServiceImpl(
            EpreuveRepository epreuveRepository,
            SessionEvaluationRepository sessionEvaluationRepository,
            PromotionRepository promotionRepository,
            ElementConstitutifRepository elementConstitutifRepository
    ) {
        this.epreuveRepository = epreuveRepository;
        this.sessionEvaluationRepository = sessionEvaluationRepository;
        this.promotionRepository = promotionRepository;
        this.elementConstitutifRepository = elementConstitutifRepository;
    }

    @Override
    public Epreuve creerEpreuve(Epreuve epreuve) {
        verifierDonneesObligatoires(epreuve);

        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(
                        epreuve.getSessionEvaluation()
                                .getIdSessionEvaluation()
                );

        Promotion promotion =
                rechercherPromotion(
                        epreuve.getPromotion().getIdPromo()
                );

        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(
                        epreuve.getElementConstitutif().getIdEC()
                );

        verifierSessionOuverte(sessionEvaluation);

        verifierDateEpreuveDansSession(
                epreuve.getDateEpreuve(),
                sessionEvaluation
        );

        verifierPonderationEtNoteMaximale(epreuve);

        boolean existeDeja =
                epreuveRepository
                        .existsBySessionEvaluationAndPromotionAndElementConstitutifAndTypeEpreuve(
                                sessionEvaluation,
                                promotion,
                                elementConstitutif,
                                epreuve.getTypeEpreuve()
                        );

        if (existeDeja) {
            throw new RuntimeException(
                    "Cette épreuve existe déjà pour cette session, "
                    + "cette promotion, cet élément constitutif "
                    + "et ce type d'épreuve."
            );
        }

        epreuve.setSessionEvaluation(sessionEvaluation);
        epreuve.setPromotion(promotion);
        epreuve.setElementConstitutif(elementConstitutif);

        if (epreuve.getCreatedAt() == null) {
            epreuve.setCreatedAt(LocalDateTime.now());
        }

        if (epreuve.getOuverte() == null) {
            epreuve.setOuverte(false);
        }

        if (epreuve.getLibelleEpreuve() == null
                || epreuve.getLibelleEpreuve().trim().isEmpty()) {
            epreuve.setLibelleEpreuve(
                    construireLibelleEpreuve(epreuve)
            );
        }

        return epreuveRepository.save(epreuve);
    }

    @Override
    public List<Epreuve> listerEpreuves() {
        return epreuveRepository.findAll();
    }

    @Override
    public Epreuve rechercherEpreuveParId(Long idEpreuve) {
        return epreuveRepository.findById(idEpreuve)
                .orElseThrow(() -> new RuntimeException(
                        "Épreuve introuvable avec l'identifiant : "
                        + idEpreuve
                ));
    }

    @Override
    public List<Epreuve> listerEpreuvesParSession(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return epreuveRepository.findBySessionEvaluation(
                sessionEvaluation
        );
    }

    @Override
    public List<Epreuve> listerEpreuvesParPromotion(
            String idPromo
    ) {
        Promotion promotion = rechercherPromotion(idPromo);

        return epreuveRepository.findByPromotion(promotion);
    }

    @Override
    public List<Epreuve> listerEpreuvesParElementConstitutif(
            String idEC
    ) {
        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        return epreuveRepository.findByElementConstitutif(
                elementConstitutif
        );
    }

    @Override
    public List<Epreuve> listerEpreuvesParType(
            TypeEpreuve typeEpreuve
    ) {
        if (typeEpreuve == null) {
            throw new RuntimeException(
                    "Le type d'épreuve est obligatoire."
            );
        }

        return epreuveRepository.findByTypeEpreuve(typeEpreuve);
    }

    @Override
    public List<Epreuve> listerEpreuvesOuvertes() {
        return epreuveRepository.findByOuverte(true);
    }

    @Override
    public List<Epreuve> listerEpreuvesParSessionEtPromotion(
            Long idSessionEvaluation,
            String idPromo
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        Promotion promotion = rechercherPromotion(idPromo);

        return epreuveRepository.findBySessionEvaluationAndPromotion(
                sessionEvaluation,
                promotion
        );
    }

    @Override
    public List<Epreuve> listerEpreuvesParSessionPromotionEtEC(
            Long idSessionEvaluation,
            String idPromo,
            String idEC
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        Promotion promotion = rechercherPromotion(idPromo);

        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        return epreuveRepository
                .findBySessionEvaluationAndPromotionAndElementConstitutif(
                        sessionEvaluation,
                        promotion,
                        elementConstitutif
                );
    }

    @Override
    public List<Epreuve> listerEpreuvesParPromotionEtEC(
            String idPromo,
            String idEC
    ) {
        Promotion promotion = rechercherPromotion(idPromo);

        ElementConstitutif elementConstitutif =
                rechercherElementConstitutif(idEC);

        return epreuveRepository.findByPromotionAndElementConstitutif(
                promotion,
                elementConstitutif
        );
    }

    @Override
    public List<Epreuve> listerEpreuvesOuvertesParSession(
            Long idSessionEvaluation
    ) {
        SessionEvaluation sessionEvaluation =
                rechercherSessionEvaluation(idSessionEvaluation);

        return epreuveRepository.findBySessionEvaluationAndOuverte(
                sessionEvaluation,
                true
        );
    }

    @Override
    public Epreuve modifierEpreuve(
            Long idEpreuve,
            Epreuve epreuve
    ) {
        Epreuve epreuveExistante =
                rechercherEpreuveParId(idEpreuve);

        SessionEvaluation sessionEvaluation =
                epreuveExistante.getSessionEvaluation();

        Promotion promotion =
                epreuveExistante.getPromotion();

        ElementConstitutif elementConstitutif =
                epreuveExistante.getElementConstitutif();

        if (epreuve.getSessionEvaluation() != null
                && epreuve.getSessionEvaluation()
                        .getIdSessionEvaluation() != null) {

            sessionEvaluation = rechercherSessionEvaluation(
                    epreuve.getSessionEvaluation()
                            .getIdSessionEvaluation()
            );
        }

        if (epreuve.getPromotion() != null
                && epreuve.getPromotion().getIdPromo() != null
                && !epreuve.getPromotion().getIdPromo()
                        .trim().isEmpty()) {

            promotion = rechercherPromotion(
                    epreuve.getPromotion().getIdPromo()
            );
        }

        if (epreuve.getElementConstitutif() != null
                && epreuve.getElementConstitutif().getIdEC() != null) {

            elementConstitutif = rechercherElementConstitutif(
                    epreuve.getElementConstitutif().getIdEC()
            );
        }

        TypeEpreuve typeEpreuve =
                epreuve.getTypeEpreuve() != null
                        ? epreuve.getTypeEpreuve()
                        : epreuveExistante.getTypeEpreuve();

        LocalDate dateEpreuve =
                epreuve.getDateEpreuve() != null
                        ? epreuve.getDateEpreuve()
                        : epreuveExistante.getDateEpreuve();

        Double ponderation =
                epreuve.getPonderation() != null
                        ? epreuve.getPonderation()
                        : epreuveExistante.getPonderation();

        Double noteMaximale =
                epreuve.getNoteMaximale() != null
                        ? epreuve.getNoteMaximale()
                        : epreuveExistante.getNoteMaximale();

        verifierSessionOuverte(sessionEvaluation);
        verifierDateEpreuveDansSession(dateEpreuve, sessionEvaluation);
        verifierPonderationEtNoteMaximale(
                typeEpreuve,
                ponderation,
                noteMaximale
        );

        boolean combinaisonModifiee =
                !sessionEvaluation.getIdSessionEvaluation().equals(
                        epreuveExistante.getSessionEvaluation()
                                .getIdSessionEvaluation()
                )
                || !promotion.getIdPromo().equals(
                        epreuveExistante.getPromotion().getIdPromo()
                )
                || !elementConstitutif.getIdEC().equals(
                        epreuveExistante.getElementConstitutif().getIdEC()
                )
                || typeEpreuve != epreuveExistante.getTypeEpreuve();

        if (combinaisonModifiee
                && epreuveRepository
                        .existsBySessionEvaluationAndPromotionAndElementConstitutifAndTypeEpreuve(
                                sessionEvaluation,
                                promotion,
                                elementConstitutif,
                                typeEpreuve
                        )) {
            throw new RuntimeException(
                    "Une autre épreuve existe déjà avec cette combinaison."
            );
        }

        epreuveExistante.setSessionEvaluation(sessionEvaluation);
        epreuveExistante.setPromotion(promotion);
        epreuveExistante.setElementConstitutif(elementConstitutif);
        epreuveExistante.setTypeEpreuve(typeEpreuve);
        epreuveExistante.setDateEpreuve(dateEpreuve);
        epreuveExistante.setPonderation(ponderation);
        epreuveExistante.setNoteMaximale(noteMaximale);

        if (epreuve.getLibelleEpreuve() != null
                && !epreuve.getLibelleEpreuve().trim().isEmpty()) {
            epreuveExistante.setLibelleEpreuve(
                    epreuve.getLibelleEpreuve().trim()
            );
        }

        if (epreuve.getOuverte() != null) {
            epreuveExistante.setOuverte(epreuve.getOuverte());
        }

        if (epreuve.getObservation() != null) {
            epreuveExistante.setObservation(
                    epreuve.getObservation()
            );
        }

        if (epreuve.getCreatedBy() != null) {
            epreuveExistante.setCreatedBy(
                    epreuve.getCreatedBy()
            );
        }

        return epreuveRepository.save(epreuveExistante);
    }

    @Override
    public Epreuve ouvrirEpreuve(Long idEpreuve) {
        Epreuve epreuve = rechercherEpreuveParId(idEpreuve);

        verifierSessionOuverte(epreuve.getSessionEvaluation());

        epreuve.setOuverte(true);

        return epreuveRepository.save(epreuve);
    }

    @Override
    public Epreuve fermerEpreuve(Long idEpreuve) {
        Epreuve epreuve = rechercherEpreuveParId(idEpreuve);

        epreuve.setOuverte(false);

        return epreuveRepository.save(epreuve);
    }

    @Override
    public void supprimerEpreuve(Long idEpreuve) {
        Epreuve epreuve = rechercherEpreuveParId(idEpreuve);

        if (Boolean.TRUE.equals(epreuve.getOuverte())) {
            throw new RuntimeException(
                    "Une épreuve ouverte ne peut pas être supprimée. "
                    + "Veuillez d'abord la fermer."
            );
        }

        epreuveRepository.delete(epreuve);
    }

    private SessionEvaluation rechercherSessionEvaluation(
            Long idSessionEvaluation
    ) {
        if (idSessionEvaluation == null) {
            throw new RuntimeException(
                    "L'identifiant de la session est obligatoire."
            );
        }

        return sessionEvaluationRepository
                .findById(idSessionEvaluation)
                .orElseThrow(() -> new RuntimeException(
                        "Session d'évaluation introuvable avec "
                        + "l'identifiant : " + idSessionEvaluation
                ));
    }

    private Promotion rechercherPromotion(String idPromo) {
        if (idPromo == null || idPromo.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'identifiant de la promotion est obligatoire."
            );
        }

        return promotionRepository.findById(idPromo.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Promotion introuvable avec l'identifiant : "
                        + idPromo
                ));
    }

    private ElementConstitutif rechercherElementConstitutif(
            String idEC
    ) {
        if (idEC == null) {
            throw new RuntimeException(
                    "L'identifiant de l'élément constitutif est obligatoire."
            );
        }

        return elementConstitutifRepository.findById(idEC)
                .orElseThrow(() -> new RuntimeException(
                        "Élément constitutif introuvable avec "
                        + "l'identifiant : " + idEC
                ));
    }

    private void verifierDonneesObligatoires(Epreuve epreuve) {
        if (epreuve == null) {
            throw new RuntimeException(
                    "Les informations de l'épreuve sont obligatoires."
            );
        }

        if (epreuve.getTypeEpreuve() == null) {
            throw new RuntimeException(
                    "Le type d'épreuve est obligatoire."
            );
        }

        if (epreuve.getDateEpreuve() == null) {
            throw new RuntimeException(
                    "La date de l'épreuve est obligatoire."
            );
        }

        if (epreuve.getSessionEvaluation() == null
                || epreuve.getSessionEvaluation()
                        .getIdSessionEvaluation() == null) {
            throw new RuntimeException(
                    "La session d'évaluation est obligatoire."
            );
        }

        if (epreuve.getPromotion() == null
                || epreuve.getPromotion().getIdPromo() == null
                || epreuve.getPromotion().getIdPromo()
                        .trim().isEmpty()) {
            throw new RuntimeException(
                    "La promotion est obligatoire."
            );
        }

        if (epreuve.getElementConstitutif() == null
                || epreuve.getElementConstitutif().getIdEC() == null) {
            throw new RuntimeException(
                    "L'élément constitutif est obligatoire."
            );
        }

        verifierPonderationEtNoteMaximale(epreuve);
    }

    private void verifierPonderationEtNoteMaximale(Epreuve epreuve) {
        verifierPonderationEtNoteMaximale(
                epreuve.getTypeEpreuve(),
                epreuve.getPonderation(),
                epreuve.getNoteMaximale()
        );
    }

    private void verifierPonderationEtNoteMaximale(
            TypeEpreuve typeEpreuve,
            Double ponderation,
            Double noteMaximale
    ) {
        if (typeEpreuve == null) {
            throw new RuntimeException(
                    "Le type d'épreuve est obligatoire."
            );
        }

        if (ponderation == null || ponderation <= 0) {
            throw new RuntimeException(
                    "La pondération doit être strictement supérieure à zéro."
            );
        }

        if (noteMaximale == null || noteMaximale <= 0) {
            throw new RuntimeException(
                    "La note maximale doit être strictement supérieure à zéro."
            );
        }
    }

    private void verifierSessionOuverte(
            SessionEvaluation sessionEvaluation
    ) {
        if (!Boolean.TRUE.equals(sessionEvaluation.getOuverte())) {
            throw new RuntimeException(
                    "La session d'évaluation doit être ouverte "
                    + "pour créer ou modifier une épreuve."
            );
        }
    }

    private void verifierDateEpreuveDansSession(
            LocalDate dateEpreuve,
            SessionEvaluation sessionEvaluation
    ) {
        if (dateEpreuve.isBefore(sessionEvaluation.getDateDebut())
                || dateEpreuve.isAfter(sessionEvaluation.getDateFin())) {
            throw new RuntimeException(
                    "La date de l'épreuve doit être comprise entre "
                    + "la date de début et la date de fin de la session."
            );
        }
    }

    private String construireLibelleEpreuve(Epreuve epreuve) {
        return epreuve.getTypeEpreuve()
                + " - "
                + epreuve.getElementConstitutif().getCodeEC()
                + " - "
                + epreuve.getPromotion().getLibePromo();
    }
}
