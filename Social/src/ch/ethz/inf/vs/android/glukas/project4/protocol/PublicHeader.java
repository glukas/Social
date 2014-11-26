package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.nio.ByteBuffer;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * This is the abstraction of a public header. Defined in package-info
 */
public class PublicHeader {

	private UserId sender;
	private UserId receiver;
	private byte consistency;
	private int messageId;
	private byte[] future;
	private int length;
	
	/**
	 * Length, in bytes, of a header
	 */
	public static final int BYTES_LENGTH_HEADER = 44;
	
	/**
	 * Construct a new PublicHeader from explicit arguments
	 * 
	 * @param sender, UserId of sender
	 * @param receiver, UserId of receiver
	 * @param consistency, byte describing kind of message
	 * @param messageId, if it's a post, the id of the post
	 */
	public PublicHeader(int length, byte[] future, byte consistency, int messageId, UserId sender, UserId receiver) {
		this.length = length;
		if (future == null) {
			this.future = new byte[] { 0, 0, 0 };
		} else {
			this.future = future;
		}

		this.consistency = consistency;
		this.messageId = messageId;
		this.sender = sender;
		this.receiver = receiver;

	}

	/**
	 * Construct new PublicHeader from a byte array
	 * @param bytesHeader, byte array received from network
	 */
	public PublicHeader(ByteBuffer buf) {

		//declare values we will get from ByteBuffer
		int length;
		byte[] future = new byte[3];
		byte consistency;
		int messageId;

		//retrieve length of message, status byte and message id from ByteBuffer
		length = buf.getInt();
		buf.get(future, 0, 3);
		consistency = buf.get();
		messageId = buf.getInt();
		
		//retrieve id of sender
		byte[] senderId = new byte[UserId.LENGTH];
		buf.get(senderId, 0, UserId.LENGTH);
		
		//retrieve id of receiver
		byte[] receiverId = new byte[UserId.LENGTH];
		buf.get(receiverId, 0, UserId.LENGTH);

		//instantiate members
		this.length = length;
		this.future = future;
		this.consistency = consistency;
		this.messageId = messageId;
		this.sender = new UserId(senderId);
		this.receiver = new UserId(receiverId);
	}

	///
	//Getters
	///
	public UserId getSender() {
		return sender;
	}

	public UserId getReceiver() {
		return receiver;
	}

	public byte getConsistency() {
		return consistency;
	}

	public int getMessageId() {
		return messageId;
	}

	public byte[] getFuture() {
		return future;
	}

	public int getLength() {
		return length;
	}
	
	
	///
	//Setters
	///
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * Returns the header as byte array
	 */
	public byte[] getbytes() {
		
		ByteBuffer buf = ByteBuffer.allocate(PublicHeader.BYTES_LENGTH_HEADER);
		
		buf.putInt(length);
		buf.put(future);
		buf.put(consistency);
		buf.putInt(messageId);
		buf.put(sender.getBytes());
		buf.put(receiver.getBytes());
		return buf.array();
	}
}
