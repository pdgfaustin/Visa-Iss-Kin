
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface SessionEvaluationRepository extends JpaRepository<SessionEvaluation, Long> {
    boolean existsByAnneeAcademiqueAndSemestreAndTypeSession(
            AnneeAcademique anneeAcademique,
            Semestre semestre,
            TypeSession typeSession
    );

    Optional<SessionEvaluation>
            findByAnneeAcademiqueAndSemestreAndTypeSession(
                    AnneeAcademique anneeAcademique,
                    Semestre semestre,
                    TypeSession typeSession
            );

    List<SessionEvaluation> findByAnneeAcademique(
            AnneeAcademique anneeAcademique
    );

    List<SessionEvaluation> findByAnneeAcademiqueOrderBySemestreAscTypeSessionAsc(
            AnneeAcademique anneeAcademique
    );

    List<SessionEvaluation> findBySemestre(
            Semestre semestre
    );

    List<SessionEvaluation> findByTypeSession(
            TypeSession typeSession
    );

    List<SessionEvaluation> findByOuverte(
            Boolean ouverte
    );

    List<SessionEvaluation> findByAnneeAcademiqueAndOuverte(
            AnneeAcademique anneeAcademique,
            Boolean ouverte
    );
}
