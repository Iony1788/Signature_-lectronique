CREATE TABLE user (
    id int AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    path_photo_CIN VARCHAR(255),
    reference VARCHAR(255)
);

CREATE TABLE workers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    worker_Name VARCHAR(100)
);


CREATE TABLE info_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_signature DATE NOT NULL,
    nom VARCHAR(100) NOT NULL,
    path_fiche VARCHAR(255),
    path_photo_signe VARCHAR(255),
    reference VARCHAR(100),
    date_expiration date
);


//Insertion donnée
INSERT INTO workers (worker_Name)
VALUES
('Cashpoint'),
('Cashpoint1'),
('Cashpoint2');

INSERT INTO workers (worker_Name)
VALUES
('PDFSigner');






