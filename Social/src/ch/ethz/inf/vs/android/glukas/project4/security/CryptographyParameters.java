package ch.ethz.inf.vs.android.glukas.project4.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptographyParameters {

	private static final String encryptionAlgorithm = "AES";
	private static final String authenticationAlgorithm = "HmacSHA256";
	private static final String encryptionMode = "CBC";
	private static final String encryptionPadding = "PKCS7Padding";
	
	public SecretKey decodeAuthenticationKey(byte[] authenticationKey) {
		SecretKeySpec spec = new SecretKeySpec(authenticationKey, authenticationAlgorithm);
		return spec;
	}
	
	public SecretKey decodeEncryptionKey(byte[] encryptionKey) {
		SecretKeySpec spec = new SecretKeySpec(encryptionKey, encryptionAlgorithm);
		return spec;
	}
	
	public static SecureRandom getRandom() {
		return new SecureRandom();
	}
	
	public static Cipher getCipher() {
		Cipher c = null;
		try {
			c = Cipher.getInstance(encryptionAlgorithm+"/"+encryptionMode+"/"+encryptionPadding);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return c;
	}

	public static Mac getMac() {
		Mac mac = null;
		try {
			mac = Mac.getInstance(authenticationAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return mac;
	}
	
	public static KeyGenerator getEncryptionKeyGenerator() {
		KeyGenerator encryptionKeyGenerator = null;
		try {
			encryptionKeyGenerator = KeyGenerator.getInstance(encryptionAlgorithm);
			encryptionKeyGenerator.init(128, getRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encryptionKeyGenerator;
	}
	
	public static KeyGenerator getAuthenticationKeyGenerator() {
		KeyGenerator macKeyGenerator = null;
		try {
			macKeyGenerator = KeyGenerator.getInstance(authenticationAlgorithm);
			macKeyGenerator.init(256, getRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return macKeyGenerator;
	}
}
