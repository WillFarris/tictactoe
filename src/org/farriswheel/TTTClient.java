package org.farriswheel;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TTTClient {

    private Socket connection;
    private TTTClientView view;

    String nickname;

    public TTTClient(String ip, int port, String nickname) {
        this.nickname = nickname;

        this.view = new TTTClientView("Tic Tac Toe", 800, 600, this);
        try {
            this.connection = new Socket(ip, port);
            this.connection.getOutputStream().write('b');
            InputStream in = this.connection.getInputStream();
            JOptionPane.showMessageDialog(view.getFrame(), (char)in.read());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(), e.getMessage());
            System.exit(1);
        }

    }

    public void handleGameButton(TTTButton pressed) {
        JOptionPane.showMessageDialog(view.getFrame(), "Pressed (" + pressed.getTileX() + ", " + pressed.getTileY() + ")");
        pressed.setText("X");
        pressed.setEnabled(false);
    }
}
