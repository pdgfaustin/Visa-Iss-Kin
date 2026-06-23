package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.StatutAnneeAcademique;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique, String> {
    List<AnneeAcademique> findByStatutAnneeAcademique(StatutAnneeAcademique statutAnnee);

    boolean existsByStatutAnneeAcademique(StatutAnneeAcademique statutAnnee);
    boolean existsById(String idAA);
}
