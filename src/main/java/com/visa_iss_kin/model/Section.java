package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 *
 * @author Faustin PADINGANYI
 */
@lombok.Getter
@lombok.Setter
@Entity
@Table(name="Section")
public class Section {
    @Id
    @Column(name = "idSect", length = 10, nullable = false)
    private String idSect;
    @Column(name = "libeSection", length = 150, nullable = false)
    private String libeSection;
    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;
    @Column(name = "createdBy",length = 20, nullable = true)
    private String createdBy;

    public Section() {
    }

    public Section(String idSect, String libeSection, LocalDate createdAt, String createdBy) {
        this.idSect = idSect;
        this.libeSection = libeSection;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
    
    @PrePersist
    public void PrePersist(){
        if (createdAt==null) {
            createdAt = LocalDate.now();
        }
    }

}
