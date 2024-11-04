package transactions.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import transactions.com.entity.Worker;
import transactions.com.repository.WorkerRepository;

@Service
public class WorkerService {

	 @Autowired
	    private WorkerRepository workerRepository; // Assurez-vous que votre repository est correctement injecté

	    public List<Worker> getAllWorkers() {
	        return workerRepository.findAll(); // Cela devrait renvoyer tous les travailleurs
	    }

}
