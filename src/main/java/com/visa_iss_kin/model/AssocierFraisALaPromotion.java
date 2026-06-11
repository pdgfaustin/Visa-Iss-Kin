
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "associerFraisALaPromotion",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukFraisPromotionAnnee",
                    columnNames = {"idFrais", "idPromo", "idAa"}
            )
        }
)
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class AssocierFraisALaPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAssociationFrais")
    private Long idAssociationFrais;

    @Column(name = "montantFrais", precision = 15, scale = 2, nullable = false)
    private BigDecimal montantFrais;

    @Enumerated(EnumType.STRING)
    @Column(name = "devise", length = 10, nullable = false)
    private Devise devise;

    @Enumerated(EnumType.STRING)
    @Column(name = "obligatoire", length = 5, nullable = false)
    private Obligatoire obligatoire;

    @Column(name = "dateDebutValidite")
    private LocalDate dateDebutValidite;

    @Column(name = "dateFinValidite")
    private LocalDate dateFinValidite;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idFrais", nullable = false)
    private FraisAcademique fraisAcademique;

    @ManyToOne
    @JoinColumn(name = "idPromo", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public AssocierFraisALaPromotion() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
