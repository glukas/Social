package ch.ethz.inf.vs.android.glukas.project4.database;

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
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public abstract class DatabaseManager extends SQLiteOpenHelper {

	// DB Metadata
	private static final String DATABASE_NAME = "SocialDB";
	private static final int DATABASE_VERSION = 1;
	
	// DDL
	// TODO: add update/delete options and references to other tables
	private static final String SQL_CREATE_FRIENDS = 
			Utility.CREATE_TABLE + " " + FriendsEntry.TABLE_NAME + " (" + 
			FriendsEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, " +
			FriendsEntry.USER_ID + " " + Utility.INTEGER_TYPE + ", " +
			FriendsEntry.FRIEND_ID + " " + Utility.INTEGER_TYPE + ")";
	private static final String SQL_CREATE_USERS =
			Utility.CREATE_TABLE + " " + UsersEntry.TABLE_NAME + " (" + 
			UsersEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, " +
			UsersEntry.NAME + " " + Utility.TEXT_TYPE + ", " +
			UsersEntry.WALL_ID + " " + Utility.INTEGER_TYPE + ", " +
			UsersEntry.PRIVATE_KEY + " " + Utility.BLOB_TYPE + ", " +
			UsersEntry.PUBLIC_KEY + " " + Utility.BLOB_TYPE + ")"; // TODO: add remaining columns
	private static final String SQL_CREATE_POSTS =
			Utility.CREATE_TABLE + " " + PostsEntry.TABLE_NAME + " (" + 
			PostsEntry._ID + " " + Utility.INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT, " +
			PostsEntry.WALL_ID + " " + Utility.INTEGER_TYPE + ", " +
			PostsEntry.TEXT + " " + Utility.TEXT_TYPE + ", " +
			PostsEntry.IMAGE + " " + Utility.BLOB_TYPE + ")";
	
	// Helper classes
	private Users mUsersHelper = new Users();
	private Friends mFriendsHelper = new Friends();
	private Posts mPostsHelper = new Posts();
	
	// CREATION
	public DatabaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create tables.
		db.execSQL(SQL_CREATE_USERS);
		db.execSQL(SQL_CREATE_FRIENDS);
		db.execSQL(SQL_CREATE_POSTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: decide if needed and define upgrade policy in case it is
	}
	
	// USER MANAGEMENT
	public abstract void putUser(User user);
	
	public abstract User getUser(int id);
	
//	update single fields necessary?
	
	public abstract void deleteUser(int id);
	
	// WALLS MANAGEMENT
	
	public abstract void putWall(Wall wall);
	
	public abstract User getWall(int id);
	
	public abstract void deleteWall(int id);
	
	// FRIENDS MANAGEMENT
	
	public abstract void putFriend(User user);
	
	public abstract User getFriend(int id);
	
	public abstract void deleteFriend(int id);
	
	// POSTS MANAGEMENT
	
	public abstract void putPost(Post post);
	
	public abstract User getPost(int id);
	
	public abstract void deletePost(int id);
	
	

}
