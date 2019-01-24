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
import javafx.scene.shape.Ellipse;

/**
 *
 * @author dale
 */
public class EllipseVisualizer1 implements Visualizer {

    private final String name = "Ellipse Visualizer 1";

    private Integer numBands;
    private AnchorPane vizPane;

    private final Double bandHeightPercentage = 1.3;
    private final Double minEllipseRadius = 2.0;  // 10.0

    private Double width = 0.0;
    private Double height = 0.0;

    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;

    private final Double startHue = 260.0;

    private Ellipse[] ellipses;

    public EllipseVisualizer1() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();

        this.numBands = numBands;
        this.vizPane = vizPane;

        height = vizPane.getHeight() - 4;
        width = vizPane.getWidth();

        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        ellipses = new Ellipse[numBands];

        for (int i = 0; i < numBands; i++) {
            Ellipse ellipse = new Ellipse();
            ellipse.setCenterX(bandWidth / 2 + bandWidth * i);
            ellipse.setCenterY(height / 2);
            ellipse.setRadiusX(bandWidth / 2);
            ellipse.setRadiusY(minEllipseRadius);
            ellipse.setFill(Color.YELLOWGREEN);
            vizPane.getChildren().add(ellipse);
            ellipses[i] = ellipse;
        }
    }

    @Override
    public void end() {
        if (ellipses != null) {
            for (Ellipse ellipse : ellipses) {
                vizPane.getChildren().remove(ellipse);
            }
            vizPane = null;
            ellipses = null;
        }
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (ellipses == null) {
            return;
        }

        Integer num = min(ellipses.length, magnitudes.length);

        for (int i = 0; i < num; i++) {
            ellipses[i].setRadiusY(((60.0 + magnitudes[i]) / 60.0) * halfBandHeight + minEllipseRadius);
            ellipses[i].setFill(Color.CHARTREUSE);
        }

    }

    @Override
    public void start(HBox vizPane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
