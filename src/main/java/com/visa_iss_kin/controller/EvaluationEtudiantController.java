package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.EvaluationEtudiantService;
import org.springframework.http.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/evaluations-etudiants")
@CrossOrigin("*")
public class EvaluationEtudiantController {
    private final EvaluationEtudiantService evaluationEtudiantService;

    public EvaluationEtudiantController(
            EvaluationEtudiantService evaluationEtudiantService
    ) {
        this.evaluationEtudiantService = evaluationEtudiantService;
    }

    @PostMapping
    public ResponseEntity<EvaluationEtudiant> creerEvaluationEtudiant(
            @RequestBody EvaluationEtudiant evaluationEtudiant
    ) {
        EvaluationEtudiant nouvelleEvaluation =
                evaluationEtudiantService.creerEvaluationEtudiant(
                        evaluationEtudiant
                );

        return new ResponseEntity<>(
                nouvelleEvaluation,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsEtudiants() {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsEtudiants()
        );
    }

    @GetMapping("/{idEvaluationEtudiant}")
    public ResponseEntity<EvaluationEtudiant>
            rechercherEvaluationEtudiantParId(
                    @PathVariable Long idEvaluationEtudiant
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .rechercherEvaluationEtudiantParId(
                                idEvaluationEtudiant
                        )
        );
    }

    @GetMapping("/epreuve/{idEpreuve}/inscription/{idInscription}")
    public ResponseEntity<EvaluationEtudiant>
            rechercherEvaluationParEpreuveEtInscription(
                    @PathVariable Long idEpreuve,
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .rechercherEvaluationParEpreuveEtInscription(
                                idEpreuve,
                                idInscription
                        )
        );
    }

    @GetMapping("/epreuve/{idEpreuve}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParEpreuve(
                    @PathVariable Long idEpreuve
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParEpreuve(idEpreuve)
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParInscription(
                    @PathVariable String idInscription
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParInscription(
                                idInscription
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParPromotion(idPromo)
        );
    }

    @GetMapping("/element-constitutif/{idEC}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParElementConstitutif(
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParElementConstitutif(idEC)
        );
    }

    @GetMapping("/session/{idSessionEvaluation}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParSession(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParSession(
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParSessionEtPromotion(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParSessionEtPromotion(
                                idSessionEvaluation,
                                idPromo
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}/element-constitutif/{idEC}")
    public ResponseEntity<List<EvaluationEtudiant>>
            listerEvaluationsParPromotionEtEC(
                    @PathVariable String idPromo,
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .listerEvaluationsParPromotionEtEC(
                                idPromo,
                                idEC
                        )
        );
    }

    @PutMapping("/{idEvaluationEtudiant}")
    public ResponseEntity<EvaluationEtudiant>
            modifierEvaluationEtudiant(
                    @PathVariable Long idEvaluationEtudiant,
                    @RequestBody EvaluationEtudiant evaluationEtudiant
            ) {

        return ResponseEntity.ok(
                evaluationEtudiantService
                        .modifierEvaluationEtudiant(
                                idEvaluationEtudiant,
                                evaluationEtudiant
                        )
        );
    }

    @DeleteMapping("/{idEvaluationEtudiant}")
    public ResponseEntity<Void> supprimerEvaluationEtudiant(
            @PathVariable Long idEvaluationEtudiant
    ) {
        evaluationEtudiantService.supprimerEvaluationEtudiant(
                idEvaluationEtudiant
        );

        return ResponseEntity.noContent().build();
    }
}
