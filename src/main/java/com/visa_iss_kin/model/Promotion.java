package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "Promotion")
public class Promotion {
    @Id
    @Column(name = "idPromo", length = 10, nullable = false)
    private String idPromo;
    @Column(name = "libePromo", length = 150, nullable = false)
    private String libePromo;
    @ManyToOne
    @JoinColumn(name = "idOpt", nullable = false)
    private OptionSuivie optionSuivie;
    @Column(name = "niveauPromotion", nullable = false)
    private NiveauPromotion niveauPromotion;
    @Column(name = "createdBy", nullable = false)
    private String createdBy;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "totUE")
    private Integer totUE;

    public Promotion(String idPromo, String libepromo, OptionSuivie optionSuivie, NiveauPromotion niveauPromotion, String createdBy, LocalDateTime createdAt, Integer totUE) {
        this.idPromo = idPromo;
        this.libePromo = libepromo;
        this.optionSuivie = optionSuivie;
        this.niveauPromotion = niveauPromotion;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.totUE = totUE;
    }

    public Promotion() {
    }
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (totUE == null) {
            totUE = 0;
        }
    }
}
