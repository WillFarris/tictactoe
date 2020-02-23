package org.farriswheel;

public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new TTTServer(69));
        serverThread.start();

        TTTClient client = new TTTClient("localhost", 69, "Will");
    }
}