package transactions.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions.com.entity.InfoUser;


public interface InfoUserRepository extends JpaRepository<InfoUser, Long> {
	
	InfoUser findByReference(String reference);
	
	List<InfoUser> findAll();  
	
}