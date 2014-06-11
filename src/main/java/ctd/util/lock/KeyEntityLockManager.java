package ctd.util.lock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class KeyEntityLockManager{
	private ConcurrentHashMap<String,KeyEntityLock> locks = new ConcurrentHashMap<String, KeyEntityLock>();
	private boolean fair = false;
	
	public KeyEntityLockManager(){
		
	};
	
	public KeyEntityLockManager(boolean fair){
		this.fair = fair;
	}
	
	public int getLockCount(){
		return locks.size();
	}
	
	public boolean tryLock(String key){
		if(locks.containsKey(key)){
			KeyEntityLock lock = locks.get(key);
			lock.lock();
			return true;
		}
		else{
			return false;
		}
	}
	
	public void lock(String key){
		KeyEntityLock lock = null;
		if(locks.containsKey(key)){
			lock = locks.get(key);
		}
		else{
			KeyEntityLock newLock = new KeyEntityLock(key,fair);
			newLock.setManager(this);
			lock = locks.putIfAbsent(key, newLock);
			if(lock == null){
				lock = newLock;
			}
		}
		lock.lock();
	}
	
	public void unlock(String key){
		if(locks.containsKey(key)){
			KeyEntityLock lock = locks.get(key);
			lock.unlock();
		}
	}
	
	protected void onIdle(String key) {
		locks.remove(key);
	}
	
	
	
}

class KeyEntityLock{
	private String key;
	private Lock lock;
	private AtomicInteger threadCount = new AtomicInteger(0);
	private KeyEntityLockManager manager; 
	
	public KeyEntityLock(String key,boolean fair){
		this.key = key;
		lock = new ReentrantLock(fair);
	}
	
	private void increaseThreads(){
		threadCount.incrementAndGet();
	}
	
	private void decreaseThreads(){
		int value = threadCount.decrementAndGet();
		if(value <= 0){
			if(manager != null){
				manager.onIdle(key);
				manager = null;
			}
		}
	}
	
	public void lock(){
		increaseThreads();
		lock.lock();
	}
	
	public void unlock(){
		lock.unlock();
		decreaseThreads();
	}
	
	public boolean lock(long time,TimeUnit unit){
		increaseThreads();
		try{
			boolean isLocked = lock.tryLock(time, unit);
			if(!isLocked){
				decreaseThreads();
			}
			return isLocked;
		}
		catch(InterruptedException e){
			decreaseThreads();
			return false;
		}
	}

	public void setManager(KeyEntityLockManager manager) {
		this.manager = manager;
	}
	
}
