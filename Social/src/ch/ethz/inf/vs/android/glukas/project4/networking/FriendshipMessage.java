package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

public abstract class FriendshipMessage {

	public String getApplicationPayload() {
		return applicationTextPayload;
	}
	
	public void setResponseApplicationPayload(String payload) {
		this.responseApplicationTextPayload = payload;
	}
	
	protected static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";

	protected enum MessageType {
		Request(".request"),
		Response(".response");
		
		public final String typeName;
		
		MessageType(String type) {
			typeName = type;
		}
	}
	
	protected String applicationTextPayload;
	protected String responseApplicationTextPayload;
	
	protected String applicationMime() {
		return "application/"+APPLICATION_NAME;
	}
	

	
	protected NdefRecord getApplicationRecord() {
		return NdefRecord.createApplicationRecord(APPLICATION_NAME);
	}
	
	protected NdefRecord getApplicationTextPayload(MessageType type) {
		String usernamePayload = responseApplicationTextPayload;
		String mimeTypeName = applicationMime()+type.typeName;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		return payload;
	}
	
	protected String parseApplicationTextPayload(NdefMessage message) {
		//The payload is always at index 0
		return new String(message.getRecords()[0].getPayload());
	}
	
}
