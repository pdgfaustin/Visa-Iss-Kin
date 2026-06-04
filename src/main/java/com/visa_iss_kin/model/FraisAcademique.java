
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
@Table(name = "fraisAcademique")
@lombok.AllArgsConstructor
@lombok.Getter
@lombok.Setter
public class FraisAcademique {
    @Id
    @Column(name = "idFrais", length = 10, nullable = false)
    private String idFrais;

    @Column(name = "libeFrais", length = 150, nullable = false)
    private String libeFrais;

    @Column(name = "descriptionFrais", columnDefinition = "TEXT")
    private String descriptionFrais;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    public FraisAcademique() {
    }
    
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
