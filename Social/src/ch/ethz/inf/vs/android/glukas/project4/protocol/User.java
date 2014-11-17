package ch.ethz.inf.vs.android.glukas.project4.protocol;

//Represents the identity of a user
public class User {

	private static byte [] broadcastId = {0, 0, 0, 0, 0, 0, 0, 0};
	private final byte[] id;//8 bytes
	
	public static User allFriends = new User(broadcastId);
	
	public User(byte[] uniqueId) {
		this.id = uniqueId;
	}
}
