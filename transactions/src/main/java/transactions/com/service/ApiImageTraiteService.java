package transactions.com.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;






@Service
public class ApiImageTraiteService {

    @Autowired
    private RestTemplate restTemplate;

    private String url_api = "http://10.10.11.134:8000/list/view/images";

    public List<String> getImagesUrlTraiterFromApi() {
        ResponseEntity<List<String>> response = restTemplate.exchange(
                url_api,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        return response.getBody();
    }
    
    
    public byte[] downloadImageAsBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl); // Exception si `imageUrl` est vide
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public byte[] getImageUrlByCin(String numeroCin) throws IOException {
        List<String> images = getImagesUrlTraiterFromApi();

        String imageUrl = "";

        for (String image : images) {
            if (image.contains(numeroCin)) {  // Vérifie si l'URL contient `numeroCin`
                imageUrl = image;
                break;
            }
        }

        // Vérification si l'URL n'est pas vide avant le téléchargement
        if (imageUrl.isEmpty()) {
            throw new IOException("Aucune URL trouvée pour le CIN : " + numeroCin);
        }

        return downloadImageAsBytes(imageUrl);
    }

   
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


