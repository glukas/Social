package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.Wall;

/**
 * Helper class that implements all functionalities between tables Users and Posts.
 * @author alessiobaehler
 *
 */

class Walls {

	// Get the whole wall of the user
	public Wall getUserWall() {
		return null;
	}
	
	// Get all the Posts in a Wall starting from id -> id or time?
	public List<Post> getAllPostsUserFrom(int timestamp) {
		return null;
	}
	
	// Get the whole Wall of a certain friend
	public Wall getFriendWall(int friendid) {
		return null;
	}
	
	// delete the whole saved Wall of a certain friend
	public void deleteFriendWall(int friendid) {
		
	}
}
