package com.pndb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    // private static Scene scene;
    private Pane pane;
    private int WIDTH = 640, HEIGHT = 480;

    @Override
    public void start(Stage stage) throws IOException {
        pane = new Pane();
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
    
        canvas.requestFocus();
        canvas.setFocusTraversable(true);
        pane.getChildren().add(canvas);
        TextState textState = new TextState(gc, WIDTH, HEIGHT);
        canvas.setOnKeyPressed(event -> {
            // System.out.println("event ::" + event.getCode()); 
            textState.handleKeyPress(event.getCode());
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}