package com.visa_iss_kin.controller;

import com.visa_iss_kin.service.SuiviChargeHoraireService;
import com.visa_iss_kin.model.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/suivis-charges-horaires")
@CrossOrigin("*")
public class SuiviChargeHoraireController {
    private final SuiviChargeHoraireService suiviChargeHoraireService;

    public SuiviChargeHoraireController(
            SuiviChargeHoraireService suiviChargeHoraireService
    ) {
        this.suiviChargeHoraireService = suiviChargeHoraireService;
    }

    @GetMapping
    public ResponseEntity<List<SuiviChargeHoraire>>
            listerSuivisChargesHoraires() {

        return ResponseEntity.ok(
                suiviChargeHoraireService
                        .listerSuivisChargesHoraires()
        );
    }

    @GetMapping("/charge-horaire/{idChargeHoraire}")
    public ResponseEntity<SuiviChargeHoraire>
            calculerSuiviParChargeHoraire(
                    @PathVariable Long idChargeHoraire
            ) {

        return ResponseEntity.ok(
                suiviChargeHoraireService
                        .calculerSuiviParChargeHoraire(
                                idChargeHoraire
                        )
        );
    }

    @GetMapping("/enseignant/{matrEns}")
    public ResponseEntity<List<SuiviChargeHoraire>>
            listerSuivisParEnseignant(
                    @PathVariable String matrEns
            ) {

        return ResponseEntity.ok(
                suiviChargeHoraireService
                        .listerSuivisParEnseignant(
                                matrEns
                        )
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<List<SuiviChargeHoraire>>
            listerSuivisParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                suiviChargeHoraireService
                        .listerSuivisParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/maquette/{idMaquette}")
    public ResponseEntity<List<SuiviChargeHoraire>>
            listerSuivisParMaquette(
                    @PathVariable Long idMaquette
            ) {

        return ResponseEntity.ok(
                suiviChargeHoraireService
                        .listerSuivisParMaquette(
                                idMaquette
                        )
        );
    }
}
