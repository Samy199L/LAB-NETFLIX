package org.example.NetflixLab.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.NetflixLab.algorithmes.Algorithme;
import org.example.NetflixLab.algorithmes.tri.TriBulle;
import org.example.NetflixLab.algorithmes.tri.TriInsertion;
import org.example.NetflixLab.algorithmes.tri.TriSelection;
import org.example.NetflixLab.dao.MediaDAO;
import org.example.NetflixLab.dao.MediaDAOPostgres;
import org.example.NetflixLab.model.*;
import org.example.NetflixLab.service.*;
import org.example.NetflixLab.util.LecteurCSV;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {

    @FXML private TextField champRecherche;
    @FXML private ComboBox<String> comboGenre;
    @FXML private ChoiceBox<String> choixType;
    @FXML private Slider sliderNoteMin;
    @FXML private Label labelNoteMin;

    @FXML private TableView<Media> tableMedias;
    @FXML private TableColumn<Media, String> colTitre;
    @FXML private TableColumn<Media, Integer> colAnnee;
    @FXML private TableColumn<Media, String> colGenre;
    @FXML private TableColumn<Media, Double> colNote;
    @FXML private TableColumn<Media, String> colType;

    @FXML private Label labelDetailTitre;
    @FXML private Label labelDetailAnnee;
    @FXML private Label labelDetailGenre;
    @FXML private Label labelDetailRealisateur;
    @FXML private Label labelDetailDuree;
    @FXML private Label labelDetailSynopsis;

    @FXML private Label labelPage;

    @FXML private TableView<Media> tableWatchlist;
    @FXML private TableColumn<Media, String> colWatchlistTitre;
    @FXML private TableColumn<Media, Integer> colWatchlistAnnee;
    @FXML private TableColumn<Media, String> colWatchlistGenre;

    @FXML private TableView<ResultatMesure> tableBenchmark;
    @FXML private TableColumn<ResultatMesure, String> colBenchAlgo;
    @FXML private TableColumn<ResultatMesure, String> colBenchComplexite;
    @FXML private TableColumn<ResultatMesure, Long> colBenchDuree;

    private final ServiceFiltrage serviceFiltrage = new ServiceFiltrage();
    private final ServicePagination servicePagination = new ServicePagination(25);
    private final ServiceTri serviceTri = new ServiceTri();
    private final Watchlist watchlist = new Watchlist();
    private final ObservableList<Media> itemsAffiches = FXCollections.observableArrayList();
    private ObservableList<Media> catalogueComplet;
    private final ServiceBenchmark serviceBenchmark = new ServiceBenchmark();
    private ObservableList<Media> mediasWatchlist = FXCollections.observableArrayList();
    private final ObservableList<ResultatMesure> itemsBenchmark = FXCollections.observableArrayList();
    private List<Media> resultatsCourants;
    private Media mediaSelectionne;
    private int pageActuelle = 1;
    private final MediaDAO mediaDAO = new MediaDAOPostgres();

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDonnees();
        remplirFiltres();
        configurerSelection();
        configurerEcouteursFiltres();
        configurerColonnesWatchlist();
        tableWatchlist.setItems(mediasWatchlist);
        configurerColonnesBenchmark();
        resultatsCourants = new ArrayList<>(catalogueComplet);
        tableBenchmark.setItems(itemsBenchmark);
        afficherPageActuelle();
        tableMedias.setItems(itemsAffiches);
        sliderNoteMin.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            labelNoteMin.setText(String.format("%.1f", nouvelleValeur.doubleValue()));
        });
    }



    private void configurerColonnes() {
        colTitre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitre()));
        colAnnee.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAnnee()).asObject());
        colGenre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGenre().toString()));
        colNote.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getNote()).asObject());
        colType.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue() instanceof Film ? "Film" : "Serie"));
    }

    private void configurerColonnesWatchlist() {
        colWatchlistTitre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTitre()));
        colWatchlistAnnee.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAnnee()).asObject());
        colWatchlistGenre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGenre().toString()));
    }


    private void chargerDonnees() {
        try {
            List<Media> medias = mediaDAO.trouverTous();
            catalogueComplet = FXCollections.observableArrayList(medias);
        } catch (RuntimeException e) {
            catalogueComplet = FXCollections.observableArrayList();
            new Alert(Alert.AlertType.ERROR,
                    "Impossible de se connecter à la base de données. Vérifiez que PostgreSQL est démarré.\n\n"
                            + e.getMessage()).showAndWait();
        }
    }

    private void remplirFiltres() {
        comboGenre.getItems().add("Tous");
        for (Genre g : Genre.values()) {
            comboGenre.getItems().add(g.toString());
        }
        comboGenre.setValue("Tous");

        choixType.getItems().addAll("Tous", "Films", "Series");
        choixType.setValue("Tous");
    }

    private void configurerSelection() {
        tableMedias.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
            if (nouveau != null) {
                mediaSelectionne = nouveau;
                afficherDetail(nouveau);
                tableMedias.refresh();
            }
        });
    }

    private void configurerEcouteursFiltres() {
        champRecherche.textProperty().addListener((obs, a, n) -> appliquerFiltresEtRecherche());
        comboGenre.valueProperty().addListener((obs, a, n) -> appliquerFiltresEtRecherche());
        choixType.valueProperty().addListener((obs, a, n) -> appliquerFiltresEtRecherche());
        sliderNoteMin.valueProperty().addListener((obs, a, n) -> appliquerFiltresEtRecherche());
    }



    private void appliquerFiltresEtRecherche() {
        FiltreCriteres criteres = new FiltreCriteres();

        if (comboGenre.getValue() != null && !comboGenre.getValue().equals("Tous")) {
            criteres.setGenre(Genre.valueOf(comboGenre.getValue()));
        }

        if (choixType.getValue() != null) {
            criteres.setType(switch (choixType.getValue()) {
                case "Films" -> TypeFilter.FILM;
                case "Series" -> TypeFilter.SERIE;
                default -> TypeFilter.TOUS;
            });
        }

        criteres.setNoteMin(sliderNoteMin.getValue());

        List<Media> filtres = serviceFiltrage.filtrer(catalogueComplet, criteres);
        resultatsCourants = serviceFiltrage.rechercherTexte(filtres, champRecherche.getText());

        pageActuelle = 1;
        afficherPageActuelle();
    }



    private void afficherPageActuelle() {
        List<Media> page = servicePagination.obtenirPage(resultatsCourants, pageActuelle);
        tableMedias.getSelectionModel().clearSelection();
        itemsAffiches.setAll(page);

        int totalPages = servicePagination.nombrePages(resultatsCourants);
        labelPage.setText("Page " + pageActuelle + " / " + totalPages);
    }

    @FXML
    private void onPagePrecedente() {
        if (pageActuelle > 1) {
            pageActuelle--;
            afficherPageActuelle();
        }
    }

    @FXML
    private void onPageSuivante() {
        int totalPages = servicePagination.nombrePages(resultatsCourants);
        if (pageActuelle < totalPages) {
            pageActuelle++;
            afficherPageActuelle();
        }
    }



    @FXML
    private void onTrierTitre() {
        resultatsCourants = serviceTri.trier(resultatsCourants, Comparator.comparing(Media::getTitre));
        pageActuelle = 1;
        afficherPageActuelle();
    }

    @FXML
    private void onTrierNote() {
        resultatsCourants = serviceTri.trier(resultatsCourants, Comparator.comparingDouble(Media::getNote).reversed());
        pageActuelle = 1;
        afficherPageActuelle();
    }

    @FXML
    private void onTrierAnnee() {
        resultatsCourants = serviceTri.trier(resultatsCourants, Comparator.comparingInt(Media::getAnnee));
        pageActuelle = 1;
        afficherPageActuelle();
    }



    private void afficherDetail(Media media) {
        labelDetailTitre.setText("Titre : " + media.getTitre());
        labelDetailAnnee.setText("Annee : " + media.getAnnee());
        labelDetailGenre.setText("Genre : " + media.getGenre());
        labelDetailRealisateur.setText("Realisateur : " + media.getRealisateur());
        labelDetailSynopsis.setText("Synopsis : " + media.getDescription());

        if (media instanceof Film film) {
            labelDetailDuree.setText("Duree : " + film.getDureeMinutes() + " min");
        } else if (media instanceof Serie serie) {
            labelDetailDuree.setText("Episodes : " + serie.getNombreEpisodes()
                    + " (" + serie.getNombreSaisons() + " saisons)");
        }
    }

    @FXML
    private void onAjouterWatchlist() {
        if (mediaSelectionne == null) return;

        boolean ajoute = watchlist.ajouter(mediaSelectionne);
        if (ajoute) {
            mediasWatchlist.setAll(watchlist.getMedias());
        } else {
            Alert alerte = new Alert(Alert.AlertType.INFORMATION, "Ce media est deja dans votre watchlist.");
            alerte.showAndWait();
        }
    }

    @FXML
    private void onRetirerWatchlist() {
        Media selection = tableWatchlist.getSelectionModel().getSelectedItem();
        if (selection != null) {
            watchlist.retirer(selection);
            mediasWatchlist.setAll(watchlist.getMedias());
        }
    }

    private void configurerColonnesBenchmark() {
        colBenchAlgo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNomAlgorithme()));
        colBenchComplexite.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getComplexiteTheorique()));
        colBenchDuree.setCellValueFactory(data ->
                new SimpleLongProperty(data.getValue().getDureeMillisecondes()).asObject());
    }

    @FXML
    private void onLancerBenchmark() {
        List<Algorithme<Media>> algorithmes = List.of(
                new TriBulle<>(),
                new TriSelection<>(),
                new TriInsertion<>()
        );

        List<ResultatMesure> resultats = serviceBenchmark.executerTous(
                algorithmes,
                catalogueComplet,
                Comparator.comparing(Media::getTitre)
        );


        itemsBenchmark.setAll(resultats);
    }


    @FXML
    private void onSupprimerMedia() {
        if (mediaSelectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Selectionnez d'abord un media.").showAndWait();
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer \"" + mediaSelectionne.getTitre() + "\" ?");
        confirmation.showAndWait().ifPresent(bouton -> {
            if (bouton == ButtonType.OK) {
                try {
                    mediaDAO.supprimer(mediaSelectionne.getId());
                    catalogueComplet.remove(mediaSelectionne);
                    mediaSelectionne = null;
                    appliquerFiltresEtRecherche();
                } catch (RuntimeException e) {
                    new Alert(Alert.AlertType.ERROR, "Suppression impossible : " + e.getMessage()).showAndWait();
                }
            }
        });
    }

    @FXML
    private void onAjouterMedia() {
        afficherFormulaire(null);
    }

    @FXML
    private void onModifierMedia() {
        if (mediaSelectionne == null) {
            new Alert(Alert.AlertType.WARNING, "Selectionnez d'abord un media.").showAndWait();
            return;
        }
        afficherFormulaire(mediaSelectionne);
    }

    private void afficherFormulaire(Media mediaExistant) {
        boolean modeModification = mediaExistant != null;

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(modeModification ? "Modifier un media" : "Ajouter un media");

        ChoiceBox<String> champType = new ChoiceBox<>(FXCollections.observableArrayList("Film", "Serie"));
        TextField champTitre = new TextField();
        TextField champTitreOriginal = new TextField();
        TextField champAnnee = new TextField();
        TextField champNote = new TextField();
        ComboBox<Genre> champGenre = new ComboBox<>(FXCollections.observableArrayList(Genre.values()));
        TextField champPays = new TextField();
        TextField champRealisateur = new TextField();
        TextArea champDescription = new TextArea();
        champDescription.setPrefRowCount(3);
        TextField champDuree = new TextField();
        TextField champSaisons = new TextField();
        TextField champEpisodes = new TextField();
        ChoiceBox<StatutSerie> champStatut = new ChoiceBox<>(FXCollections.observableArrayList(StatutSerie.values()));

        Label labelDuree = new Label("Duree (min) :");
        Label labelSaisons = new Label("Nb saisons :");
        Label labelEpisodes = new Label("Nb episodes :");
        Label labelStatut = new Label("Statut :");

        Runnable ajusterVisibilite = () -> {
            boolean estFilm = "Film".equals(champType.getValue());
            labelDuree.setVisible(estFilm); champDuree.setVisible(estFilm);
            labelSaisons.setVisible(!estFilm); champSaisons.setVisible(!estFilm);
            labelEpisodes.setVisible(!estFilm); champEpisodes.setVisible(!estFilm);
            labelStatut.setVisible(!estFilm); champStatut.setVisible(!estFilm);
        };
        champType.valueProperty().addListener((o, a, n) -> ajusterVisibilite.run());

        if (modeModification) {
            champType.setValue(mediaExistant instanceof Film ? "Film" : "Serie");
            champTitre.setText(mediaExistant.getTitre());
            champTitreOriginal.setText(mediaExistant.getTitreOriginal());
            champAnnee.setText(String.valueOf(mediaExistant.getAnnee()));
            champNote.setText(String.valueOf(mediaExistant.getNote()));
            champGenre.setValue(mediaExistant.getGenre());
            champPays.setText(mediaExistant.getPays());
            champRealisateur.setText(mediaExistant.getRealisateur());
            champDescription.setText(mediaExistant.getDescription());
            if (mediaExistant instanceof Film f) {
                champDuree.setText(String.valueOf(f.getDureeMinutes()));
            } else if (mediaExistant instanceof Serie s) {
                champSaisons.setText(String.valueOf(s.getNombreSaisons()));
                champEpisodes.setText(String.valueOf(s.getNombreEpisodes()));
                champStatut.setValue(s.getStatut());
            }
        } else {
            champType.setValue("Film");
        }
        ajusterVisibilite.run();

        GridPane grille = new GridPane();
        grille.setHgap(10); grille.setVgap(8);
        int ligne = 0;
        grille.addRow(ligne++, new Label("Type :"), champType);
        grille.addRow(ligne++, new Label("Titre :"), champTitre);
        grille.addRow(ligne++, new Label("Titre original :"), champTitreOriginal);
        grille.addRow(ligne++, new Label("Annee :"), champAnnee);
        grille.addRow(ligne++, new Label("Note (0-10) :"), champNote);
        grille.addRow(ligne++, new Label("Genre :"), champGenre);
        grille.addRow(ligne++, new Label("Pays :"), champPays);
        grille.addRow(ligne++, new Label("Realisateur :"), champRealisateur);
        grille.addRow(ligne++, new Label("Description :"), champDescription);
        grille.addRow(ligne++, labelDuree, champDuree);
        grille.addRow(ligne++, labelSaisons, champSaisons);
        grille.addRow(ligne++, labelEpisodes, champEpisodes);
        grille.addRow(ligne++, labelStatut, champStatut);

        dialog.getDialogPane().setContent(grille);
        ButtonType btnSauvegarder = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSauvegarder, ButtonType.CANCEL);

        dialog.setResultConverter(bouton -> {
            if (bouton != btnSauvegarder) return null;

            if (champTitre.getText().isBlank() || champGenre.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Titre et genre sont obligatoires.").showAndWait();
                return null;
            }
            int annee, duree = 0, saisons = 0, episodes = 0;
            double note;
            try {
                annee = Integer.parseInt(champAnnee.getText().trim());
                note = Double.parseDouble(champNote.getText().trim());
                if (note < 0 || note > 10) throw new NumberFormatException("note hors bornes");
                boolean estFilm = "Film".equals(champType.getValue());
                if (estFilm) {
                    duree = Integer.parseInt(champDuree.getText().trim());
                } else {
                    saisons = Integer.parseInt(champSaisons.getText().trim());
                    episodes = Integer.parseInt(champEpisodes.getText().trim());
                    if (champStatut.getValue() == null) throw new IllegalArgumentException("statut manquant");
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Verifiez les champs numeriques (annee, note, duree/saisons/episodes) et le statut.").showAndWait();
                return null;
            }

            Media media;
            int id = modeModification ? mediaExistant.getId() : 0;
            if ("Film".equals(champType.getValue())) {
                media = new Film(id, champTitre.getText(), champTitreOriginal.getText(), champDescription.getText(),
                        champPays.getText(), champGenre.getValue(), champRealisateur.getText(), annee, note, duree);
            } else {
                media = new Serie(id, champTitre.getText(), champTitreOriginal.getText(), champDescription.getText(),
                        champPays.getText(), champGenre.getValue(), champRealisateur.getText(), annee, note,
                        saisons, episodes, champStatut.getValue());
            }

            try {
                if (modeModification) {
                    mediaDAO.modifier(media);
                    int index = catalogueComplet.indexOf(mediaExistant);
                    catalogueComplet.set(index, media);
                } else {
                    Media mediaAjoute = mediaDAO.ajouter(media);
                    catalogueComplet.add(mediaAjoute);
                }
                appliquerFiltresEtRecherche();
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR, "Erreur base de donnees : " + e.getMessage()).showAndWait();
            }
            return null;
        });

        dialog.showAndWait();
    }
}