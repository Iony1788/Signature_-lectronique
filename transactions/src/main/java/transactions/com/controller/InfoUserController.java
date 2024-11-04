package transactions.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File; // Import File class
import java.io.IOException; // Import IOException
import java.nio.file.Files; // Import Files class
import java.nio.file.Path; // Import Path class
import java.nio.file.Paths; // Import Paths class


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import java.util.List;
import java.util.Optional;


import transactions.com.service.InfoUserService;
import transactions.com.entity.InfoUser;


//@RestController
@RequestMapping("/api/infoUsers")
@Api(value = "Gestion des utilisateurs", tags = "Utilisateurs")  // Annotation Swagger pour décrire l'API
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
    
    

  
}

