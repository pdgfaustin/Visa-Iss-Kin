package com.visa_iss_kin.serviceImpl;

import com.visa_iss_kin.model.Actif;
import com.visa_iss_kin.model.Etudiant;
import com.visa_iss_kin.model.Nationalite;
import com.visa_iss_kin.repository.EtudiantRepository;
import com.visa_iss_kin.repository.NationaliteRepository;
import com.visa_iss_kin.service.EtudiantService;
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
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepo;
    private final NationaliteRepository nationaliteRepo;

    public EtudiantServiceImpl(
            EtudiantRepository etudiantRepo,
            NationaliteRepository nationaliteRepo
    ) {
        this.etudiantRepo = etudiantRepo;
        this.nationaliteRepo = nationaliteRepo;
    }
    @Override
    public Etudiant creerEtudiant(Etudiant etudiant) {
        if (etudiant.getIdEtudiant() == null || etudiant.getIdEtudiant().trim().isEmpty()) {
            etudiant.setIdEtudiant(genererIdEtudiant());
        }

//        if (etudiant.getMatriculEtudiant() == null || etudiant.getMatriculEtudiant().trim().isEmpty()) {
//            throw new RuntimeException("Le matricule de l'étudiant est obligatoire.");
//        }

        if (etudiant.getEmailEtudiant() == null || etudiant.getEmailEtudiant().trim().isEmpty()) {
            throw new RuntimeException("L'email de l'étudiant est obligatoire.");
        }

        if (etudiantRepo.existsById(etudiant.getIdEtudiant())) {
            throw new RuntimeException("Un étudiant existe déjà avec l'identifiant : " + etudiant.getIdEtudiant());
        }

        if (etudiantRepo.existsByMatriculEtudiant(etudiant.getMatriculEtudiant())) {
            throw new RuntimeException("Ce matricule est déjà utilisé : " + etudiant.getMatriculEtudiant());
        }

        if (etudiantRepo.existsByEmailEtudiant(etudiant.getEmailEtudiant())) {
            throw new RuntimeException("Cet email est déjà utilisé : " + etudiant.getEmailEtudiant());
        }

        if (etudiant.getNationalite() == null || etudiant.getNationalite().getIdNation() == null) {
            throw new RuntimeException("La nationalité de l'étudiant est obligatoire.");
        }

        Nationalite nationalite = nationaliteRepo.findById(
                etudiant.getNationalite().getIdNation()
        ).orElseThrow(() -> new RuntimeException(
                "Nationalité introuvable avec l'identifiant : "
                + etudiant.getNationalite().getIdNation()
        ));

        etudiant.setNationalite(nationalite);

        if (etudiant.getActif() == null) {
            etudiant.setActif(Actif.OUI);
        }

        if (etudiant.getCreatedAt() == null) {
            etudiant.setCreatedAt(LocalDateTime.now());
        }

        return etudiantRepo.save(etudiant);
    }

    @Override
    public List<Etudiant> listerEtudiants() {
        return etudiantRepo.findAll();
    }

    @Override
    public Etudiant rechercherEtudiantParId(String idEtudiant) {
        return etudiantRepo.findById(idEtudiant)
                .orElseThrow(() -> new RuntimeException(
                        "Étudiant introuvable avec l'identifiant : " + idEtudiant
                ));
    }

    @Override
    public Etudiant rechercherEtudiantParMatricule(String matriculeEtudiant) {
        return etudiantRepo.findByMatriculEtudiant(matriculeEtudiant)
                .orElseThrow(() -> new RuntimeException(
                        "Étudiant introuvable avec le matricule : " + matriculeEtudiant
                ));
    }

    @Override
    public Etudiant rechercherEtudiantParEmail(String emailEtudiant) {
        return etudiantRepo.findByEmailEtudiant(emailEtudiant)
                .orElseThrow(() -> new RuntimeException(
                        "Étudiant introuvable avec l'email : " + emailEtudiant
                ));
    }

    @Override
    public List<Etudiant> rechercherEtudiantsParNom(String nomEtudiant) {
        return etudiantRepo.findByNomEtudiantContainingIgnoreCase(nomEtudiant);
    }

    @Override
    public List<Etudiant> listeDesEtudiantsParNationalite(String idNation) {
        Nationalite nationalite = nationaliteRepo.findById(idNation)
                .orElseThrow(() -> new RuntimeException(
                        "Nationalité introuvable avec l'identifiant : " + idNation
                ));

        return etudiantRepo.findByNationalite(nationalite);
    }

    @Override
    public Etudiant modifierEtudiant(String idEtudiant, Etudiant etudiant) {
        Etudiant etudiantExistant = rechercherEtudiantParId(idEtudiant);

        if (etudiant.getMatriculEtudiant() != null
                && !etudiant.getMatriculEtudiant().equals(etudiantExistant.getMatriculEtudiant())) {

            if (etudiantRepo.existsByMatriculEtudiant(etudiant.getMatriculEtudiant())) {
                throw new RuntimeException("Ce matricule est déjà utilisé : " + etudiant.getMatriculEtudiant());
            }

            etudiantExistant.setMatriculEtudiant(etudiant.getMatriculEtudiant());
        }

        if (etudiant.getEmailEtudiant() != null
                && !etudiant.getEmailEtudiant().equals(etudiantExistant.getEmailEtudiant())) {

            if (etudiantRepo.existsByEmailEtudiant(etudiant.getEmailEtudiant())) {
                throw new RuntimeException("Cet email est déjà utilisé : " + etudiant.getEmailEtudiant());
            }

            etudiantExistant.setEmailEtudiant(etudiant.getEmailEtudiant());
        }

        if (etudiant.getNomEtudiant() != null) {
            etudiantExistant.setNomEtudiant(etudiant.getNomEtudiant());
        }

        if (etudiant.getPostNomEtudiant() != null) {
            etudiantExistant.setPostNomEtudiant(etudiant.getPostNomEtudiant());
        }

        if (etudiant.getPrenomEtudiant() != null) {
            etudiantExistant.setPrenomEtudiant(etudiant.getPrenomEtudiant());
        }

        if (etudiant.getSexEtudiant() != null) {
            etudiantExistant.setSexEtudiant(etudiant.getSexEtudiant());
        }

        if (etudiant.getDateNaissance() != null) {
            etudiantExistant.setDateNaissance(etudiant.getDateNaissance());
        }

        if (etudiant.getAdressEtudiant()!= null) {
            etudiantExistant.setAdressEtudiant(etudiant.getAdressEtudiant());
        }

        if (etudiant.getTelephonEtudiant() != null) {
            etudiantExistant.setTelephonEtudiant(etudiant.getTelephonEtudiant());
        }

        if (etudiant.getActif() != null) {
            etudiantExistant.setActif(etudiant.getActif());
        }

        if (etudiant.getCreatedBy() != null) {
            etudiantExistant.setCreatedBy(etudiant.getCreatedBy());
        }

        if (etudiant.getNationalite() != null && etudiant.getNationalite().getIdNation() != null) {
            Nationalite nationalite = nationaliteRepo.findById(
                    etudiant.getNationalite().getIdNation()
            ).orElseThrow(() -> new RuntimeException(
                    "Nationalité introuvable avec l'identifiant : "
                    + etudiant.getNationalite().getIdNation()
            ));

            etudiantExistant.setNationalite(nationalite);
        }

        if (etudiantExistant.getCreatedAt() == null) {
            etudiantExistant.setCreatedAt(LocalDateTime.now());
        }

        return etudiantRepo.save(etudiantExistant);
    }

    @Override
    public void supprimerEtudiant(String idEtudiant) {
        Etudiant etudiant = rechercherEtudiantParId(idEtudiant);
        etudiantRepo.delete(etudiant);
    }
    
    private String genererIdEtudiant() {
        String prefix = "DCCISS.";

        return etudiantRepo
                .findTopByIdEtudiantStartingWithOrderByIdEtudiantDesc(prefix)
                .map(dernierEtudiant -> {
                    String dernierId = dernierEtudiant.getIdEtudiant();

                    String partieNumerique = dernierId.substring(prefix.length());

                    int dernierNumero = Integer.parseInt(partieNumerique);

                    int nouveauNumero = dernierNumero + 1;

                    return prefix + String.format("%08d", nouveauNumero);
                })
                .orElse(prefix + "00000001");
    }

    @Override
    public Etudiant enregistrerPhotoEtudiant(String idEtudiant, MultipartFile file) {
        Etudiant etudiant = rechercherEtudiantParId(idEtudiant);

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier photo est obligatoire.");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !(contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/jpg"))) {
            throw new RuntimeException("Format photo invalide. Seuls JPG, JPEG et PNG sont acceptés.");
        }

        try {
            etudiant.setPhotoEtudiant(file.getBytes());
            etudiant.setTypePhoto(contentType);
            etudiant.setNomFichierPhoto(file.getOriginalFilename());

            return etudiantRepo.save(etudiant);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la photo : " + e.getMessage());
        }
    }

    @Override
    public byte[] recupererPhotoEtudiant(String idEtudiant) {
        Etudiant etudiant = rechercherEtudiantParId(idEtudiant);

        if (etudiant.getPhotoEtudiant() == null || etudiant.getPhotoEtudiant().length == 0) {
            throw new RuntimeException("Aucune photo trouvée pour cet étudiant.");
        }

        return etudiant.getPhotoEtudiant();
    }
}
