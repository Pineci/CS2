package edu.caltech.cs2.lab06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnagramGenerator {


    public static void printPhrases(String phrase, List<String> dictionary){
        LetterBag total = new LetterBag(phrase);
        printPhrases(total, dictionary, new ArrayList<>());

    }

    private static void printPhrases(LetterBag total, List<String> dictionary, List<String> toPrint){
        if(total.isEmpty()){
            System.out.println("[]");
        }
        for(String s : dictionary){
            LetterBag subtract = total.subtract(new LetterBag(s));
            if(subtract != null) {
                if (subtract.isEmpty()) {
                    System.out.print("[");
                    for(String tp : toPrint){
                        System.out.print(tp + ", ");
                    }
                    System.out.println(s + "]");
                } else {
                    ArrayList<String> acc = new ArrayList<>();
                    for(String copy : toPrint){
                        acc.add(copy);
                    }
                    acc.add(s);
                    printPhrases(subtract, dictionary, acc);
                }
            }
        }
    }


    public static void printWords(String word, List<String> dictionary) {
        LetterBag total = new LetterBag(word);
        for(String s: dictionary){
            LetterBag subtract = total.subtract(new LetterBag(s));
            if(subtract != null && subtract.isEmpty()){
                System.out.println(s);
            }
        }
    }
}
