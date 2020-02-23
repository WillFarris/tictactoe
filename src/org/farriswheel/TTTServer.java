package org.farriswheel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TTTServer implements Runnable {

    private ServerSocket server  = null;
    private Socket       socket  = null;
    private InputStream  in      = null;
    private OutputStream out     = null;

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
            out = socket.getOutputStream();

            byte [] buffer = new byte[3];
            do {
                in.read(buffer);
                System.out.println("(" +  buffer[1] + ", " + buffer[2] + ")");
            } while(buffer[0] == 127);
            if(buffer[0] == 0) {
                byte [] payload = {0, 1, 2, 3, 4};
                out.write(payload);
            }
            socket.close();
            System.out.println("Closed socket");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
