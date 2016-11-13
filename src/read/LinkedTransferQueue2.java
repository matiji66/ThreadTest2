package read;

 
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;


public class LinkedTransferQueue2 {
	public static void main(String[] args) {
		final TransferQueue<String>  queue = new LinkedTransferQueue<String>();
		
		 
		for(int i=0;i<1;i++){
			new Thread(){
				public void run(){
					while(true){
						try {
							Thread.sleep((long)(Math.random()*1000));
							System.out.println(Thread.currentThread().getName() + "准备放数据!");							
//							queue.transfer("1");
							queue.add("1");
							System.out.println(Thread.currentThread().getName() + "已经放了数据，" + 							
										"队列目前有" + queue.size() + "个数据");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
				
			}.start();
		}
	
		new Thread(){
			public void run(){
				while(true){
					try {
						//将此处的睡眠时间分别改为100和1000，观察运行结果
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName() + "准备取数据!");
						String reString = queue.peek();
						System.out.println(Thread.currentThread().getName() + "已经取走数据，" +reString+ 							
								"队列目前有" + queue.size() + "个数据");					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();			

		
	
		
		
	}

}
