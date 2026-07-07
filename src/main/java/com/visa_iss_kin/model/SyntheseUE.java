package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "syntheseUE",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukSyntheseUEInscriptionSessionUE",
                    columnNames = {
                        "idInscription",
                        "idSessionEvaluation",
                        "idUE"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class SyntheseUE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSyntheseUE")
    private Long idSyntheseUE;

    @Column(name = "noteFinale", nullable = false)
    private Double noteFinale;

    @Column(name = "noteMaximale", nullable = false)
    private Double noteMaximale;

    @Column(name = "moyenneSurVingt", nullable = false)
    private Double moyenneSurVingt;

    @Column(name = "nombreEC", nullable = false)
    private Integer nombreEC;

    @Column(name = "nombreECValides", nullable = false)
    private Integer nombreECValides;

    @Column(name = "nombreECNonValides", nullable = false)
    private Integer nombreECNonValides;

    @Column(name = "totalCredits", nullable = false)
    private Double totalCredits;

    @Column(name = "creditsValides", nullable = false)
    private Double creditsValides;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutValidationUE", length = 40, nullable = false)
    private StatutValidationUE statutValidationUE;

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
    @JoinColumn(name = "idUE", nullable = false)
    private UniteEnseignement uniteEnseignement;

    public SyntheseUE() {
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

        if (nombreEC == null) {
            nombreEC = 0;
        }

        if (nombreECValides == null) {
            nombreECValides = 0;
        }

        if (nombreECNonValides == null) {
            nombreECNonValides = 0;
        }

        if (totalCredits == null) {
            totalCredits = 0.0;
        }

        if (creditsValides == null) {
            creditsValides = 0.0;
        }

        if (statutValidationUE == null) {
            statutValidationUE = StatutValidationUE.EN_ATTENTE;
        }
    }
}
