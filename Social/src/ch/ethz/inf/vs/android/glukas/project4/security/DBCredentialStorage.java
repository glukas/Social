package ch.ethz.inf.vs.android.glukas.project4.security;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;

public class DBCredentialStorage implements CredentialStorage {

	DatabaseAccess database;
	
	DBCredentialStorage(DatabaseAccess db) {
		database = db;
	}
	
	@Override
	public SecretKey getBroadcastEncryptionKey(UserId user) {
		byte[] encryptionKey = database.getUserCredentials(user).broadcastEncryptionKey;
		return CryptographyParameters.getInstance().decodeEncryptionKey(encryptionKey);
	}

	@Override
	public SecretKey getBroadcastAuthenticationKey(UserId user) {
		byte[] authenticationKey = database.getUserCredentials(user).broadcastAuthenticationKey;
		return CryptographyParameters.getInstance().decodeEncryptionKey(authenticationKey);
	}

	@Override
	public UserCredentials getUserCredentials(UserId user) {
		return database.getUserCredentials(user);
	}
	
}
