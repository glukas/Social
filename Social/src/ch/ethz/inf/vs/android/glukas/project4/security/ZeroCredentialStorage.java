package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Dummy storage class that returns the same constant key for all users
 */
public class ZeroCredentialStorage extends CredentialStorage {

	SecretKey enc = KeyGeneration.getInstance().generateEncryptionKey();
	SecretKey auth = KeyGeneration.getInstance().generateMACKey();
	
	@Override
	public SecretKey getBroadcastEncryptionKey(UserId user) {
		return enc;
	}

	@Override
	public SecretKey getBroadcastAuthenticationKey(UserId user) {
		return auth;
	}

	@Override
	public void setBroadcastEncryptionKey(UserId user, SecretKey key) {
	}

	@Override
	public void setBroadcastAuthenticationKey(UserId user, SecretKey key) {
	}

}
