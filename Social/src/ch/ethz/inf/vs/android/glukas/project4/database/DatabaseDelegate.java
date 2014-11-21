package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;

/**
 * Abstract list of all functionalities offered by the database.
 * Please refer to this and add here new functions you need.
 * Please consider all methods with a TODO as still having a non 
 * (complete) meaningful declaration.
 */
public interface DatabaseDelegate {

	////
	// User management
	////
	
	/**
	 * Add User to Database (first usage of the app? yes!).
	 * @param user to insert.
	 */
	public void putUser(User user);
	
	/**
	 * Get the upper bound of the number of posts in the user's wall.
	 * @return the upper bound.
	 */
	public int getUserPostsCount();
	
	/**
	 * Get the upper bound over the partial order of actual posts for the user.
	 * @return the upper bound.
	 */
	public int getUserMaxPostId();
	
	
	////
	// Friends management
	////
	
	/** TODO
	 * 
	 * @return
	 */
	public int getFriendPostsCount();
	
	/** TODO
	 * 
	 * @return
	 */
	public int getFriendMaxId();
	
	/** TODO
	 * Add a friend.
	 * @param id the id of the friend (128 bits)
	 * @param username the user name of the added friend
	 */
	public void addFriend(UserId id, String username);
	
	/**
	 * Get an user name from an user id
	 * @param id the id of the friend to retrieve the username
	 * @return A String representation of the user
	 */
	public String getFriendUsername(UserId id);
	
	/**
	 * Get an user id from an user name (cannot ensures uniqueness)
	 * @param username the username of the friend to retrieve the id
	 * @return A list of different users matched by provided user name
	 */
	public List<UserId> getFriendId(String username);
	
	/** TODO
	 * Delete a friend
	 * @param id
	 * @param username
	 */
	public void putFriend(User friend);
	
	/** TODO
	 * 
	 * @param friendid
	 */
	public void deleteFriend(UserId friendid);
	
	////
	//Posts management
	////
	
	/**
	 * Add a post to the database
	 * @param post, should be self-contained. (Contain owner wall, id)
	 */
	public void putUserPost(Post post);
	
	/** TODO
	 * Get multiple posts of a wall
	 * @param wall
	 * @param from
	 * @return a list of Posts matched by arguments
	 */
	public List<Post> getAllUserPostsFrom(int from);
	
	/**
	 * Delete a post.
	 * @param postId, should be self-contained. (Contain owner wall, id)
	 * @param userId, needed to identify uniquely the post.
	 */
	public void deleteUserPost(int postId);
	
	/** TODO
	 * 
	 * @param postid
	 * @return
	 */
	public Post getUserPost(int postid);
	
	/** TODO
	 * 
	 * @param post
	 * @param friendid
	 */
	public void putFriendPost(Post post, int friendid);

	/** TODO
	 * 
	 * @param postid
	 * @param friendid
	 * @return
	 */
	public Post getFriendPost(int postid, int friendid);
	
	/** TODO
	 * 
	 * @param friendid
	 * @param postid
	 * @return
	 */
	public List<Post> getAllFriendPostsFrom(int friendid, int postid);
	
	/** TODO
	 * 
	 * @param postid
	 * @param friendid
	 */
	public void deleteFriendPost(int postid, int friendid);
	
	
	////
	//Walls management
	////
	
	/** TODO
	 * Get the wall of an user
	 * @param id, owner of the wall
	 * @return wall of user
	 */
	public Wall getUserWall();
	
	/** TODO
	 * Delete the wall of an user
	 * @param id, owner of the wall
	 */
	public void deleteUserWall();
	
	/** TODO
	 * 
	 * @param friendid
	 * @return
	 */
	public Wall getFriendWall(int friendid);
	
	/** TODO
	 * 
	 * @param friendid
	 */
	public void deleteFriendWall(int friendid);

}
