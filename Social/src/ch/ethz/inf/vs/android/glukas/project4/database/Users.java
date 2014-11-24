package ch.ethz.inf.vs.android.glukas.project4.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
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
		int count = user.getPostsCount();
		int max = user.getMaxId();
		byte[] encryption_key = user.getCredentials().broadcastEncryptionKey;
		byte[] authentication_key = user.getCredentials().broadcastAuthenticationKey;
		
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.USER_ID, Utility.toSQLiteId(id));
		values.put(UsersEntry.USERNAME, username);
		values.put(UsersEntry.COUNT, count);
		values.put(UsersEntry.MAX, max);
		values.put(UsersEntry.BROADCAST_ENC_KEY, encryption_key);
		values.put(UsersEntry.BROADCAST_AUTH_KEY, authentication_key);

		
		// Insert content.
		db.insert(UsersEntry.TABLE_NAME, null, values);
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserPostsCount(SQLiteDatabase db) {
		return Friends.getFriendPostsCount(Utility.userID, db);
	}
	
	// Get the upper bound of the number of posts in the user's wall.
	public static int getUserMaxPostsId(SQLiteDatabase db) {
		return Friends.getFriendMaxPostsId(Utility.userID, db);
	}
	
}
