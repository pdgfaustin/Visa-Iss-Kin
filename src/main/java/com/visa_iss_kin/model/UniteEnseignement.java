
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
@Entity
@Table(name = "uniteEnseignement")
@lombok.Getter
@lombok.Setter
public class UniteEnseignement {
    @Id
    @Column(name = "idUE", length = 20, nullable = false)
    private String idUE;

    @Column(name = "codeUE", length = 30, nullable = false)
    private String codeUE;

    @Column(name = "libeUE", length = 150, nullable = false)
    private String libeUE;

    @Column(name = "descriptionUE", columnDefinition = "TEXT")
    private String descriptionUE;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    public UniteEnseignement() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
