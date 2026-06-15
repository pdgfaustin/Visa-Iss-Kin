
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.ChargeHoraire;
import com.visa_iss_kin.model.TypeCharge;
import com.visa_iss_kin.service.ChargeHoraireService;
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
@RequestMapping("/visa-iss/charges-horaires")
@CrossOrigin("*")
public class ChargeHoraireController {
    private final ChargeHoraireService chargeHoraireService;

    public ChargeHoraireController(
            ChargeHoraireService chargeHoraireService
    ) {
        this.chargeHoraireService = chargeHoraireService;
    }

    @PostMapping
    public ResponseEntity<ChargeHoraire> creerChargeHoraire(
            @RequestBody ChargeHoraire chargeHoraire
    ) {
        ChargeHoraire nouvelleCharge =
                chargeHoraireService.creerChargeHoraire(chargeHoraire);

        return new ResponseEntity<>(nouvelleCharge, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChargeHoraire>> listerChargesHoraires() {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesHoraires()
        );
    }

    @GetMapping("/{idChargeHoraire}")
    public ResponseEntity<ChargeHoraire> rechercherChargeHoraireParId(
            @PathVariable Long idChargeHoraire
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.rechercherChargeHoraireParId(idChargeHoraire)
        );
    }

    @GetMapping("/enseignant/{matrEns}")
    public ResponseEntity<List<ChargeHoraire>> listerChargesParEnseignant(
            @PathVariable String matrEns
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesParEnseignant(matrEns)
        );
    }

    @GetMapping("/maquette/{idMaquette}")
    public ResponseEntity<List<ChargeHoraire>> listerChargesParMaquette(
            @PathVariable Long idMaquette
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesParMaquette(idMaquette)
        );
    }

    @GetMapping("/type/{typeCharge}")
    public ResponseEntity<List<ChargeHoraire>> listerChargesParType(
            @PathVariable TypeCharge typeCharge
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesParType(typeCharge)
        );
    }

    @GetMapping("/enseignant/{matrEns}/type/{typeCharge}")
    public ResponseEntity<List<ChargeHoraire>> listerChargesParEnseignantEtType(
            @PathVariable String matrEns,
            @PathVariable TypeCharge typeCharge
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesParEnseignantEtType(
                        matrEns,
                        typeCharge
                )
        );
    }

    @GetMapping("/maquette/{idMaquette}/type/{typeCharge}")
    public ResponseEntity<List<ChargeHoraire>> listerChargesParMaquetteEtType(
            @PathVariable Long idMaquette,
            @PathVariable TypeCharge typeCharge
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.listerChargesParMaquetteEtType(
                        idMaquette,
                        typeCharge
                )
        );
    }

    @PutMapping("/{idChargeHoraire}")
    public ResponseEntity<ChargeHoraire> modifierChargeHoraire(
            @PathVariable Long idChargeHoraire,
            @RequestBody ChargeHoraire chargeHoraire
    ) {
        return ResponseEntity.ok(
                chargeHoraireService.modifierChargeHoraire(
                        idChargeHoraire,
                        chargeHoraire
                )
        );
    }

    @DeleteMapping("/{idChargeHoraire}")
    public ResponseEntity<Void> supprimerChargeHoraire(
            @PathVariable Long idChargeHoraire
    ) {
        chargeHoraireService.supprimerChargeHoraire(idChargeHoraire);
        return ResponseEntity.noContent().build();
    }
}
