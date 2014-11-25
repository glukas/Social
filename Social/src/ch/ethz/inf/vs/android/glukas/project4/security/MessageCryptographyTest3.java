package ch.ethz.inf.vs.android.glukas.project4.security;

import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import junit.framework.TestCase;

public class MessageCryptographyTest3 extends TestCase {

	public void testDecOfEncIsSame() {
		
		CredentialStorage store = new ZeroCredentialStorage();
		Cipher cipher = CryptographyParameters.getCipher();
		SecureRandom rand = CryptographyParameters.getRandom();
		
		MessageCryptography crypto = new MessageCryptography(store, null, cipher, rand);
		
		String text = "Hello World";
		PublicHeader header = new PublicHeader(0, null, (byte) 0, 0, new UserId("0"), new UserId("1"));
		
		NetworkMessage message = new NetworkMessage(text, header);
		byte[] crypted = crypto.encryptPost(message);
		assertTrue(crypted.length > 0);
		assertEquals(text, crypto.decryptPost(crypted).text);
		
	}
	
}
