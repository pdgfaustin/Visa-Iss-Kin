package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SyntheseSemestreService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/syntheses-semestres")
@CrossOrigin("*")
public class SyntheseSemestreController {
    private final SyntheseSemestreService syntheseSemestreService;

    public SyntheseSemestreController(
            SyntheseSemestreService syntheseSemestreService
    ) {
        this.syntheseSemestreService = syntheseSemestreService;
    }

    @PostMapping("/generer")
    public ResponseEntity<SyntheseSemestre> genererSyntheseSemestre(
            @RequestParam String idInscription,
            @RequestParam Long idSessionEvaluation,
            @RequestParam(required = false) String createdBy
    ) {
        return ResponseEntity.ok(
                syntheseSemestreService.genererSyntheseSemestre(
                        idInscription,
                        idSessionEvaluation,
                        createdBy
                )
        );
    }

    @PostMapping("/generer/session-promotion")
    public ResponseEntity<List<SyntheseSemestre>>
            genererSynthesesSemestreParSessionEtPromotion(
                    @RequestParam Long idSessionEvaluation,
                    @RequestParam String idPromo,
                    @RequestParam(required = false) String createdBy
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .genererSynthesesSemestreParSessionEtPromotion(
                                idSessionEvaluation,
                                idPromo,
                                createdBy
                        )
        );
    }

    @GetMapping
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestre() {

        return ResponseEntity.ok(
                syntheseSemestreService.listerSynthesesSemestre()
        );
    }

    @GetMapping("/{idSyntheseSemestre}")
    public ResponseEntity<SyntheseSemestre>
            rechercherSyntheseSemestreParId(
                    @PathVariable Long idSyntheseSemestre
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .rechercherSyntheseSemestreParId(
                                idSyntheseSemestre
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}/session/{idSessionEvaluation}")
    public ResponseEntity<SyntheseSemestre>
            rechercherSyntheseSemestreParInscriptionEtSession(
                    @PathVariable String idInscription,
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .rechercherSyntheseSemestreParInscriptionEtSession(
                                idInscription,
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParSession(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParSession(
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/statut/{statutValidationSemestre}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParStatutValidation(
                    @PathVariable
                    StatutValidationSemestre statutValidationSemestre
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParStatutValidation(
                                statutValidationSemestre
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParSessionEtPromotion(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParSessionEtPromotion(
                                idSessionEvaluation,
                                idPromo
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/statut/{statutValidationSemestre}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParSessionEtStatut(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable
                    StatutValidationSemestre statutValidationSemestre
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParSessionEtStatut(
                                idSessionEvaluation,
                                statutValidationSemestre
                        )
        );
    }

    @GetMapping("/annee-academique-inscription/{idAa}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParAnneeAcademiqueInscription(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParAnneeAcademiqueInscription(
                                idAa
                        )
        );
    }

    @GetMapping("/annee-academique-session/{idAa}")
    public ResponseEntity<List<SyntheseSemestre>>
            listerSynthesesSemestreParAnneeAcademiqueSession(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                syntheseSemestreService
                        .listerSynthesesSemestreParAnneeAcademiqueSession(
                                idAa
                        )
        );
    }

    @DeleteMapping("/{idSyntheseSemestre}")
    public ResponseEntity<Void> supprimerSyntheseSemestre(
            @PathVariable Long idSyntheseSemestre
    ) {
        syntheseSemestreService.supprimerSyntheseSemestre(
                idSyntheseSemestre
        );

        return ResponseEntity.noContent().build();
    }
}
