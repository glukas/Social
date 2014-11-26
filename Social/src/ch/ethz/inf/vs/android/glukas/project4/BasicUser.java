package ch.ethz.inf.vs.android.glukas.project4;

public class BasicUser {

	protected UserId id;
	protected String username;
	
	/**
	 * Construct a new User
	 * @param id
	 * @param username
	 */
	public BasicUser(UserId id, String username) {
		this.id = id;
		this.username = username;
	}
}
