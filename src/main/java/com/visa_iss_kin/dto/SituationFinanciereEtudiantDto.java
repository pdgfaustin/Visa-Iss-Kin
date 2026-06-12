
package com.visa_iss_kin.dto;

import com.visa_iss_kin.model.Devise;
import com.visa_iss_kin.model.StatutFinancier;
import java.math.BigDecimal;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class SituationFinanciereEtudiantDto {
    private Long idAssociationFrais;

    private String idFrais;

    private String libeFrais;

    private String idInscription;

    private String idEtudiant;

    private String matriculEtudiant;

    private String nomCompletEtudiant;

    private String idPromo;

    private String libePromo;

    private String idAa;

    private BigDecimal montantPrevu;

    private BigDecimal montantPaye;

    private BigDecimal resteAPayer;

    private Devise devise;

    private StatutFinancier statutFinancier;

    public SituationFinanciereEtudiantDto() {
    }
}
