package org.example.NetflixLab.algorithmes.tri;

import org.example.NetflixLab.algorithmes.Algorithme;

import java.util.Comparator;
import java.util.List;

public class TriInsertion<T> implements Algorithme<T> {

    @Override
    public String nom() { return "Tri Insertion"; }

    @Override
    public String complexiteTheorique() { return "O(n^2)"; }

    @Override
    public void trier(List<T> liste, Comparator<T> comparateur) {
        int n = liste.size();
        for (int i = 1; i < n; i++) {
            T cle = liste.get(i);
            int j = i - 1;
            while (j >= 0 && comparateur.compare(liste.get(j), cle) > 0) {
                liste.set(j + 1, liste.get(j));
                j--;
            }
            liste.set(j + 1, cle);
        }
    }
}