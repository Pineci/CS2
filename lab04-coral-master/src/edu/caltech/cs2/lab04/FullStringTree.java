package edu.caltech.cs2.lab04;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullStringTree {
    protected StringNode root;

    protected static class StringNode {
        public final String data;
        public StringNode left;
        public StringNode right;

        public StringNode(String data) {
            this(data, null, null);
        }

        public StringNode(String data, StringNode left, StringNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
            // Ensures that the StringNode is either a leaf or has two child nodes.
            if ((this.left == null || this.right == null) && !this.isLeaf()) {
                throw new IllegalArgumentException("StringNodes must represent nodes in a full binary tree");
            }
        }

        // Returns true if the StringNode has no child nodes.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    protected FullStringTree() {

    }

    public FullStringTree(Scanner in) {
        root = deserialize(in);
    }

    private StringNode deserialize(Scanner in) {
        if(in.hasNextLine()){
            String line = in.nextLine();
            StringNode nodeh = new StringNode(line.substring(line.indexOf(" ") + 1));
            if(line.charAt(0) == 'L'){
                return nodeh;
            }
            nodeh.left = deserialize(in);
            nodeh.right = deserialize(in);
            return nodeh;
        }
        else return null;
    }

    public List<String> explore() {
        return explore(root);
    }

    public List<String> explore(StringNode n){
        List<String> toret = new ArrayList<String>();

        if(n == null){
            return toret;
        }

        String prefix = "I: ";
        if(n.left == null && n.right == null){
            prefix = "L: ";
        }

        toret.add(prefix + n.data);

        toret.addAll(explore(n.left));
        toret.addAll(explore(n.right));

        return toret;
    }

    public void serialize(PrintStream output) {
        for(String i : explore()){
            output.println(i);
        }
    }
}