package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;


import java.util.Iterator;
import java.util.Random;

public class BSTDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {

    private static int LEFT = 0;
    private static int RIGHT = 1;

    protected BSTNode<K, V> root;
    protected int size;

    public BSTDictionary(){
        root = null;
        size = 0;
    }

    @Override
    public V get(K key) {
        return get(key, root);
    }

    private V get(K key, BSTNode<K, V> node){
        if(node == null){
            return null;
        }

        if(key.equals(node.key)) return node.value;

        return node.key.compareTo(key) < 0 ? get(key, node.children[LEFT]) : get(key, node.children[RIGHT]);
    }

    @Override
    public V remove(K key) {
        V toret = this.get(key);
        this.root = remove(key, root);
        return toret;
    }

    private BSTNode<K, V> remove(K key, BSTNode<K, V> nod){
        if(nod == null) return null;

        if(key.compareTo(nod.key) > 0) {
            nod.children[LEFT] = remove(key, nod.children[LEFT]);
            return nod;
        }
        if(key.compareTo(nod.key) < 0) {
            nod.children[RIGHT] = remove(key, nod.children[RIGHT]);
            return nod;
        }
        if(key.equals(nod.key)){
            if(nod.children[LEFT] == nod.children[RIGHT]){ //The only way they can be equal is if they're both null
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
            BSTNode<K, V> leastGreatest = minNode(nod.children[RIGHT]);
            leastGreatest.children[LEFT] = nod.children[LEFT];
            size--;
            return nod.children[RIGHT];
        }
        return null;
    }

 /*   private BSTNode<K, V> minNodeRem(BSTNode nod){
        BSTNode<K, V> nextnod = nod.children[LEFT];
        while(nextnod.children[LEFT] != null){
            nod = nextnod;
            nextnod = nextnod.children[LEFT];
        }
        nod.children[LEFT] = null;
        return nextnod;
    }*/

    private BSTNode<K, V> minNode(BSTNode nod){
        while(nod.children[LEFT] != null){
            nod = nod.children[LEFT];
        }
        return nod;
    }

    @Override
    public V put(K key, V value) {
        V toret = this.get(key);
        this.root = put(key, value, root);
        return toret;
    }

    private BSTNode<K, V> put(K key, V value, BSTNode<K, V> nod){
        if(nod == null) {
            size++;
            return new BSTNode<>(key, value);
        }
        if(nod.key.equals(key)){
            BSTNode<K, V> newnod = new BSTNode<>(key, value);
            newnod.children = nod.children;
            return newnod;
        }

        int comp = key.compareTo(nod.key);
        int checkBlah = comp > 0 ? LEFT : RIGHT;

        nod.children[checkBlah] = put(key, value, nod.children[checkBlah]);

        return nod;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, BSTNode<K, V> nod){
        if(nod == null) return false;
        if(nod.key.equals(key)) return true;
        if(key.compareTo(nod.key) > 0){
            return containsKey(key, nod.children[LEFT]);
        }
        return containsKey(key, nod.children[RIGHT]);
    }

    @Override
    public boolean containsValue(V value) {
        return containsValue(value, root);
    }

    private boolean containsValue(V value, BSTNode nod){
        if(nod == null) return false;
        if(nod.value.equals(value)) return true;

        return containsValue(value, nod.children[LEFT]) || containsValue(value, nod.children[RIGHT]);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keySet() {
        return keySet(new LinkedDeque<K>(), root);
    }

    private ICollection<K> keySet(LinkedDeque<K> currDeque, BSTNode<K, V> nod){
        if(nod == null) return currDeque;

        ICollection<K> leftkeys = keySet(currDeque, nod.children[LEFT]);
        ICollection<K> rightkeys = keySet(currDeque, nod.children[RIGHT]);

        ICollection<K> toret = new LinkedDeque<>();

        for(K I : rightkeys){
            toret.add(I);
        }
        toret.add(nod.key);
        for(K I: leftkeys){
            toret.add(I);
        }
        return toret;
    }

    @Override
    public ICollection<V> values() {
        return values(new LinkedDeque<V>(), root);
    }

    private ICollection<V> values(LinkedDeque<V> currDeque, BSTNode<K, V> nod){
        if(nod == null) return currDeque;

        ICollection<V> leftkeys = values(currDeque, nod.children[LEFT]);
        ICollection<V> rightkeys = values(currDeque, nod.children[RIGHT]);

        ICollection<V> toret = new LinkedDeque<>();

        for(V I : rightkeys){
            toret.add(I);
        }
        toret.add(nod.value);
        for(V I: leftkeys){
            toret.add(I);
        }
        return toret;

    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            LinkedDeque<K> iterthru = (LinkedDeque<K>) keySet();

            @Override
            public boolean hasNext() {
                return iterthru.size() > 0;
            }

            @Override
            public K next() {
                return iterthru.dequeue();
            }
        };
    }
    
    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.children[0] != null) {
                nodes.enqueue(current.children[0]);
            }
            if (current.children[1] != null) {
                nodes.enqueue(current.children[1]);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }

    protected static class BSTNode<K, V> {
        public final K key;
        public final V value;
        public BSTNode<K, V>[] children;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.children = (BSTNode<K, V>[]) new BSTNode[2];
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.children = (BSTNode<K, V>[]) new BSTNode[2];
            this.children[LEFT] = o.children[LEFT];
            this.children[RIGHT] = o.children[RIGHT];
        }

        public boolean isLeaf() {
            return this.children[LEFT] == null && this.children[RIGHT] == null;
        }

        public boolean hasBothChildren() {
            return this.children[LEFT] != null && this.children[RIGHT] != null;
        }
    }
}
