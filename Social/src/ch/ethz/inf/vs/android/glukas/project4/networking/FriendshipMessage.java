package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.security.KeyGeneration;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.util.Log;

/**
 * The protocol runs over NFC:
 * Request)   A -> B : A, key_A
 * Response)  B -> A : B, key_B
 */
public abstract class FriendshipMessage implements CreateNdefMessageCallback {
	
	/**
	 * @return the sender of this message
	 */
	public User getSender() {
		//TODO (Vincent) 
		return new User(null, applicationTextPayload, null, null);
	}
	
	////
	//PROTECTED
	////
	
	protected static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";
	
	protected String applicationTextPayload;
	protected byte[] communicationHandle;
	protected MessageType messageType;

	protected enum MessageType {
		Request(".request"),
		Response(".response");
		
		public final String typeName;
		
		MessageType(String type) {
			typeName = type;
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
		String usernamePayload = applicationTextPayload;
		String mimeTypeName = applicationMime()+type.typeName;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		return payload;
	}
	
	protected NdefRecord createCommunicationHandle(byte [] handle) {
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.CommunicationHandle.typeName, handle);
	}
	
	protected void parseMessage(NdefMessage message) {
		this.applicationTextPayload = new String(message.getRecords()[0].getPayload());
		this.communicationHandle = message.getRecords()[1].getPayload();
	}
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		NdefRecord payload = createApplicationTextPayload(messageType);
		NdefRecord commHandle = createCommunicationHandle(communicationHandle);
		NdefRecord appRecord = createApplicationRecord();
		
		NdefMessage message = new NdefMessage(payload, commHandle, appRecord);
		return message;
	}
}
