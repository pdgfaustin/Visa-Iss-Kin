
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.PromotionEnseignantService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/promotions-enseignants")
@CrossOrigin("*")
public class PromotionEnseignantController {
    private final PromotionEnseignantService promotionEnseignantService;

    public PromotionEnseignantController(
            PromotionEnseignantService promotionEnseignantService
    ) {
        this.promotionEnseignantService = promotionEnseignantService;
    }

    @PostMapping
    public ResponseEntity<PromotionEnseignant> creerPromotionEnseignant(
            @RequestBody PromotionEnseignant promotionEnseignant
    ) {
        PromotionEnseignant nouvellePromotion =
                promotionEnseignantService.creerPromotionEnseignant(
                        promotionEnseignant
                );

        return new ResponseEntity<>(
                nouvellePromotion,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<PromotionEnseignant>>
            listerPromotionsEnseignants() {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .listerPromotionsEnseignants()
        );
    }

    @GetMapping("/{idPromotionEnseignant}")
    public ResponseEntity<PromotionEnseignant>
            rechercherPromotionEnseignantParId(
                    @PathVariable Long idPromotionEnseignant
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .rechercherPromotionEnseignantParId(
                                idPromotionEnseignant
                        )
        );
    }

    @GetMapping("/enseignant/{matrEns}")
    public ResponseEntity<List<PromotionEnseignant>>
            listerHistoriqueParEnseignant(
                    @PathVariable String matrEns
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .listerHistoriqueParEnseignant(matrEns)
        );
    }

    @GetMapping("/grade/{nouveauGrade}")
    public ResponseEntity<List<PromotionEnseignant>>
            listerPromotionsParGrade(
                    @PathVariable GradeEnseignant nouveauGrade
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .listerPromotionsParGrade(nouveauGrade)
        );
    }

    @GetMapping("/statut/{statutPromotion}")
    public ResponseEntity<List<PromotionEnseignant>>
            listerPromotionsParStatut(
                    @PathVariable
                    StatutPromotionEnseignant statutPromotion
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .listerPromotionsParStatut(statutPromotion)
        );
    }

    @GetMapping(
            "/enseignant/{matrEns}/statut/{statutPromotion}"
    )
    public ResponseEntity<List<PromotionEnseignant>>
            listerPromotionsParEnseignantEtStatut(
                    @PathVariable String matrEns,
                    @PathVariable
                    StatutPromotionEnseignant statutPromotion
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .listerPromotionsParEnseignantEtStatut(
                                matrEns,
                                statutPromotion
                        )
        );
    }

    @GetMapping("/enseignant/{matrEns}/grade-actuel")
    public ResponseEntity<PromotionEnseignant>
            rechercherGradeActuelEnseignant(
                    @PathVariable String matrEns
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .rechercherGradeActuelEnseignant(matrEns)
        );
    }

    @PutMapping("/{idPromotionEnseignant}")
    public ResponseEntity<PromotionEnseignant>
            modifierPromotionEnseignant(
                    @PathVariable Long idPromotionEnseignant,
                    @RequestBody PromotionEnseignant promotionEnseignant
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .modifierPromotionEnseignant(
                                idPromotionEnseignant,
                                promotionEnseignant
                        )
        );
    }

    @PatchMapping(
            "/{idPromotionEnseignant}/statut/{statutPromotion}"
    )
    public ResponseEntity<PromotionEnseignant>
            changerStatutPromotion(
                    @PathVariable Long idPromotionEnseignant,
                    @PathVariable
                    StatutPromotionEnseignant statutPromotion
            ) {

        return ResponseEntity.ok(
                promotionEnseignantService
                        .changerStatutPromotion(
                                idPromotionEnseignant,
                                statutPromotion
                        )
        );
    }

    @DeleteMapping("/{idPromotionEnseignant}")
    public ResponseEntity<Void> supprimerPromotionEnseignant(
            @PathVariable Long idPromotionEnseignant
    ) {
        promotionEnseignantService
                .supprimerPromotionEnseignant(
                        idPromotionEnseignant
                );

        return ResponseEntity.noContent().build();
    }
}
