
package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.ElementConstitutif;
import com.visa_iss_kin.service.ElementConstitutifService;
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
@RequestMapping("/visa-iss/elements-constitutifs")
@CrossOrigin("*")
public class ElementConstitutifController {

    private final ElementConstitutifService elementConstitutifService;

    public ElementConstitutifController(
            ElementConstitutifService elementConstitutifService
    ) {
        this.elementConstitutifService = elementConstitutifService;
    }

    @PostMapping
    public ResponseEntity<ElementConstitutif> creerElementConstitutif(
            @RequestBody ElementConstitutif elementConstitutif
    ) {
        ElementConstitutif nouvelElement =
                elementConstitutifService.creerElementConstitutif(elementConstitutif);

        return new ResponseEntity<>(nouvelElement, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ElementConstitutif>> listerElementsConstitutifs() {
        return ResponseEntity.ok(
                elementConstitutifService.listerElementsConstitutifs()
        );
    }

    @GetMapping("/{idEC}")
    public ResponseEntity<ElementConstitutif> rechercherElementConstitutifParId(
            @PathVariable String idEC
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.rechercherElementConstitutifParId(idEC)
        );
    }

    @GetMapping("/code/{codeEC}")
    public ResponseEntity<ElementConstitutif> rechercherElementConstitutifParCode(
            @PathVariable String codeEC
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.rechercherElementConstitutifParCode(codeEC)
        );
    }

    @GetMapping("/recherche/libelle/{libeEC}")
    public ResponseEntity<List<ElementConstitutif>> rechercherElementsParLibelle(
            @PathVariable String libeEC
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.rechercherElementsConstitutifsParLibelle(libeEC)
        );
    }

    @GetMapping("/recherche/code/{codeEC}")
    public ResponseEntity<List<ElementConstitutif>> rechercherElementsParCode(
            @PathVariable String codeEC
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.rechercherElementsConstitutifsParCode(codeEC)
        );
    }

    @GetMapping("/ue/{idUE}")
    public ResponseEntity<List<ElementConstitutif>> listerElementsParUE(
            @PathVariable String idUE
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.listerElementsConstitutifsParUE(idUE)
        );
    }

    @PutMapping("/{idEC}")
    public ResponseEntity<ElementConstitutif> modifierElementConstitutif(
            @PathVariable String idEC,
            @RequestBody ElementConstitutif elementConstitutif
    ) {
        return ResponseEntity.ok(
                elementConstitutifService.modifierElementConstitutif(
                        idEC,
                        elementConstitutif
                )
        );
    }

    @DeleteMapping("/{idEC}")
    public ResponseEntity<Void> supprimerElementConstitutif(
            @PathVariable String idEC
    ) {
        elementConstitutifService.supprimerElementConstitutif(idEC);
        return ResponseEntity.noContent().build();
    }
}