package org.farriswheel;

import jdk.internal.util.xml.impl.Input;
import org.farriswheel.Server.*;

import java.io.*;
import java.net.Socket;

public class ServerGameSession implements Runnable {

    public static final int BUFFERSIZE = 128;

    public static final String SETUPOK      = "SETUPACK";
    public static final String YOURMOVE     = "YOURMOVE";
    public static final String OTHERMOVE    = "OTHERMOV";
    public static final String ACK          = "ACKMESSG";
    public static final String MOVEFINISHED = "MVFINISH";
    public static final String TURNACK      = "TURNACKN";
    public static final String ACKMESSAGE   = "ACKMESSG";

    private ConnectedPlayer player0;

    private ConnectedPlayer player1;

    private byte [] buffer = new byte[BUFFERSIZE];

    private char [][] board;

    public ServerGameSession(Socket player0Socket, Socket player1Socket) {
        try {
            this.player0 = new ConnectedPlayer(player0Socket);
            this.player1 = new ConnectedPlayer(player1Socket);

            this.board = new char[3][3];
            for(int x=0;x<3;++x) {
                for(int y=0;y<3;++y) {
                    board[x][y] = ' ';
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            player0.readPlayerInfo('X');
            player1.readPlayerInfo('O');
            gameLoop();
            player0.close();
            player1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Closed Game");
    }

    private void gameLoop() throws IOException {

        do {
            player0.writeLine(YOURMOVE);
            String p0Move = player0.readLine();
            System.out.println("[SERVER] P0: "+ p0Move);
            player1.writeLine(OTHERMOVE);
            System.out.println("[SERVER] P1: "+player1.readLine());
            player1.writeLine(p0Move);
            System.out.println("[SERVER] P1: "+player1.readLine());

            player1.writeLine(YOURMOVE);
            String p1Move = player1.readLine();
            System.out.println("[SERVER] P1: "+ p0Move);
            player0.writeLine(OTHERMOVE);
            System.out.println("[SERVER] P0: "+player0.readLine());
            player0.writeLine(p1Move);
            System.out.println("[SERVER] P0: "+player0.readLine());


        } while (player0.isConnected() && player1.isConnected());
        System.out.println("Server terminating game loop");
    }


}
