DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS genre;

CREATE TABLE genre (
    id   SERIAL PRIMARY KEY,
    nom  VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE media (
    id              SERIAL PRIMARY KEY,
    type            VARCHAR(10) NOT NULL CHECK (type IN ('FILM','SERIE')),
    titre           VARCHAR(255) NOT NULL,
    titre_original  VARCHAR(255),
    annee           INTEGER NOT NULL CHECK (annee BETWEEN 1900 AND 2100),
    note            NUMERIC(3,1) CHECK (note BETWEEN 0 AND 10),
    genre_id        INTEGER NOT NULL REFERENCES genre(id) ON DELETE RESTRICT,
    pays            VARCHAR(100),
    realisateur     VARCHAR(255),
    description     TEXT,
    duree_minutes   INTEGER,
    nombre_saisons  INTEGER,
    nombre_episodes INTEGER,
    statut          VARCHAR(20) CHECK (statut IN ('EN_COURS','TERMINEE','ANNULEE') OR statut IS NULL)
);



INSERT INTO genre (nom) VALUES
    ('ACTION'),('COMEDIE'),('DRAME'),('DOCUMENTAIRE'),('HORREUR'),('ROMANCE'),('SF'),('THRILLER'),('ANIMATION'),('FANTASTIQUE');