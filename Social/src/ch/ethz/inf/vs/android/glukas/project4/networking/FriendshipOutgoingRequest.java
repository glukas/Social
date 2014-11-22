package ch.ethz.inf.vs.android.glukas.project4.networking;

import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendshipOutgoingRequest extends FriendshipMessage implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private static FriendshipOutgoingRequest currentRequest;
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		//TODO replace with getOwnUsernamePayload();
		String usernamePayload = "Alice";
		String mimeTypeName = applicationMime()+REQUEST_TYPE;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		
		NdefRecord appRecord = NdefRecord.createApplicationRecord(APPLICATION_NAME);
		NdefMessage message = new NdefMessage(payload, appRecord);
		return message;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "onNdefPushComplete");
		FriendshipOutgoingRequest.setCurrentRequest(this);
	}
	
	private static void setCurrentRequest(FriendshipOutgoingRequest request) {
		currentRequest = request;
	}
	
	public static FriendshipOutgoingRequest getCurrentRequest() {
		return currentRequest;
	}

}
