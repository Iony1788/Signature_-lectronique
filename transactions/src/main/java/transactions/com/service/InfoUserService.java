package transactions.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; 

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import transactions.com.entity.InfoUser;
import transactions.com.repository.InfoUserRepository;


@Service
public class InfoUserService {

    @Autowired
    private InfoUserRepository infoUserRepository;
    
    @PersistenceContext
    private EntityManager entityManager;


    // Méthode pour sauvegarder un utilisateur
    public InfoUser saveUtilisateur(InfoUser infoUse) {
        return infoUserRepository.save(infoUse);
    }

    // Méthode pour récupérer tous les utilisateurs
    public List<InfoUser> getAllUtilisateurs() {
        return infoUserRepository.findAll(); 
    }

    // Méthode pour récupérer un utilisateur par son ID
    public Optional<InfoUser> getUtilisateurById(Long id) {
        return infoUserRepository.findById(id);
    }

 

    // Méthode pour supprimer un utilisateur
    public void deleteUtilisateur(Long id) {
        if (infoUserRepository.existsById(id)) {
            infoUserRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Utilisateur non trouvé");
        }
    }

    // Méthode pour vérifier si un utilisateur existe
    public boolean utilisateurExists(Long id) {
        return infoUserRepository.existsById(id);
    }
    
    

    public boolean referenceExists(String reference) {
        return infoUserRepository.findByReference(reference) != null;
    }

    public InfoUser findByReference(String reference) {
       return infoUserRepository.findByReference(reference);
    }

    public List<InfoUser> getAllInfoUsers() {
        return infoUserRepository.findAll();
    }

    

 
}
