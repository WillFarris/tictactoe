package org.farriswheel;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TTTClient {

    private Socket connection;
    private TTTClientView view;

    private InputStream in;
    private OutputStream out;

    String nickname;

    public TTTClient(String ip, int port, String nickname) {
        this.nickname = nickname;

        this.view = new TTTClientView("Tic Tac Toe", 800, 600, this);
        try {
            this.connection = new Socket(ip, port);
            in = this.connection.getInputStream();
            out = this.connection.getOutputStream();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(), e.getMessage());
            System.exit(1);
        }
    }

    public void handleGameButton(TTTButton pressed) {
        try
        {
            if (connection.isConnected()) {
                byte [] coord = { 127, (byte) pressed.getTileX(), (byte) pressed.getTileY()};
                out.write(coord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pressed.setText("X");
        pressed.setEnabled(false);
    }

    public void handleMetaButton(JButton pressed) {
        try {
            if(connection.isConnected()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
