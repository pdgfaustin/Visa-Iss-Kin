package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.UserRole;
import com.visa_iss_kin.model.Utilisateur;
import com.visa_iss_kin.repository.UtilisateurRepository;
import com.visa_iss_kin.service.UtilisateurService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    public UtilisateurServiceImpl(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur creerUtilisateur(
            Utilisateur utilisateur
    ) {
        verifierUtilisateurValide(utilisateur);

        String userName = utilisateur.getUserName().trim();

        if (utilisateurRepository.existsByUserName(userName)) {
            throw new RuntimeException(
                    "Ce nom d'utilisateur existe déjà : " + userName
            );
        }

        verifierEmailUnique(utilisateur.getEmail(), null);
        verifierTelephoneUnique(utilisateur.getTelephone(), null);

        utilisateur.setUserName(userName);
        utilisateur.setNomComplet(
                utilisateur.getNomComplet().trim()
        );

        if (utilisateur.getEmail() != null
                && !utilisateur.getEmail().trim().isEmpty()) {
            utilisateur.setEmail(
                    utilisateur.getEmail().trim().toLowerCase()
            );
        }

        if (utilisateur.getTelephone() != null
                && !utilisateur.getTelephone().trim().isEmpty()) {
            utilisateur.setTelephone(
                    utilisateur.getTelephone().trim()
            );
        }

        if (utilisateur.getPassword() == null
                || utilisateur.getPassword().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le mot de passe est obligatoire."
            );
        }

        utilisateur.setPassword(
                passwordEncoder.encode(utilisateur.getPassword().trim())
        );

        utilisateur.setDateCreation(LocalDateTime.now());

        if (utilisateur.getActif() == null) {
            utilisateur.setActif(true);
        }

        if (utilisateur.getCompteVerrouille() == null) {
            utilisateur.setCompteVerrouille(false);
        }

        if (utilisateur.getPremiereConnexion() == null) {
            utilisateur.setPremiereConnexion(true);
        }

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur rechercherUtilisateurParUserName(
            String userName
    ) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nom d'utilisateur est obligatoire."
            );
        }

        return utilisateurRepository.findByUserName(userName.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable avec le nom : "
                        + userName
                ));
    }

    @Override
    public Utilisateur rechercherUtilisateurParEmail(
            String email
    ) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException(
                    "L'adresse email est obligatoire."
            );
        }

        return utilisateurRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable avec l'email : "
                        + email
                ));
    }

    @Override
    public Utilisateur rechercherUtilisateurParTelephone(
            String telephone
    ) {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le téléphone est obligatoire."
            );
        }

        return utilisateurRepository
                .findByTelephone(telephone.trim())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable avec le téléphone : "
                        + telephone
                ));
    }

    @Override
    public List<Utilisateur> listerUtilisateursParRole(
            UserRole role
    ) {
        if (role == null) {
            throw new RuntimeException(
                    "Le rôle est obligatoire."
            );
        }

        return utilisateurRepository.findByRole(role);
    }

    @Override
    public List<Utilisateur> listerUtilisateursActifs() {
        return utilisateurRepository.findByActif(true);
    }

    @Override
    public List<Utilisateur> listerUtilisateursInactifs() {
        return utilisateurRepository.findByActif(false);
    }

    @Override
    public List<Utilisateur> listerComptesVerrouilles() {
        return utilisateurRepository.findByCompteVerrouille(true);
    }

    @Override
    public List<Utilisateur> listerUtilisateursPremiereConnexion() {
        return utilisateurRepository.findByPremiereConnexion(true);
    }

    @Override
    public List<Utilisateur> listerUtilisateursParRoleEtActif(
            UserRole role,
            Boolean actif
    ) {
        if (role == null) {
            throw new RuntimeException(
                    "Le rôle est obligatoire."
            );
        }

        if (actif == null) {
            throw new RuntimeException(
                    "L'état actif/inactif est obligatoire."
            );
        }

        return utilisateurRepository.findByRoleAndActif(role, actif);
    }

    @Override
    public Utilisateur modifierUtilisateur(
            String userName,
            Utilisateur utilisateur
    ) {
        Utilisateur utilisateurExistant =
                rechercherUtilisateurParUserName(userName);

        if (utilisateur == null) {
            throw new RuntimeException(
                    "Les informations de l'utilisateur sont obligatoires."
            );
        }

        if (utilisateur.getNomComplet() != null
                && !utilisateur.getNomComplet().trim().isEmpty()) {
            utilisateurExistant.setNomComplet(
                    utilisateur.getNomComplet().trim()
            );
        }

        if (utilisateur.getEmail() != null
                && !utilisateur.getEmail().trim().isEmpty()) {
            String nouvelEmail =
                    utilisateur.getEmail().trim().toLowerCase();

            verifierEmailUnique(
                    nouvelEmail,
                    utilisateurExistant.getUserName()
            );

            utilisateurExistant.setEmail(nouvelEmail);
        }

        if (utilisateur.getTelephone() != null
                && !utilisateur.getTelephone().trim().isEmpty()) {
            String nouveauTelephone =
                    utilisateur.getTelephone().trim();

            verifierTelephoneUnique(
                    nouveauTelephone,
                    utilisateurExistant.getUserName()
            );

            utilisateurExistant.setTelephone(nouveauTelephone);
        }

        if (utilisateur.getRole() != null) {
            utilisateurExistant.setRole(utilisateur.getRole());
        }

        if (utilisateur.getActif() != null) {
            utilisateurExistant.setActif(utilisateur.getActif());
        }

        if (utilisateur.getCompteVerrouille() != null) {
            utilisateurExistant.setCompteVerrouille(
                    utilisateur.getCompteVerrouille()
            );
        }

        if (utilisateur.getPremiereConnexion() != null) {
            utilisateurExistant.setPremiereConnexion(
                    utilisateur.getPremiereConnexion()
            );
        }

        return utilisateurRepository.save(utilisateurExistant);
    }

    @Override
    public Utilisateur changerMotDePasse(
            String userName,
            String ancienMotDePasse,
            String nouveauMotDePasse
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        if (!passwordEncoder.matches(
                ancienMotDePasse.trim(),
                utilisateur.getPassword()
        )) {
            throw new RuntimeException(
                    "L'ancien mot de passe est incorrect."
            );
        }

        if (!utilisateur.getPassword().equals(ancienMotDePasse.trim())) {
            throw new RuntimeException(
                    "L'ancien mot de passe est incorrect."
            );
        }

        verifierNouveauMotDePasse(nouveauMotDePasse);

        utilisateur.setPassword(
                passwordEncoder.encode(
                        nouveauMotDePasse.trim()
                )
        );
        utilisateur.setPremiereConnexion(false);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur reinitialiserMotDePasse(
            String userName,
            String nouveauMotDePasse
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        verifierNouveauMotDePasse(nouveauMotDePasse);

        utilisateur.setPassword(
                passwordEncoder.encode(
                        nouveauMotDePasse.trim()
                )
        );
        utilisateur.setPremiereConnexion(true);
        utilisateur.setCompteVerrouille(false);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur activerUtilisateur(
            String userName
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur desactiverUtilisateur(
            String userName
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        utilisateur.setActif(false);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur verrouillerCompte(
            String userName
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        utilisateur.setCompteVerrouille(true);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur deverrouillerCompte(
            String userName
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        utilisateur.setCompteVerrouille(false);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateur(
            String userName
    ) {
        Utilisateur utilisateur =
                rechercherUtilisateurParUserName(userName);

        utilisateurRepository.delete(utilisateur);
    }

    private void verifierUtilisateurValide(
            Utilisateur utilisateur
    ) {
        if (utilisateur == null) {
            throw new RuntimeException(
                    "L'utilisateur est obligatoire."
            );
        }

        if (utilisateur.getUserName() == null
                || utilisateur.getUserName().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nom d'utilisateur est obligatoire."
            );
        }

        if (utilisateur.getNomComplet() == null
                || utilisateur.getNomComplet().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nom complet est obligatoire."
            );
        }

        if (utilisateur.getRole() == null) {
            throw new RuntimeException(
                    "Le rôle de l'utilisateur est obligatoire."
            );
        }
    }

    private void verifierEmailUnique(
            String email,
            String userNameActuel
    ) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        utilisateurRepository.findByEmail(email.trim().toLowerCase())
                .ifPresent(utilisateur -> {
                    if (userNameActuel == null
                            || !utilisateur.getUserName()
                                    .equals(userNameActuel)) {
                        throw new RuntimeException(
                                "Cette adresse email est déjà utilisée : "
                                + email
                        );
                    }
                });
    }

    private void verifierTelephoneUnique(
            String telephone,
            String userNameActuel
    ) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return;
        }

        utilisateurRepository.findByTelephone(telephone.trim())
                .ifPresent(utilisateur -> {
                    if (userNameActuel == null
                            || !utilisateur.getUserName()
                                    .equals(userNameActuel)) {
                        throw new RuntimeException(
                                "Ce numéro de téléphone est déjà utilisé : "
                                + telephone
                        );
                    }
                });
    }

    private void verifierNouveauMotDePasse(
            String nouveauMotDePasse
    ) {
        if (nouveauMotDePasse == null
                || nouveauMotDePasse.trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nouveau mot de passe est obligatoire."
            );
        }

        if (nouveauMotDePasse.trim().length() < 6) {
            throw new RuntimeException(
                    "Le mot de passe doit contenir au moins 6 caractères."
            );
        }
    }
}
