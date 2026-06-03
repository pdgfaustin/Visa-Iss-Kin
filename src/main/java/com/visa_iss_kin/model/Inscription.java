
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
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(
        name = "inscription",
        uniqueConstraints ={
            @UniqueConstraint(
                name = "ukInscriptionEtudiantAA",
                columnNames = {"idEtudiant","idAa"}
            )
        }
)
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class Inscription {
    public enum StatutInscription {
        EN_COURS,
        VALIDEE,
        ANNULEE
    }

    @Id
    @Column(name = "idInscription", length = 25, nullable = false)
    private String idInscription;

    @Column(name = "dateInscription", nullable = false)
    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "statutInscription", length = 20, nullable = false)
    private StatutInscription statutInscription = StatutInscription.EN_COURS;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idEtudiant", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "idPromo", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "idAa", nullable = false)
    private AnneeAcademique anneeAcademique;

    public Inscription() {
         
    }
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (dateInscription == null) {
            dateInscription = LocalDate.now();
        }

        if (statutInscription == null) {
            statutInscription = StatutInscription.EN_COURS;
        }
    }
}
