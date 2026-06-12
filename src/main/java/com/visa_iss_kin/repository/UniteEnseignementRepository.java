
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.UniteEnseignement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface UniteEnseignementRepository extends JpaRepository<UniteEnseignement, String> {
    Optional<UniteEnseignement> findByCodeUE(String codeUE);

    boolean existsByCodeUE(String codeUE);

    List<UniteEnseignement> findByLibeUEContainingIgnoreCase(String libeUE);

    List<UniteEnseignement> findByCodeUEContainingIgnoreCase(String codeUE);
}
