package org.farriswheel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Function;

public class TTTClientView {

    private JFrame frame;
    private TTTClient client;

    public TTTClientView(String title, int width, int height, TTTClient client) {

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

        ActionListener event = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.handleGameButton((TTTButton) e.getSource());
            }
        };

        TTTButton [][] tiles = new TTTButton[3][3];
        for(int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                tiles[x][y] = new TTTButton("_", x, y);
                tiles[x][y].setFont(new Font("Arial", Font.PLAIN, 80));
                constraints.gridx = x;
                constraints.gridy = y;
                tiles[x][y].addActionListener(event);
                frame.add(tiles[x][y], constraints);
            }
        }

        JPanel info = new JPanel();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;

        JLabel test = new JLabel("hello world");
        info.add(test);


        frame.add(info, constraints);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
