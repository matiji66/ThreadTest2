package com.spring.study.countdownlatch;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier ============================ 线程之间进行先后执行的工具
 * CyclicBarrier类似于CountDownLatch也是个计数器
 * 不同的是CyclicBarrier数的是调用了CyclicBarrier.await()进入等待的线程数，
 * 当线程数达到了CyclicBarrier初始时规定的数目时，所有进入等待状态的线程被唤醒并继续。
 * CyclicBarrier就象它名字的意思一样，可看成是个障碍， 所有的线程必须到齐后才能一起通过这个障碍。
 * CyclicBarrier初始时还可带一个Runnable的参数，
 * 此Runnable任务在CyclicBarrier的数目达到后，所有其它线程被唤醒前被执行。
 * 
 * @author Administrator
 *
 */
public class CyclicBarrierTest {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final CyclicBarrier cb = new CyclicBarrier(3);
		for (int i = 0; i < 10; i++) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println("线程"
								+ Thread.currentThread().getName()
								+ "即将到达集合地点1，当前已有"
								+ (cb.getNumberWaiting() + 1)
								+ "个已经到达，"
								+ (cb.getNumberWaiting() == 2 ? "都到齐了，继续走啊"
										: "正在等候"));
						cb.await();

						Thread.sleep((long) (Math.random() * 10000));
						System.out.println("线程"
								+ Thread.currentThread().getName()
								+ "即将到达集合地点2，当前已有"
								+ (cb.getNumberWaiting() + 1)
								+ "个已经到达，"
								+ (cb.getNumberWaiting() == 2 ? "都到齐了，继续走啊"
										: "正在等候"));

						cb.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			System.out.println("111111");
			service.execute(runnable);
		}
		service.shutdown();
	}
}
