package org.farriswheel;

public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new TTTServer(9000));
        serverThread.start();

        TTTClient client = new TTTClient("localhost", 9000, "Will");
    }
}