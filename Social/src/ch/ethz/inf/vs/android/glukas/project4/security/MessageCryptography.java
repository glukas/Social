package ch.ethz.inf.vs.android.glukas.project4.security;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class MessageCryptography {

	private final CredentialStorage keyStore;
	private final Mac mac;
	private final Cipher cipher;
	private final SecureRandom random;
	
	MessageCryptography(CredentialStorage keyStorage, Mac mac, Cipher cipher, SecureRandom random) {
		this.keyStore = keyStorage;
		this.mac = mac;
		this.cipher = cipher;
		this.random = random;
	}
	
	byte[] encryptPost(NetworkMessage message) {
		byte[] cryptedBytes = null;
		try {
			//Encrypt
			SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(message.header.getReceiver());
			cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, random);
			byte[] messageBytes = message.text.getBytes();
			//byte[] headerBytes = message.header.getbytes();//TODO
			
			cryptedBytes = cipher.update(messageBytes);
			//prepend public header
			byte[] headerBytes = message.header.getbytes();
			
			//TODO Authenticate
			SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(message.header.getReceiver());
			
			//prepend header
			int messageLength = cryptedBytes.length + headerBytes.length;
			ByteBuffer result = ByteBuffer.allocate(messageLength+4);
			result.putInt(messageLength);
			result.put(headerBytes);
			result.put(cryptedBytes);
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return cryptedBytes;
	}
	
	NetworkMessage decryptPost(byte[] message) {
		ByteBuffer messageBytes = ByteBuffer.wrap(message);
		messageBytes.getInt();//length of message
		
		//messageBytes.get
		//TODO extract public header to get sender & recipient
		//TODO Authenticate

		//TODO Decrypt
		//byte[] cryptedText = ;
		//SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(header.getReceiver());
		//cipher.init(Cipher.DECRYPT_MODE, , random)
		//byte[] decryptedText = 
		
		return null;
	}
	
}
