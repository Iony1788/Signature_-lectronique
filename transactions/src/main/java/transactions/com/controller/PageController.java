package transactions.com.controller;
import java.util.List;
import org.springframework.http.MediaType;

import java.util.UUID;
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

//import com.itextpdf.styledxmlparser.css.media.MediaType;

import transactions.com.entity.InfoUser;
import transactions.com.entity.Worker;
import transactions.com.service.ApiImageTraiteService;
import transactions.com.service.ExpirationService;
import transactions.com.service.ImageService;
import transactions.com.service.InfoUserService;
import transactions.com.service.PDFService;
import transactions.com.service.WorkerService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;


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
    
    
    // @Autowired
	// private RestTemplate restTemplate;
    
    @Autowired
    private InfoUserService infoUserService;

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
    

//       @PostMapping("/uploadPhoto")
//       public ResponseEntity<String> uploadPhoto(@RequestParam("numeroCin") String numeroCin ,   
//                                                 @RequestParam("nameWorker") String user) throws Exception {
//           try {
//        	   
//        	  
//               // Vos traitements (gérer l'upload, la conversion en PDF, etc.)
//               System.out.println("Erotraq  : " + numeroCin);
//               byte[] photo = apiImageTraiteSerice.getImageUrlByCin(numeroCin);
//               byte[] imageSplite = imageService.duplicateImageInGrid(photo, 100, 100);
//               String dirClient = user;
//               Path clientDirPath = Paths.get("uploads/" + dirClient);
//               String uuid = UUID.randomUUID().toString();
//               String simpleRef = toSimpleHash(uuid);
//
//               if (!Files.exists(clientDirPath)) {
//                   Files.createDirectories(clientDirPath);
//               }
//               System.out.println("tokony mety   : ");
//
//               byte[] pdf = pdfService.convertToPDF(photo);
//               byte[] pdfimageSigne = pdfService.signPdf(pdf, user);
//               String dateSignature = pdfService.ExtractDateSignature(pdfimageSigne);
//               String photoSpliteName = simpleRef + ".png";
//               Path outputPathPhotoSplite = Paths.get("uploads/" + dirClient + "/" + photoSpliteName);
//               Files.write(outputPathPhotoSplite, imageSplite);
//               String imagePdfSigneName = simpleRef + ".pdf";
//               Path outputPathPhotoSigne = Paths.get("uploads/" + dirClient + "/" + imagePdfSigneName);
//               Files.write(outputPathPhotoSigne, pdfimageSigne);
//               byte[] signaturePdf = pdfService.getSignatureFromPdf(pdfimageSigne);
//               byte[] signaturePhoto = pdfService.convertPdfToImage(signaturePdf);
//               Path outputPathSignature = Paths.get("uploads/" + dirClient + "/signature.png");
//               Files.write(outputPathSignature, signaturePhoto);
//
//               // Construction du chemin des images et PDF
//               String pathPhotoSplite = "http://localhost:8080/uploads/" + dirClient + "/" + photoSpliteName;
//               String pathSignature = "http://localhost:8080/uploads/" + dirClient + "/signature.png";
//
//               // Génération du PDF de fiche de signature
//               String newName = pdfService.getNameWorker();
//               String htmlContent = pdfService.modelFicheDeSignature(pathPhotoSplite, pathSignature, user, dateSignature, simpleRef, newName);
//               String pdfFicheName = simpleRef + "fiche.pdf";
//               String pathFicheSignature = "uploads/" + dirClient;
//               pdfService.generatePdfAndSaveToDirectory(htmlContent, pathFicheSignature, pdfFicheName);
//
//               // Enregistrement de l'info utilisateur
//               InfoUser infoUser = new InfoUser();
//               infoUser.setNom(user);
//               infoUser.setDateSignature(dateSignature);
//               infoUser.setPathFiche(pathFicheSignature + "/" + pdfFicheName);
//               infoUser.setPathPhotoSigne(pathFicheSignature + "/" + imagePdfSigneName);
//               infoUser.setReference(simpleRef);
//               infoUserService.saveUtilisateur(infoUser);
//
//               LocalDate expirationDate = expirationRefenrece.getExpirationDate(dateSignature);
//               String uuidFileName = "uploads/" + dirClient + "/" + simpleRef + ".txt";
//               String content = "Reference: " + simpleRef + "\n"
//                       + "Date: " + dateSignature + "\n"
//                       + "Client: " + dirClient + "\n"
//                       + "Date d'expiration référence: " + expirationDate + "\n";
//               Path uuidFilePath = Paths.get(uuidFileName);
//               Files.write(uuidFilePath, content.getBytes());
//
//               // Construction de la réponse HTML avec du CSS intégré
//               String htmlResponse = "<html>"
//                       + "<head>"
//                       + "<style>"
//                       + "body { font-family: Arial, sans-serif; }"
//                       + ".success-message { background-color: #DFF2BF; color: #4F8A10; padding: 20px; margin: 10px 0; border: 1px solid #4F8A10; border-radius: 5px; }"
//                       + "</style>"
//                       + "</head>"
//                       + "<body>"
//                       + "<div class='success-message'>"
//                       + "<h2>Upload Successful!</h2>"
//                       + "<p>Your file has been uploaded and processed successfully.</p>"
//                       + "<p>Client: " + user + "</p>"
//                       + "<p>Reference: " + simpleRef + "</p>"
//                       + "<p><a href='/pages/uploadForm'>Return to Form</a></p>"
//                       + "</div>"
//                       + "</body>"
//                       + "</html>";
//
//               // Retourner la réponse HTML avec un statut HTTP 200 (OK)
//               return ResponseEntity.status(HttpStatus.OK)
//                       .contentType(MediaType.TEXT_HTML)  // Utiliser MediaType.TEXT_HTML
//                       .body(htmlResponse);
//
//           } catch (IOException e) {
//               // Gestion de l'erreur
//               String errorResponse = "<html>"
//                       + "<head>"
//                       + "<style>"
//                       + "body { font-family: Arial, sans-serif; }"
//                       + ".error-message { background-color: #FFBABA; color: #D8000C; padding: 20px; margin: 10px 0; border: 1px solid #D8000C; border-radius: 5px; }"
//                       + "</style>"
//                       + "</head>"
//                       + "<body>"
//                       + "<div class='error-message'>"
//                       + "<h2>Error!</h2>"
//                       + "<p>An error occurred while processing the file.</p>"
//                       + "</div>"
//                       + "</body>"
//                       + "</html>";
//
//               // Retourner la réponse d'erreur avec un statut HTTP 500 (Internal Server Error)
//               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                       .contentType(MediaType.TEXT_HTML)  // Utiliser MediaType.TEXT_HTML
//                       .body(errorResponse);
//           }
//       }
       
       
       
       @PostMapping("/uploadPhoto")
       public ResponseEntity<String> uploadPhoto(@RequestParam("numeroCin") String numeroCin,   
                                                 @RequestParam("nameWorker") String user) {
           try {
               System.out.println("Starting upload process for CIN: " + numeroCin);

               // Retrieve photo and create image grid
               byte[] photo = apiImageTraiteSerice.getImageUrlByCin(numeroCin);
               byte[] imageSplite = imageService.duplicateImageInGrid(photo, 100, 100);
               
               // Set up directories
               String dirClient = user;
               Path clientDirPath = Paths.get("uploads/" + dirClient);
               if (!Files.exists(clientDirPath)) {
                   Files.createDirectories(clientDirPath);
               }

               // Generate unique reference and save split photo
               String uuid = UUID.randomUUID().toString();
               String simpleRef = toSimpleHash(uuid);
               Path outputPathPhotoSplite = Paths.get("uploads/" + dirClient + "/" + simpleRef + ".png");
               Files.write(outputPathPhotoSplite, imageSplite);

               // Process PDF conversion and signing
               byte[] pdf = pdfService.convertToPDF(photo);
               byte[] pdfimageSigne = pdfService.signPdf(pdf, user);
               String dateSignature = pdfService.ExtractDateSignature(pdfimageSigne);
               
               // Save signed photo and signature
               Path outputPathPhotoSigne = Paths.get("uploads/" + dirClient + "/" + simpleRef + ".pdf");
               Files.write(outputPathPhotoSigne, pdfimageSigne);
               byte[] signaturePdf = pdfService.getSignatureFromPdf(pdfimageSigne);
               byte[] signaturePhoto = pdfService.convertPdfToImage(signaturePdf);
               Path outputPathSignature = Paths.get("uploads/" + dirClient + "/signature.png");
               Files.write(outputPathSignature, signaturePhoto);

               // Prepare response with links
               String pathPhotoSplite = "http://localhost:8080/uploads/" + dirClient + "/" + simpleRef + ".png";
               String pathSignature = "http://localhost:8080/uploads/" + dirClient + "/signature.png";

               // Generate signature PDF
               String newName = pdfService.getNameWorker();
               String htmlContent = pdfService.modelFicheDeSignature(pathPhotoSplite, pathSignature, user, dateSignature, simpleRef, newName);
               String pdfFicheName = simpleRef + "fiche.pdf";
               pdfService.generatePdfAndSaveToDirectory(htmlContent, "uploads/" + dirClient, pdfFicheName);
               

               // Save expiration details
               LocalDate expirationDate = expirationRefenrece.getExpirationDate(dateSignature);
               String uuidFileName = "uploads/" + dirClient + "/" + simpleRef + ".txt";
               String content = "Reference: " + simpleRef + "\n"
                                + "Date: " + dateSignature + "\n"
                                + "Client: " + dirClient + "\n"
                                + "Date d'expiration référence: " + expirationDate + "\n";
               Files.write(Paths.get(uuidFileName), content.getBytes());

               // Save user info
               InfoUser infoUser = new InfoUser();
               infoUser.setNom(user);
               infoUser.setDateSignature(dateSignature);
               infoUser.setPathFiche("uploads/" + dirClient + "/" + pdfFicheName);
               infoUser.setPathPhotoSigne("uploads/" + dirClient + "/" + simpleRef + ".pdf");
               infoUser.setReference(simpleRef);
               infoUser.setDateExpiration(expirationDate);
               infoUserService.saveUtilisateur(infoUser);


               // Success HTML response
               String htmlResponse = "<html><head><style>"
                       + "body { font-family: Arial, sans-serif; }"
                       + ".success-message { background-color: #DFF2BF; color: #4F8A10; padding: 20px; margin: 10px 0; border: 1px solid #4F8A10; border-radius: 5px; }"
                       + "</style></head><body><div class='success-message'>"
                       + "<h2>Upload Successful!</h2>"
                       + "<p>Your file has been uploaded and processed successfully.</p>"
                       + "<p>Client: " + user + "</p>"
                       + "<p>Reference: " + simpleRef + "</p>"
                       + "<p><a href='/pages/uploadForm'>Return to Form</a></p>"
                       + "</div></body></html>";

               return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_HTML).body(htmlResponse);

           } catch (Exception e) {
               e.printStackTrace(); // Print stack trace to identify the error cause

               // Error HTML response
               String errorResponse = "<html><head><style>"
                       + "body { font-family: Arial, sans-serif; }"
                       + ".error-message { background-color: #FFBABA; color: #D8000C; padding: 20px; margin: 10px 0; border: 1px solid #D8000C; border-radius: 5px; }"
                       + "</style></head><body><div class='error-message'>"
                       + "<h2>Error!</h2>"
                       + "<p>An error occurred while processing the file.</p>"
                       + "</div></body></html>";

               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_HTML).body(errorResponse);
           }
       }


}
     
       

        
       
       
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        



