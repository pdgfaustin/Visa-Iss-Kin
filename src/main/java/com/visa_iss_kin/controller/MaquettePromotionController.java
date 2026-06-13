
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.MaquettePromotion;
import com.visa_iss_kin.model.Semestre;
import com.visa_iss_kin.service.MaquettePromotionService;
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
@RequestMapping("/visa-iss/maquettes-promotions")
@CrossOrigin("*")
public class MaquettePromotionController {
    private final MaquettePromotionService maquettePromotionService;

    public MaquettePromotionController(
            MaquettePromotionService maquettePromotionService
    ) {
        this.maquettePromotionService = maquettePromotionService;
    }

    @PostMapping
    public ResponseEntity<MaquettePromotion> creerMaquettePromotion(
            @RequestBody MaquettePromotion maquettePromotion
    ) {
        MaquettePromotion nouvelleMaquette =
                maquettePromotionService.creerMaquettePromotion(maquettePromotion);

        return new ResponseEntity<>(nouvelleMaquette, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesPromotion() {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesPromotion()
        );
    }

    @GetMapping("/{idMaquette}")
    public ResponseEntity<MaquettePromotion> rechercherMaquettePromotionParId(
            @PathVariable Long idMaquette
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.rechercherMaquettePromotionParId(idMaquette)
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesParPromotion(
            @PathVariable String idPromo
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesParPromotion(idPromo)
        );
    }

    @GetMapping("/element-constitutif/{idEC}")
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesParElementConstitutif(
            @PathVariable String idEC
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesParElementConstitutif(idEC)
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesParAnneeAcademique(
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesParAnneeAcademique(idAa)
        );
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesParSemestre(
            @PathVariable Semestre semestre
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesParSemestre(semestre)
        );
    }

    @GetMapping("/promotion/{idPromo}/annee-academique/{idAa}")
    public ResponseEntity<List<MaquettePromotion>> listerMaquettesParPromotionEtAnnee(
            @PathVariable String idPromo,
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.listerMaquettesParPromotionEtAnnee(
                        idPromo,
                        idAa
                )
        );
    }

    @GetMapping(
            "/promotion/{idPromo}/annee-academique/{idAa}/semestre/{semestre}"
    )
    public ResponseEntity<List<MaquettePromotion>>
            listerMaquettesParPromotionAnneeEtSemestre(
                    @PathVariable String idPromo,
                    @PathVariable String idAa,
                    @PathVariable Semestre semestre
            ) {
        return ResponseEntity.ok(
                maquettePromotionService
                        .listerMaquettesParPromotionAnneeEtSemestre(
                                idPromo,
                                idAa,
                                semestre
                        )
        );
    }

    @PutMapping("/{idMaquette}")
    public ResponseEntity<MaquettePromotion> modifierMaquettePromotion(
            @PathVariable Long idMaquette,
            @RequestBody MaquettePromotion maquettePromotion
    ) {
        return ResponseEntity.ok(
                maquettePromotionService.modifierMaquettePromotion(
                        idMaquette,
                        maquettePromotion
                )
        );
    }

    @DeleteMapping("/{idMaquette}")
    public ResponseEntity<Void> supprimerMaquettePromotion(
            @PathVariable Long idMaquette
    ) {
        maquettePromotionService.supprimerMaquettePromotion(idMaquette);
        return ResponseEntity.noContent().build();
    }
}
