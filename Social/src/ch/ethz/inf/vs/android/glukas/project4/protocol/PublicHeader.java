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
	public PublicHeader(byte[] future, byte consistency, int messageId,
			UserId sender, UserId receiver) {
		
		if (future == null){
			this.future = new byte[]{0,0,0};
		}
		else {
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

		byte[] future = new byte[3];
		byte consistency;
		int messageId;
		byte[] senderId = new byte[16];
		byte[] receiverId = new byte[16];

		// wrap the byte array received
		// ByteBuffer buf = ByteBuffer.wrap(bytesHeader);

		// retrieve data from ByteBuffer
		buf.get(future, 0, 3);
		consistency = buf.get();
		messageId = buf.getInt();
		buf.get(senderId, 0, 16);
		buf.get(receiverId, 0, 16);

		// instantiate members
		this.future = future;
		this.consistency = consistency;
		this.messageId = messageId;
		this.sender = new UserId(senderId);
		this.receiver = new UserId(receiverId);

	}

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
		

		
		

		// Constructing the bytearray for messageId out of an integer
		// (Big-Endian)
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(this.getMessageId());
		byte[] messageId = b.array();

		// Fill first 3 bytes with 0 future bytes
		for (int i = 0; i < 3; i++) {
			data[i] = future[i];
		}
		// Adding the consistencybyte to the array
		data[3] = consistencybyte;

		// Filling the last 4 bytes of the data array with messageId Bytes
		for (int i = 4; i < 8; i++) {
			data[i] = messageId[i];
		}

		// Filling the first 16 bytes of the data array with the senderId Bytes
		for (int i = 8; i < 24; i++) {
			data[i] = senderId[i];
		}

		// Filling the next 16 bytes of the data array with the receiverId Bytes
		for (int i = 24; i < 40; i++) {
			data[i] = receiverId[i];
		}

		return data;
	}
}
