package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.*;
import com.visa_iss_kin.service.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/epreuves")
@CrossOrigin("*")
public class EpreuveController {
    private final EpreuveService epreuveService;

    public EpreuveController(EpreuveService epreuveService) {
        this.epreuveService = epreuveService;
    }

    @PostMapping
    public ResponseEntity<Epreuve> creerEpreuve(
            @RequestBody Epreuve epreuve
    ) {
        Epreuve nouvelleEpreuve =
                epreuveService.creerEpreuve(epreuve);

        return new ResponseEntity<>(
                nouvelleEpreuve,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Epreuve>> listerEpreuves() {
        return ResponseEntity.ok(
                epreuveService.listerEpreuves()
        );
    }

    @GetMapping("/{idEpreuve}")
    public ResponseEntity<Epreuve> rechercherEpreuveParId(
            @PathVariable Long idEpreuve
    ) {
        return ResponseEntity.ok(
                epreuveService.rechercherEpreuveParId(idEpreuve)
        );
    }

    @GetMapping("/session/{idSessionEvaluation}")
    public ResponseEntity<List<Epreuve>> listerEpreuvesParSession(
            @PathVariable Long idSessionEvaluation
    ) {
        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParSession(
                        idSessionEvaluation
                )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<Epreuve>> listerEpreuvesParPromotion(
            @PathVariable String idPromo
    ) {
        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParPromotion(idPromo)
        );
    }

    @GetMapping("/element-constitutif/{idEC}")
    public ResponseEntity<List<Epreuve>>
            listerEpreuvesParElementConstitutif(
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                epreuveService
                        .listerEpreuvesParElementConstitutif(idEC)
        );
    }

    @GetMapping("/type/{typeEpreuve}")
    public ResponseEntity<List<Epreuve>> listerEpreuvesParType(
            @PathVariable TypeEpreuve typeEpreuve
    ) {
        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParType(typeEpreuve)
        );
    }

    @GetMapping("/ouvertes")
    public ResponseEntity<List<Epreuve>> listerEpreuvesOuvertes() {
        return ResponseEntity.ok(
                epreuveService.listerEpreuvesOuvertes()
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/promotion/{idPromo}")
    public ResponseEntity<List<Epreuve>>
            listerEpreuvesParSessionEtPromotion(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParSessionEtPromotion(
                        idSessionEvaluation,
                        idPromo
                )
        );
    }

    @GetMapping(
            "/session/{idSessionEvaluation}/promotion/{idPromo}/element-constitutif/{idEC}"
    )
    public ResponseEntity<List<Epreuve>>
            listerEpreuvesParSessionPromotionEtEC(
                    @PathVariable Long idSessionEvaluation,
                    @PathVariable String idPromo,
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParSessionPromotionEtEC(
                        idSessionEvaluation,
                        idPromo,
                        idEC
                )
        );
    }

    @GetMapping("/promotion/{idPromo}/element-constitutif/{idEC}")
    public ResponseEntity<List<Epreuve>>
            listerEpreuvesParPromotionEtEC(
                    @PathVariable String idPromo,
                    @PathVariable String idEC
            ) {

        return ResponseEntity.ok(
                epreuveService.listerEpreuvesParPromotionEtEC(
                        idPromo,
                        idEC
                )
        );
    }

    @GetMapping("/session/{idSessionEvaluation}/ouvertes")
    public ResponseEntity<List<Epreuve>>
            listerEpreuvesOuvertesParSession(
                    @PathVariable Long idSessionEvaluation
            ) {

        return ResponseEntity.ok(
                epreuveService.listerEpreuvesOuvertesParSession(
                        idSessionEvaluation
                )
        );
    }

    @PutMapping("/{idEpreuve}")
    public ResponseEntity<Epreuve> modifierEpreuve(
            @PathVariable Long idEpreuve,
            @RequestBody Epreuve epreuve
    ) {
        return ResponseEntity.ok(
                epreuveService.modifierEpreuve(idEpreuve, epreuve)
        );
    }

    @PatchMapping("/{idEpreuve}/ouvrir")
    public ResponseEntity<Epreuve> ouvrirEpreuve(
            @PathVariable Long idEpreuve
    ) {
        return ResponseEntity.ok(
                epreuveService.ouvrirEpreuve(idEpreuve)
        );
    }

    @PatchMapping("/{idEpreuve}/fermer")
    public ResponseEntity<Epreuve> fermerEpreuve(
            @PathVariable Long idEpreuve
    ) {
        return ResponseEntity.ok(
                epreuveService.fermerEpreuve(idEpreuve)
        );
    }

    @DeleteMapping("/{idEpreuve}")
    public ResponseEntity<Void> supprimerEpreuve(
            @PathVariable Long idEpreuve
    ) {
        epreuveService.supprimerEpreuve(idEpreuve);
        return ResponseEntity.noContent().build();
    }
}
