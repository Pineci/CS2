package edu.caltech.cs2.datastructures;

public class AVLTreeDictionary<K extends Comparable<K>, V> extends BSTDictionary<K, V> {
    private AVLNode<K, V> root;

    private static class AVLNode<K, V> extends BSTNode<K, V> {

        public int height;
        public AVLNode<K, V>[] children;

        public AVLNode(K key, V value, int height) {
            super(key, value);
            this.height = height;
            this.children = (AVLNode<K, V>[]) new AVLNode[2];
        }

    }

    /**
     * Overrides the remove method in BST
     *
     * @param key
     * @return The value of the removed AVLNode if it exists, null otherwise
     */
    @Override
    public V remove(K key) {
        V torem = get(key);
        this.remove(key, root, 1);
        if(root.children[1].height - root.children[0].height > 1){
            V placeholder = get(key);
            this.remove(key, root, 1);
            put(key, placeholder);
        }
        if(root.children[0].height - root.children[1].height > 1){
            V placeholder = get(key);
            this.remove(key, root, 0);
            put(key, placeholder);
        }
        return torem;
    }
    
    @Override
    public V put(K key, V value){
        V toret = this.get(key);
        this.root = put(key, value, root);
        return toret;
    }   
    
    private AVLNode<K, V> put(K key, V value, AVLNode<K, V> nod){
        if(nod == null){
            size++;
            return new AVLNode<>(key, value, 0);
        }
        if(nod.key.equals(key)){
            AVLNode<K, V> newnod = new AVLNode<K, V>(key, value, nod.height);
            newnod.children = nod.children;
            return newnod;
        }
        
        int comp = key.compareTo(nod.key);
        int checkBlah = comp > 0 ? 0 : 1;
        
        if(nod.children[checkBlah] == null){
            nod.height = 1;
        }
        nod.children[checkBlah] = put(key, value, nod.children[checkBlah]);
        if(nod.height + 1 != nod.children[checkBlah].height){
            nod.height += 1;
        }

        return nod;
    }

    private AVLNode<K, V> remove(K key, AVLNode<K, V> nod, int dir){
        if(nod == null) return null;

        int otherdir = (dir + 1) % 2;

        if(key.compareTo(nod.key) > 0) {
            nod.children[0] = remove(key, nod.children[0], 1);
            return nod;
        }
        if(key.compareTo(nod.key) < 0) {
            nod.children[1] = remove(key, nod.children[1], 1);
            return nod;
        }
        if(key.equals(nod.key)){
            if(nod.children[0] == nod.children[1]){ //The only way they can be equal is if they're both null
                size--;
                return null;
            }

            for(int i = 0; i < 2; i++){
                if(nod.children[i] == null){
                    size--;
                    return nod.children[(i+1)% 2]; //hehe
                }
            }

            //At this point we want to remove a node with two children
            AVLNode<K, V> leastGreatest = minNode(nod.children[dir], otherdir);
            leastGreatest.children[otherdir] = nod.children[otherdir];
            size--;
            return nod.children[dir];
        }
        return null;
    }


    private AVLNode<K, V> minNode(AVLNode nod, int dir){
        while(nod.children[0] != null){
            nod = nod.children[1];
        }
        return nod;
    }
}



