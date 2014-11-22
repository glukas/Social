package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;
import java.util.Arrays;

import ch.ethz.inf.vs.android.glukas.project4.User;

import android.nfc.NdefMessage;
import android.util.Log;

public class FriendshipResponse extends FriendshipMessage {

	FriendshipResponse(User user, byte[] communicationHandle) {
		this.messageType = MessageType.Response;
		this.applicationTextPayload = user.getUsername();
		this.communicationHandle = communicationHandle;
	}
	
	public FriendshipResponse(NdefMessage response) {
		parseMessage(response);
	}
	
	/**
	 * @param request
	 * @return true if this is a response to 'request'
	 */
	public boolean isMatching(FriendshipRequest request) {
		if (request == null) return false;
		//Log.v(this.getClass().toString(), "comm handle : " + new BigInteger(parsedMessage.communicationHandle));
		return Arrays.equals(communicationHandle, request.communicationHandle);
	}
	
}
