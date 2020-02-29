package org.farriswheel;

import org.farriswheel.Server.*;

import java.io.*;
import java.net.Socket;

public class ServerGameSession implements Runnable {

    private static final String READYFORMOVE = "READYFORMOVE";
    private Socket player1;
    private InputStreamReader p1In;
    private OutputStreamWriter p1Out;
    private char [] p1BufferIn;

    private Socket player2;
    private InputStreamReader p2In;
    private OutputStreamWriter p2Out;
    private char [] p2BufferIn;

    public ServerGameSession(Socket player1, Socket player2) {
        try {
            this.player1 = player1;
            this.p1In = new InputStreamReader(player1.getInputStream(), "UTF-8");
            this.p1Out = new OutputStreamWriter(player1.getOutputStream(), "UTF-8");
            this.p1BufferIn = new char[100];

            this.player2 = player2;
            this.p2In = new InputStreamReader(player2.getInputStream(), "UTF-8");
            this.p2Out = new OutputStreamWriter(player2.getOutputStream(), "UTF-8");
            this.p2BufferIn = new char[100];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            do {
                p1Out.write(READYFORMOVE.length());
                p1Out.write(READYFORMOVE);
                p1In.read(p1BufferIn);
                System.out.println("Player 1: ("+p1BufferIn[1]+ ", " + p1BufferIn[2] + ")");

                p2Out.write(READYFORMOVE);
                p2In.read(p2BufferIn);
                System.out.println("Player 2: ("+p2BufferIn[1]+ ", " + p2BufferIn[2] + ")");
            } while (player1.isConnected() && player2.isConnected());
            player1.close();
            player2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Closed Game");
    }
}
