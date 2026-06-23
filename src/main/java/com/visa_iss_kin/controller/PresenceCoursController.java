package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.PresenceCoursService;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/presences-cours")
@CrossOrigin("*")
public class PresenceCoursController {
    private final PresenceCoursService presenceCoursService;

    public PresenceCoursController(
            PresenceCoursService presenceCoursService
    ) {
        this.presenceCoursService = presenceCoursService;
    }

    @PostMapping
    public ResponseEntity<PresenceCours> creerPresenceCours(
            @RequestBody PresenceCours presenceCours
    ) {
        PresenceCours nouvellePresence =
                presenceCoursService.creerPresenceCours(
                        presenceCours
                );

        return new ResponseEntity<>(
                nouvellePresence,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<PresenceCours>>
            listerPresencesCours() {

        return ResponseEntity.ok(
                presenceCoursService.listerPresencesCours()
        );
    }

    @GetMapping("/{idPresenceCours}")
    public ResponseEntity<PresenceCours>
            rechercherPresenceCoursParId(
                    @PathVariable Long idPresenceCours
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .rechercherPresenceCoursParId(
                                idPresenceCours
                        )
        );
    }

    @GetMapping("/horaire/{idHoraireCours}")
    public ResponseEntity<PresenceCours>
            rechercherPresenceParHoraire(
                    @PathVariable Long idHoraireCours
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .rechercherPresenceParHoraire(
                                idHoraireCours
                        )
        );
    }

    @GetMapping("/statut/{statutExecution}")
    public ResponseEntity<List<PresenceCours>>
            listerPresencesParStatut(
                    @PathVariable
                    StatutExecutionCours statutExecution
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .listerPresencesParStatut(
                                statutExecution
                        )
        );
    }

    @GetMapping("/charge-horaire/{idChargeHoraire}")
    public ResponseEntity<List<PresenceCours>>
            listerPresencesParChargeHoraire(
                    @PathVariable Long idChargeHoraire
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .listerPresencesParChargeHoraire(
                                idChargeHoraire
                        )
        );
    }

    @GetMapping("/enseignant/{matrEns}")
    public ResponseEntity<List<PresenceCours>>
            listerPresencesParEnseignant(
                    @PathVariable String matrEns
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .listerPresencesParEnseignant(
                                matrEns
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<PresenceCours>>
            listerPresencesParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .listerPresencesParPromotion(
                                idPromo
                        )
        );
    }

    @PutMapping("/{idPresenceCours}")
    public ResponseEntity<PresenceCours>
            modifierPresenceCours(
                    @PathVariable Long idPresenceCours,
                    @RequestBody PresenceCours presenceCours
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .modifierPresenceCours(
                                idPresenceCours,
                                presenceCours
                        )
        );
    }

    @PatchMapping(
            "/{idPresenceCours}/statut/{statutExecution}"
    )
    public ResponseEntity<PresenceCours>
            changerStatutExecution(
                    @PathVariable Long idPresenceCours,
                    @PathVariable
                    StatutExecutionCours statutExecution
            ) {

        return ResponseEntity.ok(
                presenceCoursService
                        .changerStatutExecution(
                                idPresenceCours,
                                statutExecution
                        )
        );
    }

    @DeleteMapping("/{idPresenceCours}")
    public ResponseEntity<Void> supprimerPresenceCours(
            @PathVariable Long idPresenceCours
    ) {
        presenceCoursService.supprimerPresenceCours(
                idPresenceCours
        );

        return ResponseEntity.noContent().build();
    }
}
