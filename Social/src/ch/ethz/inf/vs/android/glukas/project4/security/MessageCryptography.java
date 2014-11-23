package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class MessageCryptography {

	CredentialStorage keyStore;
	
	byte[] encryptPost(NetworkMessage message) {
		//TODO Encrypt
		SecretKey encryptionKey = keyStore.getBroadcastEncryptionKey(message.header.getReceiver());
		//TODO Authenticate
		SecretKey authenticationKey = keyStore.getBroadcastAuthenticationKey(message.header.getReceiver());
		return null;
	}
	
	NetworkMessage decryptPost(byte[] message) {
		//TODO extract public header to get sender & recipient
		//TODO Authenticate
		//TODO Decrypt
		return null;
	}
	
}
