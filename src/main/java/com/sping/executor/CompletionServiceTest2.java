package com.sping.executor;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

 
//方式二：
//第一种方式显得比较繁琐，通过使用ExecutorCompletionService，则可以达到代码最简化的效果。
 public class CompletionServiceTest2 {  
  
     static class Task   implements   Callable<String>{  
        private int i;  
          
        public Task(int i){  
            this.i = i;  
        }  
  
        @Override  
        public String call() throws Exception {  
            Thread.sleep(1000);  
            return Thread.currentThread().getName() + "执行完任务：" + i;  
        }     
    }  
      
    public static void main(String[] args) throws InterruptedException, ExecutionException{  
        testExecutorCompletionService();  
    }  
      
    private static void testExecutorCompletionService() throws InterruptedException, ExecutionException{  
        int numThread = 5;  
//	//提交多个任务  那块先执行完 先取哪个
        ExecutorService threadPool2 =  Executors.newFixedThreadPool(numThread);
        CompletionService<String> completionService = new java.util.concurrent.ExecutorCompletionService<String>(threadPool2);
        for(int i = 0;i<numThread;i++ ){  
        	
            completionService.submit(new Task (i)) ;  
        }  
 
          
        for(int i = 0;i<numThread;i++ ){       
            System.out.println(completionService.take().get());  
        }  
          
    }  

    private static void testExecutorCompletionService2() throws InterruptedException, ExecutionException{  
//	//提交多个任务  那块先执行完 先取哪个
        ExecutorService threadPool2 =  Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new java.util.concurrent.ExecutorCompletionService<Integer>(threadPool2);
//        int numThread = 5;  
//        ExecutorService executor = Executors.newFixedThreadPool(numThread);  
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