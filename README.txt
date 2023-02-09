=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
Wordle Game App README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

Explanation of Major Components:

  1. 2D Array: The 2D array is present only in the WordleGameBoard class. This class has a private instance variable
  called wordsArray that is of type JLabel, since it is very easy to change the background color/set the text. The 2D
  array is an implementation of the 6x5 grid that Wordle has, and it makes it very easy to get and set the color/text
  in each grid coordinate as needed (for example by using the setPanel helper method I wrote). Since there is no method
  to get this private instance variable, the data is encapsulated.

  2. Collections: I used File I/O to implement "word verification" to load in a list of valid words pasted into a
  txt file from an online list (cited below). This is present in my wordle1 class (getArrayOfWords, isValidWord,
  getChosenWord, etc.). I used an ArrayList for this because there is a Files.readAllLines method that adds each line
  in a given txt file into an ArrayList. Since I am returning a random word each time the game is reset, the
  ArrayList makes it easy to use the Random class's nextInt() method to get a random index from the collection. This is
  also why I used an ArrayList instead of a Set, because a set doesn't have indices. A TreeMap would not be necessary
  for this too, since we only need to store the words themselves.

  3. JUnit Testable Component: I have several tests that comprehensively check how the user can play the wordle game,
  a test for resetting the board, and a test for encapsulation. This would allow me to test the game logic without
  having to run the GUI each time For each test, I initialized a new instance of wordle1 and simulated the guesses
  using my playTurn method. For example, I tested if there were valid/invalid inputs, wins/losses, and checked the
  state of the game (ex. checking to make sure the colors are correct, numGuesses is correct, wonGame/gameOver are
  correct, etc). I also tested that inputs that have additional white space around the word/different casing are
  accepted (ex: " cRaNe " is a valid string. Since my game state only stores primitives and String, these are
  encapsulated and can't be edited. The collections I use (including getting the word list and for word verification
  as well as getting the list of colors) are encapsulated.They are just methods that return a new copy of the list
  whenever they are called.

  4. File I/O: My implementation saves the state of a game that the user chooses by pressing the Save button. This file
  is written to WordleGameData.txt, and gets rewritten every time the user saves a new game. This file only needs to
  store the wordleWord and the ordered list of previous guesses to save the entire game state, since I can use my
  playTurn method to "replay" the game and restore the entire game state as before. So, the load button reloads the
  data from the saved game data and restores the entire game state as mentioned above. All instance variables of
  wordle are reestablished, and the status panel is also changed accordingly. If no file exists/the file is incorrect,
  an error message is displayed (JOption Pane)/status is changed.

Overview of Classes:

  wordle1.java: Testable component that is separate from the GUI. Allows me to test the game logic without having to
  run any GUI components. Contains 4 main private instance variables: numGuesses, wordleWord, wonGame, and gameOver.
  This class also parses the list of valid words, generates a random wordleWord every time the game is reset, and
  also allows for testing of the coloring of the words (-1 for gray, 0 for yellow, 1 for green), where gray means
  the letter is not present in the wordleWord, yellow means the letter is present in the wordleWord but not in the
  right position, and green means the letter is in the correct position as the wordleWord. Uses Collections.

  WordleTest.java: JUnit tests that comprehensively check the state of the game after simulated guesses by creating
  instances of wordle1.

  WordleGameBoard.java: Creates the 6x5 grid layout panel that the guesses are displayed on. Uses 2D arrays. This
  class also includes methods used by the RunWordle1 class; specifically the action event listeners for the JButtons
  and textbox (ex. load and save game). Also includes methods to update the status of the board/status bar. The status
  bar keeps track of the number of guesses taken and displays the correct word if the user loses after 6 guesses, or
  if the user wins. If the user enters an invalid word, this is shown in the bar too. If the user tries to enter words
  after the game is over (whether they've won or lost), the status bar tells the user they've already won/lost and
  they can reset the game to play a new round. Uses File I/O.

  RunWordle1.java: This class compiles all the GUI components into one JFrame (control_panel, WordleGameBoard, and
  Status bar). Adds action event listeners for each of the buttons and textbox.

External Resources:

  I took the words list from https://github.com/be-a-dev/wordle.project/blob/master/assets/Words.txt
  I did add a couple of additional words to the file, such as "crane."

  I also used the JavaDocs websites extensively to help create the GUI especially, and looked over a few
  GeeksForGeeks websites to learn about different functions (ex. Files.readAllLines).
