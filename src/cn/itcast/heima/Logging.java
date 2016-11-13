package cn.itcast.heima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import javafx.event.Event;

public class Logging {
 
		  private static final ConcurrentMap<String,Integer> queryCounts =
		    new ConcurrentHashMap<String,Integer>(1000);
		  static final ExecutorService service = Executors.newFixedThreadPool(4);
		  private static void incrementCount(String q) {
		    Integer oldVal, newVal;
		    do {
		      oldVal = queryCounts.get(q);
		      newVal = (oldVal == null) ? 1 : (oldVal + 1);
		    } while (newVal!=1&&!queryCounts.replace(q, oldVal, newVal));
		  }
		 
		  public static void main(String[] args) throws InterruptedException {
			final String string[] = "a b c d e f g h j k i l m n o p q r s t u v w x y z a b c d e f g h j k i l m n o p q r s t u v w x y z".split(" ");
			System.err.println(string);
			for(int i = 0;i < 4;i++){
//				service.execute(new  Runnable() {
//					public void run() {
						for(int j =0;j<50;j++){
							
							incrementCount(string[j]);
						}
//					}
//				});
				
			}
			Thread.sleep(3000);
			  
			 System.err.println(queryCounts);
		}
}
