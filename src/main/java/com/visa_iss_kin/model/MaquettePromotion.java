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
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "maquettePromotion",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukMaquettePromotionEcAnneeSemestre",
                    columnNames = {
                        "idPromo",
                        "idEC",
                        "idAa",
                        "semestre"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class MaquettePromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMaquette")
    private Long idMaquette;

    @Enumerated(EnumType.STRING)
    @Column(name = "semestre", length = 20, nullable = false)
    private Semestre semestre;

    @Column(
            name = "coefficient",
            precision = 6,
            scale = 2,
            nullable = false
    )
    private BigDecimal coefficient;

    @Column(name = "creditApplicable", nullable = false)
    private Integer creditApplicable;

    @Column(name = "volumeHorairePrevu", nullable = false)
    private Integer volumeHorairePrevu;

    @Enumerated(EnumType.STRING)
    @Column(name = "obligatoire", length = 5, nullable = false)
    private Obligatoire obligatoire;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idPromo", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "idEC", nullable = false)
    private ElementConstitutif elementConstitutif;

    @ManyToOne
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public MaquettePromotion() {
    }

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (coefficient == null) {
            coefficient = BigDecimal.ONE;
        }

        if (creditApplicable == null) {
            creditApplicable = 0;
        }

        if (volumeHorairePrevu == null) {
            volumeHorairePrevu = 0;
        }

        if (obligatoire == null) {
            obligatoire = Obligatoire.OUI;
        }
    }
}
