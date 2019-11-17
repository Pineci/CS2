package edu.caltech.cs2.types;

import edu.caltech.cs2.datastructures.LinkedDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.Iterator;


public class NGram implements Iterable<String>, Comparable<NGram> {
    public static final String NO_SPACE_BEFORE = ",?!.-,:'";
    public static final String NO_SPACE_AFTER = "-'><=";
    public static final String REGEX_TO_FILTER = "”|\"|“|\\(|\\)|\\*";
    public static final String DELIMITER = "\\s+|\\s*\\b\\s*";
    private IDeque<String> data;

    public static String normalize(String s) {
        return s.replaceAll(REGEX_TO_FILTER, "").strip();
    }

    public NGram(IDeque<String> x) {
        this.data = new LinkedDeque<>();
        for (int i = 0; i < x.size(); i++) {
            this.data.addBack(x.peekFront());
            x.addBack(x.removeFront());
        }
    }

    public NGram(String data) {
        this(normalize(data).split(DELIMITER));
    }

    public NGram(String[] data) {
        this.data = new LinkedDeque<>();
        for (String s : data) {
            s = normalize(s);
            if (!s.isEmpty()) {
                this.data.addBack(s);
            }
        }
    }

    public NGram next(String word) {
        String[] data = new String[this.data.size()];
        for (int i = 0; i < data.length - 1; i++) {
            this.data.addBack(this.data.removeFront());
            data[i] = this.data.peekFront();
        }
        this.data.addBack(this.data.removeFront());
        data[data.length - 1] = word;
        return new NGram(data);
    }


    @Override
    public String toString() {
        String result = "";
        String prev = "";
        for (String s : this.data) {
            result += ((NO_SPACE_AFTER.contains(prev) || NO_SPACE_BEFORE.contains(s) || result.isEmpty()) ?  "" : " ") + s;
            prev = s;
        }
        return result.strip();
    }

    @Override
    public Iterator<String> iterator() {
        return this.data.iterator();
    }


    @Override
    public int compareTo(NGram other) {
        if(other == null) throw new NullPointerException();
        if(this.data.size() != other.data.size()) return this.data.size() >= other.data.size() ? 1 : -1;

        Iterator<String> yeet = this.iterator();
        Iterator<String> yoink = other.iterator();

        while(yeet.hasNext() && yoink.hasNext()){
            String str1 = yeet.next();
            String str2 = yoink.next();
            if(!(str1.equals(str2))){
                return str1.compareTo(str2);
            }
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null) return false;
        if(!(o instanceof NGram)) return false;

        NGram yuh = (NGram) o;
        if(yuh.data.size() != this.data.size()) return false;

        Iterator<String> yeet = this.iterator();
        Iterator<String> yoink = yuh.iterator();

        while(yeet.hasNext() && yoink.hasNext()){
            String item1 = yeet.next();
            String item2 = yoink.next();
            if(! item1.equals(item2)) return false;
        }

        return !(yeet.hasNext() || yoink.hasNext());
    }

    @Override
    public int hashCode() {
        int toret = 0;
        int i = 0;
        for(String s : data){
            toret += hashString(s, i);
            i += s.length();
        }
        return toret;
    }

    private int hashString(String s, int ct ){
        int toret = 0;
        int j = ct;
        for(int i = 0; i < s.length(); i++){
            toret += Math.pow(37, j) * s.charAt(i);
            j++;
        }
        return toret;
    }
}
