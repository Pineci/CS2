package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private IDictionary<K, V>[] arr;
    private int size;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain){
        this.chain = chain;
        this.arr = new IDictionary[primeBelow(100)];
    }

    private int mod(K key, int l){
        return ((key.hashCode() % l) + l) % l;
    }

    private IDeque<Integer> primeSieve(int n){
        boolean[] primality = new boolean[n];
        for(int i = 0; i < primality.length; i++){
            primality[i] = true;
        }
        primality[0] = false;
        int p = 2;
        while(p*p <= n) {
            for (int i = p; i * p <= n; i++) {
                primality[i * p - 1] = false;
            }
            for(int i = p; i < n; i++){
                if(primality[i]){
                    p = i+1;
                    break;
                }
            }
        }
        IDeque<Integer> primes = new LinkedDeque<>();
        for(int i = 0; i<n; i++){
            if(primality[i]){
                primes.addBack(i+1);
            }
        }
        return primes;
    }

    private int primeBelow(int n){
        IDeque<Integer> primes = primeSieve(n);
        return primes.peekBack();
    }

    @Override
    public V get(K key) {
        IDictionary<K, V> dic = arr[mod(key, arr.length)];
        return dic == null ? null : dic.get(key);
    }

    @Override
    public V remove(K key) {
        int code = mod(key, arr.length);
        IDictionary<K, V> dic = arr[code];
        if(dic != null){
            V val = dic.remove(key);
            if(dic.size() == 0){
                arr[code] = null;
            }
            if(val != null){
                size--;
            }
            return val;
        } else {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        int code = mod(key, arr.length);
        if(arr[code] == null){
            arr[code] = chain.get();
        }
        int tempsize = arr[code].size();
        arr[code].put(key, value);
        if(tempsize != arr[code].size()){
            size++;
        }
        reHash();
        return value;
    }

    private void reHash(){
        if(size/(float)arr.length > 1.05){
            IDictionary<K, V>[] newArr = new IDictionary[primeBelow(arr.length*10)];
            for(K key : keySet()){
                int code = mod(key, newArr.length);
                if(newArr[code] == null){
                    newArr[code] = chain.get();
                }
                newArr[code].put(key, get(key));
            }
            arr = newArr;

        }
    }

    @Override
    public boolean containsKey(K key) {
        IDictionary<K, V> c = arr[mod(key, arr.length)];
        if(c == null){
            return false;
        } else {
            return c.containsKey(key);
        }
    }

    @Override
    public boolean containsValue(V value) {
        for(IDictionary<K, V> dic : arr){
            if(dic != null && dic.containsValue(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICollection<K> keys() {
        LinkedDeque<K> keys = new LinkedDeque<>();
        for(IDictionary<K, V> dic : arr){
            if(dic != null){
                keys.addAll(dic.keys());
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        LinkedDeque<V> vals = new LinkedDeque<>();
        for(IDictionary<K, V> dic : arr){
            if(dic != null){
                vals.addAll(dic.values());
            }
        }
        return vals;
    }

    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator();
    }

    public static void main(String[] args){
        ChainingHashDictionary<Integer, Integer> test = new ChainingHashDictionary<>(MoveToFrontDictionary::new);

        System.out.println(test.primeBelow(300000));
    }
}
