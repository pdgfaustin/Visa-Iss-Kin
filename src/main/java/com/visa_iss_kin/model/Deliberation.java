package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "deliberation",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukDeliberationSyntheseAnnuelle",
                    columnNames = {
                        "idSyntheseAnnuelle"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class Deliberation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDeliberation")
    private Long idDeliberation;

    @Column(name = "moyenneFinale", nullable = false)
    private Double moyenneFinale;

    @Column(name = "totalCredits", nullable = false)
    private Double totalCredits;

    @Column(name = "creditsValides", nullable = false)
    private Double creditsValides;

    @Column(name = "creditsNonValides", nullable = false)
    private Double creditsNonValides;

    @Column(name = "tauxCreditsValides", nullable = false)
    private Double tauxCreditsValides;

    @Enumerated(EnumType.STRING)
    @Column(name = "decisionDeliberation", length = 40, nullable = false)
    private DecisionDeliberation decisionDeliberation;

    @Column(name = "mention", length = 100)
    private String mention;

    @Column(name = "motifDecision", columnDefinition = "TEXT")
    private String motifDecision;

    @Column(name = "observationJury", columnDefinition = "TEXT")
    private String observationJury;

    @Column(name = "validee", nullable = false)
    private Boolean validee;

    @Column(name = "dateDeliberation")
    private LocalDateTime dateDeliberation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @Column(name = "validatedAt")
    private LocalDateTime validatedAt;

    @Column(name = "validatedBy", length = 30)
    private String validatedBy;

    @ManyToOne
    @JoinColumn(name = "idSyntheseAnnuelle", nullable = false)
    private SyntheseAnnuelle syntheseAnnuelle;

    @ManyToOne
    @JoinColumn(name = "idInscription", nullable = false)
    private Inscription inscription;

    @ManyToOne
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public Deliberation() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (dateDeliberation == null) {
            dateDeliberation = LocalDateTime.now();
        }

        if (moyenneFinale == null) {
            moyenneFinale = 0.0;
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

        if (decisionDeliberation == null) {
            decisionDeliberation = DecisionDeliberation.EN_ATTENTE;
        }

        if (validee == null) {
            validee = false;
        }
    }
}
