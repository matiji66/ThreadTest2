package study.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentLinkedQueueTest {

	public static void main(String[] args) {
		final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
		
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 1000; i++) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						boolean add = queue.add(10);

						queue.add(11);
						queue.add(12);
						//Retrieves but does not remove, the head of this queue, or returns null if this queue is empty.
						// 尝试获得第一个元素，但是并不移除
						Integer peek = queue.peek();
						
						// 拿到当前队列最前面的元素 并且从队列中移除
						// Retrieves and removes the head of this queue, or returns null if this queue is empty.
						Integer poll = queue.poll();
						queue.remove(11);
						queue.poll();
						poll = queue.poll();
//						System.out.println("-----add : " + add + "---poll : " + poll
//								+ "---peek : " + peek);
					}
				}
			});
		}
		pool.shutdown();
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-----finished--------- ");

		
	}
}
