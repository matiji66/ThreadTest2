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
		// ���������߳�   һ���̳߳�
		//ExecutorService threadPool = Executors.newFixedThreadPool(3);
		//�Զ������̵߳ĸ���
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for ( int j = 0; j < 10;j++) {
			final int task = j;
			//������������߳�  execute �ǲ�Ҫ����ֵ�ģ�submit�ǿ���ȡ�÷���ֵ��
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
		//������ɵ��̳߳�
		threadPool.shutdown();
		//�����ɵ��߳�
		//threadPool.shutdownNow();
		
		
		
		//��ʱ�̳߳أ��������ִ��
		Executors.newScheduledThreadPool(3).schedule(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			System.out.println("boming");	
			}
		}, 3, TimeUnit.SECONDS);
		
		//��ʱ�̳߳أ��������ִ�У�ִ�к�ÿ����������ִ��
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("boming");	
			}
		}, 3,2, TimeUnit.SECONDS);
	}

}
