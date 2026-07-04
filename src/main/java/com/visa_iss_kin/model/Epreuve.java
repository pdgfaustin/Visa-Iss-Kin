
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
        name = "epreuve",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukEpreuveSessionPromotionECType",
                    columnNames = {
                        "idSessionEvaluation",
                        "idPromo",
                        "idEC",
                        "typeEpreuve"
                    }
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class Epreuve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEpreuve")
    private Long idEpreuve;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeEpreuve", length = 40, nullable = false)
    private TypeEpreuve typeEpreuve;

    @Column(name = "libelleEpreuve", length = 150)
    private String libelleEpreuve;

    @Column(name = "dateEpreuve", nullable = false)
    private LocalDate dateEpreuve;

    @Column(name = "ponderation", nullable = false)
    private Double ponderation;

    @Column(name = "noteMaximale", nullable = false)
    private Double noteMaximale;

    @Column(name = "ouverte", nullable = false)
    private Boolean ouverte;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idSessionEvaluation", nullable = false)
    private SessionEvaluation sessionEvaluation;

    @ManyToOne
    @JoinColumn(name = "idPromo", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "idEC", nullable = false)
    private ElementConstitutif elementConstitutif;

    public Epreuve() {
    }
}
