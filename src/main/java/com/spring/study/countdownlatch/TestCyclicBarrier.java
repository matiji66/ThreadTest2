package com.spring.study.countdownlatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier {
	public static void main(String[] args) {

		int num = 10;
		CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
			@Override
			public void run() {
				System.out.println("go on together!");
			}
		});

		for (int i = 1; i <= num; i++) {
			new Thread(new CyclicBarrierWorker(i, barrier)).start();
		}
	}
}

//  场景分析:10个人去春游,规定达到一个地点后才能继续前行.代码如下
class CyclicBarrierWorker implements Runnable {
	private int id;
	private CyclicBarrier barrier;

	public CyclicBarrierWorker(int id, final CyclicBarrier barrier) {
		this.id = id;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			System.out.println(id + " th people wait");
			barrier.await(); // 大家等待最后一个线程到达
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}


class Solver {
	   final int N;
	   final float[][] data;
	   //A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point. CyclicBarriers are useful in programs involving a fixed sized party of threads that must occasionally wait for each other. 
	   //The barrier is called cyclic because it can be re-used after the waiting threads are released. 
	   final CyclicBarrier barrier;
 
	   class Worker implements Runnable {
	     int myRow;
	     Worker(int row) { myRow = row; }
	     public void run() {
	       while (!done()) {
	         processRow(myRow);

	         try {
	           barrier.await();
	         } catch (InterruptedException ex) {
	           return;
	         } catch (BrokenBarrierException ex) {
	           return;
	         }
	       }
	     }
		private void processRow(int myRow2) {

			try {
				Thread.sleep(myRow2 * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		private boolean done() {
			return false;
		}
	   }

	   public Solver(float[][] matrix) {
	     data = matrix;
	     N = matrix.length;
	     Runnable barrierAction =
	       new Runnable() { public void run() { mergeRows( ); }

		private void mergeRows() {

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}};
	     barrier = new CyclicBarrier(N, barrierAction);

	     List<Thread> threads = new ArrayList<Thread>(N);
	     for (int i = 0; i < N; i++) {
	       Thread thread = new Thread(new Worker(i));
	       threads.add(thread);
	       thread.start();
	     }

	     // wait until done
	     for (Thread thread : threads)
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	   }
	 }