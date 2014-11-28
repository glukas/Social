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


public class ConnectDisconnectTest extends AndroidTestCase implements SecureChannelDelegate {

	Protocol protocol;
	StaticSecureChannel channel;
	DatabaseAccess db;
	boolean isSet = false;
	boolean wait = true;
	
	public synchronized void setSettings() {
		//initialize objects if not already done
		Log.i("DEBUG", Data.tag+"enter setSettings");
		if (!isSet) {
			db = new StaticDatabase();
			Log.i("DEBUG", Data.tag+"enter1");
			protocol = Protocol.getInstance(db);
			Log.i("DEBUG", Data.tag+"enter2");
			channel = new StaticSecureChannel("winti.mooo.com", 9000);
			Log.i("DEBUG", Data.tag+"enter3");
			channel.setDelegate(this);
			Log.i("DEBUG", Data.tag+"enter4");
			isSet = true;
		}
	}
	
	@Test
	public void testConnect() {
		setSettings();
		Log.i("DEBUG", Data.tag+"setting set");
		PublicHeader header = new PublicHeader(44, null, StatusByte.CONNECT.getByte(), 0, Data.dummySenderId, Data.serverId);
		
		Log.i("DEBUG", Data.tag+"header created");

		channel.sendHeader(header);
		while(wait)
			Thread.yield();
	}
	
	@Test
	public void testDisconnect() {
		//setSettings();
	}

	@Override
	public void onMessageReceived(NetworkMessage message) {
		Log.d("DEBUG", Data.tag+"Message received : "+MessageParser.parseMessage(message.text, message.header, db).toString());
		wait = false;
	}
	
}
