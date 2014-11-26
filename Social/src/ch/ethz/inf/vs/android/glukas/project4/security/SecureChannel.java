package ch.ethz.inf.vs.android.glukas.project4.security;

import android.os.Handler;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServer;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServerDelegate;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class SecureChannel implements AsyncServerDelegate {

	private SecureChannelDelegate secureChannelDelegate;
	private final MessageCryptography crypto;
	private AsyncServer asyncServer;
	private final Handler asyncNetworkHandler;
	
	/**
	 * Note that any secureChannelDelegate calls are made on the (handler-)thread that constructs this object.
	 * @param address of the server in some format that is acceptable in a construction of Socket(address, port)
	 * @param port (port of the server)
	 * @param keyStorage used to encrypt and decrypt messages
	 */
	public SecureChannel(String address, int port, CredentialStorage keyStorage) {
		this.asyncServer = new AsyncServer(address, port, this);
		this.crypto = new MessageCryptography(keyStorage, CryptographyParameters.getMac(), CryptographyParameters.getCipher(), CryptographyParameters.getRandom());
		this.asyncNetworkHandler = new Handler();
	}
	
	/**
	 * Further delegate calls will be made to the delegate
	 * Delegate calls are made on the thread that instantiated this object
	 * @param delegate
	 */
	public void setDelegate(SecureChannelDelegate delegate) {
		this.secureChannelDelegate = delegate;
	}
	
	/**
	 * Asynchronously sends a message over the message relay to the recipients specified by the header
	 * @param message
	 * @param header
	 */
	public void sendMessage(NetworkMessage message) {
		byte[] encrypted;
		//if(message.consistency=...) {
		//For broadcasts:
		encrypted = crypto.encryptPost(message);
		//TODO (direct messages)
		//else {
		
		//}
		this.asyncServer.sendMessage(encrypted);
	}

	////
	//ASYNC SERVER DELEGATE
	////
	
	@Override
	public Handler getCallbackHandler() {
		return this.asyncNetworkHandler;
	}

	@Override
	public void onReceive(byte[] message) {
		//decrypt
		NetworkMessage decrypted = crypto.decryptPost(message);
		if (decrypted != null) {//For now, ignore all corrupted messages
			//forward to delegate
			this.secureChannelDelegate.onMessageReceived(decrypted);
		} else {
			Log.e(this.getClass().toString(), "received invalid message");
		}
	}

	@Override
	public void onSendFailed() {
		Log.e(this.getClass().toString(), "sendFailed");
		// TODO Notify secureChannelDelegate or ignore?
	}
	
	
	//TODO: this should probably be on a higher level.
	/**
	 * Precondition: user was discovered by onPeersDiscovered
	 * @param user
	 */
	//void requestFriendship(User user);
	
	/**
	 * Start looking for peers.
	 * Will asynchronously trigger onPeersDiscovered(List<String> peers)
	 * or onPeerDiscoveryFailed(FailureReason reason) to be called on the delegate
	 */
	//void startPeerDiscovery();
	
}
