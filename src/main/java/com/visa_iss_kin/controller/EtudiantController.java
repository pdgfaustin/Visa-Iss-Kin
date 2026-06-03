
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Etudiant;
import com.visa_iss_kin.service.EtudiantService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/etudiants")
@CrossOrigin("*")
public class EtudiantController {
    private final EtudiantService etudiantService;

    public EtudiantController(EtudiantService etudiantService) {
        this.etudiantService = etudiantService;
    }

    @PostMapping
    public ResponseEntity<Etudiant> creerEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant nouvelEtudiant = etudiantService.creerEtudiant(etudiant);
        return new ResponseEntity<>(nouvelEtudiant, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Etudiant>> listerEtudiants() {
        return ResponseEntity.ok(etudiantService.listerEtudiants());
    }

    @GetMapping("/{idEtudiant}")
    public ResponseEntity<Etudiant> rechercherEtudiantParId(@PathVariable String idEtudiant) {
        return ResponseEntity.ok(etudiantService.rechercherEtudiantParId(idEtudiant));
    }

    @GetMapping("/matricule/{matriculeEtudiant}")
    public ResponseEntity<Etudiant> rechercherEtudiantParMatricule(
            @PathVariable String matriculeEtudiant
    ) {
        return ResponseEntity.ok(etudiantService.rechercherEtudiantParMatricule(matriculeEtudiant));
    }

    @GetMapping("/email/{emailEtudiant}")
    public ResponseEntity<Etudiant> rechercherEtudiantParEmail(
            @PathVariable String emailEtudiant
    ) {
        return ResponseEntity.ok(etudiantService.rechercherEtudiantParEmail(emailEtudiant));
    }

    @GetMapping("/nom/{nomEtudiant}")
    public ResponseEntity<List<Etudiant>> rechercherEtudiantsParNom(
            @PathVariable String nomEtudiant
    ) {
        return ResponseEntity.ok(etudiantService.rechercherEtudiantsParNom(nomEtudiant));
    }

    @GetMapping("/nationalite/{idNation}")
    public ResponseEntity<List<Etudiant>> listerEtudiantsParNationalite(
            @PathVariable String idNation
    ) {
        return ResponseEntity.ok(etudiantService.listeDesEtudiantsParNationalite(idNation));
    }

    @PutMapping("/{idEtudiant}")
    public ResponseEntity<Etudiant> modifierEtudiant(
            @PathVariable String idEtudiant,
            @RequestBody Etudiant etudiant
    ) {
        return ResponseEntity.ok(etudiantService.modifierEtudiant(idEtudiant, etudiant));
    }

    @DeleteMapping("/{idEtudiant}")
    public ResponseEntity<Void> supprimerEtudiant(@PathVariable String idEtudiant) {
        etudiantService.supprimerEtudiant(idEtudiant);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(value = "/{idEtudiant}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Etudiant> enregistrerPhotoEtudiant(
            @PathVariable String idEtudiant,
            @RequestParam("file") MultipartFile file
    ) {
        Etudiant etudiant = etudiantService.enregistrerPhotoEtudiant(idEtudiant, file);
        return ResponseEntity.ok(etudiant);
    }

    @GetMapping("/{idEtudiant}/photo")
    public ResponseEntity<byte[]> recupererPhotoEtudiant(@PathVariable String idEtudiant) {

        Etudiant etudiant = etudiantService.rechercherEtudiantParId(idEtudiant);
        byte[] photo = etudiantService.recupererPhotoEtudiant(idEtudiant);

        String typePhoto = etudiant.getTypePhoto();

        if (typePhoto == null || typePhoto.isBlank()) {
            typePhoto = MediaType.IMAGE_JPEG_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(typePhoto))
                .body(photo);
    }
}
