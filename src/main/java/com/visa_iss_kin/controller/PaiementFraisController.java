
package com.visa_iss_kin.controller;

import com.visa_iss_kin.dto.ResumeFinancierInscriptionDto;
import com.visa_iss_kin.dto.SituationFinanciereEtudiantDto;
import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.PaiementFrais;
import com.visa_iss_kin.model.StatutPaiement;
import com.visa_iss_kin.service.PaiementFraisService;
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
@RequestMapping("/visa-iss/paiements-frais")
@CrossOrigin("*")
public class PaiementFraisController {
    private final PaiementFraisService paiementFraisService;

    public PaiementFraisController(PaiementFraisService paiementFraisService) {
        this.paiementFraisService = paiementFraisService;
    }

    @PostMapping
    public ResponseEntity<PaiementFrais> creerPaiementFrais(
            @RequestBody PaiementFrais paiementFrais
    ) {
        PaiementFrais nouveauPaiement =
                paiementFraisService.creerPaiementFrais(paiementFrais);

        return new ResponseEntity<>(nouveauPaiement, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PaiementFrais>> listerPaiementsFrais() {
        return ResponseEntity.ok(paiementFraisService.listerPaiementsFrais());
    }

    @GetMapping("/{idPaiementFrais}")
    public ResponseEntity<PaiementFrais> rechercherPaiementFraisParId(
            @PathVariable Long idPaiementFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.rechercherPaiementFraisParId(idPaiementFrais)
        );
    }

    @GetMapping("/reference/{referencePaiement}")
    public ResponseEntity<PaiementFrais> rechercherPaiementFraisParReference(
            @PathVariable String referencePaiement
    ) {
        return ResponseEntity.ok(
                paiementFraisService.rechercherPaiementFraisParReference(referencePaiement)
        );
    }

    @GetMapping("/inscription/{idInscription}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParInscription(idInscription)
        );
    }

    @GetMapping("/association-frais/{idAssociationFrais}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParAssociationFrais(
            @PathVariable Long idAssociationFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParAssociationFrais(idAssociationFrais)
        );
    }

    @GetMapping("/numero-banque/{idNumeroBanque}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParNumeroBanque(
            @PathVariable String idNumeroBanque
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParNumeroBanque(idNumeroBanque)
        );
    }

    @GetMapping("/statut/{statutPaiement}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParStatut(
            @PathVariable StatutPaiement statutPaiement
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParStatut(statutPaiement)
        );
    }

    @GetMapping("/devise/{devisePaiement}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParDevise(
            @PathVariable Devise devisePaiement
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParDevise(devisePaiement)
        );
    }

    @GetMapping("/inscription/{idInscription}/association-frais/{idAssociationFrais}")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsParInscriptionEtAssociationFrais(
            @PathVariable String idInscription,
            @PathVariable Long idAssociationFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsParInscriptionEtAssociationFrais(
                        idInscription,
                        idAssociationFrais
                )
        );
    }

    @GetMapping("/inscription/{idInscription}/valides")
    public ResponseEntity<List<PaiementFrais>> listerPaiementsValidesParInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(
                paiementFraisService.listerPaiementsValidesParInscription(idInscription)
        );
    }

    @PutMapping("/{idPaiementFrais}")
    public ResponseEntity<PaiementFrais> modifierPaiementFrais(
            @PathVariable Long idPaiementFrais,
            @RequestBody PaiementFrais paiementFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.modifierPaiementFrais(idPaiementFrais, paiementFrais)
        );
    }

    @PutMapping("/{idPaiementFrais}/valider")
    public ResponseEntity<PaiementFrais> validerPaiementFrais(
            @PathVariable Long idPaiementFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.validerPaiementFrais(idPaiementFrais)
        );
    }

    @PutMapping("/{idPaiementFrais}/rejeter")
    public ResponseEntity<PaiementFrais> rejeterPaiementFrais(
            @PathVariable Long idPaiementFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.rejeterPaiementFrais(idPaiementFrais)
        );
    }

    @PutMapping("/{idPaiementFrais}/annuler")
    public ResponseEntity<PaiementFrais> annulerPaiementFrais(
            @PathVariable Long idPaiementFrais
    ) {
        return ResponseEntity.ok(
                paiementFraisService.annulerPaiementFrais(idPaiementFrais)
        );
    }

    @GetMapping("/inscription/{idInscription}/situation-financiere")
    public ResponseEntity<List<SituationFinanciereEtudiantDto>> consulterSituationFinanciereParInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(
                paiementFraisService.consulterSituationFinanciereParInscription(idInscription)
        );
    }
    @GetMapping("/inscription/{idInscription}/resume-financier")
    public ResponseEntity<ResumeFinancierInscriptionDto> consulterResumeFinancierParInscription(
            @PathVariable String idInscription
    ) {
        return ResponseEntity.ok(
                paiementFraisService.consulterResumeFinancierParInscription(idInscription)
        );
    }
    @DeleteMapping("/{idPaiementFrais}")
    public ResponseEntity<Void> supprimerPaiementFrais(
            @PathVariable Long idPaiementFrais
    ) {
        paiementFraisService.supprimerPaiementFrais(idPaiementFrais);
        return ResponseEntity.noContent().build();
    }
}
