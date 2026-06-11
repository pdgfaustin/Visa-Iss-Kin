
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Banque;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface BanqueRepository extends JpaRepository<Banque, String> {
    List<Banque> findByLibeBanqueContainingIgnoreCase(String libeBanque);

    List<Banque> findBySigleBanqueContainingIgnoreCase(String sigleBanque);
}
