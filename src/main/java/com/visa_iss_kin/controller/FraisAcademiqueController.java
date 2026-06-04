
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.FraisAcademique;
import com.visa_iss_kin.service.FraisAcademiqueService;
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
@RequestMapping("/visa-iss/frais-academiques")
@CrossOrigin("*")
public class FraisAcademiqueController {

    private final FraisAcademiqueService fraisAcademiqueService;

    public FraisAcademiqueController(FraisAcademiqueService fraisAcademiqueService) {
        this.fraisAcademiqueService = fraisAcademiqueService;
    }

    @PostMapping
    public ResponseEntity<FraisAcademique> creerFraisAcademique(
            @RequestBody FraisAcademique fraisAcademique
    ) {
        FraisAcademique nouveauFrais =
                fraisAcademiqueService.creerFraisAcademique(fraisAcademique);

        return new ResponseEntity<>(nouveauFrais, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FraisAcademique>> listerFraisAcademiques() {
        return ResponseEntity.ok(fraisAcademiqueService.listerFraisAcademiques());
    }

    @GetMapping("/{idFrais}")
    public ResponseEntity<FraisAcademique> rechercherFraisAcademiqueParId(
            @PathVariable String idFrais
    ) {
        return ResponseEntity.ok(fraisAcademiqueService.rechercherFraisAcademiqueParId(idFrais));
    }

    @GetMapping("/recherche/{libeFrais}")
    public ResponseEntity<List<FraisAcademique>> rechercherFraisAcademiquesParLibelle(
            @PathVariable String libeFrais
    ) {
        return ResponseEntity.ok(
                fraisAcademiqueService.rechercherFraisAcademiquesParLibelle(libeFrais)
        );
    }

    @PutMapping("/{idFrais}")
    public ResponseEntity<FraisAcademique> modifierFraisAcademique(
            @PathVariable String idFrais,
            @RequestBody FraisAcademique fraisAcademique
    ) {
        return ResponseEntity.ok(
                fraisAcademiqueService.modifierFraisAcademique(idFrais, fraisAcademique)
        );
    }

    @DeleteMapping("/{idFrais}")
    public ResponseEntity<Void> supprimerFraisAcademique(@PathVariable String idFrais) {
        fraisAcademiqueService.supprimerFraisAcademique(idFrais);
        return ResponseEntity.noContent().build();
    }
}
