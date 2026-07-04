
package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "sessionEvaluation",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukSessionAnneeSemestreType",
                    columnNames = {
                        "idAa",
                        "semestre",
                        "typeSession"
                    }
            )
        }
)
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class SessionEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSessionEvaluation")
    private Long idSessionEvaluation;

    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", length = 20, nullable = false)
    private Semestre semestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeSession", length = 30, nullable = false)
    private TypeSession typeSession;

    @Column(name = "dateDebut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "dateFin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "libelleSession", length = 150)
    private String libelleSession;

    @Column(name = "ouverte", nullable = false)
    private Boolean ouverte;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public SessionEvaluation() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (ouverte == null) {
            ouverte = false;
        }
    }
}
