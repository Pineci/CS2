package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class RandomHangmanChooser implements IHangmanChooser {

  private Boolean over;
  private int remainingGuesses;
  private final String word;
  private SortedSet<Character> guesses;
  private static Random rand = new Random();

  public RandomHangmanChooser(int wordLength, int maxGuesses){
    if(wordLength < 1 || maxGuesses < 1){
      throw new IllegalArgumentException("WordLength or MaxGuesses cannot be less than 1!");
    } else {
      String chosenWord = "";
      this.over = false;
      this.remainingGuesses =  maxGuesses;
      SortedSet<String> words = new TreeSet<String>();
      try{
        Scanner scanner = new Scanner(new File("data/scrabble.txt"));
        String next;
        while(scanner.hasNext()){
          next = scanner.next();
          if(next.length() == wordLength) {
            words.add(next);
          }
        }
      } catch(FileNotFoundException ex) {
      }
      if(words.isEmpty()){
        throw new IllegalStateException("No words of wordLength were found!");
      } else {
        int chosen = rand.nextInt(words.size());
        this.word = words.toArray()[chosen].toString();
        guesses = new TreeSet<Character>();
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
    } else {
      int count = 0;
      for(Character c : this.word.toCharArray()){
        if(c.equals(letter)){
          count++;
        }
      }
      this.guesses.add(letter);
      if (count == 0) {
        this.remainingGuesses--;
      }
      if(this.getPattern().equals(this.word)){
        this.over = true;
      }
      return count;
    }

  }

  @Override
  public boolean isGameOver(){
    return (this.over || this.getGuessesRemaining() <= 0);
  }

  @Override
  public String getPattern() {
    String str = "";
    for(int i = 0; i < this.word.length(); i++){
      str = str.concat("-");
    }
    for(int i = 0; i < this.word.length(); i++){
      if(this.guesses.contains(this.word.charAt(i))){
        String pre = str.substring(0, i);
        String post = str.substring(i+1);
        str = pre + this.word.charAt(i) + post;
      }
    }
    return str;
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
    return this.word;
  }
}