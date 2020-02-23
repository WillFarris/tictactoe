package org.farriswheel;

public class Main {
    public static void main(String[] args) {
        TTTServer server = new TTTServer(69);
        server.run();

        TTTClient client = new TTTClient("localhost", 69, "Will");
    }
}