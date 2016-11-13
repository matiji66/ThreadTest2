package com.sping.executor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//当我们通过Executor提交一组并发执行的任务，并且希望在每一个任务完成后能立即得到结果，有两种方式可以采取：
//
//方式一：
//通过一个list来保存一组future，然后在循环中轮训这组future,直到每个future都已完成。如果我们不希望出现因为排在前面的任务阻塞导致后面先完成的任务的结果没有及时获取的情况，那么在调用get方式时，需要将

public class CompletionServiceTest {

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		testUseFuture();
	}

	static class Task implements Callable<String> {
		private int i;

		public Task(int i) {
			this.i = i;
		}

		@Override
		public String call() throws Exception {
			Thread.sleep(1000);
			System.out.println(simpleDateFormat.format(new Date()) + "----");

			return Thread.currentThread().getName() + "执行完任务：" + i;
		}
	}

	private static void testUseFuture() {
		int numThread = 5;
		// 固定的线程池
		ExecutorService executor = Executors.newFixedThreadPool(numThread);
		List<Future<String>> futureList = new ArrayList<Future<String>>();
		CopyOnWriteArrayList<Future<String>> copyOnWriteArrayList = new CopyOnWriteArrayList<Future<String>>();

		// 线程池使用的时候一般在循环中调度使用，才能发挥线程的优势
		for (int i = 0; i < numThread; i++) {
			Future<String> future = executor.submit(new CompletionServiceTest.Task(i));

			copyOnWriteArrayList.add(future);
			futureList.add(future);
		}

		while (numThread > 0) {
//			for (Future<String> future : copyOnWriteArrayList) {
//				String result = null;
//				try {
//					result = future.get(0, TimeUnit.SECONDS);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				} catch (TimeoutException e) {
//					// 超时异常直接忽略
//				}
//				// 已经获得结果的future则从list中移除。
//				if (null != result) {
//					futureList.remove(future);
//					numThread--;
//					System.out.println(simpleDateFormat.format(new Date())
//							+ "-----copyOnWriteArrayList " + result);
//					// 此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
//					// java.util.ConcurrentModificationException
//					// break;
//				}
//			}

			for (Future<String> future : futureList) {
				String result = null;
				try {
					result = future.get(0, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					// 超时异常直接忽略
				}
				// 已经获得结果的future则从list中移除。
				if (null != result) {
					futureList.remove(future);
					numThread--;
					System.out.println(simpleDateFormat.format(new Date())
							+ "-----futureList " + result);
					// 此处必须break，否则会抛出并发修改异常。（也可以通过将futureList声明为CopyOnWriteArrayList类型解决）
					// java.util.ConcurrentModificationException
					break;
				}
			}
		}

	}
}