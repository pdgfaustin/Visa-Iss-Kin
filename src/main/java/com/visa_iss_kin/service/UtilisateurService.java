package com.visa_iss_kin.service;

import com.visa_iss_kin.model.*;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface UtilisateurService {
    Utilisateur creerUtilisateur(
            Utilisateur utilisateur
    );

    List<Utilisateur> listerUtilisateurs();

    Utilisateur rechercherUtilisateurParUserName(
            String userName
    );

    Utilisateur rechercherUtilisateurParEmail(
            String email
    );

    Utilisateur rechercherUtilisateurParTelephone(
            String telephone
    );

    List<Utilisateur> listerUtilisateursParRole(
            UserRole role
    );

    List<Utilisateur> listerUtilisateursActifs();

    List<Utilisateur> listerUtilisateursInactifs();

    List<Utilisateur> listerComptesVerrouilles();

    List<Utilisateur> listerUtilisateursPremiereConnexion();

    List<Utilisateur> listerUtilisateursParRoleEtActif(
            UserRole role,
            Boolean actif
    );

    Utilisateur modifierUtilisateur(
            String userName,
            Utilisateur utilisateur
    );

    Utilisateur changerMotDePasse(
            String userName,
            String ancienMotDePasse,
            String nouveauMotDePasse
    );

    Utilisateur reinitialiserMotDePasse(
            String userName,
            String nouveauMotDePasse
    );

    Utilisateur activerUtilisateur(
            String userName
    );

    Utilisateur desactiverUtilisateur(
            String userName
    );

    Utilisateur verrouillerCompte(
            String userName
    );

    Utilisateur deverrouillerCompte(
            String userName
    );

    void supprimerUtilisateur(
            String userName
    );
}
