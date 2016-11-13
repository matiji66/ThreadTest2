package cn.itcast.heima;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 创建三个线程   一个线程池
		//ExecutorService threadPool = Executors.newFixedThreadPool(3);
		//自动调节线程的个数
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for ( int j = 0; j < 10;j++) {
			final int task = j;
			//往池子里面放线程  execute 是不要返回值的，submit是可以取得返回值的
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 10; i++) {
						System.out.println(Thread.currentThread().getName()
								+ "loop of " + i+"  for task of " + task );
					}
				}
			});
		}
		//结束后干掉线程池
		threadPool.shutdown();
		//立即干掉线程
		//threadPool.shutdownNow();
		
		
		
		//定时线程池，多少秒后执行
		Executors.newScheduledThreadPool(3).schedule(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			System.out.println("boming");	
			}
		}, 3, TimeUnit.SECONDS);
		
		//定时线程池，多少秒后执行，执行后每个多少秒在执行
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("boming");	
			}
		}, 3,2, TimeUnit.SECONDS);
	}

}
