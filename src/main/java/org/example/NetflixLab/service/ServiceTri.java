package org.example.NetflixLab.service;

import org.example.NetflixLab.algorithmes.Algorithme;
import org.example.NetflixLab.algorithmes.tri.TriInsertion;
import org.example.NetflixLab.model.Media;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceTri {

    private final Algorithme<Media> algorithme = new TriInsertion<>();

    public List<Media> trier(List<Media> liste, Comparator<Media> comparateur) {
        List<Media> copie = new ArrayList<>(liste);
        algorithme.trier(copie, comparateur);
        return copie;
    }
}