package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.security.CredentialStorage;
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
		return sender;
	}
	
	////
	//PROTECTED
	////
	
	protected static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";
	
	protected User sender;
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
		CommunicationHandle("comm"),
		UserId("userId"),
		AuthKey("sAuth"),
		EncKey("sEnc");
		
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
		String usernamePayload = sender.getUsername();
		String mimeTypeName = applicationMime()+type.typeName;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		return payload;
	}
	
	protected NdefRecord createCommunicationHandleRecord(byte [] handle) {
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.CommunicationHandle.typeName, handle);
	}
	
	protected NdefRecord createUserIdRecord() {
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.UserId.typeName, sender.getId().getId().toByteArray());
	}
	
	protected void parseMessage(NdefMessage message) {
		NdefRecord[] records = message.getRecords();
		String username = new String(records[0].getPayload());
		communicationHandle = records[1].getPayload();
		UserId senderId = new UserId(records[2].getPayload());
		UserCredentials credentials = new UserCredentials(senderId, records[3].getPayload(), records[4].getPayload());
 		sender = new User(senderId, username, null, null, credentials);
	}
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		NdefRecord payload = createApplicationTextPayload(messageType);
		NdefRecord commHandle = createCommunicationHandleRecord(communicationHandle);
		NdefRecord userId = createUserIdRecord();
		NdefRecord encKey = createEncKeyRecord();
		NdefRecord authKey = createAuthKeyRecord();
		NdefRecord appRecord = createApplicationRecord();
		
		NdefMessage message = new NdefMessage(payload, commHandle, userId, encKey, authKey, appRecord);
		return message;
	}

	private NdefRecord createAuthKeyRecord() {
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.AuthKey.typeName, sender.getCredentials().broadcastAuthenticationKey);
	}

	private NdefRecord createEncKeyRecord() {
		//Log.d(this.getClass().toString(), "enc key ("+ sender.getUsername() + ")" + new BigInteger(CredentialStorage.getDefaultStore().getBroadcastEncryptionKey(sender.getId()).getEncoded()));
		return NdefRecord.createExternal(APPLICATION_NAME, ExternalType.EncKey.typeName, sender.getCredentials().broadcastEncryptionKey);
	}
}
