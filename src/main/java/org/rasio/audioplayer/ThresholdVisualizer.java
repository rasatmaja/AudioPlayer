/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rasio.audioplayer;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author dale
 */
public class ThresholdVisualizer implements Visualizer {

    private final String name = "Threshold Visualizer";

    private HBox vizPane;
    private Rectangle[] rectangle;

    public ThresholdVisualizer() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void start(HBox vizPane) {
        end();
        this.vizPane = vizPane;

        if (rectangle == null) {
            rectangle = new Rectangle[23];
            for (int i = 0; i < 23; i++) {
                Rectangle rect = new Rectangle();
                rect.setHeight(4);
                rect.setWidth(15);
                rect.setFill(Color.web("#404040"));
                vizPane.getChildren().add(rect);
                rectangle[i] = rect;
            }
        }
    }

    @Override
    public void end() {
        if (rectangle != null) {
            for (int i = 0; i < 23; i++) {
                rectangle[i].setFill(Color.web("#404040"));
            }
        }
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (rectangle == null) {
            return;
        }
        Integer num = 23 - (int) ((23 * (magnitudes[2] * -1)) / 60);
        for (int i = 0; i < 23; i++) {
            rectangle[i].setFill(Color.web("#404040"));
        }
        for (int i = 0; i < num; i++) {
            rectangle[i].setFill(Color.CHARTREUSE);
        }

    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
