package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface NationaliteRepository extends JpaRepository<Nationalite, String> {
    boolean existsByLibeNation(String libeNation);
}
