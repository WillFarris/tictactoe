package org.farriswheel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientView {

    private JFrame frame;
    private Client client;

    public ClientView(String title, int width, int height, Client client) {

        this.client = client;

        // Setup frame
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.CENTER;

        ActionListener tileEvent = e -> client.handleGameButton((TTTButton) e.getSource());
        ActionListener metaEvent = e -> client.handleMetaButton((JButton)e.getSource());

        TTTButton [][] tiles = new TTTButton[3][3];
        for(int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                tiles[x][y] = new TTTButton("_", x, y);
                tiles[x][y].setFont(new Font("Arial", Font.PLAIN, 80));
                constraints.gridx = x;
                constraints.gridy = y;
                tiles[x][y].addActionListener(tileEvent);
                frame.add(tiles[x][y], constraints);
            }
        }

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        JButton close = new JButton("Close");
        close.addActionListener(metaEvent);
        frame.add(close, constraints);


        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
