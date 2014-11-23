package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
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
	 * Add User to Database.
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
	public int getUserMaxPostsId();
	
	////
	// Credentials (Keys)
	////
	
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
	public int getFriendMaxId(UserId id);
	
	/** TODO: check with @Lukas for use in Security Layer (friends of friends)
	 * Create a friendship relation between the user and a new friend.
	 * @param id the id of the friend (128 bits)
	 * @param username the user name of the added friend
	 */
	public void putFriendship(UserId id, String username);
	
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
	
	
	////
	//Posts management
	////
	
	/**
	 * Add a user post to the database
	 * @param post
	 */
	public void putUserPost(Post post);
	
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
	 * Add a friend post to the database.
	 * @param post the post to add
	 * @param id the id of the friend
	 */
	public void putFriendPost(Post post, UserId id);

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
