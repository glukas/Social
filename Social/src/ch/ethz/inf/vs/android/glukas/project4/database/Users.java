package ch.ethz.inf.vs.android.glukas.project4.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Users.
 * @author alessiobaehler
 *
 */
class Users {

	// TODO: Add User to Database (first usage of the app?): yes!
	public static void putUser(User user, SQLiteDatabase db) {
		// Get data.
		String username = user.getUsername();
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(UsersEntry.USERNAME, username);
		
		// Insert content.
		db.insert(UsersEntry.TABLE_NAME, null, values);
	}
	
	// TODO: Get the upper bound of the number of posts in the user's wall.
	public static int getUserPostsCount(SQLiteDatabase db) {
		return 0;
	}
	
	// TODO: Get the upper bound of the number of posts in the user's wall.
	public static int getUserMaxPostsId(SQLiteDatabase db) {
		return 0;
	}
	
}
