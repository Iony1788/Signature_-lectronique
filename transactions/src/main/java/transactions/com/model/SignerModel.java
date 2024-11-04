package transactions.com.model;
import java.util.UUID;


public class SignerModel {

    private String workerName;     // Le nom du worker ou du signataire
    private String processType;    // Le type de processus (par ex. signature, validation)
    private String encoding;       // Le type d'encodage (ex: Base64)
    private String encodedString;  // Le fichier encodé en base64
    private String uuid; //reference
    
    // Constructeur par défaut
    public SignerModel() {
    }

    // Constructeur avec paramètres
    public SignerModel(String workerName, String processType, String encoding, String encodedString,String uuid) {
        this.workerName = workerName;
        this.processType = processType;
        this.encoding = encoding;
        this.encodedString = encodedString;
        this.uuid = UUID.randomUUID().toString();
    }

    // Getters et setters
    
    // Getters
    public String getUuid() {
        return uuid;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncodedString() {
        return encodedString;
    }

    public void setEncodedString(String encodedString) {
        this.encodedString = encodedString;
    }

    // Méthode pour afficher les détails du SignerModel (utile pour déboguer)
    @Override
    public String toString() {
        return "SignerModel{" +
                "workerName='" + workerName + '\'' +
                ", processType='" + processType + '\'' +
                ", encoding='" + encoding + '\'' +
                ", encodedString='" + encodedString + '\'' +
                '}';
    }
    
  
    
    
}
