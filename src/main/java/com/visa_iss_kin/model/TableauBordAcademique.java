package com.visa_iss_kin.model;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class TableauBordAcademique {
    private Long nombreEnseignants;

    private Long nombrePromotions;

    private Long nombreMaquettes;

    private Long nombreChargesHoraires;

    private Long nombreCoursProgrammes;

    private Long nombreCoursEffectues;

    private Long nombreCoursAnnules;

    private Long nombreCoursReportes;

    private Long nombreAbsencesEnseignants;

    private Double volumeHorairePrevu;

    private Double volumeHoraireAffecte;

    private Double volumeHoraireProgramme;

    private Double volumeHoraireEffectue;

    private Double volumeHoraireRestantAProgrammer;

    private Double volumeHoraireRestantAExecuter;

    private Double tauxGlobalProgrammation;

    private Double tauxGlobalExecution;

    public TableauBordAcademique() {
    }
}
