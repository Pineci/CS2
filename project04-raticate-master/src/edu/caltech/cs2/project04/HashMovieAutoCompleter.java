package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();

    public static void populateTitles() {
        titles.clear();
        for(String s : idMap.keySet()){
            IDeque<String> suffix = new LinkedDeque<String>();
            String subAcc = "";
            String[] words = s.split(" ");
            for(int i = words.length-1; i >= 0; i--){
                String sub = words[i];
                subAcc = sub + " " + subAcc;
                subAcc = subAcc.strip();
                suffix.addFront(subAcc.toLowerCase());
            }
            titles.put(s, suffix);
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> list = new LinkedDeque<>();
        for(String title : titles.keySet()){
            for(String suffix : titles.get(title)){
                String total = term + " " + suffix;
                if(titles.get(title).contains(total)){
                    list.add(title);
                    break;
                }
            }
            if(titles.get(title).contains(term)){
                list.add(title);
            }
        }
        return list;
    }
}