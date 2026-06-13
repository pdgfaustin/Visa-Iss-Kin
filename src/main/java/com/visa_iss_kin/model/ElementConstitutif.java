
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
@Entity
@Table(name = "elementConstitutif")
@lombok.Getter
@lombok.AllArgsConstructor
@lombok.Setter
public class ElementConstitutif {

    @Id
    @Column(name = "idEC", length = 20, nullable = false)
    private String idEC;

    @Column(name = "codeEC", length = 30, nullable = false)
    private String codeEC;

    @Column(name = "libeEC", length = 150, nullable = false)
    private String libeEC;

    @Column(name = "creditEC", nullable = false)
    private Integer creditEC;

    @Column(name = "nombreHeuresTheorie", nullable = false)
    private Integer nombreHeuresTheorie;

    @Column(name = "nombreHeuresPratique", nullable = false)
    private Integer nombreHeuresPratique;

    @Column(name = "descriptionEC", columnDefinition = "TEXT")
    private String descriptionEC;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "idUE", nullable = false)
    private UniteEnseignement uniteEnseignement;

    public ElementConstitutif() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (creditEC == null) {
            creditEC = 0;
        }

        if (nombreHeuresTheorie == null) {
            nombreHeuresTheorie = 0;
        }

        if (nombreHeuresPratique == null) {
            nombreHeuresPratique = 0;
        }
    }    
}
