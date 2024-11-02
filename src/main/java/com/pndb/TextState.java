package com.pndb;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.Toolkit;

public class TextState {
    static GraphicsContext gc;
    private static boolean capsOn = false;
    private static double CURSOR_WIDTH = 2.0d;
    private static double CURSOR_HEIGHT = 20.0d;
    private static double X_PADDING = 2.0d;
    private static double Y_PADDING = 10.0d;
    private static int lineNumber = 0, totalRows = 0, totalCols = 0;
    private static double initial_X = 0.0d, initial_Y = 30.0d;
    private static double X = initial_X, Y = initial_Y;
    private static double minCharWidth = Integer.MIN_VALUE;
    private static double minCharHeight = Integer.MIN_VALUE;
    private Font font = new Font("Arial", 14);
    static LL buff = new LL();
    static double APP_WIDTH, APP_HEIGHT;

    TextState(GraphicsContext gc, ReadOnlyDoubleProperty w, ReadOnlyDoubleProperty h) {
        TextState.gc = gc;
        APP_WIDTH = w.getValue();
        APP_HEIGHT = h.getValue();
        findFontDimensions();
        CURSOR_HEIGHT = minCharHeight;
        calculateTotalRowsAndCols();
        // X_PADDING = (0.05d * minCharWidth);
        // Y_PADDING = (0.05d * minCharHeight);
        // initial_Y = Y_PADDING;
        gc.setFont(font);
        gc.setFill(Color.BLACK);
        capsOn = Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
        System.out.println("capsOn: " + capsOn);

        drawCursor();
    }

    static String alphabetToDraw(KeyCode k) {
        if (capsOn) {
            return k.getChar();
        } else {
            return k.getChar().toLowerCase();
        }
    }

    static void calculateX() {
        X = X + X_PADDING;
    }

    static void calculateY() {
        Y = initial_Y + (lineNumber * (minCharHeight + Y_PADDING));
        if (Y + minCharHeight > APP_HEIGHT) {
            Y = initial_Y;
            lineNumber = 0;
        }
    }

    static void calculateTotalRowsAndCols() {
        totalRows = (int) Math.floor((APP_HEIGHT - initial_Y) / (minCharHeight + Y_PADDING));
        totalCols = (int) Math.floor((APP_WIDTH - X_PADDING - initial_X) / (minCharWidth + X_PADDING));
    }

    static void calculateCursorPosition() {
        calculateX();
        if (X + minCharWidth > APP_WIDTH) {
            X = 0;
            lineNumber++;
        }
        calculateY();
        System.out.println("x: " + X + " y: " + Y + " line: " + lineNumber);
    }

    static void drawCursor() {
        gc.fillRect(X, Y - CURSOR_HEIGHT, CURSOR_WIDTH, CURSOR_HEIGHT);
    }

    static void unDrawCursor() {
        gc.clearRect(X, Y - CURSOR_HEIGHT, CURSOR_WIDTH, CURSOR_HEIGHT);
    }

    static void toggleCaps() {
        capsOn = !capsOn;
        System.out.println("capsOn toggle: " + capsOn);
    }

    static void handleKeyPress(KeyCode inp) {
        System.out.println(inp);
        draw(inp);
    }

    static void draw(KeyCode inp) {
        switch (inp) {
            case CAPS:
                toggleCaps();
                break;
            case ENTER:
                buff.add(inp);
                unDrawCursor();
                lineNumber++;
                X = initial_X;
                calculateCursorPosition();
                drawCursor();
                break;
            default:
                buff.add(inp);
                unDrawCursor();
                calculateCursorPosition();
                gc.fillText(alphabetToDraw(inp), X, Y);
                X += minCharWidth;
                drawCursor();
                break;
        }
    }

    static void setAppWidth(double d) {
        APP_WIDTH = d;
        calculateTotalRowsAndCols();
        redraw();
    }

    static void setAppHeight(double d) {
        APP_HEIGHT = d;
        calculateTotalRowsAndCols();
        redraw();
    }

    static void redraw() {
        lineNumber = 0;
        // gc.clearRect(0, 0, Double.MAX_VALUE, Double.MAX_VALUE);
        X = initial_X;
        Y = initial_Y;
        Node curr = buff.getHead();
        while (curr != null) {
            draw(curr.k);
            curr = curr.next;
        }
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
