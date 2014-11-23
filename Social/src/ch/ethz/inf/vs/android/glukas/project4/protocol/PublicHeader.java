package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.nio.ByteBuffer;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * This is the abstraction of a public header.
 * Defined in package-info
 */
public class PublicHeader {
	
	private UserId sender;
	private UserId receiver;
	private byte consistency;
	private int messageId;
	
	/**
	 * Construct a new PublicHeader from explicit arguments
	 * @param sender, UserId of sender
	 * @param receiver, UserId of receiver
	 * @param consistency, byte describing kind of message
	 * @param messageId, if it's a post, the id of the post
	 */
	public PublicHeader(UserId sender, UserId receiver, byte consistency, int messageId) {
		this.sender = sender;
		this.receiver = receiver;
		this.consistency = consistency;
		this.messageId = messageId;
	}
	
	/**
	 * Construct new PublicHeader from a byte array
	 * @param bytesHeader, byte array received from network
	 */
	public PublicHeader(byte[] bytesHeader) {
		
		byte[] senderId = new byte[16];
		byte[] receiverId = new byte[16];
		
		//wrap the byte array received
		ByteBuffer buf = ByteBuffer.wrap(bytesHeader);
		
		//retrieve bytes from byte array
		buf.get(senderId, 0, 16);
		buf.get(receiverId, 0, 16);
		
		//instantiate members
		sender = new UserId(senderId);
		receiver = new UserId(receiverId);
		consistency = buf.get();
		messageId = buf.getInt();
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
	
	/**
	 * Returns the header as byte array
	 */
	public byte[] getbytes() {
		
		byte[] data = new byte[37];
		byte[] senderId = this.getSender().getId().toByteArray();
		byte[] receiverId = this.getReceiver().getId().toByteArray();
		byte consistencybyte = this.getConsistency();
		
		//Constructing the bytearray for messageId out of an integer (Big-Endian)
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(this.getMessageId());
		byte[] messageId = b.array();
		
		// Filling the first 16 bytes of the data array with the senderId Bytes
		for (int i = 0 ; i < 16 ; i++) {
			data[i]= senderId[i];
		}
		
		// Filling the next 16 bytes of the data array with the receiverId Bytes
		for (int i = 16 ; i < 32; i++ ) {
			data[i]= receiverId[i];	
		}
		// Adding the consistencybyte to the array
		data[32] = consistencybyte;
		
		//Filling the last 4 bytes of the data array with messageId Bytes
		for(int i = 33 ; i < 37  ; i++) {
			data[i] = messageId[i];
		}
		
		return data;
	}	
}
