package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendshipIncomingRequest extends FriendshipMessage implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	public final String usernameOfRequestingPeer;
	
	/**
	 * @param request A friendship request received over NFC
	 */
	public FriendshipIncomingRequest(NdefMessage request) {
		String usernameProtocolMessage = new String(request.getRecords()[0].getPayload());
        usernameOfRequestingPeer = usernameProtocolMessage;//TODO(Vincent) parse usernameProtocolMessage
	}
	
	/**
	 * Creates a message that accepts the friendship request
	 * To accept this friendship request, set this object as a callback, 
	 * and beam
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		//TODO replace with getOwnUsernamePayload();
		String usernamePayload = "Bob";
		String mimeTypeName = applicationMime()+RESPONSE_TYPE;
		NdefRecord payload = NdefRecord.createMime(mimeTypeName, usernamePayload.getBytes());
		
		NdefRecord appRecord = NdefRecord.createApplicationRecord(APPLICATION_NAME);
		NdefMessage message = new NdefMessage(payload, appRecord);
		return message;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "successfully friended " + usernameOfRequestingPeer);
	}
	
}
