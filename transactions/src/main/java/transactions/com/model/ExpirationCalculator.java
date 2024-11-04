package transactions.com.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpirationCalculator {

    /**
     * Calcule la date d'expiration à partir de la date de signature et de la durée en années.
     *
     * @param dateSignature      La date de signature sous forme de chaîne de caractères (format "yyyy-MM-dd").
     * @param expirationDuration La durée d'expiration en années.
     * @return La date d'expiration calculée sous forme de LocalDate.
     */
    public LocalDate calculateExpirationDate(String dateSignature, Integer expirationDuration) {
        // Convertir la date de signature de String à LocalDate
        LocalDate signatureDate = LocalDate.parse(dateSignature, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ajouter la durée d'expiration (en années) à la date de signature
        LocalDate expirationDate = signatureDate.plusYears(expirationDuration.longValue());

        return expirationDate;
    }
}
