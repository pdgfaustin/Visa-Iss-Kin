package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "evaluationEtudiant",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukEvaluationEpreuveInscription",
                    columnNames = {
                        "idEpreuve",
                        "idInscription"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class EvaluationEtudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEvaluationEtudiant")
    private Long idEvaluationEtudiant;

    @Column(name = "noteObtenue", nullable = false)
    private Double noteObtenue;

    @Column(name = "appreciation", length = 100)
    private String appreciation;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idEpreuve", nullable = false)
    private Epreuve epreuve;

    @ManyToOne
    @JoinColumn(name = "idInscription", nullable = false)
    private Inscription inscription;

    public EvaluationEtudiant() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
