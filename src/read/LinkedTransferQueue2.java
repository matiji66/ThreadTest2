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
							System.out.println(Thread.currentThread().getName() + "׼��������!");							
//							queue.transfer("1");
							queue.add("1");
							System.out.println(Thread.currentThread().getName() + "�Ѿ��������ݣ�" + 							
										"����Ŀǰ��" + queue.size() + "������");
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
						//���˴���˯��ʱ��ֱ��Ϊ100��1000���۲����н��
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName() + "׼��ȡ����!");
						String reString = queue.peek();
						System.out.println(Thread.currentThread().getName() + "�Ѿ�ȡ�����ݣ�" +reString+ 							
								"����Ŀǰ��" + queue.size() + "������");					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();			

		
	
		
		
	}

}
