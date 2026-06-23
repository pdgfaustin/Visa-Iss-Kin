package com.visa_iss_kin.model;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class SuiviChargeHoraire {
    private Long idChargeHoraire;

    private String matrEns;

    private String nomCompletEnseignant;

    private String idPromo;

    private String libellePromotion;

    private Long idMaquette;

    private String codeEC;

    private String libelleEC;

    private TypeCharge typeCharge;

    private Integer volumeHorairePrevu;

    private Integer volumeHoraireAffecte;

    private Double volumeHoraireProgramme;

    private Double volumeHoraireEffectue;

    private Double volumeHoraireRestantAProgrammer;

    private Double volumeHoraireRestantAExecuter;

    private Double tauxProgrammation;

    private Double tauxExecution;

    public SuiviChargeHoraire() {
    }
}
