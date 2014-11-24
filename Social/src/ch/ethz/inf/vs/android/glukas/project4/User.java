package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;


import android.util.Pair;

/**
 * This class represents a chat user.
 */
public class User {

	private UserId id;
	private String username;
	private Wall wall;
	private List<User> friends;
	
	private UserCredentials credentials;
	// Number of posts (partial order)
	private int postsCount;
	// Max post id
	private int maxId;
	
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
	public User(UserId id, String username, Wall wall, List<User> friends) {
		this.id = id;
		this.username = username;
		this.wall = wall;
		this.friends = friends;
		this.credentials = null;
	}
	
	public User(UserId id, String username, Wall wall, List<User> friends, UserCredentials credentials) {
		this.id = id;
		this.username = username;
		this.wall = wall;
		this.friends = friends;
		this.credentials = credentials;
	}
	
	
	// Getters.
	public UserId getId() {
		return this.id;
	}
	
	// TODO: constraints? e.g. length, special characters, ...
	public String getUsername() {
		return username;
	}
	
	public Wall getWall() {
		return wall;
	}
	
	public List<User> getFriends() {
		return friends;
	}

	public UserCredentials getCredentials() {
		return credentials;
	}
	
	public int getPostsCount() {
		return postsCount;
	}
	
	public int getMaxId() {
		return maxId;
	}
	
	// Setters.
	// TODO: add, but only for fields that can be modified!
	
	public void setPostsCount(int newCount) {
		this.postsCount = newCount;
	}
	
	public void setMaxId(int newMax) {
		this.maxId = newMax;
	}
}
