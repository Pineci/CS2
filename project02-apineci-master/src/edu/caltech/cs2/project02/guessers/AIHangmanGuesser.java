package edu.caltech.cs2.project02.guessers;

import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class AIHangmanGuesser implements IHangmanGuesser {

  private static String letters = "abcdefghijklmnopqrstuvwxyz";

  public AIHangmanGuesser(){

  }

  private String wordToPattern(String word, Set<Character> chars){
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

  private int countOccurences(String target, Character letter){
    int count = 0;
    for(Character c : target.toCharArray()){
      if(c.equals(letter)){
        count++;
      }
    }
    return count;
  }

  @Override
  public char getGuess(String pattern, Set<Character> guesses) {
    SortedSet<String> words = new TreeSet<String>();
    try{
      Scanner scanner = new Scanner(new File("data/scrabble.txt"));
      String next;
      while(scanner.hasNext()){
        next = scanner.next();
        if (next.length() == pattern.length() && wordToPattern(next, guesses).equals(pattern)) {
          words.add(next);
        }
      }
    } catch(FileNotFoundException ex) {
    }
    Map<Character, Integer> counts = new TreeMap<Character, Integer>();
    for(Character letter : letters.toCharArray()){
      int count = 0;
      if(!guesses.contains(letter)) {
        for (String s : words) {
          count += this.countOccurences(s, letter);
        }
      }
      counts.put(letter, count);
    }
    int maxCount = Collections.max(counts.values());
    //System.out.println(maxCount);
    char guess = 'a';
    for(Character c : letters.toCharArray()){
      if(counts.get(c) == maxCount){
        guess = c;
        break;
      }
    }
    return guess;
  }
}
