package org.example.NetflixLab.model;

public class Serie extends Media{
    private final int nombreSaisons;
    private final int nombreEpisodes;
    private final StatutSerie statut;

    public Serie(int id, String titre, String titreOriginal, String description,
                 String pays, Genre genre, String realisateur, int annee, double note,
                 int nombreSaisons, int nombreEpisodes, StatutSerie statut) {
        super(id, titre, titreOriginal, description, pays, genre, realisateur, annee, note);
        this.nombreSaisons = nombreSaisons;
        this.nombreEpisodes = nombreEpisodes;
        this.statut = statut;
    }

    public int getNombreSaisons() {
        return nombreSaisons;
    }

    public int getNombreEpisodes() {
        return nombreEpisodes;
    }

    public StatutSerie getStatut() {
        return statut;
    }

    @Override
    public String toString() {
        return "Serie{" +
                super.toString() +
                ", nombreSaisons=" + nombreSaisons +
                ", nombreEpisodes=" + nombreEpisodes +
                ", statut=" + statut +
                '}';
    }
}
