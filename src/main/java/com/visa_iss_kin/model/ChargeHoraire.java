
package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "chargeHoraire",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukChargeEnseignantMaquetteType",
                    columnNames = {
                        "matrEns",
                        "idMaquette",
                        "typeCharge"
                    }
            )
        }
)
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class ChargeHoraire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChargeHoraire")
    private Long idChargeHoraire;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeCharge", length = 30, nullable = false)
    private TypeCharge typeCharge;

    @Column(name = "nombreHeures", nullable = false)
    private Integer nombreHeures;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "matrEns", nullable = false)
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "idMaquette", nullable = false)
    private MaquettePromotion maquettePromotion;

    public ChargeHoraire() {
    }


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
