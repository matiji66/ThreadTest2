package cn.itcast.heima;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("boming"+ Thread.currentThread().getName());
			}
			//2秒后爆炸
		}, 2000);
		
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("boming"+ Thread.currentThread().getName());
			}
			//2秒后爆炸,以后每3秒后炸
		}, 2000,3000);
	}
}
