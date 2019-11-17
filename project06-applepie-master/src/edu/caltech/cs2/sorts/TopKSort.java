package edu.caltech.cs2.sorts;

import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IPriorityQueue;

public class TopKSort {
    /**
     * Sorts the largest K elements in the array in descending order.
     * @param array - the array to be sorted; will be manipulated.
     * @param K - the number of values to sort
     * @param <E> - the type of values in the array
     */
    public static <E> void sort(IPriorityQueue.PQElement<E>[] array, int K) {
        if (K < 0) {
            throw new IllegalArgumentException("K cannot be negative!");
        }
        if(array.length != 0 && K != 0) {
            MinFourHeap<E> sorter = new MinFourHeap<>();
            int i = 0;
            for (; i < K; i++) {
                sorter.enqueue(array[i]);
            }
            while (i < array.length) {
                if (sorter.peek().priority < array[i].priority) {
                    sorter.dequeue();
                    sorter.enqueue(array[i]);
                }
                i++;
            }
            for (i = K-1; i >= 0; i--) {
                array[i] = sorter.dequeue();
            }
            for(i = K; i < array.length; i++){
                array[i] = null;
            }
        }
        if(K == 0){
            for(int i = 0; i < array.length; i++){
                array[i] = null;
            }
        }
    }
}
