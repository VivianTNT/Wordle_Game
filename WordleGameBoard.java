package org.gameProject.wordle1;

import java.awt.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class instantiates a Wordle object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class WordleGameBoard extends JPanel {

    private Wordle1 w; // model for the game
    private JLabel status; // current status text

    private List<String> inputtedWords = new ArrayList<>(); // stores list of previously inputted
                                                            // words in case the
    // user wishes to save the game's progress.

    private JLabel[][] wordArray = new JLabel[6][5]; // 2D array

    // Game constants
    public static final int BOARD_WIDTH = 700;
    public static final int BOARD_HEIGHT = 700;

    /**
     * Initializes the game board.
     */
    public WordleGameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        w = new Wordle1(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        this.setLayout(new BorderLayout());
        this.setLayout(new GridLayout(6, 5));
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                wordArray[i][j] = new JLabel(); // creates new JLabel for each coordinate in the
                                                // grid
                wordArray[i][j].setOpaque(true);
                wordArray[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                this.add(wordArray[i][j]);
            }
        }
    }

    // helper method that sets a JLabel at a specific coordinate in the grid to a
    // certain character/color
    public void setPanel(String character, int row, int col, Color c) {
        JLabel l = this.wordArray[row][col];
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setVerticalAlignment(SwingConstants.CENTER);
        l.setText(character);
        l.setBackground(c);
    }

    // will take in the list from the getColors method in wordle1.java and convert
    // the list of integers to
    // a list of actual colors so that they can repaint the JLabels in the
    // corresponding rows.
    public List<Color> getActualColors(List<Integer> l) {
        List<Color> res = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (l.get(i) == 1) {
                res.add(Color.GREEN);
            } else if (l.get(i) == 0) {
                res.add(Color.YELLOW);
            } else {
                res.add(Color.LIGHT_GRAY);
            }
        }
        return res;
    }

    // method for inputting the user's valid guess into the board, along with proper
    // colorings
    public void setWordInBoard(String s) {
        boolean b = w.playTurn(s);
        if (b) {
            List<Integer> intColorings = w.getColors(s, w.getWordleWord());
            List<Color> colorings = getActualColors(intColorings);
            for (int i = 0; i < 5; i++) {
                setPanel(
                        Character.toString(s.charAt(i)).toUpperCase(), w.getNumGuesses() - 1, i,
                        colorings.get(i)
                );
            }
        }
        updateStatus(s, b);
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        w.reset();
        status.setText("Number of Guesses Taken: 0" + " Guess a word!");

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                wordArray[i][j].setBackground(Color.WHITE);
                wordArray[i][j].setText("");
            }
        }
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus(String guess, boolean b) {
        // If word is invalid and/or game is over and user tries to keep putting in
        // words
        if (!b) {
            if (!w.isValidInput(guess)) {
                // If player already won game
                if (w.getWonGame()) {
                    status.setText("You already won the game! Press Reset for a new round.");
                } else {
                    // player has lost game and tries to enter more words
                    if (w.getGameOver()) {
                        status.setText("You already lost the game! Press Reset for a new round.");
                        // player hasn't lost game yet and tries to enter invalid word
                    } else {
                        status.setText(
                                "Invalid word!" + " Number of Guesses Taken: " + w.getNumGuesses()
                        );
                    }
                }
            } else {
                if (w.getWonGame()) {
                    status.setText("You already won the game! Press Reset for a new round.");
                } else {
                    if (w.getGameOver()) {
                        status.setText("You already lost the game! Press Reset for a new round.");
                    } else {
                        status.setText(
                                "Sadly, you did not find the correct word. The word was: "
                                        + w.getWordleWord()
                        );
                    }
                }
            }
            // If word in textbox can be entered
        } else {
            if (w.checkWinner(guess)) {
                status.setText(
                        "You found the correct word!" + " Number of Guesses Taken: "
                                + w.getNumGuesses()
                );
            } else {
                if (w.getGameOver()) {
                    status.setText(
                            "Sadly, you did not find the correct word. The word was: "
                                    + w.getWordleWord()
                    );
                } else {
                    status.setText(
                            "Aw rip, better luck next guess." + " Number of Guesses Taken: "
                                    + (w.getNumGuesses())
                    );
                }
            }
            inputtedWords.add(guess.trim().toUpperCase());
        }

    }

    // loads the saved game data onto the board and updates the status
    public void load(JFrame f) {
        reset();
        Path p = Paths.get("src/main/java/org/gameProject/wordle1/WordleGameData.txt");
        List<String> data = new ArrayList<>();
        try {
            data = Files.readAllLines(p);
            // in case the user tries to load file without saving a game state
            if (data.isEmpty()) {
                status.setText("You haven't saved any game progress yet!");
            } else {
                String wordleWord = data.get(0);
                w.setWordleWord(wordleWord);

                // in case the user saves a game without making any guesses
                if (data.size() == 2) {
                    String prevGuesses = data.get(1);
                    String[] prevGuessesArray = prevGuesses.split(", ");
                    boolean b = true;
                    for (int i = 0; i < prevGuessesArray.length; i++) {
                        List<Color> colors = getActualColors(
                                w.getColors(prevGuessesArray[i], w.getWordleWord())
                        );
                        for (int j = 0; j < 5; j++) {
                            wordArray[i][j].setBackground(colors.get(j));
                            wordArray[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                            wordArray[i][j].setVerticalAlignment(SwingConstants.CENTER);
                            wordArray[i][j]
                                    .setText(Character.toString(prevGuessesArray[i].charAt(j)));
                        }
                        b = w.playTurn(prevGuessesArray[i]);
                    }
                    if (prevGuessesArray.length != 0) {
                        updateStatus(prevGuessesArray[prevGuessesArray.length - 1], b);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    f, "Invalid File Path!", "Instructions", JOptionPane.ERROR_MESSAGE
            );
            status.setText("Invalid File Name");
            System.out.println("Invalid File Name");
        }
    }

    // stores the wordleWord and a string of all previous guesses
    public void save(JFrame f) {
        try {
            FileWriter fw = new FileWriter("src/main/java/org/gameProject/wordle1/WordleGameData.txt");
            String inputs = inputtedWords + "";
            String inputs2 = inputs.substring(1, inputs.length() - 1);
            String data = w.getWordleWord() + "\n" + inputs2;
            fw.write(data);
            fw.flush();
            fw.close();
            status.setText("Successfully saved game data!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    f, "Invalid File Path!", "Instructions", JOptionPane.ERROR_MESSAGE
            );
            status.setText("Invalid file path");
            System.out.println("Invalid file path");
            e.printStackTrace();
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    // instructions for game
    public void openInstructions(JFrame f) {
        String instructions = "Welcome to Wordle!\n\n " +
                "To begin, you will have 6 tries to guess each word, which is \n" +
                "randomly generated from a fixed word bank. You will only be \n" +
                "able to guess 5 letter words (case/white spaces don't matter) that are \n " +
                "included within this word bank. If your word is not 5 characters\n" +
                "long or it does not exist in the word bank, then the status bar at\n" +
                "the bottom will notify you. The status bar will also tell you how\n" +
                "many guesses you have taken.\n\n" +
                "To the right of the Instructions button, you will see a textbox\n" +
                "that you can enter your guesses in each time. You can press either\n" +
                "the Enter button or press the enter key on your keyboard to submit\n " +
                "your guess. The reset button will allow you to reset the game to\n" +
                "a new word. The Save button saves your progress made on guessing a\n" +
                "particular word (note that you can only save the progress of 1 game\n" +
                "at a time). The Load button will reload your progress from the game\n" +
                "you saved using the Save button.\n \n" +
                "You will not be able to enter in more words after the game is over,\n" +
                "whether you win or lose, and the status bar is updated accordingly.";
        JOptionPane.showMessageDialog(
                f, instructions,
                "Instructions", JOptionPane.INFORMATION_MESSAGE
        );
    }
}
