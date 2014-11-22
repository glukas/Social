package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;

public class FriendshipResponse extends FriendshipMessage {

	public FriendshipResponse(NdefMessage response) {
        applicationTextPayload = parseApplicationTextPayload(response);
	}
	
	public boolean isAccepting() {
		return false;
	}
	
	public boolean isAccepting(FriendshipOutgoingRequest request) {
		return false;
	}
	
}
