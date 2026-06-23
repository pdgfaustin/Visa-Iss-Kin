
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.HoraireCours;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface HoraireCoursService {
    HoraireCours creerHoraireCours(
            HoraireCours horaireCours
    );

    List<HoraireCours> listerHorairesCours();

    HoraireCours rechercherHoraireCoursParId(
            Long idHoraireCours
    );

    List<HoraireCours> listerHorairesParDate(
            LocalDate dateCours
    );

    List<HoraireCours> listerHorairesParSalle(
            String salle
    );

    List<HoraireCours> listerHorairesParChargeHoraire(
            Long idChargeHoraire
    );

    List<HoraireCours> listerHorairesParSalleEtDate(
            String salle,
            LocalDate dateCours
    );

    HoraireCours modifierHoraireCours(
            Long idHoraireCours,
            HoraireCours horaireCours
    );
    List<HoraireCours> listerHorairesEntreDeuxDates(
        LocalDate dateDebut,
        LocalDate dateFin
    );
    List<HoraireCours> listerHorairesEntreDeuxDatesParPromotion(
        LocalDate dateDebut,
        LocalDate dateFin,
        String idPromo
    );
    void supprimerHoraireCours(
            Long idHoraireCours
    );
}
