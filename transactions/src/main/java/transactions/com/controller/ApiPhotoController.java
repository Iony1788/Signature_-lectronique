//package transactions.com.controller;
//
//import java.util.Base64;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import transactions.com.entity.InfoUser;
//import transactions.com.service.InfoUserService;
//
//@RestController
//@RequestMapping("/api")
//@Api(value = "API pour la gestion des photos d'utilisateur")
//public class ApiPhotoController {
//
//    private static final Logger logger = LoggerFactory.getLogger(ApiPhotoController.class);
//
//    @Autowired
//    private InfoUserService infoUserService;
//
//    public static byte[] readFileAsBytes(String filePath) throws IOException {
//        return Files.readAllBytes(Paths.get(filePath));
//    }
//
//    @GetMapping("/photos/{reference}")
//    @ApiOperation(value = "Obtenir les photos par référence", 
//                  notes = "Retourne les photos de l'utilisateur encodées en Base64.")
//    public ResponseEntity<List<String>> getPhotosByReference(
//            @ApiParam(value = "Référence de l'utilisateur", required = true) 
//            @PathVariable String reference) {
//
//        InfoUser infoUser = infoUserService.findByReference(reference);
//
//        if (infoUser == null) {
//            logger.warn("Aucun utilisateur trouvé avec la référence : {}", reference);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        List<String> photosBase64 = new ArrayList<>();
//
//        try {
//            // Lire les fichiers en tant que byte[]
//            byte[] userPhoto = readFileAsBytes("uploads/" + infoUser.getNom() + "/" + infoUser.getReference() + ".png");
//            byte[] signaturePhoto = readFileAsBytes("uploads/" + infoUser.getNom() + "/signature.png");
//
//            // Encoder les fichiers en Base64
//            photosBase64.add(Base64.getEncoder().encodeToString(userPhoto));
//            photosBase64.add(Base64.getEncoder().encodeToString(signaturePhoto));
//
//            return new ResponseEntity<>(photosBase64, HttpStatus.OK);
//
//        } catch (IOException e) {
//            logger.error("Erreur lors de la lecture des fichiers pour la référence : {}", reference, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
//

package transactions.com.controller;

import java.util.Base64;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import transactions.com.entity.InfoUser;
import transactions.com.service.InfoUserService;

@RestController
@RequestMapping("/api")
@Api(value = "API pour la gestion des photos d'utilisateur")
public class ApiPhotoController {

    private static final Logger logger = LoggerFactory.getLogger(ApiPhotoController.class);

    @Autowired
    private InfoUserService infoUserService;

    public static byte[] readFileAsBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }

    @GetMapping("/photos/{reference}")
    @ApiOperation(value = "Obtenir les photos et informations par référence", 
                  notes = "Retourne les photos de l'utilisateur encodées en Base64, ainsi que le nom et la date de signature.")
    public ResponseEntity<Map<String, Object>> getPhotosByReference(
            @ApiParam(value = "Référence de l'utilisateur", required = true) 
            @PathVariable String reference) {

        InfoUser infoUser = infoUserService.findByReference(reference);

        if (infoUser == null) {
            logger.warn("Aucun utilisateur trouvé avec la référence : {}", reference);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            // Lire les fichiers en tant que byte[]
            byte[] userPhoto = readFileAsBytes("uploads/" + infoUser.getNom() + "/" + infoUser.getReference() + ".png");
            byte[] signaturePhoto = readFileAsBytes("uploads/" + infoUser.getNom() + "/signature.png");

            // Encoder les fichiers en Base64
            String userPhotoBase64 = Base64.getEncoder().encodeToString(userPhoto);
            String signaturePhotoBase64 = Base64.getEncoder().encodeToString(signaturePhoto);

            // Ajouter les photos en Base64, le nom et la date de signature à la réponse
            response.put("name", infoUser.getNom());
            response.put("dateSignature", infoUser.getDateSignature());
            response.put("photoIdentite", userPhotoBase64);
            response.put("signatureImage", signaturePhotoBase64);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("Erreur lors de la lecture des fichiers pour la référence : {}", reference, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


