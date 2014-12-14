package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.R;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;

/**
 * Provides the interface with the database.
 * TODO: check performance of getWrite/ReadableDatabase() and if too slow use AsyncTask to execuTe them.
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public class DatabaseManager extends SQLiteOpenHelper implements DatabaseAccess{

	private static final String TAG = "----DATABASE----";

	// DB Metadata
	private static final String DATABASE_NAME = "SocialDB";
	private static final int DATABASE_VERSION = 9;

	// CREATION
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) throws DatabaseException {
		// TODO: set limit constants
		// Create tables.
		try {
			for (DatabaseContract.CREATE_TABLE ct : DatabaseContract.CREATE_TABLE.values()) {
				db.execSQL(ct.getCommand());
			}
		} catch (SQLException e) {
			Log.e(TAG, "Failed creating tables");
			throw new DatabaseException();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("DATABASE TESTING", "###onUpgrade");
		for(DatabaseContract.DROP_TABLE_IF_EXIST dt : DatabaseContract.DROP_TABLE_IF_EXIST.values()) {
			db.execSQL(dt.getCommand());
		}
	    onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	/**
	 * USER MANAGEMENT
	 * With user is meant the owner of the app.~
	 */
	// Add User to Database
	@Override
	public void putUser(User user) {
		Users.putUser(user, this.getWritableDatabase());
	}

	@Override
	// Get the user object from the database.
	public User getUser() {
		return Users.getUser(this.getReadableDatabase());
	}

	// Get the upper bound of the number of posts in the user's wall.
	@Override
	public int getUserPostsCount() {
		return Users.getUserPostsCount(this.getReadableDatabase());
	}

	// Get the upper bound of the number of posts in the user's wall.
	@Override
	public int getUserMaxPostsId() {
		return Users.getUserMaxPostsId(this.getReadableDatabase());
	}

	// Change the user's posts count.
	@Override
	public void setUserPostsCount(int newCount) {
		Users.updateUserPostCount(newCount, this.getWritableDatabase());
	}

	// Change the user's max posts id.
	@Override
	public void setUserMaxPostsId(int newMaxPostsId) {
		Users.updateUserMaxPostsId(newMaxPostsId, this.getWritableDatabase());
	}

	// Get the user's list of friends.
	@Override
	public List<User> getUserFriendsList() {
		return Users.getUserFriends(this.getReadableDatabase());
	}

	/**
	 * Credentials (Keys)
	 */

	// Get the credentials of the given user.
	// ATTENTION: this method will be modified in the future!!
	@Override
	public UserCredentials getUserCredentials(UserId id) {
		return Users.getUserCredentials(id, this.getReadableDatabase());
	}

	/**
	 * FRIENDS MANAGEMENT
	 */
	// Get the upper bound of the number of posts in the friend's wall.
	@Override
	public int getFriendPostsCount(UserId id) {
		return Friends.getFriendPostsCount(id, this.getReadableDatabase());
	}

	// Get the upper bound over the partial order of actual posts for the friend.
	@Override
	public int getFriendMaxPostsId(UserId id) {
		return Friends.getFriendMaxPostsId(id, this.getReadableDatabase());
	}

	// Get an user name from an user id
	@Override
	public String getFriendUsername(UserId id) {
		return Friends.getFriendUsername(id, this.getReadableDatabase());
	}

	// Add a friend in the List of Friends of the user
	@Override
	public void putFriend(User friend) {
		Friends.putFriend(friend, this.getWritableDatabase());
	}

	// Remove friend from the List of friends & everything associated with him/her
	@Override
	public void deleteFriend(UserId id) {
		Friends.deleteFriend(id, this.getWritableDatabase());
	}

	// Update the posts count for the user with this id.
	@Override
	public void setFriendPostsCount(int newCount, UserId id) {
		Friends.updateFriendPostsCount(newCount, id, this.getWritableDatabase());
	}

	//  Update the max posts id for the user with this id.
	@Override
	public void setFriendMaxPostsId(int newMaxPostsId, UserId id) {
		Friends.updateFriendMaxPostsId(newMaxPostsId, id, this.getWritableDatabase());
	}

	// Get the friend user
	@Override
	public User getFriend(UserId id) {
		return Friends.getFriend(id, this.getReadableDatabase());
	}

	/**
	 * POSTS MANAGEMENT
	 */
	// Update the wall of the user with the given post.
	@Override
	public void putPost(Post post) {
		Posts.putPost(post, this.getWritableDatabase());
	}

	// Get all the Posts in a Wall starting from id -> id or time?
	@Override
	public List<Post> getAllUserPostsFrom(int from) {
		return Posts.getAllUserPostsFrom(from, this.getReadableDatabase());
	}

	// Delete a certain post from the user's wall.
	@Override
	public void deleteUserPost(int postid) {
		Posts.deleteUserPost(postid, this.getWritableDatabase());
	}

	// Get a certain post from the user's wall.
	@Override
	public Post getUserPost(int postid) {
		return Posts.getUserPost(postid, this.getReadableDatabase());
	}

	// Get a certain Post from a certain friend
	@Override
	public Post getFriendPost(int postid, UserId friendid) {
		return Posts.getFriendPost(postid, friendid, this.getReadableDatabase());
	}

	// Get all Posts of a certain friend starting at a certain time/timestamp
	@Override
	public List<Post> getAllFriendPostsFrom(UserId friendid, int from) {
		return Posts.getAllFriendPostsFrom(friendid, from, this.getReadableDatabase());
	}

	// delete a certain Post of a certain friend
	@Override
	public void deleteFriendPost(int postid, UserId friendid) {
		Posts.deleteFriendPost(postid, friendid, this.getWritableDatabase());
	}

	// Get numberPosts older than postId
	@Override
	public List<Post> getSomeLatestPosts(UserId id, int numberPosts, int postId) {
		return Posts.getSomeLatestPosts(id, numberPosts, postId, this.getReadableDatabase());
	}


	/**
	 * WALLS MANAGEMENT
	 */

	// Get the whole wall of the user.
	@Override
	public Wall getUserWall() {
		return Walls.getUserWall(this.getReadableDatabase());
	}

	// Delete user's wall.
	@Override
	public void deleteUserWall() {
		Walls.deleteUserWall(this.getWritableDatabase());
	}

	// Get the whole Wall of a certain friend
	@Override
	public Wall getFriendWall(UserId friendid) {
		return Walls.getFriendWall(friendid, this.getReadableDatabase());
	}

	// Delete the whole saved Wall of a certain friend
	@Override
	public void deleteFriendWall(UserId friendid) {
		Walls.deleteFriendWall(friendid, this.getWritableDatabase());
	}

	/**
	 *  TESTING
	 */

	// Inserts static test data in the DB
	public void initializeTest(Context context) {
		User user = new User("Alice");
		this.putUser(user);
		Post post = new Post(1, user.getId(), user.getId(), "Hello World!", null, new Date());
		this.putPost(post);
		post = new Post(2, user.getId(), user.getId(), "Amazing app!!", null, new Date());
		this.putPost(post);
		Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		icon = Bitmap.createScaledBitmap(icon, 500, 500, false);
		post = new Post(3, user.getId(), user.getId(), "Testing image.. and now with a much longer text to see how it breaks onto the next line and stuff..", icon, null);
		this.putPost(post);
	}
	
	// Empty all the tables
	public void resetDB() {
		String selection = null;
		
		String[] selectionArgs = null;
		
		this.getReadableDatabase().delete(UsersEntry.TABLE_NAME, selection, selectionArgs);
		this.getReadableDatabase().delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
	
	// Delete any previously inserted user
	public void deleteUsers() {
		String selection = UsersEntry.IS_FRIEND + " == ?";
		
		String[] selectionArgs = {"0"};
		
		this.getReadableDatabase().delete(UsersEntry.TABLE_NAME, selection, selectionArgs);
	}

	// return a list of all user in the DB
	public List<BasicUser> getAllUser() {

		String[] projection = {};
		
		String selection = null;
		
		String[] selectionArgs = {};
		
		String orderBy = UsersEntry.USERNAME + " DESC";
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
		
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				// TODO: implement checks for basic users
				
			}
		}
		
		return null;
	}

	// return a list of all post in the DB
	public List<Post> getAllPost() {
		return null;
	}
}
