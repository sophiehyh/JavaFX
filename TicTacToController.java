package tictacto;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;


import java.awt.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TicTacToController implements Initializable {

    // Variablen mit Elementen aus FXML-Datei verknüpft
    @FXML GridPane gitter;
    @FXML StackPane paneZelle00;
    @FXML StackPane paneZelle01;
    @FXML StackPane paneZelle02;
    @FXML StackPane paneZelle10;
    @FXML StackPane paneZelle11;
    @FXML StackPane paneZelle12;
    @FXML StackPane paneZelle20;
    @FXML StackPane paneZelle21;
    @FXML StackPane paneZelle22;
    @FXML Text textErgebnis;
    @FXML Button neustart;

    private TicTacToModel model;
    private boolean spielLaeuft = true;
    private AudioClip gewonnen;
    private AudioClip verloren;
    private AudioClip click;
    private AudioClip background;

    @Override
    public void initialize(URL url, ResourceBundle rb) { // wird automatisch aufgerufen, nachdem Oberfläche geladen
        model = new TicTacToModel(); // erstellt Spielfeld

        // Event-Handler für alle 9 Zellen eintragen --> wenn Benutzer drauf klickt wird das ausgeführt
        paneZelle00.setOnMouseClicked(this::handlePaneZelle);
        paneZelle01.setOnMouseClicked(this::handlePaneZelle);
        paneZelle02.setOnMouseClicked(this::handlePaneZelle);
        paneZelle10.setOnMouseClicked(this::handlePaneZelle);
        paneZelle11.setOnMouseClicked(this::handlePaneZelle);
        paneZelle12.setOnMouseClicked(this::handlePaneZelle);
        paneZelle20.setOnMouseClicked(this::handlePaneZelle);
        paneZelle21.setOnMouseClicked(this::handlePaneZelle);
        paneZelle22.setOnMouseClicked(this::handlePaneZelle);
        neustart.setOnAction(this::handleNeustart);

        // Spielfeld rotiert (Rotationsanimation)
        RotateTransition rotateTransition = new RotateTransition();

        rotateTransition.setDuration(Duration.seconds(2));
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Transition.INDEFINITE);
        rotateTransition.setNode(gitter);
        rotateTransition.setByAngle(360);
        rotateTransition.play();

        gewonnen = soundLaden("applause.wav");
        verloren = soundLaden("gameover.wav");
        click = soundLaden("click.wav");

        background = soundLaden("background_music.wav");
        background.setCycleCount(AudioClip.INDEFINITE);
        soundAbspielen(background);
    }

    private AudioClip soundLaden(String dateiname) {
        try {
            return new AudioClip(
                    getClass().getResource(dateiname).toString());
        } catch (Exception e) {
            System.out.println("Sound nicht gefunden: " + dateiname);
            return null;
        }
    }

    private void soundAbspielen(AudioClip audioclip) {
        if (audioclip != null) {
            audioclip.play();
        }
    }

    // Wird aufgerufen, wenn der Benutzer eine Zelle anklickt
    public void handlePaneZelle(MouseEvent event) {
        // Kein Zug möglich wenn Spiel vorbei oder Computer dran
        if (!spielLaeuft) return;

        // Ermittelt, auf welches Feld geklickt wurde
        Node zelle = (Node) event.getSource();
        String fxId = zelle.getId(); // liest die ID, z.B. "paneZelle12"

        // Reihe und Spalte aus der fxId extrahieren
        // "paneZelle12" -> extrahiert Zeichen an Index 9 = '1', Index 10 = '2'
        int reihe = Character.getNumericValue(fxId.charAt(9));
        int spalte = Character.getNumericValue(fxId.charAt(10));

        // Nur ziehen, wenn Zelle leer
        if (model.getZelle(reihe, spalte) != TicTacToModel.leer) return;
        soundAbspielen(click);

        // Benutzer-Zug
        model.symbolSetzten(TicTacToModel.benutzer, reihe, spalte);
        symbolAnzeigen(reihe, spalte, TicTacToModel.benutzer);

        // Nach Benutzer-Zug prüfen
        if (ergebnisPruefen()) return;

        // Computer darf nicht klicken während Pause
        spielLaeuft = false;

        // Computer "denkt" 1 Sekunde
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(this::computerZug);
        pause.play();
    }


    // Wird nach der PauseTransition aufgerufen – Computer macht Zug
    public void computerZug(ActionEvent event) {
        int[] zelle = model.zufaelligeLeereZelle();

        if (zelle != null) {
            model.symbolSetzten(TicTacToModel.computer, zelle[0], zelle[1]);
            symbolAnzeigen(zelle[0], zelle[1], TicTacToModel.computer);
            ergebnisPruefen();
        }

        soundAbspielen(click);
        spielLaeuft = true;
    }

    private void symbolAnzeigen(int reihe, int spalte, char spieler) {
        // Zelle per fx:id aus dem Gitter holen
        Pane paneZelle = (Pane) gitter
                .lookup("#paneZelle" + reihe + spalte);

        // Text-Objekt erstellen
        Text text = new Text();
        text.setText(String.valueOf(spieler)); // schreibt X oder O rein
        text.setFont(new Font(60));

        // Symbol in die Mitte der Zelle positionieren
        text.setX(20);
        text.setY(75);

        // Farbe
        if (spieler == TicTacToModel.benutzer) {
            text.setStyle("-fx-fill: #2f7fc1;");
        } else {
            text.setStyle("-fx-fill: #b7e3ff;");
        }

        paneZelle.getChildren().add(text); // Fügt Text in entsprechende Zelle ein, sodass er sichtbar wird
    }

    public void handleNeustart(ActionEvent event) {

        if (background != null || gewonnen != null) { // stoppt die Hintergrundmusik und Applaus
            background.stop();
            gewonnen.stop();
        }

        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("TicTacToView.fxml")); // Lädt komplette Oberfläche neu
            Stage stage = (Stage) neustart.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void textMittigSetzen() {
        textErgebnis.applyCss();
        double breite = textErgebnis.getLayoutBounds().getWidth();
        textErgebnis.setLayoutX((600 - breite) / 2); // 600 = Fensterbreite
    }

    // prüft, ob das Spiel vorbei ist und zeigt dann Ergebnis an
    private boolean ergebnisPruefen() {
        char ergebnis = model.gewinnerErmitteln();

        if (ergebnis == TicTacToModel.benutzer) {
            textErgebnis.setText("Du gewinnst!");
            textMittigSetzen();
            textErgebnis.setStyle("-fx-fill: #2f7fc1;");
            soundAbspielen(gewonnen);
            spielLaeuft = false;
            neustart.setVisible(true);
            return true;
        } else if (ergebnis == TicTacToModel.computer) {
            textErgebnis.setText("Computer gewinnt!");
            textMittigSetzen();
            textErgebnis.setStyle("-fx-fill: #2f7fc1;");
            soundAbspielen(verloren);
            neustart.setVisible(true);
            spielLaeuft = false;
            return true;
        } else if (ergebnis == 'U') {
            textErgebnis.setText("Unentschieden!");
            textMittigSetzen();
            textErgebnis.setStyle("-fx-fill: #2f7fc1;");
            spielLaeuft = false;
            neustart.setVisible(true);
            return true;
        }
        return false; // Spiel läuft noch
    }
}
