package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SyntheseEvaluationECService;
import org.springframework.http.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/syntheses-evaluations-ec")
@CrossOrigin("*")
public class SyntheseEvaluationECController {
    private final SyntheseEvaluationECService syntheseEvaluationECService;

    public SyntheseEvaluationECController(
            SyntheseEvaluationECService syntheseEvaluationECService
    ) {
        this.syntheseEvaluationECService =
                syntheseEvaluationECService;
    }

    @PostMapping("/generer")
    public ResponseEntity<SyntheseEvaluationEC>
            genererSyntheseEvaluationEC(
                    @RequestParam String idInscription,
                    @RequestParam Long idSessionEvaluation,
                    @RequestParam String idEC,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .genererSyntheseEvaluationEC(
                                idInscription,
                                idSessionEvaluation,
                                idEC,
                                createdBy
                        )
        );
    }

    @PostMapping("/generer/inscription-session")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            genererSynthesesParInscriptionEtSession(
                    @RequestParam String idInscription,
                    @RequestParam Long idSessionEvaluation,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .genererSynthesesParInscriptionEtSession(
                                idInscription,
                                idSessionEvaluation,
                                createdBy
                        )
        );
    }

    @GetMapping
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesEvaluationEC() {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesEvaluationEC()
        );
    }

    @GetMapping("/{idSyntheseEvaluationEC}")
    public ResponseEntity<SyntheseEvaluationEC>
            rechercherSyntheseEvaluationECParId(
                    @PathVariable Long idSyntheseEvaluationEC
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .rechercherSyntheseEvaluationECParId(
                                idSyntheseEvaluationEC
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}/session/{idSessionEvaluation}/element-constitutif/{idEC}")
    public ResponseEntity<SyntheseEvaluationEC>
            rechercherSyntheseParInscriptionSessionEtEC(
                    @PathVariable String idInscription,
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .rechercherSyntheseParInscriptionSessionEtEC(
                                idInscription,
                                idSessionEvaluation,
                                idEC
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParSession(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParSession(
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/element-constitutif/{idEC}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParElementConstitutif(
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParElementConstitutif(
                                idEC
                        )
        );
    }

    @GetMapping("/statut/{statutValidation}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParStatutValidation(
                    @PathVariable
                    StatutValidationEC statutValidation
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParStatutValidation(
                                statutValidation
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParSessionEtPromotion(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParSessionEtPromotion(
                                idSessionEvaluation,
                                idPromo
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}/element-constitutif/{idEC}")
    public ResponseEntity<List<SyntheseEvaluationEC>>
            listerSynthesesParSessionPromotionEtEC(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo,
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                syntheseEvaluationECService
                        .listerSynthesesParSessionPromotionEtEC(
                                idSessionEvaluation,
                                idPromo,
                                idEC
                        )
        );
    }

    @DeleteMapping("/{idSyntheseEvaluationEC}")
    public ResponseEntity<Void> supprimerSyntheseEvaluationEC(
            @PathVariable Long idSyntheseEvaluationEC
    ) {
        syntheseEvaluationECService
                .supprimerSyntheseEvaluationEC(
                        idSyntheseEvaluationEC
                );

        return ResponseEntity.noContent().build();
    }
}
