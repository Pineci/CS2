package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    private static IDeque<String> stringToDeque(String s){
        IDeque<String> result = new LinkedDeque<>();
        for(String word : s.split(" ")){
            result.addBack(word);
        }
        return result;
    }

    public static void populateTitles() {
        titles.clear();
        for(String s : idMap.keySet()){
            String[] words = s.split(" ");
            for(int i = 0; i < words.length; i++){
                IDeque<String> suffix = stringToDeque(s.toLowerCase());
                for(int j = 0; j < i; j++){
                    suffix.removeFront();
                }
                IDeque<String> resultingTitles = new LinkedDeque<>();
                if (titles.containsKey(suffix)) {
                    resultingTitles = titles.get(suffix);
                }
                resultingTitles.addBack(s);
                titles.put(suffix, resultingTitles);
            }
        }
    }

    public static IDeque<String> complete(String term) {
        IDeque<String> result = new LinkedDeque<>();
        for(IDeque<String> r : titles.getCompletions(stringToDeque(term))){
            for(String s : r){
                if(!result.contains(s)){
                    result.add(s);
                }
            }
        }
        return result;
    }
}
