
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.UniteEnseignement;
import com.visa_iss_kin.service.UniteEnseignementService;
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
@RequestMapping("/visa-iss/unites-enseignement")
@CrossOrigin("*")
public class UniteEnseignementController {
    private final UniteEnseignementService uniteEnseignementService;

    public UniteEnseignementController(
            UniteEnseignementService uniteEnseignementService
    ) {
        this.uniteEnseignementService = uniteEnseignementService;
    }

    @PostMapping
    public ResponseEntity<UniteEnseignement> creerUniteEnseignement(
            @RequestBody UniteEnseignement uniteEnseignement
    ) {
        UniteEnseignement nouvelleUE =
                uniteEnseignementService.creerUniteEnseignement(uniteEnseignement);

        return new ResponseEntity<>(nouvelleUE, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UniteEnseignement>> listerUnitesEnseignement() {
        return ResponseEntity.ok(
                uniteEnseignementService.listerUnitesEnseignement()
        );
    }

    @GetMapping("/{idUE}")
    public ResponseEntity<UniteEnseignement> rechercherUniteEnseignementParId(
            @PathVariable String idUE
    ) {
        return ResponseEntity.ok(
                uniteEnseignementService.rechercherUniteEnseignementParId(idUE)
        );
    }

    @GetMapping("/code/{codeUE}")
    public ResponseEntity<UniteEnseignement> rechercherUniteEnseignementParCode(
            @PathVariable String codeUE
    ) {
        return ResponseEntity.ok(
                uniteEnseignementService.rechercherUniteEnseignementParCode(codeUE)
        );
    }

    @GetMapping("/recherche/libelle/{libeUE}")
    public ResponseEntity<List<UniteEnseignement>> rechercherUnitesEnseignementParLibelle(
            @PathVariable String libeUE
    ) {
        return ResponseEntity.ok(
                uniteEnseignementService.rechercherUnitesEnseignementParLibelle(libeUE)
        );
    }

    @GetMapping("/recherche/code/{codeUE}")
    public ResponseEntity<List<UniteEnseignement>> rechercherUnitesEnseignementParCode(
            @PathVariable String codeUE
    ) {
        return ResponseEntity.ok(
                uniteEnseignementService.rechercherUnitesEnseignementParCode(codeUE)
        );
    }

    @PutMapping("/{idUE}")
    public ResponseEntity<UniteEnseignement> modifierUniteEnseignement(
            @PathVariable String idUE,
            @RequestBody UniteEnseignement uniteEnseignement
    ) {
        return ResponseEntity.ok(
                uniteEnseignementService.modifierUniteEnseignement(
                        idUE,
                        uniteEnseignement
                )
        );
    }

    @DeleteMapping("/{idUE}")
    public ResponseEntity<Void> supprimerUniteEnseignement(
            @PathVariable String idUE
    ) {
        uniteEnseignementService.supprimerUniteEnseignement(idUE);
        return ResponseEntity.noContent().build();
    }
}
