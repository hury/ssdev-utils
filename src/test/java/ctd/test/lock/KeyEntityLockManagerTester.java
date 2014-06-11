package ctd.test.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ctd.util.lock.KeyEntityLockManager;

public class KeyEntityLockManagerTester {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		final Map<String,Integer> data = new HashMap<String,Integer>();
		int threads = 300;
		final CountDownLatch connectLatch = new CountDownLatch(threads);
		ExecutorService exec = Executors.newFixedThreadPool(threads);
		final KeyEntityLockManager manager = new KeyEntityLockManager();
		final Random rd = new Random();
		
		for(int i = 0; i < threads; i ++){
			final int num = i;
			exec.submit(new Runnable() {
				@Override
				public void run() {
					
					String key = "10";
					if(num % 2 == 0){
						key= "20";
					}
					System.out.println("thread[ " + num + " ]key=" + key);
					boolean locked = manager.tryLock(key);
					System.out.println("thread[ " + num + " ]key[" + key  + "] locked=" + locked);
					if(data.containsKey(key)){
						if(locked){
							manager.unlock(key);
						}
						System.out.println("thread[ " + num + " ]get data key[" + key + "]=" + data.get(key));
					}
					else{
						if(!locked){
							System.out.println("thread[ " + num + " ]wait for lock key[" + key + "]");
							manager.lock(key);
						}
						System.out.println("thread[ " + num + " ] step into load data for key[" + key + "]");
						if(data.containsKey(key)){
							System.out.println("thread[ " + num + " ] key[" + key + "] hava data is " + data.get(key));
							manager.unlock(key);
						}
						else{
							try {
								TimeUnit.SECONDS.sleep(2);
							} 
							catch (InterruptedException e) {
								e.printStackTrace();
							}
							Integer value = rd.nextInt();
							data.put(key, value);
							System.out.println("thread[ " + num + " ] set**** data key[" + key + "]=" + value);
							manager.unlock(key);
						}
					}
					connectLatch.countDown();
					System.out.println("----[" + num + "]connectLatch=" + connectLatch.getCount());
				}
			});
		}
		try {
			
			connectLatch.await();
			System.out.println("manageLockCount=" + manager.getLockCount());
			exec.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
