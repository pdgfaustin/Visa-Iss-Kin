
package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.SexePersonne;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EnseignantRepository extends JpaRepository<Enseignant, String> {
    Optional<Enseignant> findByEmailEns(String emailEns);

    boolean existsByEmailEns(String emailEns);

    boolean existsByTelephoneEns(String telephoneEns);

    List<Enseignant> findByNomEnsContainingIgnoreCase(String nomEns);

    List<Enseignant> findByPostNomEnsContainingIgnoreCase(String postNomEns);

    List<Enseignant> findByPrenomEnsContainingIgnoreCase(String prenomEns);

    List<Enseignant> findBySexEns(SexePersonne sexEns);
}
