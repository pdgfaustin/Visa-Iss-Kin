package com.visa_iss_kin.service;

import com.visa_iss_kin.model.Etudiant;
import java.util.List;

/**
 *
 * @author Faustin PADINGANYI
 */
public interface EtudiantService {
    Etudiant creerEtudiant(Etudiant etudiant);

    List<Etudiant> listerEtudiants();

    Etudiant rechercherEtudiantParId(String idEtudiant);

    Etudiant rechercherEtudiantParMatricule(String matriculeEtudiant);

    Etudiant rechercherEtudiantParEmail(String emailEtudiant);

    List<Etudiant> rechercherEtudiantsParNom(String nomEtudiant);

    List<Etudiant> listeDesEtudiantsParNationalite(String idNation);

    Etudiant modifierEtudiant(String idEtudiant, Etudiant etudiant);

    void supprimerEtudiant(String idEtudiant);
    
    Etudiant enregistrerPhotoEtudiant(String idEtudiant, org.springframework.web.multipart.MultipartFile file);

    byte[] recupererPhotoEtudiant(String idEtudiant);
}
