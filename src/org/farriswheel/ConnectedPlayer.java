package org.farriswheel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.farriswheel.ServerGameSession.*;

public class ConnectedPlayer {

    private Socket connection;
    private InputStream in;
    private OutputStream out;
    private String nickname;
    private char playerSymbol;

    public ConnectedPlayer(Socket socket) throws IOException {
        this.connection = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public String getNickname() {return this.nickname;}
    public char getSymbol() {return this.playerSymbol;}
    public Socket getConnection() {return this.connection;}

    public void readPlayerInfo(char playerSymbol) throws IOException {
        writeLine(SETUPINFO);
        this.nickname = readLine();
        this.playerSymbol = playerSymbol;
        out.write(playerSymbol);
        out.flush();
        System.out.print("[SERVER] "+ nickname + " is playing as "+ playerSymbol + ": ");
        System.out.println(readLine()); //get ACK from player to signify the assigned symbol was received
    }

    public String readLine() throws IOException {
        if(!connection.isConnected() || connection.isClosed())
            return null;
        byte [] buffer = new byte[BUFFERSIZE];
        int len = in.read(buffer);
        if(len < 0)
            return null;
        return new String(buffer, 0, len);
    }

    public void writeLine(String message) throws IOException {
        if(!connection.isConnected() || connection.isClosed())
            return;
        out.write(message.getBytes());
        out.flush();
    }

    public void close() throws IOException {
        connection.close();
    }

    public boolean isConnected() {return connection.isConnected();}

    public boolean isClosed() {return connection.isClosed();}

}
