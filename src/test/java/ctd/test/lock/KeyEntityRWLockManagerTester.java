package ctd.test.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ctd.util.lock.KeyEntityRWLockManager;

public class KeyEntityRWLockManagerTester {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		final Map<String,Integer> data = new HashMap<String,Integer>();
		data.put("20", 900000000);
		int threads = 200;
		final CountDownLatch connectLatch = new CountDownLatch(threads);
		ExecutorService exec = Executors.newFixedThreadPool(threads);
		final KeyEntityRWLockManager manager = new KeyEntityRWLockManager();
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
					//System.out.println("thread[ " + num + " ]key=" + key);
					boolean locked = manager.tryReadLock(key);
					System.out.println("thread[ " + num + " ]key[" + key  + "] locked=" + locked);
					if(data.containsKey(key)){
						if(locked){
							manager.readUnlock(key);
						}
						System.out.println("thread[ " + num + " ]get data key[" + key + "]=" + data.get(key));
					}
					else{
						if(locked){
							manager.readUnlock(key);
							System.out.println("thread[ " + num + " ] rlock unlock.");
						}
						System.out.println("thread[ " + num + " ]wait for wlock key[" + key + "]");
						manager.writeLock(key);
						System.out.println("thread[ " + num + " ] get wlock and step into load data for key[" + key + "]");
						if(data.containsKey(key)){
							System.out.println("thread[ " + num + " ] key[" + key + "] hava data is " + data.get(key));
							manager.writeUnlock(key);
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
							manager.writeUnlock(key);
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
