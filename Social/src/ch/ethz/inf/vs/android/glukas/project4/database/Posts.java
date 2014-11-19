package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;

/**
 * Helper class that implements all functionalities of table Posts.
 * @author alessiobaehler
 *
 */
class Posts {

	// Update the wall of the user with the given post
	public void putUserPost(Post post) {
		
	}
	
	// Get a certain post from the user's wall
	public Post getUserPost(int id) {
		
	}
	
	// Delete a certain post from the user's wall
	public void deleteUserPost(int id) {
		
	}
	
	// Get all the Posts in a Wall starting from id -> id or time?
	public List<Post> getAllUserPostsFrom(int timestamp) {
		
	}
	
	// Update the wall of a friend whose wall is saved on our phone
	public void putFriendPost(Post post, int friendid) {
		
	}
	
	// Get a certain Post from a certain friend
	public Post getFriendPost(int postid, int friendid) {
		
	}
	
	// Get all Posts of a certain friend starting at a certain time/timestamp
	public List<Post> getAllFriendPostsfrom(Date timestamp, int friendid) {
		
	}
	
	// delete a certain Post of a certain friend
	public void deleteFriendPost(int postid, int friendid) {
		
	}
}
