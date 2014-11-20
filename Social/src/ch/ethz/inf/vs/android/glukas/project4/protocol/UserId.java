package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.math.BigInteger;

/**
 * Represents the identity of a peer
 */
public class UserId {

	private BigInteger id;
	
	public UserId(String value) {
		id = new BigInteger(value);
	}
	
	public BigInteger getId(){
		return id;
	}
}
