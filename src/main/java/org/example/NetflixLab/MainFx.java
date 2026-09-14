package org.example.NetflixLab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/vue-principale.fxml"));
        Parent racine = loader.load();

        Scene scene = new Scene(racine, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("Samflix");
        stage.setScene(scene);
        stage.show();

        Image appIcon = new Image(getClass().getResourceAsStream("/Samflix.png"));
        stage.getIcons().add(appIcon);
    }

    public static void main(String[] args) {
        launch(args);
    }
}