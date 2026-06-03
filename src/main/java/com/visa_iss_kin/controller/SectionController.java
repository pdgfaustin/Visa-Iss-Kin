package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.Section;
import com.visa_iss_kin.service.SectionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/sections")
@CrossOrigin("*")
public class SectionController {
    public final SectionService sectionImpl;
    public SectionController( SectionService sectionImpl){
        this.sectionImpl = sectionImpl;
    }
    @PostMapping
    public ResponseEntity<Section> creerSection(@RequestBody Section sect){
        Section nvllSection = sectionImpl.creerSection(sect);
        return new ResponseEntity<>(nvllSection,HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Section>> listeSections(){
        List<Section> lstSect = sectionImpl.listerSection();
        return new ResponseEntity<>(lstSect, HttpStatus.ACCEPTED);
    }
    @GetMapping("/{idSect}")
    public ResponseEntity<Section> sectionById(@PathVariable String idSect){
        Section sect = sectionImpl.chercherParId(idSect);
        return new ResponseEntity(sect,HttpStatus.ACCEPTED);
    }
    @PutMapping("/{idSect}")
    public ResponseEntity<Section> editSection(@PathVariable String idSect, @RequestBody Section sect){
        Section sec = sectionImpl.chercherParId(idSect);
        sec.setLibeSection(sect.getLibeSection());
        sec.setCreatedBy(sec.getCreatedBy());
        if (sect.getCreatedAt() != null) {
            sec.setCreatedAt(sect.getCreatedAt());
        }
        if(sec.getCreatedAt() == null){
            sec.setCreatedAt(java.time.LocalDate.now());
        }
        sec = sectionImpl.modifierSection(idSect, sec);
        return new ResponseEntity<>(sec,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{idSect}")
    public ResponseEntity<Boolean> deleteSection(@PathVariable String idSect){
        if (idSect != null || !idSect.isBlank()) {
            Section sec = sectionImpl.chercherParId(idSect);
            if(sec.getIdSect() != null){
                sectionImpl.supprimerSection(idSect);
            }
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
