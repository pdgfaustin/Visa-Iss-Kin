package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Nationalite;
import com.visa_iss_kin.service.NationaliteService;
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
@RequestMapping("/visa-iss/nationalite")
@CrossOrigin
public class NationaliteController {
    private final NationaliteService natService;

    public NationaliteController(NationaliteService natService) {
        this.natService = natService;
    }

    @PostMapping
    public ResponseEntity<Nationalite> creerNationalite(@RequestBody Nationalite nationalite) {
        Nationalite nouvelleNationalite = natService.creerNationalite(nationalite);
        return new ResponseEntity<>(nouvelleNationalite, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Nationalite>> listerNationalites() {
        return ResponseEntity.ok(natService.listeDesNationalites());
    }

    @GetMapping("/{idNation}")
    public ResponseEntity<Nationalite> rechercherNationaliteParId(@PathVariable String idNation) {
        return ResponseEntity.ok(natService.rechercherNationaliteParId(idNation));
    }

    @PutMapping("/{idNation}")
    public ResponseEntity<Nationalite> modifierNationalite(
            @PathVariable String idNation,
            @RequestBody Nationalite nationalite
    ) {
        return ResponseEntity.ok(natService.modifierNationalite(idNation, nationalite));
    }

    @DeleteMapping("/{idNation}")
    public ResponseEntity<Void> supprimerNationalite(@PathVariable String idNation) {
        natService.supprimerNationalite(idNation);
        return ResponseEntity.noContent().build();
    }
}
