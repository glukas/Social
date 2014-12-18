package ch.ethz.inf.vs.android.glukas.project4;

/**
 * This class represents a chat user.
 */
public class User extends BasicUser {

	private UserCredentials credentials;
	
	/**
	 * Construct a new User
	 * @param id
	 * @param username
	 * @param wall
	 * @param friends
	 */
	public User(UserId id, String username, UserCredentials credentials) {
		super(id, username);
		this.credentials = credentials;
	}
	
	/**
	 * Creates a new User with a fresh UserId and credentials
	 * Use this to create the user for the app owner.
	 * @param username
	 */
	public User(String username) {
		super(new UserId(), username);
		this.credentials = new UserCredentials(this.id);
	}
	
	// Getters.
	public UserId getId() {
		return this.id;
	}
	
	public String getUsername() {
		return username;
	}

	public UserCredentials getCredentials() {
		return credentials;
	}
	
	@Override
	public String toString() {
		return username;
	}
}
