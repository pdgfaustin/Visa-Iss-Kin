package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SyntheseUEService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/syntheses-ue")
@CrossOrigin("*")
public class SyntheseUEController {
    private final SyntheseUEService syntheseUEService;

    public SyntheseUEController(
            SyntheseUEService syntheseUEService
    ) {
        this.syntheseUEService = syntheseUEService;
    }

    @PostMapping("/generer")
    public ResponseEntity<SyntheseUE> genererSyntheseUE(
            @RequestParam String idInscription,
            @RequestParam Long idSessionEvaluation,
            @RequestParam String idUE,
            @RequestParam(required = false) String createdBy
    ) {
        return ResponseEntity.ok(
                syntheseUEService.genererSyntheseUE(
                        idInscription,
                        idSessionEvaluation,
                        idUE,
                        createdBy
                )
        );
    }

    @PostMapping("/generer/inscription-session")
    public ResponseEntity<List<SyntheseUE>>
            genererSynthesesUEParInscriptionEtSession(
                    @RequestParam String idInscription,
                    @RequestParam Long idSessionEvaluation,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .genererSynthesesUEParInscriptionEtSession(
                                idInscription,
                                idSessionEvaluation,
                                createdBy
                        )
        );
    }

    @GetMapping
    public ResponseEntity<List<SyntheseUE>> listerSynthesesUE() {
        return ResponseEntity.ok(
                syntheseUEService.listerSynthesesUE()
        );
    }

    @GetMapping("/{idSyntheseUE}")
    public ResponseEntity<SyntheseUE> rechercherSyntheseUEParId(
            @PathVariable Long idSyntheseUE
    ) {
        return ResponseEntity.ok(
                syntheseUEService
                        .rechercherSyntheseUEParId(idSyntheseUE)
        );
    }

    @GetMapping("/inscription/{idInscription}/session/{idSessionEvaluation}/unite-enseignement/{idUE}")
    public ResponseEntity<SyntheseUE>
            rechercherSyntheseUEParInscriptionSessionEtUE(
                    @PathVariable String idInscription,
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idUE
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .rechercherSyntheseUEParInscriptionSessionEtUE(
                                idInscription,
                                idSessionEvaluation,
                                idUE
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParSession(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParSession(
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/unite-enseignement/{idUE}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParUniteEnseignement(
                    @PathVariable String idUE
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParUniteEnseignement(
                                idUE
                        )
        );
    }

    @GetMapping("/statut/{statutValidationUE}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParStatutValidation(
                    @PathVariable StatutValidationUE statutValidationUE
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParStatutValidation(
                                statutValidationUE
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}/session/{idSessionEvaluation}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParInscriptionEtSession(
                    @PathVariable String idInscription,
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParInscriptionEtSession(
                                idInscription,
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParPromotion(idPromo)
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParSessionEtPromotion(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParSessionEtPromotion(
                                idSessionEvaluation,
                                idPromo
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}/unite-enseignement/{idUE}")
    public ResponseEntity<List<SyntheseUE>>
            listerSynthesesUEParSessionPromotionEtUE(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo,
                    @PathVariable String idUE
            ) {

        return ResponseEntity.ok(
                syntheseUEService
                        .listerSynthesesUEParSessionPromotionEtUE(
                                idSessionEvaluation,
                                idPromo,
                                idUE
                        )
        );
    }

    @DeleteMapping("/{idSyntheseUE}")
    public ResponseEntity<Void> supprimerSyntheseUE(
            @PathVariable Long idSyntheseUE
    ) {
        syntheseUEService.supprimerSyntheseUE(idSyntheseUE);

        return ResponseEntity.noContent().build();
    }
}
