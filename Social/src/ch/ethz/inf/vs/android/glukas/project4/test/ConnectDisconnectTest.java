package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;
import android.test.AndroidTestCase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;


public class ConnectDisconnectTest extends AndroidTestCase {
	
	@Test
	public void testConnect() {
		NetworkingThread thread = new NetworkingThread();
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.i("DEBUG", Data.tag+"THREAD INTERRUPTED");
		}
		Log.i("DEBUG", Data.tag+"Finish test connect");
	}
	
	@Test
	public void testDisconnect() {
		//setSettings();
	}
	
	private class NetworkingThread extends Thread implements SecureChannelDelegate {
		
		private volatile boolean alive;
		Protocol protocol;
		StaticSecureChannel channel;
		DatabaseAccess db;
		boolean wait;
		
		public NetworkingThread(){
			db = new StaticDatabase();
			protocol = Protocol.getInstance(db);
			channel.setDelegate(this);
			alive = true;
			wait = true;
		}
		
		@Override
		public void run() {
			while (alive) {
				sendConnect();
				while(wait){
					Thread.yield();
				}
			}
		}
		
		private void sendConnect(){
			PublicHeader header = new PublicHeader(44, null, StatusByte.CONNECT.getByte(), 0, Data.dummySenderId, Data.serverId);
			channel.sendHeader(header);
			Log.i("DEBUG", Data.tag+"header send");
		}

		@Override
		public void onMessageReceived(NetworkMessage message) {
			Log.d("DEBUG", Data.tag+"Message received : "+MessageParser.parseMessage(message.text, message.header, db).toString());
			wait = false;
			alive = false;
		}
	}
	
}
