module org.example.NetflixLab {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.NetflixLab to javafx.fxml;
    opens org.example.NetflixLab.controller to javafx.fxml;
    opens org.example.NetflixLab.model to javafx.fxml;
    exports org.example.NetflixLab;
}