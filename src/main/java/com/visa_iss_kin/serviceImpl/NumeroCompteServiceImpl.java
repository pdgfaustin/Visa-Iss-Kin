package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Banque;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.StatutCompte;
import com.visa_iss_kin.repository.BanqueRepository;
import com.visa_iss_kin.repository.NumeroCompteRepository;
import com.visa_iss_kin.service.NumeroCompteService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class NumeroCompteServiceImpl implements NumeroCompteService {
    private final NumeroCompteRepository numeroBanqueRepository;
    private final BanqueRepository banqueRepository;

    public NumeroCompteServiceImpl(
            NumeroCompteRepository numeroBanqueRepository,
            BanqueRepository banqueRepository
    ) {
        this.numeroBanqueRepository = numeroBanqueRepository;
        this.banqueRepository = banqueRepository;
    }

    @Override
    public NumeroCompte creerNumeroCompte(NumeroCompte numeroBanque) {

        if (numeroBanque.getIdNumeroCompte() == null
                || numeroBanque.getIdNumeroCompte().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant idNumeroCompte est obligatoire.");
        }

        if (numeroBanque.getNumeroCompte() == null
                || numeroBanque.getNumeroCompte().trim().isEmpty()) {
            throw new RuntimeException("Le numéro de compte est obligatoire.");
        }

        if (numeroBanque.getIntituleCompte() == null
                || numeroBanque.getIntituleCompte().trim().isEmpty()) {
            throw new RuntimeException("L'intitulé du compte est obligatoire.");
        }

        if (numeroBanque.getDeviseCompte() == null) {
            throw new RuntimeException("La devise du compte est obligatoire.");
        }

        if (numeroBanque.getBanque() == null
                || numeroBanque.getBanque().getIdBanque() == null) {
            throw new RuntimeException("La banque est obligatoire.");
        }

        if (numeroBanqueRepository.existsById(numeroBanque.getIdNumeroCompte())) {
            throw new RuntimeException("Un numéro bancaire existe déjà avec l'identifiant : "
                    + numeroBanque.getIdNumeroCompte());
        }

        if (numeroBanqueRepository.existsByNumeroCompte(numeroBanque.getNumeroCompte())) {
            throw new RuntimeException("Ce numéro de compte bancaire existe déjà : "
                    + numeroBanque.getNumeroCompte());
        }

        Banque banque = banqueRepository.findById(
                numeroBanque.getBanque().getIdBanque()
        ).orElseThrow(() -> new RuntimeException(
                "Banque introuvable avec l'identifiant : "
                + numeroBanque.getBanque().getIdBanque()
        ));

        if (numeroBanque.getStatutCompte() == null) {
            numeroBanque.setStatutCompte(StatutCompte.ACTIF);
        }

        if (numeroBanque.getCreatedAt() == null) {
            numeroBanque.setCreatedAt(LocalDateTime.now());
        }

        numeroBanque.setBanque(banque);

        return numeroBanqueRepository.save(numeroBanque);
    }

    @Override
    public List<NumeroCompte> listerNumerosCompte() {
        return numeroBanqueRepository.findAll();
    }

    @Override
    public NumeroCompte rechercherNumeroCompteParId(String idNumeroCompte) {
        return numeroBanqueRepository.findById(idNumeroCompte)
                .orElseThrow(() -> new RuntimeException(
                        "Numéro bancaire introuvable avec l'identifiant : "
                        + idNumeroCompte
                ));
    }

    @Override
    public NumeroCompte rechercherNumeroCompteParNumeroCompte(String numeroCompte) {
        return numeroBanqueRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException(
                        "Aucun compte bancaire trouvé avec le numéro : "
                        + numeroCompte
                ));
    }

    @Override
    public List<NumeroCompte> listerNumerosParBanque(String idBanque) {
        Banque banque = banqueRepository.findById(idBanque)
                .orElseThrow(() -> new RuntimeException(
                        "Banque introuvable avec l'identifiant : " + idBanque
                ));

        return numeroBanqueRepository.findByBanque(banque);
    }

    @Override
    public List<NumeroCompte> listerNumerosParDevise(Devise deviseCompte) {
        return numeroBanqueRepository.findByDeviseCompte(deviseCompte);
    }

    @Override
    public List<NumeroCompte> listerNumerosParStatut(StatutCompte statutCompte) {
        return numeroBanqueRepository.findByStatutCompte(statutCompte);
    }

    @Override
    public List<NumeroCompte> listerNumerosParBanqueEtDevise(
            String idBanque,
            Devise deviseCompte
    ) {
        Banque banque = banqueRepository.findById(idBanque)
                .orElseThrow(() -> new RuntimeException(
                        "Banque introuvable avec l'identifiant : " + idBanque
                ));

        return numeroBanqueRepository.findByBanqueAndDeviseCompte(banque, deviseCompte);
    }

    @Override
    public List<NumeroCompte> listerNumerosParBanqueEtStatut(
            String idBanque,
            StatutCompte statutCompte
    ) {
        Banque banque = banqueRepository.findById(idBanque)
                .orElseThrow(() -> new RuntimeException(
                        "Banque introuvable avec l'identifiant : " + idBanque
                ));

        return numeroBanqueRepository.findByBanqueAndStatutCompte(banque, statutCompte);
    }

    @Override
    public List<NumeroCompte> rechercherNumerosParIntitule(String intituleCompte) {
        return numeroBanqueRepository.findByIntituleCompteContainingIgnoreCase(intituleCompte);
    }

    @Override
    public NumeroCompte modifierNumeroCompte(
            String idNumeroCompte,
            NumeroCompte numeroBanque
    ) {
        NumeroCompte numeroExistant = rechercherNumeroCompteParId(idNumeroCompte);

        if (numeroBanque.getNumeroCompte() != null) {
            String nouveauNumeroCompte = numeroBanque.getNumeroCompte();

            if (!nouveauNumeroCompte.equals(numeroExistant.getNumeroCompte())
                    && numeroBanqueRepository.existsByNumeroCompte(nouveauNumeroCompte)) {
                throw new RuntimeException("Ce numéro de compte bancaire existe déjà : "
                        + nouveauNumeroCompte);
            }

            numeroExistant.setNumeroCompte(nouveauNumeroCompte);
        }

        if (numeroBanque.getIntituleCompte() != null) {
            numeroExistant.setIntituleCompte(numeroBanque.getIntituleCompte());
        }

        if (numeroBanque.getDeviseCompte() != null) {
            numeroExistant.setDeviseCompte(numeroBanque.getDeviseCompte());
        }

        if (numeroBanque.getStatutCompte() != null) {
            numeroExistant.setStatutCompte(numeroBanque.getStatutCompte());
        }

        if (numeroBanque.getNomGestionnaire() != null) {
            numeroExistant.setNomGestionnaire(numeroBanque.getNomGestionnaire());
        }

        if (numeroBanque.getTelephoneGestionnaire() != null) {
            numeroExistant.setTelephoneGestionnaire(numeroBanque.getTelephoneGestionnaire());
        }

        if (numeroBanque.getEmailGestionnaire() != null) {
            numeroExistant.setEmailGestionnaire(numeroBanque.getEmailGestionnaire());
        }

        if (numeroBanque.getCreatedBy() != null) {
            numeroExistant.setCreatedBy(numeroBanque.getCreatedBy());
        }

        if (numeroBanque.getBanque() != null
                && numeroBanque.getBanque().getIdBanque() != null) {

            Banque banque = banqueRepository.findById(
                    numeroBanque.getBanque().getIdBanque()
            ).orElseThrow(() -> new RuntimeException(
                    "Banque introuvable avec l'identifiant : "
                    + numeroBanque.getBanque().getIdBanque()
            ));

            numeroExistant.setBanque(banque);
        }

        return numeroBanqueRepository.save(numeroExistant);
    }

    @Override
    public void supprimerNumeroCompte(String idNumeroCompte) {
        NumeroCompte numeroBanque = rechercherNumeroCompteParId(idNumeroCompte);
        numeroBanqueRepository.delete(numeroBanque);
    }


}
