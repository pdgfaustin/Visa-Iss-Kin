
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.SexePersonne;
import com.visa_iss_kin.service.EnseignantService;
import java.util.List;
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
@RequestMapping("/visa-iss/enseignants")
@CrossOrigin("*")
public class EnseignantController {
    private final EnseignantService enseignantService;

    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @PostMapping
    public ResponseEntity<Enseignant> creerEnseignant(
            @RequestBody Enseignant enseignant
    ) {
        return ResponseEntity.status(201)
                .body(enseignantService.creerEnseignant(enseignant));
    }

    @GetMapping
    public ResponseEntity<List<Enseignant>> listerEnseignants() {
        return ResponseEntity.ok(
                enseignantService.listerEnseignants()
        );
    }

    @GetMapping("/{matrEns}")
    public ResponseEntity<Enseignant> rechercherEnseignantParMatricule(
            @PathVariable String matrEns
    ) {
        return ResponseEntity.ok(
                enseignantService.rechercherEnseignantParMatricule(matrEns)
        );
    }

    @GetMapping("/email/{emailEns}")
    public ResponseEntity<Enseignant> rechercherEnseignantParEmail(
            @PathVariable String emailEns
    ) {
        return ResponseEntity.ok(
                enseignantService.rechercherEnseignantParEmail(emailEns)
        );
    }

    @GetMapping("/recherche/nom/{nomEns}")
    public ResponseEntity<List<Enseignant>> rechercherEnseignantsParNom(
            @PathVariable String nomEns
    ) {
        return ResponseEntity.ok(
                enseignantService.rechercherEnseignantsParNom(nomEns)
        );
    }

    @GetMapping("/recherche/postnom/{postNomEns}")
    public ResponseEntity<List<Enseignant>> rechercherEnseignantsParPostNom(
            @PathVariable String postNomEns
    ) {
        return ResponseEntity.ok(
                enseignantService.rechercherEnseignantsParPostNom(postNomEns)
        );
    }

    @GetMapping("/recherche/prenom/{prenomEns}")
    public ResponseEntity<List<Enseignant>> rechercherEnseignantsParPrenom(
            @PathVariable String prenomEns
    ) {
        return ResponseEntity.ok(
                enseignantService.rechercherEnseignantsParPrenom(prenomEns)
        );
    }

    @GetMapping("/sexe/{sexEns}")
    public ResponseEntity<List<Enseignant>> listerEnseignantsParSexe(
            @PathVariable SexePersonne sexEns
    ) {
        return ResponseEntity.ok(
                enseignantService.listerEnseignantsParSexe(sexEns)
        );
    }

    @PutMapping("/{matrEns}")
    public ResponseEntity<Enseignant> modifierEnseignant(
            @PathVariable String matrEns,
            @RequestBody Enseignant enseignant
    ) {
        return ResponseEntity.ok(
                enseignantService.modifierEnseignant(matrEns, enseignant)
        );
    }

    @PostMapping(
            value = "/{matrEns}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Enseignant> enregistrerPhotoEnseignant(
            @PathVariable String matrEns,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                enseignantService.enregistrerPhotoEnseignant(matrEns, file)
        );
    }

    @GetMapping("/{matrEns}/photo")
    public ResponseEntity<byte[]> recupererPhotoEnseignant(
            @PathVariable String matrEns
    ) {
        Enseignant enseignant =
                enseignantService.rechercherEnseignantParMatricule(matrEns);

        byte[] photo =
                enseignantService.recupererPhotoEnseignant(matrEns);

        String typePhoto = enseignant.getTypePhoto();

        if (typePhoto == null || typePhoto.isBlank()) {
            typePhoto = MediaType.IMAGE_JPEG_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(typePhoto))
                .body(photo);
    }

    @DeleteMapping("/{matrEns}")
    public ResponseEntity<Void> supprimerEnseignant(
            @PathVariable String matrEns
    ) {
        enseignantService.supprimerEnseignant(matrEns);
        return ResponseEntity.noContent().build();
    }
}
