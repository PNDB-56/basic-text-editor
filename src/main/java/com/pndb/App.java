package com.pndb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;
    private Pane pane;
    private int WIDTH = 400, HEIGHT = 300;
    private TextState textState;
    private GraphicsContext gc;

    @Override
    public void start(Stage stage) throws IOException {
        pane = new StackPane();
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        Canvas canvas = new Canvas();

        canvas.requestFocus();
        canvas.setFocusTraversable(true);
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        gc = canvas.getGraphicsContext2D();
        textState = new TextState(gc, pane.widthProperty(), pane.heightProperty());
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            TextState.APP_WIDTH = newVal.doubleValue();
        });
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            TextState.APP_HEIGHT = newVal.doubleValue();
        });

        pane.getChildren().add(canvas);

        canvas.setOnKeyPressed(event -> {
            // System.out.println("event ::" + event.getCode());
            textState.handleKeyPress(event.getCode());
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}