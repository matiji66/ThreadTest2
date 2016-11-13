package cn.itcast.heima;

public class Synchronization {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Synchronization().init();
	}

	public void init() {
		final Outer outer = new Outer();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					outer.print("huangdengfeng");
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					outer.print("huangjingjing");
				}
			}
		}).start();
	}

	// 内部类 最大的特点就是可以访问外部类的变量，所以确定在是用内部类的时候一定有外部类的实例创建
	class Outer {
        //synchronized 要锁在公用的上
		public synchronized void print(String name) {
			for (int i = 0; i < name.length(); i++) {
				System.out.print(name.charAt(i));
			}
			// 换行
			System.out.println();
		}
	}
}
