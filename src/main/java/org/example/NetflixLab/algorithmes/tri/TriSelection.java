package org.example.NetflixLab.algorithmes.tri;

import org.example.NetflixLab.algorithmes.Algorithme;

import java.util.Comparator;
import java.util.List;

public class TriSelection<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri Selection"; }

    @Override
    public String complexiteTheorique() { return "O(n^2)"; }

    @Override
    public void trier(List<T> liste, Comparator<T> comparateur) {
        int n = liste.size();
        for (int i = 0; i < n - 1; i++) {
            int indexMin = i;
            for (int j = i + 1; j < n; j++) {
                if (comparateur.compare(liste.get(j), liste.get(indexMin)) < 0) {
                    indexMin = j;
                }
            }
            T temp = liste.get(i);
            liste.set(i, liste.get(indexMin));
            liste.set(indexMin, temp);
        }
    }
}