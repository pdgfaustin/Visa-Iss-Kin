
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Banque;
import com.visa_iss_kin.repository.BanqueRepository;
import com.visa_iss_kin.service.BanqueService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class BanqueServiceImpl implements BanqueService {
    private final BanqueRepository banqueRepository;

    public BanqueServiceImpl(BanqueRepository banqueRepository) {
        this.banqueRepository = banqueRepository;
    }

    @Override
    public Banque creerBanque(Banque banque) {

        if (banque.getIdBanque() == null || banque.getIdBanque().trim().isEmpty()) {
            throw new RuntimeException("L'identifiant de la banque idBanque est obligatoire.");
        }

        if (banque.getLibeBanque() == null || banque.getLibeBanque().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de la banque libeBanque est obligatoire.");
        }

        if (banqueRepository.existsById(banque.getIdBanque())) {
            throw new RuntimeException("Une banque existe déjà avec l'identifiant : "
                    + banque.getIdBanque());
        }

        if (banque.getCreatedAt() == null) {
            banque.setCreatedAt(LocalDateTime.now());
        }

        return banqueRepository.save(banque);
    }

    @Override
    public List<Banque> listerBanques() {
        return banqueRepository.findAll();
    }

    @Override
    public Banque rechercherBanqueParId(String idBanque) {
        return banqueRepository.findById(idBanque)
                .orElseThrow(() -> new RuntimeException(
                        "Banque introuvable avec l'identifiant : " + idBanque
                ));
    }

    @Override
    public List<Banque> rechercherBanquesParLibelle(String libeBanque) {
        return banqueRepository.findByLibeBanqueContainingIgnoreCase(libeBanque);
    }

    @Override
    public List<Banque> rechercherBanquesParSigle(String sigleBanque) {
        return banqueRepository.findBySigleBanqueContainingIgnoreCase(sigleBanque);
    }

    @Override
    public Banque modifierBanque(String idBanque, Banque banque) {

        Banque banqueExistante = rechercherBanqueParId(idBanque);

        if (banque.getLibeBanque() != null) {
            banqueExistante.setLibeBanque(banque.getLibeBanque());
        }

        if (banque.getSigleBanque() != null) {
            banqueExistante.setSigleBanque(banque.getSigleBanque());
        }

        if (banque.getCreatedBy() != null) {
            banqueExistante.setCreatedBy(banque.getCreatedBy());
        }

        if (banqueExistante.getCreatedAt() == null) {
            banqueExistante.setCreatedAt(LocalDateTime.now());
        }

        return banqueRepository.save(banqueExistante);
    }

    @Override
    public void supprimerBanque(String idBanque) {
        Banque banque = rechercherBanqueParId(idBanque);
        banqueRepository.delete(banque);
    }
}
