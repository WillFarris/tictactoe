package org.farriswheel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TTTServer implements Runnable {

    private ServerSocket server = null;
    private Socket socket       = null;
    private InputStream in  = null;

    private int port;

    public TTTServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port "+ port);

            socket = server.accept();
            in = socket.getInputStream();
            System.out.println(in.read());
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
