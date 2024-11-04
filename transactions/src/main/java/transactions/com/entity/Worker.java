package transactions.com.entity;

import javax.persistence.*;

@Entity
@Table(name = "workers") 
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "worker_Name") // Mappage correct avec le nom de la colonne dans la base de données
    private String workerName;

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    
    @Override
    public String toString() {
        return "Worker{" +
               "id=" + id +
               ", name='" + workerName + '\'' +
               '}';
    }
}
