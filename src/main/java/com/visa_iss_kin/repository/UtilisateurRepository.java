package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.UserRole;
import com.visa_iss_kin.model.Utilisateur;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    Optional<Utilisateur> findByUserName(
            String userName
    );

    Optional<Utilisateur> findByEmail(
            String email
    );

    Optional<Utilisateur> findByTelephone(
            String telephone
    );

    boolean existsByUserName(
            String userName
    );

    boolean existsByEmail(
            String email
    );

    boolean existsByTelephone(
            String telephone
    );

    List<Utilisateur> findByRole(
            UserRole role
    );

    List<Utilisateur> findByActif(
            Boolean actif
    );

    List<Utilisateur> findByCompteVerrouille(
            Boolean compteVerrouille
    );

    List<Utilisateur> findByPremiereConnexion(
            Boolean premiereConnexion
    );

    List<Utilisateur> findByRoleAndActif(
            UserRole role,
            Boolean actif
    );
}
