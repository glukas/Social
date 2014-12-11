package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.FriendsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Users.
 * @author alessiobaehler
 *
 */
class Users {

	// Add User to Database (first usage of the app).
	public static void putUser(User user, SQLiteDatabase db) {
		// Get data.
		UserId id = user.getId();
		String username = user.getUsername();
		byte[] encryption_key = user.getCredentials().broadcastEncryptionKey;
		byte[] authentication_key = user.getCredentials().broadcastAuthenticationKey;
		
		// Set userId
		Utility.userID = id;
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.USER_ID, Utility.toSQLiteId(id));
		values.put(UsersEntry.USERNAME, username);
		values.put(UsersEntry.BROADCAST_ENC_KEY, encryption_key);
		values.put(UsersEntry.BROADCAST_AUTH_KEY, authentication_key);

		// Insert content.
		db.insert(UsersEntry.TABLE_NAME, null, values);
	}
	
	
	
	// Get the user object from the database.
	// Friends only have id!!
	public static User getUser(SQLiteDatabase db) {
		return Friends.getFriend(Utility.userID, db);
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserPostsCount(SQLiteDatabase db) {
		return Friends.getFriendPostsCount(Utility.userID, db);
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserMaxPostsId(SQLiteDatabase db) {
		return Friends.getFriendMaxPostsId(Utility.userID, db);
	}
	
	// Get the credentials of the given user (for any user!!)
	public static UserCredentials getUserCredentials(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.BROADCAST_ENC_KEY, UsersEntry.BROADCAST_AUTH_KEY};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		// Execute query.
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Retrieve and return result.
		if(cursor.moveToFirst()) {
			byte[] enc_key = cursor.getBlob(0);
			byte[] auth_key = cursor.getBlob(1);
			// Close cursor
			cursor.close();
			return new UserCredentials(id, enc_key, auth_key);
		} else {
			cursor.close();
			return null;
		}	
	}
	
	public static void updateUserPostCount(int newCount, SQLiteDatabase db) {
		Friends.updateFriendPostsCount(newCount, Utility.userID, db);
	}
	
	public static void updateUserMaxPostsId(int newMax, SQLiteDatabase db) {
		Friends.updateFriendMaxPostsId(newMax, Utility.userID, db);
	}
	
	// TODO: join tables to get username
	// Get the user's list of friends (with only ids)
	public static List<User> getUserFriends(SQLiteDatabase db) {
		//
		String[] projection = {FriendsEntry.FRIEND_ID};
		//
		String selection = FriendsEntry.USER_ID + " == ?";
		//
		String[] selectionArgs = {Utility.toSQLiteId(Utility.userID)};
		
		// Execute query.
		Cursor cursor = db.query(FriendsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		List<User> friends = new ArrayList<User>();
		
//		if(cursor.moveToFirst()) {
//			while(!cursor.isAfterLast()) {
//				friends.add(new Friend(new UserId(cursor.getString(0))));
//				cursor.moveToNext();
//			}
//			cursor.close();
//			return friends;
//		} else {
//			cursor.close();
			return null;
//		}
	}
}
