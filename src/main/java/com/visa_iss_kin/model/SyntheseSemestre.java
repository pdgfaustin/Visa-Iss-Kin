package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "syntheseSemestre",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukSyntheseSemestreInscriptionSession",
                    columnNames = {
                        "idInscription",
                        "idSessionEvaluation"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class SyntheseSemestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSyntheseSemestre")
    private Long idSyntheseSemestre;

    @Column(name = "moyenneSemestre", nullable = false)
    private Double moyenneSemestre;

    @Column(name = "noteMaximale", nullable = false)
    private Double noteMaximale;

    @Column(name = "nombreUE", nullable = false)
    private Integer nombreUE;

    @Column(name = "nombreUEValidees", nullable = false)
    private Integer nombreUEValidees;

    @Column(name = "nombreUENonValidees", nullable = false)
    private Integer nombreUENonValidees;

    @Column(name = "nombreUEPartiellementValidees", nullable = false)
    private Integer nombreUEPartiellementValidees;

    @Column(name = "totalCredits", nullable = false)
    private Double totalCredits;

    @Column(name = "creditsValides", nullable = false)
    private Double creditsValides;

    @Column(name = "tauxCreditsValides", nullable = false)
    private Double tauxCreditsValides;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutValidationSemestre", length = 40, nullable = false)
    private StatutValidationSemestre statutValidationSemestre;

    @Column(name = "appreciation", length = 100)
    private String appreciation;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idInscription", nullable = false)
    private Inscription inscription;

    @ManyToOne
    @JoinColumn(name = "idSessionEvaluation", nullable = false)
    private SessionEvaluation sessionEvaluation;

    public SyntheseSemestre() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (moyenneSemestre == null) {
            moyenneSemestre = 0.0;
        }

        if (noteMaximale == null) {
            noteMaximale = 20.0;
        }

        if (nombreUE == null) {
            nombreUE = 0;
        }

        if (nombreUEValidees == null) {
            nombreUEValidees = 0;
        }

        if (nombreUENonValidees == null) {
            nombreUENonValidees = 0;
        }

        if (nombreUEPartiellementValidees == null) {
            nombreUEPartiellementValidees = 0;
        }

        if (totalCredits == null) {
            totalCredits = 0.0;
        }

        if (creditsValides == null) {
            creditsValides = 0.0;
        }

        if (tauxCreditsValides == null) {
            tauxCreditsValides = 0.0;
        }

        if (statutValidationSemestre == null) {
            statutValidationSemestre =
                    StatutValidationSemestre.EN_ATTENTE;
        }
    }
}
