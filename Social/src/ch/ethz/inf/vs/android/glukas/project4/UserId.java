package ch.ethz.inf.vs.android.glukas.project4;

import java.math.BigInteger;

/**
 * Represents the identity of a peer
 */
public class UserId {

	private BigInteger id;

	public UserId(String value) {
		id = new BigInteger(value);
	}
	
	public UserId(byte[] twosComplement) {
		id = new BigInteger(twosComplement);
	}

	public BigInteger getId(){
		return id;
	}
}
