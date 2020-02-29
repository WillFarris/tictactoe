package org.farriswheel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket server  = null;
    private int port;

    private Socket player1 = null;
    private Socket player2 = null;

    private ExecutorService gameThreadPool = null;
    private int gameSessions = 0;
    private final int MAX_GAME_SESSIONS = 5;



    public Server(int port) {
        this.port = port;
        this.gameThreadPool = Executors.newFixedThreadPool(MAX_GAME_SESSIONS);
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                player1 = server.accept();
                player2 = server.accept();
                ++gameSessions;
                ServerGameSession session = new ServerGameSession(player1, player2);

                gameThreadPool.execute(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
