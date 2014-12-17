package ch.ethz.inf.vs.android.glukas.project4.security;

import java.nio.ByteBuffer;

import android.os.Handler;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServer;
import ch.ethz.inf.vs.android.glukas.project4.networking.AsyncServerDelegate;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

/**
 * Note this class is not thread safe.
 * Any method call should occur on the thread that instanciated this object.
 * @author lukas
 *
 */
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
		
		encrypted = crypto.encryptPost(message);
		
		/*int length = message.text.length+PublicHeader.BYTES_LENGTH_HEADER;
		message.header.setLength(length);
		ByteBuffer result = ByteBuffer.allocate(length);
		result.put(message.header.getbytes());
		result.put(message.text);
		encrypted = result.array();*/
		
		/*if (encrypted.length != length) {
			throw new RuntimeException();//This would mean a bug
		}*/
		
		Log.d(this.getClass().toString(), "send : " + message.header.toString() + " || " + message.getText().subSequence(0, message.header.getJSONTextLength()));
		
		this.asyncServer.sendMessage(encrypted);
	}
	
	/**
	 * Asynchronously sends a public header
	 * to the server.
	 * At the moment there is no authentication/encryption between the clients and the server.
	 * @param header
	 */
	public void sendHeader(PublicHeader header) {
		Log.d(this.getClass().toString(), "send : " + header.toString() + " || ");
		this.asyncServer.sendMessage(header.getbytes());
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

		/*
		ByteBuffer messageBuffer = ByteBuffer.wrap(message);
		
		PublicHeader header = new PublicHeader(messageBuffer);
		
		
		byte[] text;
		if (messageBuffer.capacity() > PublicHeader.BYTES_LENGTH_HEADER) {
			messageBuffer.position(PublicHeader.BYTES_LENGTH_HEADER);
			text = new byte[message.length-PublicHeader.BYTES_LENGTH_HEADER];
			messageBuffer.get(text);
		} else {
			text = new byte[0];
		}
		Log.d(this.getClass().toString(), "received : " + header.toString() + " || " + new String(text));
		

		if (this.secureChannelDelegate != null) {
			this.secureChannelDelegate.onMessageReceived(new NetworkMessage(text, header));
		}
		*/
		//decrypt
		
		Log.d(this.getClass().toString(), "onReceive");
		NetworkMessage decrypted = crypto.decryptPost(message);
		if (decrypted != null) {//For now, ignore all corrupted messages
			//forward to delegate
			Log.d(this.getClass().toString(), "received : " + decrypted.header.toString() + " || " + new String(decrypted.text).subSequence(0, decrypted.header.getJSONTextLength()));
			if (this.secureChannelDelegate != null) {
				this.secureChannelDelegate.onMessageReceived(decrypted);
			} else {
				Log.d(this.getClass().toString(), "received message, but delegate is null");
			}
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
