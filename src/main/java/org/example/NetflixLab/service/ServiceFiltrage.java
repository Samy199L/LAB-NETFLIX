package org.example.NetflixLab.service;

import org.example.NetflixLab.model.Film;
import org.example.NetflixLab.model.Media;
import org.example.NetflixLab.model.Serie;

import java.text.Normalizer;
import java.util.List;

public class ServiceFiltrage {

    public List<Media> filtrer(List<Media> medias, FiltreCriteres criteres) {
        return medias.stream()
                .filter(m -> respecteType(m, criteres.getType()))
                .filter(m -> criteres.getGenre() == null || m.getGenre() == criteres.getGenre())
                .filter(m -> criteres.getDecennie() == null || appartientDecennie(m.getAnnee(), criteres.getDecennie()))
                .filter(m -> criteres.getNoteMin() == null || m.getNote() >= criteres.getNoteMin())
                .filter(m -> criteres.getPays() == null || criteres.getPays().equalsIgnoreCase(m.getPays()))
                .filter(m -> respecteDureeMax(m, criteres.getDureeMax()))
                .toList();
    }

    public List<Media> rechercherTexte(List<Media> medias, String texte) {
        if (texte == null || texte.isBlank()) {
            return medias;
        }
        String recherche = normaliser(texte);
        return medias.stream()
                .filter(m -> normaliser(m.getTitre()).contains(recherche)
                        || normaliser(m.getRealisateur()).contains(recherche))
                .toList();
    }

    private boolean respecteType(Media m, TypeFilter type) {
        if (type == null || type == TypeFilter.TOUS) return true;
        if (type == TypeFilter.FILM) return m instanceof Film;
        return m instanceof Serie;
    }

    private boolean appartientDecennie(int annee, int decennie) {
        return annee >= decennie && annee < decennie + 10;
    }

    private boolean respecteDureeMax(Media m, Integer dureeMax) {
        if (dureeMax == null) return true;
        if (!(m instanceof Film film)) return true;
        return film.getDureeMinutes() <= dureeMax;
    }

    private String normaliser(String texte) {
        String sansAccents = Normalizer.normalize(texte, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sansAccents.toLowerCase();
    }
}