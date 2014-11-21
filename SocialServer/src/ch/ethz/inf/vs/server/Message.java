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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

public class Message implements Comparable<Message>, Comparator<Message> {
	
	private int length;
	
	private byte[] var;
	
	private byte status;
	
	private int clock;
	
	private BigInteger sender;
	
	private BigInteger recipient;
	
	private byte[] message;
	
	private byte[] original;
	
	private long timestamp;
	

	public Message(byte[] bytes){
		this.timestamp = System.currentTimeMillis();
		this.original = bytes;
		
		this.length = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 0, 3)).getInt();
		this.var = Arrays.copyOfRange(bytes, 4, 6);
		this.status = bytes[7];
		this.clock = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 8, 11)).getInt();
		
		this.sender = new BigInteger(Arrays.copyOfRange(bytes, 12, 27));
		this.recipient = new BigInteger(Arrays.copyOfRange(bytes, 28, 43));
		
		this.message = Arrays.copyOfRange(bytes, 44, length-1);
	}
	
	//Used to create a dummy message, which only has a clock value
	public Message(int clock){
		this.timestamp = System.currentTimeMillis();
		this.clock = clock;
		//Dummy data
		this.length = -1;
		this.var = new byte[]{0x00, 0x00, 0x00};
		this.status = 0x00;
		this.sender = BigInteger.ZERO;
		this.recipient = BigInteger.ZERO;
		this.message = new byte[]{0x00};
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public byte[] getVar() {
		return var;
	}


	public void setVar(byte[] var) {
		this.var = var;
	}


	public byte getStatus() {
		return status;
	}


	public void setStatus(byte status) {
		this.status = status;
	}


	public int getClock() {
		return clock;
	}


	public void setClock(int clock) {
		this.clock = clock;
	}


	public BigInteger getSender() {
		return sender;
	}


	public void setSender(BigInteger sender) {
		this.sender = sender;
	}


	public BigInteger getRecipient() {
		return recipient;
	}


	public void setRecipient(BigInteger recipient) {
		this.recipient = recipient;
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
		if(this.clock > o.getClock())
			return 1;
		else if(this.clock < o.getClock())
			return -1;
		else
			return 0;
	}

	@Override
	public int compare(Message o1, Message o2) {
		if(o1.getClock() > o2.getClock())
			return 1;
		else if(o1.getClock() < o2.getClock())
			return -1;
		else
			return 0;
	}
}
