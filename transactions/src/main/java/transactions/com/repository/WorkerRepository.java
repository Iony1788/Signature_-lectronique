package transactions.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import transactions.com.entity.Worker;



public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    
}

