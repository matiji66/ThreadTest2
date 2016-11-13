package study.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author Administrator
 * 
 *         ConcurrentHashMap 读操作是不加锁的，写操作也是分segment加锁的
 *         在tiger之前，我们使用得最多的数据结构之一就是HashMap和Hashtable
 *         。大家都知道，HashMap中未进行同步考虑，而Hashtable则使用了synchronized
 *         ，带来的直接影响就是可选择，我们可以在单线程时使用. HashMap提高效率，而多线程时用Hashtable来保证安全。
 *         当我们享受着jdk带来的便利时同样承受它带来的不幸恶果
 *         。通过分析Hashtable就知道，synchronized是针对整张Hash表的，即每次锁住整张表让线程独占
 *         ConcurrentHashMap通常只被看做并发效率更高的Map
 *         ，用来替换其他线程安全的Map容器，比如Hashtable和Collections
 *         .synchronizedMap。实际上，线程安全的容器，
 *         特别是Map，应用场景没有想象中的多，很多情况下一个业务会涉及容器的多个操作，即复合操作
 *         ，并发执行时，线程安全的容器只能保证自身的数据不被破坏，但无法保证业务的行为是否正确。
 * 
 *         举个例子：统计文本中单词出现的次数，把单词出现的次数记录到一个Map中，代码如下：
 */
public class ConcurrentHashMapTest {

	static final String KEY = "a";

	public static void main(String[] args) throws InterruptedException {
		// for (int i = 0; i < 10; i++) {
		// System.out.println(test1());
		// }
		final ConcurrentHashMapTest concurrentHashMapTest = new ConcurrentHashMapTest();
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 8; i++) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 100; i++) {
						// concurrentHashMapTest.increase(KEY);
						try {
							concurrentHashMapTest.increase2(KEY);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println("----------word counts " + new
						// ConcurrentHashMapTest().increase2(KEY));
					}
				}
			});
		}
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("----------word counts  "
				+ concurrentHashMapTest.wordCounts.get(KEY));
		System.out.println("----------wordCountsAtomic  "
				+ concurrentHashMapTest.wordCountsAtomic.get(KEY));
	}

	// 除了用锁解决这个问题，另外一个选择是使用ConcurrentMap接口定义的方法：
	// 这是个被很多人忽略的接口，也经常见有人错误地使用这个接口。
	// ConcurrentMap接口定义了几个基于 CAS（Compare and Set）操作，很简单，但非常有用，下面的代码用
	// public interface ConcurrentMap<K, V> extends Map<K, V> {
	// V putIfAbsent(K key, V value);
	// boolean remove(Object key, Object value);
	// boolean replace(K key, V oldValue, V newValue);
	// V replace(K key, V value);
	// }

	/**
	 * version 1 for using ConcurrentHashMap
	 * 代码有点复杂，主要因为ConcurrentMap中不能保存value为null的值，所以得同时处理word不存在和已存在两种情况。
	 * 上面的实现每次调用都会涉及Long对象的拆箱和装箱操作，很明显，更好的实现方式是采用AtomicLong，
	 */
	private final ConcurrentMap<String, Long> wordCounts = new ConcurrentHashMap<String, Long>();

	public long increase(String word) {
		Long oldValue, newValue;
		while (true) {
			oldValue = wordCounts.get(word);
			if (oldValue == null) {
				// Add the word firstly, initial the value as 1
				newValue = 1L;
				if (wordCounts.putIfAbsent(word, newValue) == null) {
					break;
				}
			} else {
				newValue = oldValue + 1;
				if (wordCounts.replace(word, oldValue, newValue)) {
					break;
				}
			}
		}
		return newValue;
	}

	/**
	 * version 2 for using ConcurrentHashMap 下面是采用AtomicLong后的代码：
	 */
	private final ConcurrentMap<String, AtomicLong> wordCountsAtomic = new ConcurrentHashMap<String, AtomicLong>();

	public long increase2(String word) throws InterruptedException {
		AtomicLong number = wordCountsAtomic.get(word);
		if (number == null) {
			AtomicLong newNumber = new AtomicLong(0);
			number = wordCountsAtomic.putIfAbsent(word, newNumber);
			System.out.println("----------wordCountsAtomic  "
					+ wordCountsAtomic.get(KEY));

			if (number == null) {
				number = newNumber;
			}
		}
		System.out.println("----------number  " + number.incrementAndGet());

		return number.get();
	}

	
	class ExpensiveObj {

		String key;

		public ExpensiveObj(String key) {
			this.key = key;
		}
	}

	/**
	 * 避免操作中产生很多重复的冗余对象，而采用future 
	 */
	private final ConcurrentMap<String, Future<ExpensiveObj>> cacheWordCounts = new ConcurrentHashMap<String, Future<ExpensiveObj>>();

	public ExpensiveObj get(final String key) {
		Future<ExpensiveObj> future = cacheWordCounts.get(key);
		if (future == null) {
			Callable<ExpensiveObj> callable = new Callable<ExpensiveObj>() {
				@Override
				public ExpensiveObj call() throws Exception {
					return new ExpensiveObj(key);
				}
			};
			FutureTask<ExpensiveObj> task = new FutureTask<ExpensiveObj>(
					callable);
			// putifAbsent() // 这个方法是如果存在key的话，那么就返回key原来对应的值
			future = cacheWordCounts.putIfAbsent(key, task);
			if (future == null) {
				future = task;
				task.run();
			}
		}
		try {
			return future.get();
		} catch (Exception e) {
			cacheWordCounts.remove(key);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unused")
	private static int test1() throws InterruptedException {
		ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < 8; i++) {
			pool.execute(new MyTask(map));
		}
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);

		return map.get(MyTask.KEY);
	}

	static class MyTask implements Runnable {
		public static final String KEY = "key";
		private ConcurrentHashMap<String, Integer> map;

		public MyTask(ConcurrentHashMap<String, Integer> map) {
			this.map = map;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				this.addup();
			}
		}

		private void addup() {
			if (!map.containsKey(KEY)) {
				map.put(KEY, 1);
			} else {
				map.putIfAbsent(KEY, map.get(KEY) + 1);
				// map.put(KEY, map.get(KEY) + 1);
			}
		}
	}

}
