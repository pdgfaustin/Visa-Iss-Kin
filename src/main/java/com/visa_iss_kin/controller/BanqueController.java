
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Banque;
import com.visa_iss_kin.service.BanqueService;
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
@RequestMapping("/visa-iss/banques")
@CrossOrigin("*")
public class BanqueController {

    private final BanqueService banqueService;

    public BanqueController(BanqueService banqueService) {
        this.banqueService = banqueService;
    }

    @PostMapping
    public ResponseEntity<Banque> creerBanque(@RequestBody Banque banque) {
        Banque nouvelleBanque = banqueService.creerBanque(banque);
        return new ResponseEntity<>(nouvelleBanque, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Banque>> listerBanques() {
        return ResponseEntity.ok(banqueService.listerBanques());
    }

    @GetMapping("/{idBanque}")
    public ResponseEntity<Banque> rechercherBanqueParId(
            @PathVariable String idBanque
    ) {
        return ResponseEntity.ok(banqueService.rechercherBanqueParId(idBanque));
    }

    @GetMapping("/recherche/libelle/{libeBanque}")
    public ResponseEntity<List<Banque>> rechercherBanquesParLibelle(
            @PathVariable String libeBanque
    ) {
        return ResponseEntity.ok(banqueService.rechercherBanquesParLibelle(libeBanque));
    }

    @GetMapping("/recherche/sigle/{sigleBanque}")
    public ResponseEntity<List<Banque>> rechercherBanquesParSigle(
            @PathVariable String sigleBanque
    ) {
        return ResponseEntity.ok(banqueService.rechercherBanquesParSigle(sigleBanque));
    }

    @PutMapping("/{idBanque}")
    public ResponseEntity<Banque> modifierBanque(
            @PathVariable String idBanque,
            @RequestBody Banque banque
    ) {
        return ResponseEntity.ok(banqueService.modifierBanque(idBanque, banque));
    }

    @DeleteMapping("/{idBanque}")
    public ResponseEntity<Void> supprimerBanque(@PathVariable String idBanque) {
        banqueService.supprimerBanque(idBanque);
        return ResponseEntity.noContent().build();
    }
}