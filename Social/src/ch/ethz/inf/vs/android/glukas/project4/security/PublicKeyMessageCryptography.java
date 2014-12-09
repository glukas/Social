package ch.ethz.inf.vs.android.glukas.project4.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;

public class PublicKeyMessageCryptography {

	private final CredentialStorage keyStore;
	private final Cipher cipher;
	private final SecureRandom random;

	PublicKeyMessageCryptography(CredentialStorage keyStorage, Cipher asymmetricCipher, SecureRandom random) {
		this.keyStore = keyStorage;
		this.cipher = asymmetricCipher;
		this.random = random;
	}
	
	//Sends a message with text encrypted under the public key of the receiver
	public byte [] encryptMessage(NetworkMessage message) {
		return null;
	}
	
	//decrypts a message encrypted under this users public key
	public NetworkMessage decryptMessage(byte[] message) {
		return null;
	}
	
}
