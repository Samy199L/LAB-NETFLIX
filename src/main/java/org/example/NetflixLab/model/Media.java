package org.example.NetflixLab.model;

import java.util.Objects;

public abstract class Media {
    private int id;
    private String titre;
    private String titreOriginal;
    private String description;
    private String pays;
    private Genre genre;
    private String realisateur;
    private int annee;
    private double note;

    public Media(int id, String titre, String titreOriginal, String description,
                 String pays, Genre genre, String realisateur, int annee, double note) {
        this.id = id;
        this.titre = titre;
        this.titreOriginal = titreOriginal;
        this.description = description;
        this.pays = pays;
        this.genre = genre;
        this.realisateur = realisateur;
        this.annee = annee;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }


    public String getTitreOriginal() {
        return titreOriginal;
    }


    public String getDescription() {
        return description;
    }



    public String getPays() {
        return pays;
    }



    public Genre getGenre() {
        return genre;
    }



    public String getRealisateur() {
        return realisateur;
    }



    public int getAnnee() {
        return annee;
    }


    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;
        return id == ((Media) o).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", titreOriginal='" + titreOriginal + '\'' +
                ", description='" + description + '\'' +
                ", pays='" + pays + '\'' +
                ", genre='" + genre + '\'' +
                ", realisateur='" + realisateur + '\'' +
                ", annee=" + annee +
                ", note=" + note +
                '}';
    }
}
