
package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.SexePersonne;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EnseignantService {
    Enseignant creerEnseignant(Enseignant enseignant);

    List<Enseignant> listerEnseignants();

    Enseignant rechercherEnseignantParMatricule(String matrEns);

    Enseignant rechercherEnseignantParEmail(String emailEns);

    List<Enseignant> rechercherEnseignantsParNom(String nomEns);

    List<Enseignant> rechercherEnseignantsParPostNom(String postNomEns);

    List<Enseignant> rechercherEnseignantsParPrenom(String prenomEns);

    List<Enseignant> listerEnseignantsParSexe(SexePersonne sexEns);

    Enseignant modifierEnseignant(String matrEns, Enseignant enseignant);

    Enseignant enregistrerPhotoEnseignant(
            String matrEns,
            MultipartFile file
    );

    byte[] recupererPhotoEnseignant(String matrEns);

    void supprimerEnseignant(String matrEns);
}
