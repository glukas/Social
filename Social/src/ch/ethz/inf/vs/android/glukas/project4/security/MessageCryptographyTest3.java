package ch.ethz.inf.vs.android.glukas.project4.security;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NullCipher;

import android.util.Log;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import junit.framework.TestCase;

public class MessageCryptographyTest3 extends TestCase {

	MessageCryptography crypto;
	
	public void setUp() {
		CredentialStorage store = new ZeroCredentialStorage();
		Cipher cipher = CryptographyParameters.getCipher();
		SecureRandom rand = CryptographyParameters.getRandom();
		Mac mac = CryptographyParameters.getMac();
		
		crypto = new MessageCryptography(store, mac, cipher, rand);
	}
	
	public void testDecOfEncIsSame() {
		String [] texts = {"", "1", "abc", "srffksjdhfasjfhweaoisrnvywirusfdhj234r89f4woirsfväööü., jjja  a sdjjqo     oeood"};
		for (String text : texts) {
			correctnessTest(text);
		}
	}
	
	public void correctnessTest(String text) {
		PublicHeader header = new PublicHeader(0, new byte[3], (byte) 0, 0, new UserId("0"), new UserId("1"));
		assertTrue(header.getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		assertTrue(new PublicHeader(ByteBuffer.wrap(header.getbytes())).getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		assertTrue(Arrays.equals(new PublicHeader(ByteBuffer.wrap(header.getbytes())).getbytes(), header.getbytes()));
		NetworkMessage message = new NetworkMessage(text, header);
		byte[] crypted = crypto.encryptPost(message);
		assertNotNull(crypted);
		assertTrue(crypted.length >= PublicHeader.BYTES_LENGTH_HEADER);
		
		NetworkMessage decrypted = crypto.decryptPost(crypted);
		assertNotNull(decrypted);
		assertTrue(decrypted.header.getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		
		assertTrue(Arrays.equals(header.getbytes(),decrypted.header.getbytes()));
		assertEquals(text, decrypted.text);
	}
	
	public void testAuthFailsGracefullyForTooShortMessages() {
		byte[] crypted = {0, 1, 2, 0, 0};
		assertNull(crypto.decryptPost(crypted));
		
		crypted = new byte[PublicHeader.BYTES_LENGTH_HEADER-1];
		assertNull(crypto.decryptPost(crypted));
		
		crypted = new byte[PublicHeader.BYTES_LENGTH_HEADER+1];
		assertNull(crypto.decryptPost(crypted));
	}
	
	public void testAuthFailsForWrongMac() {
		
		String text = "abc-def-ghi-890";
		
		PublicHeader header = new PublicHeader(0, new byte[3], (byte) 0, 0, new UserId("-10"), new UserId("-11"));
		NetworkMessage message = new NetworkMessage(text, header);
		byte[] crypted = crypto.encryptPost(message);
		crypted[PublicHeader.BYTES_LENGTH_HEADER] = 1;
		assertNull(crypto.decryptPost(crypted));
		
	}
	
}
