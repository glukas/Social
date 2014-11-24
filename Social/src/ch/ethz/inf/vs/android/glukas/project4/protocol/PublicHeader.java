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
	 * Construct a new PublicHeader from explicit arguments
	 * 
	 * @param sender
	 *            , UserId of sender
	 * @param receiver
	 *            , UserId of receiver
	 * @param consistency
	 *            , byte describing kind of message
	 * @param messageId
	 *            , if it's a post, the id of the post
	 */
	public PublicHeader(int length, byte[] future, byte consistency,
			int messageId, UserId sender, UserId receiver) {
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
	 * 
	 * @param bytesHeader
	 *            , byte array received from network
	 */
	public PublicHeader(ByteBuffer buf) {

		int length;
		byte[] future = new byte[3];
		byte consistency;
		int messageId;
		byte[] senderId = new byte[16];
		byte[] receiverId = new byte[16];

		// wrap the byte array received
		// ByteBuffer buf = ByteBuffer.wrap(bytesHeader);

		// retrieve data from ByteBuffer
		length = buf.getInt();
		buf.get(future, 0, 3);
		consistency = buf.get();
		messageId = buf.getInt();
		buf.get(senderId, 0, 16);
		buf.get(receiverId, 0, 16);

		// instantiate members
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
	public void setLength() {
		this.length = length;
	}
	/**
	 * Returns the header as byte array
	 */
	public byte[] getbytes() {
		byte[] data = new byte[40];

		// future bytes
		byte[] future = this.getFuture();

		byte consistencybyte = this.getConsistency();
		byte[] senderId = this.getSender().getId().toByteArray();
		byte[] receiverId = this.getReceiver().getId().toByteArray();

		
		// Constructing the bytearray for length out of an integer
		// (Big-Endian)
		
		ByteBuffer blength = ByteBuffer.allocate(4);
		blength.putInt(this.getLength());
		byte[] length = blength.array();

		// Constructing the bytearray for messageId out of an integer
		// (Big-Endian)
		ByteBuffer bmessage = ByteBuffer.allocate(4);
		bmessage.putInt(this.getMessageId());
		byte[] messageId = bmessage.array();

		// Fill the first 4 bytes with length integer
		for (int i = 0; i < 4; i++) {
			data[i] = length[i];
		}

		// Fill the next 3 bytes with 0 future bytes

		for (int i = 4; i < 7; i++) {
			data[i] = future[i];
		}
		// Adding the consistencybyte to the array
		data[7] = consistencybyte;

		// Filling the last 4 bytes of the data array with messageId Bytes
		for (int i = 8; i < 12; i++) {
			data[i] = messageId[i];
		}

		// Filling the first 16 bytes of the data array with the senderId Bytes
		for (int i = 12; i < 28; i++) {
			data[i] = senderId[i];
		}

		// Filling the next 16 bytes of the data array with the receiverId Bytes
		for (int i = 28; i < 44; i++) {
			data[i] = receiverId[i];
		}

		return data;
	}
}
