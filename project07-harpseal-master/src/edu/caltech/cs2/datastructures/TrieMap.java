package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ISet;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.Iterator;
import java.util.function.Function;

public class TrieMap<A, K extends Iterable<A>, V> extends ITrieMap<A, K, V> {

    public TrieMap(Function<IDeque<A>, K> collector) {
        super(collector);
    }

    @Override
    public boolean isPrefix(K key){
        return isPrefix(keyChars(key), root);
    }

    private boolean isPrefix(IDeque<A> keyChars, TrieNode<A, V> current){
        if(keyChars.size() == 0){
            return true;
        } else {
            A next = keyChars.removeFront();
            if(!current.pointers.keySet().contains(next)){
                return false;
            } else {
                return isPrefix(keyChars, current.pointers.get(next));
            }
        }
    }

    @Override
    public IDeque<V> getCompletions(K prefix) {
        if(isPrefix(prefix)){
            return getCompletions(keyChars(prefix), root);
        } else {
            return new LinkedDeque<>();
        }
    }

    private IDeque<V> getCompletions(IDeque<A> keyChars, TrieNode<A, V> current){
        for(A c : keyChars){
            current = current.pointers.get(c);
        }
        IDeque<V> completions = new LinkedDeque<>();
        for(K key : keySet(keyChars, current)){
            completions.add(get(key));
        }
        return completions;
    }

    @Override
    public V get(K key) {
        if(containsKey(key)){
            return get(keyChars(key), root);
        }
        else return null;
    }

    private V get(IDeque<A> keyChars, TrieNode<A, V> current){
        if(keyChars.size() == 0){
            return current.value;
        } else {
            A next = keyChars.removeFront();
            current = current.pointers.get(next);
            return get(keyChars, current);
        }
    }

    @Override
    public V remove(K key) {
        if(containsKey(key)) {
            V ret = get(key);
            TrieNode<A, V> toKeep = root;
            TrieNode<A, V> current = root;
            IDeque<A> chars = keyChars(key);
            A path = chars.peekFront();
            while(chars.size() > 0){
                A next = chars.removeFront();
                current = current.pointers.get(next);
                if (chars.size() > 0) {
                    if (current.value != null || current.pointers.size() > 1) {
                        toKeep = current;
                        path = chars.peekFront();
                    }
                } else if (current.pointers.size() > 0) {
                    toKeep = current;
                }
            }
            if (toKeep.equals(current)) {
                toKeep.value = null;
            } else {
                toKeep.pointers.remove(path);
            }
            size--;
            return ret;
        } else {
            return null;
        }
    }

    private IDeque<A> keyChars(K key){
        IDeque<A> keyChars = new LinkedDeque<A>();
        for(A item : key){
            keyChars.addBack(item);
        }
        return keyChars;
    }

    @Override
    public V put(K key, V value) {
        return put(keyChars(key), value, root);
    }

    private V put(IDeque<A> key, V value, TrieNode<A, V> current){
        if(key.size() == 1){
            A next = key.peekFront();
            if(current.pointers.containsKey(next)){
                if(current.pointers.get(next).value == null){
                    size += 1;
                }
                current.pointers.get(next).value = value;
            } else {
                current.pointers.put(next, new TrieNode<>(value));
                size += 1;
            }
            return value;
        } else {
            A next = key.removeFront();
            if(!current.pointers.containsKey(next)){
                current.pointers.put(next, new TrieNode<>());
            }
            current = current.pointers.get(next);
            return put(key, value, current);
        }
    }

    @Override
    public boolean containsKey(K key) {
        IDeque<A> keyChars = keyChars(key);
        return containsKey(keyChars, root);
    }

    private boolean containsKey(IDeque<A> keyChars, TrieNode<A, V> current){
        if(keyChars.size() == 0){
            return current.value != null;
        } else {
            A next = keyChars.removeFront();
            if(!current.pointers.keySet().contains(next)){
                return false;
            }
            current = current.pointers.get(next);
            return containsKey(keyChars, current);
        }
    }

    @Override
    public boolean containsValue(V value) {
        return containsValue(value, root);
    }

    private boolean containsValue(V value, TrieNode<A, V> current){
        if(current == null){
            return false;
        } else if(current.value == value){
            return true;
        } else {
            boolean contains = false;
            for(TrieNode<A, V> n : current.pointers.values()){
                if(contains){
                    break;
                } else{
                    contains = contains || containsValue(value, n);
                }
            }
            return contains;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keys() {
        return keySet(new LinkedDeque<A>(), root);
    }

   /* @Override
    public ICollection<K> keys(){
        return keySet();
    }*/

    private ICollection<K> keySet(IDeque<A> keyChars, TrieNode<A, V> current){
        IDeque<K> keys = new LinkedDeque<>();
        if(current == null){
            return keys;
        } else if (current.value != null){
            keys.add(collector.apply(keyChars));
        }
        for(A next : current.pointers.keySet()){
            IDeque<A> newChars = keyChars(collector.apply(keyChars));
            newChars.addBack(next);
            keys.addAll(keySet(newChars, current.pointers.get(next)));
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> vals = new LinkedDeque<>();
        for (K key : keySet()){
            vals.add(get(key));
        }
        return vals;
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }
}