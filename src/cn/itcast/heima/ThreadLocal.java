package cn.itcast.heima;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocal {
	private static int data = 0;
	//�������̶߳���Ϊkeyֵ�� �൱Ҳ��jdk�Դ���TreadLocal  ������ �Զ������ݸ��̹߳ҹ�
    private static Map<Object,Integer> map = new HashMap<Object,Integer>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i=0;i<2;i++) {
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				data = new Random().nextInt();
				map.put(Thread.currentThread(),data );
				System.out.println(Thread.currentThread().getName()+"has put data:"+data);
			  new A().get();
			  new B().get();
			}
		}).start();
		}
	}

	static class A {
		public void get() {
			System.out.println("A :"+Thread.currentThread().getName()+"has get data:"+map.get(Thread.currentThread()));
		}
	}
	static class B {
		public void get() {
			System.out.println("B  :"+Thread.currentThread().getName()+"has get data:"+map.get(Thread.currentThread()));
		}
	}
}
