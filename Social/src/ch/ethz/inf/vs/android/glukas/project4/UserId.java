package ch.ethz.inf.vs.android.glukas.project4;

import java.math.BigInteger;

/**
 * Represents the identity of a peer
 */
public class UserId {

	private BigInteger id;
	public static final int LENGTH = 16;

	public UserId(String value) {
		id = new BigInteger(value);
	}
	
	public UserId(byte[] twosComplement) {
		id = new BigInteger(twosComplement);
	}

	public BigInteger getId(){
		return id;
	}
	
	public String toString() {
		return "userId : " + id;
	}
	
	/**
	 * Get a byte array representing this UserId
	 */
	public byte[] getBytes() {
		byte[] array = id.toByteArray();
		if (array.length < LENGTH){
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
}
