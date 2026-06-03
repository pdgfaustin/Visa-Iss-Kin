package com.visa_iss_kin.repository;

import com.visa_iss_kin.model.Etudiant;
import com.visa_iss_kin.model.Nationalite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EtudiantRepository extends JpaRepository<Etudiant, String> {
    boolean existsByMatriculEtudiant(String matriculeEtudiant);

    boolean existsByEmailEtudiant(String emailEtudiant);

    Optional<Etudiant> findByMatriculEtudiant(String matriculeEtudiant);

    Optional<Etudiant> findByEmailEtudiant(String emailEtudiant);

    List<Etudiant> findByNationalite(Nationalite nationalite);

    List<Etudiant> findByNomEtudiantContainingIgnoreCase(String nomEtudiant);
    Optional<Etudiant> findTopByIdEtudiantStartingWithOrderByIdEtudiantDesc(String prefix);
}
