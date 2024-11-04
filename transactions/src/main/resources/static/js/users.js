function getID(firstName, groupes) {
	var userId;
	//	var grpName;
	var groupId;
	//	const username = document.getElementById('username').value;
	fetch(`/api/getUserId?username=${encodeURIComponent(firstName)}`)
		.then(response => response.text())
		.then(data => {
			userId = JSON.parse(data)[0].id;
			console.log("userID=" + userId);
		})
		.catch(error => console.error('Error:', error));


	fetch("/api/getGroupId?groupName=EQIMA")
		.then(response => {
			if (!response.ok) {
				throw new Error("Erreur lors de la récupération de l'ID du groupe");
			}
			return response.text();
		})
		.then(data => {
			if (typeof data === 'string') {
				data = JSON.parse(data);
			}
			if (Array.isArray(data)) {
				console.log(data);
				data.forEach(item => {
					console.log(groupes);
					if (groupes == item.name) {
						groupId = item.id;
						console.log("groupe id farany===" + groupId)
						group(userId, groupId)
					}
				});
			}
		})

		.catch(error => {
			console.error(error);
		});

};
async function createUser() {
	var username = document.getElementById("username").value;
	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var email = document.getElementById("email").value;
	var password = document.getElementById("password").value;
	//	var role = document.querySelector("select").value;
	var cin = document.getElementById("cin").value;
	//	var iuuid = document.getElementById("iuuid").value;
	var fonction = document.getElementById("fonction").value;
	var organisation = document.getElementById("organisation").value;
	var groupes = convertirEnMajuscules(organisation);
	const url = '/api/createUser';
	const user = {
		createdTimestamp: 1588880747548,
		username: username,
		enabled: true,
		totp: false,
		emailVerified: true,
		firstName: firstName,
		lastName: lastName,
		email: email,

		disableableCredentialTypes: [],
		requiredActions: [],
		notBefore: 0,
		access: {
			manageGroupMembership: true,
			view: true,
			mapRoles: true,
			impersonate: true,
			manage: true
		},
		realmRoles: [organisation],
		"credentials": [
			{
				"type": password,
				"value": password,
				"temporary": false
			}
		],

		"attributes": {
			CIN: cin,
			//			IUID: iuuid,
			fonction: fonction,
			organisation: organisation
		}
	};

	try {
		const response = await fetch(url, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(user)
		});

		if (response.ok) {
			const data = await response.json();
			console.log('User created successfully:', data);
			alert("Added success")
			getID(firstName, groupes);
			document.getElementById("username").value = "";
			document.getElementById("firstName").value = "";
			document.getElementById("lastName").value = "";
			document.getElementById("email").value = "";
			document.getElementById("password").value = "";
			//	var role = document.querySelector("select").value;
			document.getElementById("cin").value = "";
			//	var iuuid = document.getElementById("iuuid").value;
			document.getElementById("fonction").value = "";
			document.getElementById("organisation").value = "";
		} else {
			console.error('Failed to create user:', response.statusText);
			alert('Failed to create user', response.statusText);
			const errorData = await response.text();
			console.error('Error details:', errorData);
		}
	} catch (error) {
		console.error('Error:', error);
		alert("Added success")
		getID(firstName, groupes);
		document.getElementById("username").value = "";
		document.getElementById("firstName").value = "";
		document.getElementById("lastName").value = "";
		document.getElementById("email").value = "";
		document.getElementById("password").value = "";
		//	var role = document.querySelector("select").value;
		document.getElementById("cin").value = "";
		//	var iuuid = document.getElementById("iuuid").value;
		document.getElementById("fonction").value = "";
		document.getElementById("organisation").value = "";

	}
}

function updateUserGroup() {
	const requestOptions = {
		method: 'PUT',
		redirect: 'follow'
	};

	fetch(`/api/users/9804959f-8a22-4f0a-acfc-1be46fe8d696/groups/a91c2a52-4aa3-4b0a-8a7d-98dcadd3062d`, requestOptions)
		.then(response => response.text())
		.then(result => console.log(result))
		.catch(error => console.log('error', error));
}


function convertirEnMajuscules(texte) {
	return texte.toUpperCase();
}


async function fetchUsername() {
	try {
		const response = await fetch('/username');

		if (response.ok) {
			const username = await response.text();
			var anarana = username
			getinformationUser(anarana);
			console.log('Username:', username);

		} else {
			console.error('HTTP Error:', response.status);
			console.log(response.status);
		}
	} catch (error) {
		console.error('Fetch Error:', error);
		console.log(error.message);
	}
}
fetchUsername();


function group(userId, groupId) {
	// Construire l'URL de l'API
	var url = "/api/addGroupToUser?userID=" + encodeURIComponent(userId) + "&groupID=" + encodeURIComponent(groupId);

	// Effectuer la requête AJAX
	fetch(url, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json'
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			console.log('Résultat de la requête:', data);
			// Traiter les données ici
		})
		.catch(error => {
			console.error('Erreur lors de la requête:', error);
		});
}

function getinformationUser(anarana) {
	fetch(`/api/getUserId?username=${encodeURIComponent(anarana)}`)
		.then(response => response.text())
		.then(data => {
			//			console.log(data);

			if (typeof data === 'string') {
				data = JSON.parse(data);
			}
			if (Array.isArray(data)) {

				data.forEach(item => {
					var firstname = item.firstName;
					var lastname = item.lastName;
					var fonction = item.attributes.fonction[0];
					var organisation = item.attributes.organisation[0]
					var elements = document.getElementsByClassName("preprod");
					for (var i = 0; i < elements.length; i++) {
						elements[i].innerHTML = "Bienvenue " + firstname + ", " + lastname + ", " + fonction + ", " + organisation;
					}
				});
			}


		})
		.catch(error =>{
			 console.error('Error:', error)
		});
}
