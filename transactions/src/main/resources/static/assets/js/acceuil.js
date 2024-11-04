$(document).ready(function () {
    var dynamicProgress = 80; // You can change this value dynamically as needed

    $(".animated-progress span").each(function () {
        var $this = $(this);
        $this.attr("data-progress", dynamicProgress); // Set the dynamic progress value
        $this.animate(
                {
                    width: dynamicProgress + "%",
                },
                1000
                );
        $this.text(dynamicProgress + "%");
    });
});

var idNiveau = localStorage.getItem('niveauId');
var idLieu = localStorage.getItem('lieuId');
document.getElementById('idLieu').innerHTML = idLieu;

function mois(month) {
    if (month == 1) {
        return "Janvier";
    } else if (month == 2) {
        return "Février";
    } else if (month == 3) {
        return "Mars";
    } else if (month == 4) {
        return "Avril";
    } else if (month == 5) {
        return "Mai";
    } else if (month == 6) {
        return "Juin";
    } else if (month == 7) {
        return "Juillet";
    } else if (month == 8) {
        return "Aout";
    } else if (month == 9) {
        return "Septembre";
    } else if (month == 10) {
        return "Octobre";
    } else if (month == 11) {
        return "Novembre";
    } else if (month == 12) {
        return "Décembre";
    }
}

var chart;
function chartNiveau(heure, niveau) {
    if (chart) {
        chart.destroy();
    }
    var canvas = document.getElementById("myChart");
    canvas.height = 150; // Hauteur en pixels
    chart = new Chart(canvas, {
        type: "line",
        data: {
            labels: heure,
            datasets: [{
                    data: niveau,
                    label: "Niveau de l'eau de la citerne en %",
                    borderColor: "#16E1FF",
                    backgroundColor: "rgba(0, 123, 255, 0.2)",
                    pointBackgroundColor: "#4F8FC6",
                    pointBorderColor: "#4F8FC6",
                    pointRadius: 5,
                    pointHoverRadius: 13,
                    tension: 0.3,
                    fill: true
                }]
        },
        options: {
            plugins: {
                legend: {
                    display: true,
                    position: 'top',
                    labels: {
                        font: {
                            size: 14
                        }
                    }
                }
            },
            scales: {
                y: {
                    max: 100,
                    min: 0,
                    ticks: {
                        font: {
                            size: 14
                        }
                    }
                },
                x: {
                    ticks: {
                        font: {
                            size: 14
                        }
                    }
                }
            }
        }
    });
}

function maka(datedebut, idtanana) {
    const tableBody = document.getElementById("data-table");
    tableBody.innerHTML = "";
    var tabPorc = [];
    var tabdate = [];
    const adresse = "http://preprod-smartrano.eqima.org/jiro/"
    var urltanana = adresse + "lister/etat/niveau?datedebut=" + datedebut + "&datefin=" + datedebut + "&id=" + idtanana;
    $.getJSON(urltanana, function (data) {

        data.forEach((item, index) => {
            tabPorc.push(item.pourcentage);
            console.log("id niveau ici" + item.id_niveau);

//                                        onnReset(item.id_niveau);
//                                        offReset(item.id_niveau);
//                                        onnMarche(item.id_niveau);
//                                        offMarche(item.id_niveau);

            var h = item.heure
            var spl = h.split(":");
            tabdate.push(spl[0] + "Hr");
            const row = tableBody.insertRow();
            const cell1 = row.insertCell(0);
            const cell2 = row.insertCell(1);
            // const cell3 = row.insertCell(2);
            cell1.innerHTML = item.pourcentage + "%";
            // cell2.innerHTML = item.date;
            cell2.innerHTML = spl[0];
            var dateJ = item.date;
            var dateJJ = dateJ.split("-");
            document.getElementById('dateJ').innerHTML = dateJJ[2] + " " + mois(dateJJ[1]) + " " + dateJJ[0];
        });
        chartNiveau(tabdate.reverse(), tabPorc.reverse())
        let somme = 0;
        for (let i = 0; i < tabPorc.length; i++) {
            somme += tabPorc[i];
        }

        const moyenne = somme / tabPorc.length;

        const moyenneArrondie = moyenne.toFixed(2);
        const plusPetitNombre = Math.min(...tabPorc);
        const plusGrandNombre = Math.max(...tabPorc);
        if (moyenneArrondie === "NaN") {
            document.getElementById('moyenne').innerHTML = "Indisponible"
        } else if (moyenneArrondie === undefined) {
            document.getElementById('moyenne').innerHTML = "Indisponible"
        } else if (plusPetitNombre === "NaN") {
            document.getElementById('minimum').innerHTML = "Indisponible"
        } else if (plusPetitNombre === undefined) {
            document.getElementById('minimum').innerHTML = "Indisponible";
        } else if (plusGrandNombre === Infinity) {
            document.getElementById('maximum').innerHTML = "Indisponible";
        } else if (plusGrandNombre === "NaN") {
            document.getElementById('maximum').innerHTML = "Indisponible"
        } else if (plusGrandNombre === undefined) {
            document.getElementById('maximum').innerHTML = "Indisponible";
        } else if (plusGrandNombre === Infinity) {
            document.getElementById('maximum').innerHTML = "Indisponible";
        } else {
            document.getElementById('maximum').innerHTML = plusGrandNombre + '%';
            document.getElementById('moyenne').innerHTML = moyenneArrondie + '%';
            document.getElementById('minimum').innerHTML = plusPetitNombre + '%';
        }
//                                    document.getElementById('minimum').innerHTML = plusPetitNombre + '%';
//                                    document.getElementById('maximum').innerHTML = plusGrandNombre + '%';

        console.log("Le plus petit nombre est : " + plusPetitNombre);
        console.log("Le plus grand nombre est : " + plusGrandNombre);
        console.log("La moyenne est : " + moyenneArrondie);
    })
}

function last(datedebut, idtanana) {
    var tabPorc = [];
    var tabBat = [];
    const adresse = "http://preprod-smartrano.eqima.org/jiro/"
    var urltanana = adresse + "lister/etat/niveau?datedebut=" + datedebut + "&datefin=" + datedebut + "&id=" + idtanana;
    $.getJSON(urltanana, function (data) {
        console.log(data);
        data.forEach((item, index) => {
            tabPorc.push(item.pourcentage);
            tabBat.push(item.batterie);
        });

        $(document).ready(function () {
//                                var dynamicProgress = 75; // You can change this value dynamically as needed

            $(".animated-progress span").each(function () {
                var $this = $(this);
                $this.attr("data-progress", tabBat[0]); // Set the dynamic progress value
                $this.animate(
                        {
                            width: tabBat[0] + "%",
                        },
                        1000
                        );
                if (tabBat[0] === undefined) {
                    $this.text("Indisponible");
                } else {
                    $this.text(tabBat[0] + "%");
                }
//                                            $this.text(tabBat[0] + "%");
            });
        });
        if (tabPorc[0] === undefined) {
            document.getElementById('info-actuel').innerHTML = " Indisponible ";
        } else {
            document.getElementById('info-actuel').innerHTML = "  " + tabPorc[0] + "%";
        }


        var fm3 = new FluidMeter();
        fm3.init({
            targetContainer: document.getElementById("fluid-meter-3"),
            fillPercentage: tabPorc[0],
            options: {
                fontSize: "20px",
                drawPercentageSign: true,
                drawBubbles: false,
                size: 100,
                borderWidth: 1,
                backgroundColor: "#f3ebeba1",
                foregroundColor: "white",
                foregroundFluidLayer: {
                    fillStyle: "#16E1FF",
                    angularSpeed: 30,
                    maxAmplitude: 5,
                    frequency: 30,
                    horizontalSpeed: -20
                },
                backgroundFluidLayer: {
                    fillStyle: "#4F8FC6",
                    angularSpeed: 100,
                    maxAmplitude: 3,
                    frequency: 22,
                    horizontalSpeed: 20
                }
            }
        });
    })
}

function getFormattedDate() {
    const now = new Date();
    const year = now.getFullYear();
    var month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');

    const formattedDate = year + "-" + month + "-" + day;
    return formattedDate;
}

function affiche() {
    var debut = document.getElementById('datedebut').value;
    if (debut < '2023-10-22') {
        alert("Pas de données disponibles pour les dates antérieures au 22 octobre 2023.");
    } else if (debut > getFormattedDate()) {
        alert("Date postérieure à la date actuelle : " + getFormattedDate());
    } else {
        if (!debut) {
            alert("veuillez remplir tous les champs");
        } else {
            var element = document.getElementById("cacher");
            var infoActuel = document.getElementById("actu");
            if (debut != getFormattedDate()) {
                element.style.display = "none";
                infoActuel.style.display = "block";
                maka(debut, idNiveau);
            } else {
                element.style.display = "block";
                infoActuel.style.display = "none";
                maka(debut, idNiveau);
            }
        }
    }
}

var infoActuel = document.getElementById("actu");
infoActuel.style.display = "none";

document.getElementById("lockBox").hidden = false;
document.getElementById("openBox").hidden = false;

function lastboite(datedebut, idtanana) {
    var tabBoite = [];
    const adresse = "http://preprod-smartrano.eqima.org/jiro/"
    var urltanana = adresse + "lister/boite?datedebut=" + datedebut + "&datefin=" + datedebut + "&id=" + idtanana;
    $.getJSON(urltanana, function (data) {
//                                    console.log(data)
        data.forEach((item, index) => {
            tabBoite.push(item.etat);
        });
//                                    document.getElementById('info-actuel').innerHTML = "  " + tabBoite[0] + "%";

        document.getElementById("lockBox").hidden = false;
        document.getElementById("openBox").hidden = false;

        console.log(tabBoite[0]);
        if (tabBoite[0] == 1) {
            document.getElementById("lockBox").hidden = false;
            document.getElementById("openBox").hidden = true;

        } else if (tabBoite[0] == 0) {
            document.getElementById("openBox").hidden = false;
            document.getElementById("lockBox").hidden = true;
        } else if (tabBoite[0] == undefined) {
            document.getElementById("lockBox").hidden = false;
            document.getElementById("openBox").hidden = true;
        }

    })
}

maka(getFormattedDate(), idNiveau);
last(getFormattedDate(), idNiveau);
lastboite(getFormattedDate(), idNiveau);

// function actualiserPage() {
//     if (!document.hasFocus()) {
//         location.reload();
//     }
// }
// var intervalID = setInterval(actualiserPage, 300000); // 5 minutes = 300 000 ms

// // Ãcouter les Ã©vÃ©nements d'interaction de l'utilisateur
// document.addEventListener("mousemove", function (event) {
//     clearInterval(intervalID);
//     intervalID = setInterval(actualiserPage, 300000);
// });

// document.addEventListener("keydown", function (event) {
//     clearInterval(intervalID);
//     intervalID = setInterval(actualiserPage, 300000);
// });

function onnReset() {
    console.log("idNiveau======" + idNiveau);
    console.log("efa okay ato rall ahhh");
    const apiUrl = 'http://preprod-smartrano.eqima.org/jiro/Modifier/surpresseura?id=' + idNiveau + '&etata=1';
    fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                var res = data.resultat;
                if (res === 'ok') {
                    console.log('etat a été modifié !');
                    alert("Succcess!");

                } else {
                    console.log("erreur");
                    alert("Failed!");

                }
            })
            .catch(error => {
                console.error('Erreur lors de la requête :', error)
                alert("Failed!");

            });
}

function offReset() {
    const apiUrl = 'http://preprod-smartrano.eqima.org/jiro/Modifier/surpresseura?id=' + idNiveau + '&etata=0';
    fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                var res = data.resultat;
                if (res === 'ok') {
                    console.log('etat a été modifié !');
                    alert("Success!");

                } else {
                    console.log("erreur");
                    alert("Failed!");
                }
            })
            .catch(error => {
                console.error('Erreur lors de la requête :', error);
                alert("Failed!");

            });
}
//marche ici
function onnMarche() {
    const apiUrl = 'http://preprod-smartrano.eqima.org/jiro/Modifier/surpresseurb?id=' + idNiveau + '&etatb=1';
    fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                var res = data.resultat;
                if (res === 'ok') {
                    console.log('etat a été modifié !');
                    alert("success!");

                } else {
                    console.log("erreur");
                    alert("Failed!");

                }
            })
            .catch(error => {
                console.error('Erreur lors de la requête :', error);
                alert("Failed!");
            });
}

function offMarche() {
    const apiUrl = 'http://preprod-smartrano.eqima.org/jiro/Modifier/surpresseurb?id=' + idNiveau + '&etatb=0';
    fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                var res = data.resultat;
                if (res === 'ok') {
                    console.log('etat a été modifié !');
                    alert("Success!");
                } else {
                    console.log("erreur");
                    alert("Failed!");
                }
            })
            .catch(error => {
                console.error('Erreur lors de la requête :', error);
                alert("Failed!");

            });
}

