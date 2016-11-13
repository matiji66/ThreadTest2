package cn.itcast.heima2;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CallableAndFuture {

	/**
	 * @param args
	 */
	
	
	
	public static void main(String[] args) {
		ExecutorService threadPool =  Executors.newSingleThreadExecutor();
		Future<String> future =
			threadPool.submit(
				new Callable<String>() {
					public String call() throws Exception {
						Thread.sleep(2000);
						return "hello";
					};
				}
		);
		/**
		 * 等待结果
			Sun Oct 09 09:33:08 CST 2016
			Sun Oct 09 09:33:10 CST 2016
			拿到结果：hello
			Sun Oct 09 09:33:10 CST 2016
			说明在submit的时候就开始执行了任务了
		 */
		System.out.println("等待结果");
		try {
			System.err.println(new Date());
			// 这里可以做一些其他的任务，与此同时我们的job仍然在继续
			Thread.sleep(2000);
			System.err.println(new Date());
			System.out.println("拿到结果：" + future.get());
			System.err.println(new Date());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//提交多个任务  那块先执行完 先取哪个
		ExecutorService threadPool2 =  Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool2);
		for(int i=1;i<=10;i++){
			final int seq = i;
			completionService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Thread.sleep(new Random().nextInt(5000));
					return seq;
				}
			});
		}
		for(int i=0;i<10;i++){
			try {
				System.out.println(
						completionService.take().get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
