package ch.ethz.inf.vs.android.glukas.project4.security;

import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.crypto.SecretKey;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

public class KeyStoreCredentialStorage extends CredentialStorage {

	KeyStore keyStore;
	
	KeyStoreCredentialStorage() throws KeyStoreException {
		 keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	}
	
	@Override
	public SecretKey getBroadcastEncryptionKey(UserId user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SecretKey getBroadcastAuthenticationKey(UserId user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBroadcastEncryptionKey(UserId user, SecretKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBroadcastAuthenticationKey(UserId user, SecretKey key) {
		// TODO Auto-generated method stub
		
	}

}
