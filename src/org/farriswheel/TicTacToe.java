package org.farriswheel;

public class TicTacToe {

    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server(9000));
        serverThread.start();

        Thread client1 = new Thread(new Client("localhost", 9000, "Player 1"));
        client1.start();

        Thread client2 = new Thread(new Client("localhost", 9000, "Player 2"));
        client2.start();
    }
}