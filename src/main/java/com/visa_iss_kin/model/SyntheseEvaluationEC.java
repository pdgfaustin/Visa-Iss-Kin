package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "syntheseEvaluationEC",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukSyntheseInscriptionSessionEC",
                    columnNames = {
                        "idInscription",
                        "idSessionEvaluation",
                        "idEC"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class SyntheseEvaluationEC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSyntheseEvaluationEC")
    private Long idSyntheseEvaluationEC;

    @Column(name = "noteFinale", nullable = false)
    private Double noteFinale;

    @Column(name = "noteMaximale", nullable = false)
    private Double noteMaximale;

    @Column(name = "moyenneSurVingt", nullable = false)
    private Double moyenneSurVingt;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutValidation", length = 30, nullable = false)
    private StatutValidationEC statutValidation;

    @Column(name = "nombreEvaluations", nullable = false)
    private Integer nombreEvaluations;

    @Column(name = "totalPonderation", nullable = false)
    private Double totalPonderation;

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

    @ManyToOne
    @JoinColumn(name = "idEC", nullable = false)
    private ElementConstitutif elementConstitutif;

    public SyntheseEvaluationEC() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (noteFinale == null) {
            noteFinale = 0.0;
        }

        if (noteMaximale == null) {
            noteMaximale = 20.0;
        }

        if (moyenneSurVingt == null) {
            moyenneSurVingt = 0.0;
        }

        if (statutValidation == null) {
            statutValidation = StatutValidationEC.EN_ATTENTE;
        }

        if (nombreEvaluations == null) {
            nombreEvaluations = 0;
        }

        if (totalPonderation == null) {
            totalPonderation = 0.0;
        }
    }
}
