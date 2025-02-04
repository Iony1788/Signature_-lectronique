package transactions.com.controller;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;

import java.util.UUID;
import java.util.stream.Collectors;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import transactions.com.entity.InfoUser;
import transactions.com.entity.Worker;
import transactions.com.service.ApiImageTraiteService;
import transactions.com.service.ExpirationService;
import transactions.com.service.ImageService;
import transactions.com.service.InfoUserService;
import transactions.com.service.PDFService;
import transactions.com.service.WorkerService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@Controller
public class PageController {
		
	

    @Autowired
    private ImageService imageService;

    @Autowired
    private PDFService pdfService;
    
    @Autowired
    private ApiImageTraiteService apiImageTraiteSerice;
    
   
    
    @Autowired
    private ExpirationService expirationRefenrece;
    
    @Autowired
    WorkerService workerService;
    
    @Autowired
    private InfoUserService infoUserService;

    private String url_api = "http://192.168.88.134:8080/list/view/images";

     // Rediriger la racine "/" vers "/pages/uploadForm"
     @GetMapping("/")
     public String redirectToUploadForm() {
         return "redirect:/pages/uploadForm"; // Redirige vers l'autre méthode
     }
 

    @GetMapping("/pages/uploadForm")
    @CrossOrigin
    public ModelAndView pageAccueil(Model model) {
        List<Worker> workers = workerService.getAllWorkers();
        // Debug : Vérifier que la liste des travailleurs n'est pas vide
        System.out.println("Liste des travailleurs : " + workers);
        model.addAttribute("workers", workers);
        return new ModelAndView("/pages/acceuil0");
    }

    
    @GetMapping("/model")
    @CrossOrigin
    public ModelAndView model(Model model) throws Exception {
    		
           model.addAttribute("photopath","/images/splite.png");
           model.addAttribute("signature","/home/ionisoa/Images/save/Rindra.png");

        return new ModelAndView("/pages/model", model.asMap());
    }

    @GetMapping("/upload")
    public ModelAndView showUploadPage(@RequestParam(required = false) String success,
                                        @RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("pages/acceuil0");
        if (success != null) {
            modelAndView.addObject("successMessage", success);
        }
        if (error != null) {
            modelAndView.addObject("errorMessage", error);
        }
        return modelAndView;
    }
    
    @PostMapping("/uploaImageSplit")
    public ResponseEntity<String> uploaImageSplit(@RequestParam("file") MultipartFile file) throws Exception {
    	 try {
			byte[] imaqeSplite =  imageService.duplicateImageInGrid(file.getBytes(),100,100);
			  Path outputPath = Paths.get("/home/ionisoa/Images/save/splite"+".png");
		        Files.write(outputPath, imaqeSplite);
			 return ResponseEntity.ok("PDF signé et sauvegardé avec succès à l'emplacement : " + outputPath.toString());
		} catch (IOException e) {
			   e.printStackTrace();
		        String errorMessage = "Erreur lors de la signature de l'image : " + e.getMessage();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + errorMessage);
		}
    	 
    	
    }
    
       //fonction pour convertir l'uuid en utilisant SHA-256
    
       public static String toSimpleHash(String uuid) throws NoSuchAlgorithmException {
            // Créer un objet MessageDigest pour SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Calculer le hash
            byte[] hash = digest.digest(uuid.getBytes());
            
            // Convertir le hash en chaîne hexadécimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Compléter avec un zéro si nécessaire
                hexString.append(hex);
            }
            
            // Retourner les premiers 16 caractères en majuscules
            return hexString.toString().substring(0, 16).toUpperCase();
        }

    private List<String> getAvailableCINs() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String[]> response = restTemplate.getForEntity(url_api, String[].class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Extraire les CIN depuis les URLs
                return Arrays.stream(response.getBody())
                            .map(url -> extractCinFromUrl(url)) // Extraction du CIN
                            .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList(); // Retourne une liste vide en cas d'erreur
    }

    /**
     * Méthode pour extraire le numéro CIN depuis une URL.
     */
    private String extractCinFromUrl(String imageUrl) {
        // Supposons que l'URL ait un format comme :
        // "https://preprod-image.signature.eqima.org/view-image/101221059710.jpg"
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1); // Récupère "101221059710.jpg"
        return fileName.split("\\.")[0]; // Supprime l'extension ".jpg" -> "101221059710"
    }


    
           
        @PostMapping("/uploadPhoto")
        public ResponseEntity<String> uploadPhoto(@RequestParam("numeroCin") String numeroCin,   
                                                  @RequestParam("nameWorker") String user) {
            try {
                System.out.println("Starting upload process for CIN: " + numeroCin);
      
        
                // Vérifier si le CIN est vide ou trop court/long
                
                if (numeroCin == null || numeroCin.trim().isEmpty()) {
                    return generateErrorResponse("Le numero CIN est obligatoire.");
                }
                if (!numeroCin.matches("\\d+")) {
                    return generateErrorResponse("Le numero CIN ne doit contenir que des lettres.");
                }

                List<String> availableCINs = getAvailableCINs();
                System.out.println("Liste des CIN disponibles: " + availableCINs);
                if (!availableCINs.contains(numeroCin)) {
                    return generateErrorResponse("Le numero CIN saisi n'existe pas dans la liste des images disponibles.");
                }

                // Récupérer la photo
                byte[] photo = apiImageTraiteSerice.getImageUrlByCin(numeroCin);
                if (photo == null || photo.length == 0) {
                    return generateErrorResponse("Aucune photo trouvee pour ce numero CIN.");
                }
        
                // Création de l'image en grille
                byte[] imageSplite = imageService.duplicateImageInGrid(photo, 100, 100);
        
                // Création des répertoires
                String dirClient = user;
                Path clientDirPath = Paths.get("uploads/" + dirClient);
                if (!Files.exists(clientDirPath)) {
                    Files.createDirectories(clientDirPath);
                }
        
                // Génération et enregistrement des fichiers
                String uuid = UUID.randomUUID().toString();
                String simpleRef = toSimpleHash(uuid);
                Path outputPathPhotoSplite = Paths.get("uploads/" + dirClient + "/" + simpleRef + ".png");
                Files.write(outputPathPhotoSplite, imageSplite);
        
                // Conversion en PDF et signature
                byte[] pdf = pdfService.convertToPDF(photo);
                byte[] pdfimageSigne = pdfService.signPdf(pdf, user);
                String dateSignature = pdfService.ExtractDateSignature(pdfimageSigne);
        
                // Enregistrement du fichier signé
                Path outputPathPhotoSigne = Paths.get("uploads/" + dirClient + "/" + simpleRef + ".pdf");
                Files.write(outputPathPhotoSigne, pdfimageSigne);
        
                // Génération du fichier de signature
                byte[] signaturePdf = pdfService.getSignatureFromPdf(pdfimageSigne);
                byte[] signaturePhoto = pdfService.convertPdfToImage(signaturePdf);
                Path outputPathSignature = Paths.get("uploads/" + dirClient + "/signature.png");
                Files.write(outputPathSignature, signaturePhoto);
        
                // Création du PDF de fiche signature
                // String pathPhotoSplite = "https://preprod-signature.eqima.org/uploads/" + dirClient + "/" + simpleRef + ".png";
                // String pathSignature = "https://preprod-signature.eqima.org/uploads/" + dirClient + "/signature.png";

                // Création du PDF de fiche signature
                String pathPhotoSplite = "http://localhost/uploadsuploads/" + dirClient + "/" + simpleRef + ".png";
                String pathSignature = "http://localhost/uploads/" + dirClient + "/signature.png";

                String newName = pdfService.getNameWorker();
                String htmlContent = pdfService.modelFicheDeSignature(pathPhotoSplite, pathSignature, user, dateSignature, simpleRef, newName);
                String pdfFicheName = simpleRef + "fiche.pdf";
                pdfService.generatePdfAndSaveToDirectory(htmlContent, "uploads/" + dirClient, pdfFicheName);
        
                // Sauvegarde des infos utilisateur
                LocalDate expirationDate = expirationRefenrece.getExpirationDate(dateSignature);
                String uuidFileName = "uploads/" + dirClient + "/" + simpleRef + ".txt";
                String content = "Reference: " + simpleRef + "\n"
                                 + "Date: " + dateSignature + "\n"
                                 + "Client: " + dirClient + "\n"
                                 + "Date d'expiration référence: " + expirationDate + "\n";
                Files.write(Paths.get(uuidFileName), content.getBytes());
        
                InfoUser infoUser = new InfoUser();
                infoUser.setNom(user);
                infoUser.setDateSignature(dateSignature);
                infoUser.setPathFiche("uploads/" + dirClient + "/" + pdfFicheName);
                infoUser.setPathPhotoSigne("uploads/" + dirClient + "/" + simpleRef + ".pdf");
                infoUser.setReference(simpleRef);
                infoUser.setDateExpiration(expirationDate);
                infoUserService.saveUtilisateur(infoUser);
        
                // Réponse en cas de succès
                return generateSuccessResponse(user, simpleRef);
        
            } catch (Exception e) {
                e.printStackTrace();
                return generateErrorResponse("Une erreur s'est produite lors du traitement du fichier.");
            }
        }
        
        /**
         * Génère une réponse HTML en cas d'erreur.
         */
        private ResponseEntity<String> generateErrorResponse(String message) {
            String errorResponse = "<html><head><style>"
                    + "body { font-family: Arial, sans-serif; }"
                    + ".error-message { background-color: #FFBABA; color: #D8000C; padding: 20px; margin: 10px 0; border: 1px solid #D8000C; border-radius: 5px; }"
                    + "</style></head><body><div class='error-message'>"
                    + "<h2>Erreur!</h2>"
                    + "<p>" + message + "</p>"
                    + "</div></body></html>";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_HTML).body(errorResponse);
        }
        
        /**
         * Génère une réponse HTML en cas de succès.
         */
        private ResponseEntity<String> generateSuccessResponse(String user, String simpleRef) {
            String successResponse = "<html><head><style>"
                    + "body { font-family: Arial, sans-serif; }"
                    + ".success-message { background-color: #DFF2BF; color: #4F8A10; padding: 20px; margin: 10px 0; border: 1px solid #4F8A10; border-radius: 5px; }"
                    + "</style></head><body><div class='success-message'>"
                    + "<h2>Upload Reussi!</h2>"
                    + "<p>Votre fichier a ete telecharge et traite avec succès.</p>"
                    + "<p>Client: " + user + "</p>"
                    + "<p>Référence: " + simpleRef + "</p>"
                    + "<p><a href='/pages/uploadForm'>Retour au formulaire</a></p>"
                    + "</div></body></html>";
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_HTML).body(successResponse);
        }
        


}
     
       

        
       
       
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        



