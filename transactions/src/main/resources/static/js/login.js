isAuthenticated();
function login() {
	// Récupérer les valeurs du formulaire
	var username = document.getElementById("username").value;
	var password = document.getElementById("password").value;
	var userCredentials = {
		username: username,
		password: password
	};

	fetch('/token', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(userCredentials),
	})
		.then(response => response.json())
		.then(data => {
			setToken(data.access_token);
			localStorage.setItem('refresh_token', data.refresh_token);
			
			var token = localStorage.getItem('token');
			fetch('/accueil', {
				method: 'GET',
				headers: {
					'Authorization': 'Bearer ' + token
				}
			})
				.then(response => {

					console.log(response);

					window.location.href = '/accueil';
				})
				.catch(error => {
					console.error(error);
				});
			// Rediriger vers une autre page en cas de succès
			//window.location.href = '/accueil'; // Remplacez '/nouvelle-page' par le chemin de votre page cible
		})
		.catch(error => {
			console.error('Error:', error);
			document.getElementById("tokenResult").innerHTML = '<strong>Error:</strong> Username ou Password Disooooooooooo!';
		});
}

// Stocker le token d'authentification dans le navigateur
async function setToken(token) {
	localStorage.setItem('token', token);
}

// Obtenir le token d'authentification du navigateur
function getToken() {
	return localStorage.getItem('token');
}

// Supprimer le token d'authentification du navigateur
function removeToken() {
	localStorage.removeItem('token');
}

function isAuthenticated() {
	// Vérifier la présence du token d'authentification dans le navigateur
	if (getToken()) {
		// Rediriger l'utilisateur vers la page d'accueil si le token existe déjà
		window.location.replace('/accueil');
	}
}

