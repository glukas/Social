package ch.ethz.inf.vs.android.glukas.project4.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGeneration {

	static final KeyGeneration keyGen = new KeyGeneration();
	
	public static final KeyGeneration getInstance() {
		return keyGen;
	}
	
	//Note: not so "secure" on android 4.3 and lower.
	private SecureRandom secureRandom = new SecureRandom();
	
	private KeyGenerator encryptionKeyGenerator;
	
	private KeyGenerator macKeyGenerator;
	
	private KeyGeneration() {
		try {
			encryptionKeyGenerator = KeyGenerator.getInstance("AES");
			macKeyGenerator = KeyGenerator.getInstance("HmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	//This can be used to generate user Ids.
	public byte[] getPseudorandom(int numberOfBytes) {
		byte[] bytes = new byte[numberOfBytes];
		secureRandom.nextBytes(bytes);
		return bytes;
	}
	
	public byte[] generateUserId() {
		return getPseudorandom(16);
	}
	
	public SecretKey generateEncryptionKey() {
		return encryptionKeyGenerator.generateKey();
	}
	
	public SecretKey generateMACKey() {
		return macKeyGenerator.generateKey();
	}
}
