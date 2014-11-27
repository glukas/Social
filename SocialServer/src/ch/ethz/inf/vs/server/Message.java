/**
 * Public Headers:
 * 
 * Messages have the following public header:
 * 
 * -length of the message in bytes, including this header [4 bytes]
 * -for future use [3 bytes]
 * -status byte [1 byte]
 * -message id / "virtual clock" [4 bytes]
 * -sender [16 bytes]
 * -recipient [16 bytes]
 **/
package ch.ethz.inf.vs.server;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class Message implements Comparable<Message>, Comparator<Message> {
	
	private PublicHeader header;
	
	private byte[] message = null;
	
	private byte[] original;
	
	private long timestamp;
	
	public boolean isEmpty = true;
	

	public Message(byte[] bytes){
		this.timestamp = System.currentTimeMillis();
		this.original = bytes;
		
		this.header = new PublicHeader(ByteBuffer.wrap(bytes));
		
		//Leave message null if empty message
		if(PublicHeader.BYTES_LENGTH_HEADER < header.getLength()){
			this.message = Arrays.copyOfRange(bytes, PublicHeader.BYTES_LENGTH_HEADER, header.getLength());
			isEmpty = false;
		}
	}
	
	//Used to create a dummy message, which only has a clock value
	public Message(int messageId){
		this.timestamp = System.currentTimeMillis();
		byte b = 0;
		this.header = new PublicHeader(0, null, b, messageId, new UserId("0"), new UserId("0"));
	}

	public PublicHeader getHeader(){
		return header;
	}


	public byte[] getMessage() {
		return message;
	}


	public void setMessage(byte[] message) {
		this.message = message;
	}


	public byte[] getOriginal() {
		return original;
	}


	public void setOriginal(byte[] original) {
		this.original = original;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	


	@Override
	public int compareTo(Message o) {
		if(this.header.getMessageId() > o.getHeader().getMessageId())
			return 1;
		else if(this.header.getMessageId() < o.getHeader().getMessageId())
			return -1;
		else
			return 0;
	}

	@Override
	public int compare(Message o1, Message o2) {
		if(o1.header.getMessageId() > o2.header.getMessageId())
			return 1;
		else if(o1.header.getMessageId() < o2.header.getMessageId())
			return -1;
		else
			return 0;
	}
}
