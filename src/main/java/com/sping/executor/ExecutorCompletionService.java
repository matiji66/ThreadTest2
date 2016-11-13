package com.sping.executor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorCompletionService {
	
	public static void main(String[] args) {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		ExecutorService threadPool2 = Executors.newFixedThreadPool(2);

		/**
		 * A task that returns a result and may throw an exception. Implementors
		 * define a single method with no arguments called call. The Callable
		 * interface is similar to java.lang.Runnable, in that both are designed
		 * for classes whose instances are potentially executed by another
		 * thread. A Runnable, however, does not return a result and cannot
		 * throw a checked exception. The Executors class contains utility
		 * methods to convert from other common forms to Callable classes.
		 * 该接口类似于Runnable
		 * 接口,需要实现call方法,但是是有返回值的Runnable,并且可能会抛出异常，这两种接口都是为了其他线程使用而设计的. 当然该
		 */

		for (int i = 0; i < 2; i++) {
			if (i==0) {
				testCallable(simpleDateFormat, threadPool2);
				
			}else {
				testRunnable(simpleDateFormat, threadPool2);
			}
		}
		
//		Future<String> future = threadPool.submit(new Callable<String>() {
//			public String call() throws Exception {
//
//				System.err.println(simpleDateFormat.format(new Date())
//						+ "------future task begain-----");
//				Thread.sleep(2000);
//				System.err.println(simpleDateFormat.format(new Date())
//						+ "------future task end -----");
//				return "hello";
//			};
//		});
//
//		System.err.println(simpleDateFormat.format(new Date())
//				+ "------等待future结果-----");
//
//		try {
//			System.err.println(simpleDateFormat.format(new Date())
//					+ "------等待future任务结果中    处理其他的任务开始-----");
//			// 这里可以做一些其他的任务，与此同时我们的job仍然在继续
//			// 当自己处理的任务时间大于future task任务时间的时候，会优先将自己的任务处理完才去future task取结果
//			// 当自己的任务比较少的时候，只能等待future task执行完成才能去取future task的结果。
//			Thread.sleep(5000);
//
//			System.err.println(simpleDateFormat.format(new Date())
//					+ "------等待future任务结果中     处理其他的任务结束-----");
//			System.out.println(simpleDateFormat.format(new Date())
//					+ "------拿到future任务的结果 " + future.get());
//			System.err.println(simpleDateFormat.format(new Date())
//					+ "------全部任务结束-------");
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		/**
		 * 当自己处理的任务时间大于future task任务时间的时候，会优先将自己的任务处理完才去future task取结果
		 * 2016-11-13 19:06:55------等待future结果----- 2016-11-13
		 * 19:06:55------future task begain----- 2016-11-13
		 * 19:06:55------等待future任务结果中 处理其他的任务开始----- 2016-11-13
		 * 19:06:57------future task end ----- 2016-11-13
		 * 19:07:00------等待future任务结果中 处理其他的任务结束----- 2016-11-13
		 * 19:07:00------拿到future任务的结果 hello 2016-11-13
		 * 19:07:00------全部任务结束-------
		 */

		/**
		 * 当自己的任务比较少的时候，只能等待future task执行完成才能去取future task的结果。 等待结果 2016-11-13
		 * 19:03:15------等待future结果----- 2016-11-13 19:03:15------future task
		 * begain----- 2016-11-13 19:03:15------等待future任务结果中 处理其他的任务开始-----
		 * 2016-11-13 19:03:17------等待future任务结果中 处理其他的任务结束----- 2016-11-13
		 * 19:03:20------future task end ----- 2016-11-13
		 * 19:03:17------拿到future任务的结果 hello 2016-11-13
		 * 19:03:20------全部任务结束-------
		 */
	}

	private static void testCallable(final SimpleDateFormat simpleDateFormat,
			ExecutorService threadPool) {
		try {
			
			// 使用方法1.用线程池來提交任务，
			// 使用方法2.直接调用call方法
			threadPool.submit( 
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					System.err.println(simpleDateFormat.format(new Date())
							+ "------Callable test begain-----");
					Thread.sleep(2000);
					System.err.println(simpleDateFormat.format(new Date())
							+ "------Callable test end-----");
					return null;
				}
			}
//			.call();
		  );
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static void testRunnable(final SimpleDateFormat simpleDateFormat,
			ExecutorService threadPool) {
		threadPool.execute(
		new Runnable() {
			
			@Override
			public void run() {
				System.err.println(simpleDateFormat.format(new Date())
						+ "------Runnable test begain-----");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.err.println(simpleDateFormat.format(new Date())
						+ "------Runnable test end-----");
			}
		}
//		.run();
		);
	}
}
