package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private Node<K,V> front;
    private int size;

    private static class Node<K,V>{
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value){
            this.key = key;
            this.value = value;
        }

        public V getData(){
            return this.value;
        }

    }

    public MoveToFrontDictionary(){
        front = null;
        size = 0;
    }


    private V moveFront(K key){
        Node<K, V> curr = front;

        if(front.key.equals(key)) return front.value;

        while(curr != null && curr.next != null){
            if(curr.next.key.equals(key)){
                Node<K,V> skip = curr.next;
                Node<K, V> replacement = curr.next.next;
                curr.next = replacement;
                skip.next = front;
                front = skip;
                return front.value;
            }
            curr = curr.next;
        }
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> curr = front;
        while(curr != null){
            if(curr.key.equals(key)){
                moveFront(key);
                return front.value;
            }
            curr = curr.next;
        }
        return null;
    }

    @Override
    public V remove(K kee) {
        if(this.containsKey(kee)){
            V toret = moveFront(kee);
            front.value = null;
            front = front.next;
            size--;
            return toret;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        V toret = null;

        Node<K, V> curr = front;
        while(curr != null){
            if(curr.key.equals(key)){
                toret = moveFront(key);
                front.value = value;
                return toret;
            }
            curr = curr.next;
        }

        Node<K, V> newFront = new Node(key, value);
        newFront.next = front;
        front = newFront;
        size++;
        return toret;
    }

    @Override
    public boolean containsKey(K kee) {
        Node<K, V> curr = front;
        while(curr != null){
            if(curr.key.equals(kee)){
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        Node<K,V> curr = front;
        while(curr != null){
            if(curr.value.equals(value)){
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> toRet = new ArrayDeque<>();
        Node<K,V> curr = front;
        while(curr != null){
            toRet.add(curr.key);
            curr = curr.next;
        }
        return toRet;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> toRet = new ArrayDeque<>();
        Node<K,V> curr = front;
        while(curr != null){
            toRet.add(curr.value);
            curr = curr.next;
        }
        return toRet;
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }

    @Override
    public String toString(){
        Node<K, V> curr = front;
        String toret = "[";
        while(curr != null && curr.next != null){
            toret += "(" + curr.key + "->" + curr.value + "), ";
            curr = curr.next;
        }
        toret += "(" + curr.key + "->" + curr.value + ")]";

        return toret;
    }

    public static void main(String[] args){
        MoveToFrontDictionary<Integer, Integer> test = new MoveToFrontDictionary<>();

        for(int i = 0; i < 100; i++){
            test.put(i, i+1);
        }
        System.out.println(test.size());
        test.remove(17);
        test.remove(36);
        test.remove(40);
        test.remove(68);
        test.remove(89);
        System.out.println(test.size());
        System.out.println(test);
        System.out.println(test.containsKey(68));
        System.out.println(test.containsKey(68));


    }



}
