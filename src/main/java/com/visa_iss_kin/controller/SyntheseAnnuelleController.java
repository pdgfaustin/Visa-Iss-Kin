package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SyntheseAnnuelleService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/syntheses-annuelles")
@CrossOrigin("*")
public class SyntheseAnnuelleController {
    private final SyntheseAnnuelleService syntheseAnnuelleService;

    public SyntheseAnnuelleController(
            SyntheseAnnuelleService syntheseAnnuelleService
    ) {
        this.syntheseAnnuelleService = syntheseAnnuelleService;
    }

    @PostMapping("/generer")
    public ResponseEntity<SyntheseAnnuelle> genererSyntheseAnnuelle(
            @RequestParam String idInscription,
            @RequestParam String idAa,
            @RequestParam(required = false) String createdBy
    ) {
        return ResponseEntity.ok(
                syntheseAnnuelleService.genererSyntheseAnnuelle(
                        idInscription,
                        idAa,
                        createdBy
                )
        );
    }

    @PostMapping("/generer/annee-promotion")
    public ResponseEntity<List<SyntheseAnnuelle>>
            genererSynthesesAnnuellesParAnneeEtPromotion(
                    @RequestParam String idAa,
                    @RequestParam String idPromo,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .genererSynthesesAnnuellesParAnneeEtPromotion(
                                idAa,
                                idPromo,
                                createdBy
                        )
        );
    }

    @GetMapping
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuelles() {

        return ResponseEntity.ok(
                syntheseAnnuelleService.listerSynthesesAnnuelles()
        );
    }

    @GetMapping("/{idSyntheseAnnuelle}")
    public ResponseEntity<SyntheseAnnuelle>
            rechercherSyntheseAnnuelleParId(
                    @PathVariable Long idSyntheseAnnuelle
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .rechercherSyntheseAnnuelleParId(
                                idSyntheseAnnuelle
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}/annee-academique/{idAa}")
    public ResponseEntity<SyntheseAnnuelle>
            rechercherSyntheseAnnuelleParInscriptionEtAnnee(
                    @PathVariable String idInscription,
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .rechercherSyntheseAnnuelleParInscriptionEtAnnee(
                                idInscription,
                                idAa
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParAnneeAcademique(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParAnneeAcademique(
                                idAa
                        )
        );
    }

    @GetMapping("/statut/{statutValidationAnnee}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParStatutValidation(
                    @PathVariable
                    StatutValidationAnnee statutValidationAnnee
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParStatutValidation(
                                statutValidationAnnee
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParAnneeEtPromotion(
                    @PathVariable String idAa,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParAnneeEtPromotion(
                                idAa,
                                idPromo
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}/statut/{statutValidationAnnee}")
    public ResponseEntity<List<SyntheseAnnuelle>>
            listerSynthesesAnnuellesParAnneeEtStatut(
                    @PathVariable String idAa,
                    @PathVariable
                    StatutValidationAnnee statutValidationAnnee
            ) {

        return ResponseEntity.ok(
                syntheseAnnuelleService
                        .listerSynthesesAnnuellesParAnneeEtStatut(
                                idAa,
                                statutValidationAnnee
                        )
        );
    }

    @DeleteMapping("/{idSyntheseAnnuelle}")
    public ResponseEntity<Void> supprimerSyntheseAnnuelle(
            @PathVariable Long idSyntheseAnnuelle
    ) {
        syntheseAnnuelleService.supprimerSyntheseAnnuelle(
                idSyntheseAnnuelle
        );

        return ResponseEntity.noContent().build();
    }
}
