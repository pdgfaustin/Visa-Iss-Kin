package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/deliberations")
@CrossOrigin("*")
public class DeliberationController {
    private final DeliberationService deliberationService;

    public DeliberationController(
            DeliberationService deliberationService
    ) {
        this.deliberationService = deliberationService;
    }

    @PostMapping("/generer")
    public ResponseEntity<Deliberation> genererDeliberation(
            @RequestParam Long idSyntheseAnnuelle,
            @RequestParam(required = false) String createdBy
    ) {
        return ResponseEntity.ok(
                deliberationService.genererDeliberation(
                        idSyntheseAnnuelle,
                        createdBy
                )
        );
    }

    @PostMapping("/generer/annee-promotion")
    public ResponseEntity<List<Deliberation>>
            genererDeliberationsParAnneeEtPromotion(
                    @RequestParam String idAa,
                    @RequestParam String idPromo,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .genererDeliberationsParAnneeEtPromotion(
                                idAa,
                                idPromo,
                                createdBy
                        )
        );
    }

    @PutMapping("/{idDeliberation}/valider")
    public ResponseEntity<Deliberation> validerDeliberation(
            @PathVariable Long idDeliberation,
            @RequestParam String validatedBy,
            @RequestParam(required = false) String observationJury
    ) {
        return ResponseEntity.ok(
                deliberationService.validerDeliberation(
                        idDeliberation,
                        validatedBy,
                        observationJury
                )
        );
    }

    @PutMapping("/{idDeliberation}/decision")
    public ResponseEntity<Deliberation> modifierDecisionDeliberation(
            @PathVariable Long idDeliberation,
            @RequestParam DecisionDeliberation decisionDeliberation,
            @RequestParam(required = false) String motifDecision,
            @RequestParam(required = false) String observationJury
    ) {
        return ResponseEntity.ok(
                deliberationService.modifierDecisionDeliberation(
                        idDeliberation,
                        decisionDeliberation,
                        motifDecision,
                        observationJury
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Deliberation>> listerDeliberations() {
        return ResponseEntity.ok(
                deliberationService.listerDeliberations()
        );
    }

    @GetMapping("/{idDeliberation}")
    public ResponseEntity<Deliberation> rechercherDeliberationParId(
            @PathVariable Long idDeliberation
    ) {
        return ResponseEntity.ok(
                deliberationService.rechercherDeliberationParId(
                        idDeliberation
                )
        );
    }

    @GetMapping("/synthese-annuelle/{idSyntheseAnnuelle}")
    public ResponseEntity<Deliberation>
            rechercherDeliberationParSyntheseAnnuelle(
                    @PathVariable Long idSyntheseAnnuelle
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .rechercherDeliberationParSyntheseAnnuelle(
                                idSyntheseAnnuelle
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParAnneeAcademique(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParAnneeAcademique(
                                idAa
                        )
        );
    }

    @GetMapping("/decision/{decisionDeliberation}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParDecision(
                    @PathVariable
                    DecisionDeliberation decisionDeliberation
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParDecision(
                                decisionDeliberation
                        )
        );
    }

    @GetMapping("/validation/{validee}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParValidation(
                    @PathVariable Boolean validee
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParValidation(
                                validee
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}/promotion/{idPromo}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParAnneeEtPromotion(
                    @PathVariable String idAa,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParAnneeEtPromotion(
                                idAa,
                                idPromo
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}/decision/{decisionDeliberation}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParAnneeEtDecision(
                    @PathVariable String idAa,
                    @PathVariable
                    DecisionDeliberation decisionDeliberation
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParAnneeEtDecision(
                                idAa,
                                decisionDeliberation
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}/validation/{validee}")
    public ResponseEntity<List<Deliberation>>
            listerDeliberationsParAnneeEtValidation(
                    @PathVariable String idAa,
                    @PathVariable Boolean validee
            ) {

        return ResponseEntity.ok(
                deliberationService
                        .listerDeliberationsParAnneeEtValidation(
                                idAa,
                                validee
                        )
        );
    }

    @DeleteMapping("/{idDeliberation}")
    public ResponseEntity<Void> supprimerDeliberation(
            @PathVariable Long idDeliberation
    ) {
        deliberationService.supprimerDeliberation(idDeliberation);

        return ResponseEntity.noContent().build();
    }
}
