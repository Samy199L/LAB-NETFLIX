package org.example.NetflixLab.util;
import org.example.NetflixLab.dao.MediaDAO;

import org.example.NetflixLab.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LecteurCSV implements MediaDAO {
    private final String cheminRessource;

    public LecteurCSV(String cheminRessource) {
        this.cheminRessource = cheminRessource;
    }


    public List<Media> charger() {
        List<Media> medias = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(cheminRessource)) {
            if (is == null) {
                throw new IllegalStateException("Fichier introuvable dans les ressources : " + cheminRessource);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                reader.readLine();
                String ligne;
                int numeroLigne = 1;

                while ((ligne = reader.readLine()) != null) {
                    numeroLigne++;
                    if (ligne.isBlank()) continue;

                    try {
                        List<String> champs = parserLigne(ligne);
                        Media media = creerMedia(champs);
                        medias.add(media);
                    } catch (Exception e) {
                        System.err.println("Ligne " + numeroLigne + " ignorée (invalide) : " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier CSV : " + cheminRessource, e);
        }

        return medias;
    }

    @Override
    public List<Media> trouverTous() {
        return charger();
    }

    @Override
    public Optional<Media> trouverParId(int id) {
        return trouverTous().stream().filter(m -> m.getId() == id).findFirst();
    }

    @Override
    public Media ajouter(Media media) {
        throw new UnsupportedOperationException("Le mode CSV est en lecture seule.");
    }

    @Override
    public void modifier(Media media) {
        throw new UnsupportedOperationException("Le mode CSV est en lecture seule.");
    }

    @Override
    public void supprimer(int id) {
        throw new UnsupportedOperationException("Le mode CSV est en lecture seule.");
    }

    private List<String> parserLigne(String ligne) {
        List<String> champs = new ArrayList<>();
        StringBuilder valeurCourante = new StringBuilder();
        boolean dansGuillemets = false;

        for (int i = 0; i < ligne.length(); i++) {
            char c = ligne.charAt(i);

            if (c == '"') {
                if (dansGuillemets && i + 1 < ligne.length() && ligne.charAt(i + 1) == '"') {
                    valeurCourante.append('"');
                    i++;
                } else {
                    dansGuillemets = !dansGuillemets;
                }
            } else if (c == ',' && !dansGuillemets) {
                champs.add(valeurCourante.toString());
                valeurCourante.setLength(0);
            } else {
                valeurCourante.append(c);
            }
        }
        champs.add(valeurCourante.toString());
        return champs;
    }

    private Media creerMedia(List<String> champs) {
        int id = Integer.parseInt(champs.get(0).trim());
        String type = champs.get(1).trim();
        String titre = champs.get(2).trim();
        String titreOriginal = champs.get(3).trim();
        int annee = Integer.parseInt(champs.get(4).trim());
        double note = Double.parseDouble(champs.get(5).trim());
        Genre genre = Genre.valueOf(champs.get(6).trim());
        String pays = champs.get(7).trim();
        String realisateur = champs.get(8).trim();
        String duree = champs.get(9).trim();
        String nbSaisons = champs.get(10).trim();
        String nbEpisodes = champs.get(11).trim();
        String statut = champs.get(12).trim();
        String description = champs.get(13).trim();

        return switch (type.toUpperCase()) {
            case "FILM" -> new Film(id, titre, titreOriginal, description, pays, genre,
                    realisateur, annee, note, Integer.parseInt(duree));
            case "SERIE" -> new Serie(id, titre, titreOriginal, description, pays, genre,
                    realisateur, annee, note, Integer.parseInt(nbSaisons),
                    Integer.parseInt(nbEpisodes), StatutSerie.valueOf(statut));
            default -> throw new IllegalArgumentException("Type de média inconnu : " + type);
        };
    }
}
