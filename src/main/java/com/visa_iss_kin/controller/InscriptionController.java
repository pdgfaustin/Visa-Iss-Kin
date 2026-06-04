
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Inscription;
import com.visa_iss_kin.service.InscriptionService;
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
@RequestMapping("/visa-iss/inscriptions")
@CrossOrigin("*")
public class InscriptionController {
    private final InscriptionService inscriptionService;

    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @PostMapping
    public ResponseEntity<Inscription> creerInscription(@RequestBody Inscription inscription) {
        Inscription nouvelleInscription = inscriptionService.creerInscription(inscription);
        return new ResponseEntity<>(nouvelleInscription, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Inscription>> listerInscriptions() {
        return ResponseEntity.ok(inscriptionService.listerInscriptions());
    }

    @GetMapping("/{idInscription}")
    public ResponseEntity<Inscription> rechercherInscriptionParId(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(inscriptionService.rechercherInscriptionParId(idInscription));
    }

    @GetMapping("/etudiant/{idEtudiant}")
    public ResponseEntity<List<Inscription>> listerInscriptionsParEtudiant(
            @PathVariable String idEtudiant
    ) {
        return ResponseEntity.ok(inscriptionService.listerInscriptionsParEtudiant(idEtudiant));
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<Inscription>> listerInscriptionsParPromotion(
            @PathVariable String idPromo
    ) {
        return ResponseEntity.ok(inscriptionService.listerInscriptionsParPromotion(idPromo));
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<List<Inscription>> listerInscriptionsParAnneeAcademique(
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(inscriptionService.listerInscriptionsParAnneeAcademique(idAa));
    }

    @GetMapping("/promotion/{idPromo}/annee-academique/{idAa}")
    public ResponseEntity<List<Inscription>> listerInscriptionsParPromotionEtAnnee(
            @PathVariable String idPromo,
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(
                inscriptionService.listerInscriptionsParPromotionEtAnnee(idPromo, idAa)
        );
    }

    @PutMapping("/{idInscription}")
    public ResponseEntity<Inscription> modifierInscription(
            @PathVariable String idInscription,
            @RequestBody Inscription inscription
    ) {
        return ResponseEntity.ok(
                inscriptionService.modifierInscription(idInscription, inscription)
        );
    }

    @PutMapping("/{idInscription}/valider")
    public ResponseEntity<Inscription> validerInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(inscriptionService.validerInscription(idInscription));
    }

    @PutMapping("/{idInscription}/annuler")
    public ResponseEntity<Inscription> annulerInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(inscriptionService.annulerInscription(idInscription));
    }

    @DeleteMapping("/{idInscription}")
    public ResponseEntity<Void> supprimerInscription(@PathVariable String idInscription) {
        inscriptionService.supprimerInscription(idInscription);
        return ResponseEntity.noContent().build();
    }
}
