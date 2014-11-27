package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;
import android.test.AndroidTestCase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.MessageFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;

public class MessagesParsingTest extends AndroidTestCase {
	
	private final int dummyOldPostCount = 34;
	private final int dummyPostId = 56;
	private final int dummyNumMsg = 78;
	private final UserId dummySenderId = new UserId("11111");
	private final UserId dummyReceiverId = new UserId("-22222");
	private final BasicUser dummySender = new BasicUser(dummySenderId, "Dummy Sender");
	private final BasicUser dummyReceiver = new BasicUser(dummyReceiverId, "Dummy Receiver");
	private final DatabaseAccess db = new DatabaseManager(null);
	private final String tag = "###";
	
	@Test
	public void testGetPostMessage() {
		Message getPostMessage1 = MessageFactory.newGetPostsMessage(dummyOldPostCount, dummySender, dummyReceiver);
		String getPostMessageTxt = JSONObjectFactory.createJSONObject(getPostMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, dummySender.getId(), dummyReceiver.getId());
		
		Log.i("DEBUG", tag+" "+header.getSender());
		Message getPostMessage2 = MessageParser.parseMessage(getPostMessageTxt, header, db);
		boolean equals = getPostMessage1.toString().equals(getPostMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", tag+" "+getPostMessage1.toString());
			Log.d("After Parsing", tag+" "+getPostMessage2.toString());
		}
		assertTrue(equals);
	}
	
	@Test
	public void testSendStateMessage() {
		Message sendStateMessage1 = MessageFactory.newSendStateMessage(dummySender, dummyReceiver, dummyPostId, dummyNumMsg);
		String sendStateMessageTxt = JSONObjectFactory.createJSONObject(sendStateMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, dummySender.getId(), dummyReceiver.getId());
		Message sendStateMessage2 = MessageParser.parseMessage(sendStateMessageTxt, header, db);
		boolean equals = sendStateMessage1.toString().equals(sendStateMessage2.toString());
		assertTrue(equals);
		if (!equals){
			Log.d("Before Parsing", tag+" "+sendStateMessage1.toString());
			Log.d("After Parsing", tag+" "+sendStateMessage2.toString());
		}
		assertTrue(equals);
	}

	@Test
	public static void testGetStateMessage() {
		
	}
	
	@Test
	public static void testAckMessage() {
		
	}
	
	@Test
	public static void testPostMessage() {
		
	}
	
	@Test
	public static void testSendMessage() {
		
	}
}
