package ctd.util.store;

import ctd.util.store.support.ZooKeeperActiveStore;

public class StoreFactory {
	public static ActiveStore createStore(String address){
		return new ZooKeeperActiveStore(address);
	}
}
