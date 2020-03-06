package org.farriswheel;

import java.io.*;
import java.net.Socket;

public class ServerGameSession implements Runnable {

    public static final int BUFFERSIZE = 8;

    public static final String SETUPINFO = "SETUPINF";  //request nickname
    public static final String YOURMOVE  = "YOURMOVE";
    public static final String OTHERMOVE = "OTHERMOV";
    public static final String MYMOVE    = "MYMOVE!!";
    public static final String BADMOVE   = "FAILMOVE";
    public static final String ACK       = "ACKMESSG";
    public static final String YOUWIN    = "YOUWIN!!";
    public static final String YOULOSE   = "YOULOSE!";
    public static final String QUITNOW   = "QUITNOW!";

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

    /* Beginning of the game
     *
     * Reads connection info from the players and delegates X and O symbols.
     * then runs the game loop
     */
    @Override
    public void run() {
        try {
            player0.readPlayerInfo('X');
            player1.readPlayerInfo('O');
            player0.writeLine(player1.getNickname());
            player0.readLine();
            player1.writeLine(player0.getNickname());
            player1.readLine();
            do {
                System.out.println("[SERVER] Beginning turn");
                if(handleTurn(player0, player1) == 1)
                    System.out.println(player0.getNickname()+" won!");
                if(handleTurn(player1, player0) == 1)
                    System.out.println(player1.getNickname()+"won!");
            } while(player0.isConnected() && player1.isConnected() && !player0.isClosed() && !player1.isClosed());
            player0.close();
            player1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] Game over");
    }

    /* Handles a single turn for a single player
     *
     * The server tells the client it is their move and awaits a message reply
     * The server then handles that message
     *
     * If the message received from the client is a MYMOVE message with a valid move,
     * the server will update the other player and check for a win
     */
    private int handleTurn(ConnectedPlayer current, ConnectedPlayer other) throws IOException {
        boolean validMoveReceived = false;
        boolean madeMove = false;
        boolean didWinGame = false;
        String playerMove = null;

        // Tell the client it's their move and read the result
        current.writeLine(YOURMOVE);
        String currentResponse = current.readLine();
        System.out.println("[SERVER] got message from "+current.getNickname()+": "+currentResponse);
        if(currentResponse == null)
            return 0;

        // Handle the message received in response
        switch (currentResponse) {
            case ACK:
                break;
            case MYMOVE:
                playerMove = current.readLine();
                while(!placeSymbol(playerMove, current.getSymbol())) {
                    current.writeLine(BADMOVE);
                    playerMove = current.readLine();
                }
                madeMove = true;
                break;
            case QUITNOW:
                other.writeLine(QUITNOW);
                current.close();
                other.close();
                break;
            default:
                break;
        }
        if(madeMove) {
            didWinGame = checkWin() == current.getSymbol();
            other.writeLine(OTHERMOVE);
            other.writeLine(playerMove + ":" + current.getSymbol()+":"+checkWin());
            other.readLine();
            if (didWinGame) {
                System.out.println(current.getNickname() + " wins");
                current.writeLine(YOUWIN);
                other.writeLine(YOULOSE);
                current.close();
                other.close();
                return 1;
            }
        }
        return 0;
    }

    /* Check if a move has already been made at a location
     * takes locations in the form of a string of two coordinates
     * separated by a colon, which is the expected format for a parameter
     * to the MYMOVE message from a client
     *
     * Returns true if a move can be made at the coordinate
     * Returns false if a move has already been made
     */
    private boolean checkSquare(String move, char symbol) {
        String [] coords = move.split(":");
        int x = Integer.valueOf(coords[0]);
        int y = Integer.valueOf(coords[1]);
        if(board[x][y] == ' ')
            return true;
        return false;
    }

    /* Takes the parameter received after the MYMOVE message from a client
     * Converts the message into x,y coordinates and attempts to place in grid
     *
     * Return true if placement was successful
     * Return false if a symbol exists at that location
     */
    private boolean placeSymbol(String move, char symbol) {
        String [] coords = move.split(":");
        int newX = Integer.valueOf(coords[0]);
        int newY = Integer.valueOf(coords[1]);
        if(newX > 2 || newY > 2) {
            System.out.println("duplicate or invalid move");
            return false;
        }
        if(board[newX][newY] != ' ') {
            System.out.println("duplicate or invalid move");
            return false;
        }
        board[newX][newY] = symbol;
        return true;
    }

    /* Checks board for win
     * Returns the symbol that won or a space if no win is found
     */
    private char checkWin() {
        for(int rc=0;rc<3;++rc) {
            if(board[rc][0] != ' ' && board[rc][0] == board[rc][1] && board[rc][1] == board[rc][2]) //check each row
                return  board[rc][0];
            if(board[0][rc] != ' ' && board[0][rc] == board[1][rc] && board[1][rc] == board[2][rc]) //check each column
                return  board[0][rc];
        }
        if(board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) //check diagonal from top left -> bottom right
            return board[0][0];
        if(board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) //check diagonal from bottom left -> top right
            return board[0][2];
        return ' ';
    }


}
