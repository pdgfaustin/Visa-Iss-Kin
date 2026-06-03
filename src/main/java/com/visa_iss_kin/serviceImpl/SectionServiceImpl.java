package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Section;
import com.visa_iss_kin.repository.SectionRepository;
import com.visa_iss_kin.service.SectionService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class SectionServiceImpl implements SectionService {
    
    private final SectionRepository sectRep;
    public SectionServiceImpl(SectionRepository sectRep){
        this.sectRep = sectRep;
    }
    @Override
    public Section creerSection(Section sect) {
        if(sect == null ) return null;
        if(sect.getIdSect() == null || sect.getIdSect().trim().isEmpty()) throw new RuntimeException("le ID de la Section ne peut pas être vide");
        return sectRep.save(sect);
    }

    @Override
    public List<Section> listerSection() {
        return sectRep.findAll();
    }

    @Override
    public Section chercherParId(String idSect) {
        return sectRep.findById(idSect).orElseThrow(()-> new RuntimeException("Section Inexistante"));
    }

    @Override
    public Section modifierSection(String idSect, Section sect) {
        Section existante = sectRep.findById(idSect).orElseThrow(()-> new RuntimeException("La Section à modifier doit d'abord exister"));
        existante.setLibeSection(sect.getLibeSection());
        existante.setIdSect(idSect);
        existante.setCreatedAt(sect.getCreatedAt());
        existante.setCreatedBy(sect.getCreatedBy());
        return sectRep.save(existante);
    }

    @Override
    public void supprimerSection(String idSect) {
        sectRep.deleteById(idSect);
    }
    
}
