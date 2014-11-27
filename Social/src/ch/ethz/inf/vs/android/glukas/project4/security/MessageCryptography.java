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

import android.util.Base64;

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
			//IV
			byte[] ivBytes = random.generateSeed(cipher.getBlockSize());
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			//Encrypt
			SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(message.header.getReceiver());
			cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec);
			
			byte[] messageBytes = message.text.getBytes();
			byte [] cryptedBytes = cipher.doFinal(messageBytes);
			
			//public header
			int messageLength = cipher.getOutputSize(messageBytes.length) + ivBytes.length + PublicHeader.BYTES_LENGTH_HEADER + mac.getMacLength();
			result = ByteBuffer.allocate(messageLength);
			message.header.setLength(messageLength);
			byte[] headerBytes = message.header.getbytes();
			
			//Authenticate
			SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(message.header.getReceiver());
			mac.init(authenticationKey);
			mac.update(headerBytes);
			mac.update(ivBytes);
			mac.update(cryptedBytes);
			byte[] messageAuthenticationCode = mac.doFinal();
			
			//assemble message
			result.put(headerBytes);
			result.put(messageAuthenticationCode);
			result.put(ivBytes);
			result.put(cryptedBytes);
			
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new RuntimeException("invalid key");
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
		int headerAndMacLength = PublicHeader.BYTES_LENGTH_HEADER+mac.getMacLength();
	
		if (message.length < headerAndMacLength) return null;//Too short message to be legal
		
		ByteBuffer messageBytes = ByteBuffer.wrap(message);
		//extract public header to get sender & recipient
		PublicHeader header = new PublicHeader(messageBytes);
		//SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(header.getReceiver());
		SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(header.getReceiver());
		
		byte[] textBytes = null;
		try {
			//Authenticate
			mac.init(authenticationKey);
			mac.update(message, 0, PublicHeader.BYTES_LENGTH_HEADER);
			mac.update(message, headerAndMacLength, message.length-headerAndMacLength);
			byte[] computedMessageAuthenticationCode = mac.doFinal();//header
			byte[] receivedMessageAuthenticationCode = Arrays.copyOfRange(message, PublicHeader.BYTES_LENGTH_HEADER, headerAndMacLength);//IV and rest of ciphertext
			if (!Arrays.equals(computedMessageAuthenticationCode, receivedMessageAuthenticationCode)) {
				return null;
			}
			
			//Decrypt (the IV is the first block)
			//cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
			textBytes = cipher.doFinal(message, headerAndMacLength, message.length-headerAndMacLength);
			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new RuntimeException("invalid key");
			//return null;
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
