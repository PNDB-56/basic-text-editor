package com.pndb;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.Toolkit;

public class TextState {
    GraphicsContext gc;
    private boolean capsOn = false;
    private double CURSOR_WIDTH = 1.0d;
    private double CURSOR_HEIGHT = 20.0d;
    private double X_PADDING = 2.0d;
    private double Y_PADDING = 10.0d;
    private int lineNumber = 0;
    private double initial_X = 0.0d, initial_Y = 30.0d;
    private double X = initial_X, Y = initial_Y;
    private double minCharWidth = Integer.MIN_VALUE;
    private double minCharHeight = Integer.MIN_VALUE;
    private Font font = new Font("Arial", 14);
    static LL buff = new LL();
    static double APP_WIDTH, APP_HEIGHT;

    TextState(GraphicsContext gc, ReadOnlyDoubleProperty w, ReadOnlyDoubleProperty h) {
        this.gc = gc;
        APP_WIDTH = w.getValue();
        APP_HEIGHT = h.getValue();
        this.findFontDimensions();
        this.CURSOR_HEIGHT = minCharHeight;
        // this.X_PADDING = (0.05d * minCharWidth);
        // this.Y_PADDING =  (0.05d * minCharHeight);
        // this.initial_Y = Y_PADDING;
        this.gc.setFont(font);
        this.gc.setFill(Color.BLACK);
        capsOn = Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
        System.out.println("capsOn: " + capsOn);

        this.drawCursor();
    }

    String alphabetToDraw(KeyCode k) {
        if (capsOn) {
            return k.getChar();
        } else {
            return k.getChar().toLowerCase();
        }
    }

    void calculateX() {
        X = X + X_PADDING;
    }

    void calculateY() {
        Y = initial_Y + (lineNumber * (minCharHeight + Y_PADDING));
        if (Y + minCharHeight > APP_HEIGHT) {
            Y = this.initial_Y;
            lineNumber = 0;
        }
    }

    void calculateCursorPosition() {
        this.calculateX();
        if (X + minCharWidth > APP_WIDTH) {
            X = 0;
            lineNumber++;
            this.calculateY();
        }
        System.out.println("x: " + X + " y: " + Y + " line: " + lineNumber);
    }

    void drawCursor() {
        this.calculateCursorPosition();
        this.gc.fillRect(X, Y - CURSOR_HEIGHT, CURSOR_WIDTH, CURSOR_HEIGHT);
    }

    void unDrawCursor() {
        this.gc.clearRect(X, Y - CURSOR_HEIGHT, CURSOR_WIDTH, CURSOR_HEIGHT);
    }

    void toggleCaps() {
        this.capsOn = !this.capsOn;
        System.out.println("capsOn toggle: " + capsOn);
    }

    void handleKeyPress(KeyCode inp) {
        System.out.println(inp);
        if (inp == KeyCode.CAPS) {
            toggleCaps();
        } else {
            buff.add(inp);
            draw(inp);
        }
    }

    void draw(KeyCode inp) {
        this.unDrawCursor();
        this.calculateCursorPosition();
        gc.fillText(this.alphabetToDraw(inp), X, Y);
        X += minCharWidth;
        this.drawCursor();
    }

    void findFontDimensions() {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            // Create a Text object for the character
            Text text = new Text(String.valueOf(ch));

            text.setFont(font);

            // Get the bounding box of the character
            double width = text.getLayoutBounds().getWidth();
            double height = text.getLayoutBounds().getHeight();

            minCharWidth = Math.max(minCharWidth, width);
            minCharHeight = Math.max(minCharHeight, height);

            // System.out.printf("Character: %c, Width: %.2f pixels, Height: %.2f pixels%n",
            // ch, width, height);
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            // Create a Text object for the character
            Text text = new Text(String.valueOf(ch));

            text.setFont(font);

            // Get the bounding box of the character
            double width = text.getLayoutBounds().getWidth();
            double height = text.getLayoutBounds().getHeight();
            minCharWidth = Math.max(minCharWidth, width);
            minCharHeight = Math.max(minCharHeight, height);
            // Output the dimensions
            // System.out.printf("Character: %c, Width: %.2f pixels, Height: %.2f pixels%n",
            // ch, width, height);
        }

        for (char ch = '0'; ch <= '9'; ch++) {
            // Create a Text object for the character
            Text text = new Text(String.valueOf(ch));

            text.setFont(font);

            // Get the bounding box of the character
            double width = text.getLayoutBounds().getWidth();
            double height = text.getLayoutBounds().getHeight();

            // Output the dimensions
            minCharWidth = Math.max(minCharWidth, width);
            minCharHeight = Math.max(minCharHeight, height);
            // System.out.printf("Character: %c, Width: %.2f pixels, Height: %.2f pixels%n",
            // ch, width, height);
        }

        System.out.printf("MinWidth: %.2f pixels, MinHeight: %.2f pixels%n", minCharWidth, minCharHeight);
    }
}
