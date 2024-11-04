package transactions.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transactions.com.entity.ExpirationReference;
import transactions.com.repository.ExpirationReferenceRepository;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ExpirationService {

    @Autowired
    private ExpirationReferenceRepository expirationReferenceRepository;
    

    /**
     * Calcule et renvoie la date d'expiration à partir de la date de signature et de la durée d'expiration en années.
     *
     * @param dateSignature La date de signature (format "yyyy-MM-dd'T'HH:mm:ss").
     * @return La date d'expiration calculée.
     */
    public LocalDate getExpirationDate(String dateSignature) {
        // ---- Récupérer la durée d'expiration depuis la base de données ----
        ExpirationReference expirationReference = expirationReferenceRepository.findById(1L)
            .orElseThrow(() -> new RuntimeException("Expiration Reference not found"));
        
        int expirationDuration = expirationReference.getExpirationDate(); // La durée en années

        // Vérifier que la durée d'expiration n'est pas nulle ou invalide
        if (expirationDuration <= 0) {
            throw new IllegalArgumentException("La durée d'expiration est invalide");
        }

        // ---- Vérifier et formater la date de signature ----
        LocalDateTime signatureDateTime;
        try {
            // Parse la date avec heure (format 'yyyy-MM-ddTHH:mm:ss')
            signatureDateTime = LocalDateTime.parse(dateSignature, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Le format de la date de signature est incorrect. Utilisez 'yyyy-MM-dd'T'HH:mm:ss'.");
        }

        // Extraire seulement la partie date
        LocalDate signatureDate = signatureDateTime.toLocalDate();

        // Calculer la date d'expiration (ajout d'années à la date de signature)
        return signatureDate.plusYears(expirationDuration);
    }
    
    
 
}
