package com.visa_iss_kin.security;

import com.visa_iss_kin.model.Utilisateur;
import com.visa_iss_kin.repository.UtilisateurRepository;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(
            LoginRequest loginRequest
    ) {
        verifierLoginRequest(loginRequest);

        Utilisateur utilisateur =
                utilisateurRepository
                        .findByUserName(
                                loginRequest.getUserName().trim()
                        )
                        .orElseThrow(() -> new RuntimeException(
                                "Nom d'utilisateur ou mot de passe incorrect."
                        ));

        if (utilisateur.getActif() == null
                || !utilisateur.getActif()) {
            throw new RuntimeException(
                    "Ce compte utilisateur est désactivé."
            );
        }

        if (utilisateur.getCompteVerrouille() != null
                && utilisateur.getCompteVerrouille()) {
            throw new RuntimeException(
                    "Ce compte utilisateur est verrouillé."
            );
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword().trim(),
                utilisateur.getPassword()
        )) {
            throw new RuntimeException(
                    "Nom d'utilisateur ou mot de passe incorrect."
            );
        }

        utilisateur.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);

        return new LoginResponse(
                utilisateur.getUserName(),
                utilisateur.getNomComplet(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getActif(),
                utilisateur.getCompteVerrouille(),
                utilisateur.getPremiereConnexion(),
                "Connexion réussie."
        );
    }

    private void verifierLoginRequest(
            LoginRequest loginRequest
    ) {
        if (loginRequest == null) {
            throw new RuntimeException(
                    "Les informations de connexion sont obligatoires."
            );
        }

        if (loginRequest.getUserName() == null
                || loginRequest.getUserName().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nom d'utilisateur est obligatoire."
            );
        }

        if (loginRequest.getPassword() == null
                || loginRequest.getPassword().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le mot de passe est obligatoire."
            );
        }
    }
}
