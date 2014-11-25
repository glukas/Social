package ch.ethz.inf.vs.android.glukas.project4.security;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

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
		
		ByteBuffer result = null;
		try {
			//Encrypt
			SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(message.header.getReceiver());
			cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, random);
			byte[] messageBytes = message.text.getBytes();
			
			cipher.update(messageBytes);
			byte [] cryptedBytes = cipher.doFinal();
			//TODO Authenticate
			SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(message.header.getReceiver());
			
			//prepend header
			int messageLength = cryptedBytes.length + PublicHeader.BYTES_LENGTH_HEADER;
			result = ByteBuffer.allocate(messageLength);
			
			//prepend public header
			message.header.setLength(messageLength);
			byte[] headerBytes = message.header.getbytes();
			result.put(headerBytes);
			result.put(cryptedBytes);
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
		
		return result.array();
	}
	
	NetworkMessage decryptPost(byte[] message) {
		ByteBuffer messageBytes = ByteBuffer.wrap(message);
		ByteBuffer textBytes = null;
		//extract public header to get sender & recipient
		PublicHeader header = new PublicHeader(messageBytes);
		SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(header.getReceiver());
		try {
			
			//Decrypt
			cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
			messageBytes.position(PublicHeader.BYTES_LENGTH_HEADER);
			int textSize = cipher.getOutputSize(messageBytes.remaining());
			textBytes = ByteBuffer.allocate(textSize);
			
			cipher.doFinal(messageBytes, textBytes);
			//TODO Authenticate
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (ShortBufferException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
		
		String text = new String(textBytes.array());
		return new NetworkMessage(text, header);
	}
	
}
