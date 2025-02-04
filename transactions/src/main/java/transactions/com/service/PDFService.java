package transactions.com.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import transactions.com.entity.Settings;
import transactions.com.repository.SettingsRepository;

@Service
public class PDFService {

	
	@Autowired
	private RestTemplate restTemplate;
    
    @Autowired
	private  SettingsRepository settingsRepository;
		
	private int idCounter;
    

public byte[] convertToPDF(byte[] signedImage) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        if (signedImage != null) {
            ImageData imageData = ImageDataFactory.create(signedImage); 
            Image image = new Image(imageData);
            document.add(image);  
        } else {
          
        }
        pdfDocument.addNewPage();

        document.close();
        return byteArrayOutputStream.toByteArray();

    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

public byte [] signPdf(byte [] pdf , String workerName) {
	
	MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
	ByteArrayResource fileResource = new ByteArrayResource(pdf) {
        @Override
        public String getFilename() {
            return "file"; 
        }
    };
    
    try (PDDocument document = PDDocument.load(new ByteArrayInputStream(fileResource.getByteArray()))) {
        // Ici, vous pouvez travailler avec le document PDF chargé
        // Par exemple : document.save("output.pdf");
    } catch (IOException e) {
        e.printStackTrace();
        // Gérer l'exception selon votre logique
    }
   
    requestBody.add("workerName", workerName);
    requestBody.add("data",fileResource);   
    String externalUrl = "https://preprod-sonia.eqima.org/signserver/process";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<byte[]> responseEntity = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, byte[].class);
    
    byte[] pdfSigne = responseEntity.getBody();
    
    return pdfSigne;
    
}





//recuper le signature seulement avec la modification taille pdf signature
public byte[] getSignatureFromPdf(byte[] originalPdf) {
	  try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
          // Charger le PDF existant à partir du byte[]
          PdfReader reader = new PdfReader(new ByteArrayInputStream(originalPdf));
          PdfDocument originalDocument = new PdfDocument(reader);
          
          // Créer un nouveau PDF
          PdfWriter writer = new PdfWriter(byteArrayOutputStream);
          PdfDocument newDocument = new PdfDocument(writer);

          // Vérifier que le PDF a au moins deux pages
          if (originalDocument.getNumberOfPages() >= 2) {
              // Copier la deuxième page du PDF original dans le nouveau PDF
              originalDocument.copyPagesTo(2, 2, newDocument);
          }

          // Fermer les documents
          newDocument.close();
          originalDocument.close();
          byte[] pdfSignature = cropPDFToHaveSignature(byteArrayOutputStream.toByteArray());
          
          return pdfSignature;

      } catch (IOException e) {
          e.printStackTrace();
          return null;
      }
}
public void generatePdfAndSaveToDirectory(String htmlContent, String directoryPath, String fileName) {
    try {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + File.separator + fileName;

        // Créer un nouveau PdfDocument
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new File(filePath)));

        // Définir la taille de page en mode paysage
        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

        // Convertir HTML à PDF
        HtmlConverter.convertToPdf(htmlContent, pdfDocument, new ConverterProperties());

        pdfDocument.close();

        System.out.println("PDF généré et enregistré dans : " + filePath);

    } catch (IOException e) {
        e.printStackTrace();
    }
}

	// Méthode pour charger le dernier ID de la base de données
	public void loadIdCounter() {
	    // Charger l'entrée "Settings" avec l'ID 1 ou créer une nouvelle si elle n'existe pas
	    Settings settings = settingsRepository.findById(1L).orElse(new Settings());
	    idCounter = settings.getLastIdCounter();
	}
	
	// Méthode pour mettre à jour le dernier ID dans la base de données
	private void updateIdCounter() {
	    // Charger l'entrée Settings (toujours ID 1) ou créer une nouvelle si elle n'existe pas
	    Settings settings = settingsRepository.findById(1L).orElse(new Settings());
	    settings.setLastIdCounter(idCounter); // Mettre à jour le compteur avec la nouvelle valeur
	    settingsRepository.save(settings); // Sauvegarder les modifications
	}
	
	// Méthode pour générer un nom de travailleur
	public String getNameWorker() {
	    loadIdCounter(); // Charger le compteur actuel de la base de données
	
	    String newName = String.format("eqi-%03d", idCounter); // Générer le nouveau nom avec le compteur
	    idCounter++; // Incrémenter le compteur
	
	    updateIdCounter(); // Sauvegarder le compteur mis à jour dans la base de données
	
	    return newName; // Retourner le nom généré
	}
			
		




	public String modelFicheDeSignature(String pathPhoto, String pathSignature, String workerName, String date, String uuid, String newName) {
        return "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Fiche de Signature</title>" +
                "<link href='https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap' rel='stylesheet'>" +
                "<style>" +
                "body { font-family: 'Roboto', sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }" +
                ".badge { border-radius: 10px; padding: 30px; background-color: #d9eaff; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); max-width: 800px; margin: 20px auto; }" +
                ".header { text-align: center; margin-bottom: 20px; }" +
                ".header h1 { font-size: 24px; color: #003366; margin: 0; }" +
                ".container { display: flex; align-items: center; margin-bottom: 20px; }" +
                ".container-photo { width: 180px; height: 180px; overflow: hidden; border-radius: 10px; border: 2px solid #003366; margin-right: 20px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); }" +
                ".container-photo img { width: 100%; height: 100%; object-fit: cover; }" +
                ".signature { width: 180px; height: 180px; overflow: hidden; border-radius: 10px; border: 2px solid #003366; box-shadow: 0 0 10px rgba(0, 0, 0, 0.2); }" +
                ".signature img { width: 100%; height: 100%; object-fit: cover; }" +
                ".info { display: flex; flex-direction: column; flex-grow: 1; }" +
                ".info p { margin: 5px 0; font-size: 18px; color: #003366; text-align: left; box-shadow: 0 0 5px rgba(0, 0, 0, 0.2); padding: 10px; }" +
                ".info strong { font-size: 20px; color: #003366; }" +
                ".footer { text-align: center; margin-top: 20px; font-size: 14px; color: #003366; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='badge'>" +
                "<div class='header'>" +
                "<h1>Fiche de Signature</h1>" +
                "</div>" +
                "<div class='container'>" +
                "<div class='container-photo'>" +
                "<img src='" + pathPhoto + "' alt='Photo de l'ouvrier'/>" +
                "</div>" +
                "<div class='info'>" +
                "<p><strong>Worker Name:</strong> " + newName + "</p>" +
                "<p><strong>Reference:</strong> " + uuid + "</p>" +
                "<p><strong>Date:</strong> " + date + "</p>" +
                "<p><strong>Full Name:</strong> " + workerName + "</p>" +
                "</div>" +
                "</div>" +
                "<div class='signature'>" +
                "<img src='" + pathSignature + "' alt='Signature de l'ouvrier'/>" +
                "</div>" +
                "<div class='footer'>Fiche générée automatiquement</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
	


public  String ExtractDateSignature(byte[] pdfBytes) throws CertificateException {

    try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
        // Supposons que nous ne voulons que la première signature
        PDSignature signature = document.getSignatureDictionaries().isEmpty() ? null : document.getSignatureDictionaries().get(0);

        if (signature != null) {
        
            Instant instant = signature.getSignDate().toInstant();
   
            LocalDateTime signingDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
           return signingDateTime.toString();
           
            
        } else {
            System.out.println("Aucune signature trouvée dans le document.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return "";
}


public static void displayPdfMetadata(byte[] signedDocument) {
    if (signedDocument != null) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(signedDocument))) {
            PDDocumentInformation info = document.getDocumentInformation();
            
            
            System.out.println("Titre : " + info.getTitle());
            System.out.println("Auteur : " + info.getAuthor());
            System.out.println("Sujet : " + info.getSubject());
            System.out.println("Mots-clés : " + info.getKeywords());
            System.out.println("Créé le : " + info.getCreationDate());
            System.out.println("Modifié le : " + info.getModificationDate());
            System.out.println("Producteur : " + info.getProducer());
            System.out.println("Créé par : " + info.getCreator());

            // Nombre de pages
            System.out.println("Nombre de pages : " + document.getNumberOfPages());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("Aucun document signé n'a été reçu.");
    }
}
public static byte[] cropPDFToHaveSignature(byte[] pdfBytes) {
    try (PDDocument document = PDDocument.load(pdfBytes)) {
        // Parcourir toutes les pages du document
        for (PDPage page : document.getPages()) {
            // Obtenir les dimensions de la page
            //PDRectangle mediaBox = page.getMediaBox();
            //float pageWidth = mediaBox.getWidth();
            //float pageHeight = mediaBox.getHeight();

            // Définir les dimensions du rectangle de rognage
            float cropWidth = 150; // Largeur du rectangle de rognage
            float cropHeight = 150; // Hauteur du rectangle de rognage

            // Calculer les coordonnées du coin supérieur gauche pour le rognage
            //float cropX = (pageWidth - cropWidth) / 2; // Centré horizontalement
            //float cropY = (pageHeight - cropHeight); // Positionné en bas

            // Créer le rectangle de rognage
            PDRectangle cropBox = new PDRectangle(500, 0, cropWidth, cropHeight);
            page.setCropBox(cropBox); // Appliquer le rectangle de rognage à la page
        }

        // Utilisation de ByteArrayOutputStream pour sauvegarder le PDF rogné dans un tableau de bytes
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.save(outputStream);
            return outputStream.toByteArray(); // Retourner le tableau de bytes
        }
    } catch (IOException e) {
        e.printStackTrace();
        return null; // En cas d'erreur, retourner null
    }
}

public byte[] convertPdfToImage(byte[] pdfBytes) throws IOException {
    // Charger le document PDF à partir des bytes
    try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        
        // Rendre la première page en image
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300); // DPI de 300 pour haute qualité

        // Écrire l'image dans un ByteArrayOutputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos); // Changez "png" en "jpg" si nécessaire
            return baos.toByteArray(); // Retourner les bytes de l'image
        }
    }
}

}





   

