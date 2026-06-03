package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "AnneeAcademique")
public class AnneeAcademique {
    @Id
    @Column(name = "idAa",length = 9, nullable = false)
    private String idAa;
    @Column(name = "dateDebut")
    private LocalDate dateDebut;
    @Column(name = "dateFin")
    private LocalDate dateFin;
    @Column(name = "statutAnneeAcademique")
    private StatutAnneeAcademique statutAnneeAcademique;
    @Column(name = "createdBy", nullable = false, length = 30)
    private String createdBy;
    @Column(name = "createdAt",nullable = false)
    private LocalDateTime createdAt;

    public AnneeAcademique(String idAa, LocalDate dateDebut, LocalDate dateFin, StatutAnneeAcademique statutAnneeAcademique, String createdBy, LocalDateTime createdAt) {
        this.idAa = idAa;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statutAnneeAcademique = statutAnneeAcademique;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public AnneeAcademique() {
    }
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statutAnneeAcademique == null) {
            statutAnneeAcademique = StatutAnneeAcademique.EN_PREPARATION;
        }
    }
}
