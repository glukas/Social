package ch.ethz.inf.vs.android.glukas.project4;

import javax.crypto.KeyGenerator;

import ch.ethz.inf.vs.android.glukas.project4.security.CryptographyParameters;

public class UserCredentials {

	public final UserId userId;
	public final byte[] broadcastEncryptionKey;
	public final byte[] broadcastAuthenticationKey;
	
	public UserCredentials(UserId user, byte[] encryptionKey, byte[] authenticationKey) {
		this.userId = user;
		this.broadcastAuthenticationKey = authenticationKey;
		this.broadcastEncryptionKey = encryptionKey;
	}
	
	/**
	 * Generates fresh -random- user credentials.
	 * @param id
	 */
	public UserCredentials(UserId id) {
		this.userId = id;
		this.broadcastEncryptionKey = CryptographyParameters.getEncryptionKeyGenerator().generateKey().getEncoded();
		this.broadcastAuthenticationKey = CryptographyParameters.getAuthenticationKeyGenerator().generateKey().getEncoded();
	}
	
}

