package com.visa_iss_kin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@lombok.AllArgsConstructor
@Entity
@Table(name = "Etudiant")
public class Etudiant {
    @Id
    @Column(name = "idEtudiant",length = 15, nullable = false)
    private String idEtudiant;
    @Column(name = "matriculEtudiant", unique = true, length = 11)
    private String matriculEtudiant;
    @Column(name = "nomEtudiant", length = 20, nullable = false)
    private String nomEtudiant;
    @Column(name = "postNomEtudiant", length = 20)
    private String postNomEtudiant;
    @Column(name = "prenomEtudiant", length = 20)
    private String prenomEtudiant;
    @Column(name = "sexEtudiant", length = 1)
    private SexePersonne sexEtudiant;
    @Column(name = "dateNaissance", nullable = false)
    private LocalDate dateNaissance;
    @Column(name = "telephonEtudiant", length = 17)
    private String telephonEtudiant;
    @Column(name = "emailEtudiant", unique = true)
    private String emailEtudiant;
    @Column(name = "actif", nullable = false)
    private Actif actif;
    @Lob
    @JsonIgnore
    @Column(name = "photoEtudiant", columnDefinition = "LONGBLOB")
    private byte[] photoEtudiant;

    @Column(name = "typePhoto", length = 50)
    private String typePhoto;

    public Etudiant(){
        
    }
    @Column(name = "nomFichierPhoto", length = 150)
    private String nomFichierPhoto;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "createdBy")
    private String createdBy;
    @ManyToOne
    @JoinColumn(name = "idNation")
    private Nationalite nationalite;
    @Column(name = "adressEtudiant")
    private String adressEtudiant;
    @PrePersist
    public void prePersist(){
        if(createdAt == null)
            createdAt = LocalDateTime.now();
    }
}
