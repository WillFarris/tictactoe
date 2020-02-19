package org.farriswheel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TTTClientView {

    public TTTClientView() {
        // Setup frame
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;

        ActionListener event = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("O")) {
                    ((JButton) e.getSource()).setText("X");
                } else {
                    ((JButton) e.getSource()).setText("O");
                }
            }
        };

        JButton [][] tiles = new JButton[3][3];
        for(int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                tiles[x][y] = new JButton(" ");
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weighty = 1.0;
                constraints.gridx = x;
                constraints.gridy = y;
                tiles[x][y].addActionListener(event);
                frame.add(tiles[x][y], constraints);
            }
        }

        JButton text = new JButton("Label");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        frame.add(text, constraints);

        frame.setVisible(true);
    }
}
