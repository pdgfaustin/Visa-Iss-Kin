package com.visa_iss_kin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Entity
@Table(name = "OptionSuivie")
public class OptionSuivie {
    @Id
    @Column(name = "idOpt", length = 10, nullable = false)
    private String idOpt;
    @Column(name = "libeOpt", length = 150, nullable = false)
    private String libeOpt;
    @Column(name = "createdBy", length = 25, nullable = false)
    private String createdBy;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "idSect",nullable = false)
    private Section section;

    public OptionSuivie(String idOpt, String libeOpt, String createdBy, LocalDateTime createdAt, Section section) {
        this.idOpt = idOpt;
        this.libeOpt = libeOpt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.section = section;
    }

    public OptionSuivie() {
    }
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
