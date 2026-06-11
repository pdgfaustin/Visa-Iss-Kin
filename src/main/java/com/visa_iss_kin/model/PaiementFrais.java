
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
        name = "paiementFrais",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "ukReferencePaiement",
                    columnNames = "referencePaiement"
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class PaiementFrais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPaiementFrais")
    private Long idPaiementFrais;

    @Column(name = "montantPaye", precision = 15, scale = 2, nullable = false)
    private BigDecimal montantPaye;

    @Enumerated(EnumType.STRING)
    @Column(name = "devisePaiement", length = 10, nullable = false)
    private Devise devisePaiement;

    @Column(name = "referencePaiement", length = 100, nullable = false, unique = true)
    private String referencePaiement;

    @Column(name = "datePaiement", nullable = false)
    private LocalDateTime datePaiement;

    @Column(name = "dateEnregistrement", nullable = false)
    private LocalDateTime dateEnregistrement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutPaiement", length = 20, nullable = false)
    private StatutPaiement statutPaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "modePaiement", length = 20, nullable = false)
    private ModePaiement modePaiement;

    @Column(name = "observationPaiement", columnDefinition = "TEXT")
    private String observationPaiement;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idInscription", nullable = false)
    private Inscription inscription;

    @ManyToOne
    @JoinColumn(name = "idAssociationFrais", nullable = false)
    private AssocierFraisALaPromotion associationFrais;

    @ManyToOne
    @JoinColumn(name = "idNumeroBanque", nullable = false)
    private NumeroCompte numeroBanque;

    public PaiementFrais() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (dateEnregistrement == null) {
            dateEnregistrement = LocalDateTime.now();
        }

        if (datePaiement == null) {
            datePaiement = LocalDateTime.now();
        }

        if (statutPaiement == null) {
            statutPaiement = StatutPaiement.VALIDE;
        }

        if (modePaiement == null) {
            modePaiement = ModePaiement.BANQUE;
        }
    }
}
