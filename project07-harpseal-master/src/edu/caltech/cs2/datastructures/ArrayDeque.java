package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private E[] arr;
    private int size;
    private static final int defaultCapacity = 10;
    private static final int growFactor = 2;

    public ArrayDeque(){
        this.arr = (E[])(new Object[defaultCapacity]);
        this.size = 0;
    }

    public ArrayDeque(int initialCapacity){
        arr = (E[])(new Object[initialCapacity]);
        this.size = 0;
    }

    @Override
    public String toString(){
        String str = "";
        for(int i = 0; i < this.size-1; i++){
            str += arr[i] + ", ";
        }
        if(this.size > 0){
            str += arr[this.size-1];
        }
        return "[" + str + "]";
    }

    private void ensureSpace(){
        int startSize = this.arr.length;
        if(this.size >= startSize) {
            E[] newArr = (E[]) (new Object[startSize * this.growFactor]);
            for (int i = 0; i < startSize; i++) {
                newArr[i] = this.arr[i];
            }
            this.arr = newArr;
        }
    }

    @Override
    public void addBack(E e) {
        this.ensureSpace();
        this.arr[this.size] = e;
        this.size += 1;
    }

    @Override
    public void addFront(E e) {
        this.ensureSpace();
        for(int i = this.size; i > 0; i--){
            arr[i] = arr[i-1];
        }
        this.arr[0] = e;
        this.size += 1;
    }

    @Override
    public E removeBack() {
        if(this.size > 0){
            this.size -= 1;
            return arr[this.size];
        } else{
            return null;
        }
    }

    @Override
    public E removeFront() {
        if(this.size > 0) {
            E removed = this.arr[0];
            for (int i = 0; i < this.size - 1; i++) {
                this.arr[i] = this.arr[i + 1];
            }
            this.size -= 1;
            return removed;
        } else {
            return null;
        }
    }

    @Override
    public boolean enqueue(E e) {
        this.addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return this.removeBack();
    }

    @Override
    public boolean push(E e) {
        this.addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return this.removeBack();
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public E peekBack() {
        if (this.size == 0){
            return null;
        } else {
            return this.arr[this.size - 1];
        }
    }

    @Override
    public E peekFront() {
        if (this.size == 0){
            return null;
        } else {
            return this.arr[0];
        }
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> iter = new Iterator<E>(){

            private int idx = 0;

            @Override
            public boolean hasNext(){
                return idx < size;
            }

            @Override
            public E next(){
                return arr[idx++];
            }
        };
        return iter;
    }

    @Override
    public int size() {
        return this.size;
    }
}
