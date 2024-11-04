isAuthenticated();

// Obtenir le token d'authentification du navigateur
function getToken() {
  return localStorage.getItem('token');
 
}

function isAuthenticated() {
  // Vérifier la présence du token d'authentification dans le navigateur
  if (!getToken()) {
    // Rediriger l'utilisateur vers la page d'accueil si le token existe déjà
    window.location.replace('../index.html');
  }
}