package ctd.test.context;

import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class ContextTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ContextUtils.put("test", "success");
		final Context ctx =  ContextUtils.getContext();
		Thread t = new Thread(new Runnable() {
			public void run() {
				ContextUtils.setContext(ctx);
				System.out.println("test=" + ContextUtils.get("test"));
			}
		});
		t.start();
	}

}
