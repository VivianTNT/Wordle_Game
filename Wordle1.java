package org.gameProject.wordle1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Wordle1 {
    private int numGuesses;

    private String wordleWord;

    private boolean gameOver;

    private boolean wonGame;

    /**
     * Constructor sets up game state.
     */
    public Wordle1() {
        reset();
    }

    public int getNumGuesses() {
        return numGuesses;
    }

    public String getWordleWord() {
        return wordleWord;
    }

    public boolean getWonGame() {
        return wonGame;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setWordleWord(String s) {
        wordleWord = s;
    }

    public void setNumGuesses(int n) {
        numGuesses = n;
    }

    public void setWonGame(boolean b) {
        wonGame = b;
    }

    public void setGameOver(boolean b) {
        gameOver = b;
    }

    Path p = Paths.get("src/main/java/org/gameProject/wordle1/WordleWords.txt"); // txt file with word
                                                                             // bank

    // creates the ArrayList of valid words
    // Collections
    public List<String> getArrayOfWords() {
        List<String> words = new ArrayList<>();
        try {
            words = Files.readAllLines(p);
        } catch (IOException e) {
            System.out.println("Invalid File Name");
        }
        return words;
    }

    private List<String> words = getArrayOfWords();

    // determines if the guess (s) is in the word bank.
    private boolean isValidWord(String s) {
        if (words.contains(s.trim().toLowerCase())) {
            return true;
        }
        return false;
    }

    // returns a random word from the word bank
    private String getChosenWord() {
        List<String> words = new ArrayList<>();
        try {
            words = Files.readAllLines(p);
        } catch (IOException e) {
            System.out.println("Invalid File Name");
        }
        Random r = new Random();
        int pos = r.nextInt(words.size());
        String chosen = words.get(pos).trim().toUpperCase();
        return chosen;
    }

    // checks whether the guess (s) is valid
    // valid if the length is 5 (excluding surrounding white spaces) and if it
    // exists in the word bank
    public boolean isValidInput(String s) {
        if ((s.length() == 5) && (isValidWord(s))) {
            return true;
        }
        return false;
    }

    // green: 1, yellow: 0, gray: -1
    public List<Integer> getColors(String userWord, String wordleWord) {
        String userInput = userWord.trim().toUpperCase();
        String[] wordleWordArray = wordleWord.split("");
        String[] userWordArray = userInput.split("");
        List<String> chosenWordList = Arrays.asList(wordleWordArray);
        List<String> userWordList = Arrays.asList(userWordArray);

        List<Integer> colorings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (chosenWordList.contains(userWordList.get(i))) {
                if (chosenWordList.get(i).equals(userWordList.get(i))) {
                    colorings.add(1);
                } else {
                    colorings.add(0);
                }
            } else {
                colorings.add(-1);
            }
        }
        return colorings;
    }

    /**
     * playTurn allows players to play a turn. Returns true if the word can be
     * successfully added to the board and false if the word is invalid. A word
     * is invalid if its length is not 5 or the word is not in the given dictionary.
     * 
     * @param s the user's guess
     * @return whether the turn was successful
     */

    // true if word can be entered, false otherwise
    public boolean playTurn(String s) {
        if (gameOver || !isValidInput(s.trim())) {
            return false;
        }
        numGuesses++;
        if (numGuesses == 6 || isCorrectWord(s.trim())) {
            if (isCorrectWord(s)) {
                wonGame = true;
            }
            gameOver = true;
        }
        return true;
    }

    public boolean isCorrectWord(String s) {
        return s.trim().toUpperCase().equals(wordleWord);
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner checks whether the guess is structurally equal to the actual word
     * 
     * @param s the user's guess
     * @return true if the guess is the actual word, false otherwise
     */
    public boolean checkWinner(String s) {
        if (isCorrectWord(s)) {
            gameOver = true;
            wonGame = true;
            return true;
        }
        return false;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState(String s) {
        boolean result = playTurn(s);
        if (result) {
            List<Integer> colorings = getColors(s, wordleWord);
            if (checkWinner(s)) {
                System.out.println("You found the correct word: " + s);
                System.out.println("Number of guesses taken: " + numGuesses);
                System.out.println(colorings);
                System.out.println();
            } else {
                System.out.println("You guessed: " + s);
                System.out.println("Aw rip, better luck next guess.");
                System.out.println("Number of guesses left: " + (6 - numGuesses));
                System.out.println(colorings);
                System.out.println();
            }
        } else {
            if (!isValidInput(s)) {
                System.out.println("You guessed: " + s);
                System.out.println("Invalid word: Please input an actual 5-letter word");
                System.out.println("Number of guesses left: " + (6 - numGuesses));
                System.out.println();
            } else {
                if (wonGame) {
                    System.out.println("You already won the game silly bozo");
                } else {
                    System.out.println(
                            "Sadly, you did not find the correct word. The word was: " + wordleWord
                    );
                }
            }
        }

    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        numGuesses = 0;
        gameOver = false;
        wordleWord = getChosenWord().toUpperCase();
        wonGame = false;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");

        w.printGameState("ARISE");

        w.printGameState("COUNT");

        w.printGameState("CRANE");

        w.printGameState("URMOM");

        w.printGameState("SPLIT");

        w.printGameState("sPeNt");

    }
}
