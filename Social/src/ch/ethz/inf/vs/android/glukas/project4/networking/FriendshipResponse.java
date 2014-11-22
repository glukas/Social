package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;

public class FriendshipResponse extends FriendshipMessage {

	public final String friendsUsername;
	
	public FriendshipResponse(NdefMessage response) {
		String usernameProtocolMessage = new String(response.getRecords()[0].getPayload());
        friendsUsername = usernameProtocolMessage;//TODO(Vincent) parse usernameProtocolMessage
	}
	
	public boolean isAccepting() {
		return false;
	}
	
	public boolean isAccepting(FriendshipOutgoingRequest request) {
		return false;
	}
	
}
