package transactions.com.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Settings {
    @Id
    private Long id = 1L; // ID fixe pour accéder à cette entrée
    private int lastIdCounter;

    // Getters et Setters
    public int getLastIdCounter() {
        return lastIdCounter;
    }

    public void setLastIdCounter(int lastIdCounter) {
        this.lastIdCounter = lastIdCounter;
    }
}
