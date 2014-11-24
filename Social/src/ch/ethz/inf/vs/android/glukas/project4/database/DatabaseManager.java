package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
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

public class DatabaseManager extends SQLiteOpenHelper implements DatabaseDelegate{

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
			+ FriendsEntry.USER_ID + " " + Utility.BLOB_TYPE + ", "
			// Index 1: friend_id
			+ FriendsEntry.FRIEND_ID + " " + Utility.BLOB_TYPE
			// Foreign keys references
			+ Utility.FOREIGN_KEY + "(" + FriendsEntry.USER_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry.USER_ID + ")"
//				TODO + " " + Utility.ON_DELETE + " " + Utility.
			+ Utility.FOREIGN_KEY + "(" + FriendsEntry.FRIEND_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry.USER_ID + ")"
				+ " " + Utility.ON_DELETE + " " + Utility.CASCADE
			+ ");";
	
	/**
	 * String containing SQL code to create table users.
	 * TODO: add update/delete options, integrity checks and remaining columns
	 */
	private static final String SQL_CREATE_USERS = Utility.CREATE_TABLE + " " + UsersEntry.TABLE_NAME + " (" 
			// Index 0: _id
			+ UsersEntry._ID + " " + Utility.INTEGER_TYPE + " AUTOINCREMENT, "
			// Index 1: user_id
			+ UsersEntry.USER_ID + " " + Utility.BLOB_TYPE + " " + Utility.PRIMARY_KEY + ", "
			// Index 2: username
			+ UsersEntry.USERNAME + " " + Utility.TEXT_TYPE + ");" + ", "
			// Index 3: count
			+ UsersEntry.COUNT + " " + Utility.INTEGER_TYPE + ", "
			// Index 4: max
			+ UsersEntry.MAX + " " + Utility.INTEGER_TYPE + ", "
			// Index 5: broadcast_enc_key
			+ UsersEntry.BROADCAST_ENC_KEY + " " + Utility.BLOB_TYPE + ", "
			// Index 6: broadcast_auth_key
			+ UsersEntry.BROADCAST_AUTH_KEY + " " + Utility.BLOB_TYPE + ")";
	
	/**
	 * String containing SQL code to create table posts.
	 * TODO: add update/delete options and integrity checks
	 */
	private static final String SQL_CREATE_POSTS = Utility.CREATE_TABLE + " " + PostsEntry.TABLE_NAME + " (" 
			// Index 0: _id
			+ PostsEntry._ID + " " + Utility.INTEGER_TYPE
			// Index 1: poster_id
			+ PostsEntry.POSTER_ID + " " + Utility.BLOB_TYPE + ", "
			// Index 2: wall_id
			+ PostsEntry.WALL_ID + " " + Utility.BLOB_TYPE + ", "
			// Index 3: datetime
			+ PostsEntry.DATE_TIME + " " + Utility.TEXT_TYPE + ", "
			// Index 4: text
			+ PostsEntry.TEXT + " " + Utility.TEXT_TYPE + ", "
			// Index 5: image
			+ PostsEntry.IMAGE + " " + Utility.BLOB_TYPE + ", "
			// Primary key
			+ Utility.PRIMARY_KEY + " (" + PostsEntry._ID + ", " + PostsEntry.WALL_ID + "),"
			// Foreign key references
			+ Utility.FOREIGN_KEY + "(" + PostsEntry.POSTER_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry.USER_ID + ") " 
				 + Utility.ON_DELETE + " " + Utility.SET_NULL + ", "
			+ Utility.FOREIGN_KEY + "(" + PostsEntry.WALL_ID + ")" + " " + Utility.REFERENCES + " " + UsersEntry.TABLE_NAME + "(" + UsersEntry.USER_ID + ") "
				+ Utility.ON_DELETE + " " + Utility.CASCADE
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
	
	/**
	 * Credentials (Keys)
	 */

	@Override
	public UserCredentials getUserCredentials(UserId user) {
		// TODO (Alessio) implement
		return null;
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
	
	// FIXME: Create a friendship relation between the user and a new friend.
	@Override
	public void putFriendship(UserId id, String username) {
		Friends.putFriendship(id, username, this.getWritableDatabase());
	}
	
	// Get an user name from an user id
	@Override
	public String getFriendUsername(UserId id) {
		return Friends.getFriendUsername(id, this.getReadableDatabase());
	}
	
	// Get an user id from an user name (cannot ensures uniqueness)
	@Override
	public List<UserId> getFriendId(String username) {
		return Friends.getFriendId(username, this.getReadableDatabase());
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
	
	
	/**
	 * POSTS MANAGEMENT
	 */
	// Update the wall of the user with the given post.
	@Override
	public void putUserPost(Post post) {
		Posts.putUserPost(post, this.getWritableDatabase());
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

	// Update the wall of a friend whose wall is saved on our phone
	@Override
	public void putFriendPost(Post post, UserId friendid) {
		Posts.putFriendPost(post, friendid, this.getWritableDatabase());
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
	public Wall getFriendWall(UserId friendid) {
		return Walls.getFriendWall(friendid, this.getReadableDatabase());
	}
	
	// Delete the whole saved Wall of a certain friend
	@Override
	public void deleteFriendWall(UserId friendid) {
		Walls.deleteFriendWall(friendid, this.getWritableDatabase());
	}

}
