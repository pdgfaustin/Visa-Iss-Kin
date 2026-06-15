
package com.visa_iss_kin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(name = "enseignant")
@lombok.Setter
@lombok.Getter
@lombok.AllArgsConstructor
public class Enseignant {
    @Id
    @Column(name = "matrEns", length = 30, nullable = false)
    private String matrEns;

    @Column(name = "nomEns", length = 80, nullable = false)
    private String nomEns;

    @Column(name = "postNomEns", length = 80)
    private String postNomEns;

    @Column(name = "prenomEns", length = 80)
    private String prenomEns;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexEns", length = 10, nullable = false)
    private SexePersonne sexEns;

    @Enumerated(EnumType.STRING)
    @Column(name = "etatCivil", length = 20)
    private EtatCivil etatCivil;

    @Column(name = "dateNaissance")
    private LocalDate dateNaissance;

    @Column(name = "dateEngagement")
    private LocalDate dateEngagement;

    @Column(name = "adresseEns", length = 255)
    private String adresseEns;

    @Column(name = "telephoneEns", length = 30)
    private String telephoneEns;

    @Column(name = "emailEns", length = 120)
    private String emailEns;

    @Lob
    @JsonIgnore
    @Column(name = "photoEns", columnDefinition = "LONGBLOB")
    private byte[] photoEns;

    @Column(name = "typePhoto", length = 50)
    private String typePhoto;

    @Column(name = "nomFichierPhoto", length = 255)
    private String nomFichierPhoto;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    public Enseignant() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
