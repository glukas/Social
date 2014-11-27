package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.security.CryptographyParameters;

/**
 * This class represents a chat user.
 */
public class User extends BasicUser {

	private UserCredentials credentials;
	
	// Extra stuff. For the moment, I propose that we just ignore extra stuff.
	// It will be easy to add specialties on the top of everything.
	// int age;

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
	
	// TODO: constraints? e.g. length, special characters, ...
	public String getUsername() {
		return username;
	}

	public UserCredentials getCredentials() {
		return credentials;
	}
}
