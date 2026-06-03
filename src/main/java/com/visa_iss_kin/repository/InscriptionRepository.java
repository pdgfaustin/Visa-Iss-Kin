package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface InscriptionRepository extends JpaRepository<Inscription, String> {
    
}
