package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.os.Handler;

public interface AsyncServerDelegate {
	
	Handler getCallbackHandler();
	
	public void onReceive(byte[] message);
	
	public void onSendFailed();

}
