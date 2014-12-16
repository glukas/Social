package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;
import android.test.AndroidTestCase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.MessageFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;

public class MessagesParsingTest extends AndroidTestCase {
	
	@Test
	public void testGetPostMessage() {
		Message getPostMessage1 = MessageFactory.newGetPostsMessage(Data.dummyOldPostCount, Data.dummySender.getId(), Data.dummyReceiver.getId());
		String getPostMessageTxt = JSONObjectFactory.createJSONObject(getPostMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		Message getPostMessage2 = MessageParser.parseMessage(getPostMessageTxt, header);
		boolean equals = getPostMessage1.toString().equals(getPostMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+getPostMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+getPostMessage2.toString());
		}
		assertTrue(equals);
	}
	
	@Test
	public void testSendStateMessage() {
		Message sendStateMessage1 = MessageFactory.newSendStateMessage(Data.dummySender.getId(), Data.dummyReceiver.getId(), Data.dummyPostId, Data.dummyNumMsg);
		String sendStateMessageTxt = JSONObjectFactory.createJSONObject(sendStateMessage1, sendStateMessage1.getNumM()).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		
		//Log.i("DEBUG",Data.tag+sendStateMessageTxt);
		Message sendStateMessage2 = MessageParser.parseMessage(sendStateMessageTxt, header);
		boolean equals = sendStateMessage1.toString().equals(sendStateMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+sendStateMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+sendStateMessage2.toString());
		}
		assertTrue(equals);
	}

	@Test
	public static void testGetStateMessage() {
		Message getStateMessage1 = MessageFactory.newGetStateMessage(Data.dummySender.getId(), Data.dummyReceiver.getId());
		String getStateMessageTxt = JSONObjectFactory.createJSONObject(getStateMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		
		//Log.i("DEBUG",Data.tag+getStateMessageTxt);
		Message sendStateMessage2 = MessageParser.parseMessage(getStateMessageTxt, header);
		boolean equals = getStateMessage1.toString().equals(sendStateMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+getStateMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+sendStateMessage2.toString());
		}
		assertTrue(equals);
		
	}
	
	@Test
	public static void testAckMessage() {
		Message getStateMessage1 = MessageFactory.newAckMessage(Data.dummySender.getId(), Data.dummyReceiver.getId());
		String getStateMessageTxt = JSONObjectFactory.createJSONObject(getStateMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		
		//Log.i("DEBUG",Data.tag+getStateMessageTxt);
		Message sendStateMessage2 = MessageParser.parseMessage(getStateMessageTxt, header);
		boolean equals = getStateMessage1.toString().equals(sendStateMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+getStateMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+sendStateMessage2.toString());
		}
		assertTrue(equals);
	}
	
	@Test
	public static void testPostMessage() {
		Post post = new Post(67, Data.dummySenderId, Data.dummyReceiverId, "I'm a post!", null, null);
		Message getPostMessage1 = MessageFactory.newPostMessage(post, false);
		String getPostMessageTxt = JSONObjectFactory.createJSONObject(getPostMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		
		//Log.i("DEBUG",Data.tag+getPostMessageTxt);
		Message sendPostMessage2 = MessageParser.parseMessage(getPostMessageTxt, header);
		boolean equals = getPostMessage1.toString().equals(sendPostMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+getPostMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+sendPostMessage2.toString());
		}
		assertTrue(equals);
		
	}
	
	@Test
	public static void testSendMessage() {
		Post post = new Post(67, Data.dummySenderId, Data.dummyReceiverId, "I'm a post!", null, null);
		Message getPostMessage1 = MessageFactory.newPostMessage(post, true);
		String getPostMessageTxt = JSONObjectFactory.createJSONObject(getPostMessage1).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, Data.dummySender.getId(), Data.dummyReceiver.getId());
		
		//Log.i("DEBUG",Data.tag+getPostMessageTxt);
		Message sendPostMessage2 = MessageParser.parseMessage(getPostMessageTxt, header);
		boolean equals = getPostMessage1.toString().equals(sendPostMessage2.toString());
		if (!equals){
			Log.d("Before Parsing", Data.tag+" "+getPostMessage1.toString());
			Log.d("After Parsing", Data.tag+" "+sendPostMessage2.toString());
		}
		assertTrue(equals);
	}
}
