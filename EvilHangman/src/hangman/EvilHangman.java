package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangman {

    public static void main(String[] args) {
        int numGuesses = Integer.parseInt(args[2]);
        Set<String> remainingWords = null;
        SortedSet<Character> guessedLetters;

        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        try {
            evilHangmanGame.startGame(new File(args[0]), Integer.parseInt(args[1]));
        }
        catch (EmptyDictionaryException e) {
            System.out.println("Empty Dictionary!");
            return;
        }
        catch (IOException e) {
            System.out.println("IO Exception!");
            return;
        }

        String oldPattern;
        String newPattern;
        Scanner in = new Scanner(System.in);
        String guessStr;
        char guess = '0';
        for (int guessNum = numGuesses; guessNum > 0;) {
            System.out.print("You have ");
            if (guessNum == 1) System.out.println("1 guess left");
            else System.out.println(guessNum + " guesses left");
            guessedLetters = evilHangmanGame.getGuessedLetters();
            System.out.print("Used letters:");
            for (int i : guessedLetters) {
                System.out.print(" " + (char)i);
            }
            System.out.println();
            oldPattern = evilHangmanGame.getPattern();
            System.out.println("Word: " + oldPattern);
            while (true) {
                System.out.print("Enter guess: ");
                guessStr = in.nextLine().toLowerCase();
                if (!guessStr.equals("")) guess = guessStr.charAt(0);
                if (guessStr.length() > 1) System.out.println("Invalid input");
                else if (!Character.isAlphabetic(guess)) System.out.println("Invalid input");
                else try {
                    remainingWords = evilHangmanGame.makeGuess(guess);
                    break;
                }
                catch(GuessAlreadyMadeException e) {
                    System.out.println("You already used that letter");
                }
            }
            newPattern = evilHangmanGame.getPattern();
            if (!newPattern.contains("-")) {
                System.out.println("You Win! " + newPattern);
                return;
            }

            if (guessNum == 1) break;
            int numOccurrences = 0;
            for (int i = 0; i < newPattern.length(); i++) {
                if (newPattern.charAt(i) == guess) {
                    numOccurrences++;
                }
            }
            if (numOccurrences == 0) {
                System.out.println("Sorry, there are no " + guess + "'s");
                guessNum--;
            }
            else System.out.println("Yes, there is " + numOccurrences + " " + guess);
            System.out.println();
        }
        System.out.println("You lose!");
        String finalWord = "";
        for (String str : remainingWords) {
            finalWord = str;
        }
        System.out.println("The word was: " + finalWord);
    }

}
