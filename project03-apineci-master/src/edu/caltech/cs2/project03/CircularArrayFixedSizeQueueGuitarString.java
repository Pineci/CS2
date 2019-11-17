package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;

public class CircularArrayFixedSizeQueueGuitarString {

  private IFixedSizeQueue<Double> freq;

  private static final double decayFactor = 0.996;
  private static final double noiseFactor = 0.5;
  private static Random rand = new Random();

  public CircularArrayFixedSizeQueueGuitarString(double frequency) {
    int size = (int)(44100/frequency) + 1;
    this.freq  =  new CircularArrayFixedSizeQueue<Double>(size);
    for(int i = 0; i < size; i++){
      this.freq.enqueue(0.0);
    }
  }

  public int length() {
      return this.freq.size();
  }

  public void pluck() {
    for(int i = 0; i < this.length(); i++) {
      this.freq.dequeue();
      this.freq.enqueue(rand.nextDouble()*(2*noiseFactor) - noiseFactor);
     // this.freq.enqueue(0.0);
    }
  }

  public void tic() {
    double temp = this.freq.dequeue();
    this.freq.enqueue((temp + this.sample())/2  * this.decayFactor);
  }

  public double sample() {
      return this.freq.peek();
  }
}
