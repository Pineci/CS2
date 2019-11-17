package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 5;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        Integer idx = keyToIndexMap.get(key.data);
        if(idx == null){
            throw new IllegalArgumentException();
        }
        if(idx >= size){
            throw new IllegalArgumentException();
        }
        data[idx] = key;
        sendErDown(idx);
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        Integer idx = keyToIndexMap.get(key.data);
        if(idx == null){
            throw new IllegalArgumentException();
        }
        data[idx] = key;
        sendErUp(idx);
    }

    private void sendErUp(int ind){
        while(ind > 0 && data[ind].priority < data[parent(ind)].priority){
            swap(ind, parent(ind));
            keyToIndexMap.put(data[parent(ind)].data, ind);
            keyToIndexMap.put(data[ind].data, ind);
            ind = parent(ind);
        }
        keyToIndexMap.put(data[ind].data, ind);
    }

    private void sendErDown(int ind){
        int minLoc = minChild(ind);
        while(minLoc < size && data[ind].priority > data[minLoc].priority){
            swap(minLoc, ind);
            keyToIndexMap.put(data[minLoc].data, minLoc);
            keyToIndexMap.put(data[ind].data, ind);
            ind = minLoc;
            minLoc = minChild(ind);
        }
    }

    private int minChild(int ind){
        int minInd = 4*ind + 1;
        for(int i = 1; i < 4; i++){
            int newInd = 4 * ind + (i+1);
            if(newInd >= size){
                return minInd;
            }

            if(data[newInd].priority < data[minInd].priority ){
                minInd = newInd;
            }
        }
        return minInd;
    }

    private int parent(int cur){
        return (int)Math.floor((cur - 1) / 4.0);
    }

    private void swap(int down, int up){
        PQElement<E> temp = data[up];
        data[up] = data[down];
        data[down] = temp;
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if(epqElement == null){
            throw new IllegalArgumentException();
        }
        if(size >= data.length){
            doubleDataLength();
        }
        if(checkDuplicate(epqElement)){
            throw new IllegalArgumentException();
        }

        data[size] = epqElement;
        keyToIndexMap.put(epqElement.data, size);
        sendErUp(size);
        size++;
        return true;
    }

    private boolean checkDuplicate(PQElement<E> epqElement){
        Integer posCont = keyToIndexMap.get(epqElement.data);
        if(posCont != null){
            if(data[posCont].priority == epqElement.priority){
                return true;
            }
        }
        return false;
    }

    private void doubleDataLength(){
        PQElement<E>[] newArr = new PQElement[data.length*2];
        for(int i = 0; i < data.length; i++){
            newArr[i] = data[i];
        }
        data = newArr;
    }

    @Override
    public PQElement<E> dequeue() {
        if(size == 0){
            throw new IllegalArgumentException();
        }

        PQElement<E> toRet = data[0];
        if(size == 1){
            size--;
            return toRet;
        }

        data[0] = data[size - 1];
        sendErDown(0);
        size--;
        keyToIndexMap.remove(toRet.data);
        return toRet;

    }

    @Override
    public PQElement<E> peek() {
        return data[0];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return null;
    }

    /*public static void main(String[] args) {
        MinFourHeap<Integer> test = new MinFourHeap<Integer>();
        for(int i = 0; i < 20; i++){
            test.enqueue( new PQElement<>(i * 10, (int)Math.random() + i));
        }
        test.decreaseKey(new PQElement<>(150, 1));
    }*/
}