package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
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
 * TODO: check performance of getWrite/ReadableDatabase() and if too slow use AsyncTask to execuTe them.
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public abstract class DatabaseManager extends SQLiteOpenHelper implements DatabaseDelegate{

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
			// Index 4: datetime
			+ PostsEntry.DATE_TIME + " " + Utility.TEXT_TYPE 
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

	/**
	 * USER MANAGEMENT
	 * With user is meant the owner of the app.~
	 */
	// Add User to Database
	@Override
	public void putUser(User user) {
		Users.putUser(user, this.getWritableDatabase());
	}

	// TODO: Get the upper bound of the number of posts in the user's wall.
	@Override
	public int getUserPostsCount() {
//		return Users
		return 0;
	}
	
	// TODO: Get the upper bound of the number of posts in the user's wall.
	@Override
	public int getUserMaxPostsId() {
		// return Users
		return 0;
	}
	
	
	/**
	 * FRIENDS MANAGEMENT
	 */
	// TODO: Get the upper bound of the number of posts in the friend's wall.
	@Override
	public int getFriendPostsCount(UserId id) {
		return 0;
	}
	
	// TODO: Get the upper bound over the partial order of actual posts for the friend.
	@Override
	public int getFriendMaxId(UserId id) {
		return 0;
	}
	
	// TODO: Create a friendship relation between the user and a new friend.
	public void putFriendship(UserId id, String username) {
		
	}
	
	// TODO: Get an user name from an user id
	@Override
	public String getFriendUsername(UserId id) {
		return null;
	}
	
	// TODO: Get an user id from an user name (cannot ensures uniqueness)
	public List<UserId> getFriendId(String username) {
		return null;
	}
	
	// Add a friend in the List of Friends of the user
	@Override
	public void putFriend(User friend) {
		Friends.putFriend(friend, this.getWritableDatabase());
	}
	
	// TODO: Remove friend from the List of friends & everything associated with him/her
	@Override
	public void deleteFriend(UserId id) {
//		Friends.deleteFriend(id, this.getWritableDatabase());
	}
	
	
	/**
	 * POSTS MANAGEMENT
	 */
	// Update the wall of the user with the given post.
	@Override
	public void putUserPost(Post post) {
		Posts.putUserPost(post, this.getWritableDatabase());
	}
	
	// TODO: Get all the Posts in a Wall starting from id -> id or time?
	@Override
	public List<Post> getAllUserPostsFrom(int from) {
//	return Posts.getAllUserPostsFrom(timestamp, this.getReadableDatabase());
		return null;
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

	// Update the wall of a friend whose wall is saved on our phone
	@Override
	public void putFriendPost(Post post, int friendid) {
		Posts.putFriendPost(post, friendid, this.getWritableDatabase());
	}

	// Get a certain Post from a certain friend
	@Override
	public Post getFriendPost(int postid, int friendid) {
		return Posts.getFriendPost(postid, friendid, this.getReadableDatabase());
	}

	// Get all Posts of a certain friend starting at a certain time/timestamp
	@Override
	public List<Post> getAllFriendPostsFrom(int friendid, int postid) {
//		return Posts.getAllFriendPostsfrom(timestamp, friendid, this.getReadableDatabase());
		return null;
	}

	// delete a certain Post of a certain friend
	@Override
	public void deleteFriendPost(int postid, int friendid) {
		Posts.deleteFriendPost(postid, friendid, this.getWritableDatabase());
	}
	
	/**
	 * WALLS MANAGEMENT
	 */
	
	// Get the whole wall of the user.
	@Override
	public Wall getUserWall() {
		return Walls.getUserWall(this.getReadableDatabase());
	}
	
	// TODO: Delete user's wall.
	@Override
	public void deleteUserWall() {
		
	}
	
	// Get the whole Wall of a certain friend
	@Override
	public Wall getFriendWall(int friendid) {
		return Walls.getFriendWall(friendid, this.getReadableDatabase());
	}
	
	// Delete the whole saved Wall of a certain friend
	@Override
	public void deleteFriendWall(int friendid) {
		Walls.deleteFriendWall(friendid, this.getWritableDatabase());
	}
}
