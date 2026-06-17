
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(name = "promotionEnseignant")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class PromotionEnseignant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPromotionEnseignant")
    private Long idPromotionEnseignant;

    @Enumerated(EnumType.STRING)
    @Column(name = "ancienGrade", length = 40)
    private GradeEnseignant ancienGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "nouveauGrade", length = 40, nullable = false)
    private GradeEnseignant nouveauGrade;

    @Column(name = "datePromotion", nullable = false)
    private LocalDate datePromotion;

    @Column(name = "numeroDecision", length = 100)
    private String numeroDecision;

    @Column(name = "autoriteDecision", length = 150)
    private String autoriteDecision;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutPromotion", length = 20, nullable = false)
    private StatutPromotionEnseignant statutPromotion;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "matrEns", nullable = false)
    private Enseignant enseignant;

    public PromotionEnseignant() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statutPromotion == null) {
            statutPromotion = StatutPromotionEnseignant.EN_ATTENTE;
        }
    }
}
