package org.gameProject.wordle1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WordleTest {
    @Test
    public void testNoWordsEntered() {
        Wordle1 w = new Wordle1();
        Assertions.assertEquals(0, w.getNumGuesses());
        Assertions.assertFalse(w.getWonGame());
        Assertions.assertFalse(w.getGameOver());
    }

    @Test
    public void testWinLessThan6GuessesOnlyValidStringsAndColorings() {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");
        String actualWord = w.getWordleWord();

        w.playTurn("ARISE");
        List<Integer> colors1 = w.getColors("ARISE", actualWord);
        List<Integer> expectedColors1 = new ArrayList<>();
        expectedColors1.add(-1);
        expectedColors1.add(-1);
        expectedColors1.add(-1);
        expectedColors1.add(0);
        expectedColors1.add(0);
        Assertions.assertEquals(expectedColors1, colors1);
        Assertions.assertEquals(1, w.getNumGuesses());

        w.playTurn("cOuNt"); // tests if it is not case sensitive
        List<Integer> colors2 = w.getColors("COUNT", actualWord);
        List<Integer> expectedColors2 = new ArrayList<>();
        expectedColors2.add(-1);
        expectedColors2.add(-1);
        expectedColors2.add(-1);
        expectedColors2.add(1);
        expectedColors2.add(1);
        Assertions.assertEquals(expectedColors2, colors2);
        Assertions.assertEquals(2, w.getNumGuesses());

        w.playTurn(" SPENT "); // tests if game ignores the white spaces
        List<Integer> colors3 = w.getColors("SPENT", actualWord);
        List<Integer> expectedColors3 = new ArrayList<>();
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        Assertions.assertEquals(expectedColors3, colors3);
        Assertions.assertEquals(3, w.getNumGuesses());
        Assertions.assertTrue(w.getWonGame());
        Assertions.assertTrue(w.getGameOver());
    }

    @Test
    public void testWinInvalidAndValidEntries() {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");
        String actualWord = w.getWordleWord();

        w.playTurn("ARISE");
        List<Integer> colors1 = w.getColors("ARISE", actualWord);
        List<Integer> expectedColors1 = new ArrayList<>();
        expectedColors1.add(-1);
        expectedColors1.add(-1);
        expectedColors1.add(-1);
        expectedColors1.add(0);
        expectedColors1.add(0);
        Assertions.assertEquals(expectedColors1, colors1);
        Assertions.assertEquals(1, w.getNumGuesses());

        w.playTurn("AAAAA"); // invalid input
        List<Integer> colors2 = w.getColors("AAAAA", actualWord);
        Assertions.assertEquals(1, w.getNumGuesses()); // numGuesses shouldn't change

        w.playTurn("SPENT");
        List<Integer> colors3 = w.getColors("SPENT", actualWord);
        List<Integer> expectedColors3 = new ArrayList<>();
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        expectedColors3.add(1);
        Assertions.assertEquals(expectedColors3, colors3);
        Assertions.assertEquals(2, w.getNumGuesses());
        Assertions.assertTrue(w.getWonGame());
        Assertions.assertTrue(w.getGameOver());
    }

    @Test
    public void testOnlyInvalidEntriesAndLessThan6Entries() {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");
        String actualWord = w.getWordleWord();

        w.playTurn("AAAAA"); // not in word list
        List<Integer> colors1 = w.getColors("BBBBB", actualWord);
        Assertions.assertEquals(0, w.getNumGuesses());

        w.playTurn("ASK"); // less than 5 letters
        Assertions.assertEquals(0, w.getNumGuesses());

        w.playTurn("EXPECT"); // more than 5 letters
        Assertions.assertEquals(0, w.getNumGuesses());
        Assertions.assertFalse(w.getWonGame());
        Assertions.assertFalse(w.getGameOver());
    }

    @Test
    public void testLoseGame() {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");

        w.playTurn("ARISE");
        w.playTurn("COUNT");
        w.playTurn("CRANE");
        w.playTurn("ABOUT");
        w.playTurn("AGENT");
        w.playTurn("ANGRY");

        Assertions.assertEquals(6, w.getNumGuesses());
        Assertions.assertFalse(w.getWonGame());
        Assertions.assertTrue(w.getGameOver());
    }

    @Test
    public void testReset() {
        Wordle1 w = new Wordle1();
        w.setWordleWord("SPENT");

        w.playTurn("ARISE");
        w.playTurn("COUNT");
        w.playTurn("CRANE");
        w.playTurn("ABOUT");
        w.playTurn("AGENT");
        w.playTurn("ANGRY");

        Assertions.assertEquals(6, w.getNumGuesses());
        Assertions.assertFalse(w.getWonGame());
        Assertions.assertTrue(w.getGameOver());

        w.reset();

        Assertions.assertEquals(0, w.getNumGuesses());
        Assertions.assertFalse(w.getWonGame());
        Assertions.assertFalse(w.getGameOver());
    }

    @Test
    public void testGetWords() {
        Wordle1 w = new Wordle1();
        List<String> listOfWords = w.getArrayOfWords();
        String wordleWord = w.getWordleWord().toLowerCase();
        Assertions.assertTrue(listOfWords.contains(wordleWord));
    }

    @Test
    public void testEncapsulation() {
        Wordle1 w = new Wordle1();
        List<String> listOfWords = w.getArrayOfWords();
        int size = listOfWords.size();
        List<String> toRemove = new ArrayList<>();
        // words that are in the txt file
        toRemove.add("arise");
        toRemove.add("crane");
        listOfWords.removeAll(toRemove);
        Assertions.assertEquals(size, w.getArrayOfWords().size()); // words array should not be modified
        // since all the other instance variables are primitives/Strings, they are by
        // definition encapsulated
    }
}