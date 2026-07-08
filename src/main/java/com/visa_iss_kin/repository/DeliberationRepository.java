package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface DeliberationRepository extends JpaRepository<Deliberation, Long> {
    boolean existsBySyntheseAnnuelle(
            SyntheseAnnuelle syntheseAnnuelle
    );

    Optional<Deliberation> findBySyntheseAnnuelle(
            SyntheseAnnuelle syntheseAnnuelle
    );

    List<Deliberation> findByInscription(
            Inscription inscription
    );

    List<Deliberation> findByAnneeAcademique(
            AnneeAcademique anneeAcademique
    );

    List<Deliberation> findByDecisionDeliberation(
            DecisionDeliberation decisionDeliberation
    );

    List<Deliberation> findByValidee(
            Boolean validee
    );

    List<Deliberation> findByInscriptionPromotionIdPromo(
            String idPromo
    );

    List<Deliberation>
            findByAnneeAcademiqueIdAaAndInscriptionPromotionIdPromo(
                    String idAa,
                    String idPromo
            );

    List<Deliberation>
            findByAnneeAcademiqueIdAaAndDecisionDeliberation(
                    String idAa,
                    DecisionDeliberation decisionDeliberation
            );

    List<Deliberation>
            findByAnneeAcademiqueIdAaAndValidee(
                    String idAa,
                    Boolean validee
            );

    List<Deliberation>
            findByAnneeAcademiqueIdAa(
                    String idAa
            );
}
