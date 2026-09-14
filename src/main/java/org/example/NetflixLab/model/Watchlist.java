package org.example.NetflixLab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Watchlist {

    private final List<Media> medias = new ArrayList<>();

    public boolean ajouter(Media media) {
        if (medias.contains(media)) {
            return false;
        }
        return medias.add(media);
    }

    public boolean retirer(Media media) {
        return medias.remove(media);
    }

    public boolean contient(Media media) {
        return medias.contains(media);
    }

    public List<Media> getMedias() {
        return Collections.unmodifiableList(medias);
    }

    public int taille() {
        return medias.size();
    }
}