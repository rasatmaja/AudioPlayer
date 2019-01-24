/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rasio.audioplayer;

import animatefx.animation.SlideInDown;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author rasio
 */
public class PlayerController implements Initializable {

    @FXML
    private AnchorPane title_bar;
    @FXML
    private Label title_name;
    @FXML
    private VBox download_item;
    @FXML
    private AnchorPane visualizer_pane;
    @FXML
    private OctIconView btn_close;
    @FXML
    private MaterialDesignIconView btn_play_pause;
    @FXML
    private HBox hbox_channel_right;
    @FXML
    private HBox hbox_channel_left;

    private Media media;
    private javafx.scene.media.MediaPlayer mediaPlayer;

    private final Integer numBands = 180;
    private final Double updateInterval = 0.05;
    private Visualizer currentVisualizer, channelRight, channelLeft;
    private boolean isPlaying = false;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label file_name;
    @FXML
    private Label file_source;
    @FXML
    private BorderPane main_layout;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        currentVisualizer = new EllipseVisualizer1();
        channelRight = new ThresholdVisualizer();
        channelLeft = new ThresholdVisualizer();

        channelRight.start(hbox_channel_right);
        channelLeft.start(hbox_channel_left);  
        
        
        movableWindow();
        title_name.setOnMouseClicked(event -> {
            openFileChooser();
        });

        btn_play_pause.setOnMouseClicked(event -> {
            if (!isPlaying) {
                play();
            } else {
                pause();
            }
        });

        btn_close.setOnMouseClicked(event -> {
            Stage stage = (Stage) btn_close.getScene().getWindow();
            stage.close();
        });
    }

    private void movableWindow() {
        title_bar.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) title_bar.getScene().getWindow();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //move around here
        title_bar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) title_bar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void play() {
        mediaPlayer.play();
        btn_play_pause.setGlyphName("PAUSE");
        isPlaying = true;
    }

    private void pause() {
        mediaPlayer.pause();
        btn_play_pause.setGlyphName("PLAY");
        isPlaying = false;
    }

    private void stop() {
        pause();
        mediaPlayer.stop();
        mediaPlayer.seek(Duration.ZERO);
    }

    private void openFileChooser() {
        Stage primaryStage = (Stage) title_bar.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {

            if (mediaPlayer != null) {
                media = null;
                stop();
                mediaPlayer = null;
            }

            openMedia(file);
        }
    }

    private void openMedia(File file) {
        file_name.setText(file.getName().toUpperCase());
        file_source.setText("SRC: " + file.getParentFile().getName().toUpperCase());
        try {
            media = new Media(file.toURI().toString());
            mediaPlayer = new javafx.scene.media.MediaPlayer(media);
            mediaPlayer.setOnReady(() -> {
                currentVisualizer.start(numBands, visualizer_pane);
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                stop();
            });
            mediaPlayer.setAudioSpectrumNumBands(numBands);
            mediaPlayer.setAudioSpectrumInterval(updateInterval);
            mediaPlayer.setAudioSpectrumListener((double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                currentVisualizer.update(timestamp, duration, magnitudes, phases);
                channelRight.update(timestamp, duration, magnitudes, phases);
                channelLeft.update(timestamp, duration, magnitudes, phases);
            });

            //play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
