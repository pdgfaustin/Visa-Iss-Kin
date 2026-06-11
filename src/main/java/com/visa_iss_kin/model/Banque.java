
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
@Table(name = "banque")
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class Banque {

    @Id
    @Column(name = "idBanque", length = 20, nullable = false)
    private String idBanque;

    @Column(name = "libeBanque", length = 150, nullable = false)
    private String libeBanque;

    @Column(name = "sigleBanque", length = 30)
    private String sigleBanque;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", length = 30)
    private String createdBy;

    public Banque() {
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }    
}
