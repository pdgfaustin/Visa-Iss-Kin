
package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Enseignant;
import com.visa_iss_kin.model.SexePersonne;
import com.visa_iss_kin.repository.EnseignantRepository;
import com.visa_iss_kin.service.EnseignantService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Faustin PADINGANYI
 */
@Service
public class EnseignantServiceImpl implements EnseignantService {
    private final EnseignantRepository enseignantRepository;

    public EnseignantServiceImpl(
            EnseignantRepository enseignantRepository
    ) {
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    public Enseignant creerEnseignant(Enseignant enseignant) {

        if (enseignant.getMatrEns() == null
                || enseignant.getMatrEns().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le matricule de l'enseignant est obligatoire."
            );
        }

        if (enseignant.getNomEns() == null
                || enseignant.getNomEns().trim().isEmpty()) {
            throw new RuntimeException(
                    "Le nom de l'enseignant est obligatoire."
            );
        }

        if (enseignant.getSexEns() == null) {
            throw new RuntimeException(
                    "Le sexe de l'enseignant est obligatoire."
            );
        }

        if (enseignantRepository.existsById(enseignant.getMatrEns())) {
            throw new RuntimeException(
                    "Un enseignant existe déjà avec le matricule : "
                    + enseignant.getMatrEns()
            );
        }

        if (enseignant.getEmailEns() != null
                && !enseignant.getEmailEns().trim().isEmpty()
                && enseignantRepository.existsByEmailEns(
                        enseignant.getEmailEns()
                )) {
            throw new RuntimeException(
                    "Un enseignant existe déjà avec l'adresse email : "
                    + enseignant.getEmailEns()
            );
        }

        if (enseignant.getTelephoneEns() != null
                && !enseignant.getTelephoneEns().trim().isEmpty()
                && enseignantRepository.existsByTelephoneEns(
                        enseignant.getTelephoneEns()
                )) {
            throw new RuntimeException(
                    "Un enseignant existe déjà avec le numéro de téléphone : "
                    + enseignant.getTelephoneEns()
            );
        }

        if (enseignant.getCreatedAt() == null) {
            enseignant.setCreatedAt(LocalDateTime.now());
        }

        return enseignantRepository.save(enseignant);
    }

    @Override
    public List<Enseignant> listerEnseignants() {
        return enseignantRepository.findAll();
    }

    @Override
    public Enseignant rechercherEnseignantParMatricule(String matrEns) {
        return enseignantRepository.findById(matrEns)
                .orElseThrow(() -> new RuntimeException(
                        "Enseignant introuvable avec le matricule : "
                        + matrEns
                ));
    }

    @Override
    public Enseignant rechercherEnseignantParEmail(String emailEns) {
        return enseignantRepository.findByEmailEns(emailEns)
                .orElseThrow(() -> new RuntimeException(
                        "Enseignant introuvable avec l'adresse email : "
                        + emailEns
                ));
    }

    @Override
    public List<Enseignant> rechercherEnseignantsParNom(String nomEns) {
        return enseignantRepository.findByNomEnsContainingIgnoreCase(nomEns);
    }

    @Override
    public List<Enseignant> rechercherEnseignantsParPostNom(
            String postNomEns
    ) {
        return enseignantRepository
                .findByPostNomEnsContainingIgnoreCase(postNomEns);
    }

    @Override
    public List<Enseignant> rechercherEnseignantsParPrenom(
            String prenomEns
    ) {
        return enseignantRepository
                .findByPrenomEnsContainingIgnoreCase(prenomEns);
    }

    @Override
    public List<Enseignant> listerEnseignantsParSexe(
            SexePersonne sexEns
    ) {
        return enseignantRepository.findBySexEns(sexEns);
    }

    @Override
    public Enseignant modifierEnseignant(
            String matrEns,
            Enseignant enseignant
    ) {
        Enseignant enseignantExistant =
                rechercherEnseignantParMatricule(matrEns);

        if (enseignant.getNomEns() != null
                && !enseignant.getNomEns().trim().isEmpty()) {
            enseignantExistant.setNomEns(enseignant.getNomEns());
        }

        if (enseignant.getPostNomEns() != null) {
            enseignantExistant.setPostNomEns(
                    enseignant.getPostNomEns()
            );
        }

        if (enseignant.getPrenomEns() != null) {
            enseignantExistant.setPrenomEns(
                    enseignant.getPrenomEns()
            );
        }

        if (enseignant.getSexEns() != null) {
            enseignantExistant.setSexEns(enseignant.getSexEns());
        }

        if (enseignant.getEtatCivil() != null) {
            enseignantExistant.setEtatCivil(
                    enseignant.getEtatCivil()
            );
        }

        if (enseignant.getDateNaissance() != null) {
            enseignantExistant.setDateNaissance(
                    enseignant.getDateNaissance()
            );
        }

        if (enseignant.getDateEngagement() != null) {
            enseignantExistant.setDateEngagement(
                    enseignant.getDateEngagement()
            );
        }

        if (enseignant.getAdresseEns() != null) {
            enseignantExistant.setAdresseEns(
                    enseignant.getAdresseEns()
            );
        }

        if (enseignant.getTelephoneEns() != null) {
            String nouveauTelephone = enseignant.getTelephoneEns();

            if (!nouveauTelephone.equals(
                    enseignantExistant.getTelephoneEns()
            ) && enseignantRepository.existsByTelephoneEns(
                    nouveauTelephone
            )) {
                throw new RuntimeException(
                        "Un enseignant existe déjà avec le numéro de téléphone : "
                        + nouveauTelephone
                );
            }

            enseignantExistant.setTelephoneEns(nouveauTelephone);
        }

        if (enseignant.getEmailEns() != null) {
            String nouvelEmail = enseignant.getEmailEns();

            if (!nouvelEmail.equals(
                    enseignantExistant.getEmailEns()
            ) && enseignantRepository.existsByEmailEns(
                    nouvelEmail
            )) {
                throw new RuntimeException(
                        "Un enseignant existe déjà avec l'adresse email : "
                        + nouvelEmail
                );
            }

            enseignantExistant.setEmailEns(nouvelEmail);
        }

        if (enseignant.getCreatedBy() != null) {
            enseignantExistant.setCreatedBy(
                    enseignant.getCreatedBy()
            );
        }

        return enseignantRepository.save(enseignantExistant);
    }

    @Override
    public Enseignant enregistrerPhotoEnseignant(
            String matrEns,
            MultipartFile file
    ) {
        Enseignant enseignant =
                rechercherEnseignantParMatricule(matrEns);

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Le fichier photo est obligatoire."
            );
        }

        String contentType = file.getContentType();

        if (contentType == null
                || !(contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("image/jpg")
                || contentType.equalsIgnoreCase("image/png"))) {
            throw new RuntimeException(
                    "Format de photo invalide. "
                    + "Seuls les formats JPG, JPEG et PNG sont acceptés."
            );
        }

        try {
            enseignant.setPhotoEns(file.getBytes());
            enseignant.setTypePhoto(contentType);
            enseignant.setNomFichierPhoto(
                    file.getOriginalFilename()
            );

            return enseignantRepository.save(enseignant);

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Erreur lors de l'enregistrement de la photo : "
                    + exception.getMessage()
            );
        }
    }

    @Override
    public byte[] recupererPhotoEnseignant(String matrEns) {
        Enseignant enseignant =
                rechercherEnseignantParMatricule(matrEns);

        if (enseignant.getPhotoEns() == null
                || enseignant.getPhotoEns().length == 0) {
            throw new RuntimeException(
                    "Aucune photo trouvée pour cet enseignant."
            );
        }

        return enseignant.getPhotoEns();
    }

    @Override
    public void supprimerEnseignant(String matrEns) {
        Enseignant enseignant =
                rechercherEnseignantParMatricule(matrEns);

        enseignantRepository.delete(enseignant);
    }
}
