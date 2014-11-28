package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.io.IOException;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class AsyncServer {
	
	private Handler requestHandler;
	private HandlerThread requestThread;
	private Thread receiveThread;
	private final AsyncServerDelegate delegate;

	private boolean running;
	
	private TCPCommunicator comm;
	
	private volatile boolean noConnection;
	private final String address;
	private final int port;
	

	public AsyncServer(String address, int port, AsyncServerDelegate delegate) {
		this.address = address;
		this.port = port;
		this.delegate = delegate;
		
		//Start the handlers
		setRequestHandling();
		
		//Start communicator
		requestHandler.post(new Runnable(){
			@Override public void run(){
				start();
			}
		});
		
		setReceiveHandling();
		//start();
	}
	
	private void start() {
		//Setup server
		running = true;
		noConnection = false;
		this.comm = new TCPCommunicator(address, port);
		
	}
	
	private void setRequestHandling(){
		requestThread = new HandlerThread("requestThread");
		requestThread.start();
		requestHandler = new Handler(requestThread.getLooper());
	}
	
	
	private void setReceiveHandling(){
		receiveThread = new Thread() {
			@Override
			public void run() {
				try {
					while(running){
						byte[] message = comm.receiveMessage();
						postReceivedToDelegate(message);
					}
				} catch (IOException e) {
					if (running){
						Log.e(this.getClass().toString(), e.getLocalizedMessage());
					}
				}
			}
		};
		receiveThread.start();
	}
	
	private void postReceivedToDelegate(final byte[] message) {		
		Log.i("DEBUG", "###"+"new message");
		delegate.getCallbackHandler().post(new Runnable() {
			@Override
			public void run() {
				delegate.onReceive(message);
			}
		});
		Log.i("DEBUG", "###"+"post returned");
		
	//	delegate.onReceive(message);
	}
	
	public void close() {
		//Log.d(this.getClass().toString(), "close()");
		this.running = false;
		try {
			comm.finishConnection();
		} catch (IOException ex) {
			Log.e(this.getClass().toString(), ex.getLocalizedMessage());
		}
		requestThread.quit();
		noConnection = true;
	}

	public void sendMessage(final byte[] message) {
		if (noConnection){
			start();
		}
		requestHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					comm.sendMessage(message);
				} catch (IOException e) {
					Log.e("Error delivering : " +this.getClass().toString(), e.getLocalizedMessage());
					close();
					delegate.getCallbackHandler().post(new Runnable() {
						@Override
						public void run() {
							delegate.onSendFailed();
						}
					});
				}
			}
		});
	}

}
