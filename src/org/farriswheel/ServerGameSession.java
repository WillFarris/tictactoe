package org.farriswheel;

import java.io.*;
import java.net.Socket;

import static org.farriswheel.Client.BUFFERSIZE;

public class ServerGameSession implements Runnable {

    private Socket player1;
    private InputStream p1In;
    private OutputStream p1Out;
    private byte [] p1BufferIn;
    private byte [] p1BufferOut;

    private Socket player2;
    private InputStream p2In;
    private OutputStream p2Out;
    private byte [] p2BufferIn;
    private byte [] p2BufferOut;

    private final String SETUPCOMPLETE = "SETUPCOMPLETE";
    private final String CONFIGRECEIVED = "CONFIGRECEIVED";
    private final byte [] SETUP_COMPLETE = {69, 69, 69, 69, 69};


    public ServerGameSession(Socket player1, Socket player2) {
        try {
            this.player1 = player1;
            this.p1In = player1.getInputStream();
            this.p1Out = player1.getOutputStream();
            p1BufferIn = new byte[BUFFERSIZE];
            p1BufferOut = new byte[BUFFERSIZE];


            this.player2 = player2;
            this.p2In = player2.getInputStream();
            this.p2Out = player2.getOutputStream();
            p2BufferIn = new byte[BUFFERSIZE];
            p2BufferOut = new byte[BUFFERSIZE];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            p1Out.write(SETUPCOMPLETE.getBytes());
            p2Out.write(SETUPCOMPLETE.getBytes());
            do {
                p1Out.write(p1BufferOut);
                p1In.read(p1BufferIn);
                System.out.println("Player 1: ("+p1BufferIn[1]+ ", " + p1BufferIn[2] + ")");

                p2Out.write(p2BufferOut);
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
