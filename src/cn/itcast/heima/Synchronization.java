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

	// �ڲ��� �����ص���ǿ��Է����ⲿ��ı���������ȷ���������ڲ����ʱ��һ�����ⲿ���ʵ������
	class Outer {
        //synchronized Ҫ���ڹ��õ���
		public synchronized void print(String name) {
			for (int i = 0; i < name.length(); i++) {
				System.out.print(name.charAt(i));
			}
			// ����
			System.out.println();
		}
	}
}
