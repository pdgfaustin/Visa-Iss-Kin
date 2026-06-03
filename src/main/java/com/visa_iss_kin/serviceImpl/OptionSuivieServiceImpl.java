package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.model.Section;
import com.visa_iss_kin.repository.OptionSuivieRepository;
import com.visa_iss_kin.repository.SectionRepository;
import com.visa_iss_kin.service.OptionSuivieService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class OptionSuivieServiceImpl implements OptionSuivieService {

    private final SectionRepository sectR;
    private final OptionSuivieRepository optRep;

    public OptionSuivieServiceImpl(SectionRepository sectR, OptionSuivieRepository optRep) {
        this.sectR = sectR;
        this.optRep = optRep;
    }
    
    @Override
    public OptionSuivie creerOptionSuivie(OptionSuivie optS) {
        if(optS.getIdOpt()==null || optS.getIdOpt().isBlank()) throw new RuntimeException("Le code Option ne peut pas être vide");
        if(optS.getLibeOpt() == null || optS.getLibeOpt().isBlank()) throw new RuntimeException("Le libellé d'Option doit être connu");
        if(optS.getSection() == null) throw new RuntimeException("l'Option doit appartenir à une section");
        Section sect = sectR.findById(optS.getSection().getIdSect()).orElseThrow(()->
                new RuntimeException("La section est introuvable avec l'ID : " + optS.getSection().getIdSect())
        );
        optS.setSection(sect);
        if(optS.getCreatedAt() == null){
            optS.setCreatedAt(LocalDateTime.now());
        }
        return optRep.save(optS);
    }

    @Override
    public List<OptionSuivie> listeOptionSuivie() {
        return optRep.findAll();
    }

    @Override
    public OptionSuivie chercherOptionSuivie(String idOpt) {
        return optRep.findById(idOpt).orElseThrow(()-> new RuntimeException("Option non trouvé avec l'ID : " + idOpt));
    }

    @Override
    public List<OptionSuivie> optionSuivieParSection(String sect) {
        Section Sect = sectR.findById(sect).orElseThrow(()->
                new RuntimeException("La section est introuvable avec l'ID : " + sect)
        );
        return optRep.findBySection(Sect);
    }

    @Override
    public OptionSuivie modifierOptionSuivie(String idOpt, OptionSuivie opt) {
        OptionSuivie optExist = chercherOptionSuivie(idOpt);
        if(idOpt == null || idOpt.isBlank()) throw new RuntimeException("Impossible de modifier sans l'ID option");
        if(opt.getSection() != null || opt.getSection().getIdSect() != null){
            Section sect = sectR.findById(opt.getSection().getIdSect()).orElseThrow(()->
            new RuntimeException("La section n'existe pas avec l'ID : " + opt.getSection().getIdSect())
            );
            optExist.setSection(sect);
        }
        if(optExist.getCreatedAt() == null){
            optExist.setCreatedAt(LocalDateTime.now());
        }
        if(opt.getLibeOpt() == null || opt.getLibeOpt().isBlank()) throw new RuntimeException("le libéllé Option ne peut pas être vide");
        optExist.setLibeOpt(opt.getLibeOpt());
        if(opt.getCreatedBy()==null || opt.getCreatedBy().isBlank()) throw new RuntimeException("L'initiateur d'action non connu");
        optExist.setCreatedBy(opt.getCreatedBy());
        return optRep.save(optExist);
    }

    @Override
    public void supprimerOptionSuivie(String idOpt) {
        OptionSuivie opt = optRep.findById(idOpt).orElseThrow(
                ()-> new RuntimeException("l'Option à Supprimer n'existe pas")
                );
        optRep.deleteById(opt.getIdOpt());
    }
    
}
