package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EvilHangmanChooser implements IHangmanChooser {

  private Boolean over;
  private int remainingGuesses;
  private SortedSet<String> words;
  private SortedSet<Character> guesses;
  private static Random rand = new Random();

  public EvilHangmanChooser(int wordLength, int maxGuesses){
    if(wordLength < 1 || maxGuesses < 1){
      throw new IllegalArgumentException("WordLength or MaxGuesses cannot be less than 1!");
    } else {
      this.over = false;
      this.remainingGuesses =  maxGuesses;
      this.words = new TreeSet<String>();
      this.guesses = new TreeSet<Character>();
      try{
        Scanner scanner = new Scanner(new File("data/scrabble.txt"));
        String next;
        while(scanner.hasNext()){
          next = scanner.next();
          if(next.length() == wordLength) {
            this.words.add(next);
          }
        }
      } catch(FileNotFoundException ex) {
      }
      if(words.isEmpty()){
        throw new IllegalStateException("No words of wordLength were found!");
      }
    }
  }

  @Override
  public int makeGuess(char letter) {
    if(this.getGuessesRemaining() < 1){
      throw new IllegalStateException("There are no guesses remaining!");
    } else
    if(this.getGuesses().contains(letter) || !Character.isLowerCase(letter)){
      throw new IllegalArgumentException("Letter must be lower case and not be guessed before!");
    }
    String prevPattern = this.getPattern();
    this.guesses.add(letter);
    Map<String, Integer> families = this.findFamilies(this.guesses);
    int maxWords = Collections.max(families.values());
    SortedSet<String> sortedKeys = new TreeSet<String>();
    String newPattern = prevPattern;
    for(String s : families.keySet()){
      sortedKeys.add(s);
    }
    for(String s : sortedKeys){
      if(families.get(s) == maxWords){
        newPattern = s;
        break;
      }
    }
    SortedSet<String> newWords = new TreeSet<String>();
    for(String s : this.words){
      if(wordToPattern(s, this.guesses).equals(newPattern)){
        newWords.add(s);
      }
    }
    this.words = newWords;
    int count = 0;
    char[] prevChars = prevPattern.toCharArray();
    char[] newChars = newPattern.toCharArray();
    for(int i = 0; i < prevPattern.length(); i++){
      if(prevChars[i] != newChars[i]){
        count++;
      }
    }
    if(!newPattern.contains("-")){
      this.over = true;
    }
    if(count == 0){
      this.remainingGuesses -= 1;
    }
    return count;
  }

  @Override
  public boolean isGameOver() {
    return (this.over || this.getGuessesRemaining() <= 0);
  }

  private String wordToPattern(String word, SortedSet<Character> chars){
    String str = "";
    for(int i = 0; i < word.length(); i++){
      str = str.concat("-");
    }
    for(int i = 0; i < word.length(); i++){
      if(chars.contains(word.charAt(i))){
        String pre = str.substring(0, i);
        String post = str.substring(i+1);
        str = pre + word.charAt(i) + post;
      }
    }
    return str;
  }

  private Map<String, Integer> findFamilies(SortedSet<Character> chars){
    Map<String, Integer> families = new TreeMap<String, Integer>();
    for(String s : this.words){
      String key = wordToPattern(s, chars);
      if(families.containsKey(key)){
        families.put(key, families.get(key)+1);
      } else {
        families.put(key, 1);
      }
    }
    return families;
  }

  @Override
  public String getPattern() {
    return wordToPattern(this.words.first(), this.guesses);
  }

  @Override
  public SortedSet<Character> getGuesses() {
    return this.guesses;
  }

  @Override
  public int getGuessesRemaining() {
    return this.remainingGuesses;
  }

  @Override
  public String getWord() {
    this.over = true;
    return "";
  }
}