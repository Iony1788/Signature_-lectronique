package transactions.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions.com.entity.ExpirationReference;

public interface ExpirationReferenceRepository extends JpaRepository<ExpirationReference, Long> {
	
}
