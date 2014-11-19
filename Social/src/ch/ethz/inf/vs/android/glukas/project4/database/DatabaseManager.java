package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.FriendsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;
import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Provides the interface with the database.
 * 
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public abstract class DatabaseManager extends SQLiteOpenHelper {

	// DB Metadata
	private static final String DATABASE_NAME = "SocialDB";
	private static final int DATABASE_VERSION = 1;

	// DDL
	// TODO: add update/delete options and references to other tables
	private static final String SQL_CREATE_FRIENDS = Utility.CREATE_TABLE + " "
			+ FriendsEntry.TABLE_NAME + " (" + FriendsEntry._ID + " "
			+ Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, "
			+ FriendsEntry.USER_ID + " " + Utility.INTEGER_TYPE + ", "
			+ FriendsEntry.FRIEND_ID + " " + Utility.INTEGER_TYPE + ")";
	private static final String SQL_CREATE_USERS = Utility.CREATE_TABLE + " "
			+ UsersEntry.TABLE_NAME + " (" + UsersEntry._ID + " "
			+ Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, "
			+ UsersEntry.NAME + " " + Utility.TEXT_TYPE + ", "
			+ UsersEntry.WALL_ID + " " + Utility.INTEGER_TYPE + ", "
			+ UsersEntry.PRIVATE_KEY + " " + Utility.BLOB_TYPE + ", "
			+ UsersEntry.PUBLIC_KEY + " " + Utility.BLOB_TYPE + ")"; // TODO:
																		// add
																		// remaining
																		// columns
	private static final String SQL_CREATE_POSTS = Utility.CREATE_TABLE + " "
			+ PostsEntry.TABLE_NAME + " (" + PostsEntry._ID + " "
			+ Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, "
			+ PostsEntry.WALL_ID + " " + Utility.INTEGER_TYPE + ", "
			+ PostsEntry.TEXT + " " + Utility.TEXT_TYPE + ", "
			+ PostsEntry.IMAGE + " " + Utility.BLOB_TYPE + ")";

	// Helper classes
	private Users mUsersHelper = new Users();
	private Friends mFriendsHelper = new Friends();
	private Posts mPostsHelper = new Posts();
	private Walls mWallsHelper = new Walls();

	// CREATION
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO: set limit constants
		// Create tables.
		db.execSQL(SQL_CREATE_USERS);
		db.execSQL(SQL_CREATE_FRIENDS);
		db.execSQL(SQL_CREATE_POSTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: decide if needed and define upgrade policy in case it is
	}

	
	/** USER MANAGEMENT
	 * With user is meant the owner of the app.~
	 * @param user
	 */
	// Add User to Database (first usage of the app?): yes!
	public void putUser(User user) {
		mUsersHelper.putUser(user);
	}

	// Get the whole wall of the user.
	public Wall getUserWall() {
		return mWallsHelper.getUserWall();
	}

	// Update the wall of the user with the given post.
	public void putUserPost(Post post) {
		mPostsHelper.putUserPost(post);
	}

	// Get a certain post from the user's wall.
	public Post getUserPost(int id) {
		return mPostsHelper.getUserPost(id);
	}

	// Get all the Posts in a Wall starting from id -> id or time?
	public List<Post> getAllUserPostsFrom(int timestamp) {
		return mPostsHelper.getAllUserPostsFrom(timestamp);
	}

	// Delete a certain post from the user's wall.
	public void deleteUserPost(int id) {
		mPostsHelper.deleteUserPost(id);
	}
	
	
	/** FRIENDS MANAGEMENT
	 * 
	 * @param user
	 */	
	/**
	 * If we are going to store the wall of friends in our database we probably
	 * need the methods below If we don't make a special case for the user
	 * himself we could also use these methods instead of
	 * 
	 * @author youngban
	 */
	/**
	 * I thought a little about it, and i concluded is safer to distinguish 
	 * between the user and a friend by using separate methods.
	 * 
	 * @author alessiobaehler
	 */
	// Add a friend in the List of Friends of the user
	public void putFriend(User friend) {
		mFriendsHelper.putFriend(friend);
	}

	// Remove friend from the List of friends & everything associated with
	// him/her
	public void deleteFriend(int friendid) {
		mFriendsHelper.deleteFriend(friendid);
	}
	
	// Update the wall of a friend whose wall is saved on our phone
	public void putFriendPost(Post post, int friendid) {
		mPostsHelper.putFriendPost(post, friendid);
	}

	// Get the whole Wall of a certain friend
	public Wall getFriendWall(int friendid) {
		return mWallsHelper.getFriendWall(friendid);
	}

	// Get a certain Post from a certain friend
	public Post getFriendPost(int postid, int friendid) {
		return mPostsHelper.getFriendPost(postid, friendid);
	}

	// Get all Posts of a certain friend starting at a certain time/timestamp
	public List<Post> getAllFriendPostsfrom(Date timestamp, int friendid) {
		return mPostsHelper.getAllFriendPostsfrom(timestamp, friendid);
	}

	// delete a certain Post of a certain friend
	public void deleteFriendPost(int postid, int friendid) {
		mPostsHelper.deleteFriendPost(postid, friendid);
	}
	
	// delete the whole saved Wall of a certain friend
	public void deleteFriendWall(int friendid) {
		mWallsHelper.deleteFriendWall(friendid);
	}
	
	
	///
	// Probably not needed
	///
	// public abstract User getUser(int id);

	// public abstract void deleteUser(int id);

	// public abstract void putWall(Wall wall);

	// public abstract void deleteWall(int id);

	// public abstract User getFriend(int id);



}
