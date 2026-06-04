
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.FraisAcademique;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface FraisAcademiqueRepository extends JpaRepository<FraisAcademique, String> {
    List<FraisAcademique> findByLibeFraisContainingIgnoreCase(String libeFrais);
}
