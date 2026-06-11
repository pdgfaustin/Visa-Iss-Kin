
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Banque;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.StatutCompte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface NumeroCompteRepository extends JpaRepository<NumeroCompte, String> {
    Optional<NumeroCompte> findByNumeroCompte(String numeroCompte);

    boolean existsByNumeroCompte(String numeroCompte);

    List<NumeroCompte> findByBanque(Banque banque);

    List<NumeroCompte> findByDeviseCompte(Devise deviseCompte);

    List<NumeroCompte> findByStatutCompte(StatutCompte statutCompte);

    List<NumeroCompte> findByBanqueAndDeviseCompte(
            Banque banque,
            Devise deviseCompte
    );

    List<NumeroCompte> findByBanqueAndStatutCompte(
            Banque banque,
            StatutCompte statutCompte
    );

    List<NumeroCompte> findByIntituleCompteContainingIgnoreCase(String intituleCompte);
}
