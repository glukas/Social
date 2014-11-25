package ch.ethz.inf.vs.android.glukas.project4.security;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;

import android.util.Log;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import junit.framework.TestCase;

public class MessageCryptographyTest3 extends TestCase {

	public void testDecOfEncIsSame() throws UnsupportedEncodingException {
		
		CredentialStorage store = new ZeroCredentialStorage();
		Cipher cipher = CryptographyParameters.getCipher();
		SecureRandom rand = CryptographyParameters.getRandom();
		
		MessageCryptography crypto = new MessageCryptography(store, null, cipher, rand);
		
		String text = "1 2 3";
		PublicHeader header = new PublicHeader(0, new byte[3], (byte) 0, 0, new UserId("0"), new UserId("1"));
		assertTrue(header.getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		assertTrue(new PublicHeader(ByteBuffer.wrap(header.getbytes())).getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		//assertEquals(new PublicHeader(ByteBuffer.wrap(header.getbytes())).getbytes(), header.getbytes());
		NetworkMessage message = new NetworkMessage(text, header);
		byte[] crypted = crypto.encryptPost(message);
		assertTrue(crypted.length > 0);
		
		NetworkMessage decrypted = crypto.decryptPost(crypted);
		assertTrue(decrypted.header.getbytes().length == PublicHeader.BYTES_LENGTH_HEADER);
		//assertEquals(header.getbytes(), decrypted.header.getbytes());
		assertEquals(text, (new String(text.getBytes("UTF-8"))));
		assertEquals(text, decrypted.text);
	}
	
	
}
