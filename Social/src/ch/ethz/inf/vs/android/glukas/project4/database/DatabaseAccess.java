package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;

/**
 * Abstract list of all functionalities offered by the database.
 * Please refer to this and add new functions you need.
 * Please consider all methods with a TODO as still having a non 
 * (complete) meaningful declaration.
 * All methods are performed atomically. (TODO : is this true?)
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
	 * Delete a friend recursively. This means any data related to him
	 * (data, friendships, posts) will be removed from the database.
	 * @param id the id of the friend to delete.
	 */
	public void deleteFriend(UserId id);
	
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
	 */
	public void putPost(Post post);
	
	/**
	 * Get multiple posts from the user's wall
	 * @param the least message id to consider
	 * @return a list of Posts matched by arguments
	 */
	public List<Post> getAllUserPostsFrom(int from);
	
	/**
	 * Delete a user post.
	 * @param id the id of the post to delete 
	 */
	public void deleteUserPost(int id);
	
	/**
	 * Get a user post.
	 * @param id the id of the post to retrieve
	 * @return the requested post
	 */
	public Post getUserPost(int id);
	
	/**
	 * Get a post of a friend
	 * @param postid the id of the post to retrieve
	 * @param friendid the friend who owns the post
	 * @return
	 */
	public Post getFriendPost(int postid, UserId friendid);
	
	/**
	 * Get all post of a friend with at least 'from' as post's number
	 * @param id the friends id
	 * @param from the least post id to consider
	 * @return a list of all matching posts (not sorted)
	 */
	public List<Post> getAllFriendPostsFrom(UserId id, int from);
	
	/**
	 * Delete a friend's post.
	 * @param postid the id of the post to delete
	 * @param friendid the id of the friend
	 */
	public void deleteFriendPost(int postid, UserId friendid);
	
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
	 * Get the user's wall
	 * @return wall of user
	 */
	public Wall getUserWall();
	
	/**
	 * Delete the user's wall
	 */
	public void deleteUserWall();
	
	/**
	 * Get the wall of a friend.
	 * @param id the friend's id
	 * @return the friend's wall object
	 */
	public Wall getFriendWall(UserId id);
	
	/**
	 * Delete the wall of a friend.
	 * @param id the id of the friend whose wall is to delete
	 */
	public void deleteFriendWall(UserId id);

}
