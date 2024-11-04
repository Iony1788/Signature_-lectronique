package transactions.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions.com.entity.InfoUser;

import java.util.List;

public interface InfoUserRepository extends JpaRepository<InfoUser, Long> {
	
	InfoUser findByReference(String reference);
	
	
}