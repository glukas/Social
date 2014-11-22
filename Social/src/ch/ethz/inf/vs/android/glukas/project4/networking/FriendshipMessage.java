package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Log;

public abstract class FriendshipMessage {

	/**
	 * @return the payload that was sent in the message passed to this object at construction
	 */
	public String getApplicationPayload() {
		return parsedMessage.applicationTextPayload;
	}
	
	/**
	 * @param payload the payload that should be sent when a NdefMessage is instantiated in order to request/respond
	 * 		  this is something like the profile information of the owner of the phone executing this code
	 */
	public void setResponseApplicationPayload(String payload) {
		this.outgoingApplicationTextPayload = payload;
	}
	
	////
	//PROTECTED
	////
	
	protected static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";
	
	protected String outgoingApplicationTextPayload;
	protected byte[] communicationHandle;
	
	protected ParsedFriendshipMessage parsedMessage;
	
	protected enum MessageType {
		Request(".request"),
		Response(".response");
		
		public final String typeName;
		
		MessageType(String type) {
			typeName = type;
		}
	}
	
	protected class ParsedFriendshipMessage {
		final String applicationTextPayload;
		final byte[] communicationHandle;
		
		ParsedFriendshipMessage(String applicationPayload, byte[] commHandle) {
			this.applicationTextPayload = applicationPayload;
			this.communicationHandle = commHandle;
		}
	}
	
	private enum ExternalType {
		CommunicationHandle("comm");
		
		public final String typeName;
		
		ExternalType(String type) {
			typeName = type;
		}
	}
	
	protected static String applicationMime() {
		return "application/"+APPLICATION_NAME;
	}
	
	protected NdefRecord createApplicationRecord() {
		return NdefRecord.createApplicationRecord(APPLICATION_NAME);
	}
	
	protected NdefRecord createApplicationTextPayload(MessageType type) {
		String usernamePayload = outgoingApplicationTextPayload;
		String mimeTypeName = applicationMime()+type.typeName;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		return payload;
	}
	
	protected NdefRecord createCommunicationHandle(byte [] handle) {
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.CommunicationHandle.typeName, handle);
	}
	
	protected ParsedFriendshipMessage parseMessage(NdefMessage message) {
		String payload = new String(message.getRecords()[0].getPayload());
		byte[] commHandle = message.getRecords()[1].getPayload();
		return new ParsedFriendshipMessage(payload, commHandle);
	}
	
	
}
