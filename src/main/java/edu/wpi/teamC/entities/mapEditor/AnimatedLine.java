package edu.wpi.teamC.entities.mapEditor;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedLine extends Application {
    @Override
    public void start(Stage stage) {
        Line line = new Line(20, 100, 80, 20);
        line.getStrokeDashArray().setAll(25d, 5d, 5d, 5d);
        line.setStrokeWidth(2);

        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a + b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                0,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(4),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.setScene(new Scene(new Group(line), 100, 120));
        stage.show();
    }
    public Line animateLine(Line l){
        l.getStrokeDashArray().setAll(25d, 5d, 5d, 5d);
        final double maxOffset =
                l.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a + b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                l.strokeDashOffsetProperty(),
                                0,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(
                                l.strokeDashOffsetProperty(),
                                -maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return l;

    }
    public static void main(String[] args) {
        launch(args);
    }
}