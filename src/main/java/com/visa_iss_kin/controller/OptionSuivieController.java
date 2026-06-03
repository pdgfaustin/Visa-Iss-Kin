package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.OptionSuivie;
import com.visa_iss_kin.service.OptionSuivieService;
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
@RequestMapping("/visa-iss/options")
@CrossOrigin("*")
public class OptionSuivieController {
    final OptionSuivieService optS;

    public OptionSuivieController(OptionSuivieService optS) {
        this.optS = optS;
    }
    @PostMapping
    public ResponseEntity<OptionSuivie> creerOptionSuivie(@RequestBody OptionSuivie opt){
        OptionSuivie optN=optS.creerOptionSuivie(opt);
        return new ResponseEntity<>(optN,HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<OptionSuivie>> toutesLesOptions(){
        List<OptionSuivie> lOpt = optS.listeOptionSuivie();
        return new ResponseEntity<>(lOpt,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{idOpt}")
    public ResponseEntity<OptionSuivie> chercherOption(@PathVariable String idOpt){
        OptionSuivie opt = optS.chercherOptionSuivie(idOpt);
        return new ResponseEntity<>(opt,HttpStatus.ACCEPTED);
    }
    @PutMapping("/{idOpt}")
    public ResponseEntity<OptionSuivie> modifierOptionSuivie(@PathVariable String idOpt, @RequestBody OptionSuivie opt){
        OptionSuivie opts = optS.modifierOptionSuivie(idOpt, opt);
        return new ResponseEntity<>(opts,HttpStatus.ACCEPTED);
    }
    @GetMapping("/section/{idSect}")
    public ResponseEntity<List<OptionSuivie>> optionsParSection(@PathVariable String idSect){
        List<OptionSuivie> opts = optS.optionSuivieParSection(idSect);
        return new ResponseEntity<>(opts,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{idOpt}")
    public ResponseEntity<Boolean> supprimerOption(@PathVariable String idOpt){
        if(idOpt == null || idOpt.isBlank()) throw new RuntimeException("l'Option à supprimer doit être renseigné");
        optS.supprimerOptionSuivie(idOpt);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
