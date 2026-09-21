package org.example.NetflixLab.dao;

import org.example.NetflixLab.model.Media;

import java.util.List;
import java.util.Optional;

public interface MediaDAO {
    List<Media> trouverTous();
    Optional<Media> trouverParId(int id);
    Media ajouter(Media media);
    void modifier(Media media);
    void supprimer(int id);
}
