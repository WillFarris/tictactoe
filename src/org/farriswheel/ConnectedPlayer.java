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
        this.nickname = readLine();
        this.playerSymbol = playerSymbol;
        out.write(playerSymbol);
        out.flush();
    }

    public String readLine() throws IOException {
        byte [] buffer = new byte[BUFFERSIZE];
        int len = in.read(buffer);
        return new String(buffer, 0, len);
    }

    public void writeLine(String message) throws IOException {
        out.write(message.getBytes());
        out.flush();
    }

    public void close() throws IOException {
        connection.close();
    }

    public boolean isConnected() {return connection.isConnected();}

}
