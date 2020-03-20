package org.farriswheel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket server  = null;

    private Socket player1 = null;
    private Socket player2 = null;

    private ExecutorService gameThreadPool;
    private int gameSessions = 0;

    public Server(int port, int maxSessions) {
        this.gameThreadPool = Executors.newFixedThreadPool(maxSessions);

        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port " + port + "\nCan handle "+maxSessions+" games ("+2*maxSessions+" players)\nWaiting for connection...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                if(gameSessions < maxSessions) {
                    player1 = server.accept();
                    player2 = server.accept();
                    ++gameSessions;
                    ServerGameSession session = new ServerGameSession(player1, player2);
                    gameThreadPool.execute(session);
                } else {
                    System.out.println("Max games reached");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
