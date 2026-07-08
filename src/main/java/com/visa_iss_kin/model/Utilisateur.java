package com.visa_iss_kin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author Faustin PADINGANYI
 */
@Entity
@Table(name = "utilisateur")
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class Utilisateur {
    @Id
    @Column(name = "userName", length = 50)
    private String userName;

    @Column(name = "nomComplet", length = 100, nullable = false)
    private String nomComplet;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "telephone", length = 30, unique = true)
    private String telephone;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    private UserRole role;

    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @Column(name = "compteVerrouille", nullable = false)
    private Boolean compteVerrouille;

    @Column(name = "premiereConnexion", nullable = false)
    private Boolean premiereConnexion;

    @Column(name = "dateCreation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "derniereConnexion")
    private LocalDateTime derniereConnexion;

    @Column(name = "createdBy", length = 50)
    private String createdBy;

    public Utilisateur() {
    }
    
    @PrePersist
    public void prePersist() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }

        if (actif == null) {
            actif = true;
        }

        if (compteVerrouille == null) {
            compteVerrouille = false;
        }

        if (premiereConnexion == null) {
            premiereConnexion = true;
        }
    }
}
