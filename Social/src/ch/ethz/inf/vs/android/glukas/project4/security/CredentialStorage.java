package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Provides thread-safe access to keys
 */
public abstract class CredentialStorage {

	public abstract SecretKey getBroadcastEncryptionKey(UserId user);
	
	public abstract SecretKey getBroadcastAuthenticationKey(UserId user);
	
	public abstract void setBroadcastEncryptionKey(UserId user, SecretKey key);
	
	public abstract void setBroadcastAuthenticationKey(UserId user, SecretKey key);
	
	private static CredentialStorage defaultStore =  new ZeroCredentialStorage();//Dummy implementation
	
	public static CredentialStorage getDefaultStore() {
		return defaultStore;
	}
}
