package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;

/**
 * This class makes the link between the user interface and the outside world.
 * It provides method to post new data on the user's wall, to ask for another user's wall
 * to connect and disconnect from the connected list on server.
 */
public interface ProtocolInterface {
	
	/**
	 * Set a delegate for calls back from the below components
	 * @param delegate
	 */
	public void setDelegate(UserDelegate delegate);
	
	/**
	 * Connect the user to the network.
	 * It has to be done before any other methods call
	 * @throws NetworkException, if network is not accessible
	 */
	public void connect();
	
	/**
	 * Disconnect the user from the network.
	 * To regular quit of the application, this method should be called
	 * @throws NetworkException, if network is not accessible
	 */
	public void disconnect();
	
	/**
	 * Post a text and optionally an image to the wall of the user with specified id
	 * @param wallOwnerId 
	 * @param text
	 * @param image
	 */
	public void post(UserId wallOwnerId, String text, Bitmap image);
	
	/**
	 * Post a post of the user on his / her own wall
	 * @param post, to post 
	 * @throws DatabaseException, if database access is impossible
	 */
	public void post(String text, Bitmap image);
	
	/**
	 * Get a wall from a distant user
	 * @param userId, the user to get the wall
	 */
	public void getUserWall(UserId userId);
	
	/**
	 * Get posts of a distant user starting at provided id
	 * @param userId, the user to get the wall
	 * @param postId, id of the oldest post wanted
	 */
	public void getUserPosts(UserId userId, int postId);
	
	/**
	 * Get latest posts of a distant user
	 * @param userId, the user to get posts
	 * @param numberPosts, number of posts wanted from the latest one
	 */
	public void getSomeUserPosts(UserId userId, int numberPosts);
	
	/**
	 * Get latest posts of a distant user
	 * @param userId, the user to get posts
	 * @param numberPosts, number of posts wanted from postId
	 * @param postId
	 */
	public void getSomeUserPosts(UserId userId, int numberPosts, int postId);
	
	/**
	 * Get the local user
	 */
	public User getUser();
	
	/**
	 * Set the local user
	 */
	public void putUser(User user);
	
	/**
	 * Get a mapping from user ids to user objects
	 * @return
	 */
	public Map<UserId, User> getUserMapping();

	/**
	 * Add a friend
	 * @param friend
	 */
	public void putFriend(User friend);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public User getFriend(UserId id);

	
}
