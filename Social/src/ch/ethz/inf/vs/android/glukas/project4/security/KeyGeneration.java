package ch.ethz.inf.vs.android.glukas.project4.security;

import java.security.SecureRandom;

public class KeyGeneration {

	static final KeyGeneration keyGen = new KeyGeneration();
	
	public static final KeyGeneration getInstance() {
		return keyGen;
	}
	
	//Note: not so "secure" on android 4.3 and lower.
	private SecureRandom secureRandom = new SecureRandom();
	
	public byte[] getPseudorandom(int numberOfBytes) {
		byte[] bytes = new byte[numberOfBytes];
		secureRandom.nextBytes(bytes);
		return bytes;
	}
	
}
