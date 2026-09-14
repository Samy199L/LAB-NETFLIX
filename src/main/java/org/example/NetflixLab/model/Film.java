package org.example.NetflixLab.model;

public class Film extends Media{
    private final int dureeMinutes;

    public Film(int id, String titre, String titreOriginal, String description,
                String pays, Genre genre, String realisateur, int annee, double note,
                int dureeMinutes) {
        super(id, titre, titreOriginal, description, pays, genre, realisateur, annee, note);
        this.dureeMinutes = dureeMinutes;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    @Override
    public String toString() {
        return "Film{" +
                super.toString() +
                ", dureeMinutes=" + dureeMinutes +
                '}';
    }
}
