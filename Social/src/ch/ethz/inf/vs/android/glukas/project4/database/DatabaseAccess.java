package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

/**
 * Abstract list of all functionalities offered by the database.
 * All methods are performed atomically.
 */
public interface DatabaseAccess {

	////
	// User management
	////
	
	/**
	 * Add User to Database.
	 * @param user to insert.
	 */
	public void putUser(User user);
	
	/**
	 * Get User of device from Database.
	 * @return the User object (wall and friends list are null!)
	 * if the user exists, else null
	 */
	public User getUser();
	
	/**
	 * Get the upper bound of the number of posts in the user's wall.
	 * @return the upper bound.
	 */
	public int getUserPostsCount();
	
	/**
	 * Get the upper bound over the partial order of actual posts for the user.
	 * @return the upper bound.
	 */
	public int getUserMaxPostsId();
	
	/**
	 * Set the upper bound of the number of posts in the user's wall.
	 * @param newCount
	 */
	public void setUserPostsCount(int newCount);
	
	/**
	 * Set the upper bound over the partial order of actual posts for the user.
	 * @param newMaxPostsId
	 */
	public void setUserMaxPostsId(int newMaxPostsId);
	
	/**
	 * Get a list of all user's friends.
	 * @return List of User objects that have only the id field set, while all others are null; null if no friend is found.
	 */
	public List<User> getUserFriendsList();

	////
	// Credentials (Keys)
	////
	
	/**
	 * Get the credentials of the given user.
	 * @param user the id of the user to retrieve the credentials
	 * @return the corresponding UserCredentials object if the user was found, else null
	 */
	public UserCredentials getUserCredentials(UserId user);
	
	////
	// Friends management
	////
	
	/**
	 * Get the upper bound of the number of posts in the friend's wall.
	 * @param id the id of the friend
	 * @return the upper bound.
	 */
	public int getFriendPostsCount(UserId id);
	
	/**
	 * Get the upper bound over the partial order of actual posts for the friend.
	 * @param id the id of the friend
	 * @return the upper bound.
	 */
	public int getFriendMaxPostsId(UserId id);
	
	/**
	 * Set the upper bound of the number of posts in a friend's wall
	 * @param newCount
	 * @param id
	 */
	public void setFriendPostsCount(int newCount, UserId id);
	
	/**
	 * Set the upper bound over the partial order of actual posts for a friend
	 * @param newMaxPostsId
	 * @param id
	 */
	public void setFriendMaxPostsId(int newMaxPostsId, UserId id);
	
	/**
	 * Get an user name from an user id
	 * @param id the id of the friend to retrieve the username
	 * @return A String representation of the user
	 */
	public String getFriendUsername(UserId id);
	
	/**
	 * Insert a friend.
	 * @param friend the friend object
	 */
	public void putFriend(User friend);
	
	/**
	 * Get a friend targeted by his / her id
	 * @param id
	 * @return a friend object (both wall and friends list fields are null!) 
	 * 			if exists an entry in database, null otherwise
	 */
	public User getFriend(UserId id);
	
	
	////
	//Posts management
	////
	
	/**
	 * Add a post to the database
	 * @param post
	 * @return true if succeeded, false otherwise
	 */
	public boolean putPost(Post post);
	
	/**
	 * Get multiple posts from the user's wall
	 * @param the least message id to consider
	 * @return a list of Posts matched by arguments
	 */
	public List<Post> getAllUserPostsFrom(int from);
	
	/**
	 * Get a post of a friend
	 * @param postid the id of the post to retrieve
	 * @param author the id of the author of the post
	 * @param wallOwner the wall that the post was posted on
	 * @return
	 */
	public boolean containsPost(int postid, UserId author, UserId wallOwner);
	
	/**
	 * Get all post of a friend with at least 'from' as post's number
	 * @param id the friends id
	 * @param from the least post id to consider
	 * @return a list of all matching posts (not sorted)
	 */
	public List<Post> getAllFriendPostsFrom(UserId id, int from);

	/**
	 * Get some latest posts
	 * @param id, the id of the user
	 * @param numberPosts, number of posts older posts wanted from postId
	 * @return a list of posts older than postId of the user
	 */
	public List<Post> getSomeLatestPosts(UserId id, int numberPosts, int postId);
	
	
	////
	//Walls management
	////
	
	/**
	 * Delete the user's wall
	 */
	public void deleteUserWall();
	
	/**
	 * Delete the wall of a friend.
	 * @param id the id of the friend whose wall is to delete
	 */
	public void deleteFriendWall(UserId id);

}
