package ctd.test.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockSingleThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Lock lock = new ReentrantLock(true);
		
		for(int i = 0;i < 10;i ++){
			
			lock.lock();
			System.out.println("inde");
			
		}
		
		lock.unlock();
	}

}
