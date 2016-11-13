package cn.itcast.heima;

public class TraditionalTread {

	public static void main(String[] args) {
		Thread thread = new Thread() {

			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
		};
		thread.start();

		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName());
			}
		});
		thread1.start();

		new Thread() {
			public void run() {
				System.out.println(Thread.currentThread().getName());
			};
		}.start();

		
		new Thread(new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
		}) {
		}.start();
	}
}
