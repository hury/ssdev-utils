package ctd.util.store.listener;

public interface StateListener {
	public void onConnected();
	public void onExpired();
	public void onDisconnected();
}
