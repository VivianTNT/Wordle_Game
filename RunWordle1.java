package org.gameProject.wordle1;

import java.awt.*;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 *
 * This game adheres to a Model-View-Controller design framework.
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a Wordle object to serve as the game's model.
 */
public class RunWordle1 implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Wordle");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final WordleGameBoard board = new WordleGameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Control Panel
        // Instructions, TextBox, Enter, Reset, Load, Save (from left to right)
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new GridLayout(1, 5));

        frame.add(control_panel, BorderLayout.NORTH);

        final JButton instructions = new JButton("Instructions");
        instructions.setBackground(Color.RED);
        instructions.setOpaque(true);
        final JTextField textbox = new JTextField("Input word here");
        final JButton enter = new JButton("Enter");
        final JButton reset = new JButton("Reset");
        final JButton load = new JButton("Load Game");
        final JButton save = new JButton("Save Progress");

        instructions.addActionListener(e -> board.openInstructions(frame));
        control_panel.add(instructions);

        textbox.addActionListener(e -> {
            String s;
            s = textbox.getText().trim();
            board.setWordInBoard(s);
            textbox.setText("");
        });
        control_panel.add(textbox);

        enter.addActionListener(e -> {
            String s;
            s = textbox.getText().trim();
            board.setWordInBoard(s);
            textbox.setText("");
        });
        control_panel.add(enter);

        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        load.addActionListener(e -> board.load(frame));
        control_panel.add(load);

        save.addActionListener(e -> board.save(frame));
        control_panel.add(save);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
