package org.example.NetflixLab.dao;

import org.example.NetflixLab.model.*;
import java.sql.*;
import java.util.*;

public class MediaDAOPostgres implements MediaDAO {

    private static final String SELECT_BASE =
            "SELECT m.*, g.nom AS genre_nom FROM media m JOIN genre g ON m.genre_id = g.id";

    @Override
    public List<Media> trouverTous() {
        List<Media> resultats = new ArrayList<>();
        try (Connection cnx = Connexion.obtenirConnexion();
             PreparedStatement ps = cnx.prepareStatement(SELECT_BASE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) resultats.add(mapper(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la lecture des médias", e);
        }
        return resultats;
    }

    @Override
    public Optional<Media> trouverParId(int id) {
        String sql = SELECT_BASE + " WHERE m.id = ?";
        try (Connection cnx = Connexion.obtenirConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapper(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du média " + id, e);
        }
    }

    @Override
    public Media ajouter(Media media) {
        String sql = """
        INSERT INTO media (type, titre, titre_original, annee, note, genre_id,
                            pays, realisateur, description, duree_minutes,
                            nombre_saisons, nombre_episodes, statut)
        VALUES (?,?,?,?,?, (SELECT id FROM genre WHERE nom = ?), ?,?,?,?,?,?,?)
        """;
        try (Connection cnx = Connexion.obtenirConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            remplirParametres(ps, media);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return construireAvecId(media, keys.getInt(1));
                }
            }
            return media;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du média", e);
        }
    }

    private Media construireAvecId(Media media, int id) {
        if (media instanceof Film f) {
            return new Film(id, f.getTitre(), f.getTitreOriginal(), f.getDescription(), f.getPays(),
                    f.getGenre(), f.getRealisateur(), f.getAnnee(), f.getNote(), f.getDureeMinutes());
        } else {
            Serie s = (Serie) media;
            return new Serie(id, s.getTitre(), s.getTitreOriginal(), s.getDescription(), s.getPays(),
                    s.getGenre(), s.getRealisateur(), s.getAnnee(), s.getNote(),
                    s.getNombreSaisons(), s.getNombreEpisodes(), s.getStatut());
        }
    }

    @Override
    public void modifier(Media media) {
        String sql = """
        UPDATE media SET type=?, titre=?, titre_original=?, annee=?, note=?,
            genre_id=(SELECT id FROM genre WHERE nom = ?), pays=?, realisateur=?,
            description=?, duree_minutes=?, nombre_saisons=?, nombre_episodes=?, statut=?
        WHERE id=?
        """;
        try (Connection cnx = Connexion.obtenirConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            int derniereColonne = remplirParametres(ps, media);
            ps.setInt(derniereColonne, media.getId());
            int lignes = ps.executeUpdate();
            if (lignes == 0) throw new RuntimeException("Aucun média avec l'id " + media.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du média", e);
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM media WHERE id = ?";
        try (Connection cnx = Connexion.obtenirConnexion();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            int lignes = ps.executeUpdate();
            if (lignes == 0) throw new RuntimeException("Aucun média avec l'id " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du média " + id, e);
        }
    }

    private int remplirParametres(PreparedStatement ps, Media media) throws SQLException {
        boolean estFilm = media instanceof Film;
        ps.setString(1, estFilm ? "FILM" : "SERIE");
        ps.setString(2, media.getTitre());
        ps.setString(3, media.getTitreOriginal());
        ps.setInt(4, media.getAnnee());
        ps.setDouble(5, media.getNote());
        ps.setString(6, media.getGenre().name());
        ps.setString(7, media.getPays());
        ps.setString(8, media.getRealisateur());
        ps.setString(9, media.getDescription());
        if (estFilm) {
            ps.setInt(10, ((Film) media).getDureeMinutes());
            ps.setNull(11, Types.INTEGER);
            ps.setNull(12, Types.INTEGER);
            ps.setNull(13, Types.VARCHAR);
        } else {
            Serie s = (Serie) media;
            ps.setNull(10, Types.INTEGER);
            ps.setInt(11, s.getNombreSaisons());
            ps.setInt(12, s.getNombreEpisodes());
            ps.setString(13, s.getStatut().name());
        }
        return 14;
    }

    private Media mapper(ResultSet rs) throws SQLException {
        Genre genre = Genre.valueOf(rs.getString("genre_nom"));
        String type = rs.getString("type");
        if (type.equals("FILM")) {
            return new Film(rs.getInt("id"), rs.getString("titre"), rs.getString("titre_original"),
                    rs.getString("description"), rs.getString("pays"), genre, rs.getString("realisateur"),
                    rs.getInt("annee"), rs.getDouble("note"), rs.getInt("duree_minutes"));
        } else {
            return new Serie(rs.getInt("id"), rs.getString("titre"), rs.getString("titre_original"),
                    rs.getString("description"), rs.getString("pays"), genre, rs.getString("realisateur"),
                    rs.getInt("annee"), rs.getDouble("note"),
                    rs.getInt("nombre_saisons"), rs.getInt("nombre_episodes"),
                    StatutSerie.valueOf(rs.getString("statut")));
        }
    }
}