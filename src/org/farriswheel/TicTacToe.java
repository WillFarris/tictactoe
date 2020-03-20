package org.farriswheel;


public class TicTacToe {

    public static void main(String[] args) {

        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("client")) {
                new Client("localhost", 9000, "Player");
            } else if (args[0].equalsIgnoreCase("server")) {
                if(args.length < 3) {
                    System.err.println("usage: java -jar TicTacToe.jar server <port> <max games>");
                    System.exit(-1);
                }
                new Server(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
            }
        } else {
            System.err.println("TicTacToe: usage: java -jar TicTacToe.jar <client/server> <options>");
        }
    }
}