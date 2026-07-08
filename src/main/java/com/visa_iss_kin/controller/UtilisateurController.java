package com.visa_iss_kin.controller;

import com.visa_iss_kin.model.UserRole;
import com.visa_iss_kin.model.Utilisateur;
import com.visa_iss_kin.service.UtilisateurService;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Faustin PADINGANYI
 */
@RestController
@RequestMapping("/visa-iss/utilisateurs")
@CrossOrigin("*")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(
            UtilisateurService utilisateurService
    ) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    public ResponseEntity<Utilisateur> creerUtilisateur(
            @RequestBody Utilisateur utilisateur
    ) {
        return ResponseEntity.ok(
                utilisateurService.creerUtilisateur(utilisateur)
        );
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerUtilisateurs() {
        return ResponseEntity.ok(
                utilisateurService.listerUtilisateurs()
        );
    }

    @GetMapping("/{userName}")
    public ResponseEntity<Utilisateur> rechercherUtilisateurParUserName(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(
                utilisateurService.rechercherUtilisateurParUserName(
                        userName
                )
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> rechercherUtilisateurParEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(
                utilisateurService.rechercherUtilisateurParEmail(
                        email
                )
        );
    }

    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<Utilisateur> rechercherUtilisateurParTelephone(
            @PathVariable String telephone
    ) {
        return ResponseEntity.ok(
                utilisateurService.rechercherUtilisateurParTelephone(
                        telephone
                )
        );
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Utilisateur>> listerUtilisateursParRole(
            @PathVariable UserRole role
    ) {
        return ResponseEntity.ok(
                utilisateurService.listerUtilisateursParRole(role)
        );
    }

    @GetMapping("/actifs")
    public ResponseEntity<List<Utilisateur>> listerUtilisateursActifs() {
        return ResponseEntity.ok(
                utilisateurService.listerUtilisateursActifs()
        );
    }

    @GetMapping("/inactifs")
    public ResponseEntity<List<Utilisateur>> listerUtilisateursInactifs() {
        return ResponseEntity.ok(
                utilisateurService.listerUtilisateursInactifs()
        );
    }

    @GetMapping("/comptes-verrouilles")
    public ResponseEntity<List<Utilisateur>> listerComptesVerrouilles() {
        return ResponseEntity.ok(
                utilisateurService.listerComptesVerrouilles()
        );
    }

    @GetMapping("/premiere-connexion")
    public ResponseEntity<List<Utilisateur>>
            listerUtilisateursPremiereConnexion() {

        return ResponseEntity.ok(
                utilisateurService.listerUtilisateursPremiereConnexion()
        );
    }

    @GetMapping("/role/{role}/actif/{actif}")
    public ResponseEntity<List<Utilisateur>>
            listerUtilisateursParRoleEtActif(
                    @PathVariable UserRole role,
                    @PathVariable Boolean actif
            ) {

        return ResponseEntity.ok(
                utilisateurService.listerUtilisateursParRoleEtActif(
                        role,
                        actif
                )
        );
    }

    @PutMapping("/{userName}")
    public ResponseEntity<Utilisateur> modifierUtilisateur(
            @PathVariable String userName,
            @RequestBody Utilisateur utilisateur
    ) {
        return ResponseEntity.ok(
                utilisateurService.modifierUtilisateur(
                        userName,
                        utilisateur
                )
        );
    }

    @PutMapping("/{userName}/changer-mot-de-passe")
    public ResponseEntity<Utilisateur> changerMotDePasse(
            @PathVariable String userName,
            @RequestParam String ancienMotDePasse,
            @RequestParam String nouveauMotDePasse
    ) {
        return ResponseEntity.ok(
                utilisateurService.changerMotDePasse(
                        userName,
                        ancienMotDePasse,
                        nouveauMotDePasse
                )
        );
    }

    @PutMapping("/{userName}/reinitialiser-mot-de-passe")
    public ResponseEntity<Utilisateur> reinitialiserMotDePasse(
            @PathVariable String userName,
            @RequestParam String nouveauMotDePasse
    ) {
        return ResponseEntity.ok(
                utilisateurService.reinitialiserMotDePasse(
                        userName,
                        nouveauMotDePasse
                )
        );
    }

    @PutMapping("/{userName}/activer")
    public ResponseEntity<Utilisateur> activerUtilisateur(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(
                utilisateurService.activerUtilisateur(userName)
        );
    }

    @PutMapping("/{userName}/desactiver")
    public ResponseEntity<Utilisateur> desactiverUtilisateur(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(
                utilisateurService.desactiverUtilisateur(userName)
        );
    }

    @PutMapping("/{userName}/verrouiller")
    public ResponseEntity<Utilisateur> verrouillerCompte(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(
                utilisateurService.verrouillerCompte(userName)
        );
    }

    @PutMapping("/{userName}/deverrouiller")
    public ResponseEntity<Utilisateur> deverrouillerCompte(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(
                utilisateurService.deverrouillerCompte(userName)
        );
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<Void> supprimerUtilisateur(
            @PathVariable String userName
    ) {
        utilisateurService.supprimerUtilisateur(userName);

        return ResponseEntity.noContent().build();
    }
}
