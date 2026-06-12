
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.dto.ResumeFinancierInscriptionDto;
import com.visa_iss_kin.dto.SituationFinanciereEtudiantDto;
import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.PaiementFrais;
import com.visa_iss_kin.model.StatutFinancier;
import com.visa_iss_kin.model.StatutPaiement;
import com.visa_iss_kin.repository.AssocierFraisALaPromotionRepository;
import com.visa_iss_kin.repository.InscriptionRepository;
import com.visa_iss_kin.repository.NumeroCompteRepository;
import com.visa_iss_kin.repository.PaiementFraisRepository;
import com.visa_iss_kin.service.PaiementFraisService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class PaiementFraisServiceImpl implements PaiementFraisService {
    private final PaiementFraisRepository paiementFraisRepository;
    private final InscriptionRepository inscriptionRepository;
    private final AssocierFraisALaPromotionRepository associerFraisALaPromotionRepository;
    private final NumeroCompteRepository numeroBanqueRepository;

    public PaiementFraisServiceImpl(
            PaiementFraisRepository paiementFraisRepository,
            InscriptionRepository inscriptionRepository,
            AssocierFraisALaPromotionRepository associerFraisALaPromotionRepository,
            NumeroCompteRepository numeroBanqueRepository
    ) {
        this.paiementFraisRepository = paiementFraisRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.associerFraisALaPromotionRepository = associerFraisALaPromotionRepository;
        this.numeroBanqueRepository = numeroBanqueRepository;
    }

    @Override
    public PaiementFrais creerPaiementFrais(PaiementFrais paiementFrais) {

        if (paiementFrais.getMontantPaye() == null) {
            throw new RuntimeException("Le montant payé est obligatoire.");
        }

        if (paiementFrais.getMontantPaye().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Le montant payé doit être supérieur à zéro.");
        }

        if (paiementFrais.getDevisePaiement() == null) {
            throw new RuntimeException("La devise du paiement est obligatoire.");
        }

        if (paiementFrais.getReferencePaiement() == null
                || paiementFrais.getReferencePaiement().trim().isEmpty()) {
            throw new RuntimeException("La référence du paiement est obligatoire.");
        }

        if (paiementFraisRepository.existsByReferencePaiement(
                paiementFrais.getReferencePaiement()
        )) {
            throw new RuntimeException("Cette référence de paiement existe déjà : "
                    + paiementFrais.getReferencePaiement());
        }

        if (paiementFrais.getInscription() == null
                || paiementFrais.getInscription().getIdInscription() == null) {
            throw new RuntimeException("L'inscription est obligatoire.");
        }

        if (paiementFrais.getAssociationFrais() == null
                || paiementFrais.getAssociationFrais().getIdAssociationFrais() == null) {
            throw new RuntimeException("L'association frais-promotion est obligatoire.");
        }

        if (paiementFrais.getNumeroBanque() == null
                || paiementFrais.getNumeroBanque().getIdNumeroCompte() == null) {
            throw new RuntimeException("Le numéro bancaire est obligatoire.");
        }

        Inscription inscription = inscriptionRepository.findById(
                paiementFrais.getInscription().getIdInscription()
        ).orElseThrow(() -> new RuntimeException(
                "Inscription introuvable avec l'identifiant : "
                + paiementFrais.getInscription().getIdInscription()
        ));

        AssocierFraisALaPromotion associationFrais =
                associerFraisALaPromotionRepository.findById(
                        paiementFrais.getAssociationFrais().getIdAssociationFrais()
                ).orElseThrow(() -> new RuntimeException(
                        "Association frais-promotion introuvable avec l'identifiant : "
                        + paiementFrais.getAssociationFrais().getIdAssociationFrais()
                ));

        NumeroCompte numeroBanque = numeroBanqueRepository.findById(
                paiementFrais.getNumeroBanque().getIdNumeroCompte()
        ).orElseThrow(() -> new RuntimeException(
                "Numéro bancaire introuvable avec l'identifiant : "
                + paiementFrais.getNumeroBanque().getIdNumeroCompte()
        ));

        if (associationFrais.getDevise() != paiementFrais.getDevisePaiement()) {
            throw new RuntimeException(
                    "La devise du paiement ne correspond pas à la devise du frais associé."
            );
        }

        if (numeroBanque.getDeviseCompte() != paiementFrais.getDevisePaiement()) {
            throw new RuntimeException(
                    "La devise du compte bancaire ne correspond pas à la devise du paiement."
            );
        }

        if (!associationFrais.getPromotion().getIdPromo()
                .equals(inscription.getPromotion().getIdPromo())) {
            throw new RuntimeException(
                    "Le frais associé ne correspond pas à la promotion de l'inscription."
            );
        }

        if (!associationFrais.getAnneeAcademique().getIdAa()
                .equals(inscription.getAnneeAcademique().getIdAa())) {
            throw new RuntimeException(
                    "Le frais associé ne correspond pas à l'année académique de l'inscription."
            );
        }
        BigDecimal totalDejaPaye =
        paiementFraisRepository.calculerTotalPayeParInscriptionEtFrais(
                inscription,
                associationFrais
        );

        BigDecimal nouveauTotal =
                totalDejaPaye.add(paiementFrais.getMontantPaye());

        BigDecimal montantPrevu =
                associationFrais.getMontantFrais();

        if (nouveauTotal.compareTo(montantPrevu) > 0) {
            BigDecimal resteAPayer = montantPrevu.subtract(totalDejaPaye);

            throw new RuntimeException(
                    "Paiement refusé : le montant payé dépasse le montant prévu. "
                    + "Montant prévu : " + montantPrevu + " "
                    + associationFrais.getDevise()
                    + ", déjà payé : " + totalDejaPaye + " "
                    + associationFrais.getDevise()
                    + ", reste à payer : " + resteAPayer + " "
                    + associationFrais.getDevise()
                    + ", montant tenté : " + paiementFrais.getMontantPaye() + " "
                    + paiementFrais.getDevisePaiement()
            );
        }

        if (paiementFrais.getDatePaiement() == null) {
            paiementFrais.setDatePaiement(LocalDateTime.now());
        }

        if (paiementFrais.getDateEnregistrement() == null) {
            paiementFrais.setDateEnregistrement(LocalDateTime.now());
        }

        if (paiementFrais.getCreatedAt() == null) {
            paiementFrais.setCreatedAt(LocalDateTime.now());
        }

        if (paiementFrais.getStatutPaiement() == null) {
            paiementFrais.setStatutPaiement(StatutPaiement.VALIDE);
        }

        paiementFrais.setInscription(inscription);
        paiementFrais.setAssociationFrais(associationFrais);
        paiementFrais.setNumeroBanque(numeroBanque);

        return paiementFraisRepository.save(paiementFrais);
    }

    @Override
    public List<PaiementFrais> listerPaiementsFrais() {
        return paiementFraisRepository.findAll();
    }

    @Override
    public PaiementFrais rechercherPaiementFraisParId(Long idPaiementFrais) {
        return paiementFraisRepository.findById(idPaiementFrais)
                .orElseThrow(() -> new RuntimeException(
                        "Paiement frais introuvable avec l'identifiant : "
                        + idPaiementFrais
                ));
    }

    @Override
    public PaiementFrais rechercherPaiementFraisParReference(String referencePaiement) {
        return paiementFraisRepository.findByReferencePaiement(referencePaiement)
                .orElseThrow(() -> new RuntimeException(
                        "Paiement introuvable avec la référence : " + referencePaiement
                ));
    }

    @Override
    public List<PaiementFrais> listerPaiementsParInscription(String idInscription) {
        Inscription inscription = inscriptionRepository.findById(idInscription)
                .orElseThrow(() -> new RuntimeException(
                        "Inscription introuvable avec l'identifiant : " + idInscription
                ));

        return paiementFraisRepository.findByInscription(inscription);
    }

    @Override
    public List<PaiementFrais> listerPaiementsParAssociationFrais(Long idAssociationFrais) {
        AssocierFraisALaPromotion associationFrais =
                associerFraisALaPromotionRepository.findById(idAssociationFrais)
                        .orElseThrow(() -> new RuntimeException(
                                "Association frais-promotion introuvable avec l'identifiant : "
                                + idAssociationFrais
                        ));

        return paiementFraisRepository.findByAssociationFrais(associationFrais);
    }

    @Override
    public List<PaiementFrais> listerPaiementsParNumeroBanque(String idNumeroBanque) {
        NumeroCompte numeroBanque = numeroBanqueRepository.findById(idNumeroBanque)
                .orElseThrow(() -> new RuntimeException(
                        "Numéro bancaire introuvable avec l'identifiant : "
                        + idNumeroBanque
                ));

        return paiementFraisRepository.findByNumeroBanque(numeroBanque);
    }

    @Override
    public List<PaiementFrais> listerPaiementsParStatut(StatutPaiement statutPaiement) {
        return paiementFraisRepository.findByStatutPaiement(statutPaiement);
    }

    @Override
    public List<PaiementFrais> listerPaiementsParDevise(Devise devisePaiement) {
        return paiementFraisRepository.findByDevisePaiement(devisePaiement);
    }

    @Override
    public List<PaiementFrais> listerPaiementsParInscriptionEtAssociationFrais(
            String idInscription,
            Long idAssociationFrais
    ) {
        Inscription inscription = inscriptionRepository.findById(idInscription)
                .orElseThrow(() -> new RuntimeException(
                        "Inscription introuvable avec l'identifiant : " + idInscription
                ));

        AssocierFraisALaPromotion associationFrais =
                associerFraisALaPromotionRepository.findById(idAssociationFrais)
                        .orElseThrow(() -> new RuntimeException(
                                "Association frais-promotion introuvable avec l'identifiant : "
                                + idAssociationFrais
                        ));

        return paiementFraisRepository.findByInscriptionAndAssociationFrais(
                inscription,
                associationFrais
        );
    }

    @Override
    public List<PaiementFrais> listerPaiementsValidesParInscription(String idInscription) {
        Inscription inscription = inscriptionRepository.findById(idInscription)
                .orElseThrow(() -> new RuntimeException(
                        "Inscription introuvable avec l'identifiant : " + idInscription
                ));

        return paiementFraisRepository.findByInscriptionAndStatutPaiement(
                inscription,
                StatutPaiement.VALIDE
        );
    }
    @Override
    public List<SituationFinanciereEtudiantDto> consulterSituationFinanciereParInscription(
            String idInscription
    ) {
    Inscription inscription = inscriptionRepository.findById(idInscription)
            .orElseThrow(() -> new RuntimeException(
                    "Inscription introuvable avec l'identifiant : " + idInscription
            ));

    List<AssocierFraisALaPromotion> associationsFrais =
            associerFraisALaPromotionRepository.findByPromotionAndAnneeAcademique(
                    inscription.getPromotion(),
                    inscription.getAnneeAcademique()
            );

        List<SituationFinanciereEtudiantDto> situationFinanciere = new ArrayList<>();

        for (AssocierFraisALaPromotion associationFrais : associationsFrais) {

            BigDecimal montantPrevu = associationFrais.getMontantFrais();

            BigDecimal montantPaye =
                    paiementFraisRepository.calculerTotalPayeParInscriptionEtFrais(
                            inscription,
                            associationFrais
                    );

            BigDecimal resteAPayer = montantPrevu.subtract(montantPaye);

            StatutFinancier statutFinancier;

            if (montantPaye.compareTo(BigDecimal.ZERO) == 0) {
                statutFinancier = StatutFinancier.NON_PAYE;
            } else if (montantPaye.compareTo(montantPrevu) < 0) {
                statutFinancier = StatutFinancier.PARTIEL;
            } else {
                statutFinancier = StatutFinancier.SOLDE;
            }

            String nomCompletEtudiant =
                    inscription.getEtudiant().getNomEtudiant()
                    + " "
                    + inscription.getEtudiant().getPostNomEtudiant()
                    + " "
                    + inscription.getEtudiant().getPrenomEtudiant();

            SituationFinanciereEtudiantDto dto =
                    new SituationFinanciereEtudiantDto(
                            associationFrais.getIdAssociationFrais(),
                            associationFrais.getFraisAcademique().getIdFrais(),
                            associationFrais.getFraisAcademique().getLibeFrais(),
                            inscription.getIdInscription(),
                            inscription.getEtudiant().getIdEtudiant(),
                            inscription.getEtudiant().getMatriculEtudiant(),
                            nomCompletEtudiant,
                            inscription.getPromotion().getIdPromo(),
                            inscription.getPromotion().getLibePromo(),
                            inscription.getAnneeAcademique().getIdAa(),
                            montantPrevu,
                            montantPaye,
                            resteAPayer,
                            associationFrais.getDevise(),
                            statutFinancier
                    );

            situationFinanciere.add(dto);
        }

        return situationFinanciere;
    }
    @Override
    public ResumeFinancierInscriptionDto consulterResumeFinancierParInscription(
            String idInscription
    ) {
        List<SituationFinanciereEtudiantDto> situationFinanciere =
                consulterSituationFinanciereParInscription(idInscription);

        if (situationFinanciere == null || situationFinanciere.isEmpty()) {
            throw new RuntimeException(
                    "Aucun frais associé trouvé pour cette inscription : " + idInscription
            );
        }

        BigDecimal montantTotalPrevu = BigDecimal.ZERO;
        BigDecimal montantTotalPaye = BigDecimal.ZERO;
        BigDecimal resteTotalAPayer = BigDecimal.ZERO;

        for (SituationFinanciereEtudiantDto situation : situationFinanciere) {
            montantTotalPrevu = montantTotalPrevu.add(situation.getMontantPrevu());
            montantTotalPaye = montantTotalPaye.add(situation.getMontantPaye());
            resteTotalAPayer = resteTotalAPayer.add(situation.getResteAPayer());
        }

        StatutFinancier statutFinancierGlobal;

        if (montantTotalPaye.compareTo(BigDecimal.ZERO) == 0) {
            statutFinancierGlobal = StatutFinancier.NON_PAYE;
        } else if (montantTotalPaye.compareTo(montantTotalPrevu) < 0) {
            statutFinancierGlobal = StatutFinancier.PARTIEL;
        } else {
            statutFinancierGlobal = StatutFinancier.SOLDE;
        }

        SituationFinanciereEtudiantDto premiereLigne = situationFinanciere.get(0);

        return new ResumeFinancierInscriptionDto(
                premiereLigne.getIdInscription(),
                premiereLigne.getIdEtudiant(),
                premiereLigne.getMatriculEtudiant(),
                premiereLigne.getNomCompletEtudiant(),
                premiereLigne.getIdPromo(),
                premiereLigne.getLibePromo(),
                premiereLigne.getIdAa(),
                montantTotalPrevu,
                montantTotalPaye,
                resteTotalAPayer,
                premiereLigne.getDevise(),
                statutFinancierGlobal
        );
    }
    @Override
    public PaiementFrais modifierPaiementFrais(
            Long idPaiementFrais,
            PaiementFrais paiementFrais
    ) {
        PaiementFrais paiementExistant = rechercherPaiementFraisParId(idPaiementFrais);

        if (paiementFrais.getMontantPaye() != null) {
            if (paiementFrais.getMontantPaye().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Le montant payé doit être supérieur à zéro.");
            }
            paiementExistant.setMontantPaye(paiementFrais.getMontantPaye());
        }

        if (paiementFrais.getDevisePaiement() != null) {
            paiementExistant.setDevisePaiement(paiementFrais.getDevisePaiement());
        }

        if (paiementFrais.getReferencePaiement() != null) {
            String nouvelleReference = paiementFrais.getReferencePaiement();

            if (!nouvelleReference.equals(paiementExistant.getReferencePaiement())
                    && paiementFraisRepository.existsByReferencePaiement(nouvelleReference)) {
                throw new RuntimeException("Cette référence de paiement existe déjà : "
                        + nouvelleReference);
            }

            paiementExistant.setReferencePaiement(nouvelleReference);
        }

        if (paiementFrais.getDatePaiement() != null) {
            paiementExistant.setDatePaiement(paiementFrais.getDatePaiement());
        }

        if (paiementFrais.getObservationPaiement() != null) {
            paiementExistant.setObservationPaiement(paiementFrais.getObservationPaiement());
        }

        if (paiementFrais.getCreatedBy() != null) {
            paiementExistant.setCreatedBy(paiementFrais.getCreatedBy());
        }

        return paiementFraisRepository.save(paiementExistant);
    }

    @Override
    public PaiementFrais validerPaiementFrais(Long idPaiementFrais) {
        PaiementFrais paiementFrais = rechercherPaiementFraisParId(idPaiementFrais);
        paiementFrais.setStatutPaiement(StatutPaiement.VALIDE);
        return paiementFraisRepository.save(paiementFrais);
    }

    @Override
    public PaiementFrais rejeterPaiementFrais(Long idPaiementFrais) {
        PaiementFrais paiementFrais = rechercherPaiementFraisParId(idPaiementFrais);
        paiementFrais.setStatutPaiement(StatutPaiement.REJETE);
        return paiementFraisRepository.save(paiementFrais);
    }

    @Override
    public PaiementFrais annulerPaiementFrais(Long idPaiementFrais) {
        PaiementFrais paiementFrais = rechercherPaiementFraisParId(idPaiementFrais);
        paiementFrais.setStatutPaiement(StatutPaiement.ANNULE);
        return paiementFraisRepository.save(paiementFrais);
    }

    @Override
    public void supprimerPaiementFrais(Long idPaiementFrais) {
        PaiementFrais paiementFrais = rechercherPaiementFraisParId(idPaiementFrais);
        paiementFraisRepository.delete(paiementFrais);
    }
}
