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
		
		restart();
	}
	
	private void restart() {
		//Start the handlers
		setRequestHandling();
		
		//Start communicator, do this on separate thread because it's network task
		requestHandler.post(new Runnable(){
			@Override public void run(){
				start();
				setReceiveHandling();
			}
		});
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
				} catch (Exception e) {
					running = false;
					if (running){
						e.printStackTrace();
						Log.e(this.getClass().toString(), "exception + " + e.getMessage());
					}
				}
			}
		};
		receiveThread.start();
	}
	
	private void postReceivedToDelegate(final byte[] message) {		
		Log.i(this.getClass().toString(), "postReceivedToDelegate");
		delegate.getCallbackHandler().post(new Runnable() {
			@Override
			public void run() {
				delegate.onReceive(message);
			}
		});
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
			restart();
		}
		requestHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					comm.sendMessage(message);
					Log.d(this.getClass().toString(), "sentMessage");
					delegate.getCallbackHandler().post(new Runnable() {
						@Override
						public void run() {
							delegate.onSendSucceeded(message);
						}
					});
				} catch (Exception e) {
					Log.e("Error delivering : " +this.getClass().toString(), "in SendMessage: "+e.getLocalizedMessage());
					close();
					delegate.getCallbackHandler().post(new Runnable() {
						@Override
						public void run() {
							delegate.onSendFailed(message);
						}
					});
				}
			}
		});
	}

}
