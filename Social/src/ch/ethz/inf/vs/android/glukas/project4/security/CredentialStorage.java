package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Wraps thread safe access to keys
 */
public interface CredentialStorage {

	public SecretKey getBroadcastEncryptionKey(UserId user);
	
	public SecretKey getBroadcastAuthenticationKey(UserId user);
	
	public UserCredentials getUserCredentials(UserId user);
}
