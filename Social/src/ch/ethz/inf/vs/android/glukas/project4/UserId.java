package ch.ethz.inf.vs.android.glukas.project4;

import java.math.BigInteger;
import java.security.SecureRandom;

import ch.ethz.inf.vs.android.glukas.project4.security.CryptographyParameters;

/**
 * Represents the identity of a peer
 */
public class UserId implements Comparable<UserId>{

	private BigInteger id;
	public static final int LENGTH = 16;
	private static SecureRandom random = CryptographyParameters.getRandom();
	
	/**
	 * Generate a fresh - random - user id
	 */
	public UserId() {
		this(generateUserId());
	}
	
	public UserId(String value) {
		id = new BigInteger(value);
	}
	
	public UserId(byte[] twosComplement) {
		id = new BigInteger(twosComplement);
	}
	
	public BigInteger getId(){
		return id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
	@Override
	public int compareTo(UserId otherId) {
		return this.id.compareTo(otherId.id);
	}
	
	@Override
	public int hashCode() {
		return this.id.intValue();
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof UserId) && this.compareTo((UserId)other) == 0;
	}
	
	
	/**
	 * Get a fixed size byte array representing this UserId
	 */
	public byte[] getBytes() {
		byte[] array = id.toByteArray();
		//if the array provided by BigInteger is less than 16 bytes, extend this array
		if (array.length < LENGTH){
			//extend with zero if integer is non-negative, else with 1. (Two's complement)
			byte extendByte = (array[0] < 0 ? (byte)0xff : new Byte("0"));
			byte[] tmp = new byte[LENGTH];
			for (int i = 0; i < LENGTH; i++){
				if (array.length + i >= LENGTH){
					tmp[i] = array[i - (LENGTH - array.length)];
				} else {
					tmp[i] = extendByte;
				}
			}
			array = tmp;
		} 
		return array;
	}
	
	//This can be used to generate user Ids.
	private static byte[] getPseudorandom(int numberOfBytes) {
		byte[] bytes = new byte[numberOfBytes];
		random.nextBytes(bytes);
		return bytes;
	}
	
	private static byte[] generateUserId() {
		return getPseudorandom(16);
	}
}
