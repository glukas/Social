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
	
	public UserId getId() {
		return id;
	}

	public void setId(UserId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
