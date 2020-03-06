package org.farriswheel;

import java.io.*;
import java.net.Socket;

public class ServerGameSession implements Runnable {

    public static final int BUFFERSIZE = 16;

    public static final String SETUPINFO   = "SETUPINFO";  //request nickname
    public static final String YOURMOVE  = "YOURMOVE";
    public static final String OTHERMOVE = "OTHERMOV";
    public static final String MYMOVE    = "MYMOVE";
    public static final String BADMOVE   = "BADMOVE";
    public static final String ACK       = "ACK";
    public static final String YOUWIN    = "YOUWIN";
    public static final String YOULOSE   = "YOULOSE";

    private ConnectedPlayer player0;
    private ConnectedPlayer player1;

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
            player0.writeLine("SETUPINFO");
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
            String player0Response = player0.readLine();
            player0.writeLine(ACK);
            int win = 0;
            switch (player0Response) {
                case MYMOVE:
                    while (checkWin(player0.readLine(), player0.getSymbol()) == -1) {
                        player0.writeLine(BADMOVE);
                    }
                    break;
                default:
                    break;
            }

            player1.writeLine(YOURMOVE);
            String player1Response = player1.readLine();
            player1.writeLine(ACK);
            win = 0;
            switch (player1Response) {
                default:
                    break;
            }

            /*player0.writeLine(YOURMOVE);
            String p0Move = player0.readLine();
            if (checkWin(p0Move, player0.getSymbol())) {
                player0.writeLine(YOUWIN);
                player1.writeLine(YOULOSE);
                break;
            }
            System.out.println("[SERVER] P0: "+ p0Move);
            player1.writeLine(OTHERMOVE);
            System.out.println("[SERVER] P1: "+player1.readLine());
            player1.writeLine(p0Move);
            System.out.println("[SERVER] P1: "+player1.readLine());

            player1.writeLine(YOURMOVE);
            String p1Move = player1.readLine();
            if(checkWin(p1Move, player1.getSymbol())) {
                player0.writeLine(YOULOSE);
                player1.writeLine(YOUWIN);
                break;
            }
            System.out.println("[SERVER] P1: "+ p0Move);
            player0.writeLine(OTHERMOVE);
            System.out.println("[SERVER] P0: "+player0.readLine());
            player0.writeLine(p1Move);
            System.out.println("[SERVER] P0: "+player0.readLine());*/


        } while (player0.isConnected() && player1.isConnected());
        System.out.println("Server terminating game loop");
    }

    /* checks board for win
     * Return codes:
     * 1  -> Game was won with move
     * 0  -> Game was not won with move
     * -1 -> The chosen spot has already been picked
     */
    private int checkWin(String move, char symbol) {
        String [] coords = move.split(":");
        int newX = Integer.valueOf(coords[0]);
        int newY = Integer.valueOf(coords[1]);
        if(board[newX][newY] != ' ')
            return -1;
        board[newX][newY] = symbol;

        for(int rc=0;rc<3;++rc) {
            if(board[rc][0] != ' ' && board[rc][0] == board[rc][1] && board[rc][1] == board[rc][2]) {
                return  1;
            }
            if(board[0][rc] != ' ' && board[0][rc] == board[1][rc] && board[1][rc] == board[2][rc]) {
                return  1;
            }
        }
        if(board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return 1;

        if(board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return 1;


        return 0;
    }


}
