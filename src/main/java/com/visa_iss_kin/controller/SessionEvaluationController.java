package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.SessionEvaluationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/sessions-evaluations")
@CrossOrigin("*")
public class SessionEvaluationController {
    private final SessionEvaluationService sessionEvaluationService;

    public SessionEvaluationController(
            SessionEvaluationService sessionEvaluationService
    ) {
        this.sessionEvaluationService = sessionEvaluationService;
    }

    @PostMapping
    public ResponseEntity<SessionEvaluation> creerSessionEvaluation(
            @RequestBody SessionEvaluation sessionEvaluation
    ) {
        SessionEvaluation nouvelleSession =
                sessionEvaluationService.creerSessionEvaluation(
                        sessionEvaluation
                );

        return new ResponseEntity<>(
                nouvelleSession,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsEvaluations() {

        return ResponseEntity.ok(
                sessionEvaluationService.listerSessionsEvaluations()
        );
    }

    @GetMapping("/{idSessionEvaluation}")
    public ResponseEntity<SessionEvaluation>
            rechercherSessionEvaluationParId(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .rechercherSessionEvaluationParId(
                                idSessionEvaluation
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsParAnneeAcademique(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .listerSessionsParAnneeAcademique(idAa)
        );
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsParSemestre(
                    @PathVariable Semestre semestre
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .listerSessionsParSemestre(semestre)
        );
    }

    @GetMapping("/type-session/{typeSession}")
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsParTypeSession(
                    @PathVariable TypeSession typeSession
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .listerSessionsParTypeSession(typeSession)
        );
    }

    @GetMapping("/ouvertes")
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsOuvertes() {

        return ResponseEntity.ok(
                sessionEvaluationService.listerSessionsOuvertes()
        );
    }

    @GetMapping("/annee-academique/{idAa}/ouvertes")
    public ResponseEntity<List<SessionEvaluation>>
            listerSessionsOuvertesParAnneeAcademique(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .listerSessionsOuvertesParAnneeAcademique(
                                idAa
                        )
        );
    }

    @GetMapping(
            "/annee-academique/{idAa}/semestre/{semestre}/type-session/{typeSession}"
    )
    public ResponseEntity<SessionEvaluation>
            rechercherSessionParAnneeSemestreEtType(
                    @PathVariable String idAa,
                    @PathVariable Semestre semestre,
                    @PathVariable TypeSession typeSession
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .rechercherSessionParAnneeSemestreEtType(
                                idAa,
                                semestre,
                                typeSession
                        )
        );
    }

    @PutMapping("/{idSessionEvaluation}")
    public ResponseEntity<SessionEvaluation>
            modifierSessionEvaluation(
                    @PathVariable Long idSessionEvaluation,
                    @RequestBody SessionEvaluation sessionEvaluation
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .modifierSessionEvaluation(
                                idSessionEvaluation,
                                sessionEvaluation
                        )
        );
    }

    @PatchMapping("/{idSessionEvaluation}/ouvrir")
    public ResponseEntity<SessionEvaluation>
            ouvrirSessionEvaluation(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .ouvrirSessionEvaluation(
                                idSessionEvaluation
                        )
        );
    }

    @PatchMapping("/{idSessionEvaluation}/fermer")
    public ResponseEntity<SessionEvaluation>
            fermerSessionEvaluation(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                sessionEvaluationService
                        .fermerSessionEvaluation(
                                idSessionEvaluation
                        )
        );
    }

    @DeleteMapping("/{idSessionEvaluation}")
    public ResponseEntity<Void> supprimerSessionEvaluation(
            @PathVariable Long idSessionEvaluation
    ) {
        sessionEvaluationService.supprimerSessionEvaluation(
                idSessionEvaluation
        );

        return ResponseEntity.noContent().build();
    }
}
