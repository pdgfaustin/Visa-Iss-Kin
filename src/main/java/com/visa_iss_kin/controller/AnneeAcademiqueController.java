package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.AnneeAcademique;
import com.visa_iss_kin.model.StatutAnneeAcademique;
import com.visa_iss_kin.service.AnneeAcademiqueService;
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
@RequestMapping("/visa-iss/annee-aa")
@CrossOrigin("*")
public class AnneeAcademiqueController {
    private final AnneeAcademiqueService anneeAAS;

    public AnneeAcademiqueController(AnneeAcademiqueService anneeAAS) {
        this.anneeAAS = anneeAAS;
    }

    @PostMapping
    public ResponseEntity<AnneeAcademique> creerAnneeAcademique(
            @RequestBody AnneeAcademique anneeAcademique
    ) {
        AnneeAcademique nouvelleAnnee =
                anneeAAS.creerAnneeAcademique(anneeAcademique);

        return new ResponseEntity<>(nouvelleAnnee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AnneeAcademique>> listerAnneesAcademiques() {
        return ResponseEntity.ok(anneeAAS.listerAnneesAcademiques());
    }

    @GetMapping("/{idAa}")
    public ResponseEntity<AnneeAcademique> rechercherAnneeAcademiqueParId(
            @PathVariable String idAa
    ) {
        return ResponseEntity.ok(anneeAAS.rechercherAnneeAcademiqueParId(idAa));
    }

    @GetMapping("/statut/{statutAnnee}")
    public ResponseEntity<List<AnneeAcademique>> listerAnneesParStatut(
            @PathVariable StatutAnneeAcademique statutAnnee
    ) {
        return ResponseEntity.ok(anneeAAS.listerAnneesParStatut(statutAnnee));
    }

    @PutMapping("/{idAa}")
    public ResponseEntity<AnneeAcademique> modifierAnneeAcademique(
            @PathVariable String idAa,
            @RequestBody AnneeAcademique anneeAcademique
    ) {
        return ResponseEntity.ok(
                anneeAAS.modifierAnneeAcademique(idAa, anneeAcademique)
        );
    }

    @PutMapping("/{idAa}/ouvrir")
    public ResponseEntity<AnneeAcademique> ouvrirAnneeAcademique(@PathVariable String idAa) {
        return ResponseEntity.ok(anneeAAS.ouvrirAnneeAcademique(idAa));
    }

    @PutMapping("/{idAa}/fermer")
    public ResponseEntity<AnneeAcademique> fermerAnneeAcademique(@PathVariable String idAa) {
        return ResponseEntity.ok(anneeAAS.fermerAnneeAcademique(idAa));
    }

    @DeleteMapping("/{idAa}")
    public ResponseEntity<Void> supprimerAnneeAcademique(@PathVariable String idAa) {
        anneeAAS.supprimerAnneeAcademique(idAa);
        return ResponseEntity.noContent().build();
    }
}
