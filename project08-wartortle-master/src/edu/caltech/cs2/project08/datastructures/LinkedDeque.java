package edu.caltech.cs2.project08.datastructures;


import edu.caltech.cs2.project08.interfaces.IDeque;
import edu.caltech.cs2.project08.interfaces.IQueue;
import edu.caltech.cs2.project08.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private Node start;
    private Node end;
    private int size;

    private static class Node<E>{
        private E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e){
            this.data = e;
        }

        public void setNext(Node<E> n){
            this.next = n;
        }

        public void setPrev(Node<E> n){ this.prev = n;}

        public E getData(){
            return this.data;
        }

        public Node<E> getNext(){
            return this.next;
        }

        public Node<E> getPrev() { return this.prev; }
    }

    public LinkedDeque(){
        this.size = 0;
    }

    @Override
    public String toString(){
        String str = "";
        Node<E> curr = this.start;
        for(int i = 0; i < this.size-1; i++){
            str += curr.getData() + ", ";
            curr = curr.getNext();
        }
        if(this.size > 0){
            str += this.end.getData();
        }
        return "[" + str + "]";
    }

    @Override
    public void addFront(E e) {
        Node<E> n = new Node<E>(e);
        if(this.size == 0){
            this.start = n;
            this.end = n;
        } else {
            this.start.setPrev(n);
            n.setNext(this.start);
            this.start = n;
        }
        this.size += 1;
    }

    @Override
    public void addBack(E e) {
        Node<E> n = new Node<E>(e);
        if(this.size == 0){
            this.start = n;
            this.end = n;
        } else {
            this.end.setNext(n);
            n.setPrev(this.end);
            this.end = n;
        }
        this.size += 1;
    }

    @Override
    public E removeFront() {
        if(this.size > 0){
            E result = this.peekFront();
            this.start = this.start.getNext();
            this.size -= 1;
            if (this.size == 0) {
                this.end = null;
            }
            return result;
        } else{
            return null;
        }
    }

    @Override
    public E removeBack() {
        if(this.size > 0){
            E result = this.peekBack();
            this.end =  this.end.getPrev();
            this.size -= 1;
            if (this.size == 0) {
                this.start = null;
            }
            return result;
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
    public E peekFront() {
        if(this.size != 0){
            return (E)this.start.getData();
        } else {
            return null;
        }
    }

    @Override
    public E peekBack() {
        if(this.size != 0){
            return (E)this.end.getData();
        } else {
            return null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> iter = new Iterator<E>(){

            private Node<E> next = start;

            @Override
            public boolean hasNext(){
                //System.out.println(next.getNext());
                return next != null;
            }

            @Override
            public E next(){
                if(next != null) {
                    E ret = next.getData();
                    next = next.getNext();
                    return ret;
                } else{
                    return null;
                }
            }
        };
        return iter;
    }

    @Override
    public int size() {
        return this.size;
    }
}