package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Nationalite")
public class Nationalite {
    @Id
    @Column(name = "idNation", length = 15, nullable = false)
    private String idNation;
    @Column(name = "libeNation", nullable = false)
    private String libeNation;
    @Column(name = "createdBy",nullable = false)
    private String createdBy;
    @Column(name = "createdAt",nullable = false)
    private LocalDateTime createdAt;

    public Nationalite(String idNation, String libeNation, String createdBy, LocalDateTime createdAt) {
        this.idNation = idNation;
        this.libeNation = libeNation;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Nationalite() {
    }
    
    @PrePersist
    public void prePersist(){
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
