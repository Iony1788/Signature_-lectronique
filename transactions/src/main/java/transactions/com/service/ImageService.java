package transactions.com.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import transactions.com.model.SignerModel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Service
public class ImageService {

    //private static final String SIGN_SERVER_URL = "https://preprod-sonia.eqima.org/signserver/rest/v1/workers/{workerId}/process";
    
    @Autowired
	private RestTemplate restTemplate;
    
    private static int counter = 1;

    public static int generateUniqueNumber() {
        return counter++;
    }

    
    public byte[] signDoc(byte[] fileBytes, SignerModel signer) throws Exception {
		try {
			
			
			MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
			
			ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
	            @Override
	            public String getFilename() {
	                return "file"; 
	            }
	        };
	        requestBody.add("workerName", signer.getWorkerName());
//	        requestBody.add("processType", signer.getProcessType());
//	        requestBody.add("encoding", signer.getEncoding());
	        requestBody.add("data",fileResource); 
	        System.out.println("Request Body: " + requestBody.toString());
	        
	        String externalUrl = "https://preprod-sonia.eqima.org/signserver/process";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
	        

	        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(externalUrl, HttpMethod.POST, requestEntity, byte[].class);
	        //ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(externalUrl, new HttpEntity<>(requestBody, headers), byte[].class);
	        
	        return responseEntity.getBody();
			//return fileBytes;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error"+e.getMessage());
		}
		
		
        
	}


    public static byte[] createWhiteImageBytes() {
        // Créer une image blanche avec la largeur et la hauteur spécifiées
        int width = 100;
        int height = 100;
        BufferedImage whiteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Remplir l'image avec la couleur blanche
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                whiteImage.setRGB(x, y, 0xFFFFFF); // Couleur blanche
            }
        }

       
        // Utiliser ByteArrayOutputStream pour écrire l'image en mémoire
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Écrire l'image dans le flux en tant qu'image PNG
            ImageIO.write(whiteImage, "png", baos);
            baos.flush(); // S'assurer que tout est écrit dans le flux
            return baos.toByteArray(); // Retourner les octets de l'image
        } catch (IOException e) {
            e.printStackTrace();
            return null; // En cas d'erreur, retourner null
        }
    }
    
  
    public byte[] duplicateImageInGrid(byte[] imageBytes, int width,int height) {
        try {
            // Lire l'image à partir du tableau d'octets
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            // Définir la largeur et la hauteur de la grille
            int gridWidth = width * 2;  // Largeur totale de la grille (2 colonnes)
            int gridHeight = height * 2; // Hauteur totale de la grille (2 lignes)

            // Créer une image vide pour la grille
            BufferedImage gridImage = new BufferedImage(gridWidth, gridHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = gridImage.getGraphics();

            // Dessiner l'image d'origine 4 fois dans la grille
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    g.drawImage(originalImage, col * width, row * height, width, height, null);
                }
            }

            g.dispose(); // Libérer les ressources graphiques

            // Écrire l'image combinée dans un tableau d'octets
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(gridImage, "png", baos);
            return baos.toByteArray(); // Retourner le tableau d'octets de l'image résultante
        } catch (Exception e) {
            e.printStackTrace();
            return null; // En cas d'erreur, retourner null
        }
    }
    
    
}

