package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Dummy storage class that returns the same constant key for all users
 */
public class ZeroCredentialStorage implements CredentialStorage {

	private final SecretKey enc = CryptographyParameters.getEncryptionKeyGenerator().generateKey();
	private final SecretKey auth = CryptographyParameters.getAuthenticationKeyGenerator().generateKey();
	
	@Override
	public SecretKey getBroadcastEncryptionKey(UserId user) {
		return enc;
	}

	@Override
	public SecretKey getBroadcastAuthenticationKey(UserId user) {
		return auth;
	}
	
	public SecretKey getPublicEncryptionKey(UserId user) {
		return null;
	}
	
	public SecretKey getPublicAuthenticationKey(UserId user) {
		return null;
	}

	@Override
	public UserCredentials getUserCredentials(UserId user) {
		return new UserCredentials(user, enc.getEncoded(), auth.getEncoded());
	}

}
