function logout() {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // Assurez-vous que le serveur peut accepter JSON si nécessaire
        },
        body: JSON.stringify({}) // Envoyez éventuellement des données JSON supplémentaires si nécessaire
    })
    .then(response => {
    
            // La déconnexion a réussi, redirigez l'utilisateur vers la page d'accueil ou une autre page
            window.location.href = '/pages/accueil.html';
    })
    .catch(error => {
        console.error('Erreur lors de la déconnexion:', error);
    });
}




function RHEndpoint() {
	// Récupérer le token du localStorage
	var token = localStorage.getItem('token');
	fetch('/api/rh', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token
		}
	})
		.then(response => {

			console.log(response);
			window.location.href = '/api/rh';

		})
		.catch(error => {
			console.error(error);
		});
}

function FinanceEndpoint() {
	// Récupérer le token du localStorage
	var token = localStorage.getItem('token');
	fetch('/api/finance', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token
		}
	})
		.then(response => {

			console.log(response);

			window.location.href = '/api/finance';
		})
		.catch(error => {
			console.error(error);
		});
}


function BfvEndpoint() {
	// Récupérer le token du localStorage
	var token = localStorage.getItem('token');
	fetch('/Jirakaiky/WS_SoldesEqima', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token
		}
	})
		.then(response => response.json())
		.then(data => {
			console.log(data);
			window.location.href = '/Jirakaiky/WS_SoldesEqima';
		})
		.catch(error => {
			console.error(error);
		});
}

function EqimaEndpoint() {
	// Récupérer le token du localStorage
	var token = localStorage.getItem('token');
	fetch('/api/eqima', {
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + token
		}
	})
		.then(response => {

			console.log(response);

			window.location.href = '/api/transactions';
		})
		.catch(error => {
			console.error(error);
		});
}

