package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.HoraireCours;
import com.visa_iss_kin.model.PresenceCours;
import com.visa_iss_kin.model.StatutExecutionCours;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface PresenceCoursRepository extends JpaRepository<PresenceCours, Long> {
    boolean existsByHoraireCours(
            HoraireCours horaireCours
    );

    Optional<PresenceCours> findByHoraireCours(
            HoraireCours horaireCours
    );

    List<PresenceCours> findByStatutExecution(
            StatutExecutionCours statutExecution
    );

    List<PresenceCours>
            findByHoraireCoursChargeHoraireIdChargeHoraire(
                    Long idChargeHoraire
            );

    List<PresenceCours>
            findByHoraireCoursChargeHoraireEnseignantMatrEns(
                    String matrEns
            );

    List<PresenceCours>
            findByHoraireCoursChargeHoraireMaquettePromotionPromotionIdPromo(
                    String idPromo
            );
}
