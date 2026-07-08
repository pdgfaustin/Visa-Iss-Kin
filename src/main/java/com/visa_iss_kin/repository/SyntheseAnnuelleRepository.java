package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SyntheseAnnuelleRepository extends JpaRepository<SyntheseAnnuelle, Long> {
    boolean existsByInscriptionAndAnneeAcademique(
            Inscription inscription,
            AnneeAcademique anneeAcademique
    );

    Optional<SyntheseAnnuelle> findByInscriptionAndAnneeAcademique(
            Inscription inscription,
            AnneeAcademique anneeAcademique
    );

    List<SyntheseAnnuelle> findByInscription(
            Inscription inscription
    );

    List<SyntheseAnnuelle> findByAnneeAcademique(
            AnneeAcademique anneeAcademique
    );

    List<SyntheseAnnuelle> findByStatutValidationAnnee(
            StatutValidationAnnee statutValidationAnnee
    );

    List<SyntheseAnnuelle> findByInscriptionPromotionIdPromo(
            String idPromo
    );

    List<SyntheseAnnuelle>
            findByAnneeAcademiqueIdAaAndInscriptionPromotionIdPromo(
                    String idAa,
                    String idPromo
            );

    List<SyntheseAnnuelle>
            findByAnneeAcademiqueIdAaAndStatutValidationAnnee(
                    String idAa,
                    StatutValidationAnnee statutValidationAnnee
            );

    List<SyntheseAnnuelle>
            findByAnneeAcademiqueIdAa(
                    String idAa
            );
}
