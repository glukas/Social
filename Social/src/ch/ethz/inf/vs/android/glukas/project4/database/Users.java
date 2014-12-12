package ch.ethz.inf.vs.android.glukas.project4.database;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
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
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.USER_ID, Utility.toSQLiteId(id));
		values.put(UsersEntry.USERNAME, username);
		values.put(UsersEntry.IS_FRIEND, "0");
		values.put(UsersEntry.BROADCAST_ENC_KEY, encryption_key);
		values.put(UsersEntry.BROADCAST_AUTH_KEY, authentication_key);

		// Insert content.
		db.insert(UsersEntry.TABLE_NAME, null, values);
	}
	
	
	
	// Get the user object from the database.
	// Friends only have id!!
	public static User getUser(SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USER_ID, UsersEntry.USERNAME, UsersEntry.COUNT, UsersEntry.MAX, UsersEntry.BROADCAST_ENC_KEY, UsersEntry.BROADCAST_AUTH_KEY};
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {"0"};
		
		// Execute query.
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		if(cursor.moveToFirst()) {
			UserId id = new UserId(cursor.getString(0));
			// Set static id if not already done
			if(Utility.userId.getId().equals("-1"))
				Utility.userId = id;
			String username = cursor.getString(1);
			int count = cursor.getInt(2);
			int max = cursor.getInt(3);
			byte[] enc_key = cursor.getBlob(4);
			byte[] auth_key = cursor.getBlob(5);
			UserCredentials credentials = new UserCredentials(id, enc_key, auth_key);
			cursor.close();
			return new User(id, username, credentials);
		} else {
			cursor.close();
			return null;
		}
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserPostsCount(SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.COUNT};
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection
		String[] selectionArgs = {"0"};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			int count = cursor.getInt(0);
			cursor.close();
			return count;
		} else {
			cursor.close();
			return -1;
		}
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserMaxPostsId(SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.MAX};
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection
		String[] selectionArgs = {"0"};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			cursor.close();
			return cursor.getInt(0);
		} else {
			cursor.close();
			return -1;
		}
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
		// Create updated content.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.COUNT, newCount);
		
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {"0"};
		
		db.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);	
	}
	
	public static void updateUserMaxPostsId(int newMax, SQLiteDatabase db) {
		// Create updated content.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.MAX, newMax);
		
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {"0"};
		
		db.update(UsersEntry.TABLE_NAME, values, selection, selectionArgs);
	}
	
	// Get the user's list of friends (with only ids)
	public static List<User> getUserFriends(SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USER_ID, UsersEntry.USERNAME, UsersEntry.COUNT, UsersEntry.MAX, UsersEntry.BROADCAST_ENC_KEY, UsersEntry.BROADCAST_AUTH_KEY};
		// SQL WHERE clause
		String selection = UsersEntry.IS_FRIEND + " == ?";
		// Arguments for selection.
		String[] selectionArgs = {"1"};
		
		// Execute query.
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		List<User> friends = new ArrayList<User>();
		
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				UserId id = new UserId(cursor.getString(0));
				String username = cursor.getString(1);
				int count = cursor.getInt(2);
				int max = cursor.getInt(3);
				byte[] enc_key = cursor.getBlob(4);
				byte[] auth_key = cursor.getBlob(5);
				UserCredentials credentials = new UserCredentials(id, enc_key, auth_key);
				friends.add(new User(id, username, credentials)); // TODO: posts count?
			}
			cursor.close();
			return friends;
		} else {
			cursor.close();
			return null;
		}
	}
}
