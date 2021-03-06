package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Friends.
 * @author alessiobaehler
 *
 */
class Friends {

	// Get the upper bound of the number of posts in the friend's wall.
	public static int getFriendPostsCount(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.COUNT};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			int result = cursor.getInt(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			return -1;
		}
	}
	

	// Get the upper bound over the partial order of actual posts for the friend.
	public static int getFriendMaxPostsId(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.MAX};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			int result = cursor.getInt(0);
			cursor.close();
			return result;
		} else {
			cursor.close();
			return -1;
		}
	}
	
	// Get an user name from an user id
	public static String getFriendUsername(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USERNAME};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			String username = cursor.getString(0);
			cursor.close();
			return username;
		} else {
			cursor.close();
			return null;
		}
	}
	
	// Get an user id from an user name (cannot ensures uniqueness)
	public static List<UserId> getFriendId(String username, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USER_ID};
		// SQL WHERE clause
		String selection = UsersEntry.USERNAME + " == ?";
		// Arguments for selection
		String[] selectionArgs = {username};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Instantiate id list.
		List<UserId> idsList = new ArrayList<UserId>();
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				idsList.add(new UserId(cursor.getString(0)));
				cursor.moveToNext();
			}
			cursor.close();
			return idsList;
		} else {
			cursor.close();
			return null;
		}
	}
		
	/**
	 * Add a friend in the List of Friends of the user
	 * @param user
	 * @param db
	 */
	public static void putFriend(User user, SQLiteDatabase db) {
		// Get data.
		UserId id = user.getId();
		String username = user.getUsername();
		byte[] encryption_key = user.getCredentials().broadcastEncryptionKey;
		byte[] authentication_key = user.getCredentials().broadcastAuthenticationKey;
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.USER_ID, Utility.toSQLiteId(id));
		values.put(UsersEntry.USERNAME, username);
		values.put(UsersEntry.IS_FRIEND, "1");
		values.put(UsersEntry.BROADCAST_ENC_KEY, encryption_key);
		values.put(UsersEntry.BROADCAST_AUTH_KEY, authentication_key);

		// Insert content.
		db.insert(UsersEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * Remove friend from the List of friends & everything associated with him/her
	 * @param id
	 * @param db
	 */
	public static void deleteFriend(UserId id, SQLiteDatabase db) {

		String selection = UsersEntry.USER_ID + " == ? AND " + UsersEntry.IS_FRIEND + " == ?";
		String[] selectionArgs = {Utility.toSQLiteId(id), "1"};

		// Delete friend data
		db.delete(UsersEntry.TABLE_NAME, selection, selectionArgs);
	}
	
	// Get a friend object with no wall nor friends list
	public static User getFriend(UserId friendid, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = null;
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ? ";
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(friendid)};
		
		// Execute query.
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Retrieve and return the result.
		if(cursor.moveToFirst()) {
			UserId id = new UserId(cursor.getString(1));
			String username = cursor.getString(2);
			int count = cursor.getInt(3);
			int max = cursor.getInt(4);
			byte[] enc_key = cursor.getBlob(6);
			byte[] auth_key = cursor.getBlob(7);
			UserCredentials credentials = new UserCredentials(id, enc_key, auth_key);
			cursor.close();
			return new User(id, username, credentials);
		} else {
			cursor.close();
			return null;
		}
	}
	
	// Update the posts count for the user with this id.
	public static void updateFriendPostsCount(int newCount, UserId id, SQLiteDatabase db) {
		// Create updated content.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.COUNT, newCount);
		
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		db.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);
	}
	
	// Update the max posts id for the user with this id.
	public static void updateFriendMaxPostsId(int newMax, UserId id, SQLiteDatabase db) {
		// Create updated content.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.MAX, newMax);
		
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(id)};
		
		db.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);
	}
}
