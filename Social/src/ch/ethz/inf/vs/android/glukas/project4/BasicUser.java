package ch.ethz.inf.vs.android.glukas.project4;

public class BasicUser implements Comparable<BasicUser> {

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
	
	@Override
	public int compareTo(BasicUser another) {
		int usernameComparison = this.username.compareTo(another.username);
		if (usernameComparison != 0) return usernameComparison;
		return this.id.compareTo(another.id);
	}
	
	@Override
	public boolean equals(Object another) {
		return (another instanceof BasicUser) && this.compareTo((BasicUser) another) == 0;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
