package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * This is the abstraction of a public header.
 * Defined in package-info
 */
public class PublicHeader {
	
	private UserId sender;
	private UserId receiver;
	private byte consistency;
	private int messageId;
	
	public PublicHeader(UserId sender, UserId receiver, byte consistency, int messageId) {
		this.sender = sender;
		this.receiver = receiver;
		this.consistency = consistency;
		this.messageId = messageId;
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
}
