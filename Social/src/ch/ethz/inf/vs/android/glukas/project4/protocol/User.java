package ch.ethz.inf.vs.android.glukas.project4.protocol;

public class User {

	final byte[] id;//8 bytes
	
	public User(byte[] uniqueId) {
		this.id = uniqueId;
	}
	
}
