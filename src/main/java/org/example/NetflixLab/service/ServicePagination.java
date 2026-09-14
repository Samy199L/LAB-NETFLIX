package org.example.NetflixLab.service;

import org.example.NetflixLab.model.Media;

import java.util.List;

public class ServicePagination {

    private int taillePage;

    public ServicePagination(int taillePage) {
        this.taillePage = taillePage;
    }

    public List<Media> obtenirPage(List<Media> resultatsFiltres, int numeroPage) {
        int total = resultatsFiltres.size();
        int debut = (numeroPage - 1) * taillePage;

        if (debut >= total || debut < 0) {
            return List.of();
        }
        int fin = Math.min(debut + taillePage, total);
        return resultatsFiltres.subList(debut, fin);
    }

    public int nombrePages(List<Media> resultatsFiltres) {
        if (resultatsFiltres.isEmpty()) return 1;
        return (int) Math.ceil((double) resultatsFiltres.size() / taillePage);
    }

    public int getTaillePage() { return taillePage; }

    public void setTaillePage(int taillePage) { this.taillePage = taillePage; }
}