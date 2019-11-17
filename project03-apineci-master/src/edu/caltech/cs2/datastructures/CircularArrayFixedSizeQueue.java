package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {

    private E[] arr;
    private int front = 0;
    private int back = 0;

    public CircularArrayFixedSizeQueue(int capacity){
        arr = (E[])(new Object[capacity]);
    }

    public String toString(){
        String str = "";
        for(int i = 0; i < this.size()-1; i++){
            str += arr[(i+this.front) % this.capacity()] + ", ";
        }
        if(this.size() > 0){
            str += arr[(this.back-1) % this.capacity()];
        }
        return "[" + str + "]";
    }

    @Override
    public boolean isFull() {
        return this.size() == this.capacity();
    }

    @Override
    public int capacity() {
        return this.arr.length;
    }

    @Override
    public boolean enqueue(E e) {
        if(this.isFull()){
            return false;
        } else {
            this.arr[this.back % this.capacity()] = e;
            this.back += 1;
            return true;
        }
    }

    @Override
    public E dequeue() {
        if(this.size() == 0){
            return null;
        } else {
            E ret = this.arr[this.front % this.capacity()];
            this.front += 1;
            return ret;
        }
    }

    @Override
    public E peek() {
        return this.arr[this.front % this.capacity()];
    }

    @Override
    public int size() {
        int ret = (this.back - this.front);
        return ret;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> iter = new Iterator<E>(){

            private int count;

            @Override
            public boolean hasNext(){
                return count < capacity();
            }

            @Override
            public E next(){
                count += 1;
                return arr[count-1+front % capacity()];
            }
        };
        return iter;
    }
}
