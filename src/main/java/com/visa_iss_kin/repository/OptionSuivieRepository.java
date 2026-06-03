package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.model.Section;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface OptionSuivieRepository extends JpaRepository<OptionSuivie, String> {
    boolean existsByLibeOpt(String libeOpt);

    List<OptionSuivie> findBySection(Section section);
}
