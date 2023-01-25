package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private TreeSet<String> wordDictionary;
    private TreeSet<Character> guessedLetters;
    private String pattern;

    public EvilHangmanGame() {
        wordDictionary = new TreeSet<>();
        guessedLetters = new TreeSet<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        String newDictWord;
        Scanner in = new Scanner(dictionary);
        if (!in.hasNext()) throw new EmptyDictionaryException();
        wordDictionary.clear();
        guessedLetters.clear();
        while (in.hasNext()) {
            newDictWord = in.next().toLowerCase();
            if (newDictWord.length() == wordLength) wordDictionary.add(newDictWord);
        }
        if (wordDictionary.isEmpty()) throw new EmptyDictionaryException();
        pattern = "";
        for (int i = 0; i < wordLength; i++) {
            pattern += '-';
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);

        if (guessedLetters.contains(guess)) throw new GuessAlreadyMadeException();

        Map<String, TreeSet<String>> partitionMap = new TreeMap<>();
        guessedLetters.add(guess);

        char ch;
        String wordPattern;
        for (String word : wordDictionary) {
            wordPattern = "";
            for (int i = 0; i < word.length(); i++) {
                ch = word.charAt(i);
                if (ch == guess) wordPattern += guess;
                else wordPattern += "-";
            }
            if (!partitionMap.containsKey(wordPattern)) {
                TreeSet<String> newSet = new TreeSet<>();
                newSet.add(word);
                partitionMap.put(wordPattern, newSet);
            }
            else {
                TreeSet<String> newSet = partitionMap.get(wordPattern);
                newSet.add(word);
                partitionMap.put(wordPattern, newSet);
            }
        }

        int maxSize = 0;
        for (Map.Entry<String,TreeSet<String>> entry : partitionMap.entrySet()) { //find maxSize
            TreeSet<String> value = entry.getValue();
            if (value.size() > maxSize) maxSize = value.size();
        }

        ArrayList<String> removeList = new ArrayList<>();
        for (Map.Entry<String,TreeSet<String>> entry : partitionMap.entrySet()) { //keep only sets with maxSize
            String key = entry.getKey();
            TreeSet<String> value = entry.getValue();
            if (value.size() < maxSize) removeList.add(key);
        }
        for (String k : removeList) {
            partitionMap.remove(k);
        }
        removeList.clear();

        String blankPattern = "";
        for (int i = 0; i < pattern.length(); i++) {
            blankPattern += '-';
        }
        if (partitionMap.containsKey(blankPattern)) { //Choose the group in which the letter does not appear at all
            wordDictionary = partitionMap.get(blankPattern);
            return partitionMap.get(blankPattern);
        }

        if (partitionMap.size() > 1) {
            //If each group has the guessed letter, choose the one with the fewest letters
            int numOccurs;
            int minOccurs = pattern.length()+1;
            for (Map.Entry<String, TreeSet<String>> entry : partitionMap.entrySet()) { //find num of occurrences
                String key = entry.getKey();
                numOccurs = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == guess) numOccurs++;
                }
                if (numOccurs < minOccurs) minOccurs = numOccurs;
            }
            for (Map.Entry<String, TreeSet<String>> entry : partitionMap.entrySet()) {//remove entries with not max occurrences
                String key = entry.getKey();
                numOccurs = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == guess) numOccurs++;
                }
                if (numOccurs > minOccurs) removeList.add(key);
            }
        }
        for (String k : removeList) {
            partitionMap.remove(k);
        }
        removeList.clear();

        int checkLength = pattern.length()+1;
        while (partitionMap.size() > 1) {
            // If this still has not resolved the issue, choose the one with the rightmost guessed letter
            // If there is still more than one group, choose the one with the next rightmost letter
            checkLength--;
            int maxIndex = 0;
            for (Map.Entry<String, TreeSet<String>> entry : partitionMap.entrySet()) {
                String key = entry.getKey();
                for (int i = 0; i < checkLength; i++) {
                    if ((key.charAt(i) == guess) && (i > maxIndex)) maxIndex = i;
                }
            }
            for (Map.Entry<String, TreeSet<String>> entry : partitionMap.entrySet()) {
                String key = entry.getKey();
                if (key.charAt(maxIndex) != guess) removeList.add(key);
            }
            for (String k : removeList) {
                partitionMap.remove(k);
            }
            removeList.clear();
        }

        if (partitionMap.size() != 1) System.out.println("PROBLEM!!");

        String newPattern = "";
        for (Map.Entry<String,TreeSet<String>> entry : partitionMap.entrySet()) {
            String key = entry.getKey();
            TreeSet<String> value = entry.getValue();
            newPattern = key;
            wordDictionary = value;
        }

        String finalPattern = "";
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) != '-') finalPattern += pattern.charAt(i);
            else if (newPattern.charAt(i) != '-') finalPattern += newPattern.charAt(i);
            else finalPattern += '-';
        }
        pattern = finalPattern;
        return wordDictionary;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getPattern() {
        return pattern;
    }
}
