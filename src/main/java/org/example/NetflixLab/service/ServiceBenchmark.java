package org.example.NetflixLab.service;

import org.example.NetflixLab.algorithmes.Algorithme;
import org.example.NetflixLab.model.Media;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceBenchmark {

    public List<ResultatMesure> executerTous(List<Algorithme<Media>> algorithmes,
                                                List<Media> catalogue,
                                                Comparator<Media> comparateur) {
        List<ResultatMesure> resultats = new ArrayList<>();

        for (Algorithme<Media> algo : algorithmes) {
            List<Media> copie = new ArrayList<>(catalogue);

            long debut = System.nanoTime();
            algo.trier(copie, comparateur);
            long fin = System.nanoTime();

            long dureeMs = (fin - debut) / 1_000_000;
            resultats.add(new ResultatMesure(algo.nom(), algo.complexiteTheorique(), dureeMs));
        }

        return resultats;
    }
}