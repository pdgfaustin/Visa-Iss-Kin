
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.ElementConstitutif;
import com.visa_iss_kin.model.UniteEnseignement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface ElementConstitutifRepository extends JpaRepository<ElementConstitutif, String> {
    Optional<ElementConstitutif> findByCodeEC(String codeEC);

    boolean existsByCodeEC(String codeEC);

    List<ElementConstitutif> findByLibeECContainingIgnoreCase(String libeEC);

    List<ElementConstitutif> findByCodeECContainingIgnoreCase(String codeEC);

    List<ElementConstitutif> findByUniteEnseignement(
            UniteEnseignement uniteEnseignement
    );
}
