
package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(name = "numeroCompte")
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class NumeroCompte {

    @Id
    @Column(name = "idNumeroBanque", length = 30, nullable = false)
    private String idNumeroCompte;

    @Column(name = "numeroCompte", length = 50, nullable = false)
    private String numeroCompte;

    @Column(name = "intituleCompte", length = 150, nullable = false)
    private String intituleCompte;

    @Enumerated(EnumType.STRING)
    @Column(name = "deviseCompte", length = 10, nullable = false)
    private Devise deviseCompte;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutCompte", length = 20, nullable = false)
    private StatutCompte statutCompte;

    @Column(name = "nomGestionnaire", length = 100)
    private String nomGestionnaire;

    @Column(name = "telephoneGestionnaire", length = 30)
    private String telephoneGestionnaire;

    @Column(name = "emailGestionnaire", length = 100)
    private String emailGestionnaire;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idBanque", nullable = false)
    private Banque banque;

    public NumeroCompte() {
        
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statutCompte == null) {
            statutCompte = StatutCompte.ACTIF;
        }
    }
}
