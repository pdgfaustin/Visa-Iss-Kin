package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SectionRepository extends JpaRepository<Section, String> {

    boolean existsByLibeSection(String libeSection);
}
