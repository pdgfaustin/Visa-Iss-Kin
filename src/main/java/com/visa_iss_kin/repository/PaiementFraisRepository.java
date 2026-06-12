
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.PaiementFrais;
import com.visa_iss_kin.model.StatutPaiement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PaiementFraisRepository extends JpaRepository<PaiementFrais, Long> {
    Optional<PaiementFrais> findByReferencePaiement(String referencePaiement);

    boolean existsByReferencePaiement(String referencePaiement);

    List<PaiementFrais> findByInscription(Inscription inscription);

    List<PaiementFrais> findByAssociationFrais(
            AssocierFraisALaPromotion associationFrais
    );

    List<PaiementFrais> findByNumeroBanque(NumeroCompte numeroBanque);

    List<PaiementFrais> findByStatutPaiement(StatutPaiement statutPaiement);

    List<PaiementFrais> findByDevisePaiement(Devise devisePaiement);

    List<PaiementFrais> findByInscriptionAndAssociationFrais(
            Inscription inscription,
            AssocierFraisALaPromotion associationFrais
    );

    List<PaiementFrais> findByInscriptionAndStatutPaiement(
            Inscription inscription,
            StatutPaiement statutPaiement
    );
    @Query("""
       SELECT COALESCE(SUM(p.montantPaye), 0)
       FROM PaiementFrais p
       WHERE p.inscription = :inscription
       AND p.associationFrais = :associationFrais
       AND p.statutPaiement IN (
            com.visa_iss_kin.model.StatutPaiement.EN_ATTENTE,
            com.visa_iss_kin.model.StatutPaiement.VALIDE
       )
       """)
    BigDecimal calculerTotalPayeParInscriptionEtFrais(
            @Param("inscription") Inscription inscription,
            @Param("associationFrais") AssocierFraisALaPromotion associationFrais
    );
}
