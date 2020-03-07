package org.farriswheel;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.farriswheel.ServerGameSession.*;

public class Client implements Runnable {

    private Socket connection;
    private InputStream in;
    private OutputStream out;

    private String hostname;
    private int port;
    private String myNickname;
    private String otherNickname;
    private String symbol;

    private ClientView view;
    private JTextField hostnameField;
    private JTextField portField;
    private JTextField nicknameField;
    private boolean yourmove;

    public Client(String ip, int port, String myNickname) {
        this.hostname = ip;
        this.port = port;
        this.myNickname = myNickname;
        this.symbol = " ";
        yourmove = false;
    }

    private JPanel getHostnamePanel() {
        JPanel setupInputDialogPanel = new JPanel();
        setupInputDialogPanel = new JPanel();
        setupInputDialogPanel.add(new JLabel("Hostname: "));
        setupInputDialogPanel.add(hostnameField);
        setupInputDialogPanel.add(Box.createHorizontalStrut(15));
        setupInputDialogPanel.add(new JLabel("Port: "));
        setupInputDialogPanel.add(portField);
        setupInputDialogPanel.add(Box.createHorizontalStrut(15));
        setupInputDialogPanel.add(new JLabel("Nickname: "));
        setupInputDialogPanel.add(nicknameField);
        setupInputDialogPanel.add(Box.createHorizontalStrut(15));
        return  setupInputDialogPanel;
    }

    public Client() {
        this.symbol = " ";
        yourmove = false;
    }

    @Override
    public void run() {
        hostnameField = new JTextField(8);
        portField = new JTextField(8);
        nicknameField = new JTextField(8);
        int res = JOptionPane.showConfirmDialog(null, getHostnamePanel(), "Configure your game", JOptionPane.OK_CANCEL_OPTION);
        if(res == JOptionPane.OK_OPTION)
        {
            this.myNickname = nicknameField.getText();
            this.hostname = hostnameField.getText();
            this.port = Integer.valueOf(portField.getText());
        } else {
            System.exit(-1);
        }
        this.view = new ClientView("Tic Tac Toe - " + myNickname, 800, 600, this);
        try {
            this.connection = new Socket(this.hostname, this.port);
            in = this.connection.getInputStream();
            out = this.connection.getOutputStream();

            if(!readLine().equals(SETUPINFO)) { //the first message we get from the server should always request information about the client
                JOptionPane.showMessageDialog(view.getFrame(), "There was an error communicating with the server, exiting...");
                connection.close();
            }
            writeLine(myNickname);
            symbol = readLine();
            writeLine(ACK); //send the server an ACK message to let it know we got the symbol assignment
            otherNickname = readLine();
            writeLine(ACK);
            System.out.println("["+myNickname+"] You are "+symbol+". You are playing against "+otherNickname);
            JOptionPane.showMessageDialog(view.getFrame(), "Connected! You are playing as "+symbol+" against "+otherNickname);
            gameLoop();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(), e.getMessage());
            System.exit(-1);
        }
        System.exit(0);
    }

    private void gameLoop() throws IOException {
        String message = null;
        do {
            message = readLine();
            if(message == null) {
                System.err.println("["+myNickname+"]: Error getting message from server, quitting...");
                JOptionPane.showMessageDialog(view.getFrame(), "Lost connecting to server, quitting...");
                System.exit(-1);
            }
            System.out.println("["+myNickname+"]: got message from server: "+message);
            //writeLine(ACK);
            switch (message) {
                case BADMOVE:
                case YOURMOVE:
                    yourmove = true;
                    break;
                case OTHERMOVE:
                    otherMove();
                    break;
                case YOUWIN:
                    JOptionPane.showMessageDialog(view.getFrame(), "You Win!");
                    connection.close();
                    break;
                case YOULOSE:
                    JOptionPane.showMessageDialog(view.getFrame(), "You lose :(");
                    connection.close();
                    break;
                case QUITNOW:
                    JOptionPane.showMessageDialog(view.getFrame(), otherNickname + " quit");
                default: break;
            }
        } while(connection.isConnected() && !connection.isClosed());
    }

    public void handleGameButton(TTTButton pressed) {
        if(yourmove) {
            try {
                writeLine(MYMOVE);
                writeLine(pressed.getTileX()+":"+pressed.getTileY());
            } catch (IOException e) {
                e.printStackTrace();
            }
            pressed.setText(symbol);
            yourmove = false;
        }
    }

    /* Handles parameters for the OTHERMOVE message from the server
     * parameter to the OTHERMOVE message is a 5-character
     */
    private void otherMove() throws IOException {
        String [] move = readLine().split(":");
        TTTButton chosenByOtherPlayer = view.getByCoord(Integer.valueOf(move[0]), Integer.valueOf(move[1]));
        chosenByOtherPlayer.setText(move[2]);
        chosenByOtherPlayer.setEnabled(false);
        writeLine(ACK);
    }


    public void handleMetaButton(JButton pressed) {
        try {
            writeLine(QUITNOW);
            if(connection.isConnected() || !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readLine() throws IOException {
        byte [] buffer = new byte[BUFFERSIZE];
        int len = in.read(buffer);
        if(len < 0)
            return null;
        return new String(buffer, 0, len);
    }

    private void writeLine(String message) throws IOException {
        out.write(message.getBytes());
        out.flush();
    }


}
