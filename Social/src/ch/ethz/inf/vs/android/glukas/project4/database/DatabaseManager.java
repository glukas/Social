package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.FriendsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.*;
import android.util.Log;

/**
 * Provides the interface with the database.
 * 
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public abstract class DatabaseManager extends SQLiteOpenHelper {

	private static final String TAG = "----DATABASE----";
	// DB Metadata
	private static final String DATABASE_NAME = "SocialDB";
	private static final int DATABASE_VERSION = 1;

	// DDL
	/**
	 * String containing SQL code to create table friends.
	 * TODO: add update/delete options and integrity checks
	 */
	private static final String SQL_CREATE_FRIENDS = Utility.CREATE_TABLE + " " + FriendsEntry.TABLE_NAME + " (" 
			+ FriendsEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, "
			// Index 0: user_id 
			+ FriendsEntry.USER_ID + " " + Utility.INTEGER_TYPE + ", "
			// Index 1: friend_id
			+ FriendsEntry.FRIEND_ID + " " + Utility.INTEGER_TYPE 
			+ Utility.FOREIGN_KEY + "(" + FriendsEntry.USER_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry._ID + ")" 
			+ Utility.FOREIGN_KEY + "(" + FriendsEntry.FRIEND_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry._ID + ")" 
			+ ");";
	
	/**
	 * String containing SQL code to create table users.
	 * TODO: add update/delete options, integrity checks and remaining columns
	 */
	private static final String SQL_CREATE_USERS = Utility.CREATE_TABLE + " " + UsersEntry.TABLE_NAME + " (" 
			// Index 0: _id
			+ UsersEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY, "
			// Index 1: name
			+ UsersEntry.NAME + " " + Utility.TEXT_TYPE + ");"; //", "
//			+ UsersEntry.PRIVATE_KEY + " " + Utility.BLOB_TYPE + ", "
//			+ UsersEntry.PUBLIC_KEY + " " + Utility.BLOB_TYPE + ")";
	
	/**
	 * String containing SQL code to create table posts.
	 * TODO: add update/delete options and integrity checks
	 */
	private static final String SQL_CREATE_POSTS = Utility.CREATE_TABLE + " " + PostsEntry.TABLE_NAME + " (" 
			// Index 0: _id
			+ PostsEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY, "
			// INdex 1: wall_id
			+ PostsEntry.WALL_ID + " " + Utility.INTEGER_TYPE + ", "
			// Index 2: text
			+ PostsEntry.TEXT + " " + Utility.TEXT_TYPE + ", "
			// Index 3: image
			+ PostsEntry.IMAGE + " " + Utility.BLOB_TYPE + ", "
//			+ PostsEntry.DATE_TIME + " " + Utility.TEXT_TYPE 
			// INdex 4: timestamp
			+PostsEntry.TIMESTAMP + " " + Utility.INTEGER_TYPE
			+ Utility.FOREIGN_KEY + "(" + PostsEntry.WALL_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry._ID + ")" 
			+ ");";

	// CREATION
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) throws DatabaseException {
		// TODO: set limit constants
		// Create tables.
		try {
			db.execSQL(SQL_CREATE_USERS);
			db.execSQL(SQL_CREATE_FRIENDS);
			db.execSQL(SQL_CREATE_POSTS);
		} catch (SQLException e) {
			Log.e(TAG, "Failed creating tables");
			throw new DatabaseException();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: decide if needed and define upgrade policy in case it is
	}

	/** USER MANAGEMENT
	 * With user is meant the owner of the app.~
	 */
	// Add User to Database (first usage of the app? yes!)
	public void putUser(User user) {
		Users.putUser(user, this.getWritableDatabase());
	}

	// Get the whole wall of the user.
	public Wall getUserWall() {
		return Walls.getUserWall(this.getReadableDatabase());
	}

	// Update the wall of the user with the given post.
	public void putUserPost(Post post) {
		Posts.putUserPost(post, this.getWritableDatabase());
	}

	// Get a certain post from the user's wall.
	public Post getUserPost(int postid) {
		return Posts.getUserPost(postid, this.getReadableDatabase());
	}

	// Get all the Posts in a Wall starting from id -> id or time?
	public List<Post> getAllUserPostsFrom(int timestamp) {
		return Posts.getAllUserPostsFrom(timestamp, this.getReadableDatabase());
	}

	// Delete a certain post from the user's wall.
	public void deleteUserPost(int id) {
		Posts.deleteUserPost(id, this.getWritableDatabase());
	}
	
	// TODO: check performance of getWrite/ReadableDatabase() and if too slow use AsyncTask to execue them.
	/** FRIENDS MANAGEMENT
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
		Friends.putFriend(friend, this.getWritableDatabase());
	}

	// Remove friend from the List of friends & everything associated with
	// him/her
	public void deleteFriend(int friendid) {
		Friends.deleteFriend(friendid, this.getWritableDatabase());
	}
	
	// Update the wall of a friend whose wall is saved on our phone
	public void putFriendPost(Post post, int friendid) {
		Posts.putFriendPost(post, friendid, this.getWritableDatabase());
	}

	// Get the whole Wall of a certain friend
	public Wall getFriendWall(int friendid) {
		return Walls.getFriendWall(friendid, this.getReadableDatabase());
	}

	// Get a certain Post from a certain friend
	public Post getFriendPost(int postid, int friendid) {
		return Posts.getFriendPost(postid, friendid, this.getReadableDatabase());
	}

	// Get all Posts of a certain friend starting at a certain time/timestamp
	public List<Post> getAllFriendPostsfrom(Date timestamp, int friendid) {
		return Posts.getAllFriendPostsfrom(timestamp, friendid, this.getReadableDatabase());
	}

	// delete a certain Post of a certain friend
	public void deleteFriendPost(int postid, int friendid) {
		Posts.deleteFriendPost(postid, friendid, this.getWritableDatabase());
	}
	
	// delete the whole saved Wall of a certain friend
	public void deleteFriendWall(int friendid) {
		Walls.deleteFriendWall(friendid, this.getWritableDatabase());
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
