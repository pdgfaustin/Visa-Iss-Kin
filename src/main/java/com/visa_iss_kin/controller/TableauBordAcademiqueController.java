package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.TableauBordAcademique;
import com.visa_iss_kin.service.TableauBordAcademiqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/tableaux-bord-academiques")
@CrossOrigin("*")
public class TableauBordAcademiqueController {
    private final TableauBordAcademiqueService
            tableauBordAcademiqueService;

    public TableauBordAcademiqueController(
            TableauBordAcademiqueService
                    tableauBordAcademiqueService
    ) {
        this.tableauBordAcademiqueService =
                tableauBordAcademiqueService;
    }

    @GetMapping("/global")
    public ResponseEntity<TableauBordAcademique>
            genererTableauBordGlobal() {

        return ResponseEntity.ok(
                tableauBordAcademiqueService
                        .genererTableauBordGlobal()
        );
    }

    @GetMapping("/promotion/{idPromo}")
    public ResponseEntity<TableauBordAcademique>
            genererTableauBordParPromotion(
                    @PathVariable String idPromo
            ) {

        return ResponseEntity.ok(
                tableauBordAcademiqueService
                        .genererTableauBordParPromotion(
                                idPromo
                        )
        );
    }

    @GetMapping("/enseignant/{matrEns}")
    public ResponseEntity<TableauBordAcademique>
            genererTableauBordParEnseignant(
                    @PathVariable String matrEns
            ) {

        return ResponseEntity.ok(
                tableauBordAcademiqueService
                        .genererTableauBordParEnseignant(
                                matrEns
                        )
        );
    }

    @GetMapping("/annee-academique/{idAa}")
    public ResponseEntity<TableauBordAcademique>
            genererTableauBordParAnneeAcademique(
                    @PathVariable String idAa
            ) {

        return ResponseEntity.ok(
                tableauBordAcademiqueService
                        .genererTableauBordParAnneeAcademique(
                                idAa
                        )
        );
    }
}
