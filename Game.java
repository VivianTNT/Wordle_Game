package org.gameProject;

import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game. Initializes the runnable game.
     */
    public static void main(String[] args) {
        Runnable game = new org.gameProject.wordle1.RunWordle1();
        SwingUtilities.invokeLater(game);
    }
}