package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "presenceCours",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukPresenceHoraireCours",
                    columnNames = {"idHoraireCours"}
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class PresenceCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPresenceCours")
    private Long idPresenceCours;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutExecution", length = 30, nullable = false)
    private StatutExecutionCours statutExecution;

    @Column(name = "heureArrivee")
    private LocalTime heureArrivee;

    @Column(name = "heureDebutEffective")
    private LocalTime heureDebutEffective;

    @Column(name = "heureFinEffective")
    private LocalTime heureFinEffective;

    @Column(name = "nombreHeuresEffectuees")
    private Double nombreHeuresEffectuees;

    @Column(name = "motif", columnDefinition = "TEXT")
    private String motif;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @OneToOne
    @JoinColumn(name = "idHoraireCours", nullable = false)
    private HoraireCours horaireCours;

    public PresenceCours() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statutExecution == null) {
            statutExecution = StatutExecutionCours.PROGRAMME;
        }

        if (nombreHeuresEffectuees == null) {
            nombreHeuresEffectuees = 0.0;
        }
    }
}
