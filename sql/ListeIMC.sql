-- BONUS : base de données ListeIMC, une seule table patient
CREATE DATABASE IF NOT EXISTS ListeIMC CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ListeIMC;

DROP TABLE IF EXISTS patient;
CREATE TABLE patient (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(50)  NOT NULL,
    prenom         VARCHAR(50)  NOT NULL,
    masse          DOUBLE       NOT NULL,   -- kg
    taille         DOUBLE       NOT NULL,   -- m
    imc            DOUBLE       NOT NULL,
    interpretation VARCHAR(40)  NOT NULL
);

INSERT INTO patient (nom, prenom, masse, taille, imc, interpretation) VALUES
('DIOP',   'Daouda',  70, 1.70, 24.22, 'Corpulence normale'),
('FALL',   'Aminata', 52, 1.68, 18.42, 'Insuffisance pondérale'),
('SOW',    'Moussa',  88, 1.75, 28.73, 'Surpoids'),
('NDIAYE', 'Fatou',   98, 1.65, 36.00, 'Obésité sévère');
