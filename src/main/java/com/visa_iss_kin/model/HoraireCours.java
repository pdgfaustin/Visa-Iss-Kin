
package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
@Entity
@Table(
        name = "horaireCours",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukHoraireSalleDateHeure",
                    columnNames = {
                        "dateCours",
                        "heureDebut",
                        "heureFin",
                        "salle"
                    }
            )
        }
)
public class HoraireCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idHoraireCours")
    private Long idHoraireCours;

    @Column(name = "dateCours", nullable = false)
    private LocalDate dateCours;

    @Column(name = "heureDebut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heureFin", nullable = false)
    private LocalTime heureFin;

    @Column(name = "salle", length = 100, nullable = false)
    private String salle;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idChargeHoraire", nullable = false)
    private ChargeHoraire chargeHoraire;

    public HoraireCours() {
        
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
