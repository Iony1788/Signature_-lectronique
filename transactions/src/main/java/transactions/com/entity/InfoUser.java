package transactions.com.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InfoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String pathPhotoSigne;
    private String pathFiche;
    
    private String dateSignature;
    private String reference;
    private LocalDate date_expiration;
    
    @Column(name = "nom") 
    private String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPathPhotoSigne() {
        return pathPhotoSigne;
    }

    public void setPathPhotoSigne(String pathPhotoSigne) {
    	
        this.pathPhotoSigne = pathPhotoSigne;
    }
    
	public void setReference(String reference) {
	    	
	        this.reference = reference;
	    }

    // Getter et Setter pour pathFiche
    public String getPathFiche() {
        return pathFiche;
    }

    public void setPathFiche(String pathFiche) {
        this.pathFiche = pathFiche;
    }

    // Getter et Setter pour dateSignature
    public String getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(String dateSignature2) {
        this.dateSignature = dateSignature2;
    }
    
    public String getReference() {
        return reference;
    }
    
    public LocalDate getDateExpiration() {
    	return date_expiration;
    }
    
    public void setDateExpiration(LocalDate date_expiration) {
    	this.date_expiration = date_expiration;
    }
    



}
