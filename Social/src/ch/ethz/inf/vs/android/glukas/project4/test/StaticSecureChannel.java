package ch.ethz.inf.vs.android.glukas.project4.test;

import java.nio.ByteBuffer;
import android.os.Handler;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServer;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServerDelegate;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;

public class StaticSecureChannel implements AsyncServerDelegate {

	SecureChannelDelegate delegate;
	AsyncServer asyncServer;
	private Handler asyncNetworkHandler;
	private Runnable timeout;
	
	public StaticSecureChannel(String address, int port) {
		this.asyncServer = new AsyncServer(address, port, this);
		this.asyncNetworkHandler = new Handler();
		timeout = new Runnable(){
			@Override
			public void run(){
				Log.i("DEBUG", Data.tag+"Timeout");
			}
		};
	}
	
	public void sendHeader(PublicHeader header) {
		Log.i("DEBUG", Data.tag+"onSendHeader");
		asyncServer.sendMessage(header.getbytes());
		this.asyncNetworkHandler.postDelayed(timeout, 2000);
		Log.i("DEBUG", Data.tag+"onSendHeader return");
	}
	
	public void setDelegate(SecureChannelDelegate delegate) {
		Log.i("DEBUG", Data.tag+"onSetDelegate");
		this.delegate = delegate;
	}

	@Override
	public Handler getCallbackHandler() {
		Log.i("DEBUG", Data.tag+"ongetCallBackHandler");
		return this.asyncNetworkHandler;
	}

	@Override
	public void onReceive(byte[] message) {
		Log.i("DEBUG", Data.tag+"onReceive");
		ByteBuffer buf = ByteBuffer.allocate(message.length);
		buf.put(message);
		buf.rewind();
		PublicHeader header = new PublicHeader(buf);
		Log.i("DEBUG", Data.tag+"try create new network message");
		NetworkMessage netMessage = new NetworkMessage(new byte[0], header);
		delegate.onMessageReceived(netMessage);
		Log.i("DEBUG", Data.tag+"return onReceive");
	}

	@Override
	public void onSendFailed() {
		Log.i("DEBUG", Data.tag+"onSendFailed");
	}
}
