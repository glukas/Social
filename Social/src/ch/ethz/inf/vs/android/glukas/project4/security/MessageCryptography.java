package ch.ethz.inf.vs.android.glukas.project4.security;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class MessageCryptography {

	private final CredentialStorage keyStore;
	private final Mac mac;
	private final Cipher cipher;
	private final SecureRandom random;
	
	private final Charset charset = Charset.availableCharsets().get("UTF-8");
	
	MessageCryptography(CredentialStorage keyStorage, Mac mac, Cipher cipher, SecureRandom random) {
		this.keyStore = keyStorage;
		this.mac = mac;
		this.cipher = cipher;
		this.random = random;
	}
	
	byte[] encryptPost(NetworkMessage message) {
		
		ByteBuffer result = null;
		try {
			//IV
			byte[] ivBytes = random.generateSeed(cipher.getBlockSize());
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			//Encrypt
			SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(message.header.getReceiver());
			cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec);
			
			byte[] messageBytes = message.text.getBytes();
			byte [] cryptedBytes = cipher.doFinal(messageBytes);
			
			//public header
			int messageLength = cipher.getOutputSize(messageBytes.length) + ivBytes.length + PublicHeader.BYTES_LENGTH_HEADER;
			result = ByteBuffer.allocate(messageLength);
			message.header.setLength(messageLength);
			byte[] headerBytes = message.header.getbytes();
			
			//assemble message
			result.put(headerBytes);
			result.put(ivBytes);
			result.put(cryptedBytes);
			
			//TODO Authenticate
			//SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(message.header.getReceiver());
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		}
		
		return result.array();
	}
	
	NetworkMessage decryptPost(byte[] message) {
		ByteBuffer messageBytes = ByteBuffer.wrap(message);
		//extract public header to get sender & recipient
		PublicHeader header = new PublicHeader(messageBytes);
		SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(header.getReceiver());
		byte[] textBytes = null;
		try {
			//Decrypt (the IV is the first block)
			cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
			messageBytes.position(PublicHeader.BYTES_LENGTH_HEADER);
			textBytes = cipher.doFinal(message, PublicHeader.BYTES_LENGTH_HEADER, message.length-PublicHeader.BYTES_LENGTH_HEADER);
			//TODO Authenticate
			
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
		
		byte[] textByteClean = Arrays.copyOfRange(textBytes, cipher.getBlockSize(), textBytes.length);
		String text = new String(textByteClean);
		
		return new NetworkMessage(text, header);
	}
	
}
