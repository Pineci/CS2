package edu.caltech.cs2.lab01;

import edu.caltech.cs2.lab01.libraries.Wikipedia;

public class WikipediaPage {
    private String title;
    private String text;

    public WikipediaPage(String title, boolean followRedirects) {
        this.title = convertToTitleCase(title);
        if (this.isValid()) {
            this.text = this.getText();
            if (followRedirects && this.isRedirect()) {
                this.text = Wikipedia.getPageText(Wikipedia.parseMarkup(this.getText()));
            }
        }

    }

    public WikipediaPage(String title) {
        this(title, true);
    }

    private static String convertToTitleCase(String s) {
        s = s.replace("_", " ");
        String[] words = s.split(" ");
        String toRet = "";
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            word = word.substring(0, 1).toUpperCase() + word.substring(1, word.length()).toLowerCase();
            toRet += word + " ";
        }
        return toRet.strip();

    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        // Fill me in!
        return Wikipedia.getPageText(title);
    }

    public boolean isRedirect() {
        return this.isValid() && this.getText().toLowerCase().startsWith("#redirect");
    }

    public boolean isValid() {
        return this.getText() != null;
    }

    public boolean isGalaxy() {
        System.out.println(text);
        return this.isValid() && this.text.toLowerCase().contains("infobox galaxy");
    }

    public boolean hasNextLink() {
        // Fill me in!
        return false;
    }

    public String nextLink() {
        // Fill me in!
        return null;
    }
}