package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;

/**
 * List of all functionalities offered by the database
 */
public interface DatabaseDelegate {
	
	//TODO @Vincent : talk with Alessio and Young about all that stuff 
	
	/*
	 * One idea would be to write here all methods that the Protocol Layer needs and you can implement
	 * this interface in DatabaseManager. It has the advantage to separate clearly what client and
	 * provider see. Else, it will help us to see clearly which methods we can use.
	 */

	////
	//Friends management
	////
	
	/**
	 * Add a friend
	 * @param id the id of the friend (128 bits)
	 * @param username the user name of the added friend
	 */
	//alternatively, addFriend(User user);
	public void addMapping(UserId id, String username);
	
	/**
	 * Get an user name from an user id
	 * @param id
	 * @return A String representation of the user
	 */
	public String getUsername(UserId id);
	
	/**
	 * Get an user id from an user name (cannot ensures uniqueness)
	 * @param username
	 * @return A list of different users matched by provided user name
	 */
	public List<UserId> getUserid(String username);
	
	/**
	 * Delete a friend
	 * @param id
	 * @param username
	 */
	//alternatively, deleteFriend(User user);
	public void deleteMapping(UserId id, String username);
	
	
	////
	//Posts management
	////
	
	/**
	 * Add a post to the database
	 * @param post, should be self-contained. (Contain owner wall, id)
	 */
	public void addPost(Post post);
	
	/**
	 * Get multiple posts of a wall
	 * @param wall
	 * @param from
	 * @return a list of Posts matched by arguments
	 */
	public List<Post> getPostsFrom(Wall wall, int from);
	
	/**
	 * Delete a post.
	 * @param postId, should be self-contained. (Contain owner wall, id)
	 * @param userId, needed to identify uniquely the post.
	 */
	public void deletePost(int postId, UserId userId);
	
	////
	//Walls management
	////
	
	/**
	 * Get the wall of an user
	 * @param id, owner of the wall
	 * @return wall of user
	 */
	public Wall getWall(UserId id);
	
	/**
	 * Delete the wall of an user
	 * @param id, owner of the wall
	 */
	public void deleteWall(UserId id);
}
