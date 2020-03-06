package org.farriswheel;

public class TicTacToe {

    public static void main(String[] args) {

        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("client")) {
                Thread c = new Thread(new Client("localhost", 9000, "Player"));
                c.start();
                try {
                    c.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            } else if (args[0].equalsIgnoreCase("server")) {
                Thread s = new Thread(new Server(9000));
                s.start();
                try {
                    s.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }

        Thread serverThread = new Thread(new Server(9000));
        serverThread.start();

        Thread client1 = new Thread(new Client("localhost", 9000, "Player 1"));
        client1.start();

        Thread client2 = new Thread(new Client("localhost", 9000, "Player 2"));
        client2.start();

        try {
            serverThread.join();
            client1.join();
            client2.join();
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}