package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;
import java.util.Arrays;

import android.nfc.NdefMessage;
import android.util.Log;

public class FriendshipResponse extends FriendshipMessage {

	public FriendshipResponse(NdefMessage response) {
		parsedMessage = parseMessage(response);
		this.communicationHandle = parsedMessage.communicationHandle;
	}
	
	/**
	 * @param request
	 * @return true if this is a response to 'request'
	 */
	public boolean isMatching(FriendshipOutgoingRequest request) {
		if (request == null) return false;
		//Log.v(this.getClass().toString(), "comm handle : " + new BigInteger(parsedMessage.communicationHandle));
		return Arrays.equals(parsedMessage.communicationHandle, request.communicationHandle);
	}
	
}
