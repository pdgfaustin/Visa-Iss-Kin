package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "syntheseAnnuelle",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukSyntheseAnnuelleInscriptionAnnee",
                    columnNames = {
                        "idInscription",
                        "idAa"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class SyntheseAnnuelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSyntheseAnnuelle")
    private Long idSyntheseAnnuelle;

    @Column(name = "moyenneAnnuelle", nullable = false)
    private Double moyenneAnnuelle;

    @Column(name = "noteMaximale", nullable = false)
    private Double noteMaximale;

    @Column(name = "nombreSemestres", nullable = false)
    private Integer nombreSemestres;

    @Column(name = "nombreSemestresValides", nullable = false)
    private Integer nombreSemestresValides;

    @Column(name = "nombreSemestresNonValides", nullable = false)
    private Integer nombreSemestresNonValides;

    @Column(name = "nombreSemestresPartiellementValides", nullable = false)
    private Integer nombreSemestresPartiellementValides;

    @Column(name = "totalCredits", nullable = false)
    private Double totalCredits;

    @Column(name = "creditsValides", nullable = false)
    private Double creditsValides;

    @Column(name = "creditsNonValides", nullable = false)
    private Double creditsNonValides;

    @Column(name = "tauxCreditsValides", nullable = false)
    private Double tauxCreditsValides;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutValidationAnnee", length = 40, nullable = false)
    private StatutValidationAnnee statutValidationAnnee;

    @Column(name = "decisionFinale", length = 100)
    private String decisionFinale;

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
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public SyntheseAnnuelle() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (moyenneAnnuelle == null) {
            moyenneAnnuelle = 0.0;
        }

        if (noteMaximale == null) {
            noteMaximale = 20.0;
        }

        if (nombreSemestres == null) {
            nombreSemestres = 0;
        }

        if (nombreSemestresValides == null) {
            nombreSemestresValides = 0;
        }

        if (nombreSemestresNonValides == null) {
            nombreSemestresNonValides = 0;
        }

        if (nombreSemestresPartiellementValides == null) {
            nombreSemestresPartiellementValides = 0;
        }

        if (totalCredits == null) {
            totalCredits = 0.0;
        }

        if (creditsValides == null) {
            creditsValides = 0.0;
        }

        if (creditsNonValides == null) {
            creditsNonValides = 0.0;
        }

        if (tauxCreditsValides == null) {
            tauxCreditsValides = 0.0;
        }

        if (statutValidationAnnee == null) {
            statutValidationAnnee = StatutValidationAnnee.EN_ATTENTE;
        }
    }
}
