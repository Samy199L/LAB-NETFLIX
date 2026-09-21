package org.example.NetflixLab.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connexion {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = Connexion.class.getResourceAsStream("/database.properties")) {
            if (is == null) {
                throw new IllegalStateException(
                        "database.properties introuvable dans les ressources. " +
                                "Copiez database.properties.example vers database.properties et remplissez vos valeurs.");
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger la configuration de la base de donnees", e);
        }
    }

    public static Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}