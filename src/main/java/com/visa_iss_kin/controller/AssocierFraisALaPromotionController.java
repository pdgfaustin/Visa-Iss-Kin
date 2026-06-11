
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.AssocierFraisALaPromotion;
import com.visa_iss_kin.service.AssocierFraisALaPromotionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/associations-frais-promotions")
@CrossOrigin("*")
public class AssocierFraisALaPromotionController {

    private final AssocierFraisALaPromotionService associerFraisALaPromotionService;

    public AssocierFraisALaPromotionController(
            AssocierFraisALaPromotionService associerFraisALaPromotionService
    ) {
        this.associerFraisALaPromotionService = associerFraisALaPromotionService;
    }

    @PostMapping
    public ResponseEntity<AssocierFraisALaPromotion> creerAssociationFrais(
            @RequestBody AssocierFraisALaPromotion associationFrais
    ) {
        AssocierFraisALaPromotion nouvelleAssociation =
                associerFraisALaPromotionService.creerAssociationFrais(associationFrais);

        return new ResponseEntity<>(nouvelleAssociation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AssocierFraisALaPromotion>> listerAssociationsFrais() {
        return ResponseEntity.ok(
                associerFraisALaPromotionService.listerAssociationsFrais()
        );
    }

    @GetMapping("/{idAssociationFrais}")
    public ResponseEntity<AssocierFraisALaPromotion> rechercherAssociationFraisParId(
            @PathVariable Long idAssociationFrais
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService
                        .rechercherAssociationFraisParId(idAssociationFrais)
        );
    }

    @GetMapping("/frais/{idFrais}")
    public ResponseEntity<List<AssocierFraisALaPromotion>> listerAssociationsParFrais(
            @PathVariable String idFrais
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService.listerAssociationsParFrais(idFrais)
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<AssocierFraisALaPromotion>> listerAssociationsParPromotion(
            @PathVariable String idPromo
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService.listerAssociationsParPromotion(idPromo)
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<AssocierFraisALaPromotion>> listerAssociationsParAnneeAcademique(
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService
                        .listerAssociationsParAnneeAcademique(idAa)
        );
    }

    @GetMapping("/promotion/{idPromo}/annee-academique/{idAa}")
    public ResponseEntity<List<AssocierFraisALaPromotion>> listerAssociationsParPromotionEtAnnee(
            @PathVariable String idPromo,
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService
                        .listerAssociationsParPromotionEtAnnee(idPromo, idAa)
        );
    }

    @PutMapping("/{idAssociationFrais}")
    public ResponseEntity<AssocierFraisALaPromotion> modifierAssociationFrais(
            @PathVariable Long idAssociationFrais,
            @RequestBody AssocierFraisALaPromotion associationFrais
    ) {
        return ResponseEntity.ok(
                associerFraisALaPromotionService
                        .modifierAssociationFrais(idAssociationFrais, associationFrais)
        );
    }

    @DeleteMapping("/{idAssociationFrais}")
    public ResponseEntity<Void> supprimerAssociationFrais(
            @PathVariable Long idAssociationFrais
    ) {
        associerFraisALaPromotionService.supprimerAssociationFrais(idAssociationFrais);
        return ResponseEntity.noContent().build();
    }
}
