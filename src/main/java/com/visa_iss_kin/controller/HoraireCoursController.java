
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.HoraireCours;
import com.visa_iss_kin.service.HoraireCoursService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/horaires-cours")
@CrossOrigin("*")
public class HoraireCoursController {
    private final HoraireCoursService horaireCoursService;

    public HoraireCoursController(
            HoraireCoursService horaireCoursService
    ) {
        this.horaireCoursService = horaireCoursService;
    }

    @PostMapping
    public ResponseEntity<HoraireCours> creerHoraireCours(
            @RequestBody HoraireCours horaireCours
    ) {
        HoraireCours nouvelHoraire =
                horaireCoursService.creerHoraireCours(
                        horaireCours
                );

        return new ResponseEntity<>(
                nouvelHoraire,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<HoraireCours>>
            listerHorairesCours() {

        return ResponseEntity.ok(
                horaireCoursService.listerHorairesCours()
        );
    }

    @GetMapping("/{idHoraireCours}")
    public ResponseEntity<HoraireCours>
            rechercherHoraireCoursParId(
                    @PathVariable Long idHoraireCours
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .rechercherHoraireCoursParId(
                                idHoraireCours
                        )
        );
    }

    @GetMapping("/date/{dateCours}")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesParDate(
                    @PathVariable LocalDate dateCours
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesParDate(dateCours)
        );
    }

    @GetMapping("/periode")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesEntreDeuxDates(
                    @RequestParam LocalDate dateDebut,
                    @RequestParam LocalDate dateFin
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesEntreDeuxDates(
                                dateDebut,
                                dateFin
                        )
        );
    }

    @GetMapping("/periode/promotion/{idPromo}")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesEntreDeuxDatesParPromotion(
                    @PathVariable String idPromo,
                    @RequestParam LocalDate dateDebut,
                    @RequestParam LocalDate dateFin
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesEntreDeuxDatesParPromotion(
                                dateDebut,
                                dateFin,
                                idPromo
                        )
        );
    }

    @GetMapping("/salle/{salle}")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesParSalle(
                    @PathVariable String salle
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesParSalle(salle)
        );
    }

    @GetMapping("/charge-horaire/{idChargeHoraire}")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesParChargeHoraire(
                    @PathVariable Long idChargeHoraire
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesParChargeHoraire(
                                idChargeHoraire
                        )
        );
    }

    @GetMapping("/salle/{salle}/date/{dateCours}")
    public ResponseEntity<List<HoraireCours>>
            listerHorairesParSalleEtDate(
                    @PathVariable String salle,
                    @PathVariable LocalDate dateCours
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .listerHorairesParSalleEtDate(
                                salle,
                                dateCours
                        )
        );
    }

    @PutMapping("/{idHoraireCours}")
    public ResponseEntity<HoraireCours>
            modifierHoraireCours(
                    @PathVariable Long idHoraireCours,
                    @RequestBody HoraireCours horaireCours
            ) {

        return ResponseEntity.ok(
                horaireCoursService
                        .modifierHoraireCours(
                                idHoraireCours,
                                horaireCours
                        )
        );
    }

    @DeleteMapping("/{idHoraireCours}")
    public ResponseEntity<Void> supprimerHoraireCours(
            @PathVariable Long idHoraireCours
    ) {
        horaireCoursService
                .supprimerHoraireCours(idHoraireCours);

        return ResponseEntity.noContent().build();
    }
}
