package transactions.com.controller;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.models.Model;

public class SuccessController {

	@GetMapping("/success")
	public String showSuccessPage(Model model) {
	    // Vous pouvez ajouter des attributs au modèle si nécessaire
	    return "pages/signer"; // Chemin vers le fichier success.html
	}

}
