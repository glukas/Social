package ch.ethz.inf.vs.android.glukas.project4.test;

import java.nio.ByteBuffer;
import org.junit.Test;
import android.test.AndroidTestCase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;


public class PublicHeaderTest extends AndroidTestCase {
	
	@Test
	public static void testPublicHeader(){
		
		int testlength1 = 4 ; 
		int testmessageId1= 16;
		byte[] testfuture1 = new byte[]{1,2,3};
		byte testconsistency1 = 1;
		UserId testsenderId1 = new UserId("25534563456");
		UserId testreceiverId1 = new UserId("-2352332");
		
		PublicHeader testheader1= new PublicHeader (testlength1, testfuture1,testconsistency1 , testmessageId1, testsenderId1, testreceiverId1);
		Log.d("Length: ", "###"+String.valueOf(testheader1.getLength()));
		Log.d("MessageId: ", "###"+String.valueOf(testheader1.getMessageId()));
		Log.d("FutureArray: ", "###"+String.valueOf(testheader1.getFuture()));
		Log.d("StatusByte: ", "###"+String.valueOf(testheader1.getConsistency()));
		Log.d("SenderId: ", "###"+String.valueOf(testheader1.getSender()));
		Log.d("ReceiverId: ", "###"+String.valueOf(testheader1.getReceiver()));
		
		
		byte[] testarray = testheader1.getbytes();
		ByteBuffer testbuf1 = ByteBuffer.wrap(testarray);
		PublicHeader testbufheader1 = new PublicHeader(testbuf1);
	/*	
		assertEquals("Is Length the same? : ", testheader1.getLength(), testbufheader1.getLength());
		assertEquals("Is MessageId the same? : ", testheader1.getMessageId(), testbufheader1.getMessageId());
		assertEquals("Is FutureArray the same? : ", testheader1.getFuture(), testbufheader1.getFuture());
		assertEquals("Is StatusByte the same? : ", testheader1.getConsistency(), testbufheader1.getConsistency());
		assertEquals("Is SenderId the same? : ", testheader1.getSender(), testbufheader1.getSender());
		assertEquals("Is ReceiverId same? : ", testheader1.getReceiver(), testbufheader1.getReceiver());
		*/
		Log.d("Length: ", "###"+String.valueOf(testbufheader1.getLength()));
		Log.d("MessageId: ", "###"+String.valueOf(testbufheader1.getMessageId()));
		Log.d("FutureArray: ", "###"+String.valueOf(testbufheader1.getFuture()));
		Log.d("StatusByte: ", "###"+String.valueOf(testbufheader1.getConsistency()));
		Log.d("SenderId: ", "###"+String.valueOf(testbufheader1.getSender()));
		Log.d("ReceiverId: ", "###"+String.valueOf(testbufheader1.getReceiver()));
		
		
	}
}
