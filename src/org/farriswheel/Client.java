package org.farriswheel;

import sun.lwawt.macosx.CSystemTray;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    public static final int BUFFERSIZE = 5;

    private Socket connection;
    private String hostname;
    private int port;


    private ClientView view;

    private OutputStreamWriter out;
    private InputStreamReader in;
    private char [] buffer;

    String nickname;

    public Client(String ip, int port, String nickname) {
        this.hostname = ip;
        this.port = port;
        this.nickname = nickname;
        this.buffer = new char[BUFFERSIZE];
    }

    @Override
    public void run() {
        this.view = new ClientView("Tic Tac Toe", 800, 600, this);
        try {
            this.connection = new Socket(this.hostname, this.port);
            in = new InputStreamReader(this.connection.getInputStream(), "UTF-8");
            out = new OutputStreamWriter(this.connection.getOutputStream(), "UTF-8");

            int len = in.read();
            in.read(buffer, 0, len);
            String message = this.nickname + ": " + buffer.toString();
            JOptionPane.showMessageDialog(view.getFrame(), message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(), e.getMessage());
            System.exit(1);
        }
    }

    public void handleGameButton(TTTButton pressed) {
        try
        {
            if (connection.isConnected()) {
                char [] coord = { 127, (char) pressed.getTileX(), (char) pressed.getTileY()};
                out.write(coord.toString());
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
