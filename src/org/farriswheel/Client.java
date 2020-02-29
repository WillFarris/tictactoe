package org.farriswheel;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.farriswheel.ServerGameSession.*;

public class Client implements Runnable {

    private Socket connection;
    private InputStream in;
    private OutputStream out;

    private String hostname;
    private int port;
    private String nickname;
    private String symbol;

    private ClientView view;
    private boolean yourmove;

    public Client(String ip, int port, String nickname) {
        this.hostname = ip;
        this.port = port;
        this.nickname = nickname;
        this.symbol = " ";

        yourmove = false;
    }

    @Override
    public void run() {
        this.view = new ClientView("Tic Tac Toe - "+nickname, 800, 600, this);
        try {
            this.connection = new Socket(this.hostname, this.port);
            in = this.connection.getInputStream();
            out = this.connection.getOutputStream();

            writeLine(nickname);
            symbol = readLine();
            System.out.println(nickname + " is playing as " + symbol);
            gameLoop();
            connection.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(), e.getMessage());
            System.exit(-1);
        }
        System.out.println("Thread should exit after this message...");
    }

    private void gameLoop() throws IOException {
        String message = null;
        do {
            message = readLine();
            if(message == null)
                break;
            switch (message) {
                case YOURMOVE:
                    yourmove = true;
                    break;
                case OTHERMOVE:
                    otherMove();
                    break;
                case YOUWIN:
                    JOptionPane.showMessageDialog(view.getFrame(), "You Win!");
                    break;
                case YOULOSE:
                    JOptionPane.showMessageDialog(view.getFrame(), "You lose :(");
                    break;
                default:
                    break;
            }
        } while (connection.isConnected());
    }

    public void handleGameButton(TTTButton pressed) {
        if(yourmove) {
            try {
                writeLine(pressed.getTileX()+":"+pressed.getTileY());
            } catch (IOException e) {
                e.printStackTrace();
            }
            pressed.setText(symbol);
            pressed.setEnabled(false);
            yourmove = false;
        }
    }

    private void otherMove() throws IOException {
        writeLine(ACK);
        String [] move = readLine().split(":");
        TTTButton chosen = view.getByCoord(Integer.valueOf(move[0]), Integer.valueOf(move[1]));
        if(symbol.equals("X"))
            chosen.setText("O");
        else
            chosen.setText("X");
        chosen.setEnabled(false);
        writeLine(ACK);
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

    private String readLine() throws IOException {
        byte [] buffer = new byte[BUFFERSIZE];
        int len = in.read(buffer);
        if(len < 0)
            return null;
        return new String(buffer, 0, len);
    }

    private void writeLine(String message) throws IOException {
        out.write(message.getBytes());
        out.flush();
    }


}
