package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.math.BigInteger;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.security.KeyGeneration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendshipRequest extends FriendshipMessage {

	private static FriendshipRequest currentRequest;
	
	/**
	 * Create a request, intended to be sent over the network via Android Beam.
	 * The sender should be the user of the device this code is running on.
	 * @param sender
	 */
	public FriendshipRequest(User sender) {
		this.messageType = MessageType.Request;
		this.sender = sender;
		this.communicationHandle = KeyGeneration.getInstance().getPseudorandom(16);
		Log.v(this.getClass().toString(), "create comm handle : " + new BigInteger(communicationHandle));
	}
	
	/**
	 * Parses a request that was received over the network.
	 * @param request
	 */
	public FriendshipRequest(NdefMessage request) {
		parseMessage(request);
	}
	
	/**
	 * @param acceptingUser the user of the device this code is running on
	 * @return a response that can be used to accept this request. Intended to be sent over AndroidBeam.
	 */
	public FriendshipResponse createAcceptingResponse(User acceptingUser) {
		FriendshipResponse response = new FriendshipResponse(acceptingUser, communicationHandle);
		//TODO set own keys
		return response;
	}
	
	public static void setCurrentRequest(FriendshipRequest request) {
		//Log.v("FriendshipOutgoingRequest", "set current request create comm handle : " + new BigInteger(request.communicationHandle));
		currentRequest = request;
	}
	
	public static FriendshipRequest getCurrentRequest() {
		return currentRequest;
	}

}
