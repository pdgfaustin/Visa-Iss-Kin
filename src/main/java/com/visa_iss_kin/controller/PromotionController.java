package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.NiveauPromotion;
import com.visa_iss_kin.model.Promotion;
import com.visa_iss_kin.service.PromotionService;
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
@RequestMapping("/visa-iss/promotions")
@CrossOrigin("*")
public class PromotionController {
    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<Promotion> creerPromotion(@RequestBody Promotion promotion) {
        Promotion nouvellePromotion = promotionService.creerPromotion(promotion);
        return new ResponseEntity<>(nouvellePromotion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> listerPromotions() {
        return ResponseEntity.ok(promotionService.listeDesPromotions());
    }

    @GetMapping("/{idPro}")
    public ResponseEntity<Promotion> rechercherPromotionParId(@PathVariable String idPro) {
        return ResponseEntity.ok(promotionService.rechercherPromotionParId(idPro));
    }

    @GetMapping("/option/{idOpt}")
    public ResponseEntity<List<Promotion>> listerPromotionsParOption(@PathVariable String idOpt) {
        return ResponseEntity.ok(promotionService.listeDesPromotionsParOption(idOpt));
    }

    @GetMapping("/niveau/{niveauPromotion}")
    public ResponseEntity<List<Promotion>> listerPromotionsParNiveau(
            @PathVariable NiveauPromotion niveauPromotion
    ) {
        return ResponseEntity.ok(promotionService.listeDesPromotionsParNiveau(niveauPromotion));
    }

    @PutMapping("/{idPro}")
    public ResponseEntity<Promotion> modifierPromotion(
            @PathVariable String idPro,
            @RequestBody Promotion promotion
    ) {
        return ResponseEntity.ok(promotionService.modifierPromotion(idPro, promotion));
    }

    @DeleteMapping("/{idPro}")
    public ResponseEntity<Void> supprimerPromotion(@PathVariable String idPro) {
        promotionService.supprimerPromotion(idPro);
        return ResponseEntity.noContent().build();
    }
}
