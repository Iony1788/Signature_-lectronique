package transactions.com.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;


import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import transactions.com.model.UserCredentials;
import transactions.com.service.KeyCloakService;

@RestController
class Defaultcontroller {
	@Autowired
	KeyCloakService keyCloackService;

	@PostMapping("/token")
	@CrossOrigin
	public ResponseEntity<?> getTokenUsingCredentials(@RequestBody UserCredentials userCredentials) {

		String responseToken = null;
		try {

			responseToken = keyCloackService.getToken(userCredentials);

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}

	@Autowired
	private KeycloakDeployment keycloakDeployment;

	@PostMapping("/logout")
	@CrossOrigin
	public ModelAndView logout(HttpServletRequest request) throws ServletException {
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();

		KeycloakSecurityContext session = (KeycloakSecurityContext) token.getCredentials();

		// Révocation des tokens
		if (session instanceof RefreshableKeycloakSecurityContext) {
			RefreshableKeycloakSecurityContext refreshableSession = (RefreshableKeycloakSecurityContext) session;
			// System.out.println(refreshableSession.getRefreshToken());
			refreshableSession.logout(keycloakDeployment);
		}
		request.logout();

		return new ModelAndView("/pages/accueil.html");

	}


	   @GetMapping("/username")
	    public String getUsername() {
	        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) token.getPrincipal();
	        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
	        return session.getToken().getPreferredUsername();
	    }
	   
	   
}