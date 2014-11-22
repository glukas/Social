package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;

import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.security.KeyGeneration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendshipOutgoingRequest extends FriendshipMessage implements CreateNdefMessageCallback {

	private static FriendshipOutgoingRequest currentRequest;
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
		NdefRecord payload = createApplicationTextPayload(MessageType.Request);
		communicationHandle = KeyGeneration.getInstance().getPseudorandom(16);
		Log.v(this.getClass().toString(), "create comm handle : " + new BigInteger(communicationHandle));
		//TODO init comm handle
		NdefRecord commHandle = createCommunicationHandle(communicationHandle);
		NdefRecord appRecord = createApplicationRecord();
		
		NdefMessage message = new NdefMessage(payload, commHandle, appRecord);
		return message;
	}
	
	public static void setCurrentRequest(FriendshipOutgoingRequest request) {
		//Log.v("FriendshipOutgoingRequest", "set current request create comm handle : " + new BigInteger(request.communicationHandle));
		currentRequest = request;
	}
	
	public static FriendshipOutgoingRequest getCurrentRequest() {
		return currentRequest;
	}

}
