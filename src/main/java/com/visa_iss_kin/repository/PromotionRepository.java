package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.NiveauPromotion;
import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.model.Promotion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PromotionRepository extends JpaRepository<Promotion, String> {
    boolean existsBylibePromo(String libePromo);
    List<Promotion> findByOptionSuivie(OptionSuivie optS);
    List<Promotion> findByNiveauPromotion(NiveauPromotion niveauPromo);
}
