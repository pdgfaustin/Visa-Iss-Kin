
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.HoraireCours;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface HoraireCoursRepository extends JpaRepository<HoraireCours, Long> {
    List<HoraireCours> findByDateCours(LocalDate dateCours);

    List<HoraireCours> findBySalle(String salle);

    List<HoraireCours> findByChargeHoraire(
            ChargeHoraire chargeHoraire
    );

    List<HoraireCours> findByChargeHoraireOrderByDateCoursAscHeureDebutAsc(
            ChargeHoraire chargeHoraire
    );

    List<HoraireCours> findByDateCoursOrderByHeureDebutAsc(
            LocalDate dateCours
    );

    List<HoraireCours> findBySalleAndDateCoursOrderByHeureDebutAsc(
            String salle,
            LocalDate dateCours
    );
    List<HoraireCours> findByDateCoursBetweenOrderByDateCoursAscHeureDebutAsc(
                LocalDate dateDebut,
                LocalDate dateFin
        );
    @Query("""
        SELECT h
        FROM HoraireCours h
        WHERE h.dateCours BETWEEN :dateDebut AND :dateFin
        AND h.chargeHoraire.maquettePromotion.promotion.idPromo = :idPromo
        ORDER BY h.dateCours ASC, h.heureDebut ASC
        """)
    List<HoraireCours> listerHorairesEntreDeuxDatesParPromotion(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("idPromo") String idPromo
    );
}
