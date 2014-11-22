package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;

public abstract class FriendshipMessage {

	
	protected static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";
	protected static final String REQUEST_TYPE = ".request";
	protected static final String RESPONSE_TYPE = ".response";
	
	protected String applicationMime() {
		return "application/"+APPLICATION_NAME;
	}
	
	//TODO Vincent implement this
	protected String getOwnUsernamePayload() {
		//TODO (Vincent) here we want to pass the actual username (could be set at construction time, etc.)
		return "Alice";
	}
}
