package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Nationalite;
import com.visa_iss_kin.repository.NationaliteRepository;
import com.visa_iss_kin.service.NationaliteService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class NationaliteServiceImpl implements NationaliteService {
    private final NationaliteRepository nationR;

    public NationaliteServiceImpl(NationaliteRepository nationR) {
        this.nationR = nationR;
    }

    @Override
    public Nationalite creerNationalite(Nationalite nationalite) {

        if (nationalite.getIdNation() == null || nationalite.getIdNation().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant de la nationalité idNation est obligatoire.");
        }

        if (nationalite.getLibeNation() == null || nationalite.getLibeNation().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de la nationalité libeNation est obligatoire.");
        }

        if (nationR.existsById(nationalite.getIdNation())) {
            throw new RuntimeException("Une nationalité existe déjà avec l'identifiant : " + nationalite.getIdNation());
        }

        if (nationR.existsByLibeNation(nationalite.getLibeNation())) {
            throw new RuntimeException("Une nationalité existe déjà avec le libellé : " + nationalite.getLibeNation());
        }

        if (nationalite.getCreatedAt() == null) {
            nationalite.setCreatedAt(LocalDateTime.now());
        }

        return nationR.save(nationalite);
    }

    @Override
    public List<Nationalite> listeDesNationalites() {
        return nationR.findAll();
    }

    @Override
    public Nationalite rechercherNationaliteParId(String idNation) {
        return nationR.findById(idNation)
                .orElseThrow(() -> new RuntimeException(
                        "Nationalité introuvable avec l'identifiant : " + idNation
                ));
    }

    @Override
    public Nationalite modifierNationalite(String idNation, Nationalite nationalite) {
        Nationalite nationaliteExistante = rechercherNationaliteParId(idNation);

        if (nationalite.getLibeNation() != null) {
            nationaliteExistante.setLibeNation(nationalite.getLibeNation());
        }

        if (nationalite.getCreatedBy() != null) {
            nationaliteExistante.setCreatedBy(nationalite.getCreatedBy());
        }

        if (nationaliteExistante.getCreatedAt() == null) {
            nationaliteExistante.setCreatedAt(LocalDateTime.now());
        }

        return nationR.save(nationaliteExistante);
    }

    @Override
    public void supprimerNationalite(String idNation) {
        Nationalite nationalite = rechercherNationaliteParId(idNation);
        nationR.delete(nationalite);
    }
}
