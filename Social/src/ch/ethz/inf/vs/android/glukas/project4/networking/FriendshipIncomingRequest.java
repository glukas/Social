package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendshipIncomingRequest extends FriendshipMessage implements CreateNdefMessageCallback {

	/**
	 * @param request A friendship request received over NFC
	 */
	public FriendshipIncomingRequest(NdefMessage request) {
        parsedMessage = parseMessage(request);
	}
	
	/**
	 * Creates a message that accepts the friendship request
	 * To accept this friendship request, set this object as a callback, 
	 * and beam
	 */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		NdefRecord payload = createApplicationTextPayload(MessageType.Response);
		NdefRecord commHandle = createCommunicationHandle(this.parsedMessage.communicationHandle);
		NdefRecord appRecord = createApplicationRecord();
		NdefMessage message = new NdefMessage(payload, commHandle, appRecord);
		return message;
	}
	
}
