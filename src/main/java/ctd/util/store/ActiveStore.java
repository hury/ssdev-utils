package ctd.util.store;

import java.util.List;

import ctd.util.store.StoreException;
import ctd.util.store.listener.NodeListener;
import ctd.util.store.listener.StateListener;

public interface ActiveStore {
	public void connect();
	public void connectingAwait();
	public boolean isConnected();
	public void close();
	public String getServerAddress();
	public void addStateListener(StateListener listener);
	public void delete(String path) throws StoreException;
	public boolean isPathExist(String path) throws StoreException;
	public boolean isPathExist(String path,NodeListener listener) throws StoreException;
	public List<String> getChildren(String path) throws StoreException;
	public List<String> getChildren(String path,NodeListener listener) throws StoreException;
	public void setData(String path,byte[] data) throws StoreException;
	public byte[] getData(String path) throws StoreException;
	public byte[] getData(String path,NodeListener listener) throws StoreException;
	public void createTempPath(String path,byte[] data) throws StoreException;
	public String createSeqTempPath(String path,byte[] data) throws StoreException;
	public void createPath(String path,byte[] data) throws StoreException;
	public String createSeqPath(String path, byte[] data) throws StoreException;
	
}
