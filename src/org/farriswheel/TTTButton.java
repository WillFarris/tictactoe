package org.farriswheel;

import javax.swing.*;

public class TTTButton extends JButton {
    private int tileX, tileY;

    TTTButton(String text, int x, int y) {
        super(text);
        this.tileX = x;
        this.tileY = y;
    }

    public int getTileX() {
        return tileX;
    }
    public int getTileY() { return tileY; }
}
