package transactions.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.ui.Model;
import transactions.com.service.InfoUserService;
import transactions.com.entity.InfoUser;
import transactions.com.repository.InfoUserRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;



//@RestController
@RestController
@CrossOrigin
@Controller
public class InfoUserController {
    
    @Autowired
    private InfoUserService infoUserService;
    
    @GetMapping
    @ApiOperation(value = "Récupérer tous les utilisateurs", notes = "Cette méthode permet de récupérer tous les utilisateurs enregistrés")  // Description Swagger
    public ResponseEntity<List<InfoUser>> getAllUsers() {
        List<InfoUser> users = infoUserService.getAllUtilisateurs();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Récupérer un utilisateur par ID", notes = "Cette méthode permet de récupérer un utilisateur spécifique par son ID")
    public ResponseEntity<InfoUser> getUserById(@PathVariable Long id) {
        Optional<InfoUser> user = infoUserService.getUtilisateurById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Ajouter un nouvel utilisateur", notes = "Cette méthode permet d'ajouter un nouvel utilisateur")
    public ResponseEntity<InfoUser> createUser(@RequestBody InfoUser infoUser) {
        InfoUser savedUser = infoUserService.saveUtilisateur(infoUser);
        return ResponseEntity.status(201).body(savedUser);  // Renvoie le statut 201 Created
    }

   

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Supprimer un utilisateur", notes = "Cette méthode permet de supprimer un utilisateur par son ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        infoUserService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();  // Renvoie le statut 204 No Content
    }



    @GetMapping("/exists/{id}")
    @ApiOperation(value = "Vérifier si un utilisateur existe", notes = "Cette méthode permet de vérifier si un utilisateur existe par son ID")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {


    	
        boolean exists = infoUserService.utilisateurExists(id);
        return ResponseEntity.ok(exists);
    }

    @Autowired
    private InfoUserRepository infoUserRepository;

    @GetMapping("/pages/show_images")
    public String showImages(Model model) {
        List<InfoUser> users = infoUserRepository.findAll();
        model.addAttribute("workers", users);
        return "/pages/show_images"; // Chemin vers la vue
    }

    
//     private static final String FILE_BASE_PATH = "uploads/";
//      // Récupérer toutes les photos
//     @GetMapping("/photos")
//     public ResponseEntity<List<String>> getAllPhotos() {
//         List<InfoUser> infoUsers = infoUserService.getAllInfoUsers();
//         // Extraire les chemins des photos
//         List<String> photoPaths = infoUsers.stream()
//                 .map(infoUser -> FILE_BASE_PATH + infoUser.getPathPhotoSigne())
//                 .collect(Collection.toList());

//         return ResponseEntity.ok(photoPaths);
//     }

}




    // @CrossOrigin
    // @GetMapping("/pages/show_images")
    // public String showImages(Model model) {
    //     List<InfoUser> users = infoUserRepository.findAll();
    //     model.addAttribute("workers", users);
    //     return "/pages/show_images"; // Chemin vers la vue
    // }

    


    // @GetMapping("/{id}")
    // public ResponseEntity<Resource> getPdf(@PathVariable Long id) {
    //     Optional<InfoUser> infoUser = infoUserRepository.findById(id);
    //     if (infoUser.isEmpty()) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     try {
    //         Path path = Paths.get("src/main/resources/static/" + infoUser.get().getPathFiche());
    //         Resource resource = new UrlResource(path.toUri());

    //         if (resource.exists() || resource.isReadable()) {
    //             return ResponseEntity.ok()
    //                     .contentType(MediaType.APPLICATION_PDF)
    //                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName() + "\"")
    //                     .body(resource);
    //         } else {
    //             return ResponseEntity.notFound().build();
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    //     }
    // }

    
    

    // Récupérer une photo spécifique
    // @GetMapping("/photo")
    // public ResponseEntity<Resource> getPhoto(@RequestParam("reference") String reference) throws IOException {
    //     // Trouver l'utilisateur avec la référence donnée
    //     InfoUser infoUser = infoUserService.getAllInfoUsers().stream()
    //             .filter(user -> user.getReference().equals(reference))
    //             .findFirst()
    //             .orElseThrow(() -> new RuntimeException("Photo not found"));

    //     // Construire le chemin absolu du fichier photo
    //     Path photoPath = Paths.get(FILE_BASE_PATH + infoUser.getPathPhotoSigne());
    //     Resource resource = new UrlResource(photoPath.toUri());

    //     if (resource.exists() || resource.isReadable()) {
    //         return ResponseEntity.ok(resource);
    //     } else {
    //         throw new RuntimeException("Could not read the file.");
    //     }
    // }
  


