package study.queue;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 特点：FIFO add() // 可以一直添加、 ： 这个类似于ConcurrentLinkedQueue
 * transfer（）//可以先添加然后等待，此时队列的长度一直等于0： 这个类似于SynchronousQueue
 * take()取走。//没有拿到数据会一直阻塞 poll();// 不阻塞，没有值就是null，取走之后数据被移除 ： 这个类似于
 * unbounded（无限大的） LinkedBlockingQueue （=ConcurrentLinkedQueue），也就不会阻塞
 * peek();//拿走数据但是并不减少
 *
 */
public class LinkedTransferQueueTest {
	public static void main(String[] args) {
		// testLikedTransformQueueTest1();

		LinkedTransferQueueTest linkedTransferQueueTest = new LinkedTransferQueueTest();

		try {
			linkedTransferQueueTest.testTansferQueue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testTansferQueue() throws InterruptedException {
		final TransferQueue<String> transferQueue = new LinkedTransferQueue<String>();
		ExecutorService executorService = Executors.newCachedThreadPool();

		//若当前存在一个正在等待获取的消费者线程，即立刻移交之
		//否则，会插入当前元素e到队列尾部，并且等待进入阻塞状态，到有消费者线程取走该元素。
		final String key = "test";
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						//若当前存在一个正在等待获取的消费者线程（使用 take() 或者 poll() 函数），使用该方法会即刻转移 / 传输对象元素 e ；
						//若不存在，则返回 false ，并且不进入队列。这是一个不阻塞的操作。
//						transferQueue.tryTransfer(key);
						
						//若当前存在一个正在等待获取的消费者线程，会立即传输给它 ; 否则将插入元素 e 到队列尾部，并且等待被消费者线程获取消费掉 , 
						//若在指定的时间内元素 e 无法被消费者线程获取，则返回 false ，同时该元素被移除
						transferQueue.tryTransfer(key, 1, TimeUnit.SECONDS);

						//4.判断是否存在消费者线程
						boolean hasWaitingConsumer = transferQueue.hasWaitingConsumer();
						System.out.println("------------hasWaitingConsumer " +hasWaitingConsumer );
						//5.获取所有等待获取元素的消费线程数量
						int waitingConsumerCount = transferQueue.getWaitingConsumerCount();
						
						System.out.println("------------waitingConsumerCount  " +waitingConsumerCount );
						
						int size = transferQueue.size();
						System.out.println("-----size " +size);
//						String test = transferQueue.take();
//						System.out.printf("主线程完成获取 %s.\n", test);
						
						// 此处阻塞，等待take()，poll()的发生。
//						transferQueue.transfer(key + Math.random( ));
						System.out.println("子线程完成传递.");
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			});
		}
		
		// 此处阻塞，等待trankser(当然可以是别的插入元素的方法)的发生
		for (int i = 0; i < 5; i++) {
			
			executorService.execute(new Runnable() {
				public void run() {
					String test = null;
					try {
						test = transferQueue.take();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.printf("主线程完成获取 %s.\n", test);
				}
			});
					
		}
		Thread.sleep(1000);
	}

	private static void testLikedTransformQueueTest1() {
		final TransferQueue<String> queue = new LinkedTransferQueue<String>();

		for (int i = 0; i < 5; i++) {
			new Thread() {
				public void run() {
					while (true) {
						try {
							Thread.sleep((long) (Math.random() * 1000));
							System.out.println(Thread.currentThread().getName()
									+ "准备放数据!");
							// queue.transfer("1");
							queue.add("1");
							System.out.println(Thread.currentThread().getName()
									+ "已经放了数据，" + "队列目前有" + queue.size()
									+ "个数据");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}

			}.start();
		}

		new Thread() {
			public void run() {
				while (true) {
					try {
						// 将此处的睡眠时间分别改为100和1000，观察运行结果
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName()
								+ "准备取数据!");
						String reString = queue.peek();
						System.out.println(Thread.currentThread().getName()
								+ "已经取走数据，" + reString + "队列目前有" + queue.size()
								+ "个数据");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}
}
