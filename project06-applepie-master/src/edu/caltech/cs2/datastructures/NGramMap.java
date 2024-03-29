package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;
import edu.caltech.cs2.types.NGram;
import edu.caltech.cs2.sorts.TopKSort;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;


public class NGramMap {
    public static final Random RANDOM = new Random();
    private int N;
    private IDictionary<NGram, IDictionary<IterableString, Integer>> map;
    private Supplier<IDictionary<IterableString, Integer>> inner;

    public NGramMap(Scanner reader, int N,
                    IDictionary<NGram, IDictionary<IterableString, Integer>> map,
                    Supplier<IDictionary<IterableString, Integer>> inner) {
        this.N = N;
        this.map = map;
        this.inner = inner;

        // Stores the next edu.caltech.cs2.datastructures.NGram string
        NGram ngram;

        // Read in the first N words
        String[] words = new String[this.N];
        int i = 0;

        while (reader.hasNext() && i < this.N) {
            String next = NGram.normalize(reader.next());
            if (next.isEmpty()) {
                continue;
            }
            words[i] = next;
            i += 1;
        }
        ngram = new NGram(words);

        // Parse the rest of the text
        while (reader.hasNext()) {
            String next = NGram.normalize(reader.next());
            if (next.isEmpty()) {
                continue;
            }

            // Increment count of how many times "next" follows ngram
            updateCount(ngram, next);

            // Update the strings held in ngram
            ngram = ngram.next(next);
        }
    }

    public void updateCount(NGram ngram, String nexts) {
        IterableString next = new IterableString(nexts);
        // Check if the key already exists in map
        if (map.containsKey(ngram)) {
            IDictionary<IterableString, Integer> follows = map.get(ngram);

            // Check if the inner map already has next as a key
            if (follows.containsKey(next)) {
                follows.put(next, follows.get(next) + 1);
            }
            else {
                follows.put(next, 1);
            }
        }
        else {
            IDictionary<IterableString, Integer> follows = inner.get();
            follows.put(next, 1);
            map.put(ngram, follows);
        }
    }

    public String getRandomNext(NGram ngram) {
        if (!map.containsKey(ngram)) {
            return "No information regarding this prefix.";
        }

        IDictionary<IterableString, Integer> suffixes = map.get(ngram);

        int i = 0;
        int idx = RANDOM.nextInt(suffixes.size());

        for (IterableString suffix : suffixes) {
            if (i == idx) {
                return suffix.toString();
            }

            i++;

        }

        // Execution should never get here...
        return null;
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /**
     * Returns an array of PQElements containing words and the number of
     * times each word was seen after ngram.
     * @param ngram - the ngram we want counts for
     * @return an array of hashmaps of words to counts
     */
    public IPriorityQueue.PQElement<String>[] getCountsAfter(NGram ngram) {
        IDictionary<IterableString, Integer> counts = map.get(ngram);
        if(counts == null){
            return null;
        }

        int wordsAfter = counts.size();
        IPriorityQueue.PQElement<String>[] toRet = new IPriorityQueue.PQElement[wordsAfter];
        int i = 0;
        for(IterableString t : counts){
            toRet[i] = new IPriorityQueue.PQElement<>(makeString(t), counts.get(t));
            i++;
        }
        return toRet;
    }

    /**
     * Gets k words that could potentially follow ngram in descending sorted order
     * @param ngram - the ngram we wish to suggest words for
     * @param k - the number of suggestions to return
     * @return - an array of suggestions
     */
    public String[] getWordsAfter(NGram ngram, int k) {
        IPriorityQueue.PQElement<String>[] counts = getCountsAfter(ngram);
        if(counts == null){
            String[] ret = {""};
            return ret;
        }
        k = Math.min(k, counts.length);

        TopKSort.sort(counts, k);
        String[] toRet = new String[k];
        for(int i = 0; i < k; i++){
            toRet[i] = counts[i].data;
        }
        return toRet;
    }

    private static String makeString(IterableString s){
        String toRet = "";
        for(char t : s){
            toRet += t;
        }
        return toRet;
    }
}
