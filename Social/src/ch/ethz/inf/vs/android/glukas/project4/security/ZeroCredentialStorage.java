package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Dummy storage class that returns the same constant key for all users
 */
public class ZeroCredentialStorage implements CredentialStorage {

	public static final ZeroCredentialStorage constantCredentials = new ZeroCredentialStorage();
	
	private SecretKey enc = CryptographyParameters.getInstance().generateEncryptionKey();
	private SecretKey auth = CryptographyParameters.getInstance().generateMACKey();
	
	@Override
	public SecretKey getBroadcastEncryptionKey(UserId user) {
		return enc;
	}

	@Override
	public SecretKey getBroadcastAuthenticationKey(UserId user) {
		return auth;
	}

	@Override
	public UserCredentials getUserCredentials(UserId user) {
		return new UserCredentials(user, enc.getEncoded(), auth.getEncoded());
	}

}
