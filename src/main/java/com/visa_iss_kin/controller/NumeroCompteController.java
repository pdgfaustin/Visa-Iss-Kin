
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.NumeroCompte;
import com.visa_iss_kin.model.StatutCompte;
import com.visa_iss_kin.service.NumeroCompteService;
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
@RequestMapping("/visa-iss/numeros-banque")
@CrossOrigin("*")
public class NumeroCompteController {
    private final NumeroCompteService numeroBanqueService;

    public NumeroCompteController(NumeroCompteService numeroBanqueService) {
        this.numeroBanqueService = numeroBanqueService;
    }

    @PostMapping
    public ResponseEntity<NumeroCompte> creerNumeroBanque(
            @RequestBody NumeroCompte numeroBanque
    ) {
        NumeroCompte nouveauNumeroBanque =
                numeroBanqueService.creerNumeroCompte(numeroBanque);

        return new ResponseEntity<>(nouveauNumeroBanque, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NumeroCompte>> listerNumerosBanque() {
        return ResponseEntity.ok(numeroBanqueService.listerNumerosCompte());
    }

    @GetMapping("/{idNumeroBanque}")
    public ResponseEntity<NumeroCompte> rechercherNumeroBanqueParId(
            @PathVariable String idNumeroBanque
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.rechercherNumeroCompteParId(idNumeroBanque)
        );
    }

    @GetMapping("/numero-compte/{numeroCompte}")
    public ResponseEntity<NumeroCompte> rechercherNumeroBanqueParNumeroCompte(
            @PathVariable String numeroCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.rechercherNumeroCompteParNumeroCompte(numeroCompte)
        );
    }

    @GetMapping("/banque/{idBanque}")
    public ResponseEntity<List<NumeroCompte>> listerNumerosParBanque(
            @PathVariable String idBanque
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.listerNumerosParBanque(idBanque)
        );
    }

    @GetMapping("/devise/{deviseCompte}")
    public ResponseEntity<List<NumeroCompte>> listerNumerosParDevise(
            @PathVariable Devise deviseCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.listerNumerosParDevise(deviseCompte)
        );
    }

    @GetMapping("/statut/{statutCompte}")
    public ResponseEntity<List<NumeroCompte>> listerNumerosParStatut(
            @PathVariable StatutCompte statutCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.listerNumerosParStatut(statutCompte)
        );
    }

    @GetMapping("/banque/{idBanque}/devise/{deviseCompte}")
    public ResponseEntity<List<NumeroCompte>> listerNumerosParBanqueEtDevise(
            @PathVariable String idBanque,
            @PathVariable Devise deviseCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.listerNumerosParBanqueEtDevise(
                        idBanque,
                        deviseCompte
                )
        );
    }

    @GetMapping("/banque/{idBanque}/statut/{statutCompte}")
    public ResponseEntity<List<NumeroCompte>> listerNumerosParBanqueEtStatut(
            @PathVariable String idBanque,
            @PathVariable StatutCompte statutCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.listerNumerosParBanqueEtStatut(
                        idBanque,
                        statutCompte
                )
        );
    }

    @GetMapping("/recherche/intitule/{intituleCompte}")
    public ResponseEntity<List<NumeroCompte>> rechercherNumerosParIntitule(
            @PathVariable String intituleCompte
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.rechercherNumerosParIntitule(intituleCompte)
        );
    }

    @PutMapping("/{idNumeroBanque}")
    public ResponseEntity<NumeroCompte> modifierNumeroBanque(
            @PathVariable String idNumeroBanque,
            @RequestBody NumeroCompte numeroBanque
    ) {
        return ResponseEntity.ok(
                numeroBanqueService.modifierNumeroCompte(idNumeroBanque, numeroBanque)
        );
    }

    @DeleteMapping("/{idNumeroBanque}")
    public ResponseEntity<Void> supprimerNumeroBanque(
            @PathVariable String idNumeroBanque
    ) {
        numeroBanqueService.supprimerNumeroCompte(idNumeroBanque);
        return ResponseEntity.noContent().build();
    }
}
