package ch.ethz.inf.vs.android.glukas.project4;

public class UserCredentials {

	public final UserId userId;
	public final byte[] broadcastEncryptionKey;
	public final byte[] broadcastAuthenticationKey;
	
	public UserCredentials(UserId user, byte[] encryptionKey, byte[] authenticationKey) {
		this.userId = user;
		this.broadcastAuthenticationKey = authenticationKey;
		this.broadcastEncryptionKey = encryptionKey;
	}
	
}

