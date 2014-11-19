package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import android.util.Pair;

/**
 * This class represents a chat user.
 * 
 */
public class User {

	// author: Alessio
	// just to begin

	// User id
	int id;

	// User name
	String username;

	// Wall of the user
	Wall wall;

	// All friends of the user
	List<User> friends;

	// Key Pair from Diffie-Hellman?
	Pair<String, String> key;

	// Extra stuff
	// int age;

	// Constructor
	public User(int id, String username, Wall wall, List<User> friends) {
		this.id = id;
		this.username = username;
		this.wall = wall;
		this.friends = friends;

	}

}
