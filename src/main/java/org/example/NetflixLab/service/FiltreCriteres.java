package org.example.NetflixLab.service;

import org.example.NetflixLab.model.Genre;

public class FiltreCriteres {

    private Genre genre;
    private TypeFilter type = TypeFilter.TOUS;
    private Integer decennie;
    private Double noteMin;
    private String pays;
    private Integer dureeMax;

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }

    public TypeFilter getType() { return type; }
    public void setType(TypeFilter type) { this.type = type; }

    public Integer getDecennie() { return decennie; }
    public void setDecennie(Integer decennie) { this.decennie = decennie; }

    public Double getNoteMin() { return noteMin; }
    public void setNoteMin(Double noteMin) { this.noteMin = noteMin; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public Integer getDureeMax() { return dureeMax; }
    public void setDureeMax(Integer dureeMax) { this.dureeMax = dureeMax; }
}